package com.example.EM_KOREA.myapplication.I30;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class I37_DEL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJson_hdr = "", sJson_DataSET = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I37_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView item_cd,item_nm;

    //== View 선언(ListView) ==//
    private ListView listview;

    private String tx_prodt_no,tx_item_cd,tx_item_nm;


    //== View 선언(Button) ==//
    private Button btn_del;

    private I37_DEL selected_item = new I37_DEL();

    private int selidx =-1;

    private boolean delflag= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i37_del);

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

        tx_prodt_no     = getIntent().getStringExtra("PRODT_NO"); //다음 페이지에 가지고 넘어갈 코드
        tx_item_cd     = getIntent().getStringExtra("ITEM_CD"); //다음 페이지에 가지고 넘어갈 코드
        tx_item_nm     = getIntent().getStringExtra("ITEM_NM"); //다음 페이지에 가지고 넘어갈 코드

        //== HDR 값 바인딩 ==//
        vGetHDRItem         = (I37_HDR)getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        item_cd             = (TextView) findViewById(R.id.txt_item_cd);
        item_nm             = (TextView) findViewById(R.id.txt_item_nm);



        listview            = (ListView) findViewById(R.id.listOrder);

        btn_del             = (Button) findViewById(R.id.btn_del);
    }

    private void initializeListener() {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                selected_item = (I37_DEL) parent.getItemAtPosition(position);
                selidx = position;
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selidx < 0){
                    TGSClass.AlterMessage(getApplicationContext(),  "삭제할 데이터를 선택하세요.");
                    return;
                }
                delflag = true;
                dbDelete(selected_item);

                start();
            }
        });
    }

    private void initializeData() {
        // 가져온 값 할당
        item_cd.setText(tx_item_cd);
        item_nm.setText(tx_item_nm);

        start();
    }

    private void start() {
        dbQuery(item_cd.getText().toString());

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I37_DEL_ListViewAdapter listViewAdapter = new I37_DEL_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I37_DEL item = new I37_DEL();

                item.RECORD_NO      = jObject.getString("RECORD_NO");  //
                item.GOOD_QTY     = jObject.getString("GOOD_QTY");  //
                item.SL_CD        = jObject.getString("SL_CD");  //
                item.SL_NM        = jObject.getString("SL_NM");  //
                item.LOCATION  = jObject.getString("LOCATION");  //

                listViewAdapter.addDELItem(item);
            }
            listview.setAdapter(listViewAdapter);


        } catch (JSONException ex) {
            TGSClass.AlterMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlterMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String vitem_cd) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_PDA_I37_MVMNT_GET_ITEM ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@ITEM_CD = '" + vitem_cd +"' ";

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
        }
        catch (InterruptedException ex) {

        }
    }

    private void dbDelete(I37_DEL del_data) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DEL ";
                sql += " @RECORD_NO = '" + del_data.getRECORD_NO() + "', ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "', ";
                sql += " @ITEM_CD = '" + tx_item_cd + "', ";
                sql += " @SL_CD = '" + del_data.getSL_CD() + "', ";
                sql += " @MOVE_QTY = '" + del_data.getGOOD_QTY() + "' ";
                //sql += " @LOCATION = '" + del_data.getLOCATION() + "' ";

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
        }
        catch (InterruptedException ex) {

        }
    }

    private void setResult(boolean bool, String val) {
        // 저장 후 결과 값 돌려주기
        Intent resultIntent = new Intent();

        if (bool) {
            // 결과처리 후 부른 Activity에 보낼 값
            resultIntent.putExtra("SIGN", val);
            // 부른 Activity에게 결과 값 반환
            setResult(RESULT_OK, resultIntent);
        } else {
            // 부른 Activity에게 결과 값 반환
            setResult(RESULT_CANCELED, resultIntent);
        }

        // 현재 Activity 종료 (validation에 걸려 return되어도 무조건 현재 activity가 꺼진다. 수정이 필요)
        finish();
    }


    //뒤로가기 누른 경우
    //현재는 닫기가 뒤로가기 기능에서만 구현됨 추가로 닫기를 만들경우 닫기버튼에 같은기능 추가해야함
    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        if(delflag){
            resultIntent.putExtra("SIGN", "DEL");
        }
        else{
            resultIntent.putExtra("SIGN", "SQL");
        }
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }


}