package com.example.EM_KOREA.myapplication;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ErrorList_Popup extends Dialog {
    private TextView txt_contents,txt_name;
    private Button shutdownClick;

    public ErrorList_Popup(@NonNull final Context context, String name, String contents) {
        super(context);
        setContentView(R.layout.popup_errorlist);

        txt_name = findViewById(R.id.txt_name);
        txt_name.setText(name);

        txt_contents = findViewById(R.id.txt_contents);
        txt_contents.setText(contents);
        shutdownClick = findViewById(R.id.btn_shutdown);
        shutdownClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ActivityCompat.finishAffinity(this);
                PackageManager packageManager = context.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                context.startActivity(mainIntent);
                System.exit(0);
            }
        });

    }
}