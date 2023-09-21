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
    public String PRODT_ORDER_NO_DataSET = "";
    public String OPR_NO_DataSET = "";
    public String SL_CD_DataSET = "";
    public String ITEM_CD_DataSET = "";
    public String TRACKING_NO_DataSET = "";
    public String LOT_NO_DataSET = "";
    public String LOT_SUB_NO_DataSET = "";
    public String ENTRY_QTY_DataSET = "";
    public String ENTRY_UNIT_DataSET = "";
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

        i39_dtls   = (ArrayList<I39_DTL>)getIntent().getSerializableExtra("DTL");
        i39_Item    = (I39_DTL) getIntent().getSerializableExtra("ITEMS");


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

        for(I39_DTL dtl : i39_dtls){
            listViewAdapter.add_Item(dtl.getPRODT_ORDER_NO(),dtl.getOPR_NO(),dtl.getTRACKING_NO(),
                    dtl.getJOB_NM(),dtl.getQTY(),dtl.getSEQ(),true);

        }
        listview.setAdapter(listViewAdapter);
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
                item.setGOOD_QTY               (jObject.getString("GOOD_QTY"));                      //요청량(필요량)
                item.setLOCATION               (jObject.getString("LOCATION"));                    //적치장
                item.setSL_CD                  (jObject.getString("SL_CD"));                          //창고 (intent)
                item.setSL_NM                  (jObject.getString("SL_NM"));                          //창고 (intent)

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
             String CUD_FLAG = "U";

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

                 String PRODT_ORDER_NO = set.getPRODT_ORDER_NO();
                 String OPR_NO = set.getOPR_NO();
                 String ITEM_CD = txt_item_cd.getText().toString();
                 String SEQ_NO = set.getSEQ();
                 String OUT_QTY = set.getREQ_QTY();

                 BL_DATASET_SELECT(CUD_FLAG, vPLANT_CD, PRODT_ORDER_NO, OPR_NO, ITEM_CD, SEQ_NO, OUT_QTY, REPORT_DT, vUNIT_CD);
                if(!result_msg.equals("생산출고 완료")){
                    TGSClass.AlterMessage(getApplicationContext(),result_msg);
                    break;
                }
                 out_qty++;

                if(!out_no.equals("")){
                    out_no = out_no + " / ";
                }
                out_no = out_no + PRODT_ORDER_NO;
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

    private boolean BL_DATASET_SELECT(final String CUD_FLAG, final String vPlant_CD, final String PRODT_ORDER_NO, final String OPR_NO, final String ITEM_CD, final String SEQ_NO, final String OUT_QTY, final String REPORT_DT, final String vUnit_CD) {
        Thread workThd_BL_DATASET_SELECT = new Thread() {
            public void run() {


                String cud_flag         = CUD_FLAG;
                String plant_cd         = vPlant_CD;
                String prodt_order_no   = PRODT_ORDER_NO;
                String opr_no           = OPR_NO;
                String item_cd          = ITEM_CD;
                String seq_no           = SEQ_NO;
                String out_qty          = OUT_QTY;
                String report_dt        = REPORT_DT;
                String unit_cd          = vUnit_CD;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("plant_cd");
                parm2.setValue(plant_cd);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("prodt_order_no");
                parm3.setValue(prodt_order_no);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("opr_no");
                parm4.setValue(opr_no);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("item_cd");
                parm5.setValue(item_cd);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("seq_no");
                parm6.setValue(seq_no);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("out_qty");
                parm7.setValue(out_qty);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("report_dt");
                parm8.setValue(report_dt);
                parm8.setType(String.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("unit_cd");
                parm9.setValue(unit_cd);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("user_id");
                parm10.setValue(vUSER_ID);
                parm10.setType(String.class);

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

                result_msg = dba.SendHttpMessage("BL_SetPartListOut_ANDROID", pParms);

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