package com.example.EM_KOREA.myapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;

import org.ksoap2.serialization.PropertyInfo;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    //== JSON 선언 ==//
    protected static String sJson_grant = "", sJson_menu_grant = "";

    //== SESSION 선언 ==//
    protected static MySession global;

    //== SESSION에서 받을 변수 선언 ==//
    protected static String vUSER_ID, vPLANT_CD, vUNIT_CD;

    //== Progress Dialog 정의 ==//
    protected static ProgressDialog mProgressDialog;
    protected static AsyncTask<Integer, String, Integer> mDlg;

    //== QR 스캔 관련 변수 ==//
    protected IntentIntegrator qrScan;

    //== 날짜관련 변수 선언 ==//
    protected DateFormat df;

    //== 숫자 포맷 ==//
    protected DecimalFormat decimalForm;

    //== 저장 시 BL 후 메시지 ==//
    protected String result_msg = "";

    /*
    //== 권한 ==//
    public static String ADMIN = "N";
    public static String M10 = "N", M30 = "N", P10 = "N", I10 = "N", I20 = "N", I30 = "N";
    public static String I40 = "N", I50 = "N", I60 = "N", I70 = "N", M20 = "N", S10 = "N";
    */

    protected BaseActivity() { /* compiled code */ }

    protected void init() {

        //== SESSION 정의 ==//
        global = (MySession) getApplication();

        //== SESSION 값 바인딩 ==//
        vUSER_ID = global.getLoginString();
        vPLANT_CD = global.getPlantCDString();
        vUNIT_CD = global.getmUnitCDString();

        //테스트용
        vPLANT_CD = "H1";

        //사용자 정보 오류시 앱 다시시작
        if(global == null || vUSER_ID ==null || vUSER_ID.equals("")){
            ErrorList_Popup Error_Popup = new ErrorList_Popup(this,"사용자 정보 오류",
                    " 사용자 정보가 유효하지 않아 앱을 재시작 합니다. \n 동일한 오류가 지속될 경우 관리자에게 문의하시기 바랍니다");
            Error_Popup.show();
        }
        //== QR코드 ==//
        qrScan = new IntentIntegrator(this);

        //== DateFormat init ==//
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        //== DecimalFormat init ==//
        decimalForm = new DecimalFormat("###,###");
    }

    //== 권한 가져오기 ==//
    protected static void getMenuGrant() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_getMajorMenuGrant = new Thread() {
            public void run() {
                String sql = " exec USP_MENU_LOAD";
                sql += " @FLAG = '0'";                      //== 0 : 사용하는메뉴만, 1 : 전체 : 1(기본값 0)
                sql += ", @USER_ID = '" + vUSER_ID + "'";   //== USER_ID
                sql += ", @PAR_ID = 'APK'";                 //== APK
                sql += ", @LANG_CD = 'KO'";                 //== KO

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_menu_grant = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        wkThd_getMajorMenuGrant.start();   //스레드 시작
        try {
            wkThd_getMajorMenuGrant.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    //== 권한 가져오기 ==//
    protected static void getGrant(final String USER_ID) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_getGrant = new Thread() {
            public void run() {
                String sql = " exec XUSP_APK_USER_GRANT_CHK ";
                sql += " @USER_ID = '" + USER_ID + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_grant = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        wkThd_getGrant.start();   //스레드 시작
        try {
            wkThd_getGrant.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    //== 로딩 Dialog 시작 ==//
    protected static void progressStart(Context context, String title, String msg) {
        if (msg == null) msg = "잠시만 기다려주세요..";
        mProgressDialog = ProgressDialog.show(
                context, title, msg
        );
    }

    protected static void progressStart(Context context, String title) {
        progressStart(context, title, null);
    }

    protected static void progressStart(Context context) {
        progressStart(context, null);
    }

    //== 로딩 Dialog 끝 ==//
    protected static void progressEnd() {
        mProgressDialog.dismiss();
    }

    //== 로딩화면2 만들기
    protected void loadingStart(final Context context) {
        // 로딩
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog = new ProgressDialog(context);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage("잠시만 기다려 주세요.");
                        mProgressDialog.show();
                    }
                }
                , 0);
    }

    //== 로딩화면2 닫기
    protected void loadingEnd() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                }
                , 0);
    }

    //== 날짜 팝업 ==//
    protected void openPopupDate(final View v, final EditText et, final Calendar c) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                et.setText(df.format(c.getTime()));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public View.OnClickListener qrClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            qrScan.setPrompt("VISS 바코드를 스캔하세요");
            qrScan.setOrientationLocked(true);
            qrScan.initiateScan();
        }
    };

    /**
     * 텍스트뷰 생성
     private void createTextView(LinearLayout layout, String txt, int i) {
     // 1. 텍스트뷰 객체생성
     TextView tv = new TextView(getApplicationContext());
     // 2. 텍스트뷰에 들어갈 문자설정
     tv.setText(txt);
     // 3. 텍스트뷰 글자크기, 색상 설정
     tv.setTextSize(FONT_SIZE); // 텍스트 크기
     tv.setTextColor(getResources().getColor(R.color.design_default_color_primary));
     //tv.setTextColor(ContextCompat.getColor(this, R.color.design_default_color_primary));
     // 4. 텍스트뷰 글자타입 설정
     //tv.setTypeface(null, Typeface.BOLD);
     // 5. 텍스트뷰 ID 설정
     tv.setId(i);
     // 6. 레이아웃 설정
     LinearLayout.LayoutParams param
     = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
     param.rightMargin = 15;
     // 7. 설정한 레이아웃 텍스트뷰에 적용
     tv.setLayoutParams(param);
     // 8. 텍스트뷰 백그라운드색상 설정
     //tv.setBackgroundColor(Color.rgb(184,236,184));
     // 9. 생성 및 설정된 텍스트뷰 레이아웃에 적용
     layout.addView(tv);
     }
     */
}
