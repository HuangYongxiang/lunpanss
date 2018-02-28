package com.example.xianlong.lunpan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.User;
import com.example.xianlong.lunpan.context.CircularImage;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.CustomDatePicker;
import com.example.xianlong.lunpan.util.DateUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;

public class InfoActivitys extends BaseActivity implements View.OnClickListener{
    private CustomDatePicker customDatePicker;
    EditText tv1,tv2,tv3,tv5,tv6,tv7,tv8,tv9,tv10;
    TextView name;
    CircularImage iv_head;
    TextView xiugai,tv4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);

        intView();
        initDatePicker();
        // indata();
    }
    protected void intView(){
        iv_head = (CircularImage)findViewById(R.id.iv_head);
        xiugai = (TextView) findViewById(R.id.tijiao);
        name = (TextView) findViewById(R.id.name);
        tv1 = (EditText) findViewById(R.id.tv1);
        tv2 = (EditText)findViewById(R.id.tv2);
        tv3 = (EditText)findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (EditText)findViewById(R.id.tv5);
        tv6 = (EditText)findViewById(R.id.tv6);
        tv7 = (EditText)findViewById(R.id.tv7);
        tv8 = (EditText)findViewById(R.id.tv8);
        tv9 = (EditText)findViewById(R.id.tv9);
        tv10= (EditText)findViewById(R.id.tv10);
        xiugai.setOnClickListener(this);
        Glide.with(InfoActivitys.this).load("http://img01.sogoucdn.com/net/a/04/link?appid=100520145&url=http%3A%2F%2Fimg04.store.sogou.com%2Fapp%2Fa%2F10010016%2F53252fd7043f22aac5e4eeada1863e60")
                .placeholder(R.drawable.pic_tx).crossFade().error(R.drawable.pic_tx).into(iv_head);

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 日期格式为yyyy-MM-dd
                customDatePicker.show(tv4.getText().toString());
            }
        });

    }
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        java.util.Calendar calendar=java.util.Calendar.getInstance();
        calendar.roll(java.util.Calendar.DAY_OF_YEAR,0);//0为当天  下一天加1
        tv4.setText(sdf.format(calendar.getTime()).split(" ")[0]);
        //DateUtils.getTodayDateTimess()当天的时间
        //年月日显示
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String times) { // 回调接口，获得选中的时间
                tv4.setText(times.split(" ")[0]);
            }
        },"1918-01-01 00:00" ,"2110-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
    }
    List<User> list =new ArrayList<>();
    protected void indata(){

        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl+Constants.XiuUrl;
        params.put("turntableUserId", ValueStorage.getString("id"));
        params.put("turntableUsername", ValueStorage.getString("username"));
        //params.put("turntableUserPassword", ValueStorage.getString("id"));
        params.put("turntableUserSex", tv2.getText().toString());
        params.put("turntableUserAge", tv3.getText().toString());
        params.put("turntableUserBirthday", tv4.getText().toString());
        params.put("turntableUserPhone", tv5.getText().toString());
        params.put("turntableUserIdentityId", tv6.getText().toString());
        params.put("turntableUserAlipay", tv10.getText().toString());
        params.put("turntableUserBank",tv8.getText().toString());
        params.put("turntableUserBankUserName", tv7.getText().toString());
        params.put("turntableUserBankaddress", tv9.getText().toString());
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
                        ToastUtil.show(InfoActivitys.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(InfoActivitys.this, response.message);
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(InfoActivitys.this,response.message);
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tijiao:

                if(tv2.getText().toString().equals("")){
                    ToastUtil.show(InfoActivitys.this,"性别不能为空");
                    return;
                }else if(tv3.getText().toString().equals("")){
                    ToastUtil.show(InfoActivitys.this,"年龄不能为空");
                    return;
                }else if(tv4.getText().toString().equals("")){
                    ToastUtil.show(InfoActivitys.this,"出生日期不能为空");
                    return;
                }else if(tv5.getText().toString().equals("")){
                    ToastUtil.show(InfoActivitys.this,"手机号不能为空");
                    return;
                }else if(tv6.getText().toString().equals("")){
                    ToastUtil.show(InfoActivitys.this,"身份证号不能为空");
                    return;
                }else if(tv7.getText().toString().equals("")){
                    ToastUtil.show(InfoActivitys.this,"银行卡姓名不能为空");
                    return;
                }else if(tv8.getText().toString().equals("")){
                    ToastUtil.show(InfoActivitys.this,"银行卡号不能为空");
                    return;
                }else if(tv9.getText().toString().equals("")){
                    ToastUtil.show(InfoActivitys.this,"开户行不能为空");
                    return;
                }else if(tv10.getText().toString().equals("")){
                    ToastUtil.show(InfoActivitys.this,"支付宝不能为空");
                    return;
                }else{
                    indata();
                }
                break;
            default:
                break;
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
