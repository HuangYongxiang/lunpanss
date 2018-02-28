package com.example.xianlong.lunpan.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.CpAdapter;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.Caipu;
import com.example.xianlong.lunpan.bean.Caipu1;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.Material;
import com.example.xianlong.lunpan.bean.Process;
import com.example.xianlong.lunpan.util.TimeUtils;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CpDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpdetail);

        intview();
        //indatass();
    }
    LinearLayout lay1,lay2;
    ImageView image;
    TextView title,infos,info,time;
    protected void intview(){
        lay1=(LinearLayout)findViewById(R.id.lay1);
        lay2=(LinearLayout)findViewById(R.id.lay2);
        image=(ImageView)findViewById(R.id.image);
        title=(TextView)findViewById(R.id.title);
        infos=(TextView)findViewById(R.id.infos);
        info=(TextView)findViewById(R.id.info);
        time=(TextView)findViewById(R.id.time);
        String shuju=getIntent().getStringExtra("shuju");
        int pos=Integer.valueOf(getIntent().getStringExtra("id"));
        Type type = new TypeToken<Caipu>(){}.getType();
        Caipu caipu = new Gson().fromJson(shuju, type);
        caipus=caipu.list;
        title.setText(caipus.get(pos).name);
        infos.setText(caipus.get(pos).tag);
        info.setText(caipus.get(pos).content);
        time.setText("烹饪时间："+caipus.get(pos).cookingtime);
        Glide.with(CpDetailActivity.this).load(caipus.get(pos).pic)
                .placeholder(R.drawable.icon_sp).crossFade().error(R.drawable.icon_sp).into(image);

        materials=caipus.get(pos).material;
        processes=caipus.get(pos).process;

        LayoutInflater li=LayoutInflater.from(CpDetailActivity.this);
        for(int i=0;i<processes.size();i++){
            View view = li.inflate(R.layout.cp_item1, null);
            TextView text = (TextView) view.findViewById(R.id.text);
            ImageView img = (ImageView) view.findViewById(R.id.img);
            text.setText(processes.get(i).pcontent);
            Glide.with(CpDetailActivity.this).load(processes.get(i).pic)
                    .placeholder(R.drawable.icon_sp).crossFade().error(R.drawable.icon_sp).into(img);
            lay2.addView(view);
        }
        LayoutInflater li2=LayoutInflater.from(CpDetailActivity.this);
        for(int i=0;i<materials.size();i++){
            View view = li2.inflate(R.layout.cp_item2, null);
            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText("材料:"+materials.get(i).mname+"    "+materials.get(i).amount);
            lay1.addView(view);
        }
    }
    List<Material> materials=new ArrayList<>();
    List<Process> processes=new ArrayList<>();
    List<Caipu1> caipus=new ArrayList<>();
}
