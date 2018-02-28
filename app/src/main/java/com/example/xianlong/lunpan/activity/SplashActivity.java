package com.example.xianlong.lunpan.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.xianlong.lunpan.MainActivity;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.Guanggao;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.util.IntenetUtil;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.Utils;
import com.example.xianlong.lunpan.util.Utilss;
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

/**
 * Created by Administrator on 2015/8/8.
 */

public class SplashActivity extends Activity implements AMapLocationListener,View.OnTouchListener {
    private Context context;
    static final int PHONE_REQUEST_CODE = 66;
    ImageView img,img2;
    private TimeCount timeCount;
    FrameLayout fy;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private AlarmManager alarm = null;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        requestPermissions();
        intView();
        timeCount = new TimeCount(5000, 1000);
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
    String urls="";
    TextView time;
    boolean istrue=false;
    boolean isdianji=true;
    protected void intView(){
        img=(ImageView)findViewById(R.id.img);
        img2=(ImageView)findViewById(R.id.imgs);
        fy=(FrameLayout) findViewById(R.id.fy);
        time=(TextView) findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCount.cancel();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });
        img.setOnTouchListener(this );//执行Touch不执行OnClick
       /* img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                startActivity(browserIntent);*//*
                if (isurl) {
                    if(urls.contains("IT_CLK_PNT_DOWN_X")){

                    } if(urls.contains("IT_CLK_PNT_DOWN_Y")){

                    }
                    if(urls.contains("IT_CLK_PNT_UP_X")){

                    }
                    if(urls.contains("IT_CLK_PNT_UP_Y")){

                    }
                    intdatas(urls);
                } else {
                    Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                    startActivity(browserIntent);
                }
                istrue=true;
                timeCount.cancel();

                //dianji();
                if(isdianji){
                    dianji1();
                    isdianji=false;
                }
            }
        });*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img2.setVisibility(View.GONE);
                fy.setVisibility(View.VISIBLE);
                timeCount.start();
            }
        }, 2000);

    }
    int downx=0;
    int downy=0;
    int upx=0;
    int upy=0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            /**
             * 点击的开始位置
             */
            case MotionEvent.ACTION_DOWN:
                //tvTouchShowStart.setText("起始位置：(" + event.getX() + "," + event.getY());
                downx=(int)event.getX();
                downy=(int)event.getY();
                break;
            /**
             * 触屏实时位置
             */
            case MotionEvent.ACTION_MOVE:
                //tvTouchShow.setText("实时位置：(" + event.getX() + "," + event.getY());
                break;
            /**
             * 离开屏幕的位置
             */
            case MotionEvent.ACTION_UP:
                //tvTouchShow.setText("结束位置：(" + event.getX() + "," + event.getY());
                upx=(int)event.getX();
                upy=(int)event.getY();
                int a=downx;
                int b=downy;
                int c=upx;
                int d=upy;
                if (isurl) {
                    if(urls.contains("IT_CLK_PNT_DOWN_X")){
                        urls=urls.replace("IT_CLK_PNT_DOWN_X",downx+"");
                    }
                    if(urls.contains("IT_CLK_PNT_DOWN_Y")){
                        urls=urls.replace("IT_CLK_PNT_DOWN_Y",downy+"");
                    }
                    if(urls.contains("IT_CLK_PNT_UP_X")){
                        urls=urls.replace("IT_CLK_PNT_UP_X",upx+"");
                    }
                    if(urls.contains("IT_CLK_PNT_UP_Y")){
                        urls=urls.replace("IT_CLK_PNT_UP_Y",upy+"");
                    }
                    intdatas(urls);
                } else {
                    if(urls.contains("IT_CLK_PNT_DOWN_X")){
                        urls=urls.replace("IT_CLK_PNT_DOWN_X",downx+"");
                    }
                    if(urls.contains("IT_CLK_PNT_DOWN_Y")){
                        urls=urls.replace("IT_CLK_PNT_DOWN_Y",downy+"");
                    }
                    if(urls.contains("IT_CLK_PNT_UP_X")){
                        urls=urls.replace("IT_CLK_PNT_UP_X",upx+"");
                    }
                    if(urls.contains("IT_CLK_PNT_UP_Y")){
                        urls=urls.replace("IT_CLK_PNT_UP_Y",upy+"");
                    }
                    Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                    startActivity(browserIntent);
                }
                istrue=true;
                timeCount.cancel();
                if(isa){
                    dianji();
                }else {
                    if (isdianji) {
                        dianji1();
                        isdianji = false;
                    }
                }
                break;
            default:
                break;
        }
        /**
         *  注意返回值
         *  true：view继续响应Touch操作；
         *  false：view不再响应Touch操作，故此处若为false，只能显示起始位置，不能显示实时位置和结束位置
         */
        return true;
    }

    String count_url="";
    String click_url="";
    protected void indata() {
        WindowManager wm = SplashActivity.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Map<String, String> params = new HashMap<>();
        //String url = "http://sdk.cferw.com/api.php?z=5272&appkey=d0627f5d56a7c37570f040bde59c9218&deviceId="+ Utils.getImei(SplashActivity.this)+"&sw="+width+"&sh="+height+"&osver="+ Build.VERSION.SDK_INT;

        String url = "http://sdk.cferw.com/api.php?z=3286&appkey=528ab6a566564a96a0b7646586f3c017&deviceId="+ Utils.getImei(SplashActivity.this)+"&sw="+width+"&sh="+height+"&osver="+ Build.VERSION.SDK_INT;
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.show(SplashActivity.this, "网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id) {
                        if (response.succ.equals("1")) {
                            Glide.with(SplashActivity.this).load(response.imgurl)
                                    .placeholder(R.drawable.sp_background).crossFade().error(R.drawable.sp_background).into(img);
                            urls=response.gotourl;
                            count_url=response.count_url;
                            click_url=response.click_url;
                            huidiao();
                        } else {
                            ToastUtil.show(SplashActivity.this, "");
                        }
                    }
                });
    }
    private void requestPermissions() {
        //判断是否开启权限
        if ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            //indata();
        } else {
            //请求获取摄权限
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_REQUEST_CODE);
        }

    }
    /**
     * 请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PHONE_REQUEST_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) ) {
                // indata();
            } else {
                Toast.makeText(context, "已拒绝权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }

        public void onTick(long millisUntilFinished) {
            //tvGetCode.setClickable(false);
            time.setText("跳过"+millisUntilFinished / 1000  +"s");

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this,"SplashActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(istrue){
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
        TCAgent.onPageStart(this,"SplashActivity");
    }

    private void  huidiao(){
        Map<String, String> params = new HashMap<>();
        String url = count_url;
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.show(SplashActivity.this, "网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id) {
                        if (response.succ.equals("1")) {
                            // ToastUtil.show(SplashActivity.this, "111111111111111");
                        } else {
                            ToastUtil.show(SplashActivity.this, "");
                        }
                    }
                });
    }
    private void dianji(){
        Map<String, String> params = new HashMap<>();
        String url = click_url;
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.show(SplashActivity.this, "网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id) {
                        if (response.succ.equals("1")) {
                            //ToastUtil.show(SplashActivity.this, "22222222222222222222");
                        } else {
                            ToastUtil.show(SplashActivity.this, "");
                        }
                    }
                });
    }


    boolean isa=false;
    boolean isurl=false;//判断是否为下载
    protected  void intdata(){
        String  Model= android.os.Build.MODEL ;//型号
        String a1 =  android.os.Build.VERSION.SDK ;
        String a2=  android.os.Build.VERSION.RELEASE;//系统版本
        String a3 = android.os.Build.BRAND;//厂商
        String a = Utilss.getMacAddresss();//mac地址
        String b = Utilss.getip(SplashActivity.this);//ip
        String c = Utilss.getMacAddress();//mac地址
        String d= Utilss.getMacAddr();//mac地址
        int e= IntenetUtil.getNetworkState(SplashActivity.this);//网络类型
        String network=e+"";
        if(e==1){
            network="wifi";
        }else{
            network=e+"g";
        }
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int w1 = mDisplayMetrics.widthPixels;
        int h1 = mDisplayMetrics.heightPixels;
        float density = mDisplayMetrics.densityDpi;
        String density1=density+"";
        String density2 = density1.substring(0, density1.lastIndexOf("."));
        String sd="160"+","+density2;

        float xdpi = context.getResources().getDisplayMetrics().xdpi;
        float ydpi = context.getResources().getDisplayMetrics().ydpi;
        String x=xdpi+"";
        String y=ydpi+"";
        String x1 = x.substring(0, x.lastIndexOf("."));
        String y1 = y.substring(0, y.lastIndexOf("."));
        //String sd=x1+","+y1;

        String g= Utilss.getImei(SplashActivity.this);//imei
        String ts= Utilss.getTime();//imei
        WindowManager wm = SplashActivity.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Map<String, String> params = new HashMap<>();
        params.put("appid","2893561");//应用ID	测试ID:2151681
        params.put("ts",ts);//	时间戳(13位毫秒级)	如：1513753247587
        //params.put("sign",Utilss.MD5(ts+"9e82dff7c07715795247ed72b56c1a0f"));//签名是由时间戳ts加token再md5生成测试
        params.put("sign",Utilss.MD5(ts+"3030bb7bf11c0872923111eaf649d2cb"));
        params.put("os","1");//系统类型	ios：2;android:1
        params.put("screenwidth",width+"");//屏幕宽度	例如:640
        params.put("screenheight",height+"");//屏幕高度	例如:960
        params.put("imsi",Utilss.getIsmi(SplashActivity.this));//运营商识别码	例如:460011418603055
        params.put("ua",Utilss.getua(SplashActivity.this));//浏览器的user-agent
        params.put("mac",c);//网卡地址	例如：02:00:2e:00:46:3a
        params.put("network",network);//	网络类型	wifi 2g 3g 4g等
        params.put("sd",sd);//Density independent pixels(dpi)	如：160,240
        params.put("imei",g);//IMEI号	仅安卓机型提供
        params.put("osversion",a2);//系统版本号	如：9.3.3
        params.put("vendor",a3);//手机生产厂商	如：samsung
        params.put("model",Model);//手机型号	如:Galaxy Note8
        params.put("androidid",Utilss.getAndroidID(SplashActivity.this));//安卓id	仅安卓机型提供
        params.put("lat",lat);//	经度
        params.put("lng",lng);//	纬度
        params.put("ip",b);
        // String url= "http://116.62.77.111/public/getCommonInformationAd.shtml";
        String url= "http://116.62.77.111/public/getCommonStartUpAd.shtml";
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        ToastUtil.show(SplashActivity.this,"网络异常");
                    }
                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if(response==null){
                            //获取广告失败
                            //ToastUtil.show(SplashActivity.this,"获取广告失败");
                            isa=true;
                            indata();
                        }else {
                            isa=false;
                            if (response.state == null) {
                                //成功
                                displayreport=response.displayreport;
                                queding();
                                clickreport=response.clickreport;
                                isurl = response.json;
                                if (response.json) {
                                    //intdatas(response.url);
                                    urls=response.url;
                                } else {
                                    urls = response.url;
                                }
                                Glide.with(SplashActivity.this).load(response.imageUrl)
                                        .placeholder(R.drawable.btn_setting2).crossFade().error(R.drawable.btn_setting2).into(img);
                            } else {
                                if (response.state.equals("500")) {
                                    //获取广告异常
                                    //{"data":null,"state":"500","message":"获取广告异常"}
                                    ToastUtil.show(SplashActivity.this, "获取广告异常");
                                } else {
                                    //成功
                                    displayreport=response.displayreport;
                                    queding();
                                    clickreport=response.clickreport;
                                    isurl = response.json;
                                    if (response.json) {
                                        //intdatas(response.url);
                                        urls=response.url;
                                    } else {
                                        urls = response.url;
                                    }
                                    Glide.with(SplashActivity.this).load(response.imageUrl)
                                            .placeholder(R.drawable.btn_setting2).crossFade().error(R.drawable.btn_setting2).into(img);
                                }
                            }
                        }
                    }
                });
    }
    protected  void intdatas(String urlss){

        Map<String, String> params = new HashMap<>();
        String url= urlss;
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        ToastUtil.show(SplashActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        try {
                            String object = new Gson().toJson(response);
                            JSONObject jsonObject = new JSONObject(object);
                            String dataJson;
                            dataJson = jsonObject.optString("data");
                            Type type = new TypeToken<Guanggao>(){}.getType();
                            Guanggao gao = new Gson().fromJson(dataJson, type);
                            urls= gao.dstlink;
                            Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                            startActivity(browserIntent);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                });
    }
    List<String> displayreport=new ArrayList<>();
    List<String> clickreport=new ArrayList<>();
    protected void queding(){
        for(int i=0;i<displayreport.size();i++){
            Map<String, String> params = new HashMap<>();
            String url= displayreport.get(i);
            OkHttpUtils.get()
                    .params(params)
                    .url(url)
                    .build()
                    .connTimeOut(80000)
                    .readTimeOut(80000)
                    .writeTimeOut(80000)
                    .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                    {
                        @Override
                        public void onError(Call call, Exception e, int id)
                        {
                            ToastUtil.show(SplashActivity.this,"网络异常");
                        }

                        @Override
                        public void onResponse(final BeanResult response, int id)
                        {
                        }
                    });
        }

    }
    protected void dianji1(){
        for(int i=0;i<clickreport.size();i++){
            Map<String, String> params = new HashMap<>();
            String urlss= clickreport.get(i);
            if(urlss.contains("IT_CLK_PNT_DOWN_X")){
                urlss=urlss.replace("IT_CLK_PNT_DOWN_X",downx+"");
            }
            if(urlss.contains("IT_CLK_PNT_DOWN_Y")){
                urlss=urlss.replace("IT_CLK_PNT_DOWN_Y",downy+"");
            }
            if(urlss.contains("IT_CLK_PNT_UP_X")){
                urlss=urlss.replace("IT_CLK_PNT_UP_X",upx+"");
            }
            if(urlss.contains("IT_CLK_PNT_UP_Y")){
                urlss=urlss.replace("IT_CLK_PNT_UP_Y",upy+"");
            }
            String url=urlss;
            OkHttpUtils.post()
                    .params(params)
                    .url(url)
                    .build()
                    .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                    {
                        @Override
                        public void onError(Call call, Exception e, int id)
                        {
                            //ToastUtil.show(SplashActivity.this,"网络异常");
                        }

                        @Override
                        public void onResponse(final BeanResult response, int id)
                        {
                        }
                    });
        }
    }
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
    String lng="";
    String lat="";
    boolean isding=true;
    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            //info.setText(loc.getCity());
            if(isding){
                isding=false;
                lat=loc.getLatitude()+"";
                lng= loc.getLongitude()+"";
                intdata();

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
}
