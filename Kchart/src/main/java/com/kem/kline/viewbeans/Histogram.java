package com.kem.kline.viewbeans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.kem.kline.views.CanvasView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：柱状图
 *
 * @author zhaoyi
 */
public class Histogram extends ViewContainer {
    //实心画笔
    private Paint fillPaint = null;
    //外壳画笔
    private Paint strokePaint = null;
    //是否填充
    private boolean isFill = true;
    //数据集
    private List<HistogramBean> dataList = new ArrayList<HistogramBean>();
    //从第几个点开始画
    private int drawHistogramIndex = 0;
    //显示的点数
    private int showHistogramNums = 100;
    //最少显示的点数
    private int minHistogramNums = 30;
    //默认显示点数
    private int defaultShowHistogramNums = 2;
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
    private int zoomHistogramIndex = 0;
    //是否正在缩放
    private boolean isZooming = false;
    //是否计算极值(有时元素极值并不是元素本身数据的极值,有可能是其他数据极值)
    private boolean isCalculateDataExtremum = true;
    //柱子宽度
    private float histogramWidth;

    /**
     * 柱状图
     *
     * @param YMax 坐标系中最大值
     * @param YMin 坐标系中最小值
     */
    public Histogram(float YMax, float YMin, Context context) {
        super(context);
        this.YMax = YMax;
        this.YMin = YMin;
        //初始化线画笔
        initPaint();
    }

    public Histogram(Context context) {
        super(context);
        //初始化线画笔
        initPaint();
    }

