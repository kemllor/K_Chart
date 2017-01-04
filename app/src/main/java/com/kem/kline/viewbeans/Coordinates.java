package com.kem.kline.viewbeans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.kem.kline.utils.UnitUtils;


/**
 * 描述：坐标系背景
 *
 * @author zhaoyi
 * @date 2016-11-7
 */
public class Coordinates extends ViewContainer {
    private Context context = null;
    //左边文字画笔
    private Paint leftTextPaint = null;
    //右边文字画笔
    private Paint rightTextPaint = null;
    //底部文字画笔
    private Paint bottomTextPaint = null;
    //经线画笔
    private Paint longitudeLinePaint = null;
    //纬线画笔
    private Paint latitudeLinePaint = null;
    //左边刻度数值
    private String[] leftScaleTextArray = {};
    //右边刻度数值
    private String[] rightScaleTextArray = {};
    //底下刻度数值
    private String[] bottomScaleTextArray = {};
    //距边距的空隙值
    private float space = 0;
    //坐标系底部空余
    public static final int MARGIN_BOTTOM = 30;
    //坐标系顶部空余
    public static final int MARGIN_TOP = 20;

    /**
     * 坐标系
     *
     * @param leftScaleTextArray   左边刻度文字数组
     * @param rightScaleTextArray  右侧刻度文字数组
     * @param bottomScaleTextArray 底部刻度文字数组
     */
    public Coordinates(Context context, String[] leftScaleTextArray, String[] rightScaleTextArray, String[] bottomScaleTextArray) {
        this.context = context;
        this.leftScaleTextArray = leftScaleTextArray;
        this.rightScaleTextArray = rightScaleTextArray;
        this.bottomScaleTextArray = bottomScaleTextArray;
        //初始化画笔
        initPaint();
    }

