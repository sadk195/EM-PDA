package com.example.EM_KOREA.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity {

    /*
    public final int REQUEST_CODE_WRITE_STORAGE_PERMISSIONS = 1001;
    public final int REQUEST_CODE_WAKELOCK_PERMISSIONS = 1002;
    public final String FILE_NAME = "EM_KOREA.apk";
    public String DOWNLOAD_URL = "";
    public int sVersion = 5;
    public String sErrorMessage;
    */

    public MySession global;

    public String sJson;
    public String sJsonVersion;
    public String sClientHostName;
    public String sPlantCD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        //== 전역클래스 선언 ==//
        global  = (MySession)getApplication();

        /*
        // 현재 설치된 APP 의 버전 정보를 가져옵니다.
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pInfo.versionName;
        int versionNo = pInfo.versionCode;

        // 현재 설치된 APP 의 버전 정보와 다운로드 받을 경로 정보를 데이터 베이스에서 받아옵니다.
        if (!GetVersionFromServer()) {
            Intent error_intent = TGSClass.ChangeView(getPackageName(), ErrorPopupActivity.class.getSimpleName());
            error_intent.putExtra("MSG", sErrorMessage);
            startActivity(error_intent);
            return;
        }
         */
        //== MainActivity에서 구현한 appChk()메서드를 가져와서 사용 ==//
        ((MainActivity)MainActivity.mContext).appChk();

        /*
        //현재 설치된 앱 보다 새 버전의 앱 정보가 존재하면.
        if (versionNo < sVersion) {
            //업데이트 파일이 존재한다는 팝업창 띄우기
            //앱 여러번 실행 시.
            String vMsg = "앱 업데이트를 해야합니다. 업데이트를 진행하시겠습니까? 취소 하시면 프로그램이 종료됩니다.";

            AlertDialog.Builder alert = new AlertDialog.Builder(DownloadActivity.this);
            alert.setTitle("업데이트 알림, 현재버전:" + String.valueOf(versionNo) + ",새버전:" + String.valueOf(sVersion));
            alert.setCancelable(true);
            alert.setMessage(vMsg);

            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //현재 버전보다 업데이트 해야할 버전이 높으면 업데이트 진행
                    Update_User_Application();
                }
            });

            alert.show();
        } else {
            //업데이트 할 필요가 없으면 앱 실행
            Start();
        }
         */

        //== MainActivity에서 사용 중인 chkVersion()메서드를 가져와서 사용 ==//
        ((MainActivity)MainActivity.mContext).chkVersion();
        start();
    }

    /*
    //데이터 베이스에서 앱 최종 버전 정보를 가져옵니다.
    public Boolean GetVersionFromServer() {
        Boolean vReturn = false;
        Thread workingThread = new Thread() {
            public void run() {
                String sql = " exec XUSP_AND_APP_VERSION_GET @FILENAME='" + FILE_NAME + "'";
                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonVersion = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        workingThread.start();   //스레드 시작

        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
            try {
                Boolean vJsonType = TGSClass.isJsonData((sJsonVersion));

                if (!vJsonType) {
                    sErrorMessage = sJsonVersion;
                    return false;
                }

                JSONArray ja = new JSONArray(sJsonVersion);

                if (ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);

                    String Version = String.valueOf(jObject.getString("VERSION_CD"));
                    sVersion = Integer.parseInt(Version);
                    DOWNLOAD_URL = String.valueOf(jObject.getString("UPDATE_URL"));
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

    public void Update_User_Application() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //파일 다운로드 함.
            DownloadAPK(DOWNLOAD_URL);
            //다운로드 설치 관리자로 설치
            UpdateAPK();
            //앱 종료.
            finishAffinity();
        } else {
            //권한이 없으면 권한 요청 함수 실행.
            SetPermission();
        }
    }
     */

    //업데이트 정보가 없으면 앱 실행.
    public void start() {
        sPlantCD = dbQuery(global.getmUnitCDString()); // 등록된 기기이면 공장 코드값을 불러옴.

        if (sPlantCD.equals("")) {
            //등록되지 않은 단말기 이면 단말기 등록 요청 페이지로 이동한다.
            Intent res_intent = TGSClass.ChangeView(getPackageName(), RegisterActivity.class.getSimpleName());
            startActivity(res_intent);
        } else {
            /*
            //등록된 장비면 메뉴로 바로 이동 한다.
            Intent intent = TGSClass.ChangeView(getPackageName(), MenuActivity.class.getSimpleName());
            intent.putExtra("pID", sClientHostName);
            intent.putExtra("pDEVICE", sClientHostName);
            intent.putExtra("pPLANT_CD", sPlantCD);
            startActivity(intent);

            //기존이 TC 테크에는 로그인 기능이 따로 없고, 장비를 등록하여 등록된 장비면 바로 어플을 사용할 수 있게 되어 있었지만,
            //이엠에서는 로그인 기능을 필수로 한다.
            */
            /*
            Intent intent = TGSClass.ChangeView(getPackageName(), MainActivity.class.getSimpleName());
            TGSClass.AlterMessage(getApplicationContext(), "이미 등록된 장비입니다. \n 로그인 진행해 주시길 바랍니다.");
            startActivity(intent);
             */
            // 저장 후 결과 값 돌려주기 위한 intent
            Intent resultIntent = new Intent();
            // 결과처리 후 부른 Activity에 보낼 값
            resultIntent.putExtra("result", "FAIL");
            // 부른 Activity에게 결과 값 반환
            setResult(RESULT_CANCELED, resultIntent);
            // 현재 Activity 종료
            finish();
        }
    }

    /*
    //설치 페이지로 이동
    public void UpdateAPK() {
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
            TGSClass.AlterMessage(DownloadActivity.this, e.toString());
        }
    }

    public void UnInstallApplication(String packageName) {// Specific package Name Uninstall.
        //Uri packageURI = Uri.parse("package:com.CheckInstallApp");
        Uri packageURI = Uri.parse(packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        startActivity(uninstallIntent);
    }

    //권한 획득 팝업창
    public void SetPermission() {
        //해당 권한이 필요한 이유에 대해서 설명한다. 앱 최초 실행시 false
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            String vMsg = "APP의 자동 업데이트를 하기 위해서 디바이스 접근 권한이 필요합니다.";

            AlertDialog.Builder alert = new AlertDialog.Builder(DownloadActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();     //닫기
                    ActivityCompat.requestPermissions(DownloadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_STORAGE_PERMISSIONS);

                }
            });
            alert.setTitle("APP 권한 요청");
            alert.setMessage(vMsg);
            alert.show();

        } else {
            //앱 여러번 실행 시.
            String vMsg = "APP의 디바이스 접근 권한이 허용되지 않았습니다, 설정 메뉴에서 디바이스 저장 권한을 허용하세요.";

            AlertDialog.Builder alert = new AlertDialog.Builder(DownloadActivity.this);

            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();     //닫기
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });
            alert.setTitle("APP 권한 요청");
            alert.setMessage(vMsg);
            alert.show();
        }
        return;
    }

    //권한 팝업창에 대한 응답.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_STORAGE_PERMISSIONS:   //권한이 정상적으로 오픈되면
                //TGSClass.AlterMessage(DownloadActivity.this,"쓰기권한요청 응답 값 >> " + String.valueOf(requestCode));
                DownloadAPK(DOWNLOAD_URL);
                UpdateAPK();
                break;
            case REQUEST_CODE_WAKELOCK_PERMISSIONS:   //권한 오픈이 거절되면.
                //Code for handling WAKE_LOCK Permission results
                break;
        }
    }

    public void DownloadAPK(final String apk_url) {
        TGSClass.AlterMessage(DownloadActivity.this, apk_url + "파일 다운로드 시작", 2000);

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
     */

    //등록된 기기 여부 확인
    public String dbQuery(final String pDevice) {

        String vPLANT_CD = "";
        String vUNIT_CD = "";
        String vUNIT_NM = "";
        String vUNIT_TYPE = "";
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {
                String sql = " exec XUSP_AND_APK_DEVICE_CHECK @UNIT_CD='" + pDevice + "'";

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
        workingThread.start();   //스레드 시작

        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
            try {
                JSONArray ja = new JSONArray(sJson);

                if (ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);

                    vPLANT_CD = jObject.getString("PLANT_CD");
                    vUNIT_CD = jObject.getString("UNIT_CD");
                    vUNIT_NM = jObject.getString("UNIT_NM");
                    vUNIT_TYPE = jObject.getString("UNIT_TYPE");
                }
            } catch (JSONException ex) {
                TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
            } catch (Exception ex) {
                TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
            }

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }

        return vPLANT_CD;
    }
}

