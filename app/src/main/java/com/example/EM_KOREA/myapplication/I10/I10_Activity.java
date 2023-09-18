package com.example.EM_KOREA.myapplication.I10;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.EM_KOREA.myapplication.BaseActivity;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.TGSClass;

public class I10_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Button 선언 ==//
    private Button btn_stockyard, btn_warehouse, btn_item_query, btn_menu;

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== Request Code 상수 선언 ==//
    private final int I11_HDR_REQUEST_CODE = 1, I12_HDR_REQUEST_CODE = 2, I13_QUERY_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i10_sub_menu);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 정의 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 정의 ==//
        btn_stockyard   = (Button) findViewById(R.id.btn_stockyard);    // 1. 적치장재고조회
        btn_warehouse   = (Button) findViewById(R.id.btn_warehouse);    // 2. 창고재고조회
        btn_item_query  = (Button) findViewById(R.id.btn_item_query);   // 3. 품목재고조회(함안)
        btn_menu        = (Button) findViewById(R.id.btn_menu);
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_menu) {
                    //== 결과 값 돌려주기 ==//
                    Intent resultIntent = new Intent();
                    //== 부른 Activity에게 결과 값 반환 ==//
                    setResult(RESULT_CANCELED, resultIntent);
                    // 현재 Activity 종료
                    finish();
                } else if (v == btn_stockyard) {
                    Intent intent = TGSClass.ChangeView(getPackageName(), I11_HDR_Activity.class);
                    intent.putExtra("MENU_ID", "I11");
                    intent.putExtra("MENU_REMARK", vMenuRemark);
                    intent.putExtra("START_COMMAND", vStartCommand);
                    startActivityForResult(intent, I11_HDR_REQUEST_CODE);
                } else if (v == btn_warehouse) {
                    /*
                    Intent intent = TGSClass.ChangeView(getPackageName(), I12_HDR_Activity.class.getSimpleName());
                    intent.putExtra("MENU_ID", "I12");
                    intent.putExtra("MENU_REMARK", vMenuRemark);
                    intent.putExtra("START_COMMAND", vStartCommand);
                    startActivityForResult(intent, I12_HDR_REQUEST_CODE);
                     */
                } else if (v == btn_item_query) {
                    Intent intent = TGSClass.ChangeView(getPackageName(), I13_QUERY_Activity.class);
                    intent.putExtra("MENU_ID", "I13");
                    intent.putExtra("MENU_REMARK", vMenuRemark);
                    intent.putExtra("START_COMMAND", vStartCommand);
                    startActivityForResult(intent, I13_QUERY_REQUEST_CODE);
                }
            }
        };
        btn_stockyard.setOnClickListener(clickListener);
        btn_warehouse.setOnClickListener(clickListener);
        btn_item_query.setOnClickListener(clickListener);
        btn_menu.setOnClickListener(clickListener);
    }

    private void initializeData() {
        getGrant(vUSER_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case I11_HDR_REQUEST_CODE:
                    //Log.d("OK", "I11_HDR");
                    break;
                case I12_HDR_REQUEST_CODE:
                    //Log.d("OK", "I12_HDR");
                    break;
                case I13_QUERY_REQUEST_CODE:
                    //Log.d("OK", "I13_QUERY");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I11_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I11_HDR");
                    break;
                case I12_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I12_HDR");
                    break;
                case I13_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "I13_QUERY");
                    break;
                default:
                    break;
            }
        }
    }
}
