package com.example.EM_KOREA.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class TestProgressDlg extends AsyncTask<Integer, String, Integer> {

    private ProgressDialog mDlg;

    private Context mCtx;
    private long mSec;

    // 생성자
    public TestProgressDlg(Context ctx, long sec) {
        mCtx = ctx;
        mSec = sec;
    }

    public TestProgressDlg(Context ctx) {
        mCtx = ctx;
        mSec = 100;
    }

    @Override
    protected void onPreExecute() {
        // ProgressDialog 세팅
        mDlg = new ProgressDialog(mCtx);
        // 스타일 설정
        mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 프로그레스 다이얼로그 나올 때 메시지 설정.
        mDlg.setMessage("시 작");
        // 세팅된 다이얼로그를 보여줌.
        mDlg.show();

        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        // 프로그레스바 최대치가 몇인지 설정하는 변수
        final int taskCnt = params[0];
        // 프로그레스바 최대치 설정
        publishProgress("max", Integer.toString(taskCnt));

        for (int i = 0; i < taskCnt; i++) {
            try {
                Thread.sleep(mSec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 프로그레스바 현재 진행상황 설정
            publishProgress("progress", Integer.toString(i),
                    "Task " + Integer.toString(i) + " number");
        }
        // PostExecute로 리턴
        return taskCnt;
    }

    // 프로그래스가 업데이트 될 때 호출
    @Override
    protected void onProgressUpdate(String... values) {
        if (values[0].equals("progress")) {
            mDlg.setProgress(Integer.parseInt(values[1]));
            mDlg.setMessage(values[2]);
        } else if (values[0].equals("max")) {
            mDlg.setMax(Integer.parseInt(values[1]));
        }
    }

    // Backgroud에서 처리가 완료되면 호출
    @Override
    protected void onPostExecute(Integer i) {
        // 다이얼로그를 없앰
        mDlg.dismiss();
        Toast.makeText(mCtx, Integer.toString(i) + " total sum", Toast.LENGTH_LONG).show();
    }


}
