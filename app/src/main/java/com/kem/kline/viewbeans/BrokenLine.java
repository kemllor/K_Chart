package com.kem.kline.viewbeans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.view.MotionEvent;

import com.kem.kline.views.CanvasView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：折线
 *
 * @author zhaoyi
 * @date   2016-11-7
 */
public class BrokenLine extends ViewContainer {
    //线画笔
    private Paint linePaint = null;
    //背景色画笔
    private Paint backgroundPaint = null;
    //是否填充
    private boolean isFill = true;
    //数据集
    private List<String> dataList = new ArrayList<String>();
    //从第几个点开始画
    private int drawPointIndex = 0;
    //显示的点数
    private int showPointNums = 100;
    //最少显示的点数
    private int minPointNums = 30;
    //默认显示点数
    private int defaultShowPointNums = 2;
    //渐变开始颜色
    private int startColor = Color.WHITE;
    //渐变结束颜色
    private int endColor = Color.BLACK;
    //是否正在缩放
    private boolean isZooming = false;
    //两指间距离
    private float distance = 0f;
    //缩放的中心点下标
    private int zoomPointIndex = 0;
    //是否计算极值(有时元素极值并不是元素本身数据的极值,有可能是其他数据极值)
    private boolean isCalculateDataExtremum = true;
    //点宽度
    private float xPointWidth;

    /**
     * 折线
     *
     * @param YMax 坐标系中最大值
     * @param YMin 坐标系中最小值
     */
    public BrokenLine(float YMax, float YMin) {
        this.YMax = YMax;
        this.YMin = YMin;
        //初始化线画笔
        initPaint();
    }

    public BrokenLine() {
        //初始化线画笔
        initPaint();
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            if (isShow) {
                xPointWidth = coordinateWidth / showPointNums;
                boolean isFirstDraw = true;
                checkParamter();
                Path backgroundPath = new Path();
                Path linePath = new Path();
                if (isFill) {
                    backgroundPath.moveTo(0, coordinateHeight);
                }
                PointF point = null;
                int j = 0;
                for (int i = drawPointIndex; i < dataList.size(); i++) {
                    try {
                        if (Float.parseFloat(dataList.get(i)) == 0) {
                            j++;
                            continue;
                        }
                        point = getCoordinatePoint(i, j);
//                        if (point.x == 0 && point.y == 0) {
//                            continue;
//                        }
                    } catch (Exception e) {
                        continue;
                    }

                    //如果第一次画,就把第一次作为起始点
                    if (isFirstDraw) {
                        linePath.moveTo(point.x, point.y);
                        isFirstDraw = false;
                    } else {
                        linePath.lineTo(point.x, point.y);
                    }
                    if (isFill) {
                        backgroundPath.lineTo(point.x, point.y);
                    }
                    j++;
                }

                //背景
                if (isFill) {
                    if (point != null) {
                        backgroundPath.lineTo(point.x, coordinateHeight);
                    }
                    backgroundPath.close();
                    canvas.drawPath(backgroundPath, backgroundPaint);
                }

                //画线
                linePaint.setPathEffect(new CornerPathEffect(20));
                canvas.drawPath(linePath, linePaint);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 得到坐标系中点
     *
     * @param dataIndex 数据下标
     * @param i         屏幕上的点的个数下标
     * @return
     */
    private PointF getCoordinatePoint(int dataIndex, int i) {
        PointF pointF = new PointF();
        String str = dataList.get(dataIndex);
        float x;
        if (chartShowType == CanvasView.ChartShowType.CANDLE_DAY || chartShowType == CanvasView.ChartShowType.MACD || chartShowType == CanvasView.ChartShowType.KDJ ||
                chartShowType == CanvasView.ChartShowType.OBV || chartShowType == CanvasView.ChartShowType.WR || chartShowType == CanvasView.ChartShowType.BOLL) {
            if (i == 0) {
                x = i * xPointWidth + xPointWidth / 2;
            } else {
                x = i * xPointWidth + xPointWidth;
            }
        } else {
            x = i * xPointWidth;
        }
        float y = (1f - (Float.parseFloat(str) - YMin) / (YMax - YMin)) * coordinateHeight;
        pointF.set(x, y);
        return pointF;
    }

    /**
     * 设置线的颜色,要求在调用draw之前调用
     *
     * @param color 颜色色值
     */
    public void setLineColor(int color) {
        linePaint.setColor(color);
    }

    /**
     * 设置填充后的颜色及透明度
     *
     * @param startColor 开始颜色
     * @param endColor   结束颜色
     * @param alpha      透明度0...255
     */
    public void setLineFillColor(int startColor, int endColor, int alpha) {
        isFill = true;
        this.startColor = startColor;
        this.endColor = endColor;
        backgroundPaint.setAlpha(alpha);
    }

    /**
     * 设置填充后的颜色及透明度
     *
     * @param fillColor 填充颜色颜色
     * @param alpha     透明度0...255
     */
    public void setLineFillColor(int fillColor, int alpha) {
        setLineFillColor(fillColor, fillColor, alpha);
    }

    private void initPaint() {
        //初始化线画笔
        this.linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2f);
        linePaint.setColor(Color.BLACK);
        //初始化背景画笔
        this.backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
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
    private int mode = NONE;
    private PointF moveDownPointF = new PointF();
    private int moveOffest;

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                moveDownPointF.x = event.getX();
                moveDownPointF.y = event.getY();
                mode = MOVE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                zoomPointIndex = getZoomCenterPointIndex(event);
                mode = ZOOM;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MOVE) {
                    float difX = moveDownPointF.x - event.getX();
                    moveOffest = Math.abs((int) (difX * showPointNums / coordinateWidth));
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
                                calculateDrawPointIndex(event, -1);//-1代表了缩小
                        } else {
                            //放大
                            if (zoomIn())
                                calculateDrawPointIndex(event, 1);//1代表了放大
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
            if ((drawPointIndex + showPointNums + moveOffest) <= dataList.size() - 1) {
                drawPointIndex += moveOffest;
            } else {
                drawPointIndex = dataList.size() - showPointNums;
                if (drawPointIndex < 0) {
                    drawPointIndex = 0;
                }
            }
        } else if (difX < 0) {//手指向右移动
            drawPointIndex = drawPointIndex - moveOffest;
            if (drawPointIndex < 0) {
                drawPointIndex = 0;
            }
        }
    }

