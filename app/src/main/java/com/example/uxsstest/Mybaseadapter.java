package com.example.uxsstest;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Mellan on 2017/8/13.
 */

public class Mybaseadapter extends BaseAdapter {
    private ArrayList<Map<String, Object>> list;
    LayoutInflater infater = null;
    public Mybaseadapter(Context context, ArrayList<Map<String, Object>> cvelist){
        infater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list=cvelist;
    }
    @Override
    public int getCount() {
        return list.size();

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = infater.inflate(R.layout.listview_item, null);
            viewHolder.cve = (TextView) convertView.findViewById(R.id.cve);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.cve.setText(list.get(position).get("cveid").toString());
        viewHolder.description.setText("漏洞描述："+list.get(position).get("description").toString());

        return convertView;
    }

}
class ViewHolder {
    TextView cve;
    TextView description;
}
