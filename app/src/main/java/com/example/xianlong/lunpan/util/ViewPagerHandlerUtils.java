package com.example.xianlong.lunpan.util;

/**
 * Created by huang on 2017/12/12.
 */


import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

public class ViewPagerHandlerUtils extends Handler {

    /**
     * 请求更新显示的ImageView。
     */
    public static final int MSG_UPDATE = 1;
    /**
     * 请求暂停轮播。
     */
    public static final int MSG_KEEP = 2;
    /**
     * 请求恢复轮播。
     */
    public static final int MSG_BREAK = 3;
    /**
     * 记录最新的页号
     */
    public static final int MSG_PAGE = 4;

    //轮播间隔时间
    public static final long MSG_DELAY = 3000;

    private ViewPager mViewPager;
    private int currentItem = 0;

    public ViewPagerHandlerUtils(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mViewPager == null) {
            return;
        }
        if (hasMessages(MSG_UPDATE)) {
            removeMessages(MSG_UPDATE);
        }
        switch (msg.what) {
            case MSG_UPDATE:
                currentItem++;
                mViewPager.setCurrentItem(currentItem);
                sendEmptyMessageDelayed(MSG_UPDATE, MSG_DELAY);
                break;
            case MSG_KEEP:
                break;
            case MSG_BREAK:
                sendEmptyMessageDelayed(MSG_UPDATE, MSG_DELAY);
                break;
            case MSG_PAGE:
                currentItem = msg.arg1;
                break;
            default:
                sendEmptyMessageDelayed(MSG_UPDATE, MSG_DELAY);
                break;
        }
    }
}