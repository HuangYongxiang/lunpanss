package com.example.xianlong.lunpan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.CpAdapter;
import com.example.xianlong.lunpan.adapter.TxAdapter;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.Caipu;
import com.example.xianlong.lunpan.bean.Caipu1;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
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

public class MoneyActivity2 extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp);
        intview();
        indatass();
    }
    ListView listview;
    protected void intview(){
        listview=(ListView)findViewById(R.id.listview);
    }
    List<Caipu1> caipus=new ArrayList<>();
    TxAdapter txAdapter;
    String shuju="";
    protected void indatass(){
        Map<String, String> params = new HashMap<>();
        String url= "";
        showLoadingDialog("请求中....");
        OkHttpUtils.get()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(MoneyActivity2.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.status.equals("0")) {
                            dismissLoadingDialog();
                            try {
                                //ToastUtil.show(XwActivity.this, response.msg);
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("result");
                                shuju=dataJson;
                                Type type = new TypeToken<Caipu>(){}.getType();
                                Caipu caipu = new Gson().fromJson(dataJson, type);
                                caipus=caipu.list;
                                txAdapter=new TxAdapter(MoneyActivity2.this,caipus);
                                listview.setAdapter(txAdapter);
                                txAdapter.notifyDataSetChanged();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(MoneyActivity2.this,response.message);
                        }
                    }
                });
    }
}
