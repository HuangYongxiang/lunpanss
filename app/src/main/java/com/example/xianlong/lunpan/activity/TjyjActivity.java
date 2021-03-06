package com.example.xianlong.lunpan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.ValueStorage;
import com.tendcloud.tenddata.TCAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class TjyjActivity extends BaseActivity{

    TextView xiangqing;
    EditText number;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tjyj);
        intView();
        boolean a=checkEmulator();
    }

    private static String getSystemProperty(String name)throws Exception {
        Class systemPropertyClazz = Class.forName("android.os.SystemProperties");
        return(String) systemPropertyClazz.getMethod("get",new Class[]{String.class}).invoke(systemPropertyClazz,new Object[]{name});
    }

    public static boolean checkEmulator(){
        try{
            boolean goldfish = getSystemProperty("ro.hardware").contains("goldfish");
            boolean emu = getSystemProperty("ro.kernel.qemu").length() >0;
            boolean sdk = getSystemProperty("ro.product.model").equals("sdk");
            if(emu || goldfish || sdk) {
                return true;
            }
        }catch(Exception e) {
        }
        return false;
    }
    protected void intView(){
        try {
            Class<?> mclass = Class.forName("android.os.SystemProperties");

            Method mmethod = mclass.getMethod("get", new Class[] { String.class,String.class });

            String result =(String) mmethod.invoke(mclass.newInstance(), new Object[] {"gsm.version.baseband", "no message" });

            Log.i("基带版本:", result);

        }catch(Exception e ){
            e.printStackTrace();
        }
        String a= android.os.Build.BOARD;
        String b=  android.os.Build.BOOTLOADER;


        number=(EditText)findViewById(R.id.number);
        xiangqing=(TextView) findViewById(R.id.xiangqing);
        btn=(Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValueStorage.getString("username").equals(number.getText().toString())){
                    ToastUtil.show(TjyjActivity.this,"邀请码不可用");
                }else{
                    indata();
                }

            }
        });
        xiangqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TjyjActivity.this,Tj_Activity.class));
            }
        });
    }

    protected void indata(){
        Map<String, String> params = new HashMap<>();
        //String url= "http://192.168.3.248:2323/"+ Constants.TuijianUrl;
        String url= Constants.BaseUrl+ Constants.TuijianUrl;
        params.put("turntableUsername",ValueStorage.getString("username"));//被推荐人
        if(number.getText().toString().equals("")){
            ToastUtil.show(TjyjActivity.this,"邀请码不能为空");
            return;
        }
        params.put("codes",number.getText().toString());//推荐人
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
                        ToastUtil.show(TjyjActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            ToastUtil.show(TjyjActivity.this,response.message);
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(TjyjActivity.this,response.message);
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
