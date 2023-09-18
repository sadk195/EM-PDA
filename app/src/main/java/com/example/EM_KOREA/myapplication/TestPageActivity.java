package com.example.EM_KOREA.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TestPageActivity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(RelativeLayout) ==//
    private RelativeLayout r_layout1_1, r_layout1_2;

    //== View 선언(Button) ==//
    private Button btn_popup_test, btn_progressBar_test, btn_progress_test, btn_test;

    //== Activity 관련 변수 선언 ==//
    private final int POPUP_REQUEST_CODE = 1, PROGRESSBAR_REQUEST_CODE = 2, TEST_REQUEST_CODE = 3, TEST_PROGRESSBAR_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== Intent 값 바인딩 ==//

        //== ID 값 바인딩 ==//
        r_layout1_1             = (RelativeLayout) findViewById(R.id.RelativeLayout1_1);
        r_layout1_2             = (RelativeLayout) findViewById(R.id.RelativeLayout1_2);

        btn_popup_test          = (Button) findViewById(R.id.btn_popup_test);
        btn_progressBar_test    = (Button) findViewById(R.id.btn_progressBar_test);
        btn_progress_test       = (Button) findViewById(R.id.btn_progress_test);
        btn_test                = (Button) findViewById(R.id.btn_test);
    }

    private void initializeListener() {
        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_popup_test:
                        Intent intent = TGSClass.ChangeView(getPackageName(), TestPopupActivity.class);
                        startActivityForResult(intent, POPUP_REQUEST_CODE);
                        break;
                    case R.id.btn_progressBar_test:
                        Intent intent1 = TGSClass.ChangeView(getPackageName(), TestProgressBarActivity.class);
                        startActivityForResult(intent1, PROGRESSBAR_REQUEST_CODE);
                        break;
                    case R.id.btn_progress_test:
                        Intent intent3 = TGSClass.ChangeView(getPackageName(), TestProgressActivity.class);
                        startActivityForResult(intent3, TEST_PROGRESSBAR_REQUEST_CODE);
                        break;
                    case R.id.btn_test:
                        Intent intent2 = TGSClass.ChangeView(getPackageName(), TestXmlPage.class);
                        startActivityForResult(intent2, TEST_REQUEST_CODE);
                        break;
                }
            }
        };
        btn_popup_test.setOnClickListener(btnListener);
        btn_progressBar_test.setOnClickListener(btnListener);
        btn_progress_test.setOnClickListener(btnListener);
        btn_test.setOnClickListener(btnListener);
    }

    private void initializeData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case POPUP_REQUEST_CODE:
                    TGSClass.AlterMessage(getApplicationContext(), "POPUP_OK");
                    break;
                case PROGRESSBAR_REQUEST_CODE:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case POPUP_REQUEST_CODE:
                    TGSClass.AlterMessage(getApplicationContext(), "POPUP_CANCEL");
                    break;
                case PROGRESSBAR_REQUEST_CODE:
                    TGSClass.AlterMessage(getApplicationContext(), "PROGRESS_CANCEL");
                    break;
            }
        }
    }
}