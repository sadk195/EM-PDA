package com.example.EM_KOREA.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class TestXmlPage extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Button btn_prev_layout, btn_next_layout;
    private int[] int_xml_list;
    private String[] str_xml_list;
    private int xml_idx;
    private String str;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_xml_page);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        int_xml_list        = getResources().getIntArray(R.array.int_activity_xml);
        str_xml_list        = getResources().getStringArray(R.array.str_activity_xml);

        btn_prev_layout     = (Button) findViewById(R.id.btn_prev_layout);
        btn_next_layout     = (Button) findViewById(R.id.btn_next_layout);

        //== 1. 기반이 되는 Layout ==//
        linearLayout        = (LinearLayout) findViewById(R.id.Linear_layout);
        //== 2. inflater 생성 ==//
        inflater            = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_prev_layout:
                        if (xml_idx > 0) {
                            xml_idx -= 1;
                            setLinearLayout();
                        } else {
                            TGSClass.AlterMessage(getApplicationContext(), "첫 번째 페이지 입니다.");
                        }
                        break;
                    case R.id.btn_next_layout:
                        if (xml_idx < str_xml_list.length - 1) {
                            xml_idx += 1;
                            setLinearLayout();
                        } else {
                            TGSClass.AlterMessage(getApplicationContext(), "마지막 페이지 입니다.");
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        btn_prev_layout.setOnClickListener(clickListener);
        btn_next_layout.setOnClickListener(clickListener);
    }

    private void initializeData() {
        if (str_xml_list.length > 0) {
            xml_idx = 0;

            setLinearLayout();
        } else {
            //== 3. 넣을 xml 파일명, 기반 layout 객체, true ==//
            inflater.inflate(R.layout.activity_new, linearLayout, false);
        }
    }

    private void setLinearLayout() {
        linearLayout.removeAllViews();

        int xml = getResources().getIdentifier(getPackageName() + ":layout/" + str_xml_list[xml_idx].toLowerCase(), null, null);
        inflater.inflate(xml, linearLayout, true);
    }
}