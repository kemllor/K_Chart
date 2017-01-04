package com.kem.kline.viewbeans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：kl_boll
 *
 * @author zhaoyi
 * @date   2016-11-7
 */
public class Boll extends ViewContainer {
    //实心画笔
    private Paint fillPaint = null;
    //外壳画笔
    private Paint strokePaint = null;
    //是否填充
    private boolean isFill = true;
    //数据集
    private List<CandleLine.CandleLineBean> dataList = new ArrayList<CandleLine.CandleLineBean>();
    //从第几个点开始画
    private int drawBollIndex = 0;
    //显示的点数
    private int showBollNums = 100;
    //最少显示的点数
    private int minBollNums = 30;
    //默认显示点数
    private int defaultBollNums = 2;
    //涨时颜色
    private int upColor = Color.parseColor("#ff322e");
    //跌时颜色
    private int downColor = Color.parseColor("#2eff2e");
    //不涨不跌颜色
    private int evenColor = Color.parseColor("#656565");
    //柱之间间隙
    private float space = 0;
    //两指间距离
    private float distance = 0f;
    //缩放的中心点下标
    private int zoomBollIndex = 0;
    //是否正在缩放
    private boolean isZooming = false;
    //是否计算极值(有时元素极值并不是元素本身数据的极值,有可能是其他数据极值)
    private boolean isCalculateDataExtremum = true;
    private float bollWidth;

    /**
     * 柱状图
     *
     * @param YMax 坐标系中最大值
     * @param YMin 坐标系中最小值
     */
    public Boll(Context context,float YMax, float YMin) {
        super(context);
        this.YMax = YMax;
        this.YMin = YMin;
        //初始化线画笔
        initPaint();
    }

    public Boll(Context context) {
        super(context);
        //初始化线画笔
        initPaint();
    }

