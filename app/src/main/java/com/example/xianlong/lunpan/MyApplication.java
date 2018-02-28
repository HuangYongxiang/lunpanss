package com.example.xianlong.lunpan;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.example.xianlong.lunpan.util.GlideImageLoaderImagePicker;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.tendcloud.tenddata.TCAgent;

/**
 * APPLICATION
 */
public class MyApplication extends Application {
    public static String currentUserNick = "";
    private static MyApplication baseApplication;
    private int maxImgCount = 9;
    public static Context applicationContext;
    private static MyApplication instance;
    @Override
    public void onCreate() {
       // SpeechUtility.createUtility(MyApplication.this, "appid=" + getString(R.string.app_id));

        super.onCreate();
        applicationContext = this;
        instance = this;
        baseApplication = this;
       // DRAgent.getInstance().init(this, "528ab6a566564a96a0b7646586f3c017", true);
        TCAgent.LOG_ON=true;
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
       // TCAgent.init(this, "您的 App ID", "渠道 ID");
        TCAgent.init(this);
        // 如果已经在AndroidManifest.xml配置了App ID和渠道ID，调用TCAgent.init(this)即可；或与AndroidManifest.xml中的对应参数保持一致。
        TCAgent.setReportUncaughtExceptions(true);

       initImagePicker();
    }
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoaderImagePicker());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setMultiMode(false);                      //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(1000);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1250);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }


    public static Context getAppContext() {
        return baseApplication;
    }
    public static Resources getAppResources() {
        return baseApplication.getResources();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 分包
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
