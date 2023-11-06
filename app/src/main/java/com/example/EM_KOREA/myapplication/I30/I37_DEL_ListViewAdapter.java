package com.example.EM_KOREA.myapplication.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.EM_KOREA.myapplication.R;

import java.util.ArrayList;

public class I37_DEL_ListViewAdapter extends BaseAdapter {

    private ArrayList<I37_DEL> listViewItem = new ArrayList<I37_DEL>();

    public I37_DEL_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i37_del, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView record_no      = (TextView) convertView.findViewById(R.id.record_no);
        TextView good_qty       = (TextView) convertView.findViewById(R.id.good_qty);
        TextView sl_nm          = (TextView) convertView.findViewById(R.id.sl_nm);
        TextView location    = (TextView) convertView.findViewById(R.id.location);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I37_DEL item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영

        good_qty.setText(item.getGOOD_QTY());
        record_no.setText(item.getRECORD_NO());
        sl_nm.setText(item.getSL_NM());
        location.setText(item.getLOCATION());

        return convertView;
    }

    public void add_Item(String RECORD_NO,String SL_CD, String SL_NM,String GOOD_QTY,String LOCATION) {
        I37_DEL item = new I37_DEL();

       item.setRECORD_NO (RECORD_NO);
       item.setSL_CD(SL_CD);
       item.setSL_NM(SL_NM);
       item.setGOOD_QTY(GOOD_QTY);
       item.setLOCATION(LOCATION);

        listViewItem.add(item);
    }

    protected void addDELItem(I37_DEL item) {
        listViewItem.add(item);
    }

}
