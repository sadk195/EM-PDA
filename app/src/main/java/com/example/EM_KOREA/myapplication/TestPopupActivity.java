package com.example.EM_KOREA.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class TestPopupActivity extends AppCompatActivity {

    private Button btnOK, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //== 상태바 제거 (전체화면 모드) ==//
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup_test);

        btnOK       = (Button) findViewById(R.id.btnOK);
        btnCancel   = (Button) findViewById(R.id.btnCancel);
    }

    //== 동작 버튼 클릭 ==//
    public void mOK(View v) {
        finish();
    }

    //== 취소 버튼 클릭 ==//
    public void mCancel(View v) {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //== 바깥 레이어 클릭 시 닫히지 않도록 ==//
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //== 안드로이드 뒤로가기 버튼 막기 ==//
        return;
    }
}
