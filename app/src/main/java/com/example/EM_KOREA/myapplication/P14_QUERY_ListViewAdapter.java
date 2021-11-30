package com.example.EM_KOREA.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class P14_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<P14_QUERY> listViewProdOprNoSearch = new ArrayList<P14_QUERY>();
    public P14_QUERY_ListViewAdapter()
    {

    }

    @Override
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    public int getCount() {
        return listViewProdOprNoSearch.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewProdOprNoSearch.get(position);
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


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        P14_QUERY item = listViewProdOprNoSearch.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        lbl_production_no.setText(item.getPRODT_ORDER_NO());
        lbl_opr_no.setText(item.getOPR_NO());
        lbl_item_cd.setText(item.getITEM_CD());

        return convertView;

    }


    public void addProdOprNoItem(String vPRODT_ORDER_NO, String vOPR_NO, String vITEM_CD) {
        P14_QUERY addProdOprNoItem = new P14_QUERY();


        addProdOprNoItem.setPRODT_ORDER_NO(vPRODT_ORDER_NO);
        addProdOprNoItem.setOPR_NO(vOPR_NO);
        addProdOprNoItem.setITEM_CD(vITEM_CD);


        listViewProdOprNoSearch.add(addProdOprNoItem);
    }

}
