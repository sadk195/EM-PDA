package com.example.EM_KOREA.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class TestProgressBarActivity extends AppCompatActivity {

    private EditText et_progressbar;
    private ProgressBar progressbar;

    private Button btnOK, btnCancel;

    private int value;
    private Handler handle;
    private int cnt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //== 상태바 제거 (전체화면 모드) ==//
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_progressbar_test);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        et_progressbar  = (EditText) findViewById(R.id.et_progressbar);
        progressbar     = (ProgressBar) findViewById(R.id.progressbar);

        btnOK           = (Button) findViewById(R.id.btnOK);
        btnCancel       = (Button) findViewById(R.id.btnCancel);
    }

    private void initializeListener() {
        handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (cnt < value) {
                    cnt++;
                    progressbar.setProgress(cnt);
                    TestProgressBarActivity.this.sendMsg();
                } else {
                    handle.removeCallbacksAndMessages(null);
                    TGSClass.AlterMessage(getApplicationContext(), "진행완료", 1000);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        };
    }

    private void initializeData() {

    }

    //== 동작 버튼 클릭 ==//
    public void mOK(View v) {
        value = Integer.parseInt(et_progressbar.getText().toString());
        progressbar.setMax(value);
        cnt = 0;

        sendMsg();

        /*
        (new Thread(new Runnable() {
            @Override
            public void run() {
                final int prog = 0;
                int cnt = 0;
                while (prog < value) {
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress_bar.setProgress(prog);

                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        })).start();
        progress_bar.setProgress(value);
        */
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

    public void sendMsg() {
        Message msg = new Message();
        // 0.01초에 한번씩 Handler로 메세지를 전송
        handle.sendMessageDelayed(msg, 10);
    }
}
