package com.example.EM_KOREA.myapplication.M20;

import android.os.Bundle;
import android.view.View;
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

public class M22_QUERY_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson;

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView lbl_view_title;

    //== View 선언(EditText) ==//
    private EditText from_dt, to_dt, prodt_order_no, sl_cd, item_cd, location;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode_prodt_order_no, img_barcode_sl_cd, img_barcode_item_cd, img_barcode_location;

    //== View 선언(Button) ==//
    private Button btn_query;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal1, cal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m22_query);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        /*
        String vMenuName = getIntent().getStringExtra("MENU_NM");
        sMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        sJobCode = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드
         */
        vMenuID                     = getIntent().getStringExtra("MENU_ID");
        vMenuNm                     = getIntent().getStringExtra("MENU_NM");
        vMenuRemark                 = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand               = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어온 코드

        //== ID 값 바인딩 ==//
        lbl_view_title              = (TextView) findViewById(R.id.lbl_view_title);

        to_dt                       = (EditText) findViewById(R.id.to_dt);
        from_dt                     = (EditText) findViewById(R.id.from_dt);
        prodt_order_no              = (EditText) findViewById(R.id.prodt_order_no);
        sl_cd                       = (EditText) findViewById(R.id.sl_cd);
        item_cd                     = (EditText) findViewById(R.id.item_cd);
        location                    = (EditText) findViewById(R.id.location);

        img_barcode_prodt_order_no  = (ImageView) findViewById(R.id.img_barcode_prodt_order_no);
        img_barcode_sl_cd           = (ImageView) findViewById(R.id.img_barcode_sl_cd);
        img_barcode_item_cd         = (ImageView) findViewById(R.id.img_barcode_item_cd);
        img_barcode_location        = (ImageView) findViewById(R.id.img_barcode_location);

        btn_query                   = (Button) findViewById(R.id.btn_query);   // 조회버튼

        listview                    = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeCalendar() {
        cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        cal1.add(Calendar.YEAR, -1);

        cal2 = Calendar.getInstance();
        cal2.setTime(new Date());
    }

    private void initializeListener() {
        img_barcode_prodt_order_no.setOnClickListener(qrClickListener);
        img_barcode_sl_cd.setOnClickListener(qrClickListener);
        img_barcode_item_cd.setOnClickListener(qrClickListener);
        img_barcode_location.setOnClickListener(qrClickListener);

        from_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, from_dt, cal1);
            }
        });
        to_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, to_dt, cal2);
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    private void initializeData() {
        lbl_view_title.setText(vMenuNm);

        from_dt.setText(df.format(cal1.getTime()));
        to_dt.setText(df.format(cal2.getTime()));
    }

    //== start ==//
    private void start() {
        dbQuery();

        if (!TGSClass.isJsonData(sJson)) return;

        if (!sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                M22_QUERY_ListViewAdapter ListViewAdapter = new M22_QUERY_ListViewAdapter(); //

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    M22_QUERY item = new M22_QUERY();

                    item.INSPECT_REQ_NO     = jObject.getString("INSPECT_REQ_NO");
                    item.INSP_QTY           = jObject.getString("INSP_QTY");
                    item.G_QTY              = jObject.getString("G_QTY");
                    item.B_QTY              = jObject.getString("B_QTY");
                    item.PRODT_ORDER_NO     = jObject.getString("PRODT_ORDER_NO");
                    item.LOCATION           = jObject.getString("LOCATION");

                    ListViewAdapter.addQueryItem(item);
                }
                listview.setAdapter(ListViewAdapter);

                TGSClass.AlterMessage(this, ja.length() + " 건 조회되었습니다.");
            } catch (JSONException ex) {
                TGSClass.AlterMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlterMessage(this, e1.getMessage());
            }
        } else {
            TGSClass.AlterMessage(getApplicationContext(), "조회할 항목이 없습니다.");
            return;
        }
    }

    //== dbQuery ==//
    private void dbQuery() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_dbQuery = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_APK_QM22_GET_LIST ";
                sql += "  @PLANT_CD = '" + vPLANT_CD + "'";
                sql += "  ,@START_DT = '" + from_dt.getText().toString() + "'";
                sql += "  ,@END_DT = '" + to_dt.getText().toString() + "'";
                sql += "  ,@PRODT_ORDER_NO = '" + prodt_order_no.getText().toString() + "'";
                sql += "  ,@SL_CD = '" + sl_cd.getText().toString() + "'";
                sql += "  ,@ITEM_CD = '" + item_cd.getText().toString() + "'";
                sql += "  ,@LOCATION = '" + location.getText().toString() + "'";

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
    }
}
