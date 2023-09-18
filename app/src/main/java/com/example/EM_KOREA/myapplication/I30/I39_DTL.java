package com.example.EM_KOREA.myapplication.I30;

import java.io.Serializable;

public class I39_DTL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.



    private String ITEM_CD;
    private String ITEM_NM;
    private String LOCATION;
    private String SL_CD;
    private String GOOD_ON_HAND_QTY;
    private String BAD_ON_HAND_QTY;
    private String QTY;

    public String getITEM_CD() {
        return ITEM_CD;
    }

    public void setITEM_CD(String item_cd) {
        ITEM_CD = item_cd;
    }

    public String getITEM_NM() {
        return ITEM_NM;
    }

    public void setITEM_NM(String item_nm) {
        ITEM_NM = item_nm;
    }


    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String location) {
        LOCATION = location;
    }


    public String getSL_CD() {
        return SL_CD;
    }

    public void setSL_CD(String sl_cd) {
        SL_CD = sl_cd;
    }

    public String getGOOD_ON_HAND_QTY() {
        return GOOD_ON_HAND_QTY;
    }

    public void setGOOD_ON_HAND_QTY(String good_on_hand_qty) {
        GOOD_ON_HAND_QTY = good_on_hand_qty;
    }

    public String getBAD_ON_HAND_QTY() {
        return BAD_ON_HAND_QTY;
    }

    public void setBAD_ON_HAND_QTY(String bad_on_hand_qty) {
        BAD_ON_HAND_QTY = bad_on_hand_qty;
    }
    public String getQTY() {
        return QTY;
    }

    public void setQTY(String qty) {
        QTY = qty;
    }
}
