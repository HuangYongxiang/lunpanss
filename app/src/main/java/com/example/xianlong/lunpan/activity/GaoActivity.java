package com.example.xianlong.lunpan.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xianlong.lunpan.MainActivity;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.adapter.GgAdapter;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.bean.Ggitem;
import com.example.xianlong.lunpan.bean.Guanggao;
import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.dialog.DownloadDialog;
import com.example.xianlong.lunpan.dialog.DownloadUtil;
import com.example.xianlong.lunpan.util.IntenetUtil;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.Utils;
import com.example.xianlong.lunpan.util.Utilss;
import com.example.xianlong.lunpan.util.ValueStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.GenericsCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class GaoActivity extends BaseActivity {
    static final int PHONE_REQUEST_CODE = 66;
    ListView listview;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guanggao);
        context=this;
        intview();
        intdata();
    }
    String uri="";
    ImageView image;
    List<Ggitem> list=new ArrayList<>();
    protected void intview(){
        downloadDialog = new DownloadDialog(myActivity, "downing...");
        image=(ImageView)findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(browserIntent);
               /* if(isurl){
                    AlertDialog.Builder ab = new AlertDialog.Builder(GaoActivity.this);
                    ab.setTitle("发现广告");
                    ab.setMessage("是否去下载");
                    ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            downloadDialog.showDialog();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    downLoadApk(uri);
                                }
                            }).start();
                        }
                    });

                    ab.setNegativeButton("否", null);
                    ab.create();
                    ab.show();
                }else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(browserIntent);
                }*/
            }
        });
    }
    List<String> displayreport=new ArrayList<>();
    List<String> clickreport=new ArrayList<>();
   boolean isurl=false;//判断是否为下载
    protected  void intdata(){
        String  Model= android.os.Build.MODEL ;//型号
        String a1 =  android.os.Build.VERSION.SDK ;
        String a2=  android.os.Build.VERSION.RELEASE;//系统版本
        String a3 = android.os.Build.BRAND;//厂商
        String a = Utilss.getMacAddresss();//mac地址
        String b = Utilss.getip(GaoActivity.this);//ip
        String c = Utilss.getMacAddress();//mac地址
        String d= Utilss.getMacAddr();//mac地址
        int e= IntenetUtil.getNetworkState(GaoActivity.this);//网络类型
        String network=e+"";
        if(e==1){
            network="wifi";
        }else{
            network=e+"g";
        }
        float xdpi = context.getResources().getDisplayMetrics().xdpi;
        float ydpi = context.getResources().getDisplayMetrics().ydpi;

        String x=xdpi+"";
        String y=ydpi+"";
        String x1 = x.substring(0, x.lastIndexOf("."));
        String y1 = y.substring(0, y.lastIndexOf("."));
        String sd=x1+","+y1;
        String g= Utilss.getImei(GaoActivity.this);//imei
        String ts= Utilss.getTime();//imei
        WindowManager wm = GaoActivity.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Map<String, String> params = new HashMap<>();
        params.put("appid","2893561");//应用ID	测试ID:2151681
        params.put("ts",ts);//	时间戳(13位毫秒级)	如：1513753247587
        params.put("sign",Utilss.MD5(ts+"3030bb7bf11c0872923111eaf649d2cb"));//签名是由时间戳ts加token再md5生成
        params.put("os","1");//系统类型	ios：2;android:1
        params.put("screenwidth",width+"");//屏幕宽度	例如:640
        params.put("screenheight",height+"");//屏幕高度	例如:960
        params.put("imsi",Utilss.getIsmi(GaoActivity.this));//运营商识别码	例如:460011418603055
        params.put("ua",Utilss.getua(GaoActivity.this));//浏览器的user-agent
        params.put("mac",c);//网卡地址	例如：02:00:2e:00:46:3a
        params.put("network",network);//	网络类型	wifi 2g 3g 4g等
        params.put("sd",sd);//Density independent pixels(dpi)	如：160,240
        params.put("imei",g);//IMEI号	仅安卓机型提供
        params.put("osversion",a2);//系统版本号	如：9.3.3
        params.put("vendor",a3);//手机生产厂商	如：samsung
        params.put("model",Model);//手机型号	如:Galaxy Note8
        params.put("androidid",Utilss.getAndroidID(GaoActivity.this));//安卓id	仅安卓机型提供
        params.put("lat",getIntent().getStringExtra("lat"));//	经度
        params.put("lng",getIntent().getStringExtra("lng"));//	纬度
        params.put("ip",b);
        String url= "http://116.62.77.111/public/getCommonInformationAd.shtml";
         //String url= "http://116.62.77.111/public/getCommonStartUpAd.shtml";
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
                        ToastUtil.show(GaoActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        if(response==null){
                            dismissLoadingDialog();
                            //获取广告失败
                            ToastUtil.show(GaoActivity.this,"获取广告失败");
                        }else {
                            if (response.state == null) {
                                dismissLoadingDialog();
                                //成功
                                displayreport=response.displayreport;
                                queding();
                                clickreport=response.clickreport;
                                isurl = response.json;
                                if (response.json) {
                                    intdatas(response.url);
                                } else {
                                    uri = response.url;
                                }
                                Glide.with(GaoActivity.this).load(response.imageUrl)
                                        .placeholder(R.drawable.btn_setting2).crossFade().error(R.drawable.btn_setting2).into(image);

                            } else {
                                if (response.state.equals("500")) {
                                    dismissLoadingDialog();
                                    //获取广告异常
                                    //{"data":null,"state":"500","message":"获取广告异常"}
                                    ToastUtil.show(GaoActivity.this, "获取广告异常");
                                } else {
                                    dismissLoadingDialog();
                                    //成功
                                    displayreport=response.displayreport;
                                    queding();
                                    clickreport=response.clickreport;
                                    isurl = response.json;
                                    if (response.json) {
                                        intdatas(response.url);
                                    } else {
                                        uri = response.url;
                                    }
                                    Glide.with(GaoActivity.this).load(response.imageUrl)
                                            .placeholder(R.drawable.btn_setting2).crossFade().error(R.drawable.btn_setting2).into(image);
                                }
                            }
                        }
                    }
                });
    }
    protected  void intdatas(String urlss){

        Map<String, String> params = new HashMap<>();
        String url= urlss;
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
                        ToastUtil.show(GaoActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final BeanResult response, int id)
                    {
                        dismissLoadingDialog();
                        try {
                            String object = new Gson().toJson(response);
                            JSONObject jsonObject = new JSONObject(object);
                            String dataJson;
                            dataJson = jsonObject.optString("data");
                            Type type = new TypeToken<Guanggao>(){}.getType();
                            Guanggao gao = new Gson().fromJson(dataJson, type);
                            uri= gao.dstlink;
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
    }
    protected void queding(){
        /*for(int i=0;i<displayreport.size();i++){
           final int b=i;
            Map<String, String> params = new HashMap<>();
            String url= displayreport.get(i);
            OkHttpUtils.get()
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
                            int a=b;
                            ToastUtil.show(GaoActivity.this,"网络异常");
                        }

                        @Override
                        public void onResponse(final BeanResult response, int id)
                        {
                        }
                    });
        }*/
        Map<String, String> params = new HashMap<>();
        String url= "http://sspapi.fangtingzfc.com/v1/track.php?data=bWlkPTExMDY3MjgzNDAmYWRpZD1mMWU5NzQwYjI5ZjczNDM4Y2Q4NDQ5NWMwNmZhOTM0OCZ0eXBlPTAmc3R1ZmZpZD02MDUwMjMzMDc1NjMxMzcyJm1vYmlsZUluZm89eyJhZGNvdW50IjoxLCJhaWQiOiI3Yzc1YmNlMzcwNzVjN2JmIiwiYXBwaWQiOiIxMDEwODE4NDA1NzAiLCJhc3ZlciI6IjcuMS4xIiwiYmlkIjoiY24uenVvY2FpdmlkZW8iLCJicmFuZCI6InZpdm8iLCJjbyI6IkNOIiwiZHBpIjoiNTM3LDUzNyIsImlkZmEiOiIiLCJpbWVpIjoiODYyNjY4MDM1MTI4MTczIiwiaW1zaSI6IjAiLCJpc3AiOiIwIiwibGFuZyI6InpoIiwibWFjIjoiOUM6RkI6RDU6QUM6MTc6MzkiLCJtb2RlbCI6InZpdm8gWHBsYXk2IiwibmV0IjoiMSIsIm9yIjoiMSIsIm9zIjoiYW5kcm9pZCIsInBvc2lkIjoiMTUxMDEwODE4NDA1NzA5NDcyOCIsInJlbW90ZWlwIjoiMTE1LjE3MS4xMTIuMTIwIiwic2giOiIxOTIwIiwic2lnbiI6IjI4Y2EwNzFiODljM2NiODJmMDAzMWRhMzZjMjU4MGU3Iiwic3ciOiIxMDgwIiwicF9wbGF0Zm9ybV9pZCI6IjE3IiwicF9hcHBpZCI6IjExMDY3MjgzNDAiLCJwX3Bvc2lkIjoiNjA1MDIzMzA3NTYzMTM3MiIsInBfcGtnbmFtZSI6ImNuLnp1b2NhaXZpZGVvIiwiYWRfdHlwZV9pZCI6IjE1IiwiY2tleSI6IjEwMTA4MTg0MDU3MCIsImNrZXkyIjoiNDA1NzAiLCJwcGkiOiIxMDgweDE5MjAiLCJpcCI6IjExOC4zMS4xNjcuMTg1IiwidWEiOiJHby1odHRwLWNsaWVudFwvMS4xIiwicG9zIjp7ImlkIjoiMTUxMDEwODE4NDA1NzA5NDcyOCIsInciOiIxMjAwIiwiaCI6IjcyMCJ9LCJhZGRkYXRlIjoiMjAxODAyMjciLCJhZGR0aW1lIjoxNTE5NzA4MDM2LCJhcGl2ZXIiOiJzc3BfdjEiLCJkaXJlY3RvcnkiOiJzc3BfdjEifQ%3D%3D&sign=e10ab31fc0f5bc2cb08e5a78eab8f6c8";
        OkHttpUtils.get()
                .url(url)
                .build()
                .connTimeOut(800000)
                .readTimeOut(800000)
                .writeTimeOut(800000)
                .execute(new GenericsCallback<String>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        //int a=b;
                        ToastUtil.show(GaoActivity.this,"网络异常");
                    }

                    @Override
                    public void onResponse(final String response, int id)
                    {
                    }
                });
    }
    protected void dianji1(){
        for(int i=0;i<clickreport.size();i++){
            Map<String, String> params = new HashMap<>();
            String url= clickreport.get(i);
            OkHttpUtils.post()
                    .params(params)
                    .url(url)
                    .build()
                    .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                    {
                        @Override
                        public void onError(Call call, Exception e, int id)
                        {
                            ToastUtil.show(GaoActivity.this,"网络异常");
                        }

                        @Override
                        public void onResponse(final BeanResult response, int id)
                        {
                        }
                    });
        }
    }
    String path="";
    private DownloadDialog downloadDialog;
    private void downLoadApk(String url) {

        DownloadUtil.get().download(url, "guangao.apk", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GaoActivity.this, "downloadComplated", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
                        path= Environment.
                                getExternalStorageDirectory() + "/" + "guangao.apk";
                        Utils.archive_apk(new File(path), GaoActivity.this);
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
                        Toast.makeText(GaoActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
