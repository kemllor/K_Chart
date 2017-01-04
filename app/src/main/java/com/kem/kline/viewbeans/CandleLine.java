package com.kem.kline.viewbeans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：蜡烛线
 *
 * @author zhaoyi
 * @date   2016-11-7
 */
public class CandleLine extends ViewContainer {
    //蜡烛画笔
    private Paint candlePaint = null;
    //是否填充
    private boolean isFill = true;
    //显示的蜡烛数
    private int showCandleNums = 45;
    //最少显示的蜡烛数
    private int minCandleNums = 30;
    //默认显示条数
    private int defaultShowCandleNums = 0;
    //从第几根蜡烛开始画
    private int drawCandleIndex = 0;
    //放大缩小中心蜡烛下标
    private int zoomCandleIndex = 0;
    //数据集
    private List<CandleLineBean> dataList = new ArrayList<CandleLineBean>();
    //涨时颜色
    private int upColor = Color.parseColor("#ff322e");
    //跌时颜色
    private int downColor = Color.parseColor("#2eff2e");
    //不涨不跌颜色
    private int evenColor = Color.parseColor("#656565");
    //蜡烛宽度
    private float candleWidth;
    //柱之间间隙
    private float space = 0f;
    //两指间间隙
    private float distance = 0f;
    //是否正在放大
    private boolean isZooming = false;
    //是否计算极值(有时元素极值并不是元素本身数据的极值,有可能是其他数据极值)
    private boolean isCalculateDataExtremum = true;

    private CandleZoomMoveCallBack candleZoomMoveCallBack;

    public interface CandleZoomMoveCallBack {

        public void calacCoordinates();

        public void pageLoadChart();
    }

    public CandleLine(float YMin, float YMax,Context context) {
        super(context);
        this.YMin = YMin;
        this.YMax = YMax;
        initPaint();
    }

    public CandleLine(Context context) {
        super(context);
        initPaint();
    }

