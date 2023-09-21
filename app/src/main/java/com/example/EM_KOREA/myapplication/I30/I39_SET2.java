package com.example.EM_KOREA.myapplication.I30;

import java.io.Serializable;

public class I39_SET2 implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.


    private String ITEM_CD;
    private String ITEM_NM;

    private String TRACKING_NO;
    private String LOT_NO;

    private String LOT_SUB_NO;
    private String SL_CD;
    private String SL_NM;
    private String LOCATION;
    private String GOOD_QTY;

    private String BAD_QTY;

    private String BASIC_UNIT;


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

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public String getLOT_NO() {
        return LOT_NO;
    }

    public void setLOT_NO(String lot_no) {
        LOT_NO = lot_no;
    } public String getLOT_SUB_NO() {
        return LOT_SUB_NO;
    }

    public void setLOT_SUB_NO(String lot_sub_no) {
        LOT_SUB_NO = lot_sub_no;
    }

    public String getGOOD_QTY() {
        return GOOD_QTY;
    }

    public void setGOOD_QTY(String req_qty) {
        GOOD_QTY = req_qty;
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

    public String getSL_NM() {
        return SL_NM;
    }

    public void setSL_NM(String sl_nm) {
        SL_NM = sl_nm;
    }
    public String getBAD_QTY() {
        return BAD_QTY;
    }

    public void setBAD_QTY(String bad_qty) {
        BAD_QTY = bad_qty;
    }
    public String getBASIC_UNIT() {
        return BASIC_UNIT;
    }

    public void setBASIC_UNIT(String basic_unit) {
        BASIC_UNIT = basic_unit;
    }

}
