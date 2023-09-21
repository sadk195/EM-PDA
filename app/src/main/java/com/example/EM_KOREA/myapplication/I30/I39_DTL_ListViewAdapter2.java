package com.example.EM_KOREA.myapplication.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.EM_KOREA.myapplication.R;

import java.util.ArrayList;

public class I39_DTL_ListViewAdapter2 extends BaseAdapter {

    private ArrayList<I39_DTL> listViewItem = new ArrayList<I39_DTL>();

    public I39_DTL_ListViewAdapter2() {

    }

    @Override
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    public int getCount() {
        return listViewItem.size();
    }
    public void clear() {
        listViewItem.clear();
    }

    @Override
    public Object getItem(int position) {
        return listViewItem.get(position);
    }

    public ArrayList<I39_DTL>  getItems() {
        return listViewItem;
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
            convertView = inflater.inflate(R.layout.list_view_i39_dtl, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView item_cd                = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm                = (TextView) convertView.findViewById(R.id.item_nm);
        TextView location               = (TextView) convertView.findViewById(R.id.location);
        TextView qty                    = (TextView) convertView.findViewById(R.id.qty);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I39_DTL item = listViewItem.get(position);


        // 아이템 내 각 위젯에 데이터 반영
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        location.setText(item.getLOCATION());
        qty.setText(item.getQTY());

        return convertView;
    }

    public void add_Item(String PRODT_ORDER_NO,String OPR_NO,String ITEM_CD,String ITEM_NM,String SPEC,
                         String TRACKING_NO,String LOCATION,String QTY,String SEQ,String SL_CD,String JOB_NM) {
        I39_DTL item = new I39_DTL();

        item.setPRODT_ORDER_NO(PRODT_ORDER_NO);
        item.setOPR_NO(OPR_NO);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setSPEC(SPEC);
        item.setTRACKING_NO(TRACKING_NO);
        item.setLOCATION(LOCATION);
        item.setQTY(QTY);
        item.setSEQ(SEQ);
        item.setSL_CD(SL_CD);
        item.setJOB_NM(JOB_NM);

        listViewItem.add(item);
    }

    protected void addHDRItem(I39_DTL item) {
        listViewItem.add(item);
    }

}
