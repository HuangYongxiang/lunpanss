package com.example.xianlong.lunpan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.activity.Tj_Activity2;
import com.example.xianlong.lunpan.bean.Caipu1;
import com.example.xianlong.lunpan.bean.Tjitem;
import com.example.xianlong.lunpan.util.ViewHolder;

import java.util.List;

/**
 * Created by huang on 2017/10/24.
 */

public class TjAdapter extends BaseAdapter {

    private Context context;
    private List<Tjitem> lists;
    private LayoutInflater inflater;

    public TjAdapter(Context context, List<Tjitem> lists) {
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
            convertView = inflater.inflate(R.layout.tj_item, null);
        }
        TextView tv_title = ViewHolder.get(convertView,R.id.time);
        TextView name = ViewHolder.get(convertView,R.id.info);
        tv_title.setText("等级："+lists.get(position).recommendRewardLevel);
        name.setText("人数："+lists.get(position).nums);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Tj_Activity2.class);
                intent.putExtra("id",lists.get(position).recommendRewardLevel);
                context.startActivity(intent);
            }

        });
        return convertView;
    }

}
