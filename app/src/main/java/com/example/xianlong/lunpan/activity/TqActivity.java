package com.example.xianlong.lunpan.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.CpAdapter;
import com.example.xianlong.lunpan.bean.Aqi;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.Caipu;
import com.example.xianlong.lunpan.bean.Daily;
import com.example.xianlong.lunpan.bean.Hourly;
import com.example.xianlong.lunpan.bean.Index;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.Tianqi;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.ValueStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;
import com.zhy.http.okhttp.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class TqActivity extends BaseActivity implements AMapLocationListener {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private AlarmManager alarm = null;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tq);
        intview();
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 创建Intent对象，action为LOCATION
        alarmIntent = new Intent();
        alarmIntent.setAction("LOCATION");
        IntentFilter ift = new IntentFilter();

        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"LOCATION"的intent
        alarmPi = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION");
        registerReceiver(alarmReceiver, filter);
        dingwei();
    }
    boolean isok=true;
    public void dingwei(){
        initOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();

        if(null != alarm){
            //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 2*1000,
                    5 * 1000, alarmPi);
        }
    }
    private List<Index> index=new ArrayList<>();
    private List<Daily> daily=new ArrayList<>();
    private List<Hourly> hourly=new ArrayList<>();
    LinearLayout lay1;
    LinearLayout lay2;
    LinearLayout lay3;
    LinearLayout lay4;
    TextView du,xingqi,tianqis;
    TextView city;
    protected void intview(){
        city=(TextView)findViewById(R.id.city);
        du=(TextView)findViewById(R.id.du);
        xingqi=(TextView)findViewById(R.id.xingqi);
        tianqis=(TextView)findViewById(R.id.tianqi);
        lay1=(LinearLayout)findViewById(R.id.lay1);
        lay2=(LinearLayout)findViewById(R.id.lay2);
        lay3=(LinearLayout)findViewById(R.id.lay3);
        lay4=(LinearLayout)findViewById(R.id.lay4);
    }
    protected void indatas(String city){
        Map<String, String> params = new HashMap<>();
        String url= "http://jisutqybmf.market.alicloudapi.com/weather/query?city="+city;
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
                        ToastUtil.show(TqActivity.this,"网络异常");
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
                                Type type = new TypeToken<Tianqi>(){}.getType();
                                Tianqi tianqi = new Gson().fromJson(dataJson, type);
                                index=tianqi.index;
                                daily=tianqi.daily;
                                hourly=tianqi.hourly;
                                du.setText(tianqi.temp+"℃");
                                xingqi.setText(tianqi.week);
                                tianqis.setText(tianqi.weather);
                                for(int i=0;i<hourly.size();i++){
                                    LayoutInflater inflater = LayoutInflater.from(TqActivity.this);
                                    View view1 = inflater.inflate(R.layout.tq_item1, null);
                                    TextView tv1 = (TextView) view1.findViewById(R.id.tv1);
                                    TextView tv2 = (TextView) view1.findViewById(R.id.tv2);
                                    ImageView image = (ImageView) view1.findViewById(R.id.img);
                                    tv1.setText(hourly.get(i).time);
                                    tv2.setText(hourly.get(i).temp+"℃");
                                    images(hourly.get(i).img,image);
                                    //image.setImageDrawable(getResources().getDrawable(R.drawable.t1));
                                    /*Glide.with(TqActivity.this).load(hourly.get(i).img)
                                            .placeholder(R.drawable.t0).crossFade().error(R.drawable.t0).into(image);*/
                                    lay1.addView(view1);
                                }
                                for(int i=1;i<daily.size();i++){
                                    LayoutInflater inflater = LayoutInflater.from(TqActivity.this);
                                    View view2 = inflater.inflate(R.layout.tq_item2, null);
                                    TextView tv1 = (TextView) view2.findViewById(R.id.tv1);
                                    TextView tv2 = (TextView) view2.findViewById(R.id.tv2);
                                    TextView tv3= (TextView) view2.findViewById(R.id.tv3);
                                    ImageView image = (ImageView) view2.findViewById(R.id.img);
                                    tv1.setText(daily.get(i).week);
                                    if(daily.get(i).day.weather.equals(daily.get(i).night.weather)){
                                        tv3.setText(daily.get(i).day.weather);
                                    }else {
                                        tv3.setText(daily.get(i).day.weather+"转"+daily.get(i).night.weather);
                                    }
                                    tv2.setText(daily.get(i).night.templow+"℃"+"~"+daily.get(i).day.temphigh+"℃");
                                    //image.setImageDrawable(getResources().getDrawable(R.drawable.t1));
                                    images(daily.get(i).day.img,image);

                                   /* Glide.with(TqActivity.this).load(hourly.get(i).img)
                                            .placeholder(R.drawable.t0).crossFade().error(R.drawable.t0).into(image);*/
                                    lay2.addView(view2);
                                }
                                for(int i=0;i<index.size();i++){
                                    LayoutInflater inflater = LayoutInflater.from(TqActivity.this);
                                    View view3 = inflater.inflate(R.layout.tq_item3, null);
                                    TextView tv1 = (TextView) view3.findViewById(R.id.tv1);
                                    TextView tv2 = (TextView) view3.findViewById(R.id.tv2);
                                    TextView tv3= (TextView) view3.findViewById(R.id.tv3);
                                    tv1.setText(index.get(i).iname);
                                    tv2.setText(index.get(i).ivalue);
                                    tv3.setText(index.get(i).detail);
                                    lay4.addView(view3);
                                }
                                ToastUtil.show(TqActivity.this, response.msg);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(TqActivity.this,response.msg);
                        }
                    }
                });
    }

    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        String strInterval = "2000";
        if (!TextUtils.isEmpty(strInterval)) {
            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            locationOption.setInterval(Long.valueOf(strInterval));
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }

        if(null != alarmReceiver){
            unregisterReceiver(alarmReceiver);
            alarmReceiver = null;
        }
    }

    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            if(isok){
                city.setText(loc.getCity());
                indatas(loc.getCity());
                isok=false;
            }
        }
    }
    private BroadcastReceiver alarmReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("LOCATION")){
                if(null != locationClient){
                    locationClient.startLocation();
                }
            }
        }
    };

    private void images(String num,ImageView imageView) {
        if(num.equals("0")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t0).crossFade().error(R.drawable.t0).into(imageView);
        }else if(num.equals("1")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t1).crossFade().error(R.drawable.t1).into(imageView);
        }
        else if(num.equals("2")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t2).crossFade().error(R.drawable.t2).into(imageView);
        }else if(num.equals("3")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t3).crossFade().error(R.drawable.t3).into(imageView);
        }else if(num.equals("4")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t4).crossFade().error(R.drawable.t4).into(imageView);
        }else if(num.equals("5")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t5).crossFade().error(R.drawable.t5).into(imageView);
        }else if(num.equals("6")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t6).crossFade().error(R.drawable.t6).into(imageView);
        }else if(num.equals("7")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t7).crossFade().error(R.drawable.t7).into(imageView);
        }else if(num.equals("8")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t8).crossFade().error(R.drawable.t8).into(imageView);
        }else if(num.equals("9")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t9).crossFade().error(R.drawable.t9).into(imageView);
        }else if(num.equals("10")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t10).crossFade().error(R.drawable.t10).into(imageView);
        }else if(num.equals("11")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t11).crossFade().error(R.drawable.t11).into(imageView);
        }else if(num.equals("12")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t12).crossFade().error(R.drawable.t12).into(imageView);
        }else if(num.equals("13")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t13).crossFade().error(R.drawable.t13).into(imageView);
        }else if(num.equals("14")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t14).crossFade().error(R.drawable.t14).into(imageView);
        }else if(num.equals("15")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t15).crossFade().error(R.drawable.t15).into(imageView);
        }else if(num.equals("16")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t16).crossFade().error(R.drawable.t16).into(imageView);
        }else if(num.equals("17")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t17).crossFade().error(R.drawable.t17).into(imageView);
        }else if(num.equals("18")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t18).crossFade().error(R.drawable.t18).into(imageView);
        }else if(num.equals("19")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t19).crossFade().error(R.drawable.t19).into(imageView);
        }else if(num.equals("20")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t20).crossFade().error(R.drawable.t20).into(imageView);
        }else if(num.equals("21")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t21).crossFade().error(R.drawable.t21).into(imageView);
        }else if(num.equals("22")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t22).crossFade().error(R.drawable.t22).into(imageView);
        }else if(num.equals("23")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t23).crossFade().error(R.drawable.t23).into(imageView);
        }else if(num.equals("24")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t24).crossFade().error(R.drawable.t24).into(imageView);
        }else if(num.equals("25")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t25).crossFade().error(R.drawable.t25).into(imageView);
        }else if(num.equals("26")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t26).crossFade().error(R.drawable.t26).into(imageView);
        }else if(num.equals("27")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t27).crossFade().error(R.drawable.t27).into(imageView);
        }else if(num.equals("28")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t28).crossFade().error(R.drawable.t28).into(imageView);
        }else if(num.equals("29")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t29).crossFade().error(R.drawable.t29).into(imageView);
        }else if(num.equals("30")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t30).crossFade().error(R.drawable.t30).into(imageView);
        }else if(num.equals("31")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t31).crossFade().error(R.drawable.t31).into(imageView);
        }else if(num.equals("32")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t32).crossFade().error(R.drawable.t32).into(imageView);
        }else if(num.equals("49")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t49).crossFade().error(R.drawable.t49).into(imageView);
        }else if(num.equals("53")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t53).crossFade().error(R.drawable.t53).into(imageView);
        }else if(num.equals("54")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t54).crossFade().error(R.drawable.t54).into(imageView);
        }else if(num.equals("55")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t55).crossFade().error(R.drawable.t55).into(imageView);
        }else if(num.equals("56")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t56).crossFade().error(R.drawable.t56).into(imageView);
        }else if(num.equals("57")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t57).crossFade().error(R.drawable.t57).into(imageView);
        }else if(num.equals("58")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t58).crossFade().error(R.drawable.t58).into(imageView);
        }else if(num.equals("99")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t99).crossFade().error(R.drawable.t99).into(imageView);
        }else if(num.equals("301")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t301).crossFade().error(R.drawable.t301).into(imageView);
        }else if(num.equals("302")){
            Glide.with(TqActivity.this).load(num)
                    .placeholder(R.drawable.t302).crossFade().error(R.drawable.t302).into(imageView);
        }
    }
}
