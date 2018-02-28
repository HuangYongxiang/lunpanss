package com.example.xianlong.lunpan;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.example.xianlong.lunpan.activity.BaseActivity;
import com.example.xianlong.lunpan.activity.MoneyActivity;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.Guanggao;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.Neirong;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.IntenetUtil;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.Util;
import com.example.xianlong.lunpan.util.Utils;
import com.example.xianlong.lunpan.util.Utilss;
import com.example.xianlong.lunpan.util.ValueStorage;
import com.example.xianlong.lunpan.view.LuckPanLayout;
import com.example.xianlong.lunpan.view.RotatePan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tendcloud.tenddata.TCAgent;
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
import java.util.Map;

import okhttp3.Call;

public class MainActivity2 extends BaseActivity implements LuckPanLayout.AnimationEndListener,AMapLocationListener{

    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private ImageView goBtn;
    private ImageView yunIv;
    private String[] strs ;
    ImageView image;
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private AlarmManager alarm = null;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    private int TIME = 8000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);

        strs = getResources().getStringArray(R.array.names);
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.setAnimationEndListener(this);
        goBtn = (ImageView)findViewById(R.id.go);
        yunIv = (ImageView)findViewById(R.id.yun);
        strs = getResources().getStringArray(R.array.names);
        String a = String.valueOf((int)(Math.random() * 300+1));
        //indatas(Integer.valueOf(a));
        intview();
       // indatass();

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
        handler.postDelayed(runnable, TIME); //每隔10s执行
        handler1.postDelayed(runnable1, TIME); //每隔10s执行
        handler2.postDelayed(runnable2, TIME); //每隔10s执行
        handler3.postDelayed(runnable3, TIME); //每隔10s执行
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, TIME);
                intdata1();
                //ToastUtil.show(MainActivity.this,"1");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };
    Handler handler1 = new Handler();
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler1.postDelayed(this, TIME);
                intdata1();
                //ToastUtil.show(MainActivity.this,"2");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };
    Handler handler2 = new Handler();
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler2.postDelayed(this, TIME);
                intdata1();
                //ToastUtil.show(MainActivity.this,"3");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };
    Handler handler3 = new Handler();
    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler3.postDelayed(this, TIME);
                intdata1();
                //ToastUtil.show(MainActivity.this,"3");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };
    boolean isok=true;
    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    public void rotation(View view){
        String a = String.valueOf((int)(Math.random() * 10));
        luckPanLayout.rotate( Integer.valueOf(a),100);
       /* if(iswan){
            if(isok){
                isok=false;
                indata();
            }
        }*/
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
        handler.removeCallbacks(runnable);
        handler1.removeCallbacks(runnable1);
        handler2.removeCallbacks(runnable2);
        handler3.removeCallbacks(runnable3);
    }
    String lng="";
    String lat="";
    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            //info.setText(loc.getCity());
            lat=loc.getLatitude()+"";
            lng= loc.getLongitude()+"";
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
    TextView money,fenxiang,shu;
    protected void intview(){
        image=(ImageView) findViewById(R.id.image);
        money=(TextView)findViewById(R.id.qianbao);
        shu=(TextView)findViewById(R.id.shu);
        fenxiang=(TextView)findViewById(R.id.fenxiang);

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, MoneyActivity.class));
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //朋友圈
                mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = "http://47.95.122.185:8080/UserAdmin/tututu/lunpan.apk";
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = "幸运蚁";
                msg.description = "幸运蚁分享";
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.lun_logo2);
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                bmp.recycle();
                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = mTargetScene;
                api.sendReq(req);
            }
        });
    }
    boolean iswan=true;
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    protected void indata(){
        SimpleDateFormat sfs = new SimpleDateFormat("yyyy-MM-dd");
        String  startTime = sfs.format(new Date()).toString()+ " 00:00:00";
        String endTime = sfs.format(new Date()).toString() + " 23:59:59";
        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl+ Constants.ShuUrl;
        params.put("turntableUsername", ValueStorage.getString("username"));
        params.put("turntableUserId", ValueStorage.getString("id"));
        //params.put("turntableTaskTrialphone", "");
        params.put("turntableTaskTrialImei", Utils.getImei(MainActivity2.this));
        params.put("turntableTaskTrialIp", Utils.getip(MainActivity2.this));
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        showLoadingDialog("请求中....");
        OkHttpUtils.post()
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
                        dismissLoadingDialog();
                        ToastUtil.show(MainActivity2.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if(response.message.equals("请更换手机IMEI")){
                            isok=true;
                            dismissLoadingDialog();
                            ToastUtil.show(MainActivity2.this, "请更换手机");
                        }else {
                            if (response.code.equals("200")) {
                                dismissLoadingDialog();
                                ToastUtil.show(MainActivity2.this, response.message);
                                luckPanLayout.rotate(Integer.valueOf(response.number), 100);
                            } else {
                                if(response.message.equals("今天次数已转够，明天再来吧！")){
                                    iswan=false;
                                }

                                dismissLoadingDialog();
                                ToastUtil.show(MainActivity2.this, response.message);
                            }
                        }
                    }
                });
    }
    List<Neirong> neirongs=new ArrayList<>();
    protected void indatass(){
        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl+ Constants.RenwuUrl;
        params.put("turntableUserId", ValueStorage.getString("id"));
        showLoadingDialog("请求中....");
        OkHttpUtils.post()
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
                        dismissLoadingDialog();
                        ToastUtil.show(MainActivity2.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            try {
                                ToastUtil.show(MainActivity2.this, response.message);
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<Neirong>>(){}.getType();
                                neirongs = new Gson().fromJson(dataJson, type);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(MainActivity2.this,response.message);
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //离开
        TCAgent.onPageEnd(this,"MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //进
        TCAgent.onPageStart(this,"MainActivity");
    }
    boolean idred=true;//判断是不是红包
    @Override
    public void endAnimation(int position) {
        isok=true;
        idred=false;
        //indatas(Utils.getImei(MainActivity.this));
        intdata();
    }

    //弹出红包
    protected  void tongzhi1(String money){
        View view = LayoutInflater.from(myActivity).inflate(
                R.layout.view_alertdialog_four_option3, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView textw = (TextView) view.findViewById(R.id.text2);
        TextView text3 = (TextView) view.findViewById(R.id.text3);
        ImageView text5 = (ImageView) view.findViewById(R.id.text5);
        Button btn = (Button) view.findViewById(R.id.btn);
        Button btn2 = (Button) view.findViewById(R.id.btn2);
        text.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        text5.setVisibility(View.GONE);
        btn2.setVisibility(View.VISIBLE);
        textw.setVisibility(View.VISIBLE);
        text3.setVisibility(View.VISIBLE);
        text3.setText(money);
        final Dialog dialog2 = new Dialog(myActivity, R.style.custom_dialog);
        dialog2.setContentView(view);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                // startActivity(new Intent(MainActivity.this, MoneyActivity.class));
            }
        });
        dialog2.show();
    }
    boolean isa=false;//有新广告 false  无 true 获取洛米
    //弹出没东西
    protected  void tongzhi2(String data){
        View view = LayoutInflater.from(myActivity).inflate(
                R.layout.view_alertdialog_four_option3, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView textw = (TextView) view.findViewById(R.id.text2);
        TextView text3 = (TextView) view.findViewById(R.id.text3);
        ImageView text5 = (ImageView) view.findViewById(R.id.text5);
        Button btn = (Button) view.findViewById(R.id.btn);
        Button btn2 = (Button) view.findViewById(R.id.btn2);
        btn2.setVisibility(View.GONE);
        textw.setVisibility(View.GONE);
        text3.setVisibility(View.GONE);
        text5.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
        Glide.with(MainActivity2.this).load(imageUrl)
                .placeholder(R.drawable.btn_setting2).crossFade().error(R.drawable.btn_setting2).into(text5);
        final Dialog dialog2 = new Dialog(myActivity, R.style.custom_dialog);
        dialog2.setContentView(view);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                startActivity(browserIntent);
                if(isa){
                    dianji();
                }else {
                    dianji1();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }
    String count_url="";
    String click_url="";
    String urls="";
    String down_url="";
    String edown_url="";
    String imageUrl="";
    String finish_url="";
    protected void indatas(String imei) {
        WindowManager wm = MainActivity2.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Map<String, String> params = new HashMap<>();
        //String url = "http://sdk.cferw.com/api.php?z=5270&appkey=d0627f5d56a7c37570f040bde59c9218&deviceId="+ imei+"&sw="+width+"&sh="+height+"&osver="+ Build.VERSION.SDK_INT;
        String url = "http://sdk.cferw.com/api.php?z=3284&appkey=528ab6a566564a96a0b7646586f3c017&deviceId="+ imei+"&sw="+width+"&sh="+height+"&osver="+ Build.VERSION.SDK_INT;

        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .connTimeOut(80000)
                .readTimeOut(80000)
                .writeTimeOut(80000)
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.show(MainActivity2.this, "网络异常");
                    }
                    @Override
                    public void onResponse(final BeanResult response, int id) {
                        if (response.succ.equals("1")) {
                            urls=response.gotourl;
                            imageUrl=response.imgurl;
                            count_url=response.count_url;
                            click_url=response.click_url;
                            down_url=response.down_url;
                            edown_url=response.edown_url;
                            finish_url=response.finish_url;
                            tongzhi2("22");
                            huidiao();
                        } else {
                            ToastUtil.show(MainActivity2.this, "");
                        }
                    }
                });
    }
    protected void indat(String imei) {
        WindowManager wm = MainActivity2.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Map<String, String> params = new HashMap<>();
        //String url = "http://sdk.cferw.com/api.php?z=5270&appkey=d0627f5d56a7c37570f040bde59c9218&deviceId="+ imei+"&sw="+width+"&sh="+height+"&osver="+ Build.VERSION.SDK_INT;

        String url = "http://sdk.cferw.com/api.php?z=3284&appkey=528ab6a566564a96a0b7646586f3c017&deviceId="+ imei+"&sw="+width+"&sh="+height+"&osver="+ Build.VERSION.SDK_INT;
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .connTimeOut(80000)
                .readTimeOut(80000)
                .writeTimeOut(80000)
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.show(MainActivity2.this, "网络异常");
                    }
                    @Override
                    public void onResponse(final BeanResult response, int id) {
                        if (response.succ.equals("1")) {
                            urls=response.gotourl;
                            imageUrl=response.imgurl;
                            count_url=response.count_url;
                            click_url=response.click_url;
                            down_url=response.down_url;
                            edown_url=response.edown_url;
                            finish_url=response.finish_url;
                            huidiao();
                        } else {
                            ToastUtil.show(MainActivity2.this, "");
                        }
                    }
                });
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
                        // ToastUtil.show(TaskActivity2.this, "网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id) {

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
                        //ToastUtil.show(TaskActivity2.this, "网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id) {

                    }
                });
    }
    //不是红包时展现
    protected  void intdata1(){
        String  Model= Build.MODEL ;//型号
        String a1 =  Build.VERSION.SDK ;
        String a2=  Build.VERSION.RELEASE;//系统版本
        String a3 = Build.BRAND;//厂商
        String a = Utilss.getMacAddresss();//mac地址
        String b = Utilss.getip(MainActivity2.this);//ip
        String c = Utilss.getMacAddress();//mac地址
        String d= Utilss.getMacAddr();//mac地址
        int e= IntenetUtil.getNetworkState(MainActivity2.this);//网络类型
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

        float xdpi = MainActivity2.this.getResources().getDisplayMetrics().xdpi;
        float ydpi =  MainActivity2.this.getResources().getDisplayMetrics().ydpi;

        String x=xdpi+"";
        String y=ydpi+"";
        String x1 = x.substring(0, x.lastIndexOf("."));
        String y1 = y.substring(0, y.lastIndexOf("."));
        //String sd=x1+","+y1;
        String g= Utilss.getImei(MainActivity2.this);//imei
        String ts= Utilss.getTime();//imei
        WindowManager wm = MainActivity2.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        String sss=Utilss.getIsmi(MainActivity2.this);
      /*  if(Utilss.getIsmi(MainActivity.this)==null){
            ToastUtil.show(MainActivity.this,"请安装手机卡");
            return;
        }*/
        Map<String, String> params = new HashMap<>();
        params.put("appid","2893561");//应用ID	测试ID:2151681
        params.put("ts",ts);//	时间戳(13位毫秒级)	如：1513753247587
        params.put("sign",Utilss.MD5(ts+"3030bb7bf11c0872923111eaf649d2cb"));//签名是由时间戳ts加token再md5生成
        params.put("os","1");//系统类型	ios：2;android:1
        params.put("screenwidth",width+"");//屏幕宽度	例如:640
        params.put("screenheight",height+"");//屏幕高度	例如:960
        params.put("imsi",Utilss.getIsmi(MainActivity2.this));//运营商识别码	例如:460011418603055
        params.put("ua",Utilss.getua(MainActivity2.this));//浏览器的user-agent
        params.put("mac",c);//网卡地址	例如：02:00:2e:00:46:3a
        params.put("network",network);//	网络类型	wifi 2g 3g 4g等
        params.put("sd",sd);//Density independent pixels(dpi)	如：160,240
        params.put("imei",g);//IMEI号	仅安卓机型提供
        params.put("osversion",a2);//系统版本号	如：9.3.3
        params.put("vendor",a3);//手机生产厂商	如：samsung
        params.put("model",Model);//手机型号	如:Galaxy Note8
        params.put("androidid",Utilss.getAndroidID(MainActivity2.this));//安卓id	仅安卓机型提供
        if(lat==null){
            params.put("lat",getIntent().getStringExtra("lat"));//	经度
            params.put("lng",getIntent().getStringExtra("lng"));//	纬度
        }else {
            params.put("lat", lat);//	经度
            params.put("lng", lng);//	纬度
        }
        params.put("ip",b);
        //ToastUtil.show(MainActivity2.this,lat+"-"+lng);
        String url= "http://116.62.77.111/public/getCommonInformationAd.shtml";
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        ToastUtil.show(MainActivity2.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        //ToastUtil.show(MainActivity.this, "11111111111");
                        if(response==null){
                            //获取广告失败
                            //ToastUtil.show(MainActivity.this,"获取广告失败");
                        }else {
                            if (response.state == null) {
                                //成功
                                displayreport=response.displayreport;
                                queding();
                                //ToastUtil.show(MainActivity.this, "222222222");
                            } else {
                                if (response.state.equals("500")) {
                                    //获取广告异常
                                    //{"data":null,"state":"500","message":"获取广告异常"}
                                    ToastUtil.show(MainActivity2.this, "获取广告异常");
                                } else {
                                    //成功
                                    displayreport=response.displayreport;
                                    queding();
                                    //ToastUtil.show(MainActivity.this, "222222222");
                                }
                            }
                        }
                    }
                });
    }
    List<String> displayreport=new ArrayList<>();
    List<String> clickreport=new ArrayList<>();
    boolean isq=true;
    boolean isd=true;
    boolean isurl=false;//判断是否为下载
    protected  void intdata(){
        String  Model= Build.MODEL ;//型号
        String a1 =  Build.VERSION.SDK ;
        String a2=  Build.VERSION.RELEASE;//系统版本
        String a3 = Build.BRAND;//厂商
        String a = Utilss.getMacAddresss();//mac地址
        String b = Utilss.getip(MainActivity2.this);//ip
        String c = Utilss.getMacAddress();//mac地址
        String d= Utilss.getMacAddr();//mac地址
        int e= IntenetUtil.getNetworkState(MainActivity2.this);//网络类型
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

        float xdpi = MainActivity2.this.getResources().getDisplayMetrics().xdpi;
        float ydpi =  MainActivity2.this.getResources().getDisplayMetrics().ydpi;

        String x=xdpi+"";
        String y=ydpi+"";
        String x1 = x.substring(0, x.lastIndexOf("."));
        String y1 = y.substring(0, y.lastIndexOf("."));
        //String sd=x1+","+y1;
        String g= Utilss.getImei(MainActivity2.this);//imei
        String ts= Utilss.getTime();//imei
        WindowManager wm = MainActivity2.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Map<String, String> params = new HashMap<>();
        params.put("appid","2893561");//应用ID	测试ID:2151681
        params.put("ts",ts);//	时间戳(13位毫秒级)	如：1513753247587
        params.put("sign",Utilss.MD5(ts+"3030bb7bf11c0872923111eaf649d2cb"));//签名是由时间戳ts加token再md5生成
        params.put("os","1");//系统类型	ios：2;android:1
        params.put("screenwidth",width+"");//屏幕宽度	例如:640
        params.put("screenheight",height+"");//屏幕高度	例如:960
        params.put("imsi",Utilss.getIsmi(MainActivity2.this));//运营商识别码	例如:460011418603055
        params.put("ua",Utilss.getua(MainActivity2.this));//浏览器的user-agent
        params.put("mac",c);//网卡地址	例如：02:00:2e:00:46:3a
        params.put("network",network);//	网络类型	wifi 2g 3g 4g等
        params.put("sd",sd);//Density independent pixels(dpi)	如：160,240
        params.put("imei",g);//IMEI号	仅安卓机型提供
        params.put("osversion",a2);//系统版本号	如：9.3.3
        params.put("vendor",a3);//手机生产厂商	如：samsung
        params.put("model",Model);//手机型号	如:Galaxy Note8
        params.put("androidid",Utilss.getAndroidID(MainActivity2.this));//安卓id	仅安卓机型提供
        if(lat==null){
            params.put("lat",getIntent().getStringExtra("lat"));//	经度
            params.put("lng",getIntent().getStringExtra("lng"));//	纬度
        }else {
            params.put("lat", lat);//	经度
            params.put("lng", lng);//	纬度
        }
        params.put("ip",b);
        //ToastUtil.show(MainActivity2.this,getIntent().getStringExtra("lat")+getIntent().getStringExtra("lng"));
        String url= "http://116.62.77.111/public/getCommonInformationAd.shtml";
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        ToastUtil.show(MainActivity2.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if(response==null){
                            //获取广告失败
                            //ToastUtil.show(MainActivity.this,"获取广告失败");
                            indatas(Utils.getImei(MainActivity2.this));
                            isa=true;
                        }else {
                            isa=false;
                            if (response.state == null) {
                                //成功
                                displayreport=response.displayreport;
                                queding();
                                clickreport=response.clickreport;
                                imageUrl=response.imageUrl;
                                isurl = response.json;

                                if (response.json) {
                                    urls=response.uri;
                                    if(urls.contains("IT_CLK_PNT_DOWN_X")){
                                        urls=urls.replace("IT_CLK_PNT_DOWN_X","450");
                                    }
                                    if(urls.contains("IT_CLK_PNT_DOWN_Y")){
                                        urls=urls.replace("IT_CLK_PNT_DOWN_Y","567");
                                    }
                                    if(urls.contains("IT_CLK_PNT_UP_X")){
                                        urls=urls.replace("IT_CLK_PNT_UP_X","450");
                                    }
                                    if(urls.contains("IT_CLK_PNT_UP_Y")){
                                        urls=urls.replace("IT_CLK_PNT_UP_Y","567");
                                    }
                                    intdatas(urls);
                                } else {
                                    urls = response.uri;
                                    if(idred){
                                    }else {
                                        tongzhi2("22");
                                    }
                                }
                            } else {
                                if (response.state.equals("500")) {
                                    //获取广告异常
                                    //{"data":null,"state":"500","message":"获取广告异常"}
                                    ToastUtil.show(MainActivity2.this, "获取广告异常");
                                } else {
                                    //成功
                                    displayreport=response.displayreport;
                                    queding();

                                    clickreport=response.clickreport;
                                    imageUrl=response.imageUrl;
                                    isurl = response.json;
                                    if (response.json) {
                                        urls=response.uri;
                                        if(urls.contains("IT_CLK_PNT_DOWN_X")){
                                            urls=urls.replace("IT_CLK_PNT_DOWN_X","450");
                                        }
                                        if(urls.contains("IT_CLK_PNT_DOWN_Y")){
                                            urls=urls.replace("IT_CLK_PNT_DOWN_Y","567");
                                        }
                                        if(urls.contains("IT_CLK_PNT_UP_X")){
                                            urls=urls.replace("IT_CLK_PNT_UP_X","450");
                                        }
                                        if(urls.contains("IT_CLK_PNT_UP_Y")){
                                            urls=urls.replace("IT_CLK_PNT_UP_Y","567");
                                        }
                                        intdatas(urls);
                                    } else {
                                        urls = response.uri;
                                        if(idred){
                                        }else {
                                            tongzhi2("22");
                                        }
                                    }
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
                        ToastUtil.show(MainActivity2.this,"网络异常");
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
                            if(idred){
                            }else {
                                tongzhi2("22");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                });
    }
    protected void queding(){
        for(int i=0;i<displayreport.size();i++){
            Map<String, String> params = new HashMap<>();
            String url= displayreport.get(i);
            OkHttpUtils.get()
                    .url(url)
                    .build()
                    .writeTimeOut(80000)
                    .readTimeOut(80000)
                    .connTimeOut(80000)
                    .execute(new GenericsCallback<Object>(new JsonGenericsSerializator())
                    {
                        @Override
                        public void onError(Call call, Exception e, int id)
                        {
                            ToastUtil.show(MainActivity2.this,"网络异常");
                        }

                        @Override
                        public void onResponse(final Object response, int id)
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
                urlss=urlss.replace("IT_CLK_PNT_DOWN_X","450");
            }
            if(urlss.contains("IT_CLK_PNT_DOWN_Y")){
                urlss=urlss.replace("IT_CLK_PNT_DOWN_Y","567");
            }
            if(urlss.contains("IT_CLK_PNT_UP_X")){
                urlss=urlss.replace("IT_CLK_PNT_UP_X","450");
            }
            if(urlss.contains("IT_CLK_PNT_UP_Y")){
                urlss=urlss.replace("IT_CLK_PNT_UP_Y","567");
            }
            String url=urlss;
            //String url= clickreport.get(i);
            OkHttpUtils.get()
                    .params(params)
                    .url(url)
                    .build()
                    .execute(new GenericsCallback<Object>(new JsonGenericsSerializator())
                    {
                        @Override
                        public void onError(Call call, Exception e, int id)
                        {
                            ToastUtil.show(MainActivity2.this,"网络异常");
                        }

                        @Override
                        public void onResponse(final Object response, int id)
                        {
                        }
                    });
        }
    }
    private String[] strss ={ "868508025912208",
            "867620026569399",
            "867620026561651",
            "867789022790273",
            "867789022790208",
            "868508025929491",
            "868239023223568",
            "868853027306201",
            "867620026569431",
            "867789022790455",
            "867352026901667",
            "867876029877797",
            "867568020418743",
            "869409023346243",
            "867568028915096",
            "868508025989719",
            "867620026561693",
            "867168027289187",
            "865815025213542",
            "867352026911831",
            "867568028922852",
            "865815020695222",
            "865815029531758",
            "867352024349877",
            "867663021995508",
            "867352028287685",
            "865814024518290",
            "869409028977224",
            "867718024074527",
            "867718028473725",
            "867909026267173",
            "868436024456953",
            "867909026267389",
            "866968021253449",
            "866968020342524",
            "867718028624228",
            "867718028624293",
            "867275025734799",
            "868436024554542",
            "868238029735963",
            "868978027308665",
            "868978028766705",
            "868978028032504",
            "868978027795044",
            "868978027551025",
            "869803020813215",
            "868978028521589",
            "868978027551710",
            "869803020866080",
            "869803020830011",
            "868978028743001",
            "868978028031324",
            "869803020062649",
            "869803020070949",
            "868978028030565",
            "869803020001084",
            "869800021943397",
            "869800021514909",
            "869800022749595",
            "869800022337243",
            "869800022729977",
            "869800022139276",
            "869800021952513",
            "869800022741808",
            "869800022534450",
            "869800022345998",
            "869800021315083",
            "869800022542792",
            "869800022139292",
            "869800021920601",
            "869800021949816",
            "869800022330461",
            "869800021395382",
            "869800022140183",
            "869800022333275",
            "869800022143237",
            "869800021917797",
            "869800022544459",
            "869800021548576",
            "869800021549152",
            "869800022543857",
            "869800022742418",
            "869800021945848",
            "869800021541159",
            "869800022152204",
            "869800022148293",
            "869800021743854",
            "869800022342995",
            "869800021553501",
            "869800021312809",
            "869800022741626",
            "869800021547578",
            "869800021548550",
            "869800028816117",
            "869800028701582",
            "869800028812405",
            "869800028857830",
            "869411027465037",
            "869801028502038",
            "869801028111590",
            "869410021876553",
            "868809024889821",
            "868809024890183",
            "868978027551751",
            "868978028275400",
            "868978028766085",
            "868978028279816",
            "868978028517041",
            "868978027303344",
            "868978027789443",
            "868978028274965",
            "868978028030144",
            "868978027303161",
            "869803020962186",
            "869803020303191",
            "868978028032090",
            "869800021701001",
            "869800022740750",
            "869800022741162",
            "869800022319456",
            "869800022741899",
            "869800021517761",
            "869800021748044",
            "869800021952059",
            "869800022543055",
            "869800021941573",
            "869800022338670",
            "869800021308518",
            "869800022739620",
            "869800022534583",
            "869800022750569",
            "869800021753770",
            "869800022130556",
            "869800021956126",
            "869800022319613",
            "869800022339330",
            "869800022323854",
            "869800021548485",
            "869800022534534",
            "869800022748993",
            "869800022534492",
            "869800022332251",
            "869800021950277",
            "869800022747953",
            "869800022749082",
            "869800022333234",
            "869800021745487",
            "869800022156130",
            "869800022543113",
            "869800022543238",
            "869800021554897",
            "869800021539211",
            "869800022338472",
            "869800022337797",
            "869800028810656",
            "869800028701665",
            "869800028749557",
            "869800028742693",
            "869800028854027",
            "869800028857012",
            "869800028664004",
            "869800028700741",
            "869800028703737",
            "869800028666934",
            "869411025932699",
            "869410021890216",
            "869801028502962",
            "869801028111632",
            "869801028111665",
            "869410021876769",
            "868809024895968",
            "868809025252128",
            "868978028523494",
            "868978028518106",
            "868978027789559",
            "869803020812860",
            "869803020949068",
            "868978028031225",
            "869803020115983",
            "869803020176449",
            "868978028275335",
            "869800022739877",
            "869800021459196",
            "869800022129830",
            "869800021746956",
            "869800022149234",
            "869800022149341",
            "869800021552396",
            "869800021950913",
            "869800022329752",
            "869800021530822",
            "869800021937654",
            "869800022734605",
            "869800022749371",
            "869800021743284",
            "869800022536190",
            "869800022730801",
            "869800021754349",
            "869800021740280",
            "869800021307106",
            "869800021549285",
            "869800022749041",
            "869800022533072",
            "869800021937704",
            "869800021950988",
            "869800022329950",
            "869800021399046",
            "869800021739381",
            "869800021746253",
            "869800021943751",
            "869800022341690",
            "869800021307296",
            "869800021552917",
            "869800021755007",
            "869800021943702",
            "869800022142510",
            "869800021942183",
            "869800028702234",
            "869800028738030",
            "869800028853276",
            "869800028853839",
            "869800028666587",
            "869800028667130",
            "869411025933655",
            "869411025933168",
            "869410021884474",
            "869410021880191",
            "869410021890356",
            "869801028113273",
            "869410021890158",
            "868809024889706",
            "868809025248258",
            "868978028518122",
            "868978028275301",
            "868978028277844",
            "868978027789310",
            "868978028275921",
            "868978028273066",
            "868978028031480",
            "869800022742079",
            "869800021743896",
            "869800022331683",
            "869800021747202",
            "869800021401545",
            "869800021744167",
            "869800021460491",
            "869800022722501",
            "869800022748225",
            "869800021752509",
            "869800022337961",
            "869800021941987",
            "869800021944049",
            "869800022750593",
            "869800022150018",
            "869800022329448",
            "869800021556439",
            " 869800021307221",
            "869800022749454",
            "869800022149416",
            "869800021952364",
            "869800022534997",
            "869800021951242",
            "869800021937829",
            "869800021541563",
            "869800021541704",
            "869800022331584",
            "869800022319605",
            "869800021534188",
            "869800022536075",
            "869800022741154",
            "869800022543626",
            "869800022345469",
            "869800021739928",};

}
