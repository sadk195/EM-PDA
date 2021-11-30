package com.example.EM_KOREA.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class M22_ListViewAdapter extends BaseAdapter {

    private ArrayList<M22_ARRAYLIST> listViewItem = new ArrayList<M22_ARRAYLIST>();

    public M22_ListViewAdapter()
    {

    }

    @Override
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    public int getCount() {
        return listViewItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItem.get(position);
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
            convertView = inflater.inflate(R.layout.list_view_m21, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView INSP_QTY = (TextView) convertView.findViewById(R.id.INSP_QTY);
        TextView INSPECT_GOOD_QTY = (TextView) convertView.findViewById(R.id.G_QTY);
        TextView INSPECT_BAD_QTY = (TextView) convertView.findViewById(R.id.B_QTY);
        TextView PRODT_ORDER_NO = (TextView) convertView.findViewById(R.id.PRODT_ORDER_NO);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        M22_ARRAYLIST item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        INSP_QTY.setText(item.getINSP_QTY());
        INSPECT_GOOD_QTY.setText(item.getINSPECT_GOOD_QTY());
        INSPECT_BAD_QTY.setText(item.getINSPECT_BAD_QTY());
        PRODT_ORDER_NO.setText(item.getPRODT_ORDER_NO());

        return convertView;

    }

    public void addItem(String pINSP_QTY, String pINSPECT_GOOD_QTY, String pINSPECT_BAD_QTY,String pPRODT_ORDER_NO) {
        M22_ARRAYLIST item = new M22_ARRAYLIST();

        item.setINSP_QTY(pINSP_QTY);
        item.setINSPECT_GOOD_QTY(pINSPECT_GOOD_QTY);
        item.setINSPECT_BAD_QTY(pINSPECT_BAD_QTY);
        item.setPRODT_ORDER_NO(pPRODT_ORDER_NO);
        listViewItem.add(item);
    }
}
