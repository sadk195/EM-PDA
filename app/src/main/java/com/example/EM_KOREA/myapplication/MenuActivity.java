package com.example.EM_KOREA.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class MenuActivity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson;

    //== View 선언(Layout) ==//
    private LinearLayout leftMenu, rightMenu;

    //== View 선언(ImageView) ==//
    private ImageView btn_config;

    //== View 선언(TextView) ==//
    private TextView lbl_id;

    //== 상수 선언 ==//
    public final int REQUEST_CODE_CAMERA_PERMISSIONS = 1000;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    //== Grant관련 변수 선언 ==//
    private String ADMIN = "N";
    private String I10 = "N", I20 = "N", I30 = "N", I40 = "N", I50 = "N", I60 = "N", I70 = "N";
    private String M10 = "N", M20 = "N", M30 = "N", P10 = "N", S10 = "N";

    //== Request Code 상수 선언 ==//
    private final int M10_REQUEST_CODE = 1, M20_REQUEST_CODE = 2, I10_REQUEST_CODE = 3;
    private final int I20_REQUEST_CODE = 4, I30_REQUEST_CODE = 5, I40_REQUEST_CODE = 6;
    private final int I50_REQUEST_CODE = 7, I60_REQUEST_CODE = 8, S10_REQUEST_CODE = 9;
    private final int I70_REQUEST_CODE = 10, P10_REQUEST_CODE = 11, M30_REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== ID 값 바인딩 ==//
        leftMenu        = (LinearLayout) findViewById(R.id.lo_left);
        rightMenu       = (LinearLayout) findViewById(R.id.lo_right);

        btn_config      = (ImageView) findViewById(R.id.btn_config);
        lbl_id          = (TextView) findViewById(R.id.lbl_login_id);
    }

    private void initializeListener() {
        //== 이벤트 ==//
        btn_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TGSClass.AlterMessage(getApplicationContext(),"환경설정");
                Intent intent = TGSClass.ChangeView(getPackageName(), ConfigActivity.class.getSimpleName());
                startActivity(intent);
            }
        });
    }

    private void initializeData() {
        lbl_id.setText(vUSER_ID + " 으로 로그인 중");

        start();
    }

    /* 뒤로가기 버튼 빠르게 2번 클릭 시 프로그램 종료
     *  - 시스템 함수 onBackPressed() 함수 오버라이드
     * */
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        //Log.d("tempTime", String.valueOf(tempTime));

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            //super.finish();
            finishAffinity();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "한번 더 누르면 프로그램이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void start() {
        //== 메뉴 리스트 가져오기 ==//
        dbQuery_getMenuList(vUSER_ID);
        //== 유저별 권한 체크 ==//
        getMenuGrant();
        setGrant();

        if (!sJson.equals("")) {
            try {
                //배경색
                Drawable bgColor1 = getDrawable(R.drawable.bg_prg_menu1);
                Drawable bgColor2 = getDrawable(R.drawable.bg_prg_menu2);
                Drawable bgApplyColor = bgColor1;

                JSONArray ja = new JSONArray(sJson);

                int vMenuCnt = ja.length(); // 메뉴 숫자.

                leftMenu.setWeightSum((vMenuCnt / 2) * 3);
                rightMenu.setWeightSum((vMenuCnt / 2) * 3);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    final String vMenuID        = jObject.getString("MENU_ID");
                    final String vMenuName      = jObject.getString("MENU_NM");
                    final String vMenuRemark    = jObject.getString("REMARK");
                    final String vStartCommand  = jObject.getString("START_COMMAND");
                    final int vRowNum           = jObject.getInt("ROW_NUM");

                    //개별 메뉴를 만들 컨트롤
                    //double vWeight = (double) (vAllHeight / (vMenuCnt / 2 ));  //메뉴각하나당 크기 비율 전체 12

                    LinearLayout.LayoutParams params
                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3);

                    params.setMargins(3, 3, 3, 3);

                    LinearLayout menuBox = new LinearLayout(this);
                    menuBox.setLayoutParams(params);

                    //메뉴의 텍스트 정보를 보여주는 컨트롤.
                    RelativeLayout.LayoutParams params1
                            = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(20, 20, 0, 0);
                    params1.addRule(RelativeLayout.ALIGN_LEFT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                    TextView menuText = new TextView(this);
                    menuText.setText(vMenuName);
                    menuText.setTextSize(25);
                    menuText.setLayoutParams(params1);

                    //메뉴의 이미지 정보를 보여주는 컨트롤.
                    RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params2.setMargins(0, 0, 20, 20);
                    params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                    params2.height = 400 / (vMenuCnt / 2);
                    params2.width = 400 / (vMenuCnt / 2);

                    int id = getResources().getIdentifier(getPackageName() + ":drawable/" + vMenuID.toLowerCase(), null, null);
                    ImageView imageView = new ImageView(this);

                    //MENU ID 와 일치하는 이미지
                    imageView.setImageResource(id);
                    imageView.setLayoutParams(params2);

                    menuBox.addView(menuText);  //메뉴텍스트 뷰를 menuBox 레이아웃에 추가.
                    menuBox.addView(imageView);

                    menuBox.setOnClickListener(new View.OnClickListener() {    //동적 생성 된 컨트롤의 클릭 이벤트.
                        @Override
                        public void onClick(View v) {

                            //TGSClass.AlterMessage(getApplicationContext(),vMenuID);
                            String vActivityName = vMenuID + "_Activity";

                            if (start_grant(vMenuID, vMenuName) == true) { //== 권한 적용 ==//
                                //ACTIVITY 존재여부 확인.
                                boolean bCheck = TGSClass.isIntentAvailable(getApplicationContext(), getPackageName(), vActivityName);

                                if (bCheck) {
                                    Intent intent = TGSClass.ChangeView(getPackageName(), vActivityName);

                                    intent.putExtra("MENU_ID", vMenuID);
                                    intent.putExtra("MENU_NM", vMenuName);
                                    intent.putExtra("MENU_REMARK", vMenuRemark);
                                    intent.putExtra("START_COMMAND", vStartCommand);
                                    startActivityForResult(intent, vRowNum);
                                } else {
                                    TGSClass.AlterMessage(getApplicationContext(), vMenuName + "가 존재하지 않습니다.");
                                }
                            }
                        }
                    });

                    if (idx % 2 == 0) {

                        menuBox.setBackground(bgApplyColor);
                        leftMenu.addView(menuBox);

                        bgApplyColor = bgApplyColor == bgColor1 ? bgColor2 : bgColor1;   //메뉴 스타일 반전.

                    } else {
                        menuBox.setBackground(bgApplyColor);
                        rightMenu.addView(menuBox);

                        //bgApplyColor = bgColor1;
                    }
                }
            } catch (JSONException exJson) {
                TGSClass.AlterMessage(getApplicationContext(), exJson.getMessage());
                finishAffinity();
            } catch (Exception ex) {
                TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
                finishAffinity();
            }
        }
    }

    //== 메뉴 리스트 가져오기 ==//
    private void dbQuery_getMenuList(final String pID) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_getMenuList = new Thread() {
            public void run() {

                String sql = " exec XUSP_AND_APK_MENU_LIST @FLAG='',@USER_ID='" + pID + "',@UNIT_TYPE='" + global.getUnitType() + "'";

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
        wkThd_getMenuList.start();   //스레드 시작
        try {
            wkThd_getMenuList.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    //== 메뉴 권한 체크 ==//
    private void setGrant() {
        try {
            JSONArray ja = new JSONArray(sJson_menu_grant);

            if (sJson_menu_grant.contains("ADMIN")) {
                ADMIN = "Y";
            }
            if (sJson_menu_grant.contains("I20")) {
                I20 = "Y";
            }
            if (sJson_menu_grant.contains("I30")) {
                I30 = "Y";
            }
            if (sJson_menu_grant.contains("I40")) {
                I40 = "Y";
            }
            if (sJson_menu_grant.contains("I50")) {
                I50 = "Y";
            }
            if (sJson_menu_grant.contains("I60")) {
                I60 = "Y";
            }
            if (sJson_menu_grant.contains("I70")) {
                I70 = "Y";
            }
            if (sJson_menu_grant.contains("M10")) {
                M10 = "Y";
            }
            if (sJson_menu_grant.contains("M20")) {
                M20 = "Y";
            }
            if (sJson_menu_grant.contains("M30")) {
                M30 = "Y";
            }
            if (sJson_menu_grant.contains("P10")) {
                P10 = "Y";
            }
            if (sJson_menu_grant.contains("S10")) {
                S10 = "Y";
            }

            /*
            ADMIN_CHK = "N";     //ADMIN
            I10 = "N";      //★재고조회★ -> 전부다 조회가능함(오로지 조회만이기 때문)
            I20 = "N";      //★재고이동★
            I30 = "N";      //★생산출고관리★
            I40 = "N";      //★작업장반입★ -
            I50 = "N";      //★예외출고★
            I60 = "N";      //★개발용출고★
            I70 = "N";      //★재고실사관리★
            M10 = "N";      //★구매입고관리★
            M20 = "N";      //★생산입고관리★
            M30 = "N";      //★입하관리★
            P10 = "N";      //★생산실적관리★
            S10 = "N";      //★출하관리★

            W_IN	        // WMS_입고권한
            W_MOVE	        // WMS_재고권한
            W_OUT	        // WMS_출고권한
             */
            /*
            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                final String LEVEL_CD = jObject.getString("LEVEL_CD");
                final String PROGRAM_ID = jObject.getString("PROGRAM_ID");

                ADMIN = LEVEL_CD.equals("ADMIN") || ADMIN.equals("Y") ? "Y" : "N";

                switch (PROGRAM_ID) {
                    case "I20":
                        I20 = "Y";
                    case "I30":
                        I30 = "Y";
                    case "I40":
                        I40 = "Y";
                    case "I50":
                        I50 = "Y";
                    case "I60":
                        I60 = "Y";
                    case "I70":
                        I70 = "Y";
                    case "M10":
                        M10 = "Y";
                    case "M20":
                        M20 = "Y";
                    case "M30":
                        M30 = "Y";
                    case "P10":
                        P10 = "Y";
                    case "S10":
                        S10 = "Y";
                }
            }
            */
        } catch (JSONException exJson) {
            TGSClass.AlterMessage(getApplicationContext(), "catch : exJson");
        } catch (Exception ex) {
            TGSClass.AlterMessage(getApplicationContext(), "catch : ex");
        }
    }

    //== 권한 적용 ==//
    private boolean start_grant(final String MenuID, String MenuNm) {
        try {
            if (ADMIN.equals("N")) {
                if (MenuID.equals("I20") && I20.equals("N")) { //재고이동
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I30") && I30.equals("N")) { //생산출고
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I40") && I40.equals("N")) { //작업장반입
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I50") && I50.equals("N")) { //예외출고
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I60") && I60.equals("N")) { //개발용출고
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I70") && I70.equals("N")) { //재고실사관리
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("M10") && M10.equals("N")) { //구매입고등록
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("M20") && M20.equals("N")) { //생산입고등록
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("M30") && M30.equals("N")) { //입하관리
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("P10") && P10.equals("N")) { //생산실적관리
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("S10") && S10.equals("N")) { //출하관리
                    TGSClass.AlterMessage(getApplicationContext(), MenuNm + " 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            TGSClass.AlterMessage(getApplicationContext(), "catch : ex");
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case M10_REQUEST_CODE:
                    break;
                case M20_REQUEST_CODE:
                    break;
                case I10_REQUEST_CODE:
                    break;
                case I20_REQUEST_CODE:
                    break;
                case I30_REQUEST_CODE:
                    break;
                case I40_REQUEST_CODE:
                    break;
                case I50_REQUEST_CODE:
                    break;
                case I60_REQUEST_CODE:
                    break;
                case S10_REQUEST_CODE:
                    break;
                case I70_REQUEST_CODE:
                    break;
                case P10_REQUEST_CODE:
                    break;
                case M30_REQUEST_CODE:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case M10_REQUEST_CODE:
                    //Log.d("TEST", "M10");
                    break;
                case M20_REQUEST_CODE:
                    break;
                case I10_REQUEST_CODE:
                    break;
                case I20_REQUEST_CODE:
                    break;
                case I30_REQUEST_CODE:
                    break;
                case I40_REQUEST_CODE:
                    break;
                case I50_REQUEST_CODE:
                    break;
                case I60_REQUEST_CODE:
                    break;
                case S10_REQUEST_CODE:
                    break;
                case I70_REQUEST_CODE:
                    break;
                case P10_REQUEST_CODE:
                    break;
                case M30_REQUEST_CODE:
                    break;
            }
        }
    }
}




