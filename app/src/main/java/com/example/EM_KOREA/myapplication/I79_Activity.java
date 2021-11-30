package com.example.EM_KOREA.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.icu.text.DecimalFormat;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

public class I79_Activity extends AppCompatActivity {

    private final long INTERVAL_TIME = 500;  //간격
    private long inputEnterPressedTime = 0;  //엔터키 입력 시간.
    public String sJson = "";
    public ImageView img_barcode;
    public EditText txt_Scan;
    public String sJobCode;
    public String sMenuRemark;
    public MySession global;
    public String Plant_CD;
    public String INV_NO, SL_CD, SL_NM , WC_CD, ITEM_NM, STOCKYARD, ITEM_CD, INVENTORY_QTY, TRACKING_NO, LOT_NO, INVENTORY_COUNT_DATE, MAJOR_SL_CD, UPPER_INV_NO, formattedStringPrice;
    private  String m_item_cd="";
    private  String m_tracking_no="";
    public int GOOD_ON_HAND_QTY;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i79);

        String vMenuName = getIntent().getStringExtra("MENU_NM");
        sMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        sJobCode = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드
        INVENTORY_COUNT_DATE = getIntent().getStringExtra("INVENTORY_COUNT_DATE");
        UPPER_INV_NO = getIntent().getStringExtra("INV_NO");

        final EditText txt_tracking_no = (EditText) findViewById(R.id.txt_tracking_no);
        final EditText txt_lot_no = (EditText) findViewById(R.id.txt_lot_no);
        final TextView txt_inventory_stock_qty = findViewById(R.id.txt_inventory_stock_qty);
        final TextView txt_sl_cd = findViewById(R.id.txt_sl_cd);

        TextView lbl_view_title = findViewById(R.id.lbl_view_title);
        lbl_view_title.setText(vMenuName);

        txt_sl_cd.setText(SL_NM);

        EditText txt_stockyard = (EditText) findViewById(R.id.txt_stockyard);
        EditText txt_item_cd = (EditText) findViewById(R.id.txt_item_cd);
        EditText txt_inventory_qty = (EditText) findViewById(R.id.txt_inventory_qty);






        txt_item_cd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    global = (MySession)getApplication(); //전역 클래스
                    String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");
                    if(vPlant_CD == null) vPlant_CD = global.getPlantCDString();

                    EditText txt_item_cd = (EditText) findViewById(R.id.txt_item_cd);
                    TextView txt_item_nm = (TextView) findViewById(R.id.txt_item_nm);
                    String vitem_nm = txt_item_cd.getText().toString();
                    String vtracking_no;

                    if(scandata(vitem_nm)==true);
                    {
                        vitem_nm = m_item_cd;
                        vtracking_no = m_tracking_no;
                    }

                    item_info_query(vitem_nm,vPlant_CD,vtracking_no);
                    txt_item_cd.setText(ITEM_CD);
                    txt_item_nm.setText(ITEM_NM);
                    txt_sl_cd.setText(SL_NM);
                    txt_tracking_no.setText(TRACKING_NO);
                    txt_lot_no.setText(LOT_NO);
                    txt_inventory_stock_qty.setText(formattedStringPrice);


                    EditText txt_stockyard = (EditText) findViewById(R.id.txt_stockyard);
                    EditText txt_inventory_qty = (EditText) findViewById(R.id.txt_inventory_qty);
                    if (vPlant_CD.equals("C1")) {
                        txt_stockyard.requestFocus();
                    }
                    else {
                        txt_inventory_qty.requestFocus();
                    }

                    return true;
                }

                return false;
            }
        });

        txt_stockyard.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    EditText txt_inventory_qty = (EditText) findViewById(R.id.txt_inventory_qty);
                    txt_inventory_qty.requestFocus();
                    return true;
                }
                return false;
            }

        });


        txt_inventory_qty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {

                    Button btn_save = (Button) findViewById(R.id.btn_save);
                    btn_save.requestFocus();
                    return true;
                }

                return false;
            }
        });


        /*
        txt_tracking_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    EditText txt_lot_no = (EditText) findViewById(R.id.txt_lot_no);
                    txt_lot_no.requestFocus();
                    return true;
                }

                return false;
            }
        });

         */

        /*
        txt_lot_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    Button btn_save = (Button) findViewById(R.id.btn_save);
                    btn_save.requestFocus();
                    return true;
                }

                return false;
            }
        });
        */






        Button btn_list = findViewById(R.id.btn_save);   //findViewById = @+id/### 의 ###을 읽어와 데이터 바인딩
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText txt_stockyard = (EditText) findViewById(R.id.txt_stockyard);
                final EditText txt_item_cd = (EditText) findViewById(R.id.txt_item_cd);
                final EditText txt_inventory_qty = (EditText) findViewById(R.id.txt_inventory_qty);
                final EditText txt_tracking_no = (EditText) findViewById(R.id.txt_tracking_no);
                final EditText txt_lot_no = (EditText) findViewById(R.id.txt_lot_no);
                final TextView txt_item_nm = (TextView) findViewById(R.id.txt_item_nm);


                STOCKYARD = txt_stockyard.getText().toString();
                ITEM_CD = txt_item_cd.getText().toString();
                INVENTORY_QTY = txt_inventory_qty.getText().toString();
                TRACKING_NO = txt_tracking_no.getText().toString();
                LOT_NO = txt_lot_no.getText().toString();

                if(inventory_info_chk(INVENTORY_COUNT_DATE,STOCKYARD,ITEM_CD,TRACKING_NO,LOT_NO) == false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(I79_Activity.this);
                    builder.setTitle("재고실사등록");
                    builder.setMessage("동일한 품번의 재고실사 등록 내역이 존재합니다. 등록을 진행 하시겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener()
                    { @Override public void onClick(DialogInterface dialog, int which)
                    {
                        if(inventory_info_save(INVENTORY_COUNT_DATE,MAJOR_SL_CD,STOCKYARD,ITEM_CD,INVENTORY_QTY,TRACKING_NO,LOT_NO) == true)
                        {
                            MySoundPlayer.initSounds(getApplicationContext());
                            MySoundPlayer.play(MySoundPlayer.bee);
                            txt_stockyard.getText().clear();
                            txt_item_cd.getText().clear();
                            txt_inventory_qty.getText().clear();
                            txt_tracking_no.getText().clear();
                            txt_lot_no.getText().clear();
                            txt_item_nm.setText("");
                            txt_sl_cd.setText("");
                            txt_inventory_stock_qty.setText("");

                            txt_item_cd.requestFocus();
                        }
                        Toast.makeText(getApplicationContext(),"실사등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
                    } });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            txt_stockyard.getText().clear();
                            txt_item_cd.getText().clear();
                            txt_inventory_qty.getText().clear();
                            txt_tracking_no.getText().clear();
                            txt_lot_no.getText().clear();
                            txt_item_nm.setText("");
                            txt_sl_cd.setText("");
                            txt_inventory_stock_qty.setText("");

                            txt_item_cd.requestFocus();
                        }
                    });
                    builder.create().show();
                }
                else
                {
                    if(inventory_info_save(INVENTORY_COUNT_DATE,MAJOR_SL_CD,STOCKYARD,ITEM_CD,INVENTORY_QTY,TRACKING_NO,LOT_NO) == true)
                    {
                        //val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                        //toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 300)
                        MySoundPlayer.initSounds(getApplicationContext());
                        MySoundPlayer.play(MySoundPlayer.bee);

                        txt_stockyard.getText().clear();
                        txt_item_cd.getText().clear();
                        txt_inventory_qty.getText().clear();
                        txt_tracking_no.getText().clear();
                        txt_lot_no.getText().clear();
                        txt_item_nm.setText("");
                        txt_sl_cd.setText("");
                        txt_inventory_stock_qty.setText("");

                        txt_item_cd.requestFocus();

                    }
                    Toast.makeText(getApplicationContext(),"실사등록이 완료되었습니다.", Toast.LENGTH_LONG).show();






                }
            }
        });




    }

    public  boolean scandata(String pITEM_CD)
    {
        //1. 문자열 나누기 (VISS 코드와 RFID 태그 값이 동시 스캔 될 가능 성 있음( RFID 스캔 태크 설정 앱에서 시작,종료 문자를 * 로 설정.
        // 1.1 문자열에서 * 의 인덱스를 찾는다. (단, * 의 인덱스가 0이면 무시)

        if(pITEM_CD.length() == 0){
            return false;
        }

        //스캔 된 텍스트의 개행 문자를 #으로 변경.
        String vITEM_CD = pITEM_CD.replaceAll("\\r\\n|\\r|\\n",";");

        //# 문자 이후 데이터는 모두 버림, 즉, 한줄로 들어온 텍스트만 정상적인 값으로 판단.
        if(pITEM_CD.indexOf(";") >= 0) {
            vITEM_CD = pITEM_CD.substring(0, pITEM_CD.indexOf(";"));
        }

        int nIDX = pITEM_CD.indexOf(";",0); //2번째 칸부터(인덱스가 1)인 거 부터 <*> 검색한다.

        String val1 = "";
        String val2 = "";

        if (nIDX == -1 ) {
            val1 = pITEM_CD;
            val2 = "";
        }
        else{
            val1 = pITEM_CD.substring(0,nIDX);
            val2 = pITEM_CD.substring(nIDX + 1,pITEM_CD.length());
        }


        m_item_cd = val1;
        m_tracking_no = val2;


        return true;
    }

    public void item_info_query(final String pITEM_CD, final String pPLANT_CD, final String pTRACKING_NO) {
        Thread workingThread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {

                String sql = " XUSP_I79_ITEM_INFO_GET_LIST ";
                sql += " @PLANT_CD = '" + pPLANT_CD + "'";
                sql += " ,@ITEM_CD = '" + pITEM_CD + "'";
                sql += " ,@TRACKING_NO = '" + pTRACKING_NO + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

                ITEM_NM_INFO();
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ITEM_NM_INFO() {
        if (!sJson.equals("")) {

            try {
                JSONArray ja = new JSONArray(sJson);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    ITEM_CD = jObject.getString("ITEM_CD");
                    ITEM_NM = jObject.getString("ITEM_NM");
                    MAJOR_SL_CD = jObject.getString("MAJOR_SL_CD");
                    SL_NM = jObject.getString("SL_NM");
                    TRACKING_NO = jObject.getString("TRACKING_NO");
                    LOT_NO = jObject.getString("LOT_NO");
                    GOOD_ON_HAND_QTY = Integer.parseInt(jObject.getString("GOOD_ON_HAND_QTY"));

                    DecimalFormat myFormatter = new DecimalFormat("###,###");
                    formattedStringPrice = myFormatter.format(GOOD_ON_HAND_QTY);

                }
            } catch (JSONException ex) {
                TGSClass.AlterMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlterMessage(this, e1.getMessage());
            }
        }
    }

    public boolean inventory_info_save(final String pINVENTORY_COUNT_DATE, final String pMAJOR_SL_CD ,final String pSTOCKYARD, final String pITEM_CD, final String pINVENTORY_QTY, final String pTRACKING_NO, final String pLOT_NO) {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession)getApplication(); //전역 클래스

                String vParmID = getIntent().getStringExtra("pID");              //등록된 장비명이 로그인 아이디로 판단.
                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");
                String vDevice = getIntent().getStringExtra("pDEVICE");

                if(vParmID == null) vParmID = global.getLoginString();
                if(vPlant_CD == null) vPlant_CD = global.getPlantCDString();
                if(vDevice == null) vDevice = global.getmUnitCDString();

                String sql = " exec XUSP_I79_SET_LIST_DTL ";
                sql += "@UPPER_INV_NO = '" + UPPER_INV_NO + "'";
                sql += " ,@PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@ITEM_CD = '" + pITEM_CD + "'";
                sql += " ,@TRACKING_NO = '" + pTRACKING_NO + "'";
                sql += " ,@LOT_NO = '" + pLOT_NO + "'";
                sql += " ,@INV_QTY = " + pINVENTORY_QTY + "";
                sql += " ,@LOCATION = '" + pSTOCKYARD + "'";
                sql += " ,@USER_ID = '" + vParmID + "'";
                //sql += " ,@MAJOR_SL_CD = '" + pMAJOR_SL_CD + "'";

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

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean inventory_info_chk(final String pINVENTORY_COUNT_DATE, final String pSTOCKYARD, final String pITEM_CD, final String pTRACKING_NO, final String pLOT_NO) {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession)getApplication(); //전역 클래스

                String sql = " exec XUSP_I79_SET_LIST_CHK ";
                sql += "@INVENTORY_COUNT_DATE = '" + pINVENTORY_COUNT_DATE + "'";
                sql += " ,@ITEM_CD = '" + pITEM_CD + "'";
                sql += " ,@TRACKING_NO = '" + pTRACKING_NO + "'";
                sql += " ,@LOT_NO = '" + pLOT_NO + "'";
                sql += " ,@LOCATION = '" + pSTOCKYARD + "'";

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

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
            return false;
        }
        if (!sJson.equals("[]"))
        {
            return false;
        }
        return true;
    }

}
