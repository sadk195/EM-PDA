package com.example.EM_KOREA.myapplication.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.EM_KOREA.myapplication.R;

import java.util.ArrayList;

public class I39_SET_ListViewAdapter extends BaseAdapter {

    private ArrayList<I39_SET> listViewItem = new ArrayList<I39_SET>();

    public I39_SET_ListViewAdapter() {

    }
    public ArrayList<I39_SET> getItems() {
        return listViewItem;
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
            convertView = inflater.inflate(R.layout.list_view_i39_set, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득



        TextView prodt_order_no         = (TextView) convertView.findViewById(R.id.prodt_order_no);
        TextView tracking_no            = (TextView) convertView.findViewById(R.id.tracking_no);
        TextView job_nm                    = (TextView) convertView.findViewById(R.id.job_nm);
        TextView req_qty                    = (TextView) convertView.findViewById(R.id.req_qty);
        CheckBox chk_out                = (CheckBox) convertView.findViewById(R.id.chk_out);

        I39_SET item = listViewItem.get(position);
        chk_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getCHK_OUT())
                {
                    item.setCheckBool(false);
                }
                else{
                    item.setCheckBool(true);

                }
            }
        });


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        // 아이템 내 각 위젯에 데이터 반영
        prodt_order_no.setText(item.getPRODT_ORDER_NO());
        tracking_no.setText(item.getTRACKING_NO());
        job_nm.setText(item.getJOB_NM());
        req_qty.setText(item.getREQ_QTY());
        chk_out.setChecked(item.getCHK_OUT());

        return convertView;
    }

    public void add_Item(String PRODT_ORDER_NO, String OPR_NO,  String TRACKING_NO,String JOB_NM,String REQ_QTY,String SEQ,  boolean CHK_OUT) {
        I39_SET item = new I39_SET();

        item.setPRODT_ORDER_NO(PRODT_ORDER_NO);
        item.setOPR_NO(OPR_NO);
        item.setREQ_QTY(REQ_QTY);
        item.setTRACKING_NO(TRACKING_NO);
        item.setJOB_NM(JOB_NM);
        item.setCHK_OUT(CHK_OUT);
        item.setSEQ(SEQ);

        listViewItem.add(item);
    }

    protected void addHDRItem(I39_SET item) {
        listViewItem.add(item);
    }

}
