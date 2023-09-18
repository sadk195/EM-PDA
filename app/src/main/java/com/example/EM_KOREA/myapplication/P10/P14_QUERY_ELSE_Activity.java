package com.example.EM_KOREA.myapplication.P10;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.MySession;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class P14_QUERY_ELSE_Activity extends AppCompatActivity {

    public String sJson ="";
    public String sJsonConfig ="";
    public EditText work_fr_dt;
    public EditText work_to_dt;
    private int mYear, mMonth, mDay;
    private MySession global;
    public String sMenuName;
    public String sMenuRemark;
    public String sJOBCODE;
    public ListView listOrder;
    public Spinner cmb_work_center;
    public String sJsonCombo="";
    public String sAA = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p14_query_ekse);
        listOrder = findViewById(R.id.listOrder);

        global = (MySession)getApplication();

        //sMenuName = getIntent().getStringExtra("MENU_NM");
        //sMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        //sJOBCODE = getIntent().getStringExtra("JOB_CD");

        //작업장 리스트 콤보 값을 초기화 한다.
        Intent intent = new Intent(this.getIntent());
        String PRODT_ORDER_NO  = intent.getStringExtra("PRODT_ORDER_NO");
        DBQuery_GetProdOprNo(PRODT_ORDER_NO);

        String MENU_REMARK = intent.getStringExtra("MENU_REMARK");

        sMenuRemark = MENU_REMARK;

        InitData();
        Start();
    }

    public  void InitData()
    {
        if (!sJsonConfig.equals(""))
        {
            try {
                JSONArray ja = new JSONArray(sJsonConfig);

                if(ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);
                    //String vConfigValue = jObject.getString("CONFIG_VALUE");
                }
            } catch (JSONException ex) {
                TGSClass.AlterMessage(this,ex.getMessage());
            }
            catch (Exception e1)
            {
                TGSClass.AlterMessage(this,e1.getMessage());
            }
        }
    }

    public  void Start()
    {


        if (!sJson.equals(""))
        {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                final ArrayList<P14_QUERY> lstItem = new ArrayList<>();

                ListView listview =  findViewById(R.id.listOrder) ;
                P14_QUERY_ListViewAdapter listViewAdapter_OPRNO = new P14_QUERY_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    String vProdtNO = jObject.getString("PRODT_ORDER_NO");   //제조오더
                    String vOPR_NO = jObject.getString("OPR_NO");            //공순
                    String vItemCD = jObject.getString("ITEM_CD");           //품목이름


                    listViewAdapter_OPRNO.addProdOprNoItem(vProdtNO,vOPR_NO,vItemCD);
                }

                listview.setAdapter(listViewAdapter_OPRNO) ;

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        P14_QUERY vItem = (P14_QUERY) parent.getItemAtPosition(position) ;

                        //Toast.makeText(P_OEM11_Activity.this ,vSelectItem,Toast.LENGTH_LONG).show();

                        Intent intent = TGSClass.ChangeView(getPackageName(), P14_SAVE2_Activity.class);
                        intent.putExtra("PRODT_ORDER_NO",vItem.getPRODT_ORDER_NO());  // PRODUCTION 객체를 파라메터로 다음페이지로 넘김.
                        intent.putExtra("OPR_NO",vItem.getOPR_NO());
                        intent.putExtra("ITEM_CD", vItem.getITEM_CD());
                        //intent.putExtra("ITEM_NM", vItem.getITEM_NM());
                        //intent.putExtra("PRODT_ORDER_QTY", vItem.getPRODT_ORDER_QTY());
                        startActivity(intent);
                    }
                }) ;
            } catch (JSONException ex) {
                TGSClass.AlterMessage(this,ex.getMessage());
            }
            catch (Exception e1)
            {
                TGSClass.AlterMessage(this,e1.getMessage());
            }
        }
    }

    public void DBQuery_GetProductOrderList(final String pDate,final String pGroupCD) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " exec XUSP_AND_P_OEM11_KO773_GETLIST ";
                sql += "  @PLAN_START_DT = '" + pDate + "'";
                sql += " ,@PLANT_CD = '" + global.getPlantCDString() + "'";
                sql += " ,@ITEM_GROUP_CD = '" + pGroupCD + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

            }


        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }


    public void OpenPopupDate(View view)
    {
        if (view == work_fr_dt)
        {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,  new DatePickerDialog.OnDateSetListener()
            {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    String vYear = String.format("%04d", year);
                    String vMonth = String.format("%02d", monthOfYear + 1);
                    String vDay = String.format("%02d", dayOfMonth);

                    work_fr_dt.setText(vYear + "-" + vMonth + "-" + vDay);

                }
             }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }

    public void OpenPopupDate2(View view)
    {
        if (view == work_to_dt)
        {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,  new DatePickerDialog.OnDateSetListener()
            {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    String vYear2 = String.format("%04d", year);
                    String vMonth2 = String.format("%02d", monthOfYear + 1);
                    String vDay2 = String.format("%02d", dayOfMonth);

                    work_to_dt.setText(vYear2 + "-" + vMonth2 + "-" + vDay2);

                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    public void DBQuery_GetProdOprNo(final String pRODT_ORDER_NO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_P_APK_COMBO_OPRNO_GET ";
                sql += "  @PRODT_ORDER_NO = '" + pRODT_ORDER_NO + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

            }


        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }

        Start();
    }


    public void DBQuery(final String pfrDate, final String ptoDate, final String pWorkCenter) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_APK_PRODNO_GET ";
                sql += "  @frDt = '" + pfrDate + "'";
                sql += " ,@toDt = '" + ptoDate + "'";
                sql += " ,@WorkCenter = '" + pWorkCenter + "'";


                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);



                sJson = dba.SendHttpMessage("GetSQLData", pParms);

            }


        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }


}


