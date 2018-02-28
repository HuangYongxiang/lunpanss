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

public class CpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cp);

        intview();
        //indatass();
    }
    ListView listview;
    Button chaxun;
    EditText name;
    protected void intview(){
        listview=(ListView)findViewById(R.id.listview);
        name=(EditText)findViewById(R.id.danhao);
        chaxun=(Button)findViewById(R.id.chaxun);
        chaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indatass(name.getText().toString());
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CpActivity.this,CpDetailActivity.class);
                intent.putExtra("shuju",shuju);
                intent.putExtra("id",position+"");
               startActivity(intent);
            }
        });
    }
    List<Caipu1> caipus=new ArrayList<>();
    CpAdapter cpAdapter;
    String shuju="";
    protected void indatass(String name){
        Map<String, String> params = new HashMap<>();
        String url= "http://jisusrecipe.market.alicloudapi.com/recipe/search?keyword="+name+"&num=10&start=0";
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "APPCODE 2131d7e45b0c4bf582bbacd604a014db");
        showLoadingDialog("请求中....");
        OkHttpUtils.get()
                .headers(header)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(CpActivity.this,"网络异常");
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
                                cpAdapter=new CpAdapter(CpActivity.this,caipus);
                                listview.setAdapter(cpAdapter);
                                cpAdapter.notifyDataSetChanged();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(CpActivity.this,response.message);
                        }
                    }
                });
    }
}
