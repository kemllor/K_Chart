package com.kem.kline.viewbeans;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.view.MotionEvent;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：十字线
 *
 * @author zhaoyi
 * @date 2016-11-7
 */
public class CrossLine extends ViewContainer {
    //线画笔
    private Paint linePaint = null;
    //点画笔
    private Paint pointPaint = null;
    //矩形画笔
    private Paint rectPaint = null;

    private Paint textPaint = null;

    //坐标系当前显示总数
    private int maxShowNums = 0;
    //线颜色
    private int lineColor = Color.RED;
    //点颜色
    private int pointColor = Color.parseColor("#999999");
    //矩形填充颜色
    private int rectColor = Color.RED;
    //文字填充颜色
    private int textColor = Color.WHITE;
    //焦点
    private PointF pointF = new PointF();
    //坐标系数据
    private List<String> pointList = new ArrayList<String>();
    private List<CandleLine.CandleLineBean> candleList = new ArrayList<CandleLine.CandleLineBean>();
    //点的半径
    private int radius = 10;
    //是否显示点
    private boolean isShowPoint = true;
    //是否显示纬线
    private boolean isShowLatitude = true;
    //是否显示经线
    private boolean isShowLongitude = true;
    private CrossLineCallBack mCrossLineCallBack;
    private int drawIndex;
    private int count;

    public interface CrossLineCallBack {
        public void crossLineShowCallBack(int index);

        public void crossLineHideCallBack();
    }

    public CrossLine(float YMin, float YMax, int maxShowNums) {
        this.YMin = YMin;
        this.YMax = YMax;
        this.maxShowNums = maxShowNums;
        this.isShow = false;
        initPaint();
    }

    public CrossLine() {
        this.isShow = true;
        initPaint();
    }

