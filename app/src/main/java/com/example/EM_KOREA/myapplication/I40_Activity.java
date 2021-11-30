package com.example.EM_KOREA.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class I40_Activity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(Button) ==//
    private Button btn_workplace_good, btn_workplace_bad, btn_workplace_ect, btn_workplace_status;
    private Button btn_menu;

    //== Grant 관련 변수 ==//
    private String ADMIN_CHK = "N", W_OUT = "N", I41 = "N", I42 = "N", I43 = "N", I44 = "N";     //Grant

    //== ActivityForResult 관련 변수 ==//
    private final int I41_HDR_REQUEST_CODE = 1, I42_HDR_REQUEST_CODE = 2, I43_QUERY_REQUEST_CODE = 3, I44_QUERY_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i40_sub_menu);

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

        //== ID값 바인딩 ==//
        btn_workplace_good      = (Button) findViewById(R.id.btn_workplace_good);           // 1. 작업장 반입(양품)
        btn_workplace_bad       = (Button) findViewById(R.id.btn_workplace_bad);            // 2. 작업장 반입(불량품)
        btn_workplace_ect       = (Button) findViewById(R.id.btn_workplace_ect);            // 3. 작업장 반입(기타)
        btn_workplace_status    = (Button) findViewById(R.id.btn_workplace_status);         // 4. 작업장 반입현황
        btn_menu                = (Button) findViewById(R.id.btn_menu);                     // 메뉴
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_menu:
                        // 저장 후 결과 값 돌려주기
                        Intent resultIntent = new Intent();
                        // 부른 Activity에게 결과 값 반환
                        setResult(RESULT_CANCELED, resultIntent);
                        // 현재 Activity 종료
                        finish();
                        break;
                    case R.id.btn_workplace_good:
                        /*
                        if (start_grant("I41")) {
                            String sMenuName = "";

                            Intent intent = TGSClass.ChangeView(getPackageName(), I41_HDR_Activity.class.getSimpleName());
                            intent.putExtra("MENU_ID", "I41");
                            intent.putExtra("MENU_NM", sMenuName);
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I41_HDR_REQUEST_CODE);
                        }
                         */
                        break;
                    case R.id.btn_workplace_bad:
                        /*
                        if (start_grant("I42")) {
                            String sMenuName = "";

                            Intent intent = TGSClass.ChangeView(getPackageName(), I42_HDR_Activity.class.getSimpleName());
                            intent.putExtra("MENU_ID", "I42");
                            intent.putExtra("MENU_NM", sMenuName);
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I42_HDR_REQUEST_CODE);
                        }
                         */
                        break;
                    case R.id.btn_workplace_ect:
                        /*
                        if (start_grant("I43")) {
                            String sMenuName = "";

                            Intent intent = TGSClass.ChangeView(getPackageName(), I43_HDR_Activity.class.getSimpleName());
                            intent.putExtra("MENU_ID", "I43");
                            intent.putExtra("MENU_NM", sMenuName);
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I43_HDR_REQUEST_CODE);
                        }
                         */
                        break;
                    case R.id.btn_workplace_status:
                        /*
                        if (start_grant("I44")) {
                            String sMenuName = "";

                            Intent intent = TGSClass.ChangeView(getPackageName(), I44_HDR_Activity.class.getSimpleName());
                            intent.putExtra("MENU_ID", "I44");
                            intent.putExtra("MENU_NM", sMenuName);
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I44_HDR_REQUEST_CODE);
                        }
                         */
                        break;
                    default:
                        break;
                }
            }
        };
        btn_workplace_good.setOnClickListener(clickListener);       // 1. 작업장 반입(양품)
        btn_workplace_bad.setOnClickListener(clickListener);        // 2. 작업장 반입(불량품)
        btn_workplace_ect.setOnClickListener(clickListener);        // 3. 작업장 반입(기타)
        btn_workplace_status.setOnClickListener(clickListener);     // 4. 작업장 반입현황
        btn_menu.setOnClickListener(clickListener);                 // 메뉴
    }

    private void initializeData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case I41_HDR_REQUEST_CODE:
                    //Log.d("OK", "I41_HDR");
                    break;
                case I42_HDR_REQUEST_CODE:
                    //Log.d("OK", "I42_HDR");
                    break;
                case I43_QUERY_REQUEST_CODE:
                    //Log.d("OK", "I43_QUERY");
                    break;
                case I44_QUERY_REQUEST_CODE:
                    //Log.d("OK", "I44_QUERY");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I41_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I41_HDR");
                    break;
                case I42_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I42_HDR");
                    break;
                case I43_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "I43_QUERY");
                    break;
                case I44_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "I44_QUERY");
                    break;
                default:
                    break;
            }
        }
    }
}
