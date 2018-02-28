package com.example.xianlong.lunpan.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xianlong.lunpan.bean.JsonGenericsSerializator;
import com.example.xianlong.lunpan.bean.Users;
import com.example.xianlong.lunpan.context.Constants;
import com.example.xianlong.lunpan.util.ValueStorage;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.GenericsCallback;
import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.bean.BeanResult;
import com.example.xianlong.lunpan.util.ToastUtil;
import com.example.xianlong.lunpan.util.Utils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

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

public class LoginActivity extends BaseActivity {
    private Button login;
    CheckBox checkBox;
    private Context context;
    static final int PHONE_REQUEST_CODE = 66;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=this;
        requestPermissions();
        if(ValueStorage.getString("islogin")!=null){
            startActivity(new Intent(LoginActivity.this,SyActivity.class));
            finish();
        }
        intview();
    }
    boolean wifi;
    boolean Network;
    String ip="";
    String tel="";
    String imei="";
    String deviceid;
    String hostip;
    protected void adress(){
        wifi = LoginActivity.isWiFiActive(LoginActivity.this);
        Network = LoginActivity.isNetworkAvailable(LoginActivity.this);
        if (wifi == true) {
            WifiManager wifiManager = (WifiManager) myActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
            hostip = wifiInfo.getMacAddress();
        } else {
            //当前网络不是WIFI
            if(Network==true){
                ip = getLocalIpAddress();
            }else{
                //当前未开启网络服务
                ToastUtil.show(myActivity,"请开启wifi或者无线网络");
            }
        }
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        deviceid = tm.getDeviceId();
        tel = tm.getLine1Number();//手机号码
        String a = ip;//ip
        String imei = tm.getSimSerialNumber();

        String imsi = tm.getSubscriberId();
    }
    private String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
    /*
     *判断WIFI是否可用
     */
    public static boolean isWiFiActive(Context inContext) {
        Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI")
                            && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /*
 * 判断网络是否可用
 */
    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    EditText name;
    EditText password;
    TextView xieyi;
    protected void intview(){
        login = (Button) findViewById(R.id.login);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login();
                if(name.getText().toString().equals("")){
                    ToastUtil.show(LoginActivity.this,"用户名不能为空");
                    return;
                }else if(password.getText().toString().equals("")){
                    ToastUtil.show(LoginActivity.this,"密码不能为空");
                    return;
                }else{
                    //logins();
                    login();
                }

            }
        });
    }
    public String getLocalIpAddress()
    {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.toString();
        }
        return null;
    }
    protected  void login(){
        if(deviceid==null){
            ToastUtil.show(myActivity,"请开启权限");
            requestPermissions();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("turntableUsername",name.getText().toString());
        params.put("turntableUserPassword",password.getText().toString());
       // params.put("userApp.imei", Utils.getImei(LoginActivity.this));*/
        /*params.put("turntableUsername","a123456");
        params.put("turntableUserPassword","123456");*/
        String url= Constants.BaseUrl+Constants.LoginUrl;
        showLoadingDialog("请求中....");
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .build()
                .connTimeOut(80000)
                .writeTimeOut(80000)
                .readTimeOut(80000)
                .execute(new GenericsCallback<BeanResult>(new JsonGenericsSerializator())
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        dismissLoadingDialog();
                        ToastUtil.show(LoginActivity.this,"网络异常");
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
                                Type type = new TypeToken<List<Users>>(){}.getType();
                                user = new Gson().fromJson(dataJson, type);
                                ValueStorage.put("id",user.get(0).turntableUserId);
                                ValueStorage.put("sex",user.get(0).turntableUserSex);
                                ValueStorage.put("islogin","1");
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            ValueStorage.put("username",name.getText().toString().trim());
                            ValueStorage.put("password",password.getText().toString().trim());
                            /*ValueStorage.put("username","a123456");
                            ValueStorage.put("password","123456");*/
                            startActivity(new Intent(LoginActivity.this,SyActivity.class));
                            finish();
                        }else{
                            dismissLoadingDialog();
                            ToastUtil.show(LoginActivity.this,response.msg);
                        }
                    }
                });
    }
    List<Users> user=new ArrayList<>();
    /**
     * 请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PHONE_REQUEST_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) ) {
                adress();
            } else {
                Toast.makeText(context, "已拒绝权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPermissions() {
        //判断是否开启权限
        if ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            //判断是否开启权限
            adress();
        } else {
            //请求获取权限
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.PACKAGE_USAGE_STATS,}, PHONE_REQUEST_CODE);
        }

    }
}
