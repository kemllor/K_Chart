package com.kem.kline.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kem.kline.viewbeans.CandleLine;
import com.kem.kline.viewbeans.ChartView;
import com.kem.kline.viewbeans.Coordinates;
import com.kem.kline.viewbeans.CrossLine;
import com.kem.kline.viewbeans.ViewContainer;

import java.util.Iterator;
import java.util.List;

/**
 * 描述：绘图的View
 *
 * @author zhaoyi
 * @date 2016-11-7
 */
public class CanvasView extends View implements ChartView {

    private boolean isDoubleBuffer = false;

    public class ChartShowType {
        public static final int TIME_DAY = 1;
        public static final int CANDLE_DAY = 2;
        public static final int VOLUM = 3;
        public static final int KDJ = 4;
        public static final int MACD = 5;
        public static final int DMI = 6;
        public static final int WR = 7;
        public static final int OBV = 8;
        public static final int BOLL = 9;
        public static final int RSI = 10;
    }

    private Context context = null;
    //==============================绘图控件对象区=============================
    //画布容器
    private ViewContainer viewContainer = null;
    //坐标系
    private Coordinates coordinates = null;
    //十字线
    private CrossLine crossLine = null;
    //坐标系的最大值
    private float YMax = 0;
    //坐标系的最小值
    private float YMin = 0;
    //双缓冲内存空间
    private Bitmap bufferBmp = null;
    //双缓冲绘图画布
    private Canvas bufferCanvas = null;

    private int width;
    private int height;

    public CanvasView(Context context) {
        super(context);
        this.context = context;
        initObject();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initObject();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initObject();
    }

    /**
     * 初始化必要对象
     */
    final private void initObject() {
        this.setBackgroundColor(0xffffffff);
        viewContainer = new ViewContainer();//所有控件承载体
        coordinates = new Coordinates(context);//坐标系
        crossLine = new CrossLine();//十字线
        viewContainer.addChildren(coordinates);//把坐标系添加到控件承载体

    }

    /**
     * 加入组件
     */
    final public void addChildren(ViewContainer vc) {
        viewContainer.addChildren(vc);
        setCoordinateHeightAndWidth(vc, this.getMeasuredWidth(), this.getMeasuredHeight());
        //传入坐标系最高最低
        setYMax(this.YMax);
        setYMin(this.YMin);
    }

    /**
     * 删除组件
     */
    final public void removeChildren(ViewContainer vc) {
        if (!vc.equals(coordinates) && !vc.equals(crossLine)) {
            viewContainer.removeChildren(vc);
        }
    }

    /**
     * 删除所有组件
     */
    final public void removeAllChildren() {
        Iterator it = viewContainer.getChildrenList().iterator();
        while (it.hasNext()) {
            ViewContainer vc = (ViewContainer) it.next();
            if (!vc.equals(coordinates) && !vc.equals(crossLine)) {
                it.remove();
            }
        }
    }

    /**
     * 给组件设置画布的宽高
     *
     * @param width
     * @param height
     */
    final private void setCoordinateHeightAndWidth(int width, int height) {
        //初始化高度
        for (ViewContainer vc : viewContainer.getChildrenList()) {
            setCoordinateHeightAndWidth(vc, width, height);
        }
    }

    /**
     * 给组件设置画布的宽高
     *
     * @param vc
     * @param width
     * @param height
     */
    final private void setCoordinateHeightAndWidth(ViewContainer vc, int width, int height) {
        height = height - Coordinates.MARGIN_BOTTOM;//为底部留出空隙
        vc.setCoordinateHeight(height);
        vc.setCoordinateWidth(width);
    }

    /**
     * 设置坐标系最大表示数值
     *
     * @param YMax
     */
    final public void setYMax(float YMax) {
        //留出顶部空隙
        this.YMax = YMax;
        List<ViewContainer> childrenList = viewContainer.getChildrenList();
        for (ViewContainer viewContainer1 : childrenList) {
            viewContainer1.setYMax(this.YMax);
        }
        crossLine.setYMax(this.YMax);
    }

    /**
     * 设置坐标系最小表示数值
     *
     * @param YMin
     */
    final public void setYMin(float YMin) {
        //留出底部空隙
        this.YMin = YMin;
        List<ViewContainer> childrenList = viewContainer.getChildrenList();
        for (ViewContainer viewContainer1 : childrenList) {
            viewContainer1.setYMin(this.YMin);
        }
        crossLine.setYMin(this.YMin);
    }

