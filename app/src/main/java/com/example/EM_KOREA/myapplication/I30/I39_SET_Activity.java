package com.example.EM_KOREA.myapplication.I30;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.EM_KOREA.myapplication.BaseActivity;
import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class I39_SET_Activity extends BaseActivity {
    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand,param_sl_cd;

    //== View 선언(ListView) ==//
    private ListView listview,listview_dtl;

    //== View 선언(Button) ==//
    private Button btn_add,btn_end;

    //== View 선언(TextView) ==//
    private TextView txt_item_cd,txt_item_nm,txt_spec,txt_qty,txt_sl_cd;

    //== View 선언(EditText) ==//
    private EditText edt_total_qty;
    //== ActivityForResult 관련 변수 선언 ==//
    private final int I39_SET_REQUEST_CODE = 0;
    I39_SET_ListViewAdapter listViewAdapter;
    I39_SET_ListViewAdapter2 listViewAdapter_dtl;

    ArrayList<I39_DTL>i39_dtls;
    I39_DTL i39_Item;

    I39_SET2 vItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i39_set);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        param_sl_cd     = getIntent().getStringExtra("SL_CD"); //이전 페이지에서 설정한 출고창고

        i39_dtls   = (ArrayList<I39_DTL>)getIntent().getSerializableExtra("DTL");
        for(I39_DTL DTL : i39_dtls){

        }
        i39_Item    = (I39_DTL) getIntent().getSerializableExtra("ITEMS");


        txt_item_cd         = (TextView) findViewById(R.id.item_cd);
        txt_item_nm         = (TextView) findViewById(R.id.item_nm);
        txt_spec            = (TextView) findViewById(R.id.spec);
        txt_qty             = (TextView) findViewById(R.id.qty);
        txt_sl_cd           = (TextView) findViewById(R.id.sl_cd);

        btn_add             = (Button) findViewById(R.id.btn_add);
        btn_end             = (Button) findViewById(R.id.btn_end);

        edt_total_qty       = (EditText) findViewById(R.id.total_qty);

        listview            = (ListView) findViewById(R.id.listOrder1);
        listview_dtl        = (ListView) findViewById(R.id.listOrder2);
        listViewAdapter     = new I39_SET_ListViewAdapter();
        listViewAdapter_dtl = new I39_SET_ListViewAdapter2();
    }

    private void initializeListener() {
        //== 조회 버튼 이벤트 ==//
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSave("ADD");
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSave("EXIT");
            }
        });




        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //vItem = (I39_SET2) parent.getItemAtPosition(position);

                listViewAdapter.setChecked(position);//열을 선택할 경우에도 체크 상태 변경
                listViewAdapter.notifyDataSetChanged();

                edt_total_qty.setText(listViewAdapter.getChecked());
            }
        });

        listview_dtl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                vItem = (I39_SET2) parent.getItemAtPosition(position);

                /* Intent intent = TGSClass.ChangeView(getPackageName(), I39_SET_Activity.class.getSimpleName());

                intent.putExtra("HDR", vItem);  // 선택한 리스트를 파라메터로 다음페이지에 넘김.

                startActivityForResult(intent, I39_SET_REQUEST_CODE);*/
            }
        });

    }

    private void initializeData() {
        txt_item_cd.setText(i39_Item.getITEM_CD());
        txt_item_nm.setText(i39_Item.getITEM_NM());
        txt_spec.setText(i39_Item.getSPEC());
        txt_qty.setText(i39_Item.getQTY());
        txt_sl_cd.setText(param_sl_cd);

        for(I39_DTL dtl : i39_dtls){
            listViewAdapter.add_Item(dtl.getPRODT_ORDER_NO(),dtl.getOPR_NO(),dtl.getTRACKING_NO(),
                    dtl.getJOB_NM(),dtl.getQTY(),dtl.getSEQ(),true);
        }
        listview.setAdapter(listViewAdapter);

        edt_total_qty.setText(listViewAdapter.getChecked());//총 출고량 설정

        start();
    }

    private void start() {

        dbQuery(txt_item_cd.getText().toString());

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I39_SET2 item = new I39_SET2();

                item.setITEM_CD                (jObject.getString("ITEM_CD"));                      //품번
                item.setITEM_NM                (jObject.getString("ITEM_NM"));                      //품명
                item.setTRACKING_NO            (jObject.getString("TRACKING_NO"));                      //품명
                item.setLOT_NO                 (jObject.getString("LOT_NO"));                      //품명
                item.setLOT_SUB_NO             (jObject.getString("LOT_SUB_NO"));                      //품명
                item.setSL_CD                  (jObject.getString("SL_CD"));                          //창고 (intent)
                item.setSL_NM                  (jObject.getString("SL_NM"));                          //창고 (intent)
                item.setLOCATION               (jObject.getString("LOCATION"));                    //적치장
                item.setGOOD_QTY               (jObject.getString("GOOD_QTY"));                      //요청량(필요량)
                item.setBAD_QTY                (jObject.getString("BAD_QTY"));                      //요청량(필요량)
                item.setBASIC_UNIT             (jObject.getString("BASIC_UNIT"));                      //요청량(필요량)

                listViewAdapter_dtl.addHDRItem(item);
            }

            listview_dtl.setAdapter(listViewAdapter_dtl);


            TGSClass.AlterMessage(getApplicationContext(), ja.length() + "건 조회되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlterMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlterMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String item_cd) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " XUSP_MES_PRODT_ORDER_MULTI_INFO_STOCK_GET ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@ITEM_CD = '" + item_cd + "'";

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
        workThd_dbQuery.start();   //스레드 시작
        try {
            workThd_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }


    private void dbSave(String val) {
         try {
             if(vItem == null ){
                 TGSClass.AlterMessage(getApplicationContext(),"출고할 항목을 선택하세요.");
                 return;
             }
             ArrayList<I39_SET> i39_sets = listViewAdapter.getItems();

             double out_qty = 0;
             for (I39_SET set : i39_sets) {
                 if (!set.getCHK_OUT()) {
                     continue;
                 }
                 out_qty = out_qty + Double.valueOf(set.getREQ_QTY());
             }
             double good_qty = Double.valueOf(vItem.getGOOD_QTY());


             if (good_qty < out_qty) {

                 TGSClass.AlterMessage(getApplicationContext(),
                         "선택한 제조오더의 출고량이 출고가능수량 보다 많습니다.");
                 return;
             }

             out_qty =0; //출고수량 체크를 위해 초기화
             String out_no ="";

             long now = System.currentTimeMillis();
             Date date = new Date(now);
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             String REPORT_DT = sdf.format(date);



             for (I39_SET set : i39_sets) {
                 if (!set.getCHK_OUT()) {
                     continue;
                 }
                 if(good_qty < Double.valueOf(set.getREQ_QTY()) + out_qty ){
                     break;
                 }

                 if (dbSave_HDR(REPORT_DT,set.getPRODT_ORDER_NO()) == true) {
                     try {
                         JSONArray ja = new JSONArray(sJson);
                         int idx = 0;
                         JSONObject jObject = ja.getJSONObject(idx);

                         String RTN_ITEM_DOCUMENT_NO = jObject.getString("RTN_ITEM_DOCUMENT_NO");

                         I39_SET2 item = vItem;

                         String sl_cd            = item.getSL_CD();
                         String item_cd          = item.getITEM_CD();
                         String tracking_no      = set.getTRACKING_NO();
                         String lot_no           = item.getLOT_NO();
                         String lot_sub_no       = item.getLOT_SUB_NO();
                         String qty              = set.getREQ_QTY();
                         String basic_unit       = item.getBASIC_UNIT();
                         String location         = item.getLOCATION();
                         String bad_on_hand_qty  = item.getBAD_QTY();
                         String chk_qty          = set.getREQ_QTY();//item.getCHK_QTY();

                         dbSave_DTL(RTN_ITEM_DOCUMENT_NO, sl_cd, item_cd, tracking_no, lot_no, lot_sub_no, qty,
                                 basic_unit, location, bad_on_hand_qty, chk_qty,REPORT_DT,set.getPRODT_ORDER_NO());


                     } catch (JSONException ex) {
                         TGSClass.AlterMessage(getApplicationContext(), "catch : ex");
                     } catch (Exception e1) {
                         TGSClass.AlterMessage(getApplicationContext(), "catch : e1");
                     }

                     if(!out_no.equals("")){
                         out_no = out_no + " / ";
                     }
                     out_no = out_no + set.getPRODT_ORDER_NO();

                 } else {
                     //TGSClass.AlterMessage(getApplicationContext(), "저장 되지 않았습니다. 데이터를 정확히 입력하였는지 확인해주시기 바랍니다!");
                     continue;
                 }
                 out_qty++;

             }

             TGSClass.AlterMessage(getApplicationContext(),"생산출고 완료 \n"+out_no);

             // 저장 후 결과 값 돌려주기
             Intent resultIntent = new Intent();
             // 결과처리 후 부른 Activity에 보낼 값
             resultIntent.putExtra("SIGN", val);
             // 부른 Activity에게 결과 값 반환
             setResult(RESULT_OK, resultIntent);
             // 현재 Activity 종료
             finish();


         } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }



    private boolean dbSave_HDR(String move_date_st,String vPRODT_ORDER_NO_st) {
        Thread workThd_dbSave_HDR = new Thread() {
            public void run() {



                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@TRNS_TYPE = 'AN'";
                sql += ",@MOV_TYPE = 'I39'";
                sql += ",@DOCUMENT_DT = '" + move_date_st + "'";
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@RECORD_NO = '" + vPRODT_ORDER_NO_st + "'"; //정영진
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

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

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

    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO, final String LOT_NO,
                               final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT, final String LOCATION, final String BAD_ON_HAND_QTY,
                               final String CHK_QTY,String move_date_data, String vPRODT_ORDER_NO_st) {
        Thread workThd_dbSave_DTL = new Thread() {
            public void run() {


                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_SET ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";        //채번
                // += ",@DOCUMENT_YEAR =";                                      //프로시저 안에서 적용
                sql += ",@TRNS_TYPE = 'AN'";                                  //변경유형
                sql += ",@MOV_TYPE = 'I39'";                                    //아동유형
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";                      //공장코드
                sql += ",@DOCUMENT_DT = '" + move_date_data + "'";              //이동일자

                /* I_ONHAND_STOCK_DETAIL 에서 바인딩 받아야 하므로 ListView에 조회되도록 SELECT 프로시저에 DTL항목 추가하고 바인딩 한 후 가져와야됨*/
                sql += ",@SL_CD = '" + SL_CD + "'";                             //창고코드
                sql += ",@ITEM_CD = '" + ITEM_CD + "'";                         //품목코드
                sql += ",@TRACKING_NO ='*'" ;//+ TRACKING_NO + "'";                 //TRACKING_NO
                sql += ",@LOT_NO = '" + LOT_NO + "'";                           //LOT_NO
                sql += ",@LOT_SUB_NO = " + LOT_SUB_NO;                          //LOT_SUB_NO
                sql += ",@QTY = " + QTY;                                        //양품수량
                sql += ",@BASE_UNIT = '" + BASIC_UNIT + "'";                    //재고단위
                sql += ",@LOC_CD = '" + LOCATION + "'";                         //기존의 적치장
                sql += ",@TRNS_TRACKING_NO = '" + TRACKING_NO + "'";            /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_NO = '" + LOT_NO + "'";                      /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_SUB_NO = " + LOT_SUB_NO;                     /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + vPLANT_CD + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + SL_CD + "'";                        /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOC_CD = '출고대기장'";                   //이동할 적치장
                sql += ",@RECORD_NO = '" + vPRODT_ORDER_NO_st + "'";       //제조오더(비고)

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;                //불량 수량
                sql += ",@MOVE_QTY = " + CHK_QTY;                        //이동 수량   //현재 이동 수량을 막아놨기 때문에 혹시 풀 경우 move_date_data로 다시 선언 하면 됨

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'I35_DTL_Activity'";


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
            TGSClass.AlterMessage(getApplicationContext(), "catch : ex");
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case I39_SET_REQUEST_CODE:
                    /*String sign = data.getStringExtra("SIGN");
                    if (sign.equals("EXIT")) {
                        TGSClass.AlterMessage(this, "저장 되었습니다.");
                        finish();
                    } else if (sign.equals("ADD")) {
                        TGSClass.AlterMessage(this, "추가 되었습니다.");
                        start();
                    }*/
                    finish();
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I39_SET_REQUEST_CODE:
                    // Toast.makeText(I35_HDR_Activity.this, "취소", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}