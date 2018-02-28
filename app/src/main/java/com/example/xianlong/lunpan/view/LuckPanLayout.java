package com.example.xianlong.lunpan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.xianlong.lunpan.R;
import com.example.xianlong.lunpan.util.Logger;
import com.example.xianlong.lunpan.util.Util;

public class LuckPanLayout extends RelativeLayout {

    private Context context;
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint backgroundPaints = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int radius;
    private int radiuss;
    private int CircleX,CircleY;
    private int CircleXs,CircleYs;
    private Canvas canvas;
    private boolean isYellow = false;
    private int delayTime = 500;
    private RotatePan rotatePan;
    private ImageView startBtn;

    private int screenWidth,screeHeight;
    private int MinValue;
    /**
     * LuckPan 中间对应的Button必须设置tag为 startbtn.
     */
    private static final String START_BTN_TAG = "startbtn";
    public static final int DEFAULT_TIME_PERIOD = 500;


    public LuckPanLayout(Context context) {
        this(context,null);
    }

    public LuckPanLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LuckPanLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        Logger.setDebug(true);
        //backgroundPaint.setColor(Color.rgb(255,92,93));
        backgroundPaint.setColor(Color.rgb(199,4,214));
        backgroundPaints.setColor(Color.rgb(255,234,0));
        whitePaint.setColor(getResources().getColor(R.color.qyellow));
        yellowPaint.setColor(getResources().getColor(R.color.syellow));
        screeHeight = getResources().getDisplayMetrics().heightPixels;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        startLuckLight();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MinValue = Math.min(screenWidth,screeHeight);
        MinValue -= Util.dip2px(context,10)*2;
        Logger.getLogger().d("screenWidth = "+screenWidth + "screenHeight = "+screeHeight + "MinValue = "+MinValue);
        setMeasuredDimension(MinValue,MinValue);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int MinValue = Math.min(width,height);

        radius = MinValue /2;
        CircleX = getWidth() /2;
        CircleY = getHeight() /2;
        radiuss = (MinValue-100) /2;
        CircleXs =getWidth()/2;
        CircleYs = getHeight()/2;

        canvas.drawCircle(CircleX,CircleY,radius,backgroundPaint);
        canvas.drawCircle(CircleXs,CircleYs,radiuss,backgroundPaints);

        drawSmallCircle(isYellow);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int centerX = (right - left)/2;
        int centerY = (bottom - top)/2;
        boolean panReady = false;
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            if(child instanceof RotatePan){
                rotatePan = (RotatePan) child;
                 int panWidth = child.getWidth();
                 int panHeight = child.getHeight();
                 child.layout(centerX - panWidth/2 , centerY - panHeight/2,centerX + panWidth/2 , centerY + panHeight/2);
                 panReady = true;
            }else if(child instanceof ImageView){
                if(TextUtils.equals((String) child.getTag(),START_BTN_TAG)){
                    startBtn = (ImageView) child;
                    int btnWidth = child.getWidth();
                    int btnHeight = child.getHeight();
                    child.layout(centerX - btnWidth/2 , centerY - btnHeight/2 , centerX + btnWidth/2, centerY + btnHeight/2 );
                }
            }
        }

        if(!panReady) throw new RuntimeException("Have you add RotatePan in LuckPanLayout element ?");
    }

    private void drawSmallCircle(boolean FirstYellow){
        int pointDistance = radius - Util.dip2px(context,10);
        for(int i=0;i<=360;i+=20){
            int x = (int) (pointDistance * Math.sin(Util.change(i))) + CircleX;
            int y = (int) (pointDistance * Math.cos(Util.change(i))) + CircleY;

            if(FirstYellow)
                canvas.drawCircle(x,y,Util.dip2px(context,4),yellowPaint);
            else
                canvas.drawCircle(x,y,Util.dip2px(context,4),whitePaint);
            FirstYellow = !FirstYellow;
        }
    }


    /**
     * 开始旋转
     * @param pos  转到指定的转盘，-1 则随机
     * @param delayTime  外围灯光闪烁的间隔时间
     */
    public void rotate(int pos,int delayTime){
        rotatePan.startRotate(pos);
        setDelayTime(delayTime);
        setStartBtnEnable(false);
    }

    protected void setStartBtnEnable(boolean enable){
        if(startBtn != null)
            startBtn.setEnabled(enable);
        else throw new RuntimeException("Have you add start button in LuckPanLayout element ?");
    }

    private void startLuckLight(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isYellow = !isYellow;
                invalidate();
                postDelayed(this,delayTime);
            }
        },delayTime);
    }


    protected void setDelayTime(int delayTime){
        this.delayTime = delayTime;
    }

    public interface AnimationEndListener{
        void endAnimation(int position);
    }

    private AnimationEndListener l;

    public void setAnimationEndListener(AnimationEndListener l){
        this.l = l;
    }

    public AnimationEndListener getAnimationEndListener(){
        return l;
    }

}
