package com.example.EM_KOREA.myapplication;

import java.io.Serializable;

public class P14_PROD_ORDER_PRODUCTION_SEARCH implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String PLANT_CD;  //공장
    public  String PRODT_ORDER_NO;      //제조오더
    public  String OPR_NO;         //공순
    public  String ITEM_CD;     //품번
    public  String ITEM_NM;      //품명
    public  String PRODT_ORDER_QTY;    //제조오더 수량



    //제조오더
    public  String getPRODT_ORDER_NO(){return  PRODT_ORDER_NO;}

    public  void setPRODT_ORDER_NO(String prodt_order_no ){PRODT_ORDER_NO =  prodt_order_no;}

    //공순
    public  String getOPR_NO(){return  OPR_NO;}

    public  void setOPR_NO(String opr_no ){OPR_NO =  opr_no;}

    //품번
    public  String getITEM_CD(){return  ITEM_CD;}

    public  void setITEM_CD(String item_cd ){ITEM_CD =  item_cd;}

    //품명
    public  String getITEM_NM(){return  ITEM_NM;}

    public  void setITEM_NM(String item_nm  ){ITEM_NM =  item_nm;}

    //실적수량
    public  String getPRODT_ORDER_QTY(){return  PRODT_ORDER_QTY;}

    public  void setPRODT_ORDER_QTY(String prodt_order_qty ){PRODT_ORDER_QTY =  prodt_order_qty;}





}
