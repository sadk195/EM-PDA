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

public class getRequeryData {

    public getRequeryData()
    {

    }

    static public String s_14_hdr_dn_req_no = "";

    public void S14_HDR(String get_dn_req_no)
    {
        s_14_hdr_dn_req_no = get_dn_req_no;
    }
}
