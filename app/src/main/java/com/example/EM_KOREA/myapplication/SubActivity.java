package com.example.EM_KOREA.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button btnPre = findViewById(R.id.btn_Pre);
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면 전환 (MainActivity 화면으로 전환)
                Intent intent =  TGSClass.ChangeView(getPackageName(),"MainActivity");
                startActivity(intent);

            }
        });

    }


}