    //初始化画笔
    private void initPaint() {
        candlePaint = new Paint();
        candlePaint.setAntiAlias(true);
        candlePaint.setStrokeWidth(1.5f);
        candlePaint.setColor(Color.BLACK);
        candlePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            if (isShow) {
                //每根蜡烛的宽度
                candleWidth = coordinateWidth / showCandleNums;
                //计算间隙
                space = candleWidth / 10;
                checkParamter();
                int j = 0;
                for (int i = drawCandleIndex; i < dataList.size(); i++) {
                    drawCandle(dataList.get(i), j, canvas);
                    j++;
                }
            }
        } catch (Exception e) {
        }
    }

    //绘制蜡烛一根
    private void drawCandle(CandleLineBean candleLineBean, int i, Canvas canvas) {
        //从收盘价与开盘价之间比较出最大值
        float maxPrice = candleLineBean.closePrice >= candleLineBean.openPrice ?
                candleLineBean.closePrice :
                candleLineBean.openPrice;
        //从收盘价与开盘价之间比较出最小值
        float minPrice = candleLineBean.closePrice <= candleLineBean.openPrice ?
                candleLineBean.closePrice :
                candleLineBean.openPrice;
        //防止画出坐标系外
        if (minPrice <= 0) {
            minPrice = YMin;
        }
        //计算出蜡烛顶端尖尖的Y轴坐标
        float y1 = (1f - (candleLineBean.heightPrice - YMin) / (YMax - YMin)) * coordinateHeight;
        //计算出蜡烛顶端横线的Y轴坐标
        float y2 = (1f - (maxPrice - YMin) / (YMax - YMin)) * coordinateHeight;
        //计算出蜡烛底端横线的Y轴坐标
        float y3 = (1f - (minPrice - YMin) / (YMax - YMin)) * coordinateHeight;
        //计算出蜡烛底端尖尖的Y轴坐标
        float y4 = (1f - (candleLineBean.lowPrice - YMin) / (YMax - YMin)) * coordinateHeight;

        if (candleLineBean.openPrice < candleLineBean.closePrice) {//红
            candlePaint.setColor(upColor);
        } else if (candleLineBean.openPrice > candleLineBean.closePrice) {//绿
            candlePaint.setColor(downColor);
        } else {//黑
            candlePaint.setColor(evenColor);
        }
        //进行绘画
        Rect rect = new Rect();
        if (y2 == y3||Math.abs(y2-y3)<1) {
            rect.set((int) (i * candleWidth + space), (int) y2,
                    (int) (i * candleWidth + candleWidth - space), (int) y2 + 1);
            //当开盘与收盘相等时,颜色就要取决于上一根昨收比
            if (i > 0) {
                CandleLineBean preCandleLineBean = dataList.get(i - 1);
                if (candleLineBean.openPrice > preCandleLineBean.closePrice) {
                    candlePaint.setColor(upColor);
                } else if (candleLineBean.openPrice < preCandleLineBean.closePrice) {
                    candlePaint.setColor(downColor);
                }
            }
        } else {
            rect.set((int) (i * candleWidth + space), (int) y2,
                    (int) (i * candleWidth + candleWidth - space), (int) y3);
        }
        //画蜡烛的方块主干
        canvas.drawRect(rect, candlePaint);
        //画蜡烛的上尖尖
        canvas.drawLine((i * candleWidth) + candleWidth / 2, y1,
                (i * candleWidth) + candleWidth / 2, y2, candlePaint);
        //画蜡烛的下尖尖
        canvas.drawLine((i * candleWidth) + candleWidth / 2, y3,
                (i * candleWidth) + candleWidth / 2, y4, candlePaint);
    }

    private void checkParamter() {
        if (this.coordinateHeight <= 0) {
            throw new IllegalArgumentException(
                    "coordinateHeight can't be zero or smaller than zero");
        }
        if (this.coordinateWidth <= 0) {
            throw new IllegalArgumentException(
                    "coordinateWidth can't be zero or smaller than zero");
        }
    }

    public void setFill(boolean isFill) {
        this.isFill = isFill;
        if (this.isFill) {
            candlePaint.setStyle(Paint.Style.FILL);
        } else {
            candlePaint.setStyle(Paint.Style.STROKE);
        }
    }

    public void setColor(int upColor, int evenColor, int downColor) {
        this.upColor = upColor;
        this.downColor = downColor;
        this.evenColor = evenColor;
    }

    private static final int NONE = 0;
    private static final int MOVE = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF moveDownPointF = new PointF();
    private float difX = 0f;
    private long time;
    private long lastTime;
    private final float ACCELERATE = 0.001F;
    private float XBeforeActionUp;
    private float x;
    private final float MAX_VELOCITY = 2;
    private float accelerate;//像素点/ms^2 带方向
    private float velocity;
    private int offsetPerTimeInterval;
    private final long onDrawTimeInterval = 100;
    private int cycle;
    private int moveOffest;

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                moveDownPointF.x = event.getX();
                moveDownPointF.y = event.getY();
                mode = MOVE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                zoomCandleIndex = getZoomCenterCandleIndex(event);
                mode = ZOOM;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MOVE) {
                    difX = moveDownPointF.x - event.getX();
                    moveOffest = Math.abs((int) (difX * showCandleNums / coordinateWidth));
                    if (moveOffest >= 1) {
                        move(difX);
                        if (candleZoomMoveCallBack != null) {
                            if (drawCandleIndex >= 0) {
                                candleZoomMoveCallBack.calacCoordinates();
                            }
                        }
                        moveDownPointF.x = event.getX();
                        moveDownPointF.y = event.getY();
                    }
                    XBeforeActionUp = x;
                    lastTime = time;
                    time = System.currentTimeMillis();
                    x = event.getX();

                } else if (mode == ZOOM) {
                    float spacing = spacing(event) - distance;
                    if (Math.abs(spacing) >= MIN_FINGER_DISTANCE) {
                        distance = spacing(event);
                        if (spacing < 0) {
                            //缩小
                            if (zoomOut())
                                calculateDrawCandleIndex(event, -1);//-1代表了缩小
                        } else {
                            //放大
                            if (zoomIn())
                                calculateDrawCandleIndex(event, 1);//1代表了放大
                        }
                        //计算最大最小值
                        calculateData();
                        if (candleZoomMoveCallBack != null) {
                            candleZoomMoveCallBack.calacCoordinates();
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;

            case MotionEvent.ACTION_UP:
                if (mode == MOVE) {
                    if (candleZoomMoveCallBack != null) {
                        if (difX < 0) {
                            if (drawCandleIndex == 0 && dataList.size() >= 95) {
                                candleZoomMoveCallBack.pageLoadChart();
                            }
                        }
                    }
                }
                break;

        }
    }

    /**
     * 得到放大缩小的中心蜡烛下标
     *
     * @param event
     * @return
     */
    private int getZoomCenterCandleIndex(MotionEvent event) {
        //计算放大中心
        float pointLeft = event.getX(0) < event.getX(1) ? event.getX(0) : event.getX(1);
        float pointRight = event.getX(0) > event.getX(1) ? event.getX(0) : event.getX(1);
        int leftIndex = (int) ((pointLeft * showCandleNums) / coordinateWidth);
        int rightIndex = (int) ((pointRight * showCandleNums) / coordinateWidth);
        //得到两只之间的蜡烛相对于总显示根数的根数
        int centerCandleNums = (rightIndex - leftIndex) / 2 + leftIndex;
        int zoomIndex = drawCandleIndex + centerCandleNums;
        return zoomIndex;
    }

    /**
     * 计算绘画蜡烛的起始值
     */
    private void calculateDrawCandleIndex(MotionEvent event, int zoomType) {
        //计算左边应消失的根数,从而改变了右边消失的根数,因为总消失根数不变
        int zoomCandleIndexTemp = getZoomCenterCandleIndex(event);

        if (zoomType == 1) { //放大
            if (zoomCandleIndexTemp - zoomCandleIndex > 0) {
                //目标左移,需要向右纠正,不改变绘图起始坐标,就会让图右移,因为显示条数在变少
            } else if (zoomCandleIndexTemp - zoomCandleIndex < 0) {
                //目标右移,需要向左纠正
                drawCandleIndex = drawCandleIndex + incremental;
            }
        } else if (zoomType == -1) {//缩小
            if (zoomCandleIndexTemp - zoomCandleIndex > 0) {
                //目标左移,需要向右纠正
                drawCandleIndex = drawCandleIndex - incremental;
            } else if (zoomCandleIndexTemp - zoomCandleIndex < 0) {
                //目标右移,需要向左纠正,不改变绘图其实坐标,就会让图左移,因为显示条数增多
            }
        }
        //越界判断
        drawCandleIndex =
                drawCandleIndex >= dataList.size() ? dataList.size() - 1 : drawCandleIndex;
        drawCandleIndex = drawCandleIndex < 0 ? 0 : drawCandleIndex;

    }

    /**
     * 计算坐标极值
     */
    private void calculateData() {
        if (drawCandleIndex >= 0 && dataList.size() > drawCandleIndex && isCalculateDataExtremum) {
            float min = dataList.get(drawCandleIndex).getLowPrice();
            float max = dataList.get(drawCandleIndex).getHeightPrice();
            for (int i = drawCandleIndex + 1;
                 i < drawCandleIndex + showCandleNums && i < dataList.size(); i++) {
                CandleLineBean entity = dataList.get(i);
                min = entity.getLowPrice() < min && entity.getLowPrice() > 0 ?
                        entity.getLowPrice() :
                        min;
                max = max > entity.getHeightPrice() ? max : entity.getHeightPrice();
            }
            YMax = max;
            YMin = min;
        }
    }

    /**
     * 放大
     *
     * @return 表示是否进行了放大, true代表showCandleNums进行了--;
     */
    private boolean zoomIn() {
        if (showCandleNums >= minCandleNums) {
            //减少蜡烛根数
            showCandleNums = showCandleNums - incremental;
            showCandleNums = showCandleNums < minCandleNums ? minCandleNums : showCandleNums;
            return true;
        } else {
            //此时显示的蜡烛数应该等于最小蜡烛数
            showCandleNums = minCandleNums;
            return false;
        }
    }

    /**
     * 缩小
     *
     * @return 标识是否进行了缩小, true代表showCandleNums进行了++;
     */
    private boolean zoomOut() {
        if (showCandleNums <= defaultShowCandleNums) {
            //增加蜡烛根数
            showCandleNums = showCandleNums + incremental;
            showCandleNums =
                    showCandleNums > defaultShowCandleNums ? defaultShowCandleNums : showCandleNums;
            return true;
        } else {
            showCandleNums = defaultShowCandleNums;
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
     *动 移
     */
    private void move(float difX) {
        if (difX > 0) {//手指向左移动
            if ((drawCandleIndex + showCandleNums + moveOffest) <= dataList.size() - 1) {
                drawCandleIndex += moveOffest;
            } else {
                drawCandleIndex = dataList.size() - showCandleNums;
                if (drawCandleIndex < 0) {
                    drawCandleIndex = 0;
                }
            }
        } else if (difX < 0) {//手指向右移动
            drawCandleIndex = drawCandleIndex - moveOffest;
            if (drawCandleIndex < 0) {
                drawCandleIndex = 0;
            }
        }
    }

    public int getShowCandleNums() {
        return showCandleNums;
    }

    public int getDefaultShowCandleNums() {
        return defaultShowCandleNums;
    }

    public void setDefaultShowCandleNums(int defaultShowCandleNums) {
        this.defaultShowCandleNums = defaultShowCandleNums;
    }

    public void setShowCandleNums(int showCandleNums) {
        this.showCandleNums = showCandleNums;
    }

    public int getMinCandleNums() {
        return minCandleNums;
    }

    public void setMinCandleNums(int minCandleNums) {
        this.minCandleNums = minCandleNums;
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

    public List<CandleLineBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<CandleLineBean> dataList) {
        this.dataList = dataList;
    }

    public void setDrawCandleIndex(int drawCandleIndex) {
        this.drawCandleIndex = drawCandleIndex;
    }

    public int getDrawCandleIndex() {
        return drawCandleIndex;
    }

    public void setCandleZoomMoveCallBack(CandleZoomMoveCallBack candleZoomMoveCallBack) {
        this.candleZoomMoveCallBack = candleZoomMoveCallBack;
    }

    /**
     * 蜡烛数据信息
     */
    public static class CandleLineBean implements Cloneable {
        public CandleLineBean() {

        }

        public CandleLineBean(int index, float heightPrice, float lowPrice, float openPrice,
                              float closePrice, long turnover) {
            this.index = index;
            this.heightPrice = heightPrice;
            this.lowPrice = lowPrice;
            this.openPrice = openPrice;
            this.closePrice = closePrice;
            this.turnover = turnover;
        }

        //下标
        private int index = -1;
        //最高价
        private float heightPrice = 0.0f;
        //最低价
        private float lowPrice = 0.0f;
        //开盘价
        private float openPrice = 0.0f;
        //收盘价
        private float closePrice = 0.0f;
        //成交量
        private float turnover = 0.0f;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public float getHeightPrice() {
            return heightPrice;
        }

        public void setHeightPrice(float heightPrice) {
            this.heightPrice = heightPrice;
        }

        public float getLowPrice() {
            return lowPrice;
        }

        public void setLowPrice(float lowPrice) {
            this.lowPrice = lowPrice;
        }

        public float getOpenPrice() {
            return openPrice;
        }

        public void setOpenPrice(float openPrice) {
            this.openPrice = openPrice;
        }

        public float getClosePrice() {
            return closePrice;
        }

        public void setClosePrice(float closePrice) {
            this.closePrice = closePrice;
        }

        public float getTurnover() {
            return turnover;
        }

        public void setTurnover(float turnover) {
            this.turnover = turnover;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            CandleLineBean bean = new CandleLineBean();
            bean.setLowPrice(this.getLowPrice());
            bean.setClosePrice(this.getClosePrice());
            bean.setHeightPrice(this.getHeightPrice());
            bean.setOpenPrice(this.getOpenPrice());
            bean.setIndex(this.getIndex());
            bean.setTurnover(this.getTurnover());
            return bean;
        }
    }

    public float getCandleWidth() {
        return candleWidth;
    }

    public int getCycle() {
        return cycle;
    }

    public int getOffsetPerTimeInterval() {
        return offsetPerTimeInterval;
    }

    public void setCalculateDataExtremum(boolean isCalculateDataExtremum) {
        this.isCalculateDataExtremum = isCalculateDataExtremum;
    }
}
