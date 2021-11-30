package com.example.EM_KOREA.myapplication;

import android.app.Application;

public class MySession extends Application {

    private String mLoginID;  //로그인 계정
    private String mUnitCD;   //단말기 코드
    private String mUserIP;   //접속 아이피
    private String mPlantCD;  //공장 코드
    private String mUnitType = "APK";

    public String getUnitType() {
        return mUnitType;
    }

    public void setUnitType(String mUnitType) {
        this.mUnitType = mUnitType;
    }

    public String getLoginString()
    {
        return mLoginID;
    }

    public void setLoginString(String pID)
    {
        this.mLoginID = pID;
    }

    public String getmUnitCDString()
    {
        return mUnitCD;
    }

    public void setUnitCDString(String pUnitCD)
    {
        this.mUnitCD = pUnitCD;
    }

    public String getUserIPString()
    {
        return mUserIP;
    }

    public void setUserIPString(String pIP)
    {
        this.mUserIP = pIP;
    }

    public String getPlantCDString()
    {
        return mPlantCD;
    }

    public void setPlantCDString(String pPlantCD)
    {
        this.mPlantCD = pPlantCD;
    }

}
