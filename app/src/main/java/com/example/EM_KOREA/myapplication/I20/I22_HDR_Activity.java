package com.example.EM_KOREA.myapplication.I20;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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


public class I22_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonCombo = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== Spinner에서 사용할 변수 선언 ==//
    private String sl_cd_query = "", item_acct_query = "";

    //== View 선언(Spinner) ==//
    private Spinner storage_location, item_acct;

    //== View 선언(EditText) ==//
    private EditText txt_Scan_location, txt_Scan_item;

    //== View 선언(Button) ==//
    private Button btn_query;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I22_DTL_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i22_hdr);

        this.initializeView();

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
        vStartCommand       = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        storage_location    = (Spinner) findViewById(R.id.storage_location);
        txt_Scan_location   = (EditText) findViewById(R.id.txt_Scan_location);
        item_acct           = (Spinner) findViewById(R.id.item_acct);
        txt_Scan_item       = (EditText) findViewById(R.id.txt_Scan_item);

        btn_query           = (Button) findViewById(R.id.btn_query);

        listview            = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeListener() {
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //쿼리중복 방지(쓰레드 중복 방지)
                if(!QueryOn){
                    return;
                }
                QueryOn = false;
                //중복방지 타이머 실행
                SetTimerTask();

                start();
            }
        });

        //Start(); //처음 조회할 시 데이터가 많아서 선태사항이 느린 경우를 대비해 조회조건을 사용자가 원할히 선택한 다음 조회할 수 있도록 조정 (조회 버튼 별도 사용)

        txt_Scan_location.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    txt_Scan_item.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    private void initializeData() { //초기화
        dbQuery_get_storage_location();
        dbQuery_get_item_acct();
    }

    private void dbQuery_get_storage_location() {   //창고 스피너
        Thread workThd_get_storage_location = new Thread() {
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

                //TGSClass.AlterMessage(getApplicationContext(), pParms.toString());
            }
        };
        workThd_get_storage_location.start();   //스레드 시작
        try {
            workThd_get_storage_location.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> listItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                listItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, listItem);
            storage_location.setAdapter(adapter);

            //로딩시 기본값 세팅
            storage_location.setSelection(adapter.getPosition(itemBase));
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

    private void dbQuery_get_item_acct() {      //작업장 스피너
        Thread workThd_get_item_acct = new Thread() {
            public void run() {
                String sql = " exec XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_GET_COMBODATA ";
                sql += " @FLAG = 'item_acct' ";
                sql += " ,@PLANT_CD = ''";

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

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                listItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, listItem);
            item_acct.setAdapter(adapter);

            //로딩시 기본값 세팅
            item_acct.setSelection(adapter.getPosition(itemBase));
            //cmbBizPartner.setSelection();

            //콤보박스 클릭 이벤트 정의
            item_acct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    item_acct_query = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
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

    private void start() {
        String txt_Scan_location_Data = txt_Scan_location.getText().toString();

        String txt_Scan_item_Data = txt_Scan_item.getText().toString();

        dbQuery(sl_cd_query, txt_Scan_location_Data, txt_Scan_item_Data, item_acct_query);

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I22_HDR_ListViewAdapter listViewAdapter = new I22_HDR_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I22_HDR item = new I22_HDR();

                item.LOCATION           = jObject.getString("LOCATION");            // 적치장
                item.ITEM_CD            = jObject.getString("ITEM_CD");             // 품목코드
                item.ITEM_NM            = jObject.getString("ITEM_NM");             // 품목명
                item.GOOD_ON_HAND_QTY   = jObject.getString("GOOD_ON_HAND_QTY");    // 수량
                item.ITEM_ACCT_NM       = jObject.getString("ITEM_ACCT_NM");        // 품목계정

                item.SL_CD              = jObject.getString("SL_CD");               // 업체코드
                item.PLANT_CD           = jObject.getString("PLANT_CD");            // 공장코드
                item.TRACKING_NO        = jObject.getString("TRACKING_NO");         // 트레킹번호

                listViewAdapter.addHDRItem(item);
            }

            TGSClass.AlterMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    I22_HDR vItem = (I22_HDR) parent.getItemAtPosition(position);

                    Intent intent = TGSClass.ChangeView(getPackageName(), I22_DTL_Activity.class);
                    intent.putExtra("HDR", vItem);
                    startActivityForResult(intent, I22_DTL_REQUEST_CODE);
                }
            });

            TGSClass.AlterMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlterMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlterMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String sl_cd_data, final String txt_Scan_location_data, final String txt_Scan_item_data, final String item_acct_data) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_STORAGE_I_GOODS_MOVEMENT_HDR_GET_LIST ";
                sql += " @SL_CD = '" + sl_cd_data + "' ";
                sql += " ,@LOCATION = '" + txt_Scan_location_data + "' ";
                sql += " ,@ITEM_CD = '" + txt_Scan_item_data + "' ";
                sql += " ,@ITEM_ACCT = '" + item_acct_data + "' ";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "' ";

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
            String SIGN = data.getStringExtra("SIGN");
            switch (requestCode) {
                case I22_DTL_REQUEST_CODE:
                    //Log.d("OK", "I11_DTL");
                    if (SIGN.equals("EXIT")) {
                        finish();
                    } else if (SIGN.equals("ADD")) {

                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I22_DTL_REQUEST_CODE:
                    //Log.d("CANCELED", "I11_DTL");
                    break;
                default:
                    break;
            }
        }
    }
}