    /**
     * 计算坐标极值
     */
    private void calculateData() {
        if (drawPointIndex >= 0 && dataList.size() > drawPointIndex && isCalculateDataExtremum) {
            float min = Float.parseFloat(dataList.get(drawPointIndex));
            float max = Float.parseFloat(dataList.get(drawPointIndex));
            for (int i = drawPointIndex + 1; i < drawPointIndex + showPointNums && i < dataList.size(); i++) {
                float value = Float.parseFloat(dataList.get(i));
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
     * @return 表示是否进行了放大, true代表showPointNums进行了--;
     */
    private boolean zoomIn() {
        if (showPointNums >= minPointNums) {
            //减少蜡烛根数
            showPointNums = showPointNums - incremental;
            showPointNums = showPointNums < minPointNums ? minPointNums : showPointNums;
            return true;
        } else {
            //此时显示的蜡烛数应该等于最小蜡烛数
            showPointNums = minPointNums;
            return false;
        }
    }

    /**
     * 缩小
     *
     * @return 标识是否进行了缩小, true代表showPointNums进行了++;
     */
    private boolean zoomOut() {
        if (showPointNums <= defaultShowPointNums) {
            //增加蜡烛根数
            showPointNums = showPointNums + incremental;
            showPointNums = showPointNums > defaultShowPointNums ? defaultShowPointNums : showPointNums;
            return true;
        } else {
            showPointNums = defaultShowPointNums;
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
    private int getZoomCenterPointIndex(MotionEvent event) {
        //计算放大中心
        float pointLeft = event.getX(0) < event.getX(1) ? event.getX(0) : event.getX(1);
        float pointRight = event.getX(0) > event.getX(1) ? event.getX(0) : event.getX(1);
        int leftIndex = (int) ((pointLeft * showPointNums) / coordinateWidth);
        int rightIndex = (int) ((pointRight * showPointNums) / coordinateWidth);
        //得到两只之间的蜡烛相对于总显示根数的根数
        int centerPointNums = (rightIndex - leftIndex) / 2 + leftIndex;
        int zoomIndex = drawPointIndex + centerPointNums;
        return zoomIndex;
    }

    /**
     * 计算绘画蜡烛的起始值
     */
    private void calculateDrawPointIndex(MotionEvent event, int zoomType) {
        //计算左边应消失的根数,从而改变了右边消失的根数,因为总消失根数不变
        int zoomPointIndexTemp = getZoomCenterPointIndex(event);

        if (zoomType == 1) { //放大
            if (zoomPointIndexTemp - zoomPointIndex > 0) {
                //目标左移,需要向右纠正,不改变绘图起始坐标,就会让图右移,因为显示条数在变少
            } else if (zoomPointIndexTemp - zoomPointIndex < 0) {
                //目标右移,需要向左纠正
                drawPointIndex = drawPointIndex + incremental;
            }
        } else if (zoomType == -1) {//缩小
            if (zoomPointIndexTemp - zoomPointIndex > 0) {
                //目标左移,需要向右纠正
                drawPointIndex = drawPointIndex - incremental;
            } else if (zoomPointIndexTemp - zoomPointIndex < 0) {
                //目标右移,需要向左纠正,不改变绘图其实坐标,就会让图左移,因为现实条数增多
            }
        }
        //越界判断
        drawPointIndex = drawPointIndex >= dataList.size() ? dataList.size() - 1 : drawPointIndex;
        drawPointIndex = drawPointIndex < 0 ? 0 : drawPointIndex;

    }

    public void setCoordinateHeight(float coordinateHeight) {
        super.setCoordinateHeight(coordinateHeight);
        //获得高度后,设置渐变颜色
        LinearGradient lg = new LinearGradient(0, 0, 0, coordinateHeight, startColor, endColor, Shader.TileMode.MIRROR);
        backgroundPaint.setShader(lg);
    }

    public void setCoordinateWidth(float coordinateWidth) {
        super.setCoordinateWidth(coordinateWidth);
        //TODO:do noting
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public void setFill(boolean isFill) {
        this.isFill = isFill;
    }

    public boolean isFill() {
        return isFill;
    }

    public int getDrawPointIndex() {
        return drawPointIndex;
    }

    public void setDrawPointIndex(int drawPointIndex) {
        this.drawPointIndex = drawPointIndex;
    }

    public int getShowPointNums() {
        return showPointNums;
    }

    public int getMinPointNums() {
        return minPointNums;
    }

    public void setMinPointNums(int minPointNums) {
        this.minPointNums = minPointNums;
    }

    public int getDefaultShowPointNums() {
        return defaultShowPointNums;
    }

    public void setDefaultShowPointNums(int defaultShowPointNums) {
        this.defaultShowPointNums = defaultShowPointNums;
    }

    public void setShowPointNums(int showPointNums) {
        this.showPointNums = showPointNums;
    }

    public boolean isCalculateDataExtremum() {
        return isCalculateDataExtremum;
    }

    public void setCalculateDataExtremum(boolean isCalculateDataExtremum) {
        this.isCalculateDataExtremum = isCalculateDataExtremum;
    }

}
