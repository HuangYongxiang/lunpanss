package com.example.xianlong.lunpan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.User;
import com.example.xianlong.lunpan.bean.Users;
import com.example.xianlong.lunpan.context.CircularImage;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.TimeUtils;
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

public class InfoActivity extends BaseActivity implements View.OnClickListener{

    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,name,tv10;
    CircularImage iv_head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        intView();
        indata();
    }
    TextView xiugai;

    protected void intView(){
        iv_head = (CircularImage)findViewById(R.id.iv_head);
        xiugai = (TextView) findViewById(R.id.xiugai);
        xiugai.setOnClickListener(this);
        name = (TextView) findViewById(R.id.name);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);
        tv5 = (TextView)findViewById(R.id.tv5);
        tv6 = (TextView)findViewById(R.id.tv6);
        tv7 = (TextView)findViewById(R.id.tv7);
        tv8 = (TextView)findViewById(R.id.tv8);
        tv9 = (TextView)findViewById(R.id.tv9);
        tv10 = (TextView)findViewById(R.id.tv10);
        Glide.with(InfoActivity.this).load("http://img01.sogoucdn.com/net/a/04/link?appid=100520145&url=http%3A%2F%2Fimg04.store.sogou.com%2Fapp%2Fa%2F10010016%2F53252fd7043f22aac5e4eeada1863e60")
                .placeholder(R.drawable.pic_tx).crossFade().error(R.drawable.pic_tx).into(iv_head);
    }
    List<User> list =new ArrayList<>();
    protected void indata(){
        Map<String, String> params = new HashMap<>();
        params.put("turntableUsername",ValueStorage.getString("username"));
        params.put("turntableUserPassword",ValueStorage.getString("password"));
        // params.put("userApp.imei", Utils.getImei(LoginActivity.this));
        String url= Constants.BaseUrl+Constants.LoginUrl;
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
                        ToastUtil.show(InfoActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<User>>(){}.getType();
                                list = new Gson().fromJson(dataJson, type);
                                if(list.size()==0){
                                    name.setText("无");
                                    tv2.setText("无");
                                    tv4.setText("无");
                                    tv5.setText("无");
                                    tv6.setText("无");
                                    tv1.setText("无");
                                    tv7.setText("无");
                                    tv8.setText("无");
                                    tv9.setText("无");
                                    tv3.setText("无");
                                    tv10.setText("");
                                }else {
                                    User user = list.get(0);
                                    name.setText(user.turntableUsername);
                                    tv2.setText(user.turntableUserSex);
                                    if (user.turntableUserBirthday == null) {
                                        tv4.setText("");
                                    } else {
                                        // tv4.setText(TimeUtils.stampToDate(user.birthday).substring(0, 10));
                                        tv4.setText(user.turntableUserBirthday);
                                    }
                                    tv5.setText(user.turntableUserPhone);
                                    tv6.setText(user.turntableUserIdentityId);
                                    tv1.setText(ValueStorage.getString("username"));
                                    tv7.setText(user.turntableUserBankUserName);
                                    tv8.setText(user.turntableUserBank);
                                    tv9.setText(user.turntableUserBankaddress);
                                    tv10.setText(user.turntableUserAlipay);
                                    tv3.setText(user.turntableUserAge);
                                }
                                ToastUtil.show(InfoActivity.this, "成功");
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(InfoActivity.this,"失败");
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.xiugai:
                Intent intent = new Intent(InfoActivity.this,InfoActivitys.class);
                startActivityForResult(intent,100);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                indata();
            }
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        //TCAgent.onPageEnd(this,"InfoActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TCAgent.onPageStart(this,"InfoActivity");
    }
}