    //初始化画笔
    private void initPaint() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1.5f);
        linePaint.setColor(lineColor);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(pointColor);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(rectColor);

        textPaint = new Paint();
        textPaint.setTextSize(22f);
        textPaint.setColor(textColor);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            //修改十字线显示模式 //// TODO: 2016/12/28  kem
            count = candleList.size();

            //计算蜡烛的宽度
            float candleWidth = coordinateWidth / maxShowNums;
            //            int index = ((int) (pointF.x / candleWidth));
            //显示最后一条K线的实时价格；
            int index = maxShowNums - 1;
            //显示在纬线上面的价格
            float price = 0f;
            if (isShowLatitude) {
                float y = 0f;
                float x = pointF.x;

                CandleLine.CandleLineBean lineBean = null;

                try {
                    //控制画纬线范围
                    if (x >= count * candleWidth) {
                        index = count - 1;
                        lineBean = candleList.get(index + drawIndex);
                        //从收盘价与开盘价之间比较出最大值
                        float maxPrice = lineBean.getClosePrice() >= lineBean.getOpenPrice() ?
                                lineBean.getClosePrice() :
                                lineBean.getOpenPrice();
                        //从收盘价与开盘价之间比较出最小值
                        float minPrice = lineBean.getClosePrice() <= lineBean.getOpenPrice() ?
                                lineBean.getClosePrice() :
                                lineBean.getOpenPrice();

                        if (lineBean.getOpenPrice() > lineBean.getClosePrice()) {
                            price = minPrice;
                            y = (1f - (minPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                        } else if (lineBean.getOpenPrice() > lineBean.getClosePrice()) {
                            price = maxPrice;
                            y = (1f - (maxPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                        } else {
                            price = maxPrice;
                            y = (1f - (maxPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                        }

                    } else {
                        lineBean = candleList.get(index + drawIndex);
                        //从收盘价与开盘价之间比较出最大值
                        float maxPrice = lineBean.getClosePrice() >= lineBean.getOpenPrice() ?
                                lineBean.getClosePrice() :
                                lineBean.getOpenPrice();
                        //从收盘价与开盘价之间比较出最小值
                        float minPrice = lineBean.getClosePrice() <= lineBean.getOpenPrice() ?
                                lineBean.getClosePrice() :
                                lineBean.getOpenPrice();

                        if (lineBean.getOpenPrice() > lineBean.getClosePrice()) {
                            price = minPrice;
                            y = (1f - (minPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                        } else if (lineBean.getOpenPrice() > lineBean.getClosePrice()) {
                            price = maxPrice;
                            y = (1f - (maxPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                        } else {
                            price = maxPrice;
                            y = (1f - (maxPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                        }
                    }

                } catch (NumberFormatException e) {
                    y = pointF.y;
                }

                //绘制纬线
                drawLatitude(canvas, y);

                //画纬线刻度
                drawRect(canvas, y, format(price, 2, true), true);
            }
            if (isShowLongitude) {
                float x = pointF.x;
                //控制画经线范围
                if (x >= candleWidth * count) {
                    x = count * candleWidth - candleWidth / 2;
                    index = count - 1;
                } else {
                    if (index == 0) {
                        x = candleWidth / 2;
                    } else {
                        x = index * candleWidth + candleWidth / 2;
                    }
                }
                drawLongitude(canvas, x);
            }





           /* if (isShow) {
                checkParamter();
                if (chartShowType == CanvasView.ChartShowType.TIME_DAY) {
                    //计算点的宽度
                    float pointWidth = coordinateWidth / maxShowNums;
                    //计算触摸点
                    int index = (int) (pointF.x / pointWidth);
                    count = pointList.size();
                    if (isShowLatitude) {
                        float y = pointF.y;
                        float x = pointF.x;
                        try {
                            //控制画纬线范围
                            if (x >= count * pointWidth) {
                                index = count - 1;
                                y = (1f -
                                        (Float.parseFloat(pointList.get(index)) -
                                                YMin) /
                                                (YMax - YMin)) *
                                        coordinateHeight;
                            } else {

                                y = (1f - (Float.parseFloat(pointList.get(index)) - YMin) /
                                        (YMax - YMin)) *
                                        coordinateHeight;
                            }
                        } catch (NumberFormatException e) {
                            y = pointF.y;
                        }
                        //绘制纬线
                        drawLatitude(canvas, y);
                    }
                    if (isShowLongitude) {
                        float x = pointF.x;
                        //控制画经线范围
                        if (x >= count * pointWidth) {
                            index = count - 1;
                            x = index * pointWidth;

                        } else {
                            x = index * pointWidth;
                        }

                        //绘制经线
                        drawLongitude(canvas, x);
                    }
                    if (isShowPoint) {
                        float x = pointF.x;
                        float y = pointF.y;
                        try {
                            if (x >= count * pointWidth) {
                                x = (count - 1) * pointWidth;
                            } else {
                                x = index * pointWidth;
                            }
                            if (x >= count * pointWidth) {
                                y = (1f - (Float.parseFloat(pointList.get(count - 1)) - YMin) /
                                        (YMax - YMin)) * coordinateHeight;
                            } else {
                                y = (1f - (Float.parseFloat(pointList.get(index)) - YMin) /
                                        (YMax - YMin)) * coordinateHeight;
                            }
                        } catch (NumberFormatException e) {
                            y = pointF.y;
                        }
                        //绘制点
                        drawCircle(canvas, x, y);
                        if (mCrossLineCallBack != null) {
                            mCrossLineCallBack.crossLineShowCallBack(index);
                        }
                    }
                } else {
                    count = candleList.size();

                    //计算蜡烛的宽度
                    float candleWidth = coordinateWidth / maxShowNums;
                    int index = ((int) (pointF.x / candleWidth));
                    //显示在纬线上面的价格
                    float price = 0f;
                    if (isShowLatitude) {
                        float y = 0f;
                        float x = pointF.x;

                        CandleLine.CandleLineBean lineBean = null;

                        try {
                            //控制画纬线范围
                            if (x >= count * candleWidth) {
                                index = count - 1;
                                lineBean = candleList.get(index + drawIndex);
                                //从收盘价与开盘价之间比较出最大值
                                float maxPrice = lineBean.getClosePrice() >= lineBean.getOpenPrice() ?
                                        lineBean.getClosePrice() :
                                        lineBean.getOpenPrice();
                                //从收盘价与开盘价之间比较出最小值
                                float minPrice = lineBean.getClosePrice() <= lineBean.getOpenPrice() ?
                                        lineBean.getClosePrice() :
                                        lineBean.getOpenPrice();

                                if (lineBean.getOpenPrice() > lineBean.getClosePrice()) {
                                    price = minPrice;
                                    y = (1f - (minPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                                } else if (lineBean.getOpenPrice() > lineBean.getClosePrice()) {
                                    price = maxPrice;
                                    y = (1f - (maxPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                                } else {
                                    price = maxPrice;
                                    y = (1f - (maxPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                                }

                            } else {
                                lineBean = candleList.get(index + drawIndex);
                                //从收盘价与开盘价之间比较出最大值
                                float maxPrice = lineBean.getClosePrice() >= lineBean.getOpenPrice() ?
                                        lineBean.getClosePrice() :
                                        lineBean.getOpenPrice();
                                //从收盘价与开盘价之间比较出最小值
                                float minPrice = lineBean.getClosePrice() <= lineBean.getOpenPrice() ?
                                        lineBean.getClosePrice() :
                                        lineBean.getOpenPrice();

                                if (lineBean.getOpenPrice() > lineBean.getClosePrice()) {
                                    price = minPrice;
                                    y = (1f - (minPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                                } else if (lineBean.getOpenPrice() > lineBean.getClosePrice()) {
                                    price = maxPrice;
                                    y = (1f - (maxPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                                } else {
                                    price = maxPrice;
                                    y = (1f - (maxPrice - YMin) / (YMax - YMin)) * coordinateHeight;
                                }
                            }

                        } catch (NumberFormatException e) {
                            y = pointF.y;
                        }

                        //绘制纬线
                        drawLatitude(canvas, y);

                        //画纬线刻度
                      *//*  if (index > maxShowNums / 2) {
                            drawRect(canvas, y, format(price, 1, false), false);
                        } else {*//*
                        drawRect(canvas, y, format(price, 1, false), true);
                        //                        }


                    }
                    if (isShowLongitude) {
                        float x = pointF.x;
                        //控制画经线范围
                        if (x >= candleWidth * count) {
                            x = count * candleWidth - candleWidth / 2;
                            index = count - 1;
                        } else {
                            if (index == 0) {
                                x = candleWidth / 2;
                            } else {
                                x = index * candleWidth + candleWidth / 2;
                            }
                        }
                        drawLongitude(canvas, x);
                    }
                    if (isShowPoint) {
                        //TODO
                    }
                    if (mCrossLineCallBack != null) {
                        mCrossLineCallBack.crossLineShowCallBack(index + drawIndex);
                    }

                }

            } else {
                if (mCrossLineCallBack != null) {
                    mCrossLineCallBack.crossLineHideCallBack();
                }
            }*/
        } catch (Exception e) {
        }
    }

    //格式化数据
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String format(double in, int keepNum, boolean isRound) {

        String result = "";

        DecimalFormat format = new DecimalFormat();

        if (keepNum < 0) {
            keepNum = 0;
        }

        format.setMaximumFractionDigits(keepNum);

        format.setMinimumFractionDigits(keepNum);

        format.setGroupingUsed(false);

        if (isRound) {
            format.setRoundingMode(RoundingMode.HALF_UP);
        } else {
            format.setRoundingMode(RoundingMode.FLOOR);
        }

        result = format.format(in);

        return result;
    }

    private void checkParamter() {
        if (this.maxShowNums < 0) {
            throw new IllegalArgumentException("maxShowNums must be larger than 0");
        }
        if (this.coordinateHeight <= 0) {
            throw new IllegalArgumentException(
                    "coordinateHeight can't be zero or smaller than zero");
        }
        if (this.coordinateWidth <= 0) {
            throw new IllegalArgumentException(
                    "coordinateWidth can't be zero or smaller than zero");
        }
        if (pointF.x < 0f && pointF.y < 0f) {
            throw new IllegalArgumentException("pointF.x pointF.y,must bigger than -1");
        }
    }

    //绘制纬线
    private void drawLatitude(Canvas canvas, float y) {
        canvas.drawLine(0, y, maxCoordinateWidth, y, linePaint);
    }

    //绘制矩形 和 里面的刻度文字
    private void drawRect(Canvas canvas, float y, String values, boolean right) {

        float textsize = textPaint.measureText(values);

        if (right) {
            if ((y - 15) <= 0) {
                y = 0;
                canvas.drawRect(maxCoordinateWidth - textsize - 10, y, maxCoordinateWidth, y + 30,
                        rectPaint);

                canvas.drawText(values, maxCoordinateWidth - textsize - 5, y + 8 + 15, textPaint);
            } else {


                //// TODO: 2016/12/28 修改矩形框显示位置；
                canvas.drawRect(maxCoordinateWidth - 100 , y - 15, maxCoordinateWidth , y + 15,
                        rectPaint);

                canvas.drawText(values, coordinateWidth+5 , y + 8, textPaint);
                
               /* canvas.drawRect(coordinateWidth - textsize - 10, y - 15, coordinateWidth, y + 15,
                        rectPaint);

                canvas.drawText(values, coordinateWidth - textsize - 5, y + 8, textPaint);*/
            }


        } else {

            if ((y - 15) <= 0) {
                y = 0;
                canvas.drawRect(0, y, textsize + 10, y + 30, rectPaint);

                canvas.drawText(values, 5, y + 8 + 15, textPaint);

            } else {

                canvas.drawRect(0, y - 15, textsize + 10, y + 15, rectPaint);

                canvas.drawText(values, 5, y + 8, textPaint);

            }
        }
    }

    //绘制经线
    private void drawLongitude(Canvas canvas, float x) {
        canvas.drawLine(x, 0, x, coordinateHeight, linePaint);
    }

    //绘制圆点
    private void drawCircle(Canvas canvas, float x, float y) {
        canvas.drawCircle(x, y, radius, pointPaint);
    }

    @Override
    public void move(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setShow(true);
                break;
            case MotionEvent.ACTION_MOVE:
                pointF.x = event.getX();
                pointF.y = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                setShow(false);
                break;
        }
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public PointF getPointF() {
        return pointF;
    }

    public void setPointF(PointF pointF) {
        this.pointF = pointF;
    }

    public int getShowNums() {
        return maxShowNums;
    }

    public void setShowNums(int maxShowNums) {
        this.maxShowNums = maxShowNums;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public List<String> getPointList() {
        return pointList;
    }

    public void setPointList(List<String> data) {
        this.pointList = data;
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public boolean isShowPoint() {
        return isShowPoint;
    }

    public void setShowPoint(boolean isShowPoint) {
        this.isShowPoint = isShowPoint;
    }

    public boolean isShowLatitude() {
        return isShowLatitude;
    }

    public void setShowLatitude(boolean isShowLatitude) {
        this.isShowLatitude = isShowLatitude;
    }

    public boolean isShowLongitude() {
        return isShowLongitude;
    }

    public void setShowLongitude(boolean isShowLongitude) {
        this.isShowLongitude = isShowLongitude;
    }

    public List<CandleLine.CandleLineBean> getCandleList() {
        return candleList;
    }

    public void setCandleList(List<CandleLine.CandleLineBean> candleList) {
        this.candleList = candleList;
    }

    public void setCrossLineCallBack(CrossLineCallBack crossLineCallBack) {
        this.mCrossLineCallBack = crossLineCallBack;
    }

    public void setDrawIndex(int drawIndex) {
        this.drawIndex = drawIndex;
    }
}
