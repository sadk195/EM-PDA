package com.example.EM_KOREA.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

public class TGSClass {
    //웹서비스 네임스페이스 정의
    public static String ws_name_space = "http://tgs.com/";
    //public static String ws_url= "http://125.135.140.21/MES_ANDROID/webService.asmx"; //-- EMK 실DB(unierp5)
    //public static String ws_url = "http://125.135.140.21/MES_ANDROID_TEST/webService.asmx"; //-- EMK 테스트DB(unierp5_dev)
    //public static String ws_url= "http://localhost:6260/MES_ANDROID_TEST/WebServiceEM"; //-- EMK 테스트DB(local)
    public static String ws_url="http://106.245.142.3/WMS_TEST/webService.asmx"; //--TGS내부 테스트 DB(unierp5)
    //public static String ws_url="http://125.135.140.14/WMS_TEST/webService.asmx"; //--EM내부 테스트 DB(UNIERP5_TEST
    //public static String ws_url="http://192.168.12.14/WMS_TEST/webService.asmx"; //--EM사내  테스트 DB

    public TGSClass() {

    }

    /**
     * 변경할 Intent 반환
     *
     * @param /패키지 명칭(네임스페이스)
     * @param /클래스 네임(뷰 클래스 이름)
     * @return Intent 객체.(보여질 화면)
     */

    public static Intent ChangeView(String pPackageName, String pClassName) {
        String vComponentName = pPackageName + "." + pClassName;

        //Intent intent = new Intent(getApplicationContext(),SubActivity.class);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pPackageName, vComponentName));

        return intent;
    }
    public static Intent ChangeView(String pPackageName, Class pClassName) {

        String vComponentName = pClassName.getName();
        //Intent intent = new Intent(getApplicationContext(), SubActivity.class);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pPackageName, vComponentName));

        return intent;
    }

    /**
     * 현재 화면을 변경합니다.
     *
     * @param /컨텍스트 명칭(getApplicationContext())
     * @param /패키지  명칭(getPackageName())
     * @param /클래스  네임(Activity Name)
     * @return boolean (뷰 액티비티 존재 유무)
     */
    public static boolean ChangeView(Context pContext, String pPackageName, String pClassName) {

        String vComponentName = pPackageName + "." + pClassName;
        //Intent intent = new Intent(getApplicationContext(),SubActivity.class);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pPackageName, vComponentName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    //startActivity 를 외부 클래스(TGS Class) 에서 실행하기 위한 설정값.

        boolean isActivity = isIntentAvailable(pContext, pPackageName, pClassName);

        if (isActivity) {
            //Activity 리스트 중에서 일치하는 Activity Class 가 있으면 화면 변경.
            pContext.startActivity(intent);
        } else {
            //Activity 리스트 중에서 일치하는 Activity Class 가 없으면 에러 메세지 출력.
            AlterMessage(pContext, pClassName + " Not Found Activity");
        }

        return isActivity;
    }

    public static void MessageBox(Context pContext, String pTitle, String pMessage) {
        AlertDialog.Builder alert = new AlertDialog.Builder(pContext);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();     //닫기

            }
        });
        alert.setTitle(pTitle);
        alert.setMessage(pMessage);
        alert.show();
    }


    //메세지 박스
    public static void AlterMessage(Context pContext, String pMessage) {
        AlterMessage(pContext, pMessage, 4000);
    }

    public static void AlterMessage(Context pContext, String pMessage, int pTime) {
        final Toast toast = Toast.makeText(pContext, pMessage, Toast.LENGTH_LONG);
        showMyToast(toast, pTime);
    }

    /**
     * 현재 화면을 변경합니다.
     *
     * @toast 컨텍스트 명칭(toast)
     * @delay 메세지 보여줄 시간 - 밀리 세컨드(delay)
     */
    public static void showMyToast(final Toast toast, final int delay) {

        toast.show();
        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {
                toast.cancel();
            }

        }, delay);
    }

    //IP 주소 받아오는 메서드.
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface inf = en.nextElement();
                for (Enumeration<InetAddress> listIP = inf.getInetAddresses(); listIP.hasMoreElements(); ) {
                    InetAddress inetAddress = listIP.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //문자열이 JSON 데이터 형식인지 확인.
    public static boolean isJsonData(String pJsonString) {
        try {
            JSONArray ja = new JSONArray(pJsonString);
            return true;
        } catch (JSONException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    //문자열이 숫자인지 문자인지 판별
    public static boolean isNumeric(String value) {
        Pattern PATTERN = Pattern.compile("^(-?0|-?[1-9]\\d*)(\\.\\d+)?(E\\d+)?$");
        return value != null && PATTERN.matcher(value).matches();
    }


    //Intent 존재 여부 확인.
    public static boolean isIntentAvailable(Context pContext, String pPackageName, String pClassName) {

        String vComponentName = pPackageName + "." + pClassName;
        final PackageManager packageManager = pContext.getPackageManager();

        final Intent intent = new Intent();
        intent.setComponent(new ComponentName(pPackageName, vComponentName));
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;

    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }

    public static String transSemicolon(String txt) {
        String transStr = "";
        if (txt.contains(";")) {
            int semicolon_idx = txt.indexOf(";");
            transStr = txt.substring(0, semicolon_idx);
        } else {
            transStr = txt;
        }
        return transStr;
    }

    public static String[] transHyphen(String txt) {
        String[] transStr = null;
        if (txt.contains("-")) {
            transStr = txt.split("-");
        }

        return transStr;
    }

    public static String transHyphen(String txt, int idx) {
        String[] transStr = null;
        transStr = transHyphen(txt);

        return transStr[idx];
    }
}
