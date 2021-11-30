package com.example.EM_KOREA.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class M21_Activity extends AppCompatActivity {

    private final long INTERVAL_TIME = 500;  //간격
    private long inputEnterPressedTime = 0;  //엔터키 입력 시간.
    public String sJson = "";
    public ImageView img_barcode;
    private IntentIntegrator qrScan;
    public EditText txt_Scan;
    public String sJobCode;
    public String sMenuRemark;
    public MySession global;
    public String Plant_CD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String vMenuName = getIntent().getStringExtra("MENU_NM");
        sMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        sJobCode = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        TextView lbl_view_title = findViewById(R.id.view_title);
        lbl_view_title.setText(vMenuName);

        txt_Scan = findViewById(R.id.txt_Scan);
        txt_Scan.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return SetSCanData(sJobCode);
                }
                return false;
            }

        });

        txt_Scan.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Log.d("x5",s.toString());
                if(!s.equals("") ) {
                    //do your work here
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                if(txt_Scan.getText().toString().length() >=  20) {
                    if (!SetSCanData(sJobCode)) return;
                }
            }
        });

        qrScan = new IntentIntegrator(this);
        img_barcode = findViewById(R.id.img_barcode);

        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.setPrompt("바코드를 스캔하세요");
                qrScan.setOrientationLocked(true);
                qrScan.initiateScan();
            }
        });
    }

    private void ChangeView(String pINSP_REQ_NO, String pMenuRemark)
    {
        Intent intent = TGSClass.ChangeView(getPackageName(),M22_Activity.class.getSimpleName());
        intent.putExtra("INSP_REQ_NO",pINSP_REQ_NO);
        intent.putExtra("MENU_REMARK",pMenuRemark);
        intent.putExtra("JOB_CD",sJobCode);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                TGSClass.AlterMessage(getApplicationContext(),"취소!");
            } else {
                //qrcode 결과가 있으면
                TGSClass.AlterMessage(getApplicationContext(),"스캔완료");

                txt_Scan.setText(result.getContents());
                SetSCanData(sJobCode);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //스캔 데이터 값 처리.
    private  boolean SetSCanData(String pJobCD)
    {
        String vScanText = txt_Scan.getText().toString();

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - inputEnterPressedTime;

        if (0 <= intervalTime && INTERVAL_TIME >= intervalTime) {
            /* FINISH_INTERVAL_TIME 밀리 초 안에 엔터키 이벤트가 발생하면, 뒤에 생긴 이벤트 무시.*/
            return  false;
        }
        else
        {
            inputEnterPressedTime = tempTime;
        }

        //스캔 데이터 생성 클래스
        ScanData scan = new ScanData(vScanText);

        String vINSP_REQ_NO = scan.getm_INSP_REQ_No();

        txt_Scan.setText(null);

        ChangeView(vINSP_REQ_NO, sMenuRemark);

        return  true;
    }
}