    private void initPaint() {
        //初始化线画笔
        this.fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setStrokeWidth(1.5f);
        fillPaint.setColor(Color.parseColor("#1199EE"));

        this.strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(0.3f);
        fillPaint.setColor(Color.parseColor("#1199EE"));
        strokePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            if (isShow) {
                checkParamter();
                bollWidth = coordinateWidth / showBollNums;
                space = bollWidth / 20;
                PointF lowRightBottomPoint = null;
                PointF highLeftTopPoint = null;
                PointF openPoint = null;
                PointF closePoint = null;
                int j = 0;
                for (int i = drawBollIndex; i < dataList.size(); i++) {
                    CandleLine.CandleLineBean bean = dataList.get(i);
                    highLeftTopPoint = getHighLeftTopPoint(bean, j);
                    lowRightBottomPoint = getLowRightBottomPoint(bean, j);
                    //画柱子
                    canvas.drawRect(highLeftTopPoint.x + strokePaint.getStrokeWidth() + 10, highLeftTopPoint.y + strokePaint.getStrokeWidth(),
                            lowRightBottomPoint.x - strokePaint.getStrokeWidth() - 10,
                            lowRightBottomPoint.y - strokePaint.getStrokeWidth(), fillPaint);

                    openPoint = getOpenLeftTopPoint(bean, i);
                    closePoint = getCloseRightBottomPoint(bean, i);
                    if (Float.valueOf(bean.getOpenPrice()) > Float.valueOf(bean.getClosePrice())) {
                        canvas.drawRect(highLeftTopPoint.x + strokePaint.getStrokeWidth(), openPoint.y, highLeftTopPoint.x + strokePaint.getStrokeWidth() + 10, openPoint.y + 2, fillPaint);
                        canvas.drawRect(lowRightBottomPoint.x + strokePaint.getStrokeWidth() - 10, closePoint.y, lowRightBottomPoint.x + strokePaint.getStrokeWidth(), closePoint.y + 2, fillPaint);
                    } else if (Float.valueOf(bean.getOpenPrice()) < Float.valueOf(bean.getClosePrice())) {
                        canvas.drawRect(lowRightBottomPoint.x + strokePaint.getStrokeWidth() - 10, closePoint.y, lowRightBottomPoint.x + strokePaint.getStrokeWidth(), closePoint.y + 2, fillPaint);
                        canvas.drawRect(highLeftTopPoint.x + strokePaint.getStrokeWidth(), openPoint.y, highLeftTopPoint.x + strokePaint.getStrokeWidth() + 10, openPoint.y + 2, fillPaint);
                    } else {
                        canvas.drawRect(highLeftTopPoint.x + strokePaint.getStrokeWidth(), openPoint.y, highLeftTopPoint.x + strokePaint.getStrokeWidth() + 10, openPoint.y + 2, fillPaint);
                        canvas.drawRect(lowRightBottomPoint.x + strokePaint.getStrokeWidth() - 10, openPoint.y, lowRightBottomPoint.x + strokePaint.getStrokeWidth(), openPoint.y + 2, fillPaint);
                    }
                    j++;
                }
            }
        } catch (Exception e) {
        }
    }

    private PointF getOpenLeftTopPoint(CandleLine.CandleLineBean bean, int index) {
        PointF pointF = new PointF();
        if (dataList.size() - 1 >= index) {
            float y = (1f - (Float.valueOf(bean.getOpenPrice()) - YMin) / (YMax - YMin)) * coordinateHeight;
            pointF.set(0, y);
        } else {
            pointF.set(0, 0);
        }
        return pointF;
    }

    private PointF getCloseRightBottomPoint(CandleLine.CandleLineBean bean, int index) {
        PointF pointF = new PointF();
        float y = (1f - (Float.valueOf(bean.getClosePrice()) - YMin) / (YMax - YMin)) * coordinateHeight;
        pointF.set(0, y);
        return pointF;
    }

    private PointF getHighLeftTopPoint(CandleLine.CandleLineBean bean, int index) {
        PointF pointF = new PointF();
        if (dataList.size() - 1 >= index) {
            float x = index * (coordinateWidth / showBollNums) + space;
            float y = (1f - (Float.valueOf(bean.getHeightPrice()) - YMin) / (YMax - YMin)) * coordinateHeight;
            pointF.set(x, y);
        } else {
            pointF.set(0, 0);
        }
        return pointF;
    }

    private PointF getLowRightBottomPoint(CandleLine.CandleLineBean bean, int index) {
        PointF pointF = new PointF();
        float x = (index + 1) * (coordinateWidth / showBollNums) - space;
        float y = (1f - (Float.valueOf(bean.getLowPrice()) - YMin) / (YMax - YMin)) * coordinateHeight;
        pointF.set(x, y);
        return pointF;
    }

    private void checkParamter() {
        if (this.coordinateHeight <= 0) {
            throw new IllegalArgumentException("coordinateHeight can't be zero or smaller than zero");
        }
        if (this.coordinateWidth <= 0) {
            throw new IllegalArgumentException("coordinateWidth can't be zero or smaller than zero");
        }
    }

    private static final int NONE = 0;
    private static final int MOVE = 1;
    private static final int ZOOM = 2;
    private PointF moveDownPointF = new PointF();
    private int mode = NONE;
    private int moveOffest;

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                moveDownPointF.x = event.getX();
                moveDownPointF.y = event.getY();
                mode = MOVE;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MOVE) {
                    float difX = moveDownPointF.x - event.getX();
                    moveOffest = Math.abs((int) (difX * showBollNums / coordinateWidth));
                    if (moveOffest >= 1) {
                        move(difX);
                        moveDownPointF.x = event.getX();
                        moveDownPointF.y = event.getY();
                    }
                } else if (mode == ZOOM) {
                    float spacing = spacing(event) - distance;
                    if (Math.abs(spacing) >= MIN_FINGER_DISTANCE) {
                        distance = spacing(event);
                        if (spacing < 0) {
                            //缩小
                            if (zoomOut())
                                calculateDrawHistogramIndex(event, -1);//-1代表了缩小
                        } else {
                            //放大
                            if (zoomIn())
                                calculateDrawHistogramIndex(event, 1);//1代表了放大
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    /**
     * 移动
     */
    private void move(float difX) {
        if (difX > 0) {//手指向左移动
            if ((drawBollIndex + showBollNums + moveOffest) <= dataList.size() - 1) {
                drawBollIndex += moveOffest;
            } else {
                drawBollIndex = dataList.size() - showBollNums;
                if (drawBollIndex < 0) {
                    drawBollIndex = 0;
                }
            }
        } else if (difX < 0) {//手指向右移动
            drawBollIndex = drawBollIndex - moveOffest;
            if (drawBollIndex < 0) {
                drawBollIndex = 0;
            }
        }
    }

    /**
     * 放大
     *
     * @return 表示是否进行了放大, true代表showHistogramNums进行了--;
     */
    private boolean zoomIn() {
        if (showBollNums >= minBollNums) {
            //减少蜡烛根数
            showBollNums = showBollNums - incremental;
            showBollNums = showBollNums < minBollNums ? minBollNums : showBollNums;
            return true;
        } else {
            //此时显示的蜡烛数应该等于最小蜡烛数
            showBollNums = minBollNums;
            return false;
        }
    }

    /**
     * 缩小
     *
     * @return 标识是否进行了缩小, true代表showHistogramNums进行了++;
     */
    private boolean zoomOut() {
        if (showBollNums <= defaultBollNums) {
            //增加蜡烛根数
            showBollNums = showBollNums + incremental;
            showBollNums = showBollNums > defaultBollNums ? defaultBollNums : showBollNums;
            return true;
        } else {
            showBollNums = defaultBollNums;
            return false;
        }
    }

    /**
     * 计算两指距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 得到放大缩小的中心蜡烛下标
     *
     * @param event
     * @return
     */
    private int getZoomCenterHistogramIndex(MotionEvent event) {
        //计算放大中心
        float pointLeft = event.getX(0) < event.getX(1) ? event.getX(0) : event.getX(1);
        float pointRight = event.getX(0) > event.getX(1) ? event.getX(0) : event.getX(1);
        int leftIndex = (int) ((pointLeft * showBollNums) / coordinateWidth);
        int rightIndex = (int) ((pointRight * showBollNums) / coordinateWidth);
        //得到两只之间的蜡烛相对于总显示根数的根数
        int centerHistogramNums = (rightIndex - leftIndex) / 2 + leftIndex;
        int zoomIndex = drawBollIndex + centerHistogramNums;
        return zoomIndex;
    }

    /**
     * 计算绘画蜡烛的起始值
     */
    private void calculateDrawHistogramIndex(MotionEvent event, int zoomType) {
        //计算左边应消失的根数,从而改变了右边消失的根数,因为总消失根数不变
        int zoomHistogramIndexTemp = getZoomCenterHistogramIndex(event);

        if (zoomType == 1) { //放大
            if (zoomHistogramIndexTemp - zoomBollIndex > 0) {
                //目标左移,需要向右纠正,不改变绘图起始坐标,就会让图右移,因为显示条数在变少
            } else if (zoomHistogramIndexTemp - zoomBollIndex < 0) {
                //目标右移,需要向左纠正
                drawBollIndex = drawBollIndex + incremental;
            }
        } else if (zoomType == -1) {//缩小
            if (zoomHistogramIndexTemp - zoomBollIndex > 0) {
                //目标左移,需要向右纠正
                drawBollIndex = drawBollIndex - incremental;
            } else if (zoomHistogramIndexTemp - zoomBollIndex < 0) {
                //目标右移,需要向左纠正,不改变绘图其实坐标,就会让图左移,因为现实条数增多
            }
        }
        //越界判断
        drawBollIndex = drawBollIndex >= dataList.size() ? dataList.size() - 1 : drawBollIndex;
        drawBollIndex = drawBollIndex < 0 ? 0 : drawBollIndex;

    }

    public static class HistogramBean {
        //成交量颜色变化
        private double isUp = 0;

        private float turnover = 0;

        public HistogramBean() {
        }

        public HistogramBean(double isUp, float turnover) {
            this.isUp = isUp;
            this.turnover = turnover;
        }

        public double getIsUp() {
            return isUp;
        }

        public void setIsUp(double isUp) {
            this.isUp = isUp;
        }

        public float getTurnover() {
            return turnover;
        }

        public void setTurnover(float turnover) {
            this.turnover = turnover;
        }
    }

    public void setCoordinateHeight(float coordinateHeight) {
        super.setCoordinateHeight(coordinateHeight);
        //TODO:do noting
    }

    public void setCoordinateWidth(float coordinateWidth) {
        super.setCoordinateWidth(coordinateWidth);
        //TODO:do noting
    }

    public void setColor(int upColor, int evenColor, int downColor) {
        this.upColor = upColor;
        this.downColor = downColor;
        this.evenColor = evenColor;
    }

    public void setUpColor(int upColor) {
        this.upColor = upColor;
    }

    public void setEvenColor(int evenColor) {
        this.evenColor = evenColor;
    }

    public void setDownColor(int downColor) {
        this.downColor = downColor;
    }

    public boolean isFill() {
        return isFill;
    }

    public int getDrawBollIndex() {
        return drawBollIndex;
    }

    public void setDrawBollIndex(int drawBollIndex) {
        this.drawBollIndex = drawBollIndex;
    }

    public int getShowBollNums() {
        return showBollNums;
    }

    public int getMinBollNums() {
        return minBollNums;
    }

    public void setMinBollNums(int minBollNums) {
        this.minBollNums = minBollNums;
    }

    public int getDefaultBollNums() {
        return defaultBollNums;
    }

    public void setDefaultBollNums(int defaultBollNums) {
        this.defaultBollNums = defaultBollNums;
    }

    public void setShowBollNums(int showBollNums) {
        this.showBollNums = showBollNums;
    }

    public void setFill(boolean isFill) {
        this.isFill = isFill;
        if (isFill) {
            fillPaint.setStyle(Paint.Style.FILL);
        } else {
            fillPaint.setStyle(Paint.Style.STROKE);
        }
    }

    public boolean isCalculateDataExtremum() {
        return isCalculateDataExtremum;
    }

    public void setCalculateDataExtremum(boolean isCalculateDataExtremum) {

        this.isCalculateDataExtremum = isCalculateDataExtremum;
    }

    public List<CandleLine.CandleLineBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<CandleLine.CandleLineBean> dataList) {
        this.dataList = dataList;
    }
}
