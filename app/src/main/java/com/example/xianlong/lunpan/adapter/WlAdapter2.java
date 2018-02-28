package com.example.xianlong.lunpan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.Kditem;
import com.example.xianlong.lunpan.bean.Wuliu;
import com.example.xianlong.lunpan.util.ViewHolder;

import java.util.List;

/**
 * Created by huang on 2017/10/24.
 */

public class WlAdapter2 extends BaseAdapter {

    private Context context;
    private List<Wuliu> lists;
    private LayoutInflater inflater;

    public WlAdapter2(Context context, List<Wuliu> lists) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.wl_item2, null);
        }
        TextView info = ViewHolder.get(convertView,R.id.info);
        info.setText(lists.get(position).name);
        return convertView;
    }

}
