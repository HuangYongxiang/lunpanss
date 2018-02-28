package com.example.xianlong.lunpan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.Caipu1;
import com.example.xianlong.lunpan.util.ViewHolder;

import java.util.List;

/**
 * Created by huang on 2017/10/24.
 */

public class TxAdapter extends BaseAdapter {

    private Context context;
    private List<Caipu1> lists;
    private LayoutInflater inflater;

    public TxAdapter(Context context, List<Caipu1> lists) {
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
            convertView = inflater.inflate(R.layout.cp_item, null);
        }
        ImageView image = ViewHolder.get(convertView,R.id.image);
        TextView tv_title = ViewHolder.get(convertView,R.id.title);
        TextView money = ViewHolder.get(convertView,R.id.infos);
        tv_title.setText(lists.get(position).name);
        money.setText(lists.get(position).tag);
        Glide.with(context).load(lists.get(position).pic)
                .placeholder(R.drawable.icon_sp).crossFade().error(R.drawable.icon_sp).into(image);
        return convertView;
    }

}
