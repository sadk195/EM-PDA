package com.example.EM_KOREA.myapplication.I30;

import android.widget.CheckBox;

import java.io.Serializable;

public class I39_SET implements Serializable  {


    private String PRODT_ORDER_NO;
    private String OPR_NO;

    private String TRACKING_NO;
    private String JOB_NM;
    private String REQ_QTY;
    private String SEQ;

    private boolean CHK_OUT;
    CheckBox check =null;

    public boolean isCheck() {
        //return check.isChecked();
        return  CHK_OUT;
    }
    public void disCheckBox() {
        // check.setChecked(false);
        CHK_OUT=false;
    }

    public void setCheckBool(boolean b) {
        //this.check.setChecked(b);
        CHK_OUT = b;
    }

    public void CheckedCheckBox() {
        check.setChecked(true);
    }

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

    public String getREQ_QTY() {
        return REQ_QTY;
    }

    public void setREQ_QTY(String req_qty) {
        REQ_QTY = req_qty;
    }

    public String getJOB_NM() {
        return JOB_NM;
    }

    public void setJOB_NM(String job_nm) {
        JOB_NM = job_nm;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public String getSEQ() {
        return SEQ;
    }

    public void setSEQ(String seq) {
        SEQ = seq;
    }

    public boolean getCHK_OUT() {
        return CHK_OUT;
    }

    public void setCHK_OUT(boolean chk_out) {
        CHK_OUT = chk_out;
    }
}
