package com.example.xianlong.lunpan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.Ggitem;
import com.example.xianlong.lunpan.util.TimeUtils;
import com.example.xianlong.lunpan.util.ViewHolder;

import java.util.List;

/**
 * Created by huang on 2017/10/24.
 */

public class GgAdapter extends BaseAdapter {

    private Context context;
    private List<Ggitem> lists;
    private LayoutInflater inflater;

    public GgAdapter(Context context, List<Ggitem> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gg_item, null);
        }
        TextView info = ViewHolder.get(convertView,R.id.info);
        TextView time = ViewHolder.get(convertView,R.id.time);
        TextView title = ViewHolder.get(convertView,R.id.title);
        info.setText(lists.get(position).bulletindetail);
        title.setText(lists.get(position).bulletinname);
        if(lists.get(position).bulletdate==null){
            time.setText("");
        }else {
            time.setText(TimeUtils.stampToDate(lists.get(position).bulletdate).substring(0, 10));
        }
        return convertView;
    }

}
