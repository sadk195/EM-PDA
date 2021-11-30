package com.example.EM_KOREA.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class P14_ORDER_ListViewAdapter extends BaseAdapter {

    private ArrayList<P14_ORDER_PRODUCTION> listViewItem = new ArrayList<P14_ORDER_PRODUCTION>();

    private ArrayList<P14_ORDER_PRODUCTION_RESULT> listViewItem2 = new ArrayList<P14_ORDER_PRODUCTION_RESULT>();

    private ArrayList<P14_PROD_ORDER_PRODUCTION_SEARCH> listViewProdSearch = new ArrayList<P14_PROD_ORDER_PRODUCTION_SEARCH>();

    private ArrayList<P14_PROD_ORDER_PRODUCTION_SEARCH> listViewProdOprNoSearch = new ArrayList<P14_PROD_ORDER_PRODUCTION_SEARCH>();
    public P14_ORDER_ListViewAdapter()
    {

    }

    @Override
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    public int getCount() {
        return listViewProdSearch.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewProdSearch.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_p11_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView lbl_production_no = (TextView) convertView.findViewById(R.id.lbl_production_no) ;
        TextView lbl_opr_no = (TextView) convertView.findViewById(R.id.lbl_opr_no) ;
        TextView lbl_item_cd = (TextView) convertView.findViewById(R.id.lbl_item_cd) ;
        TextView lbl_item_nm = (TextView) convertView.findViewById(R.id.lbl_item_nm) ;
        TextView lbl_PRODT_ORDER_QTY = (TextView) convertView.findViewById(R.id.lbl_PRODT_ORDER_QTY) ;



        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        P14_PROD_ORDER_PRODUCTION_SEARCH item = listViewProdSearch.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        lbl_production_no.setText(item.getPRODT_ORDER_NO());
        lbl_opr_no.setText(item.getOPR_NO());
        lbl_item_cd.setText(item.getITEM_CD());
        lbl_item_nm.setText(item.getITEM_NM());
        lbl_PRODT_ORDER_QTY.setText(item.getPRODT_ORDER_QTY());

        return convertView;

    }

    public void addItem(String pProdt_Order_No, String pItem_CD, String pItem_NM,String pItem_Group_CD,String pItem_Group_NM,String pProductionNO,String pPlan_Start_DT) {
        P14_ORDER_PRODUCTION item = new P14_ORDER_PRODUCTION();

        item.setProdt_Order_No(pProdt_Order_No);
        item.setItem_CD(pItem_CD);
        item.setItem_NM(pItem_NM);
        item.setItem_CD(pItem_CD);
        item.setItem_Group_NM(pItem_Group_NM);
        item.setItem_Group_CD(pItem_Group_CD);
        item.setProdcution_No(pProductionNO);
        item.setPlan_Start_DT(pPlan_Start_DT);

        listViewItem.add(item);
    }
    public void addItem2(String vPRODT_ORDER_NO, String vPLANT_CD, String vITEM_CD, String vITEM_GROUP,String vOPR_NO,String vSEQ,String vPROD_QTY_IN_ORDER_UNIT,String vRCPT_QTY_IN_ORDER_UNIT) {
        P14_ORDER_PRODUCTION_RESULT item2 = new P14_ORDER_PRODUCTION_RESULT();

        item2.setPRODT_ORDER_NO(vPRODT_ORDER_NO);
        item2.setPLANT_CD(vPLANT_CD);
        item2.setITEM_CD(vITEM_CD);
        item2.setITEM_GROUP(vITEM_GROUP);
        item2.setOPR_NO(vOPR_NO);
        item2.setSEQ(vSEQ);
        item2.setPRODT_QTY_IN_ORDER_UNIT(vPROD_QTY_IN_ORDER_UNIT);
        item2.setRCPT_QTY_IN_ORDER_UNIT(vRCPT_QTY_IN_ORDER_UNIT);

        listViewItem2.add(item2);
    }

    public void addProdItem(String vPRODT_ORDER_NO, String vOPR_NO, String vITEM_CD,String vItemNM, String vPRODT_ORDER_QTY) {
        //PRODUCTION_RESULT item2 = new PRODUCTION_RESULT();
        P14_PROD_ORDER_PRODUCTION_SEARCH addProdItem = new P14_PROD_ORDER_PRODUCTION_SEARCH();


        addProdItem.setPRODT_ORDER_NO(vPRODT_ORDER_NO);
        addProdItem.setOPR_NO(vOPR_NO);
        addProdItem.setITEM_CD(vITEM_CD);
        addProdItem.setITEM_NM(vItemNM);
        addProdItem.setPRODT_ORDER_QTY(vPRODT_ORDER_QTY);

        listViewProdSearch.add(addProdItem);
    }



}
