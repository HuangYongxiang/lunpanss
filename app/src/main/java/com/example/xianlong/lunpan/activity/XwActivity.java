package com.example.xianlong.lunpan.activity;

;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.XwAdapter;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.Kuaidi;
import com.example.xianlong.lunpan.bean.Xinwen;
import com.example.xianlong.lunpan.bean.Xinwens;
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

public class XwActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xw);

        intview();
        //indatass();
    }
    ListView listView;
    RadioGroup radioGroup;
    RadioButton rabtn1,rabtn2,rabtn3,rabtn4;
    RadioButton rabtn5,rabtn6,rabtn7,rabtn8;
    RadioButton rabtn9,rabtn10;
    protected void intview(){
        radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
        rabtn1=(RadioButton) findViewById(R.id.rabtn1);
        rabtn2=(RadioButton) findViewById(R.id.rabtn2);
        rabtn3=(RadioButton) findViewById(R.id.rabtn3);
        rabtn4=(RadioButton) findViewById(R.id.rabtn4);
        rabtn5=(RadioButton) findViewById(R.id.rabtn5);
        rabtn6=(RadioButton) findViewById(R.id.rabtn6);
        rabtn7=(RadioButton) findViewById(R.id.rabtn7);
        rabtn8=(RadioButton) findViewById(R.id.rabtn8);
        rabtn9=(RadioButton) findViewById(R.id.rabtn9);
        rabtn10=(RadioButton) findViewById(R.id.rabtn10);
        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.rabtn1);
        listView=(ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdDetailActivity.startAdDetailActivity(XwActivity.this,"新闻详情",list.get(position).url);
            }
        });
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rabtn1:
                indatass("top");
                break;
            case R.id.rabtn2:
                indatass("shehui");
                break;
            case R.id.rabtn3:
                indatass("guonei");
                break;
            case R.id.rabtn4:
                indatass("guoji");
                break;
            case R.id.rabtn5:
                indatass("yule");
                break;
            case R.id.rabtn6:
                indatass("tiyu");
                break;
            case R.id.rabtn7:
                indatass("junshi");
                break;
            case R.id.rabtn8:
                indatass("keji");
                break;
            case R.id.rabtn9:
                indatass("caijingp");
                break;
            case R.id.rabtn10:
                indatass("shishang");
                break;
        }
    }
    List<Xinwens> list=new ArrayList<>();
    XwAdapter xwAdapter;
    protected void indatass(String type){
        Map<String, String> params = new HashMap<>();
        String url= "http://toutiao-ali.juheapi.com/toutiao/index?type="+type;
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "APPCODE 2131d7e45b0c4bf582bbacd604a014db");
        showLoadingDialog("请求中....");
        OkHttpUtils.get()
                .params(params)
                .headers(header)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(XwActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.reason.equals("成功的返回")) {
                            dismissLoadingDialog();
                            try {
                                //ToastUtil.show(XwActivity.this, response.msg);
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("result");
                                Type type = new TypeToken<Xinwen>(){}.getType();
                                Xinwen xinwen = new Gson().fromJson(dataJson, type);
                                list=xinwen.data;
                                xwAdapter=new XwAdapter(XwActivity.this,list);
                                listView.setAdapter(xwAdapter);
                                xwAdapter.notifyDataSetChanged();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(XwActivity.this,"获取成功");
                        }
                    }
                });
    }
}
