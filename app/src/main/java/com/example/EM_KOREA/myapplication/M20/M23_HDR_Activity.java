package com.example.EM_KOREA.myapplication.M20;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.EM_KOREA.myapplication.BaseActivity;
import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.ScanData;
import com.example.EM_KOREA.myapplication.TGSClass;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class M23_HDR_Activity extends BaseActivity {

    private final long INTERVAL_TIME = 500;  //간격
    private long inputEnterPressedTime = 0;  //엔터키 입력 시간.

    //== JSON 선언 ==//
    private String sJson;

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView lbl_view_title;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText txt_Scan;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int M23_DTL_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        //== ID 값 바인딩 ==//
        lbl_view_title  = (TextView) findViewById(R.id.view_title);
        txt_Scan        = (EditText) findViewById(R.id.txt_Scan);
        img_barcode     = (ImageView) findViewById(R.id.img_barcode);
    }

    private void initializeListener() {
        txt_Scan.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (txt_Scan.getText().toString().equals("")) {
                        TGSClass.AlterMessage(getApplicationContext(), "제조오더번호를 입력하십시오.");
                        return false;
                    }
                    return setScanData();
                }
                return false;
            }

        });

        txt_Scan.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Log.d("x5", s.toString());
                if (!s.equals("")) {
                    //do your work here
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                if (txt_Scan.getText().toString().length() >= 20) {
                    if (!setScanData()) return;
                }
            }
        });

        img_barcode.setOnClickListener(qrClickListener);
    }

    private void initializeData() {
        lbl_view_title.setText(vMenuNm);
    }

    private void changeView(String REQ_NO, String pMenuRemark) {
        Intent intent = TGSClass.ChangeView(getPackageName(), M23_DTL_Activity.class);
        intent.putExtra("PRODT_ORDER_NO", REQ_NO);
        intent.putExtra("MENU_REMARK", pMenuRemark);
        intent.putExtra("START_COMMAND", vStartCommand);
        startActivityForResult(intent, M23_DTL_REQUEST_CODE);
    }

    //스캔 데이터 값 처리.
    private boolean setScanData() {

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - inputEnterPressedTime;

        if (0 <= intervalTime && INTERVAL_TIME >= intervalTime) {
            /* FINISH_INTERVAL_TIME 밀리 초 안에 엔터키 이벤트가 발생하면, 뒤에 생긴 이벤트 무시.*/
            return false;
        } else {
            inputEnterPressedTime = tempTime;
        }

        String txt_Scan_st = txt_Scan.getText().toString();

        if(txt_Scan_st.length() < 9){
            TGSClass.AlterMessage(getApplicationContext(), "제조오더번호를 확인하십시오.");
            return false;
        }

        txt_Scan_st = txt_Scan_st.substring(0, 8);


        dbQuery_get_REQ_NO_INFO(txt_Scan_st);         //검사요청번호

        if (sJson.equals("[]") || sJson.equals("[{\"Column1\":\"N\"}]") ) {
            TGSClass.AlterMessage(getApplicationContext(), "검색 된 제조오더번호 결과가 없습니다.");
            return false;
        }

        txt_Scan.setText(null);

        changeView(txt_Scan_st, vMenuRemark);

        return true;
    }

    private void dbQuery_get_REQ_NO_INFO(String order_no) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String txt_Scan_st = txt_Scan.getText().toString();

                String sql = " EXEC XUSP_MES_PRODT_ORDER_SET_CHECK ";
                sql += "  @PLANT_CD = '" + vPLANT_CD + "'";
                sql += "  ,@PRODT_ORDER_NO = '" + order_no + "'";
                System.out.println("sql:"+sql);

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

        } catch (InterruptedException ex) {

        }

        //Start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
         if (result != null) {
         //qrcode 가 없으면
         if (result.getContents() == null)
         {
         TGSClass.AlterMessage(getApplicationContext(),"취소!");
         }
         else {
         //qrcode 결과가 있으면
         TGSClass.AlterMessage(getApplicationContext(),"스캔완료");

         txt_Scan.setText(result.getContents());
         SetSCanData(sJobCode);
         }

         } else {
         super.onActivityResult(requestCode, resultCode, data);
         }
         */
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case M23_DTL_REQUEST_CODE:
                    String sign = data.getStringExtra("SIGN");
                   if (sign.equals("OK")) {
                       //TGSClass.AlterMessage(this, "저장 되었습니다.");
                       finish();
                   }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case M23_DTL_REQUEST_CODE:
                    TGSClass.AlterMessage(getApplicationContext(), "취소!");
                    break;
                default:
                    break;
            }
        }
    }
}
