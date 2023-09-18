package com.example.EM_KOREA.myapplication.P10;

import java.io.Serializable;

public class P14_ORDER_PRODUCTION_RESULT implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String PRODT_ORDER_NO;      //제조오더
    public  String PLANT_CD;  //공장
    public  String ITEM_CD;     //품목
    public  String ITEM_GROUP;         //상위품목
    public  String OPR_NO;         //공순
    public  String SEQ;   //공정순서
    public  String PRODT_QTY_IN_ORDER_UNIT;   //실적량
    public  String RCPT_QTY_IN_ORDER_UNIT;    //제조오더 수량



    //제조오더
    public  String getPRODT_ORDER_NO(){return  PRODT_ORDER_NO;}

    public  void setPRODT_ORDER_NO(String prodt_order_no ){PRODT_ORDER_NO =  prodt_order_no;}

    //공장
    public  String getPLANT_CD(){return  PLANT_CD;}

    public  void setPLANT_CD(String plant_cd ){PLANT_CD =  plant_cd;}

    //품명
    public  String getITEM_CD(){return  ITEM_CD;}

    public  void setITEM_CD(String item_cd ){ITEM_CD =  item_cd;}

    //상위품목
    public  String getITEM_GROUP(){return  ITEM_GROUP;}

    public  void setITEM_GROUP(String item_group ){ITEM_GROUP =  item_group;}

    //공순
    public  String getOPR_NO(){return  OPR_NO;}

    public  void setOPR_NO(String opr_no ){OPR_NO =  opr_no;}

    //순번
    public  String getSEQ(){return  SEQ;}

    public  void setSEQ(String seq ){SEQ =  seq;}


    //오더수량
    public  String getPRODT_QTY_IN_ORDER_UNIT(){return  PRODT_QTY_IN_ORDER_UNIT;}

    public  void setPRODT_QTY_IN_ORDER_UNIT(String prodt_qty_in_order_unit ){PRODT_QTY_IN_ORDER_UNIT =  prodt_qty_in_order_unit;}

    //실적수량
    public  String getRCPT_QTY_IN_ORDER_UNIT(){return  RCPT_QTY_IN_ORDER_UNIT;}

    public  void setRCPT_QTY_IN_ORDER_UNIT(String rcpt_qty_in_order_unit ){RCPT_QTY_IN_ORDER_UNIT =  rcpt_qty_in_order_unit;}
}
