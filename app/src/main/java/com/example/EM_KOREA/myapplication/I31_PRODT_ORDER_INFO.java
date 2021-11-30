package com.example.EM_KOREA.myapplication;

import java.io.Serializable;

public class I31_PRODT_ORDER_INFO implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String ITEM_CD;
    public  String ITEM_NM;
    public  String TRACKING_NO;
    public  String PRODT_ORDER_QTY;
    public  String REMAIN_QTY;
    public  String GOOD_QTY;
    public  String BAD_QTY;

    public String getITEM_CD() { return ITEM_CD;}
    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM;}
    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getTRACKING_NO() { return TRACKING_NO; }
    public void setTRACKING_NO(String tracking_no) { TRACKING_NO = tracking_no; }

    public String getPRODT_ORDER_QTY() { return PRODT_ORDER_QTY; }
    public void setPRODT_ORDER_QTY(String prodt_order_qty) { PRODT_ORDER_QTY = prodt_order_qty; }

    public String getREMAIN_QTY() { return REMAIN_QTY; }
    public void setREMAIN_QTY(String remain_qty) { REMAIN_QTY = remain_qty; }

    public String getGOOD_QTY() { return GOOD_QTY; }
    public void setGOOD_QTY(String good_qty) { GOOD_QTY = good_qty; }

    public String getBAD_QTY() { return BAD_QTY; }
    public void setBAD_QTY(String bad_qty) { BAD_QTY = bad_qty; }
}