    private void initPaint() {
        //初始化线画笔
        this.fillPaint = new Paint();
        fillPaint.setAntiAlias(true);
        fillPaint.setStrokeWidth(1.5f);
        fillPaint.setColor(Color.GRAY);

        this.strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(0.3f);
        strokePaint.setColor(Color.GRAY);
        strokePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            if (isShow) {
                checkParamter();
                PointF rightBottomPoint = null;
                PointF leftTopPoint = null;
                histogramWidth = coordinateWidth / showHistogramNums;
                space = histogramWidth / 10;
                int j = 0;
                for (int i = drawHistogramIndex; i < dataList.size(); i++) {
                    HistogramBean bean = dataList.get(i);
                    leftTopPoint = getLeftTopPoint(j, bean);
                    rightBottomPoint = getRightBottomPoint(j);
                    if (bean.isUp > 0) {
                        fillPaint.setColor(upColor);
                    } else if (bean.isUp < 0) {
                        fillPaint.setColor(downColor);
                    } else {
                        fillPaint.setColor(evenColor);
                    }

                    //画实心
                    if (histogramWidth < 9) {
                        canvas.drawRect(leftTopPoint.x - histogramWidth / 2 , leftTopPoint.y,
                                rightBottomPoint.x - histogramWidth / 2 ,
                                rightBottomPoint.y, fillPaint);
                    } else {
                        canvas.drawRect(leftTopPoint.x, leftTopPoint.y,
                                rightBottomPoint.x,
                                rightBottomPoint.y, fillPaint);
                    }
                    //画边框
//                    canvas.drawRect(leftTopPoint.x, leftTopPoint.y, rightBottomPoint.x, rightBottomPoint.y, strokePaint);
                    j++;
                }
            }
        } catch (Exception e) {
        }


    }

    private PointF getLeftTopPoint(int index, HistogramBean bean) {
        PointF pointF = new PointF();
//        space = (coordinateWidth / showHistogramNums) / 10;

        //画纬线
        if (dataList.size() - 1 >= index) {
            float x = index * (coordinateWidth / showHistogramNums) + space;
            float y = (1f - (bean.turnover - YMin) / (YMax - YMin)) * coordinateHeight;
           /* if (histogramWidth < 9) {
                pointF.set(x - histogramWidth / 2, y);
            } else {
                pointF.set(x, y);
            }*/

            pointF.set(x, y);
        } else {
            pointF.set(0, 0);
        }
        return pointF;
    }

    private PointF getRightBottomPoint(int index) {
        PointF pointF = new PointF();
//        space = (coordinateWidth / showHistogramNums) / 20;

        //画纬线
        float x = (index + 1) * (coordinateWidth / showHistogramNums) - space;
        float y;
        if (chartShowType == CanvasView.ChartShowType.MACD) {
            y = coordinateHeight / 2;
        } else {
            y = coordinateHeight;
        }

      /*  if (histogramWidth < 9) {
            pointF.set(x - histogramWidth / 2, y);
        } else {
            pointF.set(x, y);
        }*/
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
                zoomHistogramIndex = getZoomCenterHistogramIndex(event);
                mode = ZOOM;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MOVE) {
                    float difX = moveDownPointF.x - event.getX();
                    moveOffest = Math.abs((int) (difX * showHistogramNums / coordinateWidth));
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
                        //计算最大最小值
                        calculateData();
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
            if ((drawHistogramIndex + showHistogramNums + moveOffest) <= dataList.size() - 1) {
                drawHistogramIndex += moveOffest;
            } else {
                drawHistogramIndex = dataList.size() - showHistogramNums;
                if (drawHistogramIndex < 0) {
                    drawHistogramIndex = 0;
                }
            }
        } else if (difX < 0) {//手指向右移动
            drawHistogramIndex = drawHistogramIndex - moveOffest;
            if (drawHistogramIndex < 0) {
                drawHistogramIndex = 0;
            }
        }
    }

    /**
     * 计算坐标极值
     */
    private void calculateData() {
        if (drawHistogramIndex >= 0 && dataList.size() > drawHistogramIndex && isCalculateDataExtremum) {
            float min = dataList.get(drawHistogramIndex).turnover;
            float max = dataList.get(drawHistogramIndex).turnover;
            for (int i = drawHistogramIndex + 1; i < drawHistogramIndex + showHistogramNums && i < dataList.size(); i++) {
                float value = dataList.get(i).turnover;
                min = value < min && value > 0 ? value : min;
                max = max > value ? max : value;
            }
            YMax = max;
            YMin = min;
        }
    }

    /**
     * 放大
     *
     * @return 表示是否进行了放大, true代表showHistogramNums进行了--;
     */
    private boolean zoomIn() {
        if (showHistogramNums >= minHistogramNums) {
            //减少蜡烛根数
            showHistogramNums = showHistogramNums - incremental;
            showHistogramNums = showHistogramNums < minHistogramNums ? minHistogramNums : showHistogramNums;
            return true;
        } else {
            //此时显示的蜡烛数应该等于最小蜡烛数
            showHistogramNums = minHistogramNums;
            return false;
        }
    }

    /**
     * 缩小
     *
     * @return 标识是否进行了缩小, true代表showHistogramNums进行了++;
     */
    private boolean zoomOut() {
        if (showHistogramNums <= defaultShowHistogramNums) {
            //增加蜡烛根数
            showHistogramNums = showHistogramNums + incremental;
            showHistogramNums = showHistogramNums > defaultShowHistogramNums ? defaultShowHistogramNums : showHistogramNums;
            return true;
        } else {
            showHistogramNums = defaultShowHistogramNums;
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
        int leftIndex = (int) ((pointLeft * showHistogramNums) / coordinateWidth);
        int rightIndex = (int) ((pointRight * showHistogramNums) / coordinateWidth);
        //得到两只之间的蜡烛相对于总显示根数的根数
        int centerHistogramNums = (rightIndex - leftIndex) / 2 + leftIndex;
        int zoomIndex = drawHistogramIndex + centerHistogramNums;
        return zoomIndex;
    }

    /**
     * 计算绘画蜡烛的起始值
     */
    private void calculateDrawHistogramIndex(MotionEvent event, int zoomType) {
        //计算左边应消失的根数,从而改变了右边消失的根数,因为总消失根数不变
        int zoomHistogramIndexTemp = getZoomCenterHistogramIndex(event);

        if (zoomType == 1) { //放大
            if (zoomHistogramIndexTemp - zoomHistogramIndex > 0) {
                //目标左移,需要向右纠正,不改变绘图起始坐标,就会让图右移,因为显示条数在变少
            } else if (zoomHistogramIndexTemp - zoomHistogramIndex < 0) {
                //目标右移,需要向左纠正
                drawHistogramIndex = drawHistogramIndex + incremental;
            }
        } else if (zoomType == -1) {//缩小
            if (zoomHistogramIndexTemp - zoomHistogramIndex > 0) {
                //目标左移,需要向右纠正
                drawHistogramIndex = drawHistogramIndex - incremental;
            } else if (zoomHistogramIndexTemp - zoomHistogramIndex < 0) {
                //目标右移,需要向左纠正,不改变绘图其实坐标,就会让图左移,因为现实条数增多
            }
        }
        //越界判断
        drawHistogramIndex = drawHistogramIndex >= dataList.size() ? dataList.size() - 1 : drawHistogramIndex;
        drawHistogramIndex = drawHistogramIndex < 0 ? 0 : drawHistogramIndex;

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

    public List<HistogramBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<HistogramBean> dataList) {
        this.dataList = dataList;
    }

    public boolean isFill() {
        return isFill;
    }

    public int getDrawHistogramIndex() {
        return drawHistogramIndex;
    }

    public void setDrawHistogramIndex(int drawHistogramIndex) {
        this.drawHistogramIndex = drawHistogramIndex;
    }

    public int getShowHistogramNums() {
        return showHistogramNums;
    }

    public int getMinHistogramNums() {
        return minHistogramNums;
    }

    public void setMinHistogramNums(int minHistogramNums) {
        this.minHistogramNums = minHistogramNums;
    }

    public int getDefaultShowHistogramNums() {
        return defaultShowHistogramNums;
    }

    public void setDefaultShowHistogramNums(int defaultShowHistogramNums) {
        this.defaultShowHistogramNums = defaultShowHistogramNums;
    }

    public void setShowHistogramNums(int showHistogramNums) {
        this.showHistogramNums = showHistogramNums;
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
}
