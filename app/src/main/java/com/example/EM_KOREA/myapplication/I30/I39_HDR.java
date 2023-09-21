package com.example.EM_KOREA.myapplication.I30;

import java.io.Serializable;

public class I39_HDR implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.


    private String PRODT_ORDER_NO;
    private String ITEM_CD;
    private String PRODT_ORDER_UNIT;
    private String ITEM_NM;
    private String SPEC;
    private String TRACKING_NO;
    private String PRODT_ORDER_QTY;
    private String GOOD_QTY;
    private String REMAIN_QTY;
    private String BAD_QTY;
    private String SL_CD;
    private String JOB_NM;
    private String OPR_NO;

    public String getPRODT_ORDER_NO() {
        return PRODT_ORDER_NO;
    }

    public String getITEM_CD() {
        return ITEM_CD;
    }

    public String getPRODT_ORDER_UNIT() {
        return PRODT_ORDER_UNIT;
    }

    public String getITEM_NM() {
        return ITEM_NM;
    }

    public String getSPEC() {
        return SPEC;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public String getPRODT_ORDER_QTY() {
        return PRODT_ORDER_QTY;
    }

    public String getGOOD_QTY() {
        return GOOD_QTY;
    }

    public String getBAD_QTY() {
        return BAD_QTY;
    }

    public String getREMAIN_QTY() {
        return REMAIN_QTY;
    }

    public String getSL_CD() {
        return SL_CD;
    }

    public String getJOB_NM() {
        return JOB_NM;
    }
    public String getOPR_NO() {
        return OPR_NO;
    }



    public void setPRODT_ORDER_NO(String prodt_order_no) {
        PRODT_ORDER_NO = prodt_order_no;
    }

    public void setITEM_CD(String item_cd) {
        ITEM_CD = item_cd;
    }

    public void setPRODT_ORDER_UNIT(String prodt_order_unit) {
        PRODT_ORDER_UNIT = prodt_order_unit;
    }

    public void setITEM_NM(String item_nm) {
        ITEM_NM = item_nm;
    }

    public void setSPEC(String spec) {
        SPEC = spec;
    }

    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public void setPRODT_ORDER_QTY(String prodt_order_qty) {
        PRODT_ORDER_QTY = prodt_order_qty;
    }

    public void setGOOD_QTY(String good_qty) {
        GOOD_QTY = good_qty;
    }

    public void setBAD_QTY(String bad_qty) {
        BAD_QTY = bad_qty;
    }

    public void setREMAIN_QTY(String remain_qty) {
        REMAIN_QTY = remain_qty;
    }

    public void setSL_CD(String sl_cd) {
        SL_CD = sl_cd;
    }

    public void setJOB_NM(String job_nm) {
        JOB_NM = job_nm;
    }
    public void setOPR_NO(String opr_no) {
        OPR_NO = opr_no;
    }


}




//PRODT_ORDER_NO
//ITEM_CD
//PRODT_ORDER_UNIT
//ITEM_NM
//SPEC
//TRACKING_NO
//PRODT_ORDER_QTY
//GOOD_QTY
//BAD_QTY
//REMAIN_QTY
//SL_CD
//SL_NM
//JOB_NM

