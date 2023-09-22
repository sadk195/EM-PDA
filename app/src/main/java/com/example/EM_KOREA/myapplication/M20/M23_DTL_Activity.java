package com.example.EM_KOREA.myapplication.M20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.EM_KOREA.myapplication.BaseActivity;
import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.MySession;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class M23_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson, sJson_hdr, sJson_select_location_master;

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String prodt_order_no_ex;

    //== View 선언(EditText) ==//
    private EditText prodt_order_no,item_cd,item_nm,sts_nm,good_qty,bad_qty,remain_qty,sl_nm,in_dt;

    //== View 선언(Button) ==//
    private Button btn_save;


    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m23_dtl);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        //ScanData에서 스캔 한 값을 받아 QM_10의 INSP_REQ_NO(검사의뢰번호)를 받아서 QM_11로 받아 오는 소스
        prodt_order_no_ex   = getIntent().getStringExtra("REQ_NO");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어온 코드

        //== ID 값 바인딩 ==//

        prodt_order_no      = (EditText) findViewById(R.id.prodt_order_no);
        item_cd             = (EditText) findViewById(R.id.item_cd);
        item_nm             = (EditText) findViewById(R.id.item_nm);
        sts_nm              = (EditText) findViewById(R.id.sts_nm);
        good_qty            = (EditText) findViewById(R.id.good_qty);
        bad_qty             = (EditText) findViewById(R.id.bad_qty);
        remain_qty          = (EditText) findViewById(R.id.remain_qty);
        sl_nm               = (EditText) findViewById(R.id.sl_nm);
        in_dt               = (EditText) findViewById(R.id.in_dt);

        btn_save            = (Button) findViewById(R.id.btn_save);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance();
        cal.setTime(new Date());
    }

    private void initializeListener() {

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String str_INSP_DT = in_dt.getText().toString();
                int INSP_YYYY = Integer.parseInt(str_INSP_DT.substring(0, 4));
                int INSP_MM = Integer.parseInt(str_INSP_DT.substring(5, 7));
                int INSP_DD = Integer.parseInt(str_INSP_DT.substring(8, 10));


                //DBQuery_GET_BL("C", vPLANT_CD, "G", "1", prodt_order_no.getText().toString(), lbl_opr_no_st, "1", "", "1", str_INSP_DT);
            }
        });
    }

    private void initializeData() {
        in_dt.setText(df.format(cal.getTime()));
        prodt_order_no.setText(prodt_order_no_ex);
        start();
    }

    private void start() {

        dbQuery_get_PRODT_NO_INFO();

    }

    private void dbQuery_get_PRODT_NO_INFO() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_INSP_REQ_NO_INFO = new Thread() {
            public void run() {
                String vSTS = "D";

                String sql = " EXEC XUSP_APK_QM21_GET_LIST ";
                sql += "  @FLAG = 'GRID1'";
                sql += "  ,@PLANT_CD = '" + vPLANT_CD + "'";
                sql += "  ,@PRODT_ORDER_NO = '" + prodt_order_no.getText().toString() + "'";
                sql += "  ,@STS = '" + vSTS + "'";
                sql += "  ,@ITEM_CD = ''";

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
        workThd_INSP_REQ_NO_INFO.start();   //스레드 시작
        try {
            workThd_INSP_REQ_NO_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

            try {

                boolean jSonType = TGSClass.isJsonData(sJson);
                if (!jSonType) return;

                if (!sJson.equals("[]")) {
                    try {
                        JSONArray ja = new JSONArray(sJson);

                        JSONObject jObject = ja.getJSONObject(0);

                        prodt_order_no.setText(jObject.getString("prodt_order_no"));
                        item_cd.setText(jObject.getString("item_cd"));
                        item_nm.setText(jObject.getString("item_nm"));
                        sts_nm.setText(jObject.getString("sts_nm"));
                        good_qty.setText(jObject.getString("good_qty"));
                        bad_qty.setText(jObject.getString("bad_qty"));
                        remain_qty.setText(jObject.getString("remain_qty"));
                        sl_nm.setText(jObject.getString("sl_nm"));
                        in_dt.setText(jObject.getString("in_dt"));

                    } catch (JSONException ex) {
                        TGSClass.AlterMessage(this, ex.getMessage());
                    } catch (Exception e1) {
                        TGSClass.AlterMessage(this, e1.getMessage());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InterruptedException ex) {

        }
    }

    public boolean DBQuery_GET_BL(final String cud_flag, final String plant_cd, final String report_type, final String seq, final String prodt_order_no,
                                  final String opr_no, final String qty, final String b_type_cd, final String qty_in, final String report_dt)
    {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession)getApplication(); //전역 클래스

                String vUnit_CD = getIntent().getStringExtra("pUNIT_CD");

                if(vUnit_CD == null)
                    vUnit_CD = global.getmUnitCDString();

                String cud_flag_parm             = cud_flag;
                String plant_cd_parm             = plant_cd;
                String report_type_parm          = report_type;
                String seq_parm                  = seq;
                String prodt_order_no_parm       = prodt_order_no;
                String opr_no_parm               = opr_no;
                String qty_parm                  = qty;
                String b_type_cd_parm            = b_type_cd;
                String qty_in_parm               = qty_in;
                String report_dt_parm            = report_dt;
                String unit_cd_parm              = vUnit_CD;

                String wk_id = vUnit_CD;//lbl_WORKER_CD.getText().toString();

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag_parm);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("plant_cd");
                parm2.setValue(plant_cd_parm);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("report_type");
                parm3.setValue(report_type_parm);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("seq_parm");
                parm4.setValue(seq_parm);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("prodt_order_no");
                parm5.setValue(prodt_order_no_parm);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("opr_no");
                parm6.setValue(opr_no_parm);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("qty");
                parm7.setValue(qty_parm);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("b_type_cd");
                parm8.setValue(b_type_cd_parm);
                parm8.setType(Integer.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("qty_in");
                parm9.setValue(qty_in_parm);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("report_dt");
                parm10.setValue(report_dt_parm);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("wk_id");
                parm11.setValue(wk_id);
                parm11.setType(String.class);

                PropertyInfo parm12 = new PropertyInfo();
                parm12.setName("unit_cd");
                parm12.setValue(unit_cd_parm);
                parm12.setType(String.class);

                pParms.add(parm);
                pParms.add(parm2);
                pParms.add(parm3);
                pParms.add(parm4);
                pParms.add(parm5);
                pParms.add(parm6);
                pParms.add(parm7);
                pParms.add(parm8);
                pParms.add(parm9);
                pParms.add(parm10);
                pParms.add(parm11);
                pParms.add(parm12);

                result_msg = dba.SendHttpMessage("BL_SetProductionResult_ANDROID", pParms);
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }



    private boolean dbSave_HDR() {
        Thread workThd_dbSave_HDR = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@TRNS_TYPE = 'PR'";
                sql += ",@MOV_TYPE = 'R01'";
                sql += ",@DOCUMENT_DT = '" + in_dt.getText().toString() + "'";
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@RTN_ITEM_DOCUMENT_NO = ''";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_hdr = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_dbSave_HDR.start();   //스레드 시작
        try {
            workThd_dbSave_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), "catch : ex");
        }

        if (sJson.equals("[]")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO,
                              final String LOT_NO, final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT,
                              final String LOCATION, final String BAD_ON_HAND_QTY) {
        Thread workThd_dbSave_DTL = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_I_GOODS_MOVEMENT_DTL_SET_CALCUATE_ANDROID ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";    //채번
                // += ",@DOCUMENT_YEAR =";                                  //프로시저 안에서 적용
                sql += ",@TRNS_TYPE = 'ST'";                                  //변경유형
                sql += ",@MOV_TYPE = 'T73'";                                   //이동유형
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";                  //공장코드
                sql += ",@DOCUMENT_DT = '" + in_dt.getText().toString() + "'";            //이동일자(t)

                /* I_ONHAND_STOCK_DETAIL 에서 바인딩 받아야 하므로 ListView에 조회되도록 SELECT 프로시저에 DTL항목 추가하고 바인딩 한 후 가져와야됨*/
                sql += ",@SL_CD = '" + SL_CD + "'";                             //창고코드
                sql += ",@ITEM_CD = '" + ITEM_CD + "'";                         //품목코드
                sql += ",@TRACKING_NO = '" + TRACKING_NO + "'";                 //TRACKING_NO
                sql += ",@LOT_NO = '" + LOT_NO + "'";                           //LOT_NO
                sql += ",@LOT_SUB_NO = " + LOT_SUB_NO;                    //LOT_SUB_NO
                sql += ",@QTY = " + Double.parseDouble(QTY);                                  //양품수량
                sql += ",@BASE_UNIT = '" + BASIC_UNIT + "'";                    //재고단위
                sql += ",@LOC_CD = '" + LOCATION + "'";                         //기존의 적치장
                sql += ",@TRNS_TRACKING_NO = '" + TRACKING_NO + "'";            /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_NO = '" + LOT_NO + "'";                      /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_SUB_NO = " + LOT_SUB_NO;                     /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + vPLANT_CD + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + SL_CD + "'";                        /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOC_CD = '" + LOCATION + "'";   //이동할 적치장

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;         //불량 수량
                sql += ",@MOVE_QTY = " + Double.parseDouble(QTY);          //이동 수량
                sql += ",@DEBIT_CREDIT_FLAG = 'D'";                     //증가 감소

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'M21_DTL_Activity'";

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
        workThd_dbSave_DTL.start();   //스레드 시작
        try {
            workThd_dbSave_DTL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
}