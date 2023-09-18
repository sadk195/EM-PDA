package com.example.EM_KOREA.myapplication.P10;

import java.io.Serializable;

public class P14_ORDER_PRODUCTION implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String Item_Group_CD;      //그룹
    public  String Prodt_Order_No;  //제조오더
    public  String Item_Group_NM;
    public  String Item_CD;         //품목
    public  String Item_NM;         //품명
    public  String Prodcution_No;   //호기
    public  String Plan_Start_DT;   //계획일

    public String getPlan_Start_DT() {
        return Plan_Start_DT;
    }

    public void setPlan_Start_DT(String plan_Start_DT) {
        Plan_Start_DT = plan_Start_DT;
    }


    public String getProdt_Order_No() {
        return Prodt_Order_No;
    }

    public void setProdt_Order_No(String prodt_Order_No) {
        Prodt_Order_No = prodt_Order_No;
    }

    public String getItem_CD() {
        return Item_CD;
    }

    public void setItem_CD(String item_CD) {
        Item_CD = item_CD;
    }

    public String getItem_NM() {
        return Item_NM;
    }

    public void setItem_NM(String item_NM) {
        Item_NM = item_NM;
    }

    public String getProdcution_No() {
        return Prodcution_No;
    }

    public void setProdcution_No(String prodcution_No) {
        Prodcution_No = prodcution_No;
    }

    public String getItem_Group_NM() {
        return Item_Group_NM;
    }

    public void setItem_Group_NM(String item_Group_NM) {
        Item_Group_NM = item_Group_NM;
    }



    public String getItem_Group_CD() {
        return Item_Group_CD;
    }

    public void setItem_Group_CD(String item_Group_CD) {
        Item_Group_CD = item_Group_CD;
    }


}
