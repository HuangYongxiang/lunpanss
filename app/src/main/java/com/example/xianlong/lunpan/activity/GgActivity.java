package com.example.xianlong.lunpan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.GgAdapter;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.Ggitem;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.IntenetUtil;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.Utilss;
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

public class GgActivity extends BaseActivity {

    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gg);

        intview();
        intdata();
    }
    List<Ggitem> list=new ArrayList<>();
    protected void intview(){

        listview = (ListView)findViewById(R.id.listview);

    }
    GgAdapter ggAdapter;
    protected  void intdata(){

        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl+Constants.GgUrl;
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
                        ToastUtil.show(GgActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson;
                                dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<Ggitem>>(){}.getType();
                                list = new Gson().fromJson(dataJson, type);
                                ggAdapter=new GgAdapter(myActivity,list);
                                listview.setAdapter(ggAdapter);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            //startActivity(new Intent(TaskActivity.this,VoiceDetailActivity.class));
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(GgActivity.this,response.message);
                        }
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
