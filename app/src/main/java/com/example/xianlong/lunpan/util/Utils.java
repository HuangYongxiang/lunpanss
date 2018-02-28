package com.example.xianlong.lunpan.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.example.xianlong.lunpan.util.ToastUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by job on 2017/9/5.
 */
public class Utils {


    public static String APPSDKKEY = "9c722699e93cdc860006ce5947d4acf2";
    public static String DATA_FOMATE = "yyyyMMddHHmmss";
    public static String APK_NAME = "lunpan.apk";

    //和js交互的三种弹窗的类型
    public static String JS_ALERT_FLAG_ADVERT = "js://guanggao";
    public static String JS_START_OTHER_APP = "js://start";
    public static String JS_YOU_MI_SDK = "js://webview";
    //测试imei
    public static String TEST_IMEI = "ebe596017db2f8c69136e5d6e594d365";


    public static final int REQUEST_READ_PHONE_STATE = 0;

    public static String getImei(Context context) {
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String a= mTm.getDeviceId();
        return mTm.getDeviceId();
    }

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取版本号
     * {"version":2}
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String versionCode = info.versionCode + "";
            return versionCode;
        } catch (Exception e) {
            return "1.0";
        }
    }


    /**
     * 获取版本号
     * {"version":2}
     *
     * @return 当前应用的版本号
     */
    public static String getVersionCode(Activity context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version+"";
        } catch (Exception e) {
            return "1.0";
        }
    }


    /**
     * 获取包名
     *
     * @return 当前应用包名
     */
    public static String getPackageName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String packageName = info.packageName;
            return packageName;
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 返回一个随机数（广告的）
     *
     * @param totalAdvertNum
     * @return
     */
    public static int getRandomNum(int totalAdvertNum) {
        Random random = new Random();
        return random.nextInt(totalAdvertNum);
    }


    /**
     * 执行具体的静默安装逻辑，需要手机ROOT。
     *
     * @param apkPath 要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public static boolean install(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }


    public static void init(final String path) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //下载
//                    String path = "http://192.168.0.20:8080/game.apk";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("get");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        File file = new File("sdcard/apk/game.apk");

                        if (!file.exists()) {
                            file.mkdirs();
                        }


                        FileOutputStream fos = new FileOutputStream(file);
                        int len = -1;
                        byte[] buffer = new byte[1024];
                        while ((len = is.read()) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        fos.close();
                    }

                    install("sdcard/apk/game.apk");
//                    //偷偷安装
//                    RootTools.sendShell("pm install sdcard/apk/game.apk", 5000);
//                    System.out.println("下载完毕");
//                    //偷偷启动
//                    RootTools.sendShell("start -n com.android.game/com.android.game.GameUI", 5000);
//                    System.out.println("启动完毕");
//                    //RootTools.sendShell("am kill com.android.game", 5000);
//                    //偷偷卸载
//                    RootTools.sendShell("pm uninstall com.android.game", 5000);
//                    System.out.println("卸载完毕");
//                    //偷偷删除
//                    RootTools.sendShell("rm sdcard/apk/gameview.apk", 5000);
//                    System.out.println("删除完毕");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void startFeiXingState(ContentResolver cr, Context context, boolean enabling) {

        Settings.Global.putInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, enabling ? 1 : 0);


//        Settings.System.putString(cr, Settings.System.AIRPLANE_MODE_ON, "1");
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", enabling);
        context.sendBroadcast(intent);
    }


    public static void closeFeiXingState(ContentResolver cr, Context context, boolean enabling) {
        Settings.Global.putInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, enabling ? 1 : 0);


//        Settings.System.putString(cr, Settings.System.AIRPLANE_MODE_ON, "1");
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", enabling);
        context.sendBroadcast(intent);
    }


    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 保存绑定状态
     *
     * @param state
     * @param context
     */
    public static void saveBindState(boolean state, Context context) {
        SharedPreferences sp = context.getSharedPreferences("bind", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("bind_data", state);
        editor.commit();
    }

    /**
     * 获取绑定状态
     *
     * @param context
     * @return
     */
    public static boolean getBindState(Context context) {
        SharedPreferences sp = context.getSharedPreferences("bind", Context.MODE_PRIVATE);
        return sp.getBoolean("bind_data", false);
    }

    /**
     * 跳转到安装界面
     *
     * @param file
     * @param context
     */
    public static void archive_apk(File file, Context context) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT<24){
            //7.0这里会闪退，imgfile是图片文件路径
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri,
                    "application/vnd.android.package-archive");
        }else{
            Uri uri= FileProvider.getUriForFile(context,"com.chc.photo.fileprovider",file);
            intent.setDataAndType(uri,
                    "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION );//添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        context.startActivity(intent);
    }


   /* public static void setAirPlaneMode(Context context, boolean enable, int value) {
      *//*  Settings.System.putInt(context.getContentResolver(), "State", enable ? 1 : 0);
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("stat", enable);
        context.sendBroadcast(intent);*//* //注释部分为 4.2版本之前的做法，现在已经失效。

        //对版本做出判断
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //if less than verson 4.2
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, value);
        } else {
            Settings.Global.putInt(
                    context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, value);
        }
        // broadcast an intent to inform
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", !enable);
        context.sendBroadcast(intent);

    }

    public static boolean isAirPlaneOn(Context context) {
       *//* int isEnable = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
        return (isEnable==1)?true:false;*//*
        //注释部分，是4.2版本之前的做法，现在已经失效。

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //android version lower than 4.2
            return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;

        } else {
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }*/


    private final static String COMMAND_AIRPLANE_ON = "settings put global airplane_mode_on 1 \n " +
            "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n ";
    private final static String COMMAND_AIRPLANE_OFF = "settings put global airplane_mode_on 0 \n" +
            " am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n ";
    private final static String COMMAND_SU = "su";


    //判断飞行模式开关
    public static boolean isAirplaneModeOn(Context context) {
        //4.2以下
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else //4.2或4.2以上
        {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    //设置飞行模式
    public static void setAirplaneModeOn(boolean isEnable, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, isEnable ? 1 : 0);
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", isEnable);
            context.sendBroadcast(intent);
        } else //4.2或4.2以上
        {

            if (isEnable)
                execShell(COMMAND_AIRPLANE_ON);
//                writeCmd(COMMAND_AIRPLANE_ON);
            else
                execShell(COMMAND_AIRPLANE_OFF);
//                writeCmd(COMMAND_AIRPLANE_OFF);
        }

    }

    //写入shell命令
    public static void writeCmd(String command) {
        try {
            Process su = Runtime.getRuntime().exec(COMMAND_SU);
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            outputStream.writeBytes(command);
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void execShell(String cmd) {

        try {

            //权限设置

            Process p = Runtime.getRuntime().exec("su");

            //获取输出流

            OutputStream
                    outputStream = p.getOutputStream();

            DataOutputStream
                    dataOutputStream = new

                    DataOutputStream(outputStream);

            //将命令写入

            dataOutputStream.writeBytes(cmd);

            //提交命令

            dataOutputStream.flush();

            //关闭流操作

            dataOutputStream.close();

            outputStream.close();

        } catch (Throwable
                t)

        {

            t.printStackTrace();

        }

    }

    /**
     * 通过包名启动app
     *
     * @param appPackageName
     * @param context
     */
    public static boolean startAPP(String appPackageName, Context context) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(context, "没有安装", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    /**该方法用于判断该应用是否有安装到手机
     *return true  有安装
     *false 没有安装
     * pName  应用包名 例如 com.zhang.mm
     */
    public static boolean chackApkExist(String appPackageName, Context context){
        if(appPackageName==null || "".equals(appPackageName)){
            return false;
        }else{
            try {
                //这句的意思是获取有可能被系统删除的但是还在应用程序列表中保留的appname.
                //因为有些apk可能被删掉 但是数据还在 所以用GET_UNINSTALLED_PACKAGES 这个flag
                context.getPackageManager().
                        getApplicationInfo(appPackageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    /**
     * 获取手机是否root信息
     *
     * @return
     */
    public static String isRoot() {
        String bool = "Root:false";
        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = "Root:false";
            } else {
                bool = "Root:true";
            }
        } catch (Exception e) {
        }
        return bool;
    }


    /**
     * 判断用户是否完成了任务
     *
     * @param userPlayTime
     * @param userNeedPlayTime
     * @return
     */
    public static boolean isUserAlreadyComplated(int userPlayTime, int userNeedPlayTime) {
        if (userPlayTime / 60 >= userNeedPlayTime) {
            return true;
        }
        return false;
    }


    /**
     * 获取ip
     *
     * @param context
     * @return
     */
    public static String getip(Context context) {
        String ip="";
        boolean wifi =isWiFiActive(context);
        boolean Network = isNetworkAvailable(context);
        if (wifi == true) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        } else {
            //当前网络不是WIFI
            if(Network==true){
                ip = getLocalIpAddress();
            }else{
                //当前未开启网络服务
                ToastUtil.show(context,"请开启wifi或者无线网络");
            }
        }
        return ip;
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
    public static String getLocalIpAddress()
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
    public static String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }
}
