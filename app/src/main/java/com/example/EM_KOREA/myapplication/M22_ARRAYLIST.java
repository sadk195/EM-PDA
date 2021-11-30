package com.example.EM_KOREA.myapplication;

import java.io.Serializable;

public class M22_ARRAYLIST implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String PROD_DT;                 //생산실적일
    public  String ITEM_CD;                 //픔번
    public  String ITEM_NM;                 //품명
    public  String INSP_QTY;                //검사수량
    public  String INSPECT_GOOD_QTY;        //검사양품수량
    public  String INSPECT_BAD_QTY;         //검사불량수량
    public  String PRODT_ORDER_NO;                   //제조오더번호
    public  String INSPECT_REQ_NO;          //검사요청번호
    public  String Q_STS;                   //검사진행상태
    public  String INSPECTOR_CD; //검사원 코드
    public  String INSPECTOR_NM; //검사원 명



    public String getPROD_DT() {
        return PROD_DT;
    }
    public void setPROD_DT(String prod_dt) {
        PROD_DT = prod_dt;
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

    public String getINSP_QTY() {
        return INSP_QTY;
    }
    public void setINSP_QTY(String insp_qty) {
        INSP_QTY = insp_qty;
    }

    public String getINSPECT_GOOD_QTY() {
        return INSPECT_GOOD_QTY;
    }
    public void setINSPECT_GOOD_QTY(String inspect_good_qty) {
        INSPECT_GOOD_QTY = inspect_good_qty;
    }

    public String getINSPECT_BAD_QTY() {
        return INSPECT_BAD_QTY;
    }
    public void setINSPECT_BAD_QTY(String inspect_bad_qty) {
        INSPECT_BAD_QTY = inspect_bad_qty;
    }


    public String getINSPECT_REQ_NO() {
        return INSPECT_REQ_NO;
    }
    public void setINSPECT_REQ_NO(String inspect_req_no) {
        INSPECT_REQ_NO = inspect_req_no;
    }

    public String getQ_STS() {
        return Q_STS;
    }
    public void setQ_STS(String q_sts) {
        Q_STS = q_sts;
    }

    public String getPRODT_ORDER_NO() {
        return PRODT_ORDER_NO;
    }
    public void setPRODT_ORDER_NO(String prodt_order_no) {
        PRODT_ORDER_NO = prodt_order_no;
    }

    public String getINSPECTOR_CD() {
        return INSPECTOR_CD;
    }
    public void setINSPECTOR_CD(String inspector_cd) {
        INSPECTOR_CD = inspector_cd;
    }

    public String getINSPECTOR_NM() {
        return INSPECTOR_NM;
    }
    public void setINSPECTOR_NM(String inspector_nm) {
        INSPECTOR_NM = inspector_nm;
    }
}
