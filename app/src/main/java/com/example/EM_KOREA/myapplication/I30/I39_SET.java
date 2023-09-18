package com.example.EM_KOREA.myapplication.I30;

import java.io.Serializable;

public class I39_SET implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.


    private String PRODT_ORDER_NO;
    private String ITEM_CD;
    private String ITEM_NM;
    private String REQ_QTY;
    private String ISSUED_QTY;
    private String LOCATION;
    private String SL_CD;
    private String OUT_QTY;
    private String TRACKING_NO;
    private String REMAIN_QTY;
    private String WMS_GOOD_ON_HAND_QTY;
    private boolean CHK_OUT;

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

    public String getREQ_QTY() {
        return REQ_QTY;
    }

    public void setREQ_QTY(String req_qty) {
        REQ_QTY = req_qty;
    }

    public String getISSUED_QTY() {
        return ISSUED_QTY;
    }

    public void setISSUED_QTY(String issued_qty) {
        ISSUED_QTY = issued_qty;
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

    public String getOUT_QTY() {
        return OUT_QTY;
    }

    public void setOUT_QTY(String out_qty) {
        OUT_QTY = out_qty;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public String getPRODT_ORDER_NO() {
        return PRODT_ORDER_NO;
    }

    public void setPRODT_ORDER_NO(String prodt_order_no) {
        PRODT_ORDER_NO = prodt_order_no;
    }

    public String getREMAIN_QTY() {
        return REMAIN_QTY;
    }

    public void setREMAIN_QTY(String remain_qty) {
        REMAIN_QTY = remain_qty;
    }

    public String getWMS_GOOD_ON_HAND_QTY() {
        return WMS_GOOD_ON_HAND_QTY;
    }

    public void setWMS_GOOD_ON_HAND_QTY(String wms_good_on_hand_qty) {
        WMS_GOOD_ON_HAND_QTY = wms_good_on_hand_qty;
    }
    public boolean getCHK_OUT() {
        return CHK_OUT;
    }

    public void setCHK_OUT(boolean chk_out) {
        CHK_OUT = chk_out;
    }
}
