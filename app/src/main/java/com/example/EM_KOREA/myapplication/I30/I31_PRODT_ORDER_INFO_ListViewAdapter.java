package com.example.EM_KOREA.myapplication.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.EM_KOREA.myapplication.R;

import java.util.ArrayList;

public class I31_PRODT_ORDER_INFO_ListViewAdapter extends BaseAdapter {

    private ArrayList<I31_PRODT_ORDER_INFO> listViewItem = new ArrayList<I31_PRODT_ORDER_INFO>();

    public I31_PRODT_ORDER_INFO_ListViewAdapter()
    {

    }

    @Override
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    public int getCount() { return listViewItem.size(); }

    @Override
    public Object getItem(int position) { return listViewItem.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_i31_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_cd = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm = (TextView) convertView.findViewById(R.id.item_nm);
        TextView tracking_no = (TextView) convertView.findViewById(R.id.tracking_no);
        TextView prodt_qty = (TextView) convertView.findViewById(R.id.prodt_qty);
        TextView remain_qty = (TextView) convertView.findViewById(R.id.remain_qty);
        TextView good_qty = (TextView) convertView.findViewById(R.id.good_qty);
        TextView bad_qty = (TextView) convertView.findViewById(R.id.bad_qty);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I31_PRODT_ORDER_INFO item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        tracking_no.setText(item.getTRACKING_NO());
        prodt_qty.setText(item.getPRODT_ORDER_QTY());
        remain_qty.setText(item.getREMAIN_QTY());
        good_qty.setText(item.getGOOD_QTY());
        bad_qty.setText(item.getBAD_QTY());

        return convertView;
    }

    public void add_Loading_Place_Item(String ITEM_CD, String ITEM_NM,  String TRACKING_NO, String PRODT_ORDER_QTY, String GOOD_QTY, String BAD_QTY, String REMAIN_QTY)
    {
        I31_PRODT_ORDER_INFO item = new I31_PRODT_ORDER_INFO();

        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setTRACKING_NO(TRACKING_NO);
        item.setPRODT_ORDER_QTY(PRODT_ORDER_QTY);
        item.setGOOD_QTY(GOOD_QTY);
        item.setBAD_QTY(BAD_QTY);
        item.setREMAIN_QTY(REMAIN_QTY);

        listViewItem.add(item);
    }



}
