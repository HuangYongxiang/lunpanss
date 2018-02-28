package com.example.xianlong.lunpan.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.TjAdapter;
import com.example.xianlong.lunpan.adapter.TjAdapter2;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.Tjitem;
import com.example.xianlong.lunpan.bean.Tjitem2;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.ValueStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tendcloud.tenddata.TCAgent;
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

public class Tj_Activity2 extends BaseActivity{

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);
        intView();
        indata();
    }
    TjAdapter2 tjAdapter;
    protected void intView(){
        listView=(ListView) findViewById(R.id.listview);
    }
    List<Tjitem2> list =new ArrayList<>();
    protected void indata(){
        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl+"recommendRewardController/querysdetailsRecommendReward ";
        params.put("recommendRewardUserNameMaster", ValueStorage.getString("username"));
        params.put("recommendRewardLevel",getIntent().getStringExtra("id"));
        showLoadingDialog("请求中....");
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(Tj_Activity2.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.message.equals("成功")) {
                            dismissLoadingDialog();
                            ToastUtil.show(Tj_Activity2.this,response.message);
                            try {
                                ToastUtil.show(Tj_Activity2.this, response.msg);
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<Tjitem2>>(){}.getType();
                                List<Tjitem2> lists= new Gson().fromJson(dataJson, type);
                                list=lists;
                                tjAdapter=new TjAdapter2(Tj_Activity2.this,list);
                                listView.setAdapter(tjAdapter);
                                tjAdapter.notifyDataSetChanged();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(Tj_Activity2.this,response.message);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onPageStart(this,"TjyjActivity");
    }
    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this,"TjyjActivity");
    }
}
