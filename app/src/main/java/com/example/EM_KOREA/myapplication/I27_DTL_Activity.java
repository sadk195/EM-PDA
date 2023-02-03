package com.example.EM_KOREA.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class I27_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonCombo = "", sJsonHDR = "", sJsonDTL = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I27_DTL vItem;

    //== View 선언(TextView) ==//
    private TextView  txt_item_nm, txt_spec,  txt_division_nm, txt_procur_type, txt_location,txt_good_on_hand_qty,txt_item_cd_t;

    //== View 선언(EditText) ==//
    private EditText txt_item_cd, txt_out_qty;

    //== View 선언(CheckBox) ==//
    private CheckBox chk_direct;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Spinner) ==//
    private Spinner cmb_origin,cmb_move;

    //== View 선언(Button) ==//
    private Button btn_open,btn_hide,btn_save,btn_add,btn_del,btn_clear;

    //== View 선언(Layout) ==//
    private LinearLayout box_view2;
    private RelativeLayout layout_btn;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    //== Dialog ==//
    //private DBQueryList dbQueryList;

    //== Spinner에 필요한 변수 ==//
    private String str_storage;

    //== itme 선택 시 몇 번째 item을 선택한지 판단하는 변수 선언 ==//
    private int sel_item_idx;

  /*  //== View 선언(DrawerLayout) ==//
    private DrawerLayout DrawerView;*/

    private String tx_item_cd="";
    private I27_DTL_ListViewAdapter listViewAdapter;

    private I27_DTL current_dtl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i27_dtl);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID                 = getIntent().getStringExtra("MENU_ID");
        vMenuNm                 = getIntent().getStringExtra("MENU_NM");
        vMenuRemark             = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand           = getIntent().getStringExtra("START_COMMAND");

        //vGetHDRItem             = (I27_HDR)getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        txt_item_nm             = (TextView) findViewById(R.id.txt_item_nm);
        txt_spec                = (TextView) findViewById(R.id.txt_spec);
        txt_division_nm         = (TextView) findViewById(R.id.txt_division_nm);
        txt_procur_type         = (TextView) findViewById(R.id.txt_procur_type);
        txt_location            = (TextView) findViewById(R.id.txt_location);
        txt_good_on_hand_qty    = (TextView) findViewById(R.id.txt_good_on_hand_qty);
        txt_item_cd_t           = (TextView) findViewById(R.id.txt_item_cd_t);

        txt_item_cd             = (EditText) findViewById(R.id.txt_item_cd);

        txt_out_qty             = (EditText) findViewById(R.id.txt_out_qty);
        listview                = (ListView) findViewById(R.id.listOrder);

        cmb_origin              = (Spinner) findViewById(R.id.cmb_origin);
        cmb_move                = (Spinner) findViewById(R.id.cmb_move);
        chk_direct              = (CheckBox) findViewById(R.id.chk_direct);

        btn_save                = (Button) findViewById(R.id.btn_save);
        btn_hide                = (Button) findViewById(R.id.btn_hide);
        btn_open                = (Button) findViewById(R.id.btn_open);
        btn_add                 = (Button) findViewById(R.id.btn_add);
        btn_del                 = (Button) findViewById(R.id.btn_del);
        btn_clear               = (Button) findViewById(R.id.btn_clear);
        //DrawerView       = (DrawerLayout) findViewById(R.id.drawer);

        box_view2            =(LinearLayout) findViewById(R.id.box_view2);

        //layout_btn              = (RelativeLayout) findViewById(R.id.layout_btn);
    }


    private void initializeCalendar() {
        cal = Calendar.getInstance();
        cal.setTime(new Date());
    }

    private void initializeListener() {

        //== 외부창고 Check 이벤트 ==//
        chk_direct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //== 체크박스 클릭 시 창고 목록(Spinner) 재조회
                if(b){
                    txt_out_qty.setVisibility(View.VISIBLE);
                    txt_out_qty.setEnabled(true);
                    btn_add.setEnabled(true);
                }
                else{
                    txt_out_qty.setVisibility(View.GONE);
                    txt_out_qty.setEnabled(false);
                    btn_add.setEnabled(false);
                }
            }
        });

        btn_open.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                box_view2.setVisibility(View.VISIBLE);
            }
        });
        btn_hide.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                box_view2.setVisibility(View.GONE);
            }
        });

        txt_item_cd.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                   /* String temp=txt_item_cd.getText().toString().replaceFirst(tx_item_cd,"");
                    txt_item_cd.setText(temp);
                    tx_item_cd=txt_item_cd.getText().toString();*/

                    //직접입력이 아닐때만
                   /* if(!chk_direct.isChecked()){
                        txt_out_qty.setText("1");
                        dbQueryList();
                    }*/

                    dbQueryList();

                    txt_item_cd.setText("");
                    txt_item_cd.requestFocus();
                    //start();
                    return true;
                }
                return false;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존 소스 dbSave()로 옮김
                dbSave("EXIT");
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(current_dtl == null){
                    return;
                }

                if(Integer.parseInt(current_dtl.getGOOD_ON_HAND_QTY()) < Integer.parseInt(txt_out_qty.getText().toString())){
                    TGSClass.AlterMessage(getApplicationContext(), "출고수량이 재고수량보다 많습니다.",5000);
                    return;
                }

                //직접입력일때만
                if(chk_direct.isChecked()){
                    current_dtl.setMOVE_QTY(txt_out_qty.getText().toString());

                    //이동 수량
                    listViewAdapter.addDTLItem(current_dtl);
                    listview.setAdapter(listViewAdapter);
                    listViewAdapter.notifyDataSetChanged();


                    txt_item_nm.setText("");
                    txt_spec.setText("");
                    txt_good_on_hand_qty.setText("");
                    txt_division_nm.setText("");
                    txt_procur_type.setText("");
                    txt_location.setText("");
                    txt_out_qty.setText("");
                    txt_item_cd.setText("");
                    txt_item_cd_t.setText("");
                }

            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewAdapter.delDTLItem(sel_item_idx);
                listViewAdapter.notifyDataSetChanged();

                txt_item_nm.setText("");
                txt_spec.setText("");
                txt_good_on_hand_qty.setText("");
                txt_division_nm.setText("");
                txt_procur_type.setText("");
                txt_location.setText("");
                txt_item_cd_t.setText("");

            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_item_cd.setText("");
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                vItem = (I27_DTL) parent.getItemAtPosition(position);
                //== 선택 된 item의 position을 전역변수에 저장(재고이동시 선택한 item의 position값이 필요함) ==//
                sel_item_idx = position;

                //txt_item_cd.setText(vItem.getITEM_CD());
                txt_item_nm.setText(vItem.getITEM_NM());
                txt_spec.setText(vItem.getSPEC());
                txt_division_nm.setText(vItem.getDIVISION_NM());
                txt_procur_type.setText(vItem.getPROCUR_TYPE());
                txt_location.setText(vItem.getLOCATION());
                txt_item_cd_t.setText(vItem.getITEM_CD());

            }
        });

    }

    private void initializeData() {
        //콤보박스 데이터 조회
        setCmb_move();
        setCmb_origin();
        //dbQuery_getComboData();

        start();
    }



    private void start() {
        sel_item_idx = 0;

        txt_out_qty.setEnabled(false);
        btn_add.setEnabled(false);
        txt_out_qty.setVisibility(View.GONE);

        listViewAdapter = new I27_DTL_ListViewAdapter();
    }

    public void dbQueryList() {
      /*  progressStart(this);

        dbQueryList = new DBQueryList();
        dbQueryList.start();*/

        GetComboNUM spinner_origin = (GetComboNUM) cmb_origin.getSelectedItem();
        if(spinner_origin == null){
            TGSClass.AlterMessage(getApplicationContext(), "현재 창고를 입력하여 주시기 바랍니다.",5000);
            return;
        }

        if (spinner_origin.getMINOR_CD().equals("")) {
            TGSClass.AlterMessage(getApplicationContext(), "현재 창고를 입력하여 주시기 바랍니다.",5000);
            return;
        }

        String origin=  spinner_origin.getMINOR_CD();
        dbquery(origin);
        cmb_move.setEnabled(false);
        cmb_origin.setEnabled(false);

    }


    //LOT번호 스캔하여 데이터 저장
    private void dbquery(final String sl_cd) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {


                String sql = " EXEC XUSP_APK_I27_GET_HDR_LIST ";
                sql += " @PLANT_CD = '" + "H1" + "' ";
                sql += ", @ITEM_CD = '" + txt_item_cd.getText().toString() + "'";
                sql += ", @SL_CD = '" + sl_cd + "'";

                System.out.println("sql:"+sql);
                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
                //handler.sendMessage(handler.obtainMessage());

            }
        };
        wkThd_dbQuery.start();   //스레드 시작

        try {
            wkThd_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();

            for (int idx = 0; idx < 1; idx++) {
                JSONObject jObject = ja.getJSONObject(idx);//

                I27_DTL item = new I27_DTL();

                item.setITEM_CD           (jObject.getString("ITEM_CD"));                             // 품번
                item.setITEM_NM           (jObject.getString("ITEM_NM"));                             // 품명
                item.setSL_CD             (jObject.getString("SL_CD"));                               // 창고코드
                item.setSL_NM             (jObject.getString("SL_NM"));                               // 창고명
                item.setGOOD_ON_HAND_QTY  (decimalForm.format(jObject.getInt("GOOD_ON_HAND_QTY")));   // 양품수량
                item.setBAD_ON_HAND_QTY   (decimalForm.format(jObject.getInt("BAD_ON_HAND_QTY")));    // 불량품수량
                item.setTRACKING_NO       (jObject.getString("TRACKING_NO"));                         // T/K
                item.setLOT_NO            (jObject.getString("LOT_NO"));                              // LOT번호
                item.setLOT_SUB_NO        (jObject.getString("LOT_SUB_NO"));                          // LOT순번
                item.setBASIC_UNIT        (jObject.getString("BASIC_UNIT"));                          // 재고단위
                item.setLOCATION          (jObject.getString("LOCATION"));                            // 적치장
                item.setMOVE_QTY          (txt_out_qty.getText().toString());


                txt_item_cd_t.setText(item.getITEM_CD());
                txt_item_nm.setText(item.getITEM_NM());
                txt_spec.setText(item.getSPEC());
                txt_good_on_hand_qty.setText(item.getGOOD_ON_HAND_QTY());
                txt_division_nm.setText(item.getDIVISION_NM());
                txt_procur_type.setText(item.getPROCUR_TYPE());
                txt_location.setText(item.getLOCATION());
                current_dtl=item;
            }

            //직접입력일때만
            if(!chk_direct.isChecked()){
                current_dtl.setMOVE_QTY("1");
                txt_out_qty.setText("1");
                //이동 수량
                listViewAdapter.addDTLItem(current_dtl);
                listview.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
            }



        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }
        catch (JSONException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlterMessage(getApplicationContext(), e1.getMessage());
        }
    }