    /**
     * 坐标系
     */
    public Coordinates(Context context) {
        this.context = context;
        //初始化画笔
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //初始化经线画笔
        this.longitudeLinePaint = new Paint();
        longitudeLinePaint.setStyle(Paint.Style.FILL);
        longitudeLinePaint.setColor(Color.parseColor("#999999"));
        longitudeLinePaint.setStrokeWidth(1f);
        //初始化纬线画笔
        this.latitudeLinePaint = new Paint();
        latitudeLinePaint.setStyle(Paint.Style.FILL);
        latitudeLinePaint.setColor(Color.parseColor("#999999"));
        latitudeLinePaint.setStrokeWidth(1f);
        //初始化左边文字画笔
        this.leftTextPaint = new Paint();
        leftTextPaint.setTextSize(UnitUtils.spToPx(context,8));
        leftTextPaint.setAntiAlias(true);
        leftTextPaint.setColor(Color.GRAY);
        //初始化右边文字画笔
        this.rightTextPaint = new Paint();
        rightTextPaint.setTextSize(UnitUtils.spToPx(context,8));
        rightTextPaint.setAntiAlias(true);
        rightTextPaint.setColor(Color.GRAY);
        //初始化底部文字画笔
        this.bottomTextPaint = new Paint();
        bottomTextPaint.setTextSize(UnitUtils.spToPx(context,8));
        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setColor(Color.GRAY);

        //初始化空隙,该空隙用于文字与边距和纬线之间的距离
        space = UnitUtils.spToPx(context,8);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            if (isShow) {
                checkParamter();
                //画经线,及其刻度
                drawLongitude(canvas);
                //画纬线,及其刻度
                drawLatitude(canvas);
            }
        } catch (Exception e) {
        }
    }

    //画经线
    private void drawLongitude(Canvas canvas) {
        //经线总条数
        int longitudeNums = bottomScaleTextArray.length;
        //经线间的宽度
        float longitudeSpace = coordinateWidth / (longitudeNums - 1);
        Paint.FontMetrics fm = new Paint.FontMetrics();
        for (int i = 0; i < longitudeNums; i++) {
            String bottomScale = bottomScaleTextArray[i];//底部刻度文字
            float scaleWidth = bottomTextPaint.measureText(bottomScale);//文字宽度
            bottomTextPaint.getFontMetrics(fm);
            float scaleHeight = Math.abs(fm.ascent);//文字高度
            if (i == 0) {//第一条经线
                //画经线
                canvas.drawLine(longitudeLinePaint.getStrokeWidth(), 0, longitudeLinePaint
                        .getStrokeWidth(), coordinateHeight, longitudeLinePaint);
                //画经线刻度
                canvas.drawText(bottomScale, space, coordinateHeight + MARGIN_BOTTOM / 2 + scaleHeight / 2, bottomTextPaint);
            } else if (i == longitudeNums - 1) {//最后一条经线
                //画经线
                canvas.drawLine(coordinateWidth - longitudeLinePaint.getStrokeWidth(), 0, coordinateWidth - longitudeLinePaint.getStrokeWidth(), coordinateHeight, longitudeLinePaint);
                //画经线刻度
                canvas.drawText(bottomScale, coordinateWidth - scaleWidth - space, coordinateHeight + MARGIN_BOTTOM / 2 + scaleHeight / 2, bottomTextPaint);
            } else {//其中所有的经线
                //画经线
                float tempLongitudeSpace = i * longitudeSpace;//经线间隙,i此时应从1开始,因为第一个if屏蔽了0
                canvas.drawLine(tempLongitudeSpace - longitudeLinePaint.getStrokeWidth(), 0, tempLongitudeSpace - longitudeLinePaint.getStrokeWidth(), coordinateHeight, longitudeLinePaint);
                //画经线刻度
                canvas.drawText(bottomScale, tempLongitudeSpace - scaleWidth / 2, coordinateHeight + MARGIN_BOTTOM / 2 + scaleHeight / 2, bottomTextPaint);
            }
        }
    }

    //画纬线
    private void drawLatitude(Canvas canvas) {
        //纬线总条数
        int latitudeNums = leftScaleTextArray.length;
        //保证左右的内容数据量一致
        if (latitudeNums != rightScaleTextArray.length) {
            rightScaleTextArray = null;
            rightScaleTextArray = new String[latitudeNums];
            for (int i = 0; i < latitudeNums; i++) {
                rightScaleTextArray[i] = "";
            }
        }
        //纬线间宽度
        float latitudeSpace = coordinateHeight / (latitudeNums - 1);
        Paint.FontMetrics fm = new Paint.FontMetrics();
        for (int i = 0; i < latitudeNums; i++) {
            String leftScale = leftScaleTextArray[i];//左边刻度文字
            float leftScaleWidth = leftTextPaint.measureText(leftScale);//左边文字宽度
            leftTextPaint.getFontMetrics(fm);
            float leftScaleHeight = Math.abs(fm.ascent);//左边文字高度

            String rightScale = rightScaleTextArray[i];//右边刻度文字
            float rightScaleWidth = rightTextPaint.measureText(rightScale);//右边文字宽度
            rightTextPaint.getFontMetrics(fm);
            float rightScaleHeight = Math.abs(fm.ascent);//右边文字高度
            if (i == 0) {//第一条纬线
                //画纬线
                canvas.drawLine(0, 0.1f, coordinateWidth, 0.1f, latitudeLinePaint);
                //画纬线刻度(左)
                canvas.drawText(leftScale, space, leftScaleHeight + space, leftTextPaint);
                //画纬线刻度(右)
                canvas.drawText(rightScale, coordinateWidth  + space, leftScaleHeight + space, rightTextPaint);
            } else if (i == latitudeNums - 1) {//最后一条纬线
                //画纬线
                canvas.drawLine(0, coordinateHeight, coordinateWidth, coordinateHeight, latitudeLinePaint);
                //画纬线刻度(左)
                canvas.drawText(leftScale, space, coordinateHeight - leftScaleHeight + space, leftTextPaint);
                //画纬线刻度(右)
                canvas.drawText(rightScale, coordinateWidth  + space, coordinateHeight - rightScaleHeight + space, rightTextPaint);
            } else {//中间的纬线
                //画纬线
                float tempLatitudeSpace = i * latitudeSpace;//纬线间隙,i此时应从1开始,因为第一个if屏蔽了0
                canvas.drawLine(0, tempLatitudeSpace, coordinateWidth, tempLatitudeSpace, latitudeLinePaint);
                //画纬线刻度(左)
                canvas.drawText(leftScale, space, tempLatitudeSpace - leftScaleHeight + space, leftTextPaint);
                //画纬线刻度(右)
                canvas.drawText(rightScale, coordinateWidth  + space, tempLatitudeSpace - rightScaleHeight + space, rightTextPaint);
            }
        }
    }

    private void checkParamter() {
        if (this.coordinateHeight <= 0) {
            throw new IllegalArgumentException("coordinateHeight can't be zero or smaller than zero");
        }
        if (this.coordinateWidth <= 0) {
            throw new IllegalArgumentException("coordinateWidth can't be zero or smaller than zero");
        }

    }

    /**
     * 设置底部文字大小
     *
     * @param size
     */
    public void setBottomTextSize(float size) {
        bottomTextPaint.setTextSize(size);
    }

    /**
     * 设置底部文字颜色
     *
     * @param color
     */
    public void setBottomTextColor(int color) {
        bottomTextPaint.setColor(color);
    }

    @Override
    public void setCoordinateHeight(float coordinateHeight) {
        super.setCoordinateHeight(coordinateHeight);
        //TODO: doing soming
    }

    @Override
    public void setCoordinateWidth(float coordinateWidth) {
        super.setCoordinateWidth(coordinateWidth);
        //TODO: doing soming

    }

    public String[] getBottomScaleTextArray() {
        return bottomScaleTextArray;
    }

    public void setBottomScaleTextArray(String[] bottomScaleTextArray) {
        this.bottomScaleTextArray = bottomScaleTextArray;
    }

    public String[] getLeftScaleTextArray() {
        return leftScaleTextArray;
    }

    public void setLeftScaleTextArray(String[] leftScaleTextArray) {
        this.leftScaleTextArray = leftScaleTextArray;
    }

    public String[] getRightScaleTextArray() {
        return rightScaleTextArray;
    }

    public void setRightScaleTextArray(String[] rightScaleTextArray) {
        this.rightScaleTextArray = rightScaleTextArray;
    }

}
