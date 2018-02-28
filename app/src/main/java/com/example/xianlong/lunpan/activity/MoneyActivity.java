package com.example.xianlong.lunpan.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.TjAdapter2;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.Tjitem2;
import com.example.xianlong.lunpan.bean.Zhanghao;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.ValueStorage;
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

public class MoneyActivity extends BaseActivity {
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        intview();
        indatass();
        indatasss();
    }
    TextView money;
    Button tixian;
    String alipay="";
    protected void intview(){
        tixian=(Button)findViewById(R.id.tixian);
        image=(ImageView)findViewById(R.id.image);
        money=(TextView) findViewById(R.id.money);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(MoneyActivity.this,"该功能待开放！");
                /*if(zhanghaos.contains(ValueStorage.getString("username"))){
                    if(alipay.equals("0")){
                        tongzhi2();
                    }else{
                        ToastUtil.show(MoneyActivity.this,"你今天已经提现过！");
                    }
                }else{
                    ToastUtil.show(MoneyActivity.this,"你没有提现资格");
                }*/
            }
        });
    }
    List<Zhanghao> zhanghao=new ArrayList<>();
    List<String> zhanghaos=new ArrayList<>();
    protected void indatasss(){
        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl1+"TurntableUser/listcs";
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
                        ToastUtil.show(MoneyActivity.this,"网络异常");
                    }
                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(MoneyActivity.this,response.message);
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<Zhanghao>>(){}.getType();
                                zhanghao= new Gson().fromJson(dataJson, type);
                                for(int i=0;i<zhanghao.size();i++){
                                    String name=zhanghao.get(i).username;
                                    zhanghaos.add(name);
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(MoneyActivity.this,response.message);
                        }
                    }
                });
    }
    boolean isdian=false;
    protected void indatass(){
        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl1+ Constants.MoneyUrl;
        params.put("turntableUserId", ValueStorage.getString("id"));
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
                        ToastUtil.show(MoneyActivity.this,"网络异常");
                    }
                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(MoneyActivity.this,response.message);
                            money.setText("¥"+response.money);
                            alipay=response.alipay;
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(MoneyActivity.this,response.message);
                        }
                    }
                });
    }
    protected void indatas(String moneys,String number,String name){
        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl1+ Constants.TiXianUrl;
        params.put("turntableUserId", ValueStorage.getString("id"));
        params.put("turntableWithdrawalsMoney", moneys);
        params.put("turntableUsername", ValueStorage.getString("username"));
        params.put("turntableUserAlipay", number);
        params.put("turntableUserBankUserName", name);
        params.put("turntableUserBank", "");
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
                        ToastUtil.show(MoneyActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        dismissLoadingDialog();
                        dialog2.dismiss();
                        ToastUtil.show(MoneyActivity.this,response.message);
                        //indatass();
                        finish();
                    }
                });
    }
    Dialog dialog2;
    protected  void tongzhi2(){
        View view = LayoutInflater.from(myActivity).inflate(
                R.layout.money_item, null);
        final EditText moneys = (EditText) view.findViewById(R.id.moneys);
        final EditText number = (EditText) view.findViewById(R.id.number);
        final EditText name = (EditText) view.findViewById(R.id.name);
        Button btn = (Button) view.findViewById(R.id.tixian);

        dialog2 = new Dialog(myActivity, R.style.custom_dialog);
        dialog2.setContentView(view);
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.setCancelable(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a=Double.parseDouble(money.getText().toString().split("¥")[1]);
                double b=Double.parseDouble(moneys.getText().toString());
                double c=b+2;
                if(c>a){
                    ToastUtil.show(MoneyActivity.this,"账户金额不足");
                    return;
                }else  if(moneys.getText().toString().equals("")){
                    ToastUtil.show(MoneyActivity.this,"金额不能为空");
                    return;
                }else if(number.getText().toString().equals("")){
                    ToastUtil.show(MoneyActivity.this,"支付宝账号不能为空");
                    return;
                }else if(number.getText().toString().equals("")){
                    ToastUtil.show(MoneyActivity.this,"账号姓名不能为空");
                    return;
                }else{
                    indatas(moneys.getText().toString(),number.getText().toString(),name.getText().toString());
                }
            }
        });
        dialog2.show();
    }
}
