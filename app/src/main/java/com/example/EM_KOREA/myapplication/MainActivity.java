package com.example.EM_KOREA.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //== JSON 선언 ==//
    private String strJson, strJson_Auto_LogIn, strJsonVersion;

    //== SESSION 선언 ==//
    protected MySession global;

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuNm, vMenuRemark, vJobCd;

    //== View 선언(ImageView) ==//
    private ImageView img_user, img_logo;

    //== View 선언(EditText) ==//
    private EditText txtID, txtPWD;

    //== View 선언(TextView) ==//
    private TextView lbl_device;

    //== View 선언(Button) ==//
    private Button btnLogIn, btnEnd;

    //== Check 변수 선언 ==//
    private boolean permissionChk, updateChk, chkLogIn = false;

    //== Tag ==//
    private static final String TAG = "MyTag";

    //== 상수 선언 ==//
    private final long FINISH_INTERVAL_TIME = 2000;
    private final int REQUEST_CODE_WRITE_STORAGE_PERMISSIONS = 1001;
    private long backPressedTime = 0;
    private final int DOWNLOAD_ACTIVITY_REQUEST_CODE = 1;
    public int intVersionNo, sVersion = 5;

    //== String 선언 ==//
    private final String FILE_NAME = "EM_KOREA.apk";

    //== 변수 선언 ==//
    private String strClientIP, strClientHostNm, strErrorMsg;
    private String strPlantCD, strUSER_ID, strPWD;
    public String strVersionNm;
    private String DOWNLOAD_URL;

    //== 선언 ==//
    private int cntUserTouch = 0, cntLogoCnt = 0;
    private boolean blnUser = false, blnLogo = false;

    //== 키보드 관련 함수 선언 ==//
    InputMethodManager imm;

    //-- 다른 액티비티에서 현재 액티비티의 메소드를 호출할 수 있도록 하기 위해 정의 --//
    public static Context mContext;

    //-- 로그인 화면에서 뒤로가기를 눌렀을때 발생하는 이벤트 --//
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        //2초 안에 2번 클릭 시 프로그램 종료.
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            //super.finish();
            //-- 앱종료 --//
            finishAffinity();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "한번 더 누르면 프로그램이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        this.initializeView();

        this.initializeListener();

        this.initializeData();

        //-- 권한 체크 --//
        chkPermission();

        //-- ERP프로그램 "MES 자동로그인정보관리" 에 단말키 코드가 호스트명으로 존재하면 자동로그인 --//
        if (permissionChk && updateChk && (strErrorMsg == null)) {
            dbQuery_Auto_LogIn(strClientHostNm);
        }

        /*
        if (!strJson_Auto_LogIn.equals("[]")) {
            Intent intent = TGSClass.ChangeView(getPackageName(), MenuActivity.class.getSimpleName());
            intent.putExtra("pID", strClientHostNm);
            intent.putExtra("pDEVICE", strClientHostNm);
            intent.putExtra("pPLANT_CD", strPlantCD);
            //== Session에도 값 추가 ==//
            global.setLoginString(strClientHostNm);
            global.setUnitCDString(strClientHostNm);
            global.setPlantCDString(strPlantCD);
            startActivity(intent);
            finish();
        }
         */
        //-----------------------------------------------------------------------------------//
    }

    ////////////////////////////// 메소드 정의 ////////////////////////////////////////////////////////

    private void initializeView() {
        //== SESSION 정의 ==//
        global          = (MySession) getApplication();

        //== ID 값 바인딩 ==//
        img_user        = (ImageView) findViewById(R.id.img_user);
        img_logo        = (ImageView) findViewById(R.id.img_logo);

        txtID           = (EditText) findViewById(R.id.txt_id);
        txtPWD          = (EditText) findViewById(R.id.txt_pwd);
        btnLogIn        = (Button) findViewById(R.id.btn_Login);
        btnEnd          = (Button) findViewById(R.id.btn_End);
        lbl_device      = (TextView) findViewById(R.id.lbl_device_name);

        //-- 다른 액티비티에서 현재 액티비티의 메소드를 사용할 수 있도록 하기 위한 정의 --//
        mContext    = this;

        //-- 키보드 컨트롤 정의 --//
        imm         = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_End:
                        // finishAffinity();
                        Intent intent = TGSClass.ChangeView(getPackageName(), DownloadActivity.class);
                        startActivityForResult(intent, DOWNLOAD_ACTIVITY_REQUEST_CODE);
                        break;
                    case R.id.lbl_device_name:
                        //-- 단말기 고유코드를 클릭시 단말기 등록 요청 프로세스 작동 --//
                        dbSave_RequestRegister();
                        break;
                    case R.id.btn_Login:
                        // Log.d("click","1"); // 디버그화면에서 d로 확인하기

                        //strPlantCD = dbQuery(strClientHostNm); // 등록된 기기이면 공장 코드값을 불러옴.
                        if (strPlantCD.equals("") || strPlantCD == null) {
                            TGSClass.AlterMessage(getApplicationContext(), "등록되지 않은 기기입니다. 기기등록요청 하시길 바랍니다.", 1000);
                            return;
                        }
                        if (logIn_chk(txtID.getText().toString(), txtPWD.getText().toString()) == true) {
                            //-- 등록된 장비일 시 메뉴로 바로 이동 --//
                            logIn(strUSER_ID, strPWD);
                        }
                        break;
                }
            }
        };
        btnEnd.setOnClickListener(clickListener);
        lbl_device.setOnClickListener(clickListener);
        btnLogIn.setOnClickListener(clickListener);

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (strClientHostNm.equals("A4D4B20AE02") || strClientHostNm.equals("02000044556")) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (v == img_user) {
                                if (!blnUser) {
                                    if (cntUserTouch < 2) {
                                        cntUserTouch++;
                                    } else {
                                        blnUser = true;
                                        TGSClass.AlterMessage(getApplicationContext(), "잠금해제1");
                                    }
                                } else if (blnUser && blnLogo) {
                                    Intent intent = TGSClass.ChangeView(getPackageName(), TestPageActivity.class);
                                    startActivity(intent);
                                    cntUserTouch = 0;
                                    cntLogoCnt = 0;
                                    blnUser = false;
                                    blnLogo = false;
                                }
                            } else if (v == img_logo) {
                                if (!blnLogo && blnUser) {
                                    if (cntLogoCnt < 2) {
                                        cntLogoCnt++;
                                    } else {
                                        blnLogo = true;
                                        TGSClass.AlterMessage(getApplicationContext(), "잠금해제2");
                                    }
                                } else {
                                    blnUser = false;
                                    blnLogo = false;
                                    TGSClass.AlterMessage(getApplicationContext(), "모두잠금");
                                }
                            }
                            break;
                    }
                }

                return false;
            }
        };
        img_user.setOnTouchListener(touchListener);
        img_logo.setOnTouchListener(touchListener);

        /*
        //-- 단말기 등록 버튼 --//
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finishAffinity();
                Intent intent = TGSClass.ChangeView(getPackageName(), DownloadActivity.class);
                startActivityForResult(intent, DOWNLOAD_ACTIVITY_REQUEST_CODE);
            }
        });
        lbl_device.setText(strClientHostNm);

        //-- 본인의 맥주소를 눌렀을때 --//
        lbl_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-- 단말기 고유코드를 클릭시 단말기 등록 요청 프로세스 작동 --//
                dbSave_RequestRegister();
            }
        });

        //-- 로그인 버튼 --//
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("click","1"); // 디버그화면에서 d로 확인하기

                //strPlantCD = dbQuery(strClientHostNm); // 등록된 기기이면 공장 코드값을 불러옴.
                if (strPlantCD.equals("") || strPlantCD == null) {
                    TGSClass.AlterMessage(getApplicationContext(), "등록되지 않은 기기입니다. 기기등록요청 하시길 바랍니다.", 1000);
                    return;
                }

                if (logIn_chk(txtID.getText().toString(), txtPWD.getText().toString()) == true) {
                    //-- 등록된 장비일 시 메뉴로 바로 이동 --//
                    logIn(strUSER_ID, strPWD);
                }
            }
        });
        */
        /*
        EditText 이벤트
        //-- 아이디에서 확인 버튼을 눌렀을 때 --//
        txtID.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    txtPWD.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //-- 비밀번호에서 확인 버튼을 눌렀을 때 --//
        txtPWD.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // imm.hideSoftInputFromWindow(txtPWD.getWindowToken(), 0); // 키보드 숨기기
                    btnLogIn.requestFocus();
                    return true;
                }
                return false;
            }
        });

        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT :
                        //Log.d("ACT", "IME_ACTION_NEXT");
                        txtPWD.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE :
                        //Log.d("ACT", "IME_ACTION_DONE");
                        btnLogIn.requestFocus();
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
        txtID.setOnEditorActionListener(listener);
        txtPWD.setOnEditorActionListener(listener);
         */
    }

    private void initializeData() {
        strClientIP     = TGSClass.getLocalIpAddress();
        strClientHostNm = TGSClass.getMacAddress();

        System.out.println("strClientHostNm:"+strClientHostNm);
        //== SESSION에 값 저장 ==//
        global.setUserIPString(strClientIP);
        global.setUnitCDString(strClientHostNm);

        //== Check ==//
        permissionChk   = false;
        updateChk       = false;
    }

    //-- 앱 체크(appChk) --//
    public void appChk() {
        /* 현재 설치된 APP 의 버전 정보를 가져옵니다. */
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // e.printStackTrace();
            Log.w(TAG, "something error!!", e);
        }

        strVersionNm    = pInfo.versionName;
        intVersionNo    = pInfo.versionCode;

        /* 현재 설치된 APP 의 버전 정보와 다운로드 받을 경로 정보를 데이터 베이스에서 받아옵니다. */
        if (!getVersionFromServer()) {
            Intent error_intent = TGSClass.ChangeView(getPackageName(), ErrorPopupActivity.class);
            error_intent.putExtra("MSG", strErrorMsg);
            startActivity(error_intent);
            finish();
            return;
        }
    }

    //== 버전 체크(chkVersion) ==//
    public void chkVersion() {
        //-- 현재 설치된 앱 보다 새 버전의 앱 정보가 존재하면 --//
        if (intVersionNo < sVersion) {
            //-- 업데이트 파일이 존재한다는 팝업창 띄우기 --//
            // 앱 여러번 실행 시.
            String vTitle = "버전 업데이트 현재버전 : " + String.valueOf(intVersionNo) + ", 새버전 : " + String.valueOf(sVersion);
            String vMsg = "앱 업데이트를 해야합니다.\n업데이트를 진행하겠습니까? 취소 하면 프로그램이 종료됩니다.";

            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle(vTitle) // 메세지 타이틀
                    .setMessage(vMsg) // 메세지 내용
                    .setCancelable(false) // Dialog 밖이나 뒤로가기 막기위한 소스 true : 풀기, false : 막기
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // App을 업데이트 시키기 위한 메서드
                            update_User_Application();
                        }
                    }) // 확인 클릭 시 이벤트
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TGSClass.AlterMessage(MainActivity.this, "프로그램을 종료합니다.");
                            finishAffinity();
                        }
                    }) // 취소 클릭 시 이벤트
                    .show();
        } else {
            //-- 업데이트 할 필요가 없으면 앱 실행 --//
            updateChk = true;
            start();
        }
    }

    //-- APP 권한 체크(chkPermission) --//
    private void chkPermission() {
        //-- 카메라 권한 --//
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //-- 외부저장소 권한 --//
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //-- 카메라, 외부저장소 권한 체크 --//
        if (cameraPermission == PackageManager.PERMISSION_GRANTED
                && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //-- 권한을 다 가지고 있다면 동작 --//
            permissionChk = true;
            //-- 앱 체크 --//
            appChk();
            //== 버전 체크 ==//
            chkVersion();
        } else {
            //-- 권한 요청 --//
            setPermission();
        }
    }

    //-- 로그인(logIn) --//
    private void logIn(final String pID, final String pPWD) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_logIn = new Thread() {
            public void run() {

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                global.setLoginString(pID);
                global.setPlantCDString(strPlantCD);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parmID = new PropertyInfo();
                parmID.setName("pUSER_ID");
                parmID.setValue(pID);
                parmID.setType(String.class);
                pParms.add(parmID);

                PropertyInfo parmPWD = new PropertyInfo();
                parmPWD.setName("pPass");
                parmPWD.setValue(pPWD);
                parmPWD.setType(String.class);
                pParms.add(parmPWD);

                PropertyInfo parmIP = new PropertyInfo();
                parmIP.setName("pClientIP");
                parmIP.setValue(strClientIP);
                parmIP.setType(String.class);
                pParms.add(parmIP);

                PropertyInfo parmHostName = new PropertyInfo();
                parmHostName.setName("pClientHostName");
                parmHostName.setValue(strClientHostNm);
                parmHostName.setType(String.class);
                pParms.add(parmHostName);

                // String vResponse = dba.SendHttpMessage("HelloWorld",pParms);

                String vResponse = dba.SendHttpMessage("Login", pParms);

                Bundle bun = new Bundle();
                bun.putString("HTML_DATA", vResponse);
                bun.putString("TYPE", "LOGIN");
                bun.putString("ID", pID);

                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        };
        workThd_logIn.start();
        try {
            workThd_logIn.join();
        } catch (InterruptedException ex) {

        }
    }

    //-- 로그인 체크(logIn_chk) --//
    private boolean logIn_chk(final String pID, final String pPWD) {
        chkLogIn = true;
        Thread workThd_logIn_chk = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_PDA_LOGIN_CHK ";
                sql += "  @USER_ID = '" + pID + "'";
                sql += " ,@PW = '" + pPWD + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                strJson = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_logIn_chk.start();   //스레드 시작
        try {
            workThd_logIn_chk.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함

            try {
                boolean jSonType = TGSClass.isJsonData(strJson);

                if (jSonType) {
                    if (!strJson.equals("[]")) {
                        try {
                            JSONArray ja = new JSONArray(strJson);

                            if (ja.length() > 0) {
                                JSONObject jObject = ja.getJSONObject(0);

                                strUSER_ID  = jObject.getString("USER_ID");
                                strPWD      = jObject.getString("PW");
                            }
                        } catch (JSONException ex) {
                            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
                        }
                    } else {
                        //Toast.makeText(getApplicationContext(),"로그인 정보를 다시 입력 해주시기 바랍니다.", Toast.LENGTH_LONG).show();
                        chkLogIn = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
            return false;
        }

        if (!chkLogIn) {
            Toast.makeText(getApplicationContext(), "로그인 정보가 바르지 않습니다. 다시 입력 부탁드립니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    Handler handler = new Handler() {
        //스레드에서 값을 받아와서 화면에 뿌림.
        public void handleMessage(Message msg) {

            Bundle bun = msg.getData();

            String vType = bun.getString("TYPE");
            String vResponse = bun.getString("HTML_DATA");
            String vID = bun.getString("ID");

            switch (vType) {
                case "LOGIN":
                    if (vResponse.equals("OK")) {
                        TGSClass.AlterMessage(MainActivity.this, "인증완료", 500);

                        Intent intent = TGSClass.ChangeView(getPackageName(), MenuActivity.class);
                        //== SESSION에 값 추가 ==//
                        global.setLoginString(vID);
                        global.setUnitCDString(strClientHostNm);
                        global.setPlantCDString(strPlantCD);
                        startActivity(intent);
                        finish();
                    } else if (vResponse.equals("FAIL")) {
                        TGSClass.AlterMessage(getApplicationContext(), "*계정 및 패스워드 확인하세요*");
                    } else {
                        TGSClass.AlterMessage(getApplicationContext(), vResponse);
                    }
            }
        }
    };

    protected String dbQuery(final String pDevice) {
        String vPLANT_CD = "";
        String vUNIT_CD = "";
        String vUNIT_NM = "";
        String vUNIT_TYPE = "";
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " exec XUSP_AND_APK_DEVICE_CHECK @UNIT_CD='" + pDevice + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                strJson = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_dbQuery.start();   //스레드 시작
        try {
            workThd_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(strJson);

            if (ja.length() > 0) {
                JSONObject jObject = ja.getJSONObject(0);

                vPLANT_CD   = jObject.getString("PLANT_CD");
                vUNIT_CD    = jObject.getString("UNIT_CD");
                vUNIT_NM    = jObject.getString("UNIT_NM");
                vUNIT_TYPE  = jObject.getString("UNIT_TYPE");
            }
        } catch (JSONException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }
        return vPLANT_CD;
    }

    public void dbSave_RequestRegister() {
        Thread wTh = new Thread() {
            public void run() {
                String vUnitCD = global.getmUnitCDString();
                String vUserID = global.getLoginString();

                String sql = "DECLARE  @RTN_MSG NVARCHAR(200)=''";
                sql += "EXEC XUSP_AND_REGISTER_REQUEST_KO773_SET  ";
                sql += "  @UNIT_CD='" + strClientHostNm + "'";
                sql += " ,@UNIT_NM='" + strClientHostNm + "'";
                sql += " ,@UNIT_TYPE='EM_KOREA'";
                sql += " , @RTN_MSG =  @RTN_MSG  OUTPUT";
                sql += " SELECT  @RTN_MSG AS RTN_MSG ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                strJson = dba.SendHttpMessage("SetSQLSave", pParms);
                String vMSG = "";
                String vStatus = "";

                try {
                    JSONArray ja = new JSONArray(strJson);

                    if (ja.length() > 0) {

                        JSONObject jObject = ja.getJSONObject(0);

                        vMSG = !jObject.isNull("RTN_MSG") ? jObject.getString("RTN_MSG") : "";
                        vStatus = !jObject.isNull("STATUS") ? jObject.getString("STATUS") : "";
                    }

                    Bundle bun = new Bundle();
                    if (!vStatus.equals("OK")) {
                        bun.putString("RTN_MSG", vMSG);
                        bun.putString("STATUS", vStatus);
                    } else {
                        bun.putString("RTN_MSG", "등록 요청 완료되었습니다.");
                        bun.putString("STATUS", vStatus);
                    }
                    Message msg = handler1.obtainMessage();
                    msg.setData(bun);
                    handler1.sendMessage(msg);
                } catch (JSONException ex) {

                }
            }
        };

        wTh.start();   //스레드 시작
        try {
            wTh.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    Handler handler1 = new Handler() {
        //스레드에서 값을 받아와서 화면에 뿌림.
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();

            String vMsg = bun.getString("RTN_MSG");

            TGSClass.AlterMessage(MainActivity.this, vMsg);
        }

    };

    //-- APK 버전과 config 값 가져오기 --//
    public Boolean getVersionFromServer() {
        Boolean vReturn = false;
        Thread workThd_getVersionFromServer = new Thread() {
            public void run() {
                String sql = " exec XUSP_AND_APP_VERSION_GET @FILENAME='" + FILE_NAME + "'";
                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                strJsonVersion = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };

        workThd_getVersionFromServer.start();   //스레드 시작

        try {
            workThd_getVersionFromServer.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
            try {
                Boolean vJsonType = TGSClass.isJsonData(strJsonVersion);

                if (!vJsonType) {
                    strErrorMsg = strJsonVersion;
                    return false;
                }

                JSONArray ja = new JSONArray(strJsonVersion);

                if (ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);

                    DOWNLOAD_URL = String.valueOf(jObject.getString("UPDATE_URL"));
                    String Version = String.valueOf(jObject.getString("VERSION_CD"));
                    sVersion = Integer.parseInt(Version);
                }

                vReturn = true;
            } catch (JSONException ex) {
                TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
            } catch (Exception ex) {
                TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
            }
        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }

        return vReturn;
    }

    public void update_User_Application() {
        //-- 1. 파일 다운로드 --//
        downloadAPK(DOWNLOAD_URL);
        //-- 2. 다운로드 설치 관리자로 설치 --//
        updateAPK();
        //-- 3. 앱 종료 --//
        finishAffinity();
    }

    public void start() {
        strPlantCD = dbQuery(strClientHostNm); // 등록된 기기이면 공장 코드값을 불러옴.
        System.out.println("strPlantCD:"+strPlantCD);

        if (strPlantCD.equals("")) {
            //등록되지 않은 단말기 이면 단말기 등록 요청 페이지로 이동한다.
            Intent res_intent = TGSClass.ChangeView(getPackageName(), RegisterActivity.class);
            startActivity(res_intent);
            finish();
        }
    }

    //== 파일다운로드(downloadAPK) ==//
    public void downloadAPK(final String apk_url) {

        TGSClass.AlterMessage(MainActivity.this, apk_url + "파일 다운로드 시작", 2000);

        Thread downThread = new Thread() {
            public void run() {
                try {

                    //다운로드 페이지 접속
                    URL url = new URL(apk_url);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    //c.setDoOutput(true);

                    c.connect();

                    //다운로드 받을 폴더 설정.
                    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EM_KOREA";
                    File file = new File(PATH);
                    file.mkdirs();
                    String filePath = file.getPath();

                    //다운로드 받을 파일명 설정
                    File outputFile = new File(filePath, FILE_NAME);
                    FileOutputStream fos = new FileOutputStream(outputFile);

                    //파일 다운로드 진행.
                    InputStream is = c.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                    fos.close();
                    is.close(); //till here, it works fine - .apk is download to my sdcard in download file

                } catch (MalformedURLException ex1) {
                    Toast.makeText(getApplicationContext(), ex1.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    String aaa = e.toString();

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        };

        downThread.start();   //스레드 시작
        try {
            downThread.join();
        } catch (InterruptedException ex) {

        }
    }

    //== 다운받은 APK를 설치(updateAPK) ==//
    public void updateAPK() {
        try {
            //File file = new File(Environment.getExternalStorageDirectory() + FILE_NAME);

            File directory = Environment.getExternalStoragePublicDirectory("EM_KOREA");
            File file = new File(directory, FILE_NAME);

            Uri fileUri = Uri.fromFile(file); //for Build.VERSION.SDK_INT <= 24

            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //dont forget add this line
            startActivity(intent);
        } catch (Exception e) {
            TGSClass.AlterMessage(MainActivity.this, e.toString());
        }
    }

    //== 권한 요청(setPermission) ==//
    public void setPermission() {
        //-- 권한 요청을 허용한 적이 없다면 권한 요청(2가지 경우) --//
        //-- 1. 사용자가 권한 거부를 한 적이 있는 경우 --//
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
            //-- 요청을 진행하기 전에 사용자에게 권한이 필요한 이유를 설명 --//
            String vMsg = "APP을 정상적으로 사용하기 위해서 권한이 필요합니다.";

            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("APP 권한 요청")
                    .setMessage(vMsg)
                    .setCancelable(false) // Dialog 밖이나 뒤로가기 막기위한 소스 true : 풀기, false : 막기
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();     //닫기
                            ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_WRITE_STORAGE_PERMISSIONS);
                            // Update_User_Application();
                        }
                    })
                    .show();
        } else {
            //-- 2. 사용자가 퍼미션 거부를 한 적이 없는 경우 --//
            //-- 권한 요청을 바로 실시 --//
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_WRITE_STORAGE_PERMISSIONS);
        }
    }

    //== MES에 등록된 사용자 자동 로그인(dbQuery_Auto_LogIn) ==//
    public void dbQuery_Auto_LogIn(final String HostName) {
        Thread workThd_dbQuery_Auto_LogIn = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_AUTO_LOGIN_CHECK_YN_ANDROID ";
                sql += " @UNIT_CD = '" + HostName + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                strJson_Auto_LogIn = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_dbQuery_Auto_LogIn.start();   //스레드 시작
        try {
            workThd_dbQuery_Auto_LogIn.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함

            try {
                boolean jSonType = TGSClass.isJsonData(strJson_Auto_LogIn);

                if (jSonType) {
                    //반환받은 id 값을 가지고 프로그램 실행
                    JSONArray ja = new JSONArray(strJson_Auto_LogIn);

                    String id = "";

                    for (int idx = 0; idx < ja.length(); idx++) {
                        JSONObject jObject = ja.getJSONObject(idx);
                        id = jObject.getString("USER_ID");
                    }

                    if(id.equals("")){
                        return;
                    }
                    if (!strJson_Auto_LogIn.equals("[]")) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), MenuActivity.class);
                        //== SESSION에도 값 추가 ==//

                        //LOGINS STRING에는 로그인 ID가 들어가도록 수정
                        //global.setLoginString(strClientHostNm);
                        global.setLoginString(id);

                        global.setUnitCDString(strClientHostNm);
                        global.setPlantCDString(strPlantCD);
                        startActivity(intent);
                        finish();

                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        } catch (InterruptedException ex) {

        }
    }

    //== 권한 요청 후 Callback ==//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_STORAGE_PERMISSIONS && grantResults.length == REQUIRED_PERMISSIONS.length) {
            //-- 요청 코드가 REQUEST_CODE_WRITE_STORAGE_PERMISSIONS 이고, 요청한 권한 개수만큼 수신되었다면 --//
            boolean chk_result = true;

            //-- 모든 권한을 허용했는지 체크 --//
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    chk_result = false;
                    break;
                }
            }

            if (chk_result) {
                //-- 모든 권한을 허용했다면 앱체크와 버전 체크 --//
                permissionChk = true;
                //== 앱 체크 ==//
                appChk();
                //== 버전 체크 ==//
                chkVersion();
            } else {
                //-- 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료 --//
                //-- 2가지 경우 --//
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    //-- 1. 사용자가 거부를 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있다.
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setMessage("필수 권한이 거부되었습니다. 앱을 다시 실행하여 권한을 허용해주세요.")
                            .setCancelable(false) // Dialog 밖이나 뒤로가기 막기위한 소스 true : 풀기, false : 막기
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .create().show();
                } else {
                    // 2. "다시 묻지 않음" 을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 권한을 허용해야 앱을 사용할 수 있다.
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setMessage("필수 권한이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해야 합니다.")
                            .setCancelable(false) // Dialog 밖이나 뒤로가기 막기위한 소스 true : 풀기, false : 막기
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .create().show();
                }
            }
        }
    }

    // 카메라와 외부 저장소 권한
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DOWNLOAD_ACTIVITY_REQUEST_CODE:
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case DOWNLOAD_ACTIVITY_REQUEST_CODE:
                    TGSClass.AlterMessage(getApplicationContext(), "이미 등록된 장비입니다.\n 로그인을 진행해 주시길 바랍니다.");
                    break;
                default:
                    break;
            }
        }
    }
}
