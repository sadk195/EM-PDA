package com.example.EM_KOREA.myapplication.P10;

public class P14_PROD_ORDER_WORK_CENTER {

    private String WC_CD;
    private String WC_NM;


    public String getWC_CD() {
        return WC_CD;
    }

    public void setWC_CD(String wc_cd) { this.WC_CD = wc_cd; }

    public String getWC_NM() {
        return WC_NM;
    }

    public void setWC_NM(String wc_nm) {
        this.WC_NM = wc_nm;
    }

    @Override
    /* Spinner 위젯에서 보여줄 값 세팅*/
    public String toString() {

        return WC_NM;

    }

}
