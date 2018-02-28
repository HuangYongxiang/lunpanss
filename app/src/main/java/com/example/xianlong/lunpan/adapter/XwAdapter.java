package com.example.xianlong.lunpan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.Kditem;
import com.example.xianlong.lunpan.bean.Xinwens;
import com.example.xianlong.lunpan.util.ViewHolder;

import java.util.List;

/**
 * Created by huang on 2017/10/24.
 */

public class XwAdapter extends BaseAdapter {

    private Context context;
    private List<Xinwens> lists;
    private LayoutInflater inflater;

    public XwAdapter(Context context, List<Xinwens> lists) {
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
            convertView = inflater.inflate(R.layout.xw_item, null);
        }
        ImageView image = ViewHolder.get(convertView,R.id.image);
        TextView tv_title = ViewHolder.get(convertView,R.id.title);
        TextView infos = ViewHolder.get(convertView,R.id.infos);
        tv_title.setText(lists.get(position).title);
        infos.setText(lists.get(position).date);
        return convertView;
    }

}
