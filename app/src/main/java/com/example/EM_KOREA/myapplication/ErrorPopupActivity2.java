package com.example.EM_KOREA.myapplication;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ErrorPopupActivity2 extends Activity {

    public TextView lbl_error_msg;
    protected Timer timer = new Timer();
    protected TimerTask timerTask;
    boolean col = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_popup2);

        //== ID값 바인딩 ==//
        lbl_error_msg = findViewById(R.id.lbl_error_msg);

        //== 이전 Intent에서 MSG라고 받은 값을 가져온다 ==//
        String vErrorMSG = getIntent().getStringExtra("MSG");

        //== 가져온 값을 바인딩 ==//
        lbl_error_msg.setText(vErrorMSG);

        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {

                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        };

        // timer 실행
        timer.schedule(timerTask, 0, 1000);


        //lbl_error_msg.setBackgroundColor(Color.parseColor("#ED1C24"));

    }


    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            // 원래 하려던 동작 (UI변경 작업 등)
           /* if(col){
                lbl_error_msg.setBackgroundColor(Color.parseColor("#ED1C24"));
                col = false;
            }
            else{
                lbl_error_msg.setBackgroundColor(Color.parseColor("#FFFF00"));
                col = true;
            }*/
        }
    };
    public void mOnClose(View v){
        //== 액티비티(팝업) 닫기(앱 종료) ==//
        this.finish();
    }
}

