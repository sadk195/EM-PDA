package com.example.EM_KOREA.myapplication.P10;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.MySession;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapPrimitive;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class P14_SAVE2_Activity extends AppCompatActivity {


    Button bt;

    public String sJson = "";
    public String sJsonConfig = "";
    public String sJsonConfigSet = "";
    public String sJsonCombo = "";
    public EditText selectDate;
    private int mYear, mMonth, mDay;
    private MySession global;
    public String sJOB_CD;
    public String sFACILTY_NM;
    public String sWORKER_NM;

    public TextView lbl_ProdtOrderNo;
    public TextView lbl_opr_no;
    public TextView lbl_item_cd;
    public TextView lbl_item_name;
    public TextView lbl_ORD_QTY;
    public TextView lbl_REMAIN_QTY;
    public TextView lbl_TRACKING_NO;
    public TextView lbl_FACILITY_CD;
    //public TextView lbl_FACILITY_NM;
    public Spinner cmb_FACILITY_NM;
    public TextView lbl_WORKER_CD;
    public Spinner cmb_WORKER_NM;
    public TextView lbl_ROUT_ORDER;
    public TextView lbl_PRODT_ORDER_UNIT;
    public TextView lbl_BASE_UNIT;
    public TextView lbl_SPEC;
    public TextView lbl_RCPT_ORDER_QTY;
    public TextView lbl_RCPT_BASE_QTY;
    public TextView lbl_PROD_QTY_IN_ORDER_UNIT;
    public TextView lbl_GOOD_QTY;
    public TextView lbl_BAD_QTY;
    public TextView lbl_SL_CD;
    public TextView lbl_SL_NM;
    public TextView lbl_WC_CD;
    public TextView lbl_WC_NM;
    public TextView lbl_WC_MGR;


    public EditText start_day;
    public EditText start_time;
    public EditText end_day;
    public EditText end_time;
    public TextView lbl_WORK_QTY;
    SoapPrimitive resultString;

    public String Sql;

    Calendar myCalendar = Calendar.getInstance();

    /*public class MainActivity extends AppCompatActivity {*/

    //Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update_start_dt();

        }
    };


    DatePickerDialog.OnDateSetListener myDatePicker1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update_end_dt();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p14_save);

        bt = (Button) findViewById(R.id.btn_save);

        lbl_ProdtOrderNo = findViewById(R.id.lbl_ProdtOrderNo);
        lbl_opr_no = findViewById(R.id.lbl_opr_no);
        lbl_item_cd = findViewById(R.id.lbl_item_cd);
        lbl_item_name = findViewById(R.id.lbl_item_name);
        lbl_ORD_QTY = findViewById(R.id.lbl_ORD_QTY);
        lbl_REMAIN_QTY = findViewById(R.id.lbl_REMAIN_QTY);
        lbl_TRACKING_NO = findViewById(R.id.lbl_TRACKING_NO);
        lbl_FACILITY_CD = findViewById(R.id.lbl_FACILITY_CD);
        cmb_FACILITY_NM = findViewById(R.id.cmb_FACILITY_NM);
        lbl_WORKER_CD = findViewById(R.id.lbl_WORKER_CD);
        cmb_WORKER_NM = findViewById(R.id.cmb_WORKER_NM);
        start_day = findViewById(R.id.start_day);
        start_time = findViewById(R.id.start_time);
        end_day = findViewById(R.id.end_day);
        end_time = findViewById(R.id.end_time);
        lbl_WORK_QTY = findViewById(R.id.lbl_WORK_QTY);
        lbl_ROUT_ORDER = findViewById(R.id.lbl_ROUT_ORDER);
        lbl_PRODT_ORDER_UNIT = findViewById(R.id.lbl_PRODT_ORDER_UNIT);
        lbl_BASE_UNIT = findViewById(R.id.lbl_BASE_UNIT);
        lbl_SPEC = findViewById(R.id.lbl_SPEC);
        lbl_RCPT_ORDER_QTY = findViewById(R.id.lbl_RCPT_ORDER_QTY);
        lbl_RCPT_BASE_QTY = findViewById(R.id.lbl_RCPT_BASE_QTY);
        lbl_PROD_QTY_IN_ORDER_UNIT = findViewById(R.id.lbl_PROD_QTY_IN_ORDER_UNIT);
        lbl_GOOD_QTY = findViewById(R.id.lbl_GOOD_QTY);
        lbl_BAD_QTY = findViewById(R.id.lbl_BAD_QTY);
        lbl_SL_CD = findViewById(R.id.lbl_SL_CD);
        lbl_SL_NM = findViewById(R.id.lbl_SL_NM);
        lbl_WC_CD = findViewById(R.id.lbl_WC_CD);
        lbl_WC_NM = findViewById(R.id.lbl_WC_NM);
        lbl_WC_MGR = findViewById(R.id.lbl_WC_MGR);

        sJOB_CD = getIntent().getStringExtra("JOB_CD");
        global = (MySession) getApplication();

        //PRODUCTION_SEARCH ppp = (PRODUCTION_SEARCH) getIntent().getSerializableExtra("PRODT_ORDER_NO");

        Intent intent = new Intent(this.getIntent());
        String PRODT_ORDER_NO = intent.getStringExtra("PRODT_ORDER_NO");
        String OPR_NO = intent.getStringExtra("OPR_NO");
        String ITEM_CD = intent.getStringExtra("ITEM_CD");
        //String ITEM_NM  = intent.getStringExtra("ITEM_NM");
        //String PRODT_ORDER_QTY  = intent.getStringExtra("PRODT_ORDER_QTY");


        DBQuery_GET_PRODT_INFO(PRODT_ORDER_NO, OPR_NO);

        final EditText start_day = (EditText) findViewById(R.id.start_day);
        SimpleDateFormat current_dt = new SimpleDateFormat("yyyy-MM-dd");
        start_day.setText(current_dt.format(new Date()));
        start_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(P14_SAVE2_Activity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        EditText end_day = (EditText) findViewById(R.id.end_day);
        end_day.setText(current_dt.format(new Date()));
        end_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(P14_SAVE2_Activity.this, myDatePicker1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText start_time = (EditText) findViewById(R.id.start_time);
        SimpleDateFormat current_time = new SimpleDateFormat("HH:mm");
        long now = System.currentTimeMillis();
        start_time.setText(current_time.format(new Time(now)));
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(P14_SAVE2_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        // EditText에 출력할 형식 지정
                        start_time.setText(selectedHour + "시" + selectedMinute + "분");
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        final EditText end_time = (EditText) findViewById(R.id.end_time);
        end_time.setText(current_time.format(new Time(now)));
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(P14_SAVE2_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // EditText에 출력할 형식 지정
                        end_time.setText(selectedHour + "시 " + selectedMinute + "분");
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        /*
        // EMK 에서는 작업지시서를 스캔해서 생산실적 등록을 하지만, 작업지시서를 분실했을 때를 대비하여 미리 만들어 놓은 작업지시서 화면입니다.
        // 해당 작업지시서 선택 화면은 업체에서 요청했을 경우 배포하는 것으로 결정났으므로 2020-12-11 현재는 주석처리하였습니다.
        lbl_ProdtOrderNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TGSClass.ChangeView(getPackageName(), P11_PROD_ORDER_Activity.class.getSimpleName());
                startActivity(intent);
            }
        });

         */
/*
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vPRODT_ORDER_NO = lbl_ProdtOrderNo.getText().toString();
                String vOPR_NO = lbl_opr_no.getText().toString();
                //String vITEM_CD = lbl_item_cd.getText().toString();
                //String vPRODT_ORDER_QTY = lbl_ORD_QTY.getText().toString();
                //String vFACILITY_CD = lbl_FACILITY_CD.getText().toString();
                String vWORK_CD = lbl_WORKER_CD.getText().toString();
                String vWORK_QTY = lbl_WORK_QTY.getText().toString();
                String vREPORT_DT = start_day.getText().toString() + " " + start_time.getText().toString();

                DBQuery_Testing(vPRODT_ORDER_NO, vOPR_NO,vWORK_CD, vWORK_QTY, vREPORT_DT);

                try {
                    //get_bl(Sql);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
        InitData();

 */
    }

    public void DBQuery_Testing(final String vPRODT_ORDER_NO,final String vOPR_NO,final String vWORK_CD,final String vWORK_QTY,final String vREPORT_DT) {
        Thread aa = new Thread() {
            public void run() {
                String sql = "EXEC XUSP_TPC_P2002MA1_GET_BL";

                sql += " @CUD_FLAG = '" + "C" + "'";
                sql += ",@PLANT_CD = '" + "@PLANT_CD" + "'";
                sql += ",@PRODT_ORDER_NO = '" + vPRODT_ORDER_NO + "'";
                sql += ",@OPR_NO='" + vOPR_NO + "'";
                sql += ",@REPORT_TYPE = '" + "G" + "'";
                sql += ",@SEQ = '" + "1" + "'";
                sql += ",@QTY='" + vWORK_QTY + "'";
                sql += ",@WK_ID='" + vWORK_CD + "'";
                sql += ",@B_TYPE_CD ='" + "" + "'";
                sql += ",@QTY_IN='" + vWORK_QTY + "'";
                sql += ",@REPORT_DT='" + vREPORT_DT + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                Sql = dba.SendHttpMessage("GetSQLData", pParms);

            }
        };
        aa.start();
        try {
            aa.join();
        }
        catch (InterruptedException ex)
        {

        }
    }
        private void update_start_dt() {
            String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            EditText et_date = (EditText) findViewById(R.id.start_day);
            et_date.setText(sdf.format(myCalendar.getTime()));
        }

        private void update_end_dt() {
            String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            EditText end_day = (EditText) findViewById(R.id.end_day);
            end_day.setText(sdf.format(myCalendar.getTime()));
        }

    public  void InitData()
    {
        //설비 Spinner 값을 초기화 한다.
        DBQuery_GetComboData();
        //작업자 Spinner 값을 초기화 한다.
        DBQuery_GetWoker();
        //설정 값을 초기화 한다.
        DBQuery_GetConfig();

        if (!sJsonConfig.equals("")) {

            try {

                JSONArray ja = new JSONArray(sJsonConfig);


                if(ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);

                    String vConfigValue = jObject.getString("CONFIG_VALUE");

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
                final ArrayList<P14_SAVE_INFO> lstItem = new ArrayList<>();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);


                    lbl_ProdtOrderNo.setText( jObject.getString("PRODT_ORDER_NO")); //제조오더
                    lbl_opr_no.setText( jObject.getString("OPR_NO")); //공순
                    lbl_ROUT_ORDER.setText( jObject.getString("ROUT_ORDER")); //공정구분
                    lbl_item_cd.setText( jObject.getString("ITEM_CD")); //품번
                    lbl_item_name.setText( jObject.getString("ITEM_NM")); //품명
                    lbl_PRODT_ORDER_UNIT.setText( jObject.getString("PRODT_ORDER_UNIT")); //단위
                    lbl_BASE_UNIT.setText( jObject.getString("BASE_UNIT")); //단위
                    lbl_SPEC.setText( jObject.getString("SPEC")); //규격
                    lbl_TRACKING_NO.setText( jObject.getString("TRACKING_NO")); //TRACKING_NO
                    lbl_ORD_QTY.setText( jObject.getString("PRODT_ORDER_QTY")); //지시량
                    lbl_RCPT_ORDER_QTY.setText( jObject.getString("RCPT_ORDER_QTY")); //오더단위 입고량
                    lbl_RCPT_BASE_QTY.setText( jObject.getString("RCPT_BASE_QTY"));  //기준단위 입고량
                    lbl_PROD_QTY_IN_ORDER_UNIT.setText( jObject.getString("PROD_QTY_IN_ORDER_UNIT"));  //생산량
                    lbl_GOOD_QTY.setText( jObject.getString("GOOD_QTY"));  //양품생산량
                    lbl_BAD_QTY.setText( jObject.getString("BAD_QTY"));  //불량생산량
                    lbl_REMAIN_QTY.setText( jObject.getString("REMAIN_QTY"));  //잔량
                    lbl_SL_CD.setText( jObject.getString("SL_CD"));  //출고창고
                    lbl_SL_NM.setText( jObject.getString("SL_NM"));  //출고창고명
                    lbl_WC_CD.setText( jObject.getString("WC_CD"));  //작업장코드
                    lbl_WC_NM.setText( jObject.getString("WC_NM"));  //작업장명
                    lbl_WC_MGR.setText( jObject.getString("WC_MGR"));  //작업장그룹

                    //listViewAdapter.addItem(vPRODUCTION_ORDER_NO,vItemCD,vItemNM,vGroupCD,vGroupNM,vPRODUCTION_NO,vDate);

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

    public void DBQuery(final String pDate,final String pGroupCD) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_AND_P_RESULT_GETLIST_KO773 ";
                sql += "  @DT = '" + pDate + "'";
                sql += " ,@ITEM_GROUP_CD = '" + pGroupCD + "'";
                sql += " ,@JOB_CD= '" + sJOB_CD + "'";


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

    public void DBQuery_GetConfig()
    {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread2 = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_AND_P_UNIT_CONFIG ";
                sql += "  @FLAG = 'GET'";
                sql += " ,@PLANT_CD = '" + global.getPlantCDString() + "'";
                sql += " ,@CONFIG_ID = 'ITEM_GROUP'";
                sql += " ,@UNIT_CD = '" + global.getmUnitCDString() + "'";


                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);


                sJsonConfig = dba.SendHttpMessage("GetSQLData", pParms);

            }


        };
        workingThread2.start();   //스레드 시작
        try
        {
            workingThread2.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
        }
        catch (InterruptedException ex)
        {

        }


    }

    public void DBSave_SetConfig(final String pUnitCD,final String pConfigID,final String pConfigValue,final  String pRemark)
    {
        Thread workingThread3 = new Thread() {
            public void run() {


                String vUnitCD = global.getmUnitCDString();
                String vUserID = global.getLoginString();

                String sql = "DECLARE  @RTN_MSG NVARCHAR(200)=''";
                sql += "EXEC XUSP_AND_P_UNIT_CONFIG  ";
                sql += "  @FLAG = 'SET'";
                sql += " ,@PLANT_CD = '" + global.getPlantCDString() + "'";
                sql += " ,@CONFIG_ID = '" + pConfigID + "'";
                sql += " ,@CONFIG_VALUE = '" + pConfigValue + "'";
                sql += " ,@UNIT_CD = '" + pUnitCD + "'";
                sql += " ,@REMARK = '" + pRemark + "'";
                sql += " ,@RTN_MSG =  @RTN_MSG  OUTPUT";
                sql += " SELECT  @RTN_MSG AS RTN_MSG ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);


                sJsonConfigSet = dba.SendHttpMessage("SetSQLSave", pParms);
                String vMSG = "";
                String vStatus = "";

                try {
                    JSONArray ja = new JSONArray(sJsonConfigSet);

                    if(ja.length() > 0) {
                        JSONObject jObject = ja.getJSONObject(0);

                        vMSG = !jObject.isNull("RTN_MSG") ? jObject.getString("RTN_MSG") : "";
                        vStatus = !jObject.isNull("STATUS") ? jObject.getString("STATUS") : "";
                    }

                    if(!vStatus.equals("OK"))
                    {
                        TGSClass.AlterMessage(P14_SAVE2_Activity.this,vMSG);
                        return;
                    }
                }
                catch (JSONException ex)
                {
                    TGSClass.AlterMessage(P14_SAVE2_Activity.this,ex.toString());
                    return;
                }

            }

        };

        workingThread3.start();   //스레드 시작
        try {
            workingThread3.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }

    }

    public void OpenPopupDate(View view)
    {
        if (view == selectDate)
        {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,  new DatePickerDialog.OnDateSetListener()
            {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    String vYear = String.format("%04d", year);
                    String vMonth = String.format("%02d", monthOfYear + 1);
                    String vDay = String.format("%02d", dayOfMonth);

                    selectDate.setText(vYear + "-" + vMonth + "-" + vDay);

                }
             }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    public void DBQuery_GetComboData() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " exec XUSP_P_OEM_COMBO_BASIC_GET @FLAG='APK_FACILTY'";

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

            final ArrayList<P14_SAVE_RESOURCE> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            P14_SAVE_RESOURCE itemBase = new P14_SAVE_RESOURCE();

            for(int i=0; i< ja.length();i++){
                JSONObject jObject = ja.getJSONObject(i);

                final String vFACILTY_CD = jObject.getString("FACILTY_CD");
                final String vFACILTY_NM = jObject.getString("FACILTY_NM");

                P14_SAVE_RESOURCE item = new P14_SAVE_RESOURCE();
                item.setFACILTY_CD(vFACILTY_CD);
                item.setFACILTY_NM(vFACILTY_NM);

                lstItem.add(item);
            }

            ArrayAdapter<P14_SAVE_RESOURCE> adapter = new ArrayAdapter<P14_SAVE_RESOURCE>(this, android.R.layout.simple_dropdown_item_1line, lstItem);
            cmb_FACILITY_NM.setAdapter(adapter);

            //로딩시 기본값 세팅
            cmb_FACILITY_NM.setSelection(adapter.getPosition(itemBase));

            //콤보박스 클릭 이벤트 정의
            cmb_FACILITY_NM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //아이템 클릭 이벤트 정의
                    lbl_FACILITY_CD.setText(((P14_SAVE_RESOURCE)parent.getSelectedItem()).getFACILTY_CD());
                    sFACILTY_NM = ((P14_SAVE_RESOURCE)parent.getSelectedItem()).getFACILTY_NM();
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


    public void DBQuery_GetWoker() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " exec XUSP_P_OEM_COMBO_BASIC_GET @FLAG='APK_WOKER'";

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

            final ArrayList<P14_SAVE_WORKER> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            P14_SAVE_WORKER itemBase = new P14_SAVE_WORKER();

            for(int i=0; i< ja.length();i++){
                JSONObject jObject = ja.getJSONObject(i);

                final String vWORKER_CD = jObject.getString("WORKER_CD");
                final String vWORKER_NM = jObject.getString("WORKER_NM");

                P14_SAVE_WORKER item = new P14_SAVE_WORKER();
                item.setWORKER_CD(vWORKER_CD);
                item.setWORKER_NM(vWORKER_NM);

                lstItem.add(item);
            }

            ArrayAdapter<P14_SAVE_WORKER> adapter = new ArrayAdapter<P14_SAVE_WORKER>(this, android.R.layout.simple_dropdown_item_1line, lstItem);
            cmb_WORKER_NM.setAdapter(adapter);

            //로딩시 기본값 세팅
            cmb_WORKER_NM.setSelection(adapter.getPosition(itemBase));

            //콤보박스 클릭 이벤트 정의
            cmb_WORKER_NM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //아이템 클릭 이벤트 정의
                    lbl_WORKER_CD.setText(((P14_SAVE_WORKER)parent.getSelectedItem()).getWORKER_CD());
                    sWORKER_NM = ((P14_SAVE_WORKER)parent.getSelectedItem()).getWORKER_NM();
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



    public void DBQuery_GET_PRODT_INFO(final String pPRODT_NO,final String pOPR_NO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_APK_PRODT_ORDER_DETAIL_INFO_GET ";
                sql += "  @PRODT_ORDER_NO = '" + pPRODT_NO + "'";
                sql += " ,@OPR_NO = '" + pOPR_NO + "'";

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

/*
    public void get_bl(String sql) throws IOException, XmlPullParserException {
       final String SOAP_ACTION = "http://www.unierp.com/cPCnfmRsltArr_P_CONFIRM_RSLT_ARR";
        String METHOD_NAME = "cPCnfmRsltArr_P_CONFIRM_RSLT_ARR";
        String NAMESPACE = "http://www.unierp.com/";
        String URL = "http://220.89.62.82/KO773_Default/Services/PP/PP4G452FL.asmx";

        //String[] pvStrGlobalCollection = TGSClass.CommonVariable.gStrGlobalCollection("Unierp");
        Gson gson = new Gson();
        //Gson gson1 = new Gson();
        //String result = gson.toJson(pvStrGlobalCollection);
        //String result2 = gson.toJson(sql);
        SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Request.addProperty("pvStrGlobalCollection", result);
        Request.addProperty("PLANT_CD", "P1");
        Request.addProperty("D", "D");
        Request.addProperty("dataset", sql);


        final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //soapEnvelope.dotNet = true;
            //soapEnvelope.setOutputSoapObject(Request);

           final HttpTransportSE transport = new HttpTransportSE(URL);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        transport.call(SOAP_ACTION,soapEnvelope);
                        resultString = (SoapPrimitive) soapEnvelope.getResponse();

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    } 
                }
            }).start();
            //transport.call(SOAP_ACTION,soapEnvelope);
            //resultString = (SoapPrimitive) soapEnvelope.getResponse();
            //TGSClass.AlterMessage(getApplicationContext(), "완료");
        //} catch (Exception ex) {
        //       TGSClass.AlterMessage(getApplicationContext(), "완료");
        //}
    }

 */

}

