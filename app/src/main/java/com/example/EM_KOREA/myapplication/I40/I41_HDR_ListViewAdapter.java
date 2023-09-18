package com.example.EM_KOREA.myapplication.I40;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.EM_KOREA.myapplication.R;

import java.util.ArrayList;

public class I41_HDR_ListViewAdapter extends BaseAdapter {

    private ArrayList<I41_HDR> listViewItem = new ArrayList<I41_HDR>();

    public I41_HDR_ListViewAdapter() {

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

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_i41_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView PRODT_ORDER_NO     = (TextView) convertView.findViewById(R.id.prodt_order_no);
        TextView ITEM_CD            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView ITEM_NM            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView TRACKING_NO        = (TextView) convertView.findViewById(R.id.tracking_no);
        TextView TYPE               = (TextView) convertView.findViewById(R.id.type);
        TextView REQ_QTY         = (TextView) convertView.findViewById(R.id.req_qty);
        TextView STATUS             = (TextView) convertView.findViewById(R.id.status);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I41_HDR item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        PRODT_ORDER_NO.setText(item.getPRODT_ORDER_NO());
        ITEM_CD.setText(item.getITEM_CD());
        ITEM_NM.setText(item.getITEM_NM());
        TRACKING_NO.setText(item.getTRACKING_NO());
        TYPE.setText(item.getMOV_TYPE());
        REQ_QTY.setText(item.getREQ_QTY());
        STATUS.setText(item.getMOV_STATUS());
        if(STATUS.getText().equals("E")){
            convertView.setBackgroundColor(Color.parseColor("#FF9999"));
        }
        else if(STATUS.getText().equals("S")){
            convertView.setBackgroundColor(Color.parseColor("#CCE5FF"));
        }

        return convertView;
    }

    protected void add_Item(String PRODT_ORDER_NO, String ITEM_CD , String ITEM_NM, String TRACKING_NO, String MOV_TYPE,
                         String REQ_QTY, String MOV_STATUS, String LOT_NO,String SL_CD) {
        I41_HDR item = new I41_HDR();

        item.setPRODT_ORDER_NO(PRODT_ORDER_NO);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setTRACKING_NO(TRACKING_NO);
        item.setMOV_TYPE(MOV_TYPE);
        item.setREQ_QTY(REQ_QTY);
        item.setMOV_STATUS(MOV_STATUS);
        item.setLOT_NO(LOT_NO);
        item.setSL_CD(SL_CD);
        listViewItem.add(item);
    }

    protected void addHDRItem(I41_HDR item) {
        listViewItem.add(item);
    }
    protected void setHDRStatus(String status,int idx){

        I41_HDR item = listViewItem.get(idx);
        item.setMOV_STATUS(status);
        listViewItem.set(idx,item);
    }


}
