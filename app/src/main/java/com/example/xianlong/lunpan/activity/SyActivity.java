package com.example.xianlong.lunpan.activity;
import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.xianlong.lunpan.MainActivity;
import com.example.xianlong.lunpan.MainActivity2;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.MyPagerAdapter;
import com.example.xianlong.lunpan.bean.Banners;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.Ggitem;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.Vesion;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.dialog.DownloadDialog;
import com.example.xianlong.lunpan.dialog.DownloadUtil;
import com.example.xianlong.lunpan.util.IntenetUtil;
import com.example.xianlong.lunpan.util.TimeUtils;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.Utils;
import com.example.xianlong.lunpan.util.Utilss;
import com.example.xianlong.lunpan.util.ValueStorage;
import com.example.xianlong.lunpan.util.ViewPagerHandlerUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zhy.http.okhttp.callback.GenericsCallback;
import okhttp3.Call;

public class SyActivity extends BaseActivity1 implements View.OnClickListener,AMapLocationListener{
    private List<String> imagesList = new ArrayList<>();
    private List<Banners> images = new ArrayList<>();
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private AlarmManager alarm = null;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sy2);
        requestPermissions();
        /*获取当前系统的android版本号*/
        int currentapiVersion=android.os.Build.VERSION.SDK_INT;
        String  Model= android.os.Build.MODEL + ","//型号
                + android.os.Build.VERSION.SDK + ","
                + android.os.Build.VERSION.RELEASE+","//系统版本
                +android.os.Build.BRAND;//厂商
        intview();
        intdata();
        //indata("123");
        banben();
        String a = Utilss.getMacAddresss();//mac地址
        String b = Utilss.getip(SyActivity.this);//ip
        String c = Utilss.getMacAddress();//mac地址
        String d= Utilss.getMacAddr();//mac地址
        int e= IntenetUtil.getNetworkState(SyActivity.this);//网络类型
        //String f= Utilss.getXydpi(SyActivity.this);
        String g= Utilss.getImei(SyActivity.this);//imei
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
    }
    private int TIME = 10000;
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

    TextView info,tuichu;
    TextView textView1,textView2,textView5,textView6;
    private ViewPager pager;
    //每一个界面
    private List<View> views;
    MyPagerAdapter myPagerAdapter;
    private ViewPagerHandlerUtils handlerUtils;
    Button youxi;
    Button tuijian;
    protected void intview(){
        try {
            Class<?> mclass = Class.forName("android.os.SystemProperties");

            Method mmethod = mclass.getMethod("get", new Class[] { String.class,String.class });

            String result =(String) mmethod.invoke(mclass.newInstance(), new Object[] {"gsm.version.baseband", "no message" });

            Log.i("基带版本:", result);

        }catch(Exception e ){
            e.printStackTrace();
        }
        downloadDialog = new DownloadDialog(myActivity, "downing...");
        tuichu=(TextView) findViewById(R.id.tuichu);
        textView1=(TextView) findViewById(R.id.textview1);
        textView2=(TextView) findViewById(R.id.textview2);
        textView5=(TextView) findViewById(R.id.textview5);
        textView6=(TextView) findViewById(R.id.textview6);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);
        tuichu.setOnClickListener(this);
        info=(TextView) findViewById(R.id.info);
        info.setOnClickListener(this);
        youxi=(Button) findViewById(R.id.youxi);
        youxi.setOnClickListener(this);
        tuijian=(Button) findViewById(R.id.tuijian);
        tuijian.setOnClickListener(this);
        pager=(ViewPager) findViewById(R.id.viewpagers);
        views=new ArrayList<View>();
        handlerUtils = new ViewPagerHandlerUtils(pager);
        handlerUtils.sendEmptyMessageDelayed(handlerUtils.MSG_UPDATE, handlerUtils.MSG_DELAY);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == views.size()-1) {
                    // 设置当前值为1
                    position = 0;
                }
                handlerUtils.sendMessage(Message.obtain(handlerUtils, handlerUtils.MSG_PAGE, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handlerUtils.sendEmptyMessage(handlerUtils.MSG_KEEP);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handlerUtils.sendEmptyMessageDelayed(handlerUtils.MSG_UPDATE,
                                handlerUtils.MSG_DELAY);
                }
            }
        });
        gg();
        banner = (Banner)findViewById(R.id.banner1);

        //addBanner();
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
    List<Vesion> vesions=new ArrayList<>();
    protected  void banben(){
        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl+Constants.BanbenUrl;
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
                        ToastUtil.show(SyActivity.this,"网络异常");
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
                                dataJson = jsonObject.optString("select");
                                Type type = new TypeToken<List<Vesion>>(){}.getType();
                                vesions = new Gson().fromJson(dataJson, type);
                                String versioCode = vesions.get(0).version+"";
                                if (!(versioCode + "").equals(Utils.getVersionCode(SyActivity.this))) {
                                    SyActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showUpDateDialog();
                                        }
                                    });
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(SyActivity.this,response.message);
                        }
                    }
                });
    }
    protected void intdata(){
        Map<String, String> params = new HashMap<>();
        String url= Constants.BaseUrl+Constants.LunUrl;
        // String url= "http://60.205.216.178:8080/focuss/focus";
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
                        ToastUtil.show(SyActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            //startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            try {
                                String object = new Gson().toJson(response);
                                JSONObject jsonObject = new JSONObject(object);
                                String dataJson;
                                dataJson = jsonObject.optString("data");
                                Type type = new TypeToken<List<Banners>>(){}.getType();
                                images = new Gson().fromJson(dataJson, type);
                                addBanner();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(SyActivity.this,response.message);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.youxi:
                //startActivity(new Intent(SyActivity.this, MainActivity.class));
                Intent intent1=new Intent(SyActivity.this, MainActivity2.class);
                intent1.putExtra("lat",lat);
                intent1.putExtra("lng",lng);
                startActivity(intent1);
                break;
            case R.id.info:
                startActivity(new Intent(SyActivity.this, InfoActivity.class));
                break;
            case R.id.textview1:
                startActivity(new Intent(SyActivity.this, TqActivity.class));
                //ToastUtil.show(SyActivity.this,"此功能正在开发中，请耐心等待！");
                break;
            case R.id.textview2:
                startActivity(new Intent(SyActivity.this, XwActivity.class));
                break;
            case R.id.textview5:
                startActivity(new Intent(SyActivity.this, WlActivity.class));
                //根据下载的apk地址 获取包名
               /* PackageManager pm = this.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
                ApplicationInfo appInfo = null;
                String appPackageName="";
                if (info != null) {
                    appInfo = info.applicationInfo;
                    appPackageName = appInfo.packageName;//此为apk包名
                }
                try{
                    Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
                    startActivity(intent);
                }catch(Exception e){
                    Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show();
                }*/
                break;
            case R.id.textview6:
                /*try {
                    Intent intent = SyActivity.this.getPackageManager().getLaunchIntentForPackage("com.sina.news");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(SyActivity.this, "没有安装", Toast.LENGTH_LONG).show();
                }*/
                startActivity(new Intent(SyActivity.this, CpActivity.class));
                // startActivity(new Intent(SyActivity.this, TouchTest.class));
               /* Intent intents=new Intent(SyActivity.this, GaoActivity.class);
                intents.putExtra("lat",lat);
                intents.putExtra("lng",lng);
                startActivity(intents);*/
                break;
            case R.id.tuichu:
                //杀掉之前的所有activity*/
                ValueStorage.remove("username");
                ValueStorage.remove("id");
                ValueStorage.remove("islogin");
                ValueStorage.remove("sex");
                Intent intent=new Intent(SyActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.tuijian:
                Toast.makeText(SyActivity.this, "敬请期待", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(SyActivity.this, TjyjActivity.class));
                break;
            default:
                break;
        }
    }

    Banner banner;
    //添加轮播图
    private void addBanner() {
        if (images != null) {
            int size = images.size();
            for (int i = 0; i < size; i++) {
                imagesList.add(images.get(i).CarouselImgUrl);
            }
        }
        // imagesList.add("file:///C:/Users/huang/Desktop/%E6%B7%98%E9%92%B1%E8%9A%81%20APP-UI%E7%95%8C%E9%9D%A2%E8%AE%BE%E8%AE%A1%E7%A8%BF/index%20%E5%88%87%E5%9B%BE/android/drawable-hdpi/icon_binner.png");
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setImages(imagesList);//可以选择设置图片网址，或者资源文件，默认用Glide加载
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitleList(imagesList);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                // Toast.makeText(SyActivity.this, "你点击了：" + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showUpDateDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("发现新版本");
        ab.setMessage("是否去更新");
        ab.setCancelable(false);
        ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ValueStorage.remove("username");
                ValueStorage.remove("id");
                ValueStorage.remove("islogin");
                ValueStorage.remove("sex");
                downloadDialog.showDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downLoadApk(Constants.version_download_apk);
                    }
                }).start();
            }
        });

        // ab.setNegativeButton("否", null);
        ab.create();
        ab.show();
    }
    String path="";
    private DownloadDialog downloadDialog;
    private void downLoadApk(String url) {

        DownloadUtil.get().download(url, Utils.APK_NAME, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SyActivity.this, "downloadComplated", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
                        path=Environment.
                                getExternalStorageDirectory() + "/" + Utils.APK_NAME;
                        Utils.archive_apk(new File(path), SyActivity.this);
                    }
                });
            }

            @Override
            public void onDownloading(final int progress) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadDialog.setTextContent("已完成" + progress + "%");
                    }
                });

                Log.i("HJHJHJK", progress + "");
            }

            @Override
            public void onDownloadFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadDialog.dismiss();
                        Toast.makeText(SyActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        // TCAgent.onPageStart(this,"SyActivity");
    }
    @Override
    protected void onPause() {
        super.onPause();
        //TCAgent.onPageEnd(this,"SyActivity");
    }
    List<Ggitem> list=new ArrayList<>();
    protected  void gg(){
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
                        ToastUtil.show(SyActivity.this,"网络异常");
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
                                LayoutInflater li=LayoutInflater.from(SyActivity.this);
                                for(int i=0;i<list.size();i++){

                                    View view = li.inflate(R.layout.pager_item2, null);
                                    TextView info = (TextView) view.findViewById(R.id.info);
                                    TextView time = (TextView) view.findViewById(R.id.time);
                                    TextView title = (TextView) view.findViewById(R.id.title);
                                    info.setText(list.get(i).bulletindetail);
                                    title.setText(list.get(i).bulletinname);
                                    if (list.get(i).bulletdate == null) {
                                        time.setText("");
                                    } else {
                                        time.setText(TimeUtils.stampToDate(list.get(i).bulletdate).substring(0, 10));
                                    }
                                    view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(SyActivity.this,GgActivity.class));
                                        }
                                    });
                                    views.add(view);
                                    if(list.size()==1){
                                        //一条不滑动
                                    }else {
                                        if (i == list.size() - 1) {
                                            View view2 = li.inflate(R.layout.pager_item, null);
                                            TextView info2 = (TextView) view2.findViewById(R.id.info);
                                            TextView time2 = (TextView) view2.findViewById(R.id.time);
                                            TextView title2 = (TextView) view2.findViewById(R.id.title);
                                            info2.setText(list.get(0).bulletindetail);
                                            title2.setText(list.get(0).bulletinname);
                                            if (list.get(0).bulletdate == null) {
                                                time2.setText("");
                                            } else {
                                                time2.setText(TimeUtils.stampToDate(list.get(0).bulletdate).substring(0, 10));
                                            }
                                            view2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    startActivity(new Intent(SyActivity.this,GgActivity.class));
                                                }
                                            });
                                            views.add(view2);
                                        }
                                    }
                                }
                                myPagerAdapter=new MyPagerAdapter(views);
                                pager.setAdapter(myPagerAdapter);

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(SyActivity.this,response.message);
                        }
                    }
                });
    }
    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.PACKAGE_USAGE_STATS,
                            Manifest.permission.READ_LOGS,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.SET_DEBUG_APP,
                            Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.GET_ACCOUNTS,
                            Manifest.permission.WRITE_APN_SETTINGS},0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    List<String> displayreport=new ArrayList<>();
    //不是红包时展现
    protected  void intdata1(){
        String  Model= android.os.Build.MODEL ;//型号
        String a1 =  android.os.Build.VERSION.SDK ;
        String a2=  android.os.Build.VERSION.RELEASE;//系统版本
        String a3 = android.os.Build.BRAND;//厂商
        String a = Utilss.getMacAddresss();//mac地址
        String b = Utilss.getip(SyActivity.this);//ip
        String c = Utilss.getMacAddress();//mac地址
        String d= Utilss.getMacAddr();//mac地址,.
        int e= IntenetUtil.getNetworkState(SyActivity.this);//网络类型
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

        float xdpi = SyActivity.this.getResources().getDisplayMetrics().xdpi;
        float ydpi =  SyActivity.this.getResources().getDisplayMetrics().ydpi;

        String x=xdpi+"";
        String y=ydpi+"";
        String x1 = x.substring(0, x.lastIndexOf("."));
        String y1 = y.substring(0, y.lastIndexOf("."));
        //String sd=x1+","+y1;
        String g= Utilss.getImei(SyActivity.this);//imei
        String ts= Utilss.getTime();//imei
        WindowManager wm = SyActivity.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        String sss=Utilss.getIsmi(SyActivity.this);
        //ToastUtil.show(SyActivity.this,b);
        Map<String, String> params = new HashMap<>();
        params.put("appid","2893561");//应用ID	测试ID:2151681
        params.put("ts",ts);//	时间戳(13位毫秒级)	如：1513753247587
        params.put("sign",Utilss.MD5(ts+"3030bb7bf11c0872923111eaf649d2cb"));//签名是由时间戳ts加token再md5生成
        params.put("os","1");//系统类型	ios：2;android:1
        params.put("screenwidth",width+"");//屏幕宽度	例如:640
        params.put("screenheight",height+"");//屏幕高度	例如:960
        params.put("imsi",Utilss.getIsmi(SyActivity.this));//运营商识别码	例如:460011418603055
        params.put("ua",Utilss.getua(SyActivity.this));//浏览器的user-agent
        params.put("mac",c);//网卡地址	例如：02:00:2e:00:46:3a
        params.put("network",network);//	网络类型	wifi 2g 3g 4g等
        params.put("sd",sd);//Density independent pixels(dpi)	如：160,240
        params.put("imei",g);//IMEI号	仅安卓机型提供
        params.put("osversion",a2);//系统版本号	如：9.3.3
        params.put("vendor",a3);//手机生产厂商	如：samsung
        params.put("model",Model);//手机型号	如:Galaxy Note8
        params.put("androidid",Utilss.getAndroidID(SyActivity.this));//安卓id	仅安卓机型提供
        params.put("lat",getIntent().getStringExtra("lat"));//	经度
        params.put("lng",getIntent().getStringExtra("lng"));//	纬度
        params.put("ip",b);
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
                        ToastUtil.show(SyActivity.this,"网络异常");
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
                                    ToastUtil.show(SyActivity.this, "获取广告异常");
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
    protected void queding(){
        for(int i=0;i<displayreport.size();i++){
            Map<String, String> params = new HashMap<>();
            String url= displayreport.get(i);
            OkHttpUtils.get()
                    .params(params)
                    .url(url)
                    .build()
                    .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                    {
                        @Override
                        public void onError(Call call, Exception e, int id)
                        {
                            ToastUtil.show(SyActivity.this,"网络异常");
                        }

                        @Override
                        public void onResponse(final BeanResult response, int id)
                        {
                        }
                    });
        }

    }
    protected void indata(String name){
        Map<String, String> params = new HashMap<>();
        String url= "http://192.168.3.248:2323/recommendRewardController/insertRecommendRewardCode";
        //String url= Constants.BaseUrl+ Constants.TuijianUrl;
        params.put("turntableUsername",ValueStorage.getString("username"));//被推荐人
        params.put("codes",name);//推荐人
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
                        ToastUtil.show(SyActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            //ToastUtil.show(SyActivity.this,response.message);
                            if(response.message.equals("推荐码输入错误")){
                                tongzhi3();
                            }

                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(SyActivity.this,response.message);
                        }
                    }
                });
    }
    Dialog dialog;
    protected  void tongzhi3(){
        View view = LayoutInflater.from(SyActivity.this).inflate(
                R.layout.view_alertdialog_four_option2, null);
        ((TextView) view.findViewById(R.id.alertdialog_option_one_title))
                .setText("通知");
       final EditText number=(EditText)view.findViewById(R.id.alertdialog_option_one_content);
        TextView alertdialog_option_two_left = (TextView) view
                .findViewById(R.id.alertdialog_option_two_left);
        alertdialog_option_two_left.setText("否");
        alertdialog_option_two_left.setVisibility(View.GONE);
        TextView alertdialog_option_two_right = (TextView) view
                .findViewById(R.id.alertdialog_option_two_right);
        alertdialog_option_two_right.setText("确定");
        dialog = new Dialog(SyActivity.this, R.style.custom_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        alertdialog_option_two_left
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        alertdialog_option_two_right
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(number.getText().toString().equals("")){
                            ToastUtil.show(SyActivity.this,"邀请码不能为空");
                            return;
                        }
                        if(ValueStorage.getString("username").equals(number.getText().toString())){
                            ToastUtil.show(SyActivity.this,"邀请码不可用");
                        }else {
                            indata1(number.getText().toString());
                        }
                    }
                });
        dialog.show();
    }
    protected void indata1(String name){
        Map<String, String> params = new HashMap<>();
        String url= "http://192.168.3.248:2323/recommendRewardController/insertRecommendRewardCode";
        //String url= Constants.BaseUrl+ Constants.TuijianUrl;
        params.put("turntableUsername",ValueStorage.getString("username"));//被推荐人
        params.put("codes",name);//推荐人
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
                        ToastUtil.show(SyActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if (response.code.equals("200")) {
                            dismissLoadingDialog();
                            if(response.message.equals("推荐码输入错误")){
                                ToastUtil.show(SyActivity.this,response.message);
                            }else if(response.message.equals("已经填写过推荐码")){
                                ToastUtil.show(SyActivity.this,response.message);
                            }else if(response.message.equals("此用户没有推荐资格！")){
                                ToastUtil.show(SyActivity.this,response.message);
                            }else if(response.message.equals("推荐码失效")){
                                ToastUtil.show(SyActivity.this,response.message);
                            }else{
                                dialog.dismiss();
                                ToastUtil.show(SyActivity.this,response.message);
                            }
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(SyActivity.this,response.message);
                        }
                    }
                });
    }
}
