package com.example.EM_KOREA.myapplication;

public class GetComboNUM {
    private int NUM;
    private String MINOR_CD;
    private String MINOR_NM;

    public int getNUM() {
        return NUM;
    }

    public void setNUM(int num) { this.NUM = num; }

    public String getMINOR_CD() {
        return MINOR_CD;
    }

    public void setMINOR_CD(String minor_cd) { this.MINOR_CD = minor_cd; }

    public String getMINOR_NM() {
        return MINOR_NM;
    }

    public void setMINOR_NM(String minor_nm) {
        this.MINOR_NM = minor_nm;
    }

    @Override
    /* Spinner 위젯에서 보여줄 값 세팅*/
    public String toString()
    {
        return MINOR_NM;
    }

}
