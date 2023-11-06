package com.example.EM_KOREA.myapplication.I30;

import java.io.Serializable;

public class I37_DEL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String RECORD_NO;
    public  String SL_CD;
    public  String SL_NM;

    public  String GOOD_QTY;
    public  String LOCATION;



    public String getRECORD_NO() { return RECORD_NO; }
    public void setRECORD_NO(String record_no) { RECORD_NO = record_no; }

    public String getSL_CD() { return SL_CD; }
    public void setSL_CD(String sl_cd) { SL_CD = sl_cd; }

    public String getSL_NM() { return SL_NM; }
    public void setSL_NM(String sl_nm) { SL_NM = sl_nm; }

    public String getGOOD_QTY() { return GOOD_QTY; }
    public void setGOOD_QTY(String good_qty) { GOOD_QTY = good_qty; }

    public String getLOCATION() { return LOCATION; }
    public void setLOCATION(String location) { LOCATION = location; }


}