/*
    private class DBQueryList extends Thread {
        @Override
        public void run() {
            try {
                String sql = " EXEC XUSP_APK_I27_GET_HDR_LIST ";
                sql += " @PLANT_CD = '" + "H1" + "' ";
                sql += ", @ITEM_CD = '" + txt_item_cd.getText().toString() + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
                handler.sendMessage(handler.obtainMessage());
            }
            catch (Exception ex) {
                TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
                progressEnd();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            boolean retry = true;
            while (retry) {
                try {
                    dbQueryList.join();

                    JSONArray ja = new JSONArray(sJson);

                    // 빈 데이터 리스트 생성.
                    //final ArrayList<String> items = new ArrayList<String>();
                    listViewAdapter = new I27_DTL_ListViewAdapter();
                    for (int idx = 0; idx < 1; idx++) {
                        JSONObject jObject = ja.getJSONObject(idx);//

                        I27_DTL item = new I27_DTL();

                        item.setITEM_CD           (jObject.getString("ITEM_CD"));                             // 품번
                        item.setITEM_NM           (jObject.getString("ITEM_NM"));                             // 품명
                        item.setSL_CD             (jObject.getString("SL_CD"));                               // 창고코드
                        item.setSL_NM             (jObject.getString("SL_NM"));                               // 창고명
                        item.setGOOD_ON_HAND_QTY  (decimalForm.format(jObject.getInt("GOOD_ON_HAND_QTY")));   // 양품수량
                        item.setBAD_ON_HAND_QTY   (decimalForm.format(jObject.getInt("BAD_ON_HAND_QTY")));    // 불량품수량
                        item.setTRACKING_NO       (jObject.getString("TRACKING_NO"));                         // T/K
                        item.setLOT_NO            (jObject.getString("LOT_NO"));                              // LOT번호
                        item.setLOT_SUB_NO        (jObject.getString("LOT_SUB_NO"));                          // LOT순번
                        item.setBASIC_UNIT        (jObject.getString("BASIC_UNIT"));                          // 재고단위
                        item.setLOCATION          (jObject.getString("LOCATION"));                            // 적치장
                        item.setMOVE_QTY          (txt_out_qty.getText().toString());                                //이동 수량
                        listViewAdapter.addDTLItem(item);
                    }
                    listview.setAdapter(listViewAdapter);

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View v, int position, long id) {
                            vItem = (I27_DTL) parent.getItemAtPosition(position);
                            //== 선택 된 item의 position을 전역변수에 저장(재고이동시 선택한 item의 position값이 필요함) ==//
                            sel_item_idx = position;

                            //txt_item_cd.setText(vItem.getITEM_CD());
                            txt_item_nm.setText(vItem.getITEM_NM());
                            txt_spec.setText(vItem.getSPEC());
                            txt_division_nm.setText(vItem.getDIVISION_NM());
                            txt_procur_type.setText(vItem.getPROCUR_TYPE());
                            txt_location.setText(vItem.getLOCATION());



                        }
                    });
                } catch (JSONException ex) {
                    TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
                } catch (Exception e1) {
                    TGSClass.AlterMessage(getApplicationContext(), e1.getMessage());
                }
                progressEnd();

                retry = false;
            }
        }
    };*/

    //이동창고 스피너 세팅
    private void setCmb_move(){
        dbQuery_getComboData();
        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboNUM> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboNUM itemBase = new GetComboNUM();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                GetComboNUM item = new GetComboNUM();
                item.setNUM(jObject.getInt("NUM"));
                item.setMINOR_CD(jObject.getString("CODE"));
                item.setMINOR_NM(jObject.getString("NAME"));

                lstItem.add(item);
            }
            ArrayAdapter<GetComboNUM> adapter = new ArrayAdapter<GetComboNUM>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            cmb_move.setAdapter(adapter);
            //로딩시 기본값 세팅
            cmb_move.setSelection(adapter.getPosition(itemBase));
            //콤보박스 클릭 이벤트 정의
            cmb_move.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    str_storage = ((GetComboNUM) parent.getSelectedItem()).getMINOR_CD();
                    //Start();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (JSONException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }
    }
    //기존창고 스피너 세팅
    private void setCmb_origin(){
        dbQuery_getComboData();
        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboNUM> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboNUM itemBase = new GetComboNUM();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                GetComboNUM item = new GetComboNUM();
                item.setNUM(jObject.getInt("NUM"));
                item.setMINOR_CD(jObject.getString("CODE"));
                item.setMINOR_NM(jObject.getString("NAME"));

                lstItem.add(item);
            }
            ArrayAdapter<GetComboNUM> adapter = new ArrayAdapter<GetComboNUM>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            cmb_origin.setAdapter(adapter);
            //로딩시 기본값 세팅
            cmb_origin.setSelection(adapter.getPosition(itemBase));
            //콤보박스 클릭 이벤트 정의
            cmb_origin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    str_storage = ((GetComboNUM) parent.getSelectedItem()).getMINOR_CD();
                    //Start();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (JSONException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }
    }
    //창고 스피너 데이터 조회
    private void dbQuery_getComboData() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_APK_STORAGE_GET_COMBODATA ";
                sql += " @FLAG= 'Y'";
                sql += ", @PLANT_CD= '" + "H1" + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonCombo = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        workThd_dbQuery_getComboData.start();   //스레드 시작
        try {
            workThd_dbQuery_getComboData.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
        }


    }

    private void dbSave(String val) {
        try {

            GetComboNUM spinner_move = (GetComboNUM) cmb_move.getSelectedItem();
            GetComboNUM spinner_origin = (GetComboNUM) cmb_origin.getSelectedItem();

            if (spinner_move.getMINOR_CD().equals("")) {
                TGSClass.AlterMessage(getApplicationContext(), "이동 창고를 입력하여 주시기 바랍니다.",5000);
                return;
            } else
            if (spinner_origin.getMINOR_CD().equals(spinner_move.getMINOR_CD())) {
                TGSClass.AlterMessage(getApplicationContext(), "동일한 창고로 이동할 수 없습니다.",5000);
                return;
            }

            int idx =0;
            ArrayList<Integer> s_idxs = new ArrayList<>();
            String msg ="";
            for(I27_DTL item :  (ArrayList<I27_DTL>)listViewAdapter.getItems()) {

                //I27_DTL_ListViewAdapter listViewAdapter = (I27_DTL_ListViewAdapter) listview.getAdapter();
                //I27_DTL item = (I27_DTL) listViewAdapter.getItem(sel_item_idx);

                double double_move_qty = Double.parseDouble(item.getMOVE_QTY());
                double qty = Double.parseDouble(item.getGOOD_ON_HAND_QTY());

                if (qty < double_move_qty) {
                    msg += "(" + item.getITEM_CD() + ")" + item.getITEM_NM() + "의 현재고수량보다 이동수량이 초과되었습니다."+"\n\r";
                    //TGSClass.AlterMessage(getApplicationContext(), "현재고수량보다 이동수량이 초과되었습니다.");
                    continue;
                }


                String sl_cd = item.getSL_CD();
                String item_cd = item.getITEM_CD();
                String tracking_no = item.getTRACKING_NO();
                String lot_no = item.getLOT_NO();
                String lot_sub_no = item.getLOT_SUB_NO();
                String move_qty = item.getMOVE_QTY();

                //== STEP 1. BL 실행 ==//
                BL_DATASET_SELECT(sl_cd, item_cd, tracking_no, lot_no, lot_sub_no,move_qty);
                System.out.println("result_msg:"+result_msg+":");
                //== STEP 2. BL 실행 후 반환 메시지 확인 ==//
                if (!result_msg.contains("재고이동 성공")) {
                    msg +=result_msg;
                    //TGSClass.AlterMessage(getApplicationContext(), "(" + item.getITEM_CD() + ")" + item.getITEM_NM() + " 재고이동 실패");
                    continue;
                }
                else{
                    //== STEP 3. 뭐해야할까? ==//
                    if (!dbSave_HDR()) {
                        msg +="(" + item.getITEM_CD() + ")" + item.getITEM_NM() + " 수불대장 저장 실패";
                        TGSClass.AlterMessage(getApplicationContext(), "(" + item.getITEM_CD() + ")" + item.getITEM_NM() + " 수불대장 저장 실패");
                        continue;
                    }

                    try {
                        JSONArray ja_HDR = new JSONArray(sJsonHDR);
                        JSONObject jObj_HDR = ja_HDR.getJSONObject(0);

                        String RTN_ITEM_DOCUMENT_NO = jObj_HDR.getString("RTN_ITEM_DOCUMENT_NO");
                        String dtl_sl_cd = item.getSL_CD();
                        String dtl_item_cd = item.getITEM_CD();
                        String dtl_tracking_no = item.getTRACKING_NO();
                        String dtl_lot_no = item.getLOT_NO();
                        String dtl_lot_sub_no = item.getLOT_SUB_NO();
                        String dtl_qty = item.getGOOD_ON_HAND_QTY();
                        String dtl_basic_unit = item.getBASIC_UNIT();
                        String dtl_location = item.getLOCATION();
                        String dtl_bad_on_hand_qty = item.getBAD_ON_HAND_QTY();

                        dbSave_DTL(RTN_ITEM_DOCUMENT_NO, dtl_sl_cd, dtl_item_cd, dtl_tracking_no, dtl_lot_no, dtl_lot_sub_no,
                                dtl_qty, dtl_basic_unit, dtl_location, dtl_bad_on_hand_qty,move_qty);
                        System.out.println("dtl");

                    } catch (JSONException exJson) {
                        TGSClass.AlterMessage(getApplicationContext(), exJson.getMessage());
                    } catch (Exception ex) {
                        TGSClass.AlterMessage(getApplicationContext(), ex.getMessage());
                    }

                    //재고이동 메시지 추가
                    msg += "(" + item.getITEM_CD() + ")" + item.getITEM_NM() + "의 재고가 이동되었습니다."+"\n\r";

                    //재고이동 완료된항목 리스트에 추가하여 재고이동 완료된 항목은 삭제
                    s_idxs.add(idx);

                }
                idx ++;
            }

            // 리스트 거꾸로 정렬하기위한 리스트
            ArrayList<Integer> r_idx = new ArrayList<>();

            //리스트의 뒤부도 조회하여 새로운 리스트에 입력
            for (int i = s_idxs.size() - 1;i >= 0; i--) {
                r_idx.add(s_idxs.indexOf(i));

            }

            //뒤쪽 항목부터 삭제해야 인덱스 오류가 없음
            for(int i : r_idx){
                //재고이동 완료된 항목은 삭제처리
                listViewAdapter.RemoveDTLItem(i);
                listViewAdapter.notifyDataSetChanged();
            }

            TGSClass.AlterMessage(getApplicationContext(), msg, 10000);
            /*//== STEP 4. 현재 Activity 종료 ==//
            // 저장 후 결과 값 돌려주기
            Intent resultIntent = new Intent();
            // 결과처리 후 부른 Activity에 보낼 값
            resultIntent.putExtra("SIGN", val);
            // 부른 Activity에게 결과 값 반환
            setResult(RESULT_OK, resultIntent);
            // 현재 Activity 종료
            finish();*/
        } catch (Exception e1) {
            TGSClass.AlterMessage(getApplicationContext(), e1.getMessage());
            System.out.println("err:"+e1);
        }

    }

    //== BL 만드는 메서드(I22_DTL 참고) ==//
    private boolean BL_DATASET_SELECT(final String SL_CD, final String ITEM_CD, final String TRACKING_NO, final String LOT_NO, final String LOT_SUB_NO,final String MOVE_QTY) {
        Thread wkThd_BL_DATASET_SELECT = new Thread() {
            public void run() {

                String str_move_date    = getCurrentDate();
                //str_move_date    = "2022-12-26"
                System.out.println("str_move_date:"+str_move_date);
                GetComboNUM spinner_move= (GetComboNUM) cmb_move.getSelectedItem();
                GetComboNUM spinner_origin= (GetComboNUM) cmb_origin.getSelectedItem();

                //String str_move_qty     = txt_out_qty.getText().toString();
                //String str_move_qty     = MOVE_QTY;
                String cud_flag         = "C";
                String plant_cd         = "H1";
                String trans_plant_cd   = "H1";
                String item_cd          = ITEM_CD;
                String tracking_no      = TRACKING_NO;
                String trns_tracking_no = TRACKING_NO;
                String lot_no           = LOT_NO;
                int lot_sub_no          = Integer.parseInt(LOT_SUB_NO);
                String major_sl_cd      = spinner_origin.getMINOR_CD();
                String issued_sl_cd     = spinner_move.getMINOR_CD();
                //String qty              = str_move_qty;
                String qty              = MOVE_QTY;
                String document_dt      = str_move_date;

                System.out.println("item_cd:"+item_cd+":");
                System.out.println("tracking_no:"+tracking_no+":");
                System.out.println("trns_tracking_no:"+trns_tracking_no+":");
                System.out.println("lot_no:"+lot_no+":");
                System.out.println("lot_sub_no:"+lot_sub_no+":");
                System.out.println("major_sl_cd:"+major_sl_cd+":");
                System.out.println("issued_sl_cd:"+issued_sl_cd+":");
                System.out.println("qty:"+qty+":");
                System.out.println("document_dt:"+document_dt+":");



                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("plant_cd");
                parm2.setValue(plant_cd);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("trans_plant_cd");
                parm3.setValue(trans_plant_cd);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("item_cd");
                parm4.setValue(item_cd);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("tracking_no");
                parm5.setValue(tracking_no);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("trns_tracking_no");
                parm6.setValue(trns_tracking_no);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("lot_no");
                parm7.setValue(lot_no);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("lot_sub_no");
                parm8.setValue(lot_sub_no);
                parm8.setType(Integer.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("major_sl_cd");
                parm9.setValue(major_sl_cd);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("issued_sl_cd");
                parm10.setValue(issued_sl_cd);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("qty");
                parm11.setValue(qty);
                parm11.setType(String.class);

                PropertyInfo parm12 = new PropertyInfo();
                parm12.setName("document_dt");
                parm12.setValue(document_dt);
                parm12.setType(String.class);

                PropertyInfo parm13 = new PropertyInfo();
                parm13.setName("unit_cd");
                parm13.setValue(vUNIT_CD);
                parm13.setType(String.class);

                PropertyInfo parm14 = new PropertyInfo();
                parm14.setName("user_id");
                parm14.setValue(vUSER_ID);
                parm14.setType(String.class);

                pParms.add(parm);
                pParms.add(parm2);
                pParms.add(parm3);
                pParms.add(parm4);
                pParms.add(parm5);
                pParms.add(parm6);
                pParms.add(parm7);
                pParms.add(parm8);
                pParms.add(parm9);
                pParms.add(parm10);
                pParms.add(parm11);
                pParms.add(parm12);
                pParms.add(parm13);
                pParms.add(parm14);

                result_msg = dba.SendHttpMessage("BL_Set_INVENTORY_MOVE_ANDROID", pParms);
            }


        };
        wkThd_BL_DATASET_SELECT.start();   //스레드 시작
        try {
            wkThd_BL_DATASET_SELECT.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }

    //== 수불정보_HDR 저장(?) ==//
    private boolean dbSave_HDR() {
        Thread wkThd_dbSave_HDR = new Thread() {
            public void run() {
                //String str_move_date = move_date.getText().toString();
                String str_move_date = getCurrentDate();
                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@RTN_ITEM_DOCUMENT_NO = ''";
                sql += ",@DOCUMENT_YEAR = ''";
                sql += ",@TRNS_TYPE = 'ST'";
                sql += ",@MOV_TYPE = 'T61'";
                sql += ",@DOCUMENT_DT = '" + str_move_date + "'";
                sql += ",@RECORD_NO = ''";

                sql += ",@PLANT_CD = '" + "H1" + "'";
                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";

                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonHDR = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        wkThd_dbSave_HDR.start();   //스레드 시작
        try {
            wkThd_dbSave_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(), "catch : ex");
        }

        if (sJsonHDR.equals("[]")) {
            return false;
        } else {
            return true;
        }
    }

    //== 수불정보_DTL 저장(?) ==//
    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO, final String LOT_NO,
                               final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT, final String LOCATION, final String BAD_ON_HAND_QTY,final  String move_qty) {
        Thread wkThd_dbSave_DTL = new Thread() {
            public void run() {
                GetComboNUM spinner_move= (GetComboNUM) cmb_move.getSelectedItem();
                GetComboNUM spinner_origin= (GetComboNUM) cmb_origin.getSelectedItem();

                //String str_move_date = move_date.getText().toString();
                String str_move_date = getCurrentDate();

                //String str_move_qty = txt_out_qty.getText().toString();

                String str_move_qty = move_qty;
                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_SET ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";        // 채번
                sql += ",@DOCUMENT_YEAR = ''";                                  // 년도
                sql += ",@TRNS_TYPE = 'ST'";                                      // 변경유형
                sql += ",@MOV_TYPE = 'T61'";                                       // 이동유형
                sql += ",@PLANT_CD = '" + "H1" + "'";                      // 공장코드

                sql += ",@DOCUMENT_DT = '" + str_move_date + "'";               // 이동일자

                /* I_ONHAND_STOCK_DETAIL 에서 바인딩 받아야 하므로 ListView에 조회되도록 SELECT 프로시저에 DTL항목 추가하고 바인딩 한 후 가져와야됨*/
                sql += ",@SL_CD = '" + spinner_origin.getMINOR_CD() + "'";                             // 창고코드
                sql += ",@ITEM_CD = '" + ITEM_CD + "'";                         // 품목코드
                sql += ",@TRACKING_NO = '" + TRACKING_NO + "'";                 // TRACKING_NO
                sql += ",@LOT_NO = '" + LOT_NO + "'";                           // LOT_NO
                sql += ",@LOT_SUB_NO = " + LOT_SUB_NO;                          // LOT_SUB_NO
                sql += ",@QTY = " + QTY;                                        // 양품수량
                sql += ",@BASE_UNIT = '" + BASIC_UNIT + "'";                    // 재고단위
                sql += ",@LOC_CD = '" + LOCATION + "'";                         // 기존의 적치장
                sql += ",@TRNS_TRACKING_NO = '" + TRACKING_NO + "'";            /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_NO = '" + LOT_NO + "'";                      /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_SUB_NO = " + LOT_SUB_NO;                     /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + "H1" + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + spinner_move.getMINOR_CD() + "'";  /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/

                sql += ",@RECORD_NO = ''";                                      // 제조오더
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'I27_DTL_Activity'";

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;                // 불량 수량
                sql += ",@MOVE_QTY = " + str_move_qty;                          // 이동 수량

                sql += ",@TRNS_LOC_CD = '*'";                                   // 이동할 적치장
                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonDTL = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        wkThd_dbSave_DTL.start();   //스레드 시작
        try {
            wkThd_dbSave_DTL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }

    public static String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        return format.format(currentTime);
    }

}