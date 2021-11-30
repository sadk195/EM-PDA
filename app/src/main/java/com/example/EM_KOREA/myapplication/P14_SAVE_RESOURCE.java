package com.example.EM_KOREA.myapplication;

public class P14_SAVE_RESOURCE {

    private String FACILTY_CD;
    private String FACILTY_NM;


    public String getFACILTY_CD() {
        return FACILTY_CD;
    }

    public void setFACILTY_CD(String facitly_cd) { this.FACILTY_CD = facitly_cd; }

    public String getFACILTY_NM() {
        return FACILTY_NM;
    }

    public void setFACILTY_NM(String facitly_nm) {
        this.FACILTY_NM = facitly_nm;
    }

    @Override
    /* Spinner 위젯에서 보여줄 값 세팅*/
    public String toString() {

        return FACILTY_NM;

    }

}
