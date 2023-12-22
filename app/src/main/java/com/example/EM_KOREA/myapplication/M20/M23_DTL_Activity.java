package com.example.EM_KOREA.myapplication.M20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.EM_KOREA.myapplication.BaseActivity;
import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.GetComboData;
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
    private String sJson, sJsonCombo, sJson_select_location_master;

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String prodt_order_no_ex,sl_cd;

    //== View 선언(EditText) ==//
    private EditText prodt_order_no,item_cd,item_nm,sts_nm,good_qty,bad_qty,stock_qty,in_dt,in_qty,opr_no;

    private Spinner sl_nm;
    //== View 선언(Button) ==//
    private Button btn_save;
    private String txt_prodt_order_no,txt_opr_no,txt_item_cd,txt_seq,txt_report_type,txt_prod_qty_in_base_unit;
    private String txt_base_unit,txt_lot_no,txt_lot_sub_no,txt_tracking_no,txt_sl_cd,txt_wc_cd,txt_order_status;


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
        prodt_order_no_ex   = getIntent().getStringExtra("PRODT_ORDER_NO");

        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어온 코드

        //== ID 값 바인딩 ==//

        prodt_order_no      = (EditText) findViewById(R.id.prodt_order_no);
        opr_no              = (EditText) findViewById(R.id.opr_no);

        item_cd             = (EditText) findViewById(R.id.item_cd);
        item_nm             = (EditText) findViewById(R.id.item_nm);
        sts_nm              = (EditText) findViewById(R.id.sts_nm);
        good_qty            = (EditText) findViewById(R.id.good_qty);
        bad_qty             = (EditText) findViewById(R.id.bad_qty);
        stock_qty           = (EditText) findViewById(R.id.stock_qty);
        sl_nm               = (Spinner) findViewById(R.id.sl_nm);
        in_dt               = (EditText) findViewById(R.id.in_dt);
        in_qty              = (EditText) findViewById(R.id.in_qty);
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

                try {
                    String str_INSP_DT = in_dt.getText().toString();


                    if (Double.valueOf(txt_prod_qty_in_base_unit) <= 0) {
                        TGSClass.AlterMessage(getApplicationContext(), "입고수량은 0보다 커야합니다.");
                        return;
                    }

                    String prodt_order_no = txt_prodt_order_no;
                    String opr_no = txt_opr_no;
                    String item_cd = txt_item_cd;
                    String seq = txt_seq;
                    String report_type = txt_report_type;
                    String prod_qty_in_base_unit = txt_prod_qty_in_base_unit;
                    String base_unit = txt_base_unit;
                    String lot_no = txt_lot_no;
                    String lot_sub_no = txt_lot_sub_no;
                    String tracking_no = txt_tracking_no;
                    String sl_cd = txt_sl_cd;
                    String wc_cd = txt_wc_cd;
                    String order_status = txt_order_status;
                    String report_dt = str_INSP_DT;

                    DBQuery_GET_BL(prodt_order_no, opr_no, item_cd, seq, report_type, prod_qty_in_base_unit,
                            base_unit, lot_no, lot_sub_no, tracking_no, sl_cd, wc_cd, order_status, report_dt);
                    if (result_msg.equals("완료처리 되었습니다.")) {
                        TGSClass.AlterMessage(getApplicationContext(), "생산입고 완료");

                        // 저장 후 결과 값 돌려주기
                        Intent resultIntent = new Intent();
                        // 결과처리 후 부른 Activity에 보낼 값
                        resultIntent.putExtra("SIGN", "OK");
                        // 부른 Activity에게 결과 값 반환
                        setResult(RESULT_OK, resultIntent);
                        // 현재 Activity 종료
                        finish();
                    } else {
                        TGSClass.AlterMessage(getApplicationContext(), "생산입고 실패\n" + result_msg);
                    }
                }
                catch (Exception e){
                    TGSClass.AlterMessage(getApplicationContext(), "오류가 발생하였습니다.");
                }
            }
        });

        in_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, in_dt, cal);
            }
        });
    }

    private void initializeData() {
        in_dt.setText(df.format(cal.getTime()));

        prodt_order_no.setText(prodt_order_no_ex);

        start();
        txt_sl_cd ="H119";
        dbQuery_get_cmbSL();
    }

    private void start() {

        dbQuery_get_PRODT_NO_INFO();

    }

    private void dbQuery_get_PRODT_NO_INFO() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_INSP_REQ_NO_INFO = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_ANDROID_PRODT_ORDER_GET ";
                sql += "  @PLANT_CD = '" + vPLANT_CD + "'";
                sql += "  ,@PRODT_ORDER_NO = '" + prodt_order_no.getText().toString() + "'";

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
                        for(int i = 0; i<ja.length(); i++){
                            JSONObject jObject = ja.getJSONObject(i);

                            prodt_order_no.setText(jObject.getString("PRODT_ORDER_NO"));
                            item_cd.setText(jObject.getString("ITEM_CD"));
                            item_nm.setText(jObject.getString("ITEM_NM"));
                            sts_nm.setText(jObject.getString("ORDER_STATUS"));
                            good_qty.setText(jObject.getString("GOOD_QTY_IN_ORDER_UNIT"));
                            bad_qty.setText(jObject.getString("BAD_QTY_IN_ORDER_UNIT"));
                            stock_qty.setText(jObject.getString("GOOD_STOCK"));
                            sl_cd = jObject.getString("MAJOR_SL_CD");
                            //in_dt.setText(jObject.getString("in_dt"));
                            in_qty.setText(jObject.getString("GOOD_QTY_IN_ORDER_UNIT"));

                            txt_prodt_order_no = jObject.getString("PRODT_ORDER_NO");
                            txt_opr_no = jObject.getString("OPR_NO");
                            txt_item_cd = jObject.getString("ITEM_CD");
                            txt_seq = jObject.getString("SEQ");
                            txt_report_type = jObject.getString("REPORT_TYPE");
                            txt_prod_qty_in_base_unit = jObject.getString("PROD_QTY_IN_BASE_UNIT");
                            txt_base_unit = jObject.getString("BASE_UNIT");
                            txt_lot_no = "";//jObject.getString("LOT_NO");
                            txt_lot_sub_no = "0";//jObject.getString("LOT_SUB_NO");
                            txt_tracking_no = jObject.getString("TRACKING_NO");
                            txt_sl_cd = jObject.getString("SL_CD");
                            txt_wc_cd = jObject.getString("WC_CD");
                            txt_order_status = jObject.getString("ORDER_STATUS");
                        }
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

    public boolean DBQuery_GET_BL(final String prodt_order_no, final String opr_no, final String item_cd, final String seq, final String report_type,
                                  final String prod_qty_in_base_unit, final String base_unit, final String lot_no, final String lot_sub_no, final String tracking_no,
                                  final String sl_cd, final String wc_cd, final String order_status, final String report_dt)
    {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession)getApplication(); //전역 클래스

                String vUnit_CD = vUNIT_CD;

                if(vUnit_CD == null)
                    vUnit_CD = global.getmUnitCDString();

                String plant_cd_parm                = vPLANT_CD;
                String prodt_order_no_parm          = prodt_order_no;
                String opr_no_parm                  = opr_no;
                String item_cd_parm                 = item_cd;
                String seq_parm                     = seq;
                String report_type_parm             = report_type;
                String prod_qty_in_base_unit_parm   = prod_qty_in_base_unit;
                String base_unit_parm               = base_unit;
                String lot_no_parm                  = lot_no;
                String lot_sub_no_parm              = lot_sub_no;
                String tracking_no_parm             = tracking_no;
                String sl_cd_parm                   = sl_cd;
                String wc_cd_parm                   = wc_cd;
                String order_status_parm            = order_status;
                String report_dt_parm               = report_dt;
                String rcpt_item_document_no_parm    ="";
                String count_parm                   ="1";
                String unit_cd_parm                 = vUnit_CD;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("plant_cd");
                parm.setValue(plant_cd_parm);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("prodt_order_no");
                parm2.setValue(prodt_order_no_parm);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("opr_no");
                parm3.setValue(opr_no_parm);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("item_cd");
                parm4.setValue(item_cd_parm);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("seq");
                parm5.setValue(seq_parm);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("report_type");
                parm6.setValue(report_type_parm);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("prod_qty_in_base_unit");
                parm7.setValue(prod_qty_in_base_unit_parm);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("base_unit");
                parm8.setValue(base_unit_parm);
                parm8.setType(String.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("lot_no");
                parm9.setValue(lot_no_parm);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("lot_sub_no");
                parm10.setValue(lot_sub_no_parm);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("tracking_no");
                parm11.setValue(tracking_no_parm);
                parm11.setType(String.class);

                PropertyInfo parm12 = new PropertyInfo();
                parm12.setName("sl_cd");
                parm12.setValue(sl_cd_parm);
                parm12.setType(String.class);

                PropertyInfo parm13 = new PropertyInfo();
                parm13.setName("wc_cd");
                parm13.setValue(wc_cd_parm);
                parm13.setType(String.class);

                PropertyInfo parm14 = new PropertyInfo();
                parm14.setName("order_status");
                parm14.setValue(order_status_parm);
                parm14.setType(String.class);

                PropertyInfo parm15 = new PropertyInfo();
                parm15.setName("report_dt");
                parm15.setValue(report_dt_parm);
                parm15.setType(String.class);

                PropertyInfo parm16 = new PropertyInfo();
                parm16.setName("rcpt_item_document_no");
                parm16.setValue(rcpt_item_document_no_parm);
                parm16.setType(String.class);

                PropertyInfo parm17 = new PropertyInfo();
                parm17.setName("count");
                parm17.setValue(count_parm);
                parm17.setType(String.class);

                PropertyInfo parm18 = new PropertyInfo();
                parm18.setName("unit_cd");
                parm18.setValue(unit_cd_parm);
                parm18.setType(String.class);

                PropertyInfo parm19 = new PropertyInfo();
                parm19.setName("user_id");
                parm19.setValue(unit_cd_parm);
                parm19.setType(String.class);

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
                pParms.add(parm13);
                pParms.add(parm14);
                pParms.add(parm15);
                pParms.add(parm16);
                pParms.add(parm17);
                pParms.add(parm18);
                pParms.add(parm19);

                result_msg = dba.SendHttpMessage("BL_SetProductionRcpt_ANDROID", pParms);
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }


    private void dbQuery_get_cmbSL() {       //작업장 스피너
        Thread workThd_get_item_acct = new Thread() {
            public void run() {
                String sql = " exec XUSP_TPC_SHIPMENT_GET_COMBODATA ";
                sql += " @FLAG = 'cmbSL'";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonCombo = dba.SendHttpMessage("GetSQLData", pParms);

                //TGSClass.AlterMessage(getApplicationContext(), pParms.toString());
            }
        };
        workThd_get_item_acct.start();   //스레드 시작
        try {
            workThd_get_item_acct.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> listItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();
            int idx =0;

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                if(vMINOR_CD.equals(txt_sl_cd)){
                    idx=i;
                }
                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                listItem.add(item);
            }


            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, listItem);
            sl_nm.setAdapter(adapter);

            //로딩시 기본값 세팅
            //sl_nm.setSelection(adapter.getPosition(itemBase));
            sl_nm.setSelection(idx);

            //cmbBizPartner.setSelection();

            //콤보박스 클릭 이벤트 정의
            sl_nm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    txt_sl_cd = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (JSONException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }
    }


}


