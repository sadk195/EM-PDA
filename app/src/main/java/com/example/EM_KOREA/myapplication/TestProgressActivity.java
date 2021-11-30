package com.example.EM_KOREA.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

public class TestProgressActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnProgressDlg, mBtnLarge, mBtnMid, mBtnSmall, mBtnStick;
    private Button mBtnOk, mBtnCancel;

    private AsyncTask<Integer, String, Integer> mProgressDlg;

    private ProgressBar mProgressLarge, mProgressMid, mProgressSmall, mProgressStick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //== 상태바 제거 (전체화면 모드) ==//
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_progress_test);

        mBtnProgressDlg     = (Button) findViewById(R.id.btnProgressDialog);
        mBtnLarge           = (Button) findViewById(R.id.btnProgressLarge);
        mBtnMid             = (Button) findViewById(R.id.btnProgressMid);
        mBtnSmall           = (Button) findViewById(R.id.btnProgressSmall);
        mBtnStick           = (Button) findViewById(R.id.btnProgressStick);

        mBtnOk              = (Button) findViewById(R.id.btnOk);
        mBtnCancel          = (Button) findViewById(R.id.btnCancel);


        mProgressLarge      = (ProgressBar) findViewById(R.id.progressBar1);
        mProgressMid        = (ProgressBar) findViewById(R.id.progressBar2);
        mProgressSmall      = (ProgressBar) findViewById(R.id.progressBar3);
        mProgressStick      = (ProgressBar) findViewById(R.id.progressBar4);

        mBtnProgressDlg.setOnClickListener(this);
        mBtnLarge.setOnClickListener(this);
        mBtnMid.setOnClickListener(this);
        mBtnSmall.setOnClickListener(this);
        mBtnStick.setOnClickListener(this);

        mBtnOk.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mProgressLarge.setVisibility(ProgressBar.GONE);
        mProgressMid.setVisibility(ProgressBar.GONE);
        mProgressSmall.setVisibility(ProgressBar.GONE);
        mProgressStick.setVisibility(ProgressBar.GONE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnProgressDialog:
                mProgressDlg = new TestProgressDlg(this).execute(100);
                break;

            case R.id.btnProgressLarge:
                mProgressLarge.setVisibility(ProgressBar.VISIBLE);
                // 게이지가 올라가거나 화살표가 돌아가는 것이 작업이 완료될 때 까지 멈추지 않게(boolean)
                mProgressLarge.setIndeterminate(true);
                // 최대치 설정
                mProgressLarge.setMax(100);
                break;

            case R.id.btnProgressMid:
                mProgressMid.setVisibility(ProgressBar.VISIBLE);
                mProgressMid.setIndeterminate(true);
                mProgressMid.setMax(100);
                break;

            case R.id.btnProgressSmall:
                mProgressSmall.setVisibility(ProgressBar.VISIBLE);
                mProgressSmall.setIndeterminate(true);
                mProgressSmall.setMax(100);
                break;

            case R.id.btnProgressStick:
                mProgressStick.setVisibility(ProgressBar.VISIBLE);
                mProgressStick.setIndeterminate(true);
                mProgressStick.setMax(100);
                break;

            default:
                finish();
                break;
        }
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
