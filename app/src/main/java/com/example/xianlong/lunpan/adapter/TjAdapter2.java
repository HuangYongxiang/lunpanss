package com.example.xianlong.lunpan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.Tjitem;
import com.example.xianlong.lunpan.bean.Tjitem2;
import com.example.xianlong.lunpan.util.ViewHolder;

import java.util.List;

/**
 * Created by huang on 2017/10/24.
 */

public class TjAdapter2 extends BaseAdapter {

    private Context context;
    private List<Tjitem2> lists;
    private LayoutInflater inflater;

    public TjAdapter2(Context context, List<Tjitem2> lists) {
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
        tv_title.setText("用户信息："+lists.get(position).recommendRewardUserName);
        name.setVisibility(View.GONE);

        return convertView;
    }

}
