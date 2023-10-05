package com.example.EM_KOREA.myapplication.I30;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.EM_KOREA.myapplication.BaseActivity;
import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.GetComboData;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class I39_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonCombo = "";;

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand,txtItemCd;

    //== View 선언(EditText) ==//
    private EditText  txt_Scan_item_cd;

    //== View 선언(Spinner) ==//
    private Spinner storage_location;

    //== View 선언(ImageView) ==//
    private ImageView  img_barcode_item_cd;

    //== View 선언(ListView) ==//
    private ListView listview,listview_dtl;

    //== View 선언(Button) ==//
    public Button btn_query,btn_cancle;

    //== Spinner 관련 변수 선언 ==//
    private String sl_cd_query = "";

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I39_SET_REQUEST_CODE = 0;
    I39_DTL_ListViewAdapter listViewAdapter;
    I39_DTL_ListViewAdapter2 listViewAdapter_dtl;
    ArrayList<I39_HDR> I39_HDR_ITEMS;
    ArrayList<I39_DTL> I39_DTL_ITEMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i39_dtl);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        I39_HDR_ITEMS   = (ArrayList<I39_HDR>)getIntent().getSerializableExtra("I39_HDR");
        I39_DTL_ITEMS   = new ArrayList<>();
        //== ID 값 바인딩 ==//

        txt_Scan_item_cd     = (EditText) findViewById(R.id.txt_Scan_item_cd);
        img_barcode_item_cd  = (ImageView) findViewById(R.id.img_barcode_item_cd);
        btn_query            = (Button) findViewById(R.id.btn_query);
        btn_cancle           = (Button) findViewById(R.id.btn_cancle);

        storage_location        = (Spinner) findViewById(R.id.storage_location);

        listview            = (ListView) findViewById(R.id.listOrder);
        listview_dtl        = (ListView) findViewById(R.id.listOrder2);
        listViewAdapter     = new I39_DTL_ListViewAdapter();
        listViewAdapter_dtl = new I39_DTL_ListViewAdapter2();


    }

    private void initializeListener() {
        //== 조회 버튼 이벤트 ==//
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatas();//기본적으로 리스트 조회
                //주소참조로 가져오므로 데이터만 가져와서 비교한후 temp에 add하여 적용
                ArrayList<I39_DTL> listItem = listViewAdapter_dtl.getItems();
                ArrayList<I39_DTL> temp = new ArrayList<>();//실젝 적용되는 리스트

                String scan_item = txt_Scan_item_cd.getText().toString();

                if(!scan_item.equals("")){//빈값일 경우 전체 품목 표시
                    for(int i =0 ; i <listViewAdapter_dtl.getCount(); i++){
                        if(listItem.get(i).getITEM_CD().contains(scan_item)){//해당 문자열을 포함하는 경우 표시
                            temp.add(listItem.get(i));
                        }
                    }

                    listViewAdapter_dtl.clear();//아래 리스트 초기화

                    for(I39_DTL dtl : temp){
                        listViewAdapter_dtl.addHDRItem(dtl);
                    }
                    listViewAdapter_dtl.notifyDataSetChanged();
                }
                TGSClass.AlterMessage(getApplicationContext(), listViewAdapter_dtl.getCount() + "건 조회되었습니다.");

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //== 제조오더번호 이벤트 ==//
        txt_Scan_item_cd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    txt_Scan_item_cd.requestFocus();
                    return true;
                }
                return false;
            }
        });



        //== 바코드 이벤트 ==//
        img_barcode_item_cd.setOnClickListener(qrClickListener);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
            }
        });

        listview_dtl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                I39_DTL vItem = (I39_DTL) parent.getItemAtPosition(position);

                //선택한 품목과 같은 리스트를 넘김
                ArrayList<I39_DTL> DTL_SEL = new ArrayList<>();
                for(I39_DTL dtl : I39_DTL_ITEMS){
                    if(vItem.getITEM_CD().equals(dtl.getITEM_CD())){
                        DTL_SEL.add(dtl);
                    }
                }

                Intent intent = TGSClass.ChangeView(getPackageName(), I39_SET_Activity.class);
                intent.putExtra("MENU_ID", "I39");
                intent.putExtra("MENU_REMARK", vMenuRemark);
                intent.putExtra("START_COMMAND", vStartCommand);

                intent.putExtra("ITEMS", vItem);  // 선택한 리스트를 파라메터로 다음페이지에 넘김.
                intent.putExtra("DTL", DTL_SEL);  // 선택한 리스트를 파라메터로 다음페이지에 넘김.
                intent.putExtra("SL_CD", sl_cd_query);  // 선택한 리스트를 파라메터로 다음페이지에 넘김.


                startActivityForResult(intent, I39_SET_REQUEST_CODE);
            }
        });

    }
    private void initializeData() {
        getDatas();
        TGSClass.AlterMessage(getApplicationContext(), listViewAdapter_dtl.getCount() + "건 조회되었습니다.");

        dbQuery_get_storage_location();

    }

    private void getDatas() {
        listViewAdapter.clear();
        listViewAdapter_dtl.clear();
        I39_DTL_ITEMS.clear();

        for(I39_HDR ITEM: I39_HDR_ITEMS ){ //위쪽 그리드 내용은 앞페이지에서 조회한 내용 표시(마지막 페이지까지 사용)
            listViewAdapter.addHDRItem(ITEM);
            start(ITEM.getPRODT_ORDER_NO(),ITEM.getOPR_NO());//품목 조회 시작
        }

        listview.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

        setStockData();//제조오더에 해당하는 품목을 모두 조회한 후 품목별로 그룹핑
        listview_dtl.setAdapter(listViewAdapter_dtl);
        listViewAdapter_dtl.notifyDataSetChanged();
    }

    private void start(String prodt_order_no,String opr_no) {
        dbQuery(prodt_order_no,opr_no); //조회 및 리스트 추가
    }

    private void dbQuery(String prodt_order_no,String opr_no) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_MES_PRODT_ORDER_MULTI_INFO_SUB_GET ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "'";
                sql += " ,@OPR_NO = '" + opr_no + "'";

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
                //I39_DTL_ITEMS.clear();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    I39_DTL item = new I39_DTL();

                    item.setPRODT_ORDER_NO(jObject.getString("PRODT_ORDER_NO"));
                    item.setOPR_NO(jObject.getString("OPR_NO"));
                    item.setITEM_CD(jObject.getString("ITEM_CD"));
                    item.setITEM_NM(jObject.getString("ITEM_NM"));
                    item.setSPEC(jObject.getString("SPEC"));
                    item.setTRACKING_NO(jObject.getString("TRACKING_NO"));
                    item.setLOCATION(jObject.getString("LOCATION"));
                    item.setQTY(jObject.getString("QTY"));
                    item.setSEQ(jObject.getString("SEQ"));
                    item.setSL_CD(jObject.getString("SL_CD"));
                    item.setJOB_NM(jObject.getString("JOB_NM"));

                    I39_DTL_ITEMS.add(item);

                }

            } catch (JSONException ex) {
                TGSClass.AlterMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlterMessage(this, e1.getMessage());
            }
        } catch (InterruptedException ex) {

        }
    }


    private void setStockData(){
        ArrayList<I39_DTL> temp =  new ArrayList<>();
        ArrayList<String>  Items = new ArrayList<>();

        for(I39_DTL dtl : I39_DTL_ITEMS){

            if(Items.contains(dtl.getITEM_CD())) { // 처리완료된 품목이면 다음 리스트로
                continue;
            }

            I39_DTL temp_dtl = dtl;
            String item_cd = dtl.getITEM_CD();

            double temp_qty = 0;//.parseInt(temp_dtl.getQTY());

            for(I39_DTL dtl2 : I39_DTL_ITEMS) {
                if(dtl2.getITEM_CD().equals(item_cd) ){//현재 찾고있는 품목과 같은 품목의 수량 더하기

                    temp_qty = temp_qty +  Double.parseDouble(dtl2.getQTY());
                }
            }

            Items.add(item_cd);
            temp_dtl.setQTY(String.valueOf(temp_qty));
            temp.add(temp_dtl);
        }
        listViewAdapter_dtl.clear();

        for(I39_DTL dtl : temp){
            listViewAdapter_dtl.addHDRItem(dtl);
        }

        listViewAdapter_dtl.notifyDataSetChanged();
    }

    private void dbQuery_get_storage_location() {   //창고 스피너
        Thread workThd_dbQuery_get_storage_location = new Thread() {
            public void run() {

                String sql = " exec XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_GET_COMBODATA ";
                sql += " @FLAG = 'storage_location' ";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";

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
        workThd_dbQuery_get_storage_location.start();   //스레드 시작
        try {
            workThd_dbQuery_get_storage_location.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> listItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();


            String basic_sl_cd = getUserSl_cd(vUSER_ID);
            int basic_idx = 0;
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                if(vMINOR_CD.equals(basic_sl_cd)){
                    basic_idx = i;
                }

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                listItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, listItem);
            storage_location.setAdapter(adapter);

            //로딩시 기본값 세팅
            //storage_location.setSelection(adapter.getPosition(itemBase));
            storage_location.setSelection(basic_idx);
            //cmbBizPartner.setSelection();



            //콤보박스 클릭 이벤트 정의
            storage_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sl_cd_query = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
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


    private String getUserSl_cd(final String user_id) {
        String sl_cd ="";
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = "SELECT ISNULL(SL_CD,'') AS SL_CD FROM CA_USER_MASTER WHERE USER_ID = '"+user_id+"'";

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
            JSONArray ja = new JSONArray(sJson);

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                sl_cd          = jObject.getString("SL_CD");             //품번
            }

        } catch (Exception ex) {

        }
        return sl_cd;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case I39_SET_REQUEST_CODE:
                    String sign = data.getStringExtra("SIGN");
                    if (sign.equals("EXIT")) {
                        TGSClass.AlterMessage(this, "저장 되었습니다.");
                        finish();
                    } else if (sign.equals("ADD")) {
                        TGSClass.AlterMessage(this, "추가 되었습니다.");
                        getDatas();
                    }
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