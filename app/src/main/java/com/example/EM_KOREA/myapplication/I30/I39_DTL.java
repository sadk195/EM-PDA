package com.example.EM_KOREA.myapplication.I30;

import java.io.Serializable;

public class I39_DTL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    private String PRODT_ORDER_NO;
    private String OPR_NO;
    private String ITEM_CD;
    private String ITEM_NM;
    private String SPEC;
    private String TRACKING_NO;
    private String LOCATION;
    private String JOB_NM;

    private String QTY;

    private String SEQ;


    public String getPRODT_ORDER_NO() {
        return PRODT_ORDER_NO;
    }

    public void setPRODT_ORDER_NO(String prodt_order_no) {
        PRODT_ORDER_NO = prodt_order_no;
    }

    public String getOPR_NO() {
        return OPR_NO;
    }

    public void setOPR_NO(String opr_no) {
        OPR_NO = opr_no;
    }

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
    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String location) {
        LOCATION = location;
    }
    public String getJOB_NM() {
        return JOB_NM;
    }

    public void setJOB_NM(String job_nm) {JOB_NM = job_nm;}
    public String getQTY() {
        return QTY;
    }

    public void setQTY(String qty) {QTY = qty;}
    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String seq) {SEQ = seq;}
}
//PRODT_ORDER_NO
//ITEM_CD
//PRODT_ORDER_UNIT
//ITEM_NM
//SPEC
//TRACKING_NO
//LOCATION
//JOB_NM