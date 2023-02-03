package com.example.EM_KOREA.myapplication;

import java.io.Serializable;

public class I27_DTL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    private String ITEM_CD;
    private String ITEM_NM;
    private String SPEC;
    private String SL_CD;
    private String SL_NM;
    private String GOOD_ON_HAND_QTY;
    private String BAD_ON_HAND_QTY;
    private String TRACKING_NO;

    private String LOT_NO;
    private String LOT_SUB_NO;
    private String BASIC_UNIT;
    private String LOCATION;
    private String DIVISION_NM;
    private String PROCUR_TYPE;

    public String MOVE_QTY;

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

    public String getSPEC() {
        return SPEC;
    }

    public void setSPEC(String spec) {
        SPEC = spec;
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
        SL_CD = sl_nm;
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
    }

    public String getLOT_SUB_NO() {
        return LOT_SUB_NO;
    }

    public void setLOT_SUB_NO(String lot_sub_no) {
        LOT_SUB_NO = lot_sub_no;
    }

    public String getBASIC_UNIT() {
        return BASIC_UNIT;
    }

    public void setBASIC_UNIT(String basic_unit) {
        BASIC_UNIT = basic_unit;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String location) {
        LOCATION = location;
    }


    public String getDIVISION_NM() {
        return DIVISION_NM;
    }

    public void setDIVISION_NM(String division_nm) {
        DIVISION_NM = division_nm;
    }

    public String getPROCUR_TYPE() {
        return PROCUR_TYPE;
    }

    public void setPROCUR_TYPE(String procur_type) {
        PROCUR_TYPE = procur_type;
    }

    public String getMOVE_QTY() {
        return MOVE_QTY;
    }

    public void setMOVE_QTY(String move_qty) {
        MOVE_QTY = move_qty;
    }


}
