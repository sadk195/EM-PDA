package com.example.EM_KOREA.myapplication.I30;

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

import com.example.EM_KOREA.myapplication.BaseActivity;
import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class I39_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(EditText) ==//
    private EditText txt_Scan_prodt_order_no, txt_Scan_item_cd;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode_prodt_order_no, img_barcode_item_cd;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    public Button btn_up,btn_down,btn_del,btn_order_start;

    private int selected_idx;

    private String txt_ProdtNO="";
    //== ActivityForResult 관련 변수 선언 ==//
    private final int I39_DTL_REQUEST_CODE = 0;
    I39_HDR_ListViewAdapter listViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i39_hdr);

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

        //== ID 값 바인딩 ==//
        txt_Scan_prodt_order_no     = (EditText) findViewById(R.id.txt_Scan_prodt_order_no);
        img_barcode_prodt_order_no  = (ImageView) findViewById(R.id.img_barcode_prodt_order_no);


        btn_up              = (Button) findViewById(R.id.btn_up);
        btn_down            = (Button) findViewById(R.id.btn_down);
        btn_del             = (Button) findViewById(R.id.btn_del);
        btn_order_start     = (Button) findViewById(R.id.btn_order_start);
        listview            = (ListView) findViewById(R.id.listOrder);
        listViewAdapter     = new I39_HDR_ListViewAdapter();

        //리스트뷰를 다시 쓸때 포커스가 리스트뷰로 이동하기때문에 포커스를 가지고 있지 못하도록 수정
        //리스트의 항목선택은 리스트뷰 안의 어댑터와 로우가 가지고 있기 때문에 리스트뷰 자체의 포커스와 무관
        listview.setFocusable(false);

    }

    private void initializeListener() {


        //== 제조오더번호 이벤트 ==//
        txt_Scan_prodt_order_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    txt_ProdtNO = txt_Scan_prodt_order_no.getText().toString();

                    start();
                    txt_Scan_prodt_order_no.requestFocus();
                    txt_Scan_prodt_order_no.setText("");
                    return true;
                }
                return false;
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // I39_HDR vItem = (I39_HDR) parent.getItemAtPosition(position);

                selected_idx = position;
                listViewAdapter.setSelected(position);
                listViewAdapter.notifyDataSetChanged();
            }
        });

        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_idx = listViewAdapter.UpItem(selected_idx);
                listViewAdapter.notifyDataSetChanged();
            }
        });
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_idx = listViewAdapter.DownItem(selected_idx);
                listViewAdapter.notifyDataSetChanged();
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_idx = listViewAdapter.DelItem(selected_idx);
                listViewAdapter.notifyDataSetChanged();
            }
        });

        btn_order_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<I39_HDR> HDR = listViewAdapter.getItems();

                if(HDR == null || HDR.size() ==0){
                    TGSClass.AlterMessage(getApplicationContext(), "먼저 제조오더를 조회하세요");
                    return;
                }
                Intent intent = TGSClass.ChangeView(getPackageName(), I39_DTL_Activity.class);

                intent.putExtra("MENU_ID", "I39");
                intent.putExtra("MENU_REMARK", vMenuRemark);
                intent.putExtra("START_COMMAND", vStartCommand);

                intent.putExtra("I39_HDR",HDR);

                startActivityForResult(intent, I39_DTL_REQUEST_CODE);
            }
        });


        //== 바코드 이벤트 ==//
        img_barcode_prodt_order_no.setOnClickListener(qrClickListener);
    }

    private void initializeData() {
        //Start(); //처음 조회할 시 데이터가 많아서 선태사항이 느린 경우를 대비해 조회조건을 사용자가 원할히 선택한 다음 조회할 수 있도록 조정 (조회 버튼 별도 사용)
    }

    private void start() {

        dbQuery(txt_ProdtNO);

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            //listViewAdapter.Clear();

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I39_HDR item = new I39_HDR();

                item.setPRODT_ORDER_NO      (jObject.getString("PRODT_ORDER_NO"));
                item.setITEM_CD             (jObject.getString("ITEM_CD"));
                item.setPRODT_ORDER_UNIT    (jObject.getString("PRODT_ORDER_UNIT"));
                item.setITEM_NM             (jObject.getString("ITEM_NM"));
                item.setSPEC                (jObject.getString("SPEC"));
                item.setTRACKING_NO         (jObject.getString("TRACKING_NO"));
                item.setPRODT_ORDER_QTY     (jObject.getString("PRODT_ORDER_QTY"));
                item.setGOOD_QTY            (jObject.getString("GOOD_QTY"));
                item.setBAD_QTY             (jObject.getString("BAD_QTY"));
                item.setREMAIN_QTY          (jObject.getString("REMAIN_QTY"));
                item.setSL_CD               (jObject.getString("SL_CD"));
                item.setJOB_NM              (jObject.getString("JOB_NM"));
                item.setOPR_NO              (jObject.getString("OPR_NO"));
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
                String sql = " EXEC XUSP_MES_PRODT_ORDER_MULTI_INFO_GET ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "' ";
               // sql += " ,@ITEM_CD = '" + item_cd + "'";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case I39_DTL_REQUEST_CODE:
                    String sign = data.getStringExtra("SIGN");
                   /* if (sign.equals("EXIT")) {
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
                case I39_DTL_REQUEST_CODE:
                    // Toast.makeText(I35_HDR_Activity.this, "취소", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}