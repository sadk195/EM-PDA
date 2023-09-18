package com.example.EM_KOREA.myapplication.S10;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.EM_KOREA.myapplication.BaseActivity;
import com.example.EM_KOREA.myapplication.DBAccess;
import com.example.EM_KOREA.myapplication.R;
import com.example.EM_KOREA.myapplication.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class S11_QUERY_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJSON_QUERY = "", sJSON_PRODT_ORDER_INFO = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText prodtorder_no, fr_dt, to_dt;

    //== View 선언(TextView) ==//
    private TextView item_cd, item_nm, tracking_no, prodt_qty, remain_qty;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_query;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal1, cal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s11_query);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 정의 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID값 바인딩 ==//
        img_barcode         = (ImageView) findViewById(R.id.img_barcode);
        prodtorder_no       = (EditText) findViewById(R.id.prodtorder_no);
        fr_dt               = (EditText) findViewById(R.id.fr_dt);
        to_dt               = (EditText) findViewById(R.id.to_dt);

        item_cd             = (TextView) findViewById(R.id.item_cd);
        item_nm             = (TextView) findViewById(R.id.item_nm);
        tracking_no         = (TextView) findViewById(R.id.tracking_no);
        prodt_qty           = (TextView) findViewById(R.id.prodt_qty);
        remain_qty          = (TextView) findViewById(R.id.remain_qty);

        listview            = (ListView) findViewById(R.id.listOrder);

        btn_query           = (Button) findViewById(R.id.btn_query);
    }

    private void initializeCalendar() {
        cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        cal1.add(Calendar.DAY_OF_MONTH, -7);

        cal2 = Calendar.getInstance();
        cal2.setTime(new Date());
    }

    private void initializeListener() {

        img_barcode.setOnClickListener(qrClickListener);

        fr_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, fr_dt, cal1);
            }
        });
        to_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, to_dt, cal2);
            }
        });

        prodtorder_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String prodtorder_no_st = prodtorder_no.getText().toString();

                    dbQuery_PRODT_ORDER_INFO(prodtorder_no_st);
                    SELECT_PRODT_ORDER_INFO(0);

                    start();
                    return true;
                }
                return false;
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodtorder_no_st = "";
                //String prodtorder_no_st = prodtorder_no.getText().toString();

                dbQuery_PRODT_ORDER_INFO(prodtorder_no_st);
                SELECT_PRODT_ORDER_INFO(0);

                start();     //조회 버튼을 눌렀을 때는 입력한 수불발생일 기간동안의 모든 제조오더 데이터가 나오도록 설계 되어있음
            }
        });
    }

    private void initializeData() {
        fr_dt.setText(df.format(cal1.getTime()));

        to_dt.setText(df.format(cal2.getTime()));
    }

    private void start() {
        dbQuery();

        try {
            JSONArray ja = new JSONArray(sJSON_QUERY);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            S11_QUERY_ListViewAdapter listViewAdapter = new S11_QUERY_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                S11_QUERY item = new S11_QUERY();

                item.PRODT_ORDER_NO   = jObject.getString("PRODT_ORDER_NO");              //제조오더번호
                item.DOCUMENT_DT      = jObject.getString("DOCUMENT_DT");                 //수불 발생일
                item.ITEM_CD          = jObject.getString("ITEM_CD");                     //품번
                item.ITEM_NM          = jObject.getString("ITEM_NM");                     //품명
                item.QTY              = jObject.getString("QTY");                         //출고수량
                item.TRACKING_NO      = jObject.getString("TRACKING_NO");                 //Tracking 번호
                item.SL_NM            = jObject.getString("SL_NM");                       //공장명
                item.WC_NM            = jObject.getString("WC_NM");                       //작업장명

                listViewAdapter.addQueryItem(item);
            }

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    //SELECT_PRODT_ORDER_INFO(position);
                }
            });

            TGSClass.AlterMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlterMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlterMessage(this, e1.getMessage());
        }
    }

    private void dbQuery() {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String prodtorder_no_st = prodtorder_no.getText().toString();
                String fr_dt_st = fr_dt.getText().toString();
                String to_dt_st = to_dt.getText().toString();

                String sql = " EXEC XUSP_MES_PRODT_ORDER_RESERVATION_INFO_GET ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + prodtorder_no_st + "'";
                sql += " ,@DOCUMENT_DT_FROM = '" + fr_dt_st + "'";
                sql += " ,@DOCUMENT_DT_TO = '" + to_dt_st + "'";
                sql += " ,@ITEM_CD = ''";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJSON_QUERY = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_dbQuery.start();   //스레드 시작
        try {
            workThd_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void dbQuery_PRODT_ORDER_INFO(final String prodt_order_no) {
        Thread workThread_QUERY_PRODT_ORDER_INFO = new Thread() {
            public void run() {
                String fr_dt_st = fr_dt.getText().toString();
                String to_dt_st = to_dt.getText().toString();

                String sql = " EXEC XUSP_MES_PRODT_ORDER_INFO_GET3 ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "' ";
                sql += " ,@DATE_FR = '" + fr_dt_st + "' ";
                sql += " ,@DATE_TO = '" + to_dt_st + "' ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJSON_PRODT_ORDER_INFO = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThread_QUERY_PRODT_ORDER_INFO.start();   //스레드 시작
        try {
            workThread_QUERY_PRODT_ORDER_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void SELECT_PRODT_ORDER_INFO(final int idx) {
        try {
            JSONArray ja = new JSONArray(sJSON_PRODT_ORDER_INFO);

            JSONObject jObject = ja.getJSONObject(idx);

            String item_cd_data             = jObject.getString("ITEM_CD");  //품번
            String item_nm_data             = jObject.getString("ITEM_NM");   //품명
            String tracking_no_data         = jObject.getString("TRACKING_NO");  //수량
            String prodt_order_qty_data     = jObject.getString("PRODT_ORDER_QTY");  //지시량
            String remain_qty_data          = jObject.getString("REMAIN_QTY");  //잔량

            item_cd.setText(item_cd_data);
            item_nm.setText(item_nm_data);
            tracking_no.setText(tracking_no_data);
            prodt_qty.setText(prodt_order_qty_data);
            remain_qty.setText(remain_qty_data);
        } catch (JSONException ex) {
            TGSClass.AlterMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlterMessage(this, e1.getMessage());
        }
    }
}