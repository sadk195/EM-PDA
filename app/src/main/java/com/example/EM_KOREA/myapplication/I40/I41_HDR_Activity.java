package com.example.EM_KOREA.myapplication.I40;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.EM_KOREA.myapplication.BaseActivity;
import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.ErrorList_Popup;
import com.example.EM_KOREA.myapplication.ErrorPopupActivity2;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class I41_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJson_DataSET = "", sJson_hdr = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand,Str_Order_No="";

    //== 수불번호 받을 변수 선언 ==//
    private String ITEM_DOCUMENT_NO = "";

    //== View 선언(EditText) ==//
    private EditText Order_No;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    public Button btn_save;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I41_DTL_REQUEST_CODE = 0;

    I41_HDR_ListViewAdapter listViewAdapter;

    private boolean Runed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i41_hdr);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID 값 바인딩 ==//
        Order_No   = (EditText) findViewById(R.id.Order_No);
        img_barcode         = (ImageView) findViewById(R.id.img_barcode);

        listview            = (ListView) findViewById(R.id.listOrder);

        btn_save            = (Button) findViewById((R.id.btn_save));

    }

    private void initializeCalendar() {
        cal = Calendar.getInstance();
        cal.setTime(new Date());
    }

    private void initializeListener() {
        //== 제조오더번호 이벤트 ==//
        Order_No.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Str_Order_No = Order_No.getText().toString();
                    start();
                    Runed = false;
                    return true;
                }
                return false;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                I41_HDR vItem = (I41_HDR) parent.getItemAtPosition(position);

                  /*  if (!vItem.getLOCATION().equals("출고대기장")) {
                        TGSClass.AlterMessage(getApplicationContext(), "선택하신 품목의 적치장이 [출고대기장]으로 이동되지 않았습니다.");
                    } else {
                        Intent intent = TGSClass.ChangeView(getPackageName(), 41_DTL_Activity.class.getSimpleName());
                        intent.putExtra("HDR", vItem);
                        startActivityForResult(intent, I41_DTL_REQUEST_CODE);
                    }*/
            }
        });

        //== QR코드 관련 이벤트 ==//
        img_barcode.setOnClickListener(qrClickListener);

        //== 일괄처리 버튼 이벤트 ==//
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Str_Order_No.equals("") || listview.getCount() < 1){
                    TGSClass.AlterMessage(getApplicationContext(), "반입/반출할 데이터가 없습니다 먼저 요청번호를 스캔하세요");
                    return;
                }
                if(Runed){
                    TGSClass.AlterMessage(getApplicationContext(), "먼저 요청번호를 스캔하세요");
                }
                I41_HDR_ListViewAdapter Adap = (I41_HDR_ListViewAdapter) listview.getAdapter();

                int ct = listview.getCount();

                String err_txt = "";
                for (int i = 0; i < ct; i++) {

                    I41_HDR vItem = (I41_HDR) Adap.getItem(i);

                    String prodt_order_no = vItem.getPRODT_ORDER_NO();
                    String plant_cd =vPLANT_CD;
                    String item_cd =vItem.getITEM_CD();
                    String tracking_no = "*";//vItem.getTRACKING_NO();
                    String lot_no  = vItem.getLOT_NO();
                    String sl_cd = vItem.getSL_CD();
                    String qty = vItem.getREQ_QTY();
                    String document_text ="";
                    String mov_type = vItem.getMOV_TYPE();
                    String wc_cd = vItem.getWC_CD();
                    switch (mov_type){
                        case "I03" : document_text = "-- PDA 작업장 반출 --";
                            prodt_order_no ="";
                                break;
                        case "I97" : document_text = "-- PDA 미사용잔량 반입 --";
                            break;
                        case "I99" : document_text = "-- PDA 작업장 반입 --";
                            prodt_order_no ="";
                            break;
                    }
                    if(vItem.getMOV_STATUS().trim().equals("S")){
                        continue;
                    }
                    String unit_cd = vUNIT_CD;
                    BL_DATASET_SELECT(prodt_order_no, plant_cd, item_cd, tracking_no, lot_no, sl_cd, qty, document_text, mov_type,unit_cd,wc_cd);
                    if(result_msg.contains("OK")){
                        listViewAdapter.setHDRStatus("S",i);//성공
                        dbSave(vItem,"S");
                    }
                    else {
                        //TGSClass.AlterMessage(getApplicationContext(), "오류 발생. "+result_msg);
                        listViewAdapter.setHDRStatus("E",i);//에러
                        dbSave(vItem,"E");
                        //listViewAdapter.notifyDataSetChanged();//변경데이터 저장(adapter의 getview실행)
                        //return;
                        err_txt += prodt_order_no +" 오류발생 :"+result_msg+"\n";
                    }
                System.out.println("err_txt:"+err_txt);
                }//for문
                if(!err_txt.equals("")){
                    Intent error_intent = TGSClass.ChangeView(getPackageName(), ErrorPopupActivity2.class);
                    error_intent.putExtra("MSG", err_txt);
                    startActivity(error_intent);
                }
                else{
                    TGSClass.AlterMessage(getApplicationContext(), "반입/반출 처리가 완료되었습니다.");
                }
                listViewAdapter.notifyDataSetChanged();//변경데이터 저장(adapter의 getview실행)
                Runed = true;
            }
        });
    }

    private void initializeData() {

    }

    private void start() {

        dbQuery(Str_Order_No);

        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                //final ArrayList<String> items = new ArrayList<String>();

                listViewAdapter = new I41_HDR_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    I41_HDR item = new I41_HDR();
                    item.setPRODT_ORDER_NO(jObject.getString("PRODT_ORDER_NO"));//제조오더번호
                    item.setOPR_NO(jObject.getString("OPR_NO"));//제조오더번호
                    item.setWC_CD(jObject.getString("WC_CD"));//제조오더번호
                    item.setITEM_CD(jObject.getString("ITEM_CD"));//품목
                    item.setITEM_NM(jObject.getString("ITEM_NM"));//품명
                    item.setTRACKING_NO(jObject.getString("TRACKING_NO"));//tracking no
                    item.setMOV_TYPE(jObject.getString("MOV_TYPE"));//반입/반출유형
                    item.setREQ_QTY(jObject.getString("REQ_QTY"));//수량
                    item.setMOV_STATUS(jObject.getString("MOV_STATUS"));//처리상태
                    item.setLOT_NO(jObject.getString("LOT_NO"));//LOT NO
                    item.setSL_CD(jObject.getString("SL_CD"));//이동창고
                    item.setNO_SEQ(jObject.getInt("NO_SEQ"));//순번

                    listViewAdapter.addHDRItem(item);
                }

                listview.setAdapter(listViewAdapter);



                TGSClass.AlterMessage(getApplicationContext(), ja.length() + " 건 조회 되었습니다.");

            } catch (JSONException ex) {
                TGSClass.AlterMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlterMessage(this, e1.getMessage());
            }
        } else if (sJson.equals("")) {
            TGSClass.AlterMessage(getApplicationContext(), "제조오더 정보가 없습니다.");
            return;
        }
    }

    private void dbQuery(final String order_no) {
        Thread workThd_dbQuery_PRODT_ORDER_INFO = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_I41_HDR_GET_LIST ";
                sql += " @PRODT_REQ_NO = '" + order_no + "' ";
                //sql += " ,@PLANT_CD = '" + vPLANT_CD + "' ";

                System.out.println("Sql:"+sql);
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
        workThd_dbQuery_PRODT_ORDER_INFO.start();   //스레드 시작
        try {
            workThd_dbQuery_PRODT_ORDER_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.



        } catch (InterruptedException ex) {

        }
    }

    private void dbSave(final I41_HDR I41,String status) {
        Thread workThd_dbQuery_PRODT_ORDER_INFO = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_I41_HDR_SET_LIST ";
                sql += " @PRODT_REQ_NO" +
                        " = '" + Str_Order_No + "' ";
                sql += " ,@ITEM_CD = '" + I41.getITEM_CD() + "' ";
                sql += " ,@NO_SEQ = '" + I41.getNO_SEQ() + "' ";
                sql += " ,@MOV_STATUS = '" + status + "' ";
                sql += " ,@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "' ";
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
        workThd_dbQuery_PRODT_ORDER_INFO.start();   //스레드 시작
        try {
            workThd_dbQuery_PRODT_ORDER_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.



        } catch (InterruptedException ex) {

        }
    }

    private boolean BL_DATASET_SELECT(final String prodt_order_no,final String plant_cd,final String item_cd,final String tracking_no,
                                      final String lot_no,final String sl_cd, final String qty, final String document_text,
                                      final String mov_type,final String unit_cd,final String wc_cd) {

        Thread workThd_BL_DATASET_SELECT = new Thread() {
            public void run() {

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();




                PropertyInfo parm = new PropertyInfo();
                parm.setName("prodt_order_no");
                parm.setValue(prodt_order_no);
                parm.setType(String.class);
                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("plant_cd");
                parm2.setValue(plant_cd);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("item_cd");
                parm3.setValue(item_cd);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("tracking_no");
                parm4.setValue(tracking_no);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("lot_no");
                parm5.setValue(lot_no);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("sl_cd");
                parm6.setValue(sl_cd);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("qty");
                parm7.setValue(qty);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("document_text");
                parm8.setValue(document_text);
                parm8.setType(String.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("mov_type");
                parm9.setValue(mov_type);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("unit_cd");
                parm10.setValue(unit_cd);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("wc_cd");
                parm11.setValue(wc_cd);
                parm11.setType(String.class);

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

                result_msg = dba.SendHttpMessage("BL_SetPartListETCOut_ANDROID2", pParms);
                ITEM_DOCUMENT_NO = result_msg.substring(result_msg.lastIndexOf(':') + 1).trim();
            }
        };
        workThd_BL_DATASET_SELECT.start();   //스레드 시작
        try {
            workThd_BL_DATASET_SELECT.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case I41_DTL_REQUEST_CODE:
                    String sign = data.getStringExtra("SIGN");
                    if (sign.equals("EXIT")) {
                        Toast.makeText(I41_HDR_Activity.this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (sign.equals("ADD")) {
                        Toast.makeText(I41_HDR_Activity.this, "추가 되었습니다.", Toast.LENGTH_SHORT).show();
                        start();
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I41_DTL_REQUEST_CODE:
                    // Toast.makeText(I35_HDR_Activity.this, "취소", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}