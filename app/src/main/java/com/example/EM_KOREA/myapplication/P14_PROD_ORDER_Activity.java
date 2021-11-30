package com.example.EM_KOREA.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class P14_PROD_ORDER_Activity extends AppCompatActivity {

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
        setContentView(R.layout.activity_p14_order);

        listOrder = findViewById(R.id.listOrder);
        cmb_work_center = findViewById(R.id.cmb_work_center);

        global = (MySession)getApplication();

        sMenuName = getIntent().getStringExtra("MENU_NM");
        sMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        sJOBCODE = getIntent().getStringExtra("JOB_CD");



        DecimalFormat decimalFormat = new DecimalFormat("00");// decimalformat은
        // 숫자에 대한 자바 포멧
        // 00은 두자리로표현
        DecimalFormat NumFormat = new DecimalFormat("0000");// 4자리로 표현 한다.

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        String vYear = String.format("%04d", mYear);
        String vMonth = String.format("%02d", mMonth + 1);
        String vDay = String.format("%02d", mDay);

        work_fr_dt = findViewById(R.id.work_fr_dt);
        work_fr_dt.setText(vYear + "-" + vMonth  + "-" + vDay);
        work_fr_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenPopupDate(v);
            }
        });

        Calendar d = Calendar.getInstance();
        mYear = d.get(Calendar.YEAR);
        mMonth = d.get(Calendar.MONTH);
        mDay = d.get(Calendar.DAY_OF_MONTH);

        String vYear2 = String.format("%04d", mYear);
        String vMonth2 = String.format("%02d", mMonth + 1);
        String vDay2 = String.format("%02d", mDay);

        work_to_dt = findViewById(R.id.work_to_dt);
        work_to_dt.setText(vYear2 + "-" + vMonth2  + "-" + vDay2);
        work_to_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenPopupDate2(v);
            }
        });

        work_fr_dt.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0)
                { //do your work here }
                    Start();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
                Start();
            }

        });


        work_to_dt.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0)
                { //do your work here }
                    Start();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
                Start();
            }

        });

        cmb_work_center.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Start();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        InitData();
        //Start();


    }

    public  void InitData()
    {
        //작업장 리스트 콤보 값을 초기화 한다.
        DBQuery_GetComboData();


        if (!sJsonConfig.equals("")) {

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
        EditText work_fr_dt =  findViewById(R.id.work_fr_dt);
        EditText work_to_dt =  findViewById(R.id.work_to_dt);

        String Work_Center = sAA.toString();

        DBQuery(work_fr_dt.getText().toString(),work_to_dt.getText().toString(),Work_Center);

        if (!sJson.equals("")) {

            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                final ArrayList<P14_PROD_ORDER_PRODUCTION_SEARCH> lstItem = new ArrayList<>();

                ListView listview =  findViewById(R.id.listOrder) ;
                P14_ORDER_ListViewAdapter listViewAdapter = new P14_ORDER_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    String vProdtNO = jObject.getString("PRODT_ORDER_NO");   //제조오더
                    String vOPR_NO = jObject.getString("OPR_NO");            //공순
                    String vItemNM = jObject.getString("ITEM_NM");           //품목이름
                    String vItemCD = jObject.getString("ITEM_CD");           //품목이름
                    String vPRODT_ORDER_QTY = jObject.getString("PRODT_ORDER_QTY");  //오더수량

                    listViewAdapter.addProdItem(vProdtNO,vOPR_NO,vItemCD,vItemNM,vPRODT_ORDER_QTY);
                }

                listview.setAdapter(listViewAdapter) ;

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        P14_PROD_ORDER_PRODUCTION_SEARCH vItem = (P14_PROD_ORDER_PRODUCTION_SEARCH) parent.getItemAtPosition(position) ;

                        //Toast.makeText(P_OEM11_Activity.this ,vSelectItem,Toast.LENGTH_LONG).show();

                        Intent intent = TGSClass.ChangeView(getPackageName(), P14_SAVE2_Activity.class.getSimpleName());
                        intent.putExtra("PRODT_ORDER_NO",vItem.getPRODT_ORDER_NO());  // PRODUCTION 객체를 파라메터로 다음페이지로 넘김.
                        intent.putExtra("OPR_NO",vItem.getOPR_NO());
                        intent.putExtra("ITEM_CD", vItem.getITEM_CD());
                        intent.putExtra("ITEM_NM", vItem.getITEM_NM());
                        intent.putExtra("PRODT_ORDER_QTY", vItem.getPRODT_ORDER_QTY());
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

    public void DBQuery_GetComboData() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " exec XUSP_P_OEM_COMBO_BASIC_GET @FLAG='APK_WORK_CENTER'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);


                sJsonCombo = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(),ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<P14_PROD_ORDER_WORK_CENTER> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            P14_PROD_ORDER_WORK_CENTER itemBase = new P14_PROD_ORDER_WORK_CENTER();

                          for(int i=0; i< ja.length();i++){
                    JSONObject jObject = ja.getJSONObject(i);

                    //final String vJOB_CD = jObject.getString("JOB_CD");
                    //final String vREF_CD = jObject.getString("REF_CD");
                    final String vWC_CD = jObject.getString("WC_CD");
                    final String vWC_NM = jObject.getString("WC_NM");

                P14_PROD_ORDER_WORK_CENTER item = new P14_PROD_ORDER_WORK_CENTER();
                //item.setCODE(vREF_CD);
                item.setWC_CD(vWC_CD);
                item.setWC_NM(vWC_NM);

                lstItem.add(item);
            }

            ArrayAdapter<P14_PROD_ORDER_WORK_CENTER> adapter = new ArrayAdapter<P14_PROD_ORDER_WORK_CENTER>(this, android.R.layout.simple_dropdown_item_1line, lstItem);
            cmb_work_center.setAdapter(adapter);

            //로딩시 기본값 세팅
            cmb_work_center.setSelection(adapter.getPosition(itemBase));

            //콤보박스 클릭 이벤트 정의
            cmb_work_center.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //아이템 클릭 이벤트 정의
                    sAA =  ((P14_PROD_ORDER_WORK_CENTER)parent.getSelectedItem()).getWC_CD();
                    Start();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
        }
        catch (JSONException ex) {
            TGSClass.AlterMessage(getApplicationContext(),ex.getMessage());
        }
        catch (Exception ex){
            TGSClass.AlterMessage(getApplicationContext(),ex.getMessage());
        }

        //Start();
        //InitData();

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