    /**
     * 设置坐标系底部标识数组
     *
     * @param bottomScaleTextArray
     */
    public void setBottomScaleTextArray(String[] bottomScaleTextArray) {
        coordinates.setBottomScaleTextArray(bottomScaleTextArray);
    }

    /**
     * 设置坐标系左侧标识数组
     *
     * @param leftScaleTextArray
     */
    public void setLeftScaleTextArray(String[] leftScaleTextArray) {
        coordinates.setLeftScaleTextArray(leftScaleTextArray);
    }

    /**
     * 设置坐标系右侧标识数组
     *
     * @param rightScaleTextArray
     */
    public void setRightScaleTextArray(String[] rightScaleTextArray) {
        coordinates.setRightScaleTextArray(rightScaleTextArray);
    }

    @Override
    final protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = this.getMeasuredWidth();
        height = this.getMeasuredHeight();
        setCoordinateHeightAndWidth(width, height);
        setCoordinateHeightAndWidth(crossLine, width, height);
        if (isDoubleBuffer) {
            // destoryBufferPaintCanvas();
            createBufferPaintCanvas();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 销毁双缓冲画布
     */
    public void destoryBufferPaintCanvas() {
        if (bufferBmp != null) {
            bufferBmp.recycle();
            bufferBmp = null;
        }
        System.gc();
    }

    /**
     * 创建双缓冲画布
     */
    final private void createBufferPaintCanvas() {
        try {
            bufferBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bufferCanvas = new Canvas(bufferBmp);

        } catch (Throwable e) {

        }

    }


    @Override
    final protected void onDraw(Canvas canvas) {
        try {
            //双缓冲画法
            if (isDoubleBuffer && bufferBmp != null) {
                Paint paint = new Paint();
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                bufferCanvas.drawPaint(paint);
                viewContainer.draw(bufferCanvas);
                crossLine.draw(bufferCanvas);
                canvas.drawBitmap(bufferBmp, 0, 0, null);
            } else {
                viewContainer.draw(canvas);
                crossLine.draw(canvas);
            }
        } catch (Throwable e) {

        }
    }

    /**
     * 十字线手指触摸事件
     *
     * @param event
     */
    public void crossLineMove(MotionEvent event) {
        crossLine.setShow(true);
        crossLine.move(event);
    }

    /**
     * 设置十字线是否显示；
     *
     * @param ishow
     */
    public void setCrossLineisShow(boolean ishow) {
        crossLine.setShow(ishow);
    }

    /**
     * 设置十字线分时数据源
     *
     * @param dataList
     */
    public void setCrossLinePointData(List<String> dataList) {
        crossLine.setPointList(dataList);
    }

    /**
     * 设置十字线的蜡烛数据源
     *
     * @param kList
     */
    public void setCrossLineCandleData(List<CandleLine.CandleLineBean> kList) {
        crossLine.setCandleList(kList);
    }

    /**
     * 设置屏幕展示的数目
     *
     * @param showNums
     */
    public void setCrossLineShowNums(int showNums) {
        crossLine.setShowNums(showNums);
    }

    /**
     * 设置十字线类型
     *
     * @param needLongitude
     * @param needLatitude
     * @param needPoint
     */
    public void setCrossLineStyle(boolean needLongitude, boolean needLatitude,
                                  boolean needPoint) {
        crossLine.setShowLongitude(needLongitude);
        crossLine.setShowLatitude(needLatitude);
        crossLine.setShowPoint(needPoint);
    }

    /**
     * 设置展示图表类型(线,还是蜡烛)
     *
     * @param chartShowType
     */
    public void setChartShowType(int chartShowType) {
        List<ViewContainer> childrenList = viewContainer.getChildrenList();
        for (ViewContainer viewContainer1 : childrenList) {
            viewContainer1.setChartShowType(chartShowType);
        }
        crossLine.setChartShowType(chartShowType);
    }

    /**
     * 设置十字线事件回调
     *
     * @param showCrossLineCallBack
     */
    public void setCrossLineCallBack(CrossLine.CrossLineCallBack showCrossLineCallBack) {
        crossLine.setCrossLineCallBack(showCrossLineCallBack);
    }

    /**
     * 设置屏幕显示的索引
     *
     * @param drawIndex
     */
    public void setDrawIndex(int drawIndex) {
        crossLine.setDrawIndex(drawIndex);
    }


    public void setIsDoubleBuffer(boolean isDoubleBuffer) {
        this.isDoubleBuffer = isDoubleBuffer;
    }
}
