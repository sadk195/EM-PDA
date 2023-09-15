package com.example.EM_KOREA.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class I39_SET_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(ListView) ==//
    private ListView listview,listview_dtl;

    //== View 선언(Button) ==//
    private Button btn_add,btn_end;

    //== View 선언(TextView) ==//
    private TextView txt_item_cd,txt_item_nm,txt_spec,txt_qty;

    //== View 선언(EditText) ==//
    private EditText edt_total_qty;
    //== ActivityForResult 관련 변수 선언 ==//
    private final int I39_SET_REQUEST_CODE = 0;
    I39_SET_ListViewAdapter listViewAdapter;
    I39_SET_ListViewAdapter2 listViewAdapter_dtl;

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

        txt_item_cd         = (TextView) findViewById(R.id.item_cd);
        txt_item_nm         = (TextView) findViewById(R.id.item_nm);
        txt_spec            = (TextView) findViewById(R.id.spec);
        txt_qty             = (TextView) findViewById(R.id.qty);

        btn_add             = (Button) findViewById(R.id.btn_add);
        btn_end             = (Button) findViewById(R.id.btn_end);

        edt_total_qty       = (EditText) findViewById(R.id.total_qty);

        listview            = (ListView) findViewById(R.id.listOrder);
        listview_dtl        = (ListView) findViewById(R.id.listOrder2);
        listViewAdapter     = new I39_SET_ListViewAdapter();
        listViewAdapter_dtl = new I39_SET_ListViewAdapter2();
    }

    private void initializeListener() {
        //== 조회 버튼 이벤트 ==//
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("i39 set btn_add click");

                //start();
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("i39 set btn_end click");
                //finish();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
              dbQuery2("1");
            }
        });

        listview_dtl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
               /* I39_SET vItem = (I39_SET) parent.getItemAtPosition(position);

                Intent intent = TGSClass.ChangeView(getPackageName(), I39_SET_Activity.class.getSimpleName());

                intent.putExtra("HDR", vItem);  // 선택한 리스트를 파라메터로 다음페이지에 넘김.

                startActivityForResult(intent, I39_SET_REQUEST_CODE);*/
            }
        });

    }

    private void initializeData() {
        //Start(); //처음 조회할 시 데이터가 많아서 선태사항이 느린 경우를 대비해 조회조건을 사용자가 원할히 선택한 다음 조회할 수 있도록 조정 (조회 버튼 별도 사용)
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

                I39_SET item = new I39_SET();

                item.setITEM_CD                (jObject.getString("ITEM_CD"));                      //품번
                item.setITEM_NM                (jObject.getString("ITEM_NM"));                      //품명
                item.setREQ_QTY                (jObject.getString("REQ_QTY"));                      //요청량(필요량)
                item.setISSUED_QTY             (jObject.getString("ISSUED_QTY"));                //출고량
                item.setLOCATION               (jObject.getString("LOCATION"));                    //적치장
                item.setSL_CD                  (jObject.getString("SL_CD"));                          //창고 (intent)
                item.setOUT_QTY                (jObject.getString("QTY"));                              //현재고
                item.setTRACKING_NO            (jObject.getString("TRACKING_NO"));              //
                item.setPRODT_ORDER_NO         (jObject.getString("PRODT_ORDER_NO"));        //
                item.setREMAIN_QTY             (jObject.getString("REMAIN_QTY"));                //잔량
                item.setWMS_GOOD_ON_HAND_QTY   (jObject.getString("WMS_GOOD_ON_HAND_QTY"));                //잔량
                item.setCHK_OUT(true);
                listViewAdapter.addHDRItem(item);
            }

            listview.setAdapter(listViewAdapter);


            TGSClass.AlterMessage(getApplicationContext(), ja.length() + "건 조회되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlterMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlterMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String prodt_order_no) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_MES_APK_PRODT_OUT_LOCATION_MOVE_I39_QUERY_ANDROID ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "'";

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

    private void dbQuery2(final String prodt_order_no) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_MES_APK_PRODT_OUT_LOCATION_MOVE_I39_QUERY_ANDROID ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "'";

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

            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                //final ArrayList<String> items = new ArrayList<String>();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    I39_SET2 item = new I39_SET2();

                    item.setITEM_CD                (jObject.getString("ITEM_CD"));                      //품번
                    item.setITEM_NM                (jObject.getString("ITEM_NM"));                      //품명
                    item.setREQ_QTY                (jObject.getString("REQ_QTY"));                      //요청량(필요량)
                    item.setISSUED_QTY             (jObject.getString("ISSUED_QTY"));                //출고량
                    item.setLOCATION               (jObject.getString("LOCATION"));                    //적치장
                    item.setSL_CD                  (jObject.getString("SL_CD"));                          //창고 (intent)
                    item.setOUT_QTY                (jObject.getString("QTY"));                              //현재고
                    item.setTRACKING_NO            (jObject.getString("TRACKING_NO"));              //
                    item.setPRODT_ORDER_NO         (jObject.getString("PRODT_ORDER_NO"));        //
                    item.setREMAIN_QTY             (jObject.getString("REMAIN_QTY"));                //잔량
                    item.setWMS_GOOD_ON_HAND_QTY   (jObject.getString("WMS_GOOD_ON_HAND_QTY"));                //잔량

                    listViewAdapter_dtl.addHDRItem(item);
                }

                listview_dtl.setAdapter(listViewAdapter_dtl);


                TGSClass.AlterMessage(getApplicationContext(), ja.length() + "건 조회되었습니다.");
            } catch (JSONException ex) {
                TGSClass.AlterMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlterMessage(this, e1.getMessage());
            }
        } catch (InterruptedException ex) {

        }
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