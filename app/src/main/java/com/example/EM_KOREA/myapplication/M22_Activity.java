package com.example.EM_KOREA.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class M22_Activity extends AppCompatActivity {

    public String sJson ="";
    public String sJsonConfig ="";
    public String sJsonCombo ="";
    private int mYear, mMonth, mDay;
    private MySession global;
    public String sJOB_CD;
    public String sMenuRemark;
    public ListView listOrder;
    public String INSPECT_REQ_NO;

    public TextView insp_req_no;
    public EditText selectSaveDate;
    public EditText selectStartDate;
    public TextView item_cd;
    public TextView item_nm;
    public TextView STS;
    public EditText INSPECTOR_NM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m22);
        listOrder = findViewById(R.id.listOrder);

        insp_req_no = findViewById(R.id.insp_req_no);
        item_cd = findViewById(R.id.item_cd);
        item_nm = findViewById(R.id.item_nm);
        STS = findViewById(R.id.STS);
        INSPECTOR_NM = findViewById(R.id.INSPECTOR_NM);

        Intent intent = new Intent(this.getIntent());
        INSPECT_REQ_NO = intent.getStringExtra("INSP_REQ_NO");
        String MENU_REMARK = intent.getStringExtra("MENU_REMARK");
        sJOB_CD = getIntent().getStringExtra("JOB_CD");
        sMenuRemark = MENU_REMARK;

        // 숫자에 대한 자바 포멧
        // 00은 두자리로표현
        DecimalFormat decimalFormat = new DecimalFormat("00");// decimalformat은
        DecimalFormat NumFormat = new DecimalFormat("0000");// 4자리로 표현 한다.

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        String StartvYear = String.format("%04d", mYear);
        String StartvMonth = String.format("%02d", mMonth + 1);
        String StartvDay = String.format("%02d", mDay);


        String SavevYear = String.format("%04d", mYear);
        String SavevMonth = String.format("%02d", mMonth + 1);
        String SavevDay = String.format("%02d", mDay);


        selectStartDate = findViewById(R.id.start_dt);

        selectSaveDate = findViewById(R.id.save_dt);
        selectSaveDate.setText(SavevYear + "-" + SavevMonth  + "-" + SavevDay);
        selectSaveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenPopupDate(v);
            }
        });

        if (!INSPECT_REQ_NO.equals(""))
        {
            DBQuery_Get_INSP_REQ_NO_INFO(INSPECT_REQ_NO);
        }

        selectSaveDate.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0)
                { //do your work here }
                    //Start();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,int after)
            {

            }
            public void afterTextChanged(Editable s) {
                Start();
            }
        });

        InitData();



    }

    public void OpenPopupDate(View view)
    {

        if (view == selectSaveDate)
        {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,  new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    String vYear = String.format("%04d", year);
                    String vMonth = String.format("%02d", monthOfYear + 1);
                    String vDay = String.format("%02d", dayOfMonth);

                    selectSaveDate.setText(vYear + "-" + vMonth + "-" + vDay);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }


    public  void InitData()
    {
        if (!sJsonConfig.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJsonConfig);

                if(ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);
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

        if (!sJson.equals("")) {

            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                final ArrayList<M22_ARRAYLIST> lstItem = new ArrayList<>();

                ListView listview = findViewById(R.id.listOrder);
                M22_ListViewAdapter listViewAdapter = new M22_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    insp_req_no.setText( jObject.getString("INSPECT_REQ_NO")); //검사요청번호
                    selectStartDate.setText( jObject.getString("PROD_DT")); //기간
                    item_cd.setText( jObject.getString("ITEM_CD")); //품번
                    item_nm.setText( jObject.getString("ITEM_NM")); //품명
                    STS.setText( jObject.getString("STS_NM")); //진행상태
                    INSPECTOR_NM.setText(jObject.getString("WK_NM")); //검사원 명

                    String pINSP_QTY = jObject.getString("INSP_QTY");
                    String pINSPECT_GOOD_QTY = jObject.getString("G_QTY");
                    String pINSPECT_BAD_QTY = jObject.getString("B_QTY");
                    String pPRODT_ORDER_NO = jObject.getString("PRODT_ORDER_NO");

                    listViewAdapter.addItem(pINSP_QTY, pINSPECT_GOOD_QTY, pINSPECT_BAD_QTY, pPRODT_ORDER_NO);
                }

                listview.setAdapter(listViewAdapter);
            }

            catch (JSONException ex) {
                TGSClass.AlterMessage(this,ex.getMessage());
            }
            catch (Exception e1)
            {
                TGSClass.AlterMessage(this,e1.getMessage());
            }
        }
    }


    public void DBQuery_Get_INSP_REQ_NO_INFO(final String pINSP_REQ_NO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession)getApplication();

                String vParmID = getIntent().getStringExtra("pID");              //등록된 장비명이 로그인 아이디로 판단.
                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");
                String vDevice = getIntent().getStringExtra("pDEVICE");

                if(vParmID == null) vParmID = global.getLoginString();
                if(vPlant_CD == null) vPlant_CD = global.getPlantCDString();
                if(vDevice == null) vDevice = global.getmUnitCDString();

                String vITEM_CD = "";
                String vSTS = "";
                String vWK_ID = "";
                String vCHK = "Y";
                String vPROD_WK_ID = "";
                String vPROD_ITEM_CD = "";

                String sql = " EXEC XUSP_APK_QM21_GET_LIST ";
                sql += "  @PLANT_CD = '" + vPlant_CD + "',";
                sql += "  @PRODT_ORDER_NO = '" + pINSP_REQ_NO + "',";
                sql += "  @ITEM_CD = '" + vITEM_CD + "',";
                sql += "  @STS = '" + vSTS + "',";
                sql += "  @CHK = '" + vCHK + "',";
                sql += "  @PROD_WK_ID = '" + vPROD_WK_ID + "',";
                sql += "  @PROD_ITEM_CD= '" + vPROD_ITEM_CD + "',";
                sql += "  @WK_ID = '" + vWK_ID + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

                Start();

            }


        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }

    }


}


