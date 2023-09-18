package com.example.EM_KOREA.myapplication.P10;

public class P14_SAVE_WORKER {

    private String WORKER_CD;
    private String WORKER_NM;


    public String getWORKER_CD() {
        return WORKER_CD;
    }

    public void setWORKER_CD(String worker_cd) { this.WORKER_CD = worker_cd; }

    public String getWORKER_NM() {
        return WORKER_NM;
    }

    public void setWORKER_NM(String worker_nm) {
        this.WORKER_NM = worker_nm;
    }

    @Override
    /* Spinner 위젯에서 보여줄 값 세팅*/
    public String toString() {

        return WORKER_NM;

    }

}
