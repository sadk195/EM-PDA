package com.example.EM_KOREA.myapplication.P10;

import java.io.Serializable;

public class P14_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String PRODT_ORDER_NO;      //제조오더
    public  String OPR_NO;         //공순
    public  String ITEM_CD;     //품번




    //제조오더
    public  String getPRODT_ORDER_NO(){return  PRODT_ORDER_NO;}

    public  void setPRODT_ORDER_NO(String prodt_order_no ){PRODT_ORDER_NO =  prodt_order_no;}

    //공순
    public  String getOPR_NO(){return  OPR_NO;}

    public  void setOPR_NO(String opr_no ){OPR_NO =  opr_no;}

    //품번
    public  String getITEM_CD(){return  ITEM_CD;}

    public  void setITEM_CD(String item_cd ){ITEM_CD =  item_cd;}



}
