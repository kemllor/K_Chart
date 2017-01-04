package com.kem.kline.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kem.kchart.R;
import com.kem.kline.databeans.KLineBean;
import com.kem.kline.databeans.StockDetailChartBean;
import com.kem.kline.databeans.TimeSharingBean;
import com.kem.kline.utils.NumberUtils;
import com.kem.kline.utils.UnitUtils;
import com.kem.kline.viewbeans.Boll;
import com.kem.kline.viewbeans.BrokenLine;
import com.kem.kline.viewbeans.CandleLine;
import com.kem.kline.viewbeans.CrossLine;
import com.kem.kline.viewbeans.Histogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：行情图表；
 *
 * @author zhaoyi
 * @time 2016-11-7
 */
public class HQChartView extends LinearLayout {

    /**
     * 成交量指标
     */
    public static final int VOLUM_TYPE = 1;
    /**
     * kdj指标
     */
    public static final int KDJ_TYPE = 2;
    /**
     * macd指标
     */
    public static final int MACD_TYPE = 3;
    /**
     * dmi指标
     */
    public static final int DMI_TYPE = 4;
    /**
     * wr指标
     */
    public static final int WR_TYPE = 5;
    /**
     * obv指标
     */
    public static final int OBV_TYPE = 6;
    /**
     * rsi指标
     */
    public static final int RSI_TYPE = 7;

    /**
     * boll线
     */
    public static final int BOLL_TYPE = 8;

    public enum ChartType {
        //// TODO: 2016/11/8 修改显示数量 kem
        AB_ONE_DAY(45),//深户

        GGT_ONE_DAY(330),//港股
        FF_ONE_DAY(270),//期权1
        FF_ONE_DAY2(255),//期权2
        FF_ONE_DAY3(405),//期权3
        FF_ONE_DAY4(495),//期权4
        FF_ONE_DAY5(585),//期权5
        FF_ONE_DAY6(375),//期权6
        FIVE_DAY(300),

        VERTICAL_K_DAY(45),
        VERTICAL_K_WEEK(60),
        VERTICAL_K_MONTH(60),

        LANDSPACE_K_DAY(100),
        LANDSPACE_K_WEEK(100),
        LANDSPACE_K_MONTH(100);

        private int pointNum = 0;

        private ChartType(int num) {
            this.pointNum = num;
        }

        public int getPointNum() {
            return pointNum;
        }
    }

    private Context mContext;

    private ChartType chartType = ChartType.AB_ONE_DAY;
    /**
     * 是否支持缩放和移动
     */
    private boolean isSupportTouchEvent = false;
    /**
     * 是否显示了十字线
     */
    private boolean isShowCrossLine = true;
    /**
     * 主画板
     */
    public CanvasView mainView = null;
    /**
     * 副画板
     */
    private CanvasView subView = null;

    /**
     * 加载层
     */
    private PopupWindow mLoadingPop = null;
    private TextView loadTv = null;

    /**
     * 分时价格线(元素)
     */
    private BrokenLine priceLineElement = null;
    /**
     * 价格均线(元素)
     */
    private BrokenLine averageLineElement = null;
    /**
     * 成交量线(元素)
     */
    private Histogram turnoverLineElement = null;

    /**
     * 成交量标题栏view
     */
    private View turnoverTitleView = null;
    /**
     * K线(元素)
     */
    private CandleLine kLineElement = null;
    /**
     * 5日均线(元素)
     */
    private BrokenLine MA5 = null;
    /**
     * 10日均线(元素)
     */
    private BrokenLine MA10 = null;
    /**
     * 20日均线(元素)
     */
    private BrokenLine MA20 = null;

    /**
     * 60日均线(元素)
     */
    private BrokenLine MA60 = null;

    /**
     * kdj标题栏view
     */
    private View kdjTitleView = null;

    /**
     * k线(元素)
     */
    private BrokenLine k = null;
    /**
     * d线(元素)
     */
    private BrokenLine d = null;
    /**
     * j线(元素)
     */
    private BrokenLine j = null;

    /**
     * macd标题栏view
     */
    private View macdTitleView = null;

    /**
     * dif线(元素)
     */
    private BrokenLine dif = null;

    /**
     * ema线(元素)
     */
    private BrokenLine ema = null;

    /**
     * 成交量(元素)
     */
    private Histogram macdHistogram = null;

    /**
     * dmi标题栏view
     */
    private View dmiTitleView = null;

    /**
     * pdi线(元素)
     */
    private BrokenLine pdi = null;
    /**
     * mdi线(元素)
     */
    private BrokenLine mdi = null;
    /**
     * adx线(元素)
     */
    private BrokenLine adx = null;
    /**
     * adxr线(元素)
     */
    private BrokenLine adxr = null;

    /**
     * wr标题栏view
     */
    private View wrTitleView = null;

    /**
     * wr线(元素)
     */
    private BrokenLine wr = null;
    /**
     * obv标题栏view
     */
    private View obvTitleView = null;

    /**
     * obv线(元素)
     */
    private BrokenLine obv = null;

    /**
     * rsi标题栏view
     */
    private View rsiTitleView = null;

    /**
     * rsi6线(元素)
     */
    private BrokenLine rsi6 = null;

    /**
     * rsi12线(元素)
     */
    private BrokenLine rsi12 = null;
    /**
     * rsi24线(元素)
     */
    private BrokenLine rsi24 = null;

    /**
     * boll标题栏view
     */
    private View bollTitleView = null;

    /**
     * boll线-上轨
     */
    private BrokenLine up = null;

    /**
     * boll线- 中轨
     */
    private BrokenLine middle = null;

    /**
     * boll线- 下轨
     */
    private BrokenLine down = null;

    /**
     * kl_boll 柱子
     */
    private Boll boll = null;

    /**
     * 副视图指标类型
     */
    private int mSubViewIndexType = VOLUM_TYPE;

    private Handler longClickHandler = new Handler();
    private LongClickRunnable longClickRunnable = new LongClickRunnable();
    private float downX = 0f;
    private int lastIndex = 0;
    private SharedPreferences mSettingPreferences;
    //默认5日均线显示
    private boolean isfive = true;
    //默认10日均线显示
    private boolean isten = true;
    //默认20日均线显示
    private boolean istwenty = true;
    //默认60日均线显示
    private boolean issixty = false;

    public HQChartView(Context context) {
        super(context);
        init(context);
    }

    public HQChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HQChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化该组件
     *
     * @param context
     */
    private void init(Context context) {
        this.mContext = context;
        this.setOrientation(LinearLayout.VERTICAL);
        this.removeAllViews();

        //// TODO: 2016/11/06 kem
      /*  mSettingPreferences = mContext.getSharedPreferences(QuotationSettingActivity
                .OPTION_AVERAGE, Context.MODE_PRIVATE);*/
        //获取当前存储值，

        //从配置文件获取是否显示均线；
       /* isfive = mSettingPreferences.getBoolean(QuotationSettingActivity.FIVE_OPTION_AVERAGE, true);
        isten = mSettingPreferences.getBoolean(QuotationSettingActivity.TEN_OPTION_AVERAGE, true);
        istwenty = mSettingPreferences.getBoolean(QuotationSettingActivity.TWENTY_OPTION_AVERAGE,
                true);
        issixty = mSettingPreferences.getBoolean(QuotationSettingActivity.SIXTY_OPTION_AVERAGE,
                false);*/
        isfive = true;
        isten = true;
        istwenty = true;
        issixty = true;

        //初始化主画布
        mainView = new CanvasView(context);
        LayoutParams priceParam = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        priceParam.weight = 1;
        mainView.setLayoutParams(priceParam);
        this.addView(mainView, 0);

        //初始化副画布
        subView = new CanvasView(context);
        LayoutParams turnoverParam = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        turnoverParam.weight = 2;
        subView.setLayoutParams(turnoverParam);
        //// TODO: 2016/11/14 隐藏副画板 ；
        //        this.addView(subView, 1);

        initPriceElements();//初始化分时元素
        initKElements();//初始化K线元素
        initKDJElements();//初始化kdj元素
        initMACDElements();//初始化macd元素
        initDMIElements();//初始化dmi元素
        initWRElements();//初始化wr元素
        initOBVElements();//初始化obv元素
        initRSIElements();//初始化rsi元素
        initBOLLElements();//初始化boll元素
    }

    /**
     * 初始化分时的元素
     */
    private void initPriceElements() {
        //价格线
        priceLineElement = new BrokenLine(mContext);
        priceLineElement.setFill(true);
        priceLineElement.setLineColor(Color.parseColor("#387ea6"));
        priceLineElement.setDefaultShowPointNums(chartType.getPointNum());
        priceLineElement.setLineFillColor(Color.parseColor("#71bbe5"), 30);
        priceLineElement.setCalculateDataExtremum(false);

        //均线
        averageLineElement = new BrokenLine(mContext);
        averageLineElement.setFill(false);
        averageLineElement.setLineColor(Color.parseColor("#d79e15"));
        averageLineElement.setDefaultShowPointNums(chartType.getPointNum());
        averageLineElement.setCalculateDataExtremum(false);

        //成交量
        turnoverLineElement = new Histogram(mContext);
        turnoverLineElement.setFill(true);
        turnoverLineElement.setColor(Color.parseColor("#fc4638"), Color.parseColor("#fc4638"),
                Color.parseColor("#38a647"));
        turnoverLineElement.setDefaultShowHistogramNums(chartType.getPointNum());
    }

    /**
     * 初始化K线的元素
     */
    private void initKElements() {
        loadTv = new TextView(mContext);
        loadTv.setTextSize(12);
        loadTv.setText("加载中...");
        mLoadingPop = new PopupWindow(loadTv, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //K线
        kLineElement = new CandleLine(mContext);
        kLineElement.setFill(true);
        //// TODO: 2016/11/06 kem
        //k线颜色配置；
        //                kLineElement.setUpColor(Color.parseColor(QuotationConfigUtils.sPriceUpColor));
        kLineElement.setUpColor(Color.RED);
        kLineElement.setEvenColor(Color.parseColor("#fc4638"));
        //        kLineElement.setDownColor(Color.parseColor(QuotationConfigUtils.sPriceDownColor));
        kLineElement.setDownColor(Color.parseColor("#18dbd3"));
        kLineElement.setCalculateDataExtremum(false);
        kLineElement.setContext(mContext);

        //MA5
        MA5 = new BrokenLine(mContext);
        MA5.setFill(false);
        MA5.setLineColor(Color.parseColor("#FFC730"));
        MA5.setCalculateDataExtremum(false);

        //MA10
        MA10 = new BrokenLine(mContext);
        MA10.setFill(false);
        MA10.setLineColor(Color.parseColor("#DF79F7"));
        MA10.setCalculateDataExtremum(false);

        //MA20
        MA20 = new BrokenLine(mContext);
        MA20.setFill(false);
        MA20.setLineColor(Color.parseColor("#00CCFA"));
        MA20.setCalculateDataExtremum(false);

        //MA60
        MA60 = new BrokenLine(mContext);
        MA60.setFill(false);
        MA60.setLineColor(Color.parseColor("#8D8D8D"));
        MA60.setCalculateDataExtremum(false);

        //成交量
        turnoverLineElement = new Histogram(mContext);
        turnoverLineElement.setFill(true);
        //// TODO: 2016/11/06 kem
        //        成交量颜色配置
       /* turnoverLineElement.setColor(Color.parseColor(QuotationConfigUtils.sPriceUpColor), Color.parseColor("#fc4638"),
                Color.parseColor(QuotationConfigUtils.sPriceDownColor)); */
        turnoverLineElement.setColor(Color.RED, Color.parseColor("#fc4638"), Color.BLUE);


    }

    /**
     * 初始化kdj元素
     */
    private void initKDJElements() {
        //k线
        k = new BrokenLine(mContext);
        k.setFill(false);
        k.setLineColor(Color.parseColor("#95E2F7"));
        k.setCalculateDataExtremum(false);
        //d线
        d = new BrokenLine(mContext);
        d.setFill(false);
        d.setLineColor(Color.parseColor("#F888A6"));
        d.setCalculateDataExtremum(false);
        //j线
        j = new BrokenLine(mContext);
        j.setFill(false);
        j.setLineColor(Color.parseColor("#FFB415"));
        j.setCalculateDataExtremum(false);
    }

    /**
     * 初始化macd元素
     */
    private void initMACDElements() {
        //dif线
        dif = new BrokenLine(mContext);
        dif.setFill(false);
        dif.setLineColor(Color.parseColor("#95E2F7"));
        dif.setCalculateDataExtremum(false);

        //ema线
        ema = new BrokenLine(mContext);
        ema.setFill(false);
        ema.setLineColor(Color.parseColor("#F888A6"));
        ema.setCalculateDataExtremum(false);

        //成交量
        macdHistogram = new Histogram(mContext);
        macdHistogram.setFill(true);
        // // TODO: 2016/11/06 kem
       /* macdHistogram.setColor(Color.parseColor(QuotationConfigUtils.sPriceUpColor), Color.parseColor("#fc4638"),
                Color.parseColor(QuotationConfigUtils.sPriceDownColor));
*/
        macdHistogram.setColor(Color.RED, Color.parseColor("#fc4638"), Color.BLUE);
        macdHistogram.setCalculateDataExtremum(false);
    }

    /**
     * 初始化dmi元素
     */
    private void initDMIElements() {
        //pdi线
        pdi = new BrokenLine(mContext);
        pdi.setFill(false);
        pdi.setLineColor(Color.parseColor("#95E2F7"));
        pdi.setCalculateDataExtremum(false);

        //mdi线
        mdi = new BrokenLine(mContext);
        mdi.setFill(false);
        mdi.setLineColor(Color.parseColor("#F888A6"));
        mdi.setCalculateDataExtremum(false);

        //adx线
        adx = new BrokenLine(mContext);
        adx.setFill(false);
        adx.setLineColor(Color.parseColor("#FFB415"));
        adx.setCalculateDataExtremum(false);

        //adxr线
        adxr = new BrokenLine(mContext);
        adxr.setFill(false);
        adxr.setLineColor(Color.parseColor("#4CB649"));
        adxr.setCalculateDataExtremum(false);
    }

    /**
     * 初始化wr元素
     */
    private void initWRElements() {
        //wr线
        wr = new BrokenLine(mContext);
        wr.setFill(false);
        wr.setLineColor(Color.parseColor("#009be7"));
        wr.setCalculateDataExtremum(false);
    }

    /**
     * 初始化obv元素
     */
    private void initOBVElements() {
        //obv线
        obv = new BrokenLine(mContext);
        obv.setFill(false);
        obv.setLineColor(Color.parseColor("#009be7"));
        obv.setCalculateDataExtremum(false);
    }

    /**
     * 初始化rsi元素
     */
    private void initRSIElements() {

        //rsi6线
        rsi6 = new BrokenLine(mContext);
        rsi6.setFill(false);
        rsi6.setLineColor(Color.parseColor("#95E2F7"));
        rsi6.setCalculateDataExtremum(false);

        //rsi12线
        rsi12 = new BrokenLine(mContext);
        rsi12.setFill(false);
        rsi12.setLineColor(Color.parseColor("#F888A6"));
        rsi12.setCalculateDataExtremum(false);

        //rsi24线
        rsi24 = new BrokenLine(mContext);
        rsi24.setFill(false);
        rsi24.setLineColor(Color.parseColor("#FFB415"));
        rsi24.setCalculateDataExtremum(false);
    }

    /**
     * 初始化boll线元素
     */
    private void initBOLLElements() {
        //上轨
        up = new BrokenLine(mContext);
        up.setFill(false);
        up.setLineColor(Color.parseColor("#F4BE04"));
        up.setCalculateDataExtremum(false);
        //中轨
        middle = new BrokenLine(mContext);
        middle.setFill(false);
        middle.setLineColor(Color.parseColor("#000000"));
        middle.setCalculateDataExtremum(false);
        //下轨
        down = new BrokenLine(mContext);
        down.setFill(false);
        down.setLineColor(Color.parseColor("#9932CD"));
        down.setCalculateDataExtremum(false);
        //boll柱子
        boll = new Boll(mContext);
        boll.setFill(true);
        macdHistogram.setCalculateDataExtremum(false);

    }

    /**
     * 设置分时数据
     *
     * @param priceList
     * @param averageList
     * @param turnoverList
     */
    public void setPriceData(List<String> priceList, List<String> averageList,
                             List<Histogram.HistogramBean> turnoverList) {
        if (priceLineElement != null) {
            priceLineElement.setDataList(priceList);
            int pointNum = chartType.getPointNum();
            priceLineElement.setShowPointNums(pointNum);
            priceLineElement.setDefaultShowPointNums(chartType.getPointNum());
        }
        if (averageLineElement != null) {
            averageLineElement.setDataList(averageList);
            averageLineElement.setShowPointNums(chartType.getPointNum());
            averageLineElement.setDefaultShowPointNums(chartType.getPointNum());
        }
        if (turnoverLineElement != null) {
            turnoverLineElement.setDataList(turnoverList);
            turnoverLineElement.setShowHistogramNums(chartType.getPointNum());
            turnoverLineElement.setDefaultShowHistogramNums(chartType.getPointNum());
        }

        mainView.setCrossLinePointData(priceList);
        subView.setCrossLinePointData(priceList);
        mainView.setDrawIndex(0);
        subView.setDrawIndex(0);
        mainView.setCrossLineShowNums(chartType.getPointNum());
        subView.setCrossLineShowNums(chartType.getPointNum());
    }

    /**
     * 设置K线数据
     *
     * @param kList
     * @param ma5List
     * @param ma10List
     * @param ma20List
     */
    public void setKData(List<CandleLine.CandleLineBean> kList,
                         List<String> ma5List,
                         List<String> ma10List,
                         List<String> ma20List,
                         List<String> ma60List,
                         List<Histogram.HistogramBean> turnoverList) {

        int drawIndex = 0;
        if (kList.size() - lastIndex > 0) {
            drawIndex = kList.size() - lastIndex;
        }

        lastIndex = kList.size();

        if (kLineElement != null) {
            kLineElement.setDataList(kList);

            kLineElement.setDrawCandleIndex((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK ||
                    chartType == ChartType.VERTICAL_K_MONTH) ? 0 : drawIndex);

            kLineElement.setShowCandleNums((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK ||
                    chartType == ChartType.VERTICAL_K_MONTH) ? 45 : getCandleShowNums());

            kLineElement.setDefaultShowCandleNums(chartType.getPointNum());
        }

        if (MA5 != null) {
            MA5.setDataList(ma5List);
            MA5.setDrawPointIndex((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK || chartType
                    == ChartType.VERTICAL_K_MONTH) ? 0 : drawIndex);
            MA5.setShowPointNums((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK || chartType
                    == ChartType.VERTICAL_K_MONTH) ? 45 : getCandleShowNums());
            MA5.setDefaultShowPointNums(chartType.getPointNum());

        }

        if (MA10 != null) {
            MA10.setDataList(ma10List);
            MA10.setDrawPointIndex((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK ||
                    chartType == ChartType.VERTICAL_K_MONTH) ? 0 : drawIndex);
            MA10.setShowPointNums((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK || chartType
                    == ChartType.VERTICAL_K_MONTH) ? 45 : getCandleShowNums());
            MA10.setDefaultShowPointNums(chartType.getPointNum());
        }

        if (MA20 != null) {
            MA20.setDataList(ma20List);
            MA20.setDrawPointIndex((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK ||
                    chartType == ChartType.VERTICAL_K_MONTH) ? 0 : drawIndex);
            MA20.setShowPointNums((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK || chartType
                    == ChartType.VERTICAL_K_MONTH) ? 45 : getCandleShowNums());
            MA20.setDefaultShowPointNums(chartType.getPointNum());
        }

        if (MA60 != null) {
            MA60.setDataList(ma60List);
            MA60.setDrawPointIndex((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK ||
                    chartType == ChartType.VERTICAL_K_MONTH) ? 0 : drawIndex);
            MA60.setShowPointNums((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK || chartType
                    == ChartType.VERTICAL_K_MONTH) ? 45 : getCandleShowNums());
            MA60.setDefaultShowPointNums(chartType.getPointNum());
        }

        if (turnoverLineElement != null) {
            turnoverLineElement.setDataList(turnoverList);
            turnoverLineElement.setDrawHistogramIndex((chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType
                    .VERTICAL_K_WEEK || chartType == ChartType.VERTICAL_K_MONTH) ? 0 : drawIndex);
            turnoverLineElement.setShowHistogramNums(
                    (chartType == ChartType.VERTICAL_K_DAY || chartType == ChartType.VERTICAL_K_WEEK || chartType == ChartType
                            .VERTICAL_K_MONTH) ? 45 : getCandleShowNums());
            turnoverLineElement.setDefaultShowHistogramNums(chartType.getPointNum());
        }
        mainView.setCrossLineCandleData(kList);
        subView.setCrossLineCandleData(kList);
        mainView.setDrawIndex(lastIndex);
        subView.setDrawIndex(0);
        mainView.setCrossLineShowNums(getCandleShowNums());
        subView.setCrossLineShowNums(getCandleShowNums());
    }

    /**
     * 设置主坐标系的极值
     *
     * @param max
     * @param min
     */
    public void setMainCoordinatesExtremum(String max, String min) {
        mainView.setYMax(Float.parseFloat(max));
        mainView.setYMin(Float.parseFloat(min));
    }

    /**
     * 设置副坐标系的极值
     *
     * @param max
     * @param min
     */
    public void setSubCoordinatesExtremum(String max, String min) {
        subView.setYMax(Float.parseFloat(max));
        subView.setYMin(Float.parseFloat(min));
    }

    /**
     * 设置主绘图板的刻度经线
     *
     * @param left
     * @param right
     * @param bottom
     */
    public void setMainChartScale(String[] left, String[] right, String[] bottom) {
        mainView.setLeftScaleTextArray(left);
        mainView.setRightScaleTextArray(right);
        mainView.setBottomScaleTextArray(bottom);
    }

    /**
     * 设置副绘图板的刻度经线
     * @param left
     * @param right
     * @param bottom
     */
    public void setSubChartScale(String[] left, String[] right, String[] bottom) {
        subView.setLeftScaleTextArray(left);
        subView.setRightScaleTextArray(right);
        subView.setBottomScaleTextArray(bottom);
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
        this.mSubViewIndexType = VOLUM_TYPE;
        switch (chartType) {
            case AB_ONE_DAY:
            case GGT_ONE_DAY:
            case FF_ONE_DAY:
            case FF_ONE_DAY2:
            case FF_ONE_DAY3:
            case FF_ONE_DAY4:
            case FF_ONE_DAY5:
            case FF_ONE_DAY6:
                oneDay();
                break;
            case FIVE_DAY:
                fiveDay();
                break;
            case VERTICAL_K_DAY:
            case LANDSPACE_K_DAY:
                kDay();
                break;
            case VERTICAL_K_WEEK:
            case LANDSPACE_K_WEEK:
                kWeek();
                break;
            case VERTICAL_K_MONTH:
            case LANDSPACE_K_MONTH:
                kMonth();
                break;
        }
    }

    private void oneDay() {
        mainView.removeAllChildren();
        subView.removeAllChildren();

        mainView.setCrossLineStyle(false, false, false);
        subView.setCrossLineStyle(true, false, false);

        mainView.addChildren(priceLineElement);
       /* if (Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_BOND
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_BOND
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_NATIONAL_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_BUSINESS_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_CONVERTIBLE_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_BUY_BACK
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_NATIONAL_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_BUSINESS_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_CONVERTIBLE_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_BUY_BACK) {
            mainView.addChildren(averageLineElement);
        }*/
        mainView.addChildren(averageLineElement);
        subView.addChildren(turnoverLineElement);

        mainView.setChartShowType(CanvasView.ChartShowType.TIME_DAY);
        subView.setChartShowType(CanvasView.ChartShowType.TIME_DAY);
    }

    private void fiveDay() {
        mainView.removeAllChildren();
        subView.removeAllChildren();

        mainView.setCrossLineStyle(true, true, true);
        subView.setCrossLineStyle(true, false, false);

        mainView.addChildren(priceLineElement);
       /* if (Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_BOND
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_BOND
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_NATIONAL_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_BUSINESS_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_CONVERTIBLE_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SZ_BUY_BACK
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_NATIONAL_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_BUSINESS_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_CONVERTIBLE_DEBT
                && Integer.parseInt(StockDetailsFragmentActivity.mType) != StockType.SH_BUY_BACK) {
            mainView.addChildren(averageLineElement);
        }*/
        mainView.addChildren(averageLineElement);
        subView.addChildren(turnoverLineElement);

        mainView.setChartShowType(CanvasView.ChartShowType.TIME_DAY);
        subView.setChartShowType(CanvasView.ChartShowType.TIME_DAY);
    }

    private void kDay() {
        mainView.removeAllChildren();
        subView.removeAllChildren();

        mainView.setCrossLineStyle(false, true, true);
        subView.setCrossLineStyle(true, false, false);

        mainView.addChildren(kLineElement);
        if (isfive)
            mainView.addChildren(MA5);
        if (isten)
            mainView.addChildren(MA10);
        if (istwenty)
            mainView.addChildren(MA20);
        if (issixty)
            mainView.addChildren(MA60);
        subView.addChildren(turnoverLineElement);

        mainView.setChartShowType(CanvasView.ChartShowType.CANDLE_DAY);
        subView.setChartShowType(CanvasView.ChartShowType.CANDLE_DAY);
    }

    private void kWeek() {
        mainView.removeAllChildren();
        subView.removeAllChildren();

        mainView.setCrossLineStyle(true, true, true);
        subView.setCrossLineStyle(true, false, false);

        mainView.addChildren(kLineElement);
        if (isfive)
            mainView.addChildren(MA5);
        if (isten)
            mainView.addChildren(MA10);
        if (istwenty)
            mainView.addChildren(MA20);
        if (issixty)
            mainView.addChildren(MA60);
        subView.addChildren(turnoverLineElement);

        mainView.setChartShowType(CanvasView.ChartShowType.CANDLE_DAY);
        subView.setChartShowType(CanvasView.ChartShowType.CANDLE_DAY);
    }

    private void kMonth() {

        mainView.removeAllChildren();
        subView.removeAllChildren();

        mainView.setCrossLineStyle(true, true, true);
        subView.setCrossLineStyle(true, false, false);

        mainView.addChildren(kLineElement);
        if (isfive)
            mainView.addChildren(MA5);
        if (isten)
            mainView.addChildren(MA10);
        if (istwenty)
            mainView.addChildren(MA20);
        if (issixty)
            mainView.addChildren(MA60);
        subView.addChildren(turnoverLineElement);

        mainView.setChartShowType(CanvasView.ChartShowType.CANDLE_DAY);
        subView.setChartShowType(CanvasView.ChartShowType.CANDLE_DAY);
    }

    /**
     * 展示成交量指标
     */
    public void showVolumChart(KLineBean kLineBean) {
        if (turnoverTitleView == null) {
            turnoverTitleView = LayoutInflater.from(mContext).inflate(R.layout.kl_turnover, null);
            this.addView(turnoverTitleView, 1);
        } else {
            turnoverTitleView.setVisibility(View.VISIBLE);
        }

        if (kLineBean == null) {
            return;
        }

        mSubViewIndexType = VOLUM_TYPE;
        subView.setChartShowType(CanvasView.ChartShowType.VOLUM);
        subView.removeAllChildren();
        turnoverLineElement.setDrawHistogramIndex(getDrawCandleIndex());
        turnoverLineElement.setShowHistogramNums(getCandleShowNums());
        turnoverLineElement.setDefaultShowHistogramNums(chartType.getPointNum());
        turnoverLineElement.setDataList(kLineBean.getTurnovers());
        subView.addChildren(turnoverLineElement);
        subView.setChartShowType(CanvasView.ChartShowType.VOLUM);
        calacCoordinatesValues(kLineBean);
        setCoordinatesValues(kLineBean);
        invalidateSubView();
    }

    /**
     * 隐藏成交量标题栏
     */
    public void hideVolumTitleView() {
        if (turnoverTitleView != null) {
            turnoverTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示macd指标
     */
    public void showMACDChart(KLineBean kLineBean) {
        if (macdTitleView == null) {
            macdTitleView = LayoutInflater.from(mContext).inflate(R.layout.kl_macd, null);
            this.addView(macdTitleView, 1);
        } else {
            macdTitleView.setVisibility(View.VISIBLE);
        }
        mSubViewIndexType = MACD_TYPE;
        subView.removeAllChildren();

        dif.setDrawPointIndex(getDrawCandleIndex());
        ema.setDrawPointIndex(getDrawCandleIndex());
        macdHistogram.setDrawHistogramIndex(getDrawCandleIndex());
        dif.setDefaultShowPointNums(chartType.getPointNum());
        ema.setDefaultShowPointNums(chartType.getPointNum());
        macdHistogram.setDefaultShowHistogramNums(chartType.getPointNum());
        dif.setShowPointNums(getCandleShowNums());
        ema.setShowPointNums(getCandleShowNums());
        macdHistogram.setShowHistogramNums(getCandleShowNums());

        dif.setDataList(Arrays.asList(kLineBean.getDIF()));
        ema.setDataList(Arrays.asList(kLineBean.getEMA()));
        macdHistogram.setDataList(kLineBean.getMacdTurnoversList());
        subView.addChildren(macdHistogram);
        subView.addChildren(dif);
        subView.addChildren(ema);
        subView.setChartShowType(CanvasView.ChartShowType.MACD);
        calacCoordinatesValues(kLineBean);
        setCoordinatesValues(kLineBean);
        invalidateSubView();
    }

    /**
     * 隐藏macd标题栏
     */
    private void hideMACDTitleView() {
        if (macdTitleView != null) {
            macdTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示dmi指标
     */
    public void showDMIChart(KLineBean kLineBean) {
        if (dmiTitleView == null) {
            dmiTitleView = LayoutInflater.from(mContext).inflate(R.layout.kl_dmi, null);
            this.addView(dmiTitleView, 1);
        } else {
            dmiTitleView.setVisibility(View.VISIBLE);
        }
        mSubViewIndexType = DMI_TYPE;
        subView.removeAllChildren();

        pdi.setDrawPointIndex(getDrawCandleIndex());
        mdi.setDrawPointIndex(getDrawCandleIndex());
        adx.setDrawPointIndex(getDrawCandleIndex());
        adxr.setDrawPointIndex(getDrawCandleIndex());
        pdi.setDefaultShowPointNums(chartType.getPointNum());
        mdi.setDefaultShowPointNums(chartType.getPointNum());
        adx.setDefaultShowPointNums(chartType.getPointNum());
        adxr.setDefaultShowPointNums(chartType.getPointNum());
        pdi.setShowPointNums(getCandleShowNums());
        mdi.setShowPointNums(getCandleShowNums());
        adx.setShowPointNums(getCandleShowNums());
        adxr.setShowPointNums(getCandleShowNums());

        pdi.setDataList(Arrays.asList(kLineBean.getPdi()));
        mdi.setDataList(Arrays.asList(kLineBean.getMdi()));
        adx.setDataList(Arrays.asList(kLineBean.getAdx()));
        adxr.setDataList(Arrays.asList(kLineBean.getAdxr()));
        subView.addChildren(pdi);
        subView.addChildren(mdi);
        subView.addChildren(adx);
        subView.addChildren(adxr);
        subView.setChartShowType(CanvasView.ChartShowType.DMI);
        calacCoordinatesValues(kLineBean);
        setCoordinatesValues(kLineBean);
        invalidateSubView();
    }

    /**
     * 隐藏dmi标题栏
     */
    private void hideDMITitleView() {
        if (dmiTitleView != null) {
            dmiTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示kdj指标
     */
    public void showKDJChart(KLineBean kLineBean) {
        //添加kdj标题栏view
        if (kdjTitleView == null) {
            kdjTitleView = LayoutInflater.from(mContext).inflate(R.layout.kl_kdj, null);
            this.addView(kdjTitleView, 1);
        } else {
            kdjTitleView.setVisibility(View.VISIBLE);
        }

        mSubViewIndexType = KDJ_TYPE;
        subView.removeAllChildren();

        k.setDrawPointIndex(getDrawCandleIndex());
        d.setDrawPointIndex(getDrawCandleIndex());
        j.setDrawPointIndex(getDrawCandleIndex());
        k.setDefaultShowPointNums(chartType.getPointNum());
        d.setDefaultShowPointNums(chartType.getPointNum());
        j.setDefaultShowPointNums(chartType.getPointNum());
        k.setShowPointNums(getCandleShowNums());
        d.setShowPointNums(getCandleShowNums());
        j.setShowPointNums(getCandleShowNums());

        k.setDataList(Arrays.asList(kLineBean.getK()));
        d.setDataList(Arrays.asList(kLineBean.getD()));
        j.setDataList(Arrays.asList(kLineBean.getJ()));
        subView.addChildren(k);
        subView.addChildren(d);
        subView.addChildren(j);
        subView.setChartShowType(CanvasView.ChartShowType.KDJ);
        calacCoordinatesValues(kLineBean);
        setCoordinatesValues(kLineBean);
        invalidateSubView();
    }

    /**
     * 隐藏kdj标题栏
     */
    private void hideKDJTitleView() {
        if (kdjTitleView != null) {
            kdjTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示boll线
     */
    public void showBOLLChart(KLineBean kLineBean) {
        //添加boll标题栏view
        if (bollTitleView == null) {
            bollTitleView = LayoutInflater.from(mContext).inflate(R.layout.kl_boll, null);
            this.addView(bollTitleView, 1);
        } else {
            bollTitleView.setVisibility(View.VISIBLE);
        }
        mSubViewIndexType = BOLL_TYPE;
        subView.removeAllChildren();

        up.setDrawPointIndex(getDrawCandleIndex());
        middle.setDrawPointIndex(getDrawCandleIndex());
        down.setDrawPointIndex(getDrawCandleIndex());
        boll.setDrawBollIndex(getDrawCandleIndex());
        up.setDefaultShowPointNums(chartType.getPointNum());
        middle.setDefaultShowPointNums(chartType.getPointNum());
        down.setDefaultShowPointNums(chartType.getPointNum());
        boll.setDefaultBollNums(chartType.getPointNum());
        up.setShowPointNums(getCandleShowNums());
        middle.setShowPointNums(getCandleShowNums());
        down.setShowPointNums(getCandleShowNums());
        boll.setShowBollNums(getCandleShowNums());

        up.setDataList(Arrays.asList(kLineBean.getUp()));
        middle.setDataList(Arrays.asList(kLineBean.getMiddle()));
        down.setDataList(Arrays.asList(kLineBean.getDown()));
        boll.setDataList(kLineBean.getCandleLineDataList());
        subView.addChildren(boll);
        subView.addChildren(up);
        subView.addChildren(middle);
        subView.addChildren(down);
        subView.setChartShowType(CanvasView.ChartShowType.BOLL);
        calacCoordinatesValues(kLineBean);
        setCoordinatesValues(kLineBean);
        invalidateSubView();
    }

    /**
     * 隐藏boll标题栏
     */
    private void hideBOLLTitleView() {
        if (bollTitleView != null) {
            bollTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示wr指标
     */
    public void showWRChart(KLineBean kLineBean) {
        if (wrTitleView == null) {
            wrTitleView = LayoutInflater.from(mContext).inflate(R.layout.kl_wr, null);
            this.addView(wrTitleView, 1);
        } else {
            wrTitleView.setVisibility(View.VISIBLE);
        }

        mSubViewIndexType = WR_TYPE;
        subView.removeAllChildren();

        wr.setDrawPointIndex(getDrawCandleIndex());
        wr.setDefaultShowPointNums(chartType.getPointNum());
        wr.setShowPointNums(getCandleShowNums());

        wr.setDataList(Arrays.asList(kLineBean.getWr()));
        subView.addChildren(wr);
        subView.setChartShowType(CanvasView.ChartShowType.WR);
        calacCoordinatesValues(kLineBean);
        setCoordinatesValues(kLineBean);
        invalidateSubView();

    }

    /**
     * 隐藏wr标题栏
     */
    private void hideWRTitleView() {
        if (wrTitleView != null) {
            wrTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示obv指标
     */
    public void showOBVChart(KLineBean kLineBean) {
        if (obvTitleView == null) {
            obvTitleView = LayoutInflater.from(mContext).inflate(R.layout.kl_obv, null);
            this.addView(obvTitleView, 1);
        } else {
            obvTitleView.setVisibility(View.VISIBLE);
        }

        mSubViewIndexType = OBV_TYPE;
        subView.removeAllChildren();

        obv.setDrawPointIndex(getDrawCandleIndex());
        obv.setDefaultShowPointNums(chartType.getPointNum());
        obv.setShowPointNums(getCandleShowNums());

        obv.setDataList(Arrays.asList(kLineBean.getObv()));
        subView.addChildren(obv);
        subView.setChartShowType(CanvasView.ChartShowType.OBV);
        calacCoordinatesValues(kLineBean);
        setCoordinatesValues(kLineBean);
        invalidateSubView();

    }

    /**
     * 隐藏wr标题栏
     */
    private void hideOBVTitleView() {
        if (obvTitleView != null) {
            obvTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示rsi指标
     */
    public void showRSIChart(KLineBean kLineBean) {
        if (rsiTitleView == null) {
            rsiTitleView = LayoutInflater.from(mContext).inflate(R.layout.kl_rsi, null);
            this.addView(rsiTitleView, 1);
        } else {
            rsiTitleView.setVisibility(View.VISIBLE);
        }

        mSubViewIndexType = RSI_TYPE;
        subView.removeAllChildren();

        rsi6.setDrawPointIndex(getDrawCandleIndex());
        rsi12.setDrawPointIndex(getDrawCandleIndex());
        rsi24.setDrawPointIndex(getDrawCandleIndex());
        rsi6.setDefaultShowPointNums(chartType.getPointNum());
        rsi12.setDefaultShowPointNums(chartType.getPointNum());
        rsi24.setDefaultShowPointNums(chartType.getPointNum());
        rsi6.setShowPointNums(getCandleShowNums());
        rsi12.setShowPointNums(getCandleShowNums());
        rsi24.setShowPointNums(getCandleShowNums());

        rsi6.setDataList(Arrays.asList(kLineBean.getRsi6()));
        rsi12.setDataList(Arrays.asList(kLineBean.getRsi12()));
        rsi24.setDataList(Arrays.asList(kLineBean.getRsi24()));
        subView.addChildren(rsi6);
        subView.addChildren(rsi12);
        subView.addChildren(rsi24);
        subView.setChartShowType(CanvasView.ChartShowType.RSI);
        calacCoordinatesValues(kLineBean);
        setCoordinatesValues(kLineBean);
        invalidateSubView();
    }

    /**
     * 隐藏rsi标题栏
     */
    private void hideRSITitleView() {
        if (rsiTitleView != null) {
            rsiTitleView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isSupportTouchEvent) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN & MotionEvent.ACTION_MASK:
                    longClickHandler.postDelayed(longClickRunnable, 500);
                    touch(event);
                    downX = event.getX();
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    touch(event);
                    break;

                case MotionEvent.ACTION_MOVE:
                    //显示十字线，不进行左右滑动
                    if (isShowCrossLine) {
                        mainView.crossLineMove(event);
                        subView.crossLineMove(event);
                        invalidateAllView();
                        return true;
                    }
                    //不显示十字线，进行左右滑动或缩放
                    if (Math.abs(event.getX() - downX) > UnitUtils.dpToPx(mContext, 10) || event.getPointerCount() == 2) {
                        longClickHandler.removeCallbacks(longClickRunnable);
                        touch(event);
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    if (!isShowCrossLine) {
                        touch(event);
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (!isShowCrossLine) {
                        touch(event);
                    }
                    isShowCrossLine = false;
                    longClickHandler.removeCallbacks(longClickRunnable);
                    mainView.crossLineMove(event);
                    subView.crossLineMove(event);
                    break;

            }
            invalidateAllView();
            return true;
        }

        return super.onTouchEvent(event);
    }


    /**
     * 手指事件分发到各个元素
     *
     * @param event
     */
    private void touch(MotionEvent event) {
        if (chartType != ChartType.AB_ONE_DAY &&
                chartType != ChartType.FIVE_DAY &&
                chartType != ChartType.GGT_ONE_DAY &&
                chartType != ChartType.FF_ONE_DAY &&
                chartType != ChartType.FF_ONE_DAY2 &&
                chartType != ChartType.FF_ONE_DAY3 &&
                chartType != ChartType.FF_ONE_DAY4 &&
                chartType != ChartType.FF_ONE_DAY5 &&
                chartType != ChartType.FF_ONE_DAY6) {
            kLineElement.onTouchEvent(event);
            MA5.onTouchEvent(event);
            MA10.onTouchEvent(event);
            MA20.onTouchEvent(event);
            MA60.onTouchEvent(event);

            if (mSubViewIndexType == VOLUM_TYPE) {
                turnoverLineElement.onTouchEvent(event);
            }

            if (mSubViewIndexType == KDJ_TYPE) {
                k.onTouchEvent(event);
                d.onTouchEvent(event);
                j.onTouchEvent(event);
            }

            if (mSubViewIndexType == MACD_TYPE) {
                macdHistogram.onTouchEvent(event);
                dif.onTouchEvent(event);
                ema.onTouchEvent(event);
            }

            if (mSubViewIndexType == DMI_TYPE) {
                pdi.onTouchEvent(event);
                mdi.onTouchEvent(event);
                adx.onTouchEvent(event);
                adxr.onTouchEvent(event);
            }

            if (mSubViewIndexType == WR_TYPE) {
                wr.onTouchEvent(event);
            }

            if (mSubViewIndexType == OBV_TYPE) {
                obv.onTouchEvent(event);
            }

            if (mSubViewIndexType == RSI_TYPE) {
                rsi6.onTouchEvent(event);
                rsi12.onTouchEvent(event);
                rsi24.onTouchEvent(event);
            }

            if (mSubViewIndexType == BOLL_TYPE) {
                up.onTouchEvent(event);
                middle.onTouchEvent(event);
                down.onTouchEvent(event);
                boll.onTouchEvent(event);
            }
        }

    }

    public void invalidateAllView() {
        mainView.invalidate();
        subView.invalidate();
    }

    public void invalidateMainView() {
        mainView.invalidate();
    }

    private void invalidateSubView() {
        subView.invalidate();
    }

    public boolean isSupportTouchEvent() {
        return isSupportTouchEvent;
    }

    public void setSupportTouchEvent(boolean isSupportZoomAndMove) {
        this.isSupportTouchEvent = isSupportZoomAndMove;
    }

    private class LongClickRunnable implements Runnable {

        //// TODO: 2016/11/7 屏蔽十字线 kem

        @Override
        public void run() {
            isShowCrossLine = true;
        }
    }

    /**
     * 设置十字线事件回调
     *
     * @param showCrossLineCallBack
     */
    public void setShowCrossLineCallBack(CrossLine.CrossLineCallBack showCrossLineCallBack) {
        mainView.setCrossLineCallBack(showCrossLineCallBack);
    }

    /**
     * 设置缩放和移动事件回调
     *
     * @param candleZoomMoveCallBack
     */
    public void setCandleZoomMoveCallBack(CandleLine.CandleZoomMoveCallBack candleZoomMoveCallBack) {
        this.kLineElement.setCandleZoomMoveCallBack(candleZoomMoveCallBack);
    }

    /**
     * 获取蜡烛屏幕起始下标
     *
     * @return
     */
    public int getDrawCandleIndex() {
        return kLineElement.getDrawCandleIndex();
    }

    /**
     * 获取蜡烛屏幕展示数量
     *
     * @return
     */
    public int getCandleShowNums() {
        return kLineElement.getShowCandleNums();
    }

    /**
     * 设置坐标系极值极刻度
     *
     * @param chartBean
     */
    public void setCoordinatesValues(StockDetailChartBean chartBean) {

        try {
            float maxPrice = Float.valueOf(chartBean.getYMaxPrice());
            float minPrice = Float.valueOf(chartBean.getYMinPrice());
            setMainCoordinatesExtremum("" + (maxPrice + (maxPrice * 0.001)), "" + (minPrice - (minPrice * 0.001)));
            setMainChartScale(chartBean.getLeftScale().toArray(new String[]{}), chartBean.getRightScale().toArray(new
                    String[]{}), chartBean.getBottomScale().toArray(new String[]{}));
            //// TODO: 2016/11/14 隐藏副坐标系；
/*
            if (mSubViewIndexType == VOLUM_TYPE) {
                setSubChartScale(new String[]{NumberUtils.formatToChinese(chartBean.getYMaxTurnover(), 2, true), "0"}, new
                        String[]{"", ""}, new String[]{"", ""});
                setSubCoordinatesExtremum("" + chartBean.getYMaxTurnover(), "" + 0);
            } else if (mSubViewIndexType == KDJ_TYPE) {
                KLineBean kLineBean = (KLineBean) chartBean;
                setSubChartScale(new String[]{kLineBean.getMaxkdj(), "" + ((Float.parseFloat(kLineBean.getMaxkdj()) - Math.abs
                        (Float.parseFloat(kLineBean.getMinkdj()))) / 2), kLineBean.getMinkdj()}, new String[]{"", "", ""}, new
                        String[]{"", ""});
                setSubCoordinatesExtremum(kLineBean.getMaxkdj(), kLineBean.getMinkdj());
            } else if (mSubViewIndexType == MACD_TYPE) {
                KLineBean kLineBean = (KLineBean) chartBean;
                setSubChartScale(new String[]{kLineBean.getMaxMacd(), "0", "-" + kLineBean.getMaxMacd()}, new String[]{"", "",
                        ""}, new String[]{"", ""});
                setSubCoordinatesExtremum(kLineBean.getMaxMacd(), "" + (-1 * Float.valueOf(kLineBean.getMaxMacd())));
            } else if (mSubViewIndexType == DMI_TYPE) {
                KLineBean kLineBean = (KLineBean) chartBean;
                setSubChartScale(new String[]{kLineBean.getMaxDmi(), "0"}, new String[]{"", ""}, new String[]{"", ""});
                setSubCoordinatesExtremum(kLineBean.getMaxDmi(), "0");
            } else if (mSubViewIndexType == WR_TYPE) {
                setSubChartScale(new String[]{"100", "50", "0"}, new String[]{"", "", ""}, new String[]{"", ""});
                setSubCoordinatesExtremum("100", "0");
            } else if (mSubViewIndexType == OBV_TYPE) {
                KLineBean kLineBean = (KLineBean) chartBean;
                setSubChartScale(new String[]{NumberUtils.formatToChinese(Double.valueOf(kLineBean.getMaxObv()), 2, true),
                        "0"}, new String[]{"", ""}, new String[]{"", ""});
                setSubCoordinatesExtremum(kLineBean.getMaxObv(), "0");
            } else if (mSubViewIndexType == RSI_TYPE) {
                KLineBean kLineBean = (KLineBean) chartBean;
                setSubChartScale(new String[]{kLineBean.getMaxRsi(), kLineBean.getMinRsi()}, new String[]{"", ""}, new
                        String[]{"", ""});
                setSubCoordinatesExtremum(kLineBean.getMaxRsi(), kLineBean.getMinRsi());
            } else if (mSubViewIndexType == BOLL_TYPE) {
                KLineBean kLineBean = (KLineBean) chartBean;
                setSubChartScale(
                        new String[]{kLineBean.getMaxBoll(), NumberUtils.format((Float.valueOf(kLineBean.getMaxBoll()) - Float
                                .valueOf(kLineBean.getMinBoll()) / 2), 2, true),
                                kLineBean.getMinBoll()
                        },
                        new String[]{"", "", ""}, new String[]{"", ""});
                setSubCoordinatesExtremum(kLineBean.getMaxBoll(), kLineBean.getMinBoll());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //计算坐标系最高,最低值
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void calacCoordinatesValues(StockDetailChartBean chartBean) {
        float maxTurnover = 0f;
        float maxPrice = 0f;
        float minPrice = 0f;
        Float[] maxPriceArray = new Float[4];
        Float[] minPriceArray = new Float[4];
        try {
            if (chartBean instanceof KLineBean) {
                KLineBean bean = (KLineBean) chartBean;
                List<CandleLine.CandleLineBean> candleLineBeanlist = bean.getCandleLineDataList();
                List<String> ma5 = bean.getMa5DataList();
                List<String> ma10 = bean.getMa10DataList();
                List<String> ma20 = bean.getMa20DataList();
                List<String> ma60 = bean.getMa60DataList();

                mainView.setDrawIndex(getDrawCandleIndex());
                subView.setDrawIndex(getDrawCandleIndex());
                mainView.setCrossLineShowNums(getCandleShowNums());
                subView.setCrossLineShowNums(getCandleShowNums());

                for (int i = getDrawCandleIndex(); i < getDrawCandleIndex() + getCandleShowNums(); i++) {
                    if (i < 0 || i >= candleLineBeanlist.size()) {
                        continue;
                    }

                    CandleLine.CandleLineBean candleLineBean = candleLineBeanlist.get(i);

                    if (i == getDrawCandleIndex()) {//第一次初始化值
                        maxPrice = candleLineBean.getHeightPrice();
                        maxPriceArray[0] = Float.valueOf(ma5.get(i));
                        maxPriceArray[1] = Float.valueOf(ma10.get(i));
                        maxPriceArray[2] = Float.valueOf(ma20.get(i));
                        maxPriceArray[3] = Float.valueOf(ma60.get(i));
                        minPrice = candleLineBean.getLowPrice();
                        minPriceArray[0] = Float.valueOf(ma5.get(i));
                        minPriceArray[1] = Float.valueOf(ma10.get(i));
                        minPriceArray[2] = Float.valueOf(ma20.get(i));
                        minPriceArray[3] = Float.valueOf(ma60.get(i));
                        maxTurnover = candleLineBean.getTurnover();
                    } else {
                        //比maxPrice大小,挑出最大值,最小值
                        maxPrice = candleLineBean.getHeightPrice() > maxPrice ?
                                candleLineBean.getHeightPrice() : maxPrice;

                        maxPriceArray[0] = Float.valueOf(ma5.get(i)) > maxPriceArray[0] ?
                                Float.valueOf(ma5.get(i)) : maxPriceArray[0];
                        maxPriceArray[1] = Float.valueOf(ma10.get(i)) > maxPriceArray[1] ?
                                Float.valueOf(ma10.get(i)) : maxPriceArray[1];
                        maxPriceArray[2] = Float.valueOf(ma20.get(i)) > maxPriceArray[2] ?
                                Float.valueOf(ma20.get(i)) : maxPriceArray[2];
                        maxPriceArray[3] = Float.valueOf(ma60.get(i)) > maxPriceArray[3] ?
                                Float.valueOf(ma60.get(i)) : maxPriceArray[3];

                        minPrice = candleLineBean.getLowPrice() < minPrice && candleLineBean.getLowPrice() > 0 ?
                                candleLineBean.getLowPrice() : minPrice;
                        minPriceArray[0] = Float.valueOf(ma5.get(i)) < minPriceArray[0] ?
                                Float.valueOf(ma5.get(i)) : minPriceArray[0];
                        minPriceArray[1] = Float.valueOf(ma10.get(i)) < minPriceArray[1] ?
                                Float.valueOf(ma10.get(i)) : maxPriceArray[1];
                        minPriceArray[2] = Float.valueOf(ma20.get(i)) < minPriceArray[2] ?
                                Float.valueOf(ma20.get(i)) : minPriceArray[2];
                        minPriceArray[3] = Float.valueOf(ma60.get(i)) < minPriceArray[3] ?
                                Float.valueOf(ma60.get(i)) : minPriceArray[3];
                        maxTurnover = maxTurnover > candleLineBean.getTurnover() ? maxTurnover : candleLineBean.getTurnover();
                    }
                }

                Arrays.sort(maxPriceArray);
                maxPrice = maxPriceArray[3] > maxPrice ? maxPriceArray[3] : maxPrice;
                Arrays.sort(minPriceArray);

                minPrice = minPriceArray[0] < minPrice ? minPriceArray[0] : minPrice;
                if (minPrice == 0) {
                    minPrice = minPriceArray[1];
                }
                if (mSubViewIndexType == KDJ_TYPE) {
                    float[] maxKdj = new float[3];
                    float[] minKdj = new float[3];
                    float[] k = sortArray(Arrays.copyOfRange(bean.getK(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] d = sortArray(Arrays.copyOfRange(bean.getD(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] j = sortArray(Arrays.copyOfRange(bean.getJ(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    maxKdj[0] = k[k.length - 1];
                    maxKdj[1] = d[d.length - 1];
                    maxKdj[2] = j[j.length - 1];
                    minKdj[0] = k[0];
                    minKdj[1] = d[0];
                    minKdj[2] = j[0];
                    Arrays.sort(maxKdj);
                    Arrays.sort(minKdj);
                    bean.setMaxkdj(NumberUtils.format(maxKdj[2], 2, true));
                    bean.setMinkdj(NumberUtils.format(minKdj[0], 2, true));

                } else if (mSubViewIndexType == MACD_TYPE) {
                    String[] macdMax = new String[3];
                    float[] dif = sortArray(Arrays.copyOfRange(bean.getDIF(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] ema = sortArray(Arrays.copyOfRange(bean.getEMA(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] macd = sortArray(Arrays.copyOfRange(bean.getMACD(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    macdMax[0] = "" + (Math.abs(dif[dif.length - 1]) > Math.abs(dif[0]) ? Math.abs(dif[dif.length - 1]) : Math
                            .abs(dif[0]));
                    macdMax[1] = "" + (Math.abs(ema[ema.length - 1]) > Math.abs(ema[0]) ? Math.abs(ema[ema.length - 1]) : Math
                            .abs(ema[0]));
                    macdMax[2] = "" + (Math.abs(macd[macd.length - 1]) > Math.abs(macd[0]) ? Math.abs(macd[macd.length - 1]) :
                            Math.abs(macd[0]));
                    Arrays.sort(macdMax);
                    bean.setMaxMacd(NumberUtils.format(macdMax[2], 2, true));
                } else if (mSubViewIndexType == DMI_TYPE) {
                    String[] macdDmi = new String[4];
                    float[] pdi = sortArray(Arrays.copyOfRange(bean.getPdi(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] mdi = sortArray(Arrays.copyOfRange(bean.getMdi(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] adx = sortArray(Arrays.copyOfRange(bean.getAdx(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] adxr = sortArray(Arrays.copyOfRange(bean.getAdxr(), getDrawCandleIndex(), getCandleShowNums()));
                    macdDmi[0] = "" + pdi[pdi.length - 1];
                    macdDmi[1] = "" + mdi[mdi.length - 1];
                    macdDmi[2] = "" + adx[adx.length - 1];
                    macdDmi[3] = "" + adxr[adxr.length - 1];
                    Arrays.sort(macdDmi);
                    bean.setMaxDmi(NumberUtils.format(macdDmi[3], 2, true));
                } else if (mSubViewIndexType == WR_TYPE) {
                } else if (mSubViewIndexType == OBV_TYPE) {
                    float[] obv = sortArray(Arrays.copyOfRange(bean.getObv(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    bean.setMaxObv("" + obv[obv.length - 1]);
                } else if (mSubViewIndexType == RSI_TYPE) {
                    float[] maxRsi = new float[3];
                    float[] minRsi = new float[3];
                    float[] rsi6 = sortArray(Arrays.copyOfRange(bean.getRsi6(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] rsi12 = sortArray(Arrays.copyOfRange(bean.getRsi12(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] rsi24 = sortArray(Arrays.copyOfRange(bean.getRsi24(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    maxRsi[0] = rsi6[rsi6.length - 1];
                    maxRsi[1] = rsi12[rsi12.length - 1];
                    maxRsi[2] = rsi24[rsi24.length - 1];
                    minRsi[0] = rsi6[0];
                    minRsi[1] = rsi12[0];
                    minRsi[2] = rsi24[0];
                    Arrays.sort(maxRsi);
                    Arrays.sort(minRsi);
                    bean.setMaxRsi(NumberUtils.format(maxRsi[2], 2, true));
                    bean.setMinRsi(NumberUtils.format(minRsi[0], 2, true));
                } else if (mSubViewIndexType == BOLL_TYPE) {
                    float[] maxBoll = new float[3];
                    float[] minBoll = new float[3];
                    float[] up = sortArray(Arrays.copyOfRange(bean.getUp(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    float[] middle = sortArray(Arrays.copyOfRange(bean.getMiddle(), getDrawCandleIndex(), getDrawCandleIndex()
                            + getCandleShowNums()));
                    float[] down = sortArray(Arrays.copyOfRange(bean.getDown(), getDrawCandleIndex(), getDrawCandleIndex() +
                            getCandleShowNums()));
                    maxBoll[0] = up[up.length - 1];
                    maxBoll[1] = middle[middle.length - 1];
                    maxBoll[2] = down[down.length - 1];
                    minBoll[0] = up[0];
                    minBoll[1] = middle[0];
                    minBoll[2] = down[0];
                    Arrays.sort(maxBoll);
                    Arrays.sort(minBoll);
                    bean.setMaxBoll(NumberUtils.format("" + (maxPrice > maxBoll[2] ? maxPrice : maxBoll[2]), 2, true));
                    bean.setMinBoll(NumberUtils.format("" + (minPrice > minBoll[0] ? minBoll[0] : minPrice), 2, true));
                }

                bean.setYMaxPrice("" + maxPrice);
                bean.setYMinPrice("" + minPrice);
                bean.setYMaxTurnover("" + maxTurnover);


            } else if (chartBean instanceof TimeSharingBean) {
                TimeSharingBean bean = (TimeSharingBean) chartBean;
                List<String> list = bean.getPrices();
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        maxPrice = Float.valueOf(list.get(i));
                        minPrice = Float.valueOf(list.get(i));
                        maxTurnover = Float.valueOf(bean.getTurnovers().get(i).getTurnover());
                    } else {
                        maxPrice = Float.valueOf(list.get(i)) > maxPrice ?
                                Float.valueOf(list.get(i)) :
                                maxPrice;
                        minPrice = Float.valueOf(list.get(i)) < minPrice &&
                                Float.valueOf(list.get(i)) > 0 ?
                                Float.valueOf(list.get(i)) :
                                minPrice;
                        maxTurnover = maxTurnover > Float.valueOf(bean.getTurnovers().get(i).getTurnover()) ?
                                maxTurnover :
                                Float.valueOf(bean.getTurnovers().get(i).getTurnover());
                    }
                }

                bean.setYMaxPrice("" + maxPrice);
                bean.setYMinPrice("" + minPrice);
                bean.setYMaxTurnover("" + maxTurnover);
            }
        } catch (Exception e) {
            Log.d(HQChartView.class.toString(), e.toString());
        }
    }

    //设置蜡烛起始画的位置
    public void setDrawCandleIndex(int index) {
        kLineElement.setDrawCandleIndex(index);
        MA5.setDrawPointIndex(index);
        MA10.setDrawPointIndex(index);
        MA20.setDrawPointIndex(index);
        priceLineElement.setDrawPointIndex(index);
        turnoverLineElement.setDrawHistogramIndex(index);
    }

    //设置蜡烛显示数量；
    public void setShowCandleNums(int num) {
        kLineElement.setShowCandleNums(num);
    }

    public void hideView() {
        hideMACDTitleView();
        hideDMITitleView();
        hideKDJTitleView();
        hideWRTitleView();
        hideOBVTitleView();
        hideRSITitleView();
        hideBOLLTitleView();

        hideLoadingPop();
    }


    public void showLoadingPop() {
        mLoadingPop.showAtLocation(mainView, Gravity.CENTER, -1 * mainView.getWidth() / 2, 0);
    }

    public void hideLoadingPop() {
        mLoadingPop.dismiss();
    }

    public boolean isShowPop() {
        return mLoadingPop.isShowing();
    }

    public View getTurnoverTitleView() {
        return turnoverTitleView;
    }

    public View getKDJTitleView() {
        return kdjTitleView;
    }

    public View getMacdTitleView() {
        return macdTitleView;
    }

    public View getDmiTitleView() {
        return dmiTitleView;
    }

    public View getObvTitleView() {
        return obvTitleView;
    }

    public View getRsiTitleView() {
        return rsiTitleView;
    }

    public View getWrTitleView() {
        return wrTitleView;
    }

    public View getBollTitleView() {
        return bollTitleView;
    }

    private float[] sortArray(String[] str) {
        int length = str.length;
        float[] f = new float[length];
        for (int i = 0; i < length; i++) {
            try {
                f[i] = Float.valueOf(str[i]);
            } catch (Exception e) {
                f[i] = 0f;
            }
        }
        Arrays.sort(f);
        return f;
    }

    public void resetHorDrawIndexCount() {
        lastIndex = 100;
        kLineElement.setShowCandleNums(100);
    }

    public void setIsDoubleBuffer(boolean isDoubleBuffer) {
        mainView.setIsDoubleBuffer(isDoubleBuffer);
        subView.setIsDoubleBuffer(isDoubleBuffer);
    }

    public void destroy() {
        mainView.destoryBufferPaintCanvas();
        subView.destoryBufferPaintCanvas();
    }


    public int getmSubViewIndexType() {
        return mSubViewIndexType;
    }

    public void setmSubViewIndexType(int mSubViewIndexType) {
        this.mSubViewIndexType = mSubViewIndexType;
    }

    public boolean isfive() {
        return isfive;
    }

    public void setIsfive(boolean isfive) {
        this.isfive = isfive;
    }

    public boolean isten() {
        return isten;
    }

    public void setIsten(boolean isten) {
        this.isten = isten;
    }

    public boolean issixty() {
        return issixty;
    }

    public void setIssixty(boolean issixty) {
        this.issixty = issixty;
    }

    public boolean istwenty() {
        return istwenty;
    }

    public void setIstwenty(boolean istwenty) {
        this.istwenty = istwenty;
    }

    public void setShowCrossLine(boolean showCrossLine) {
        mainView.setCrossLineisShow(showCrossLine);
    }


    //计算显示范围内的极值；
    public void calacShowCoordinates(int drawCandleIndex, int nums,KLineBean mHisKData) {
        List<CandleLine.CandleLineBean> list;
        List<String> list5;
        List<String> list10;
        List<String> list20;
        try {
            list = mHisKData.getCandleLineDataList().subList(drawCandleIndex, drawCandleIndex + nums);
            list5 = mHisKData.  getMa5DataList().subList(drawCandleIndex, drawCandleIndex + nums);
            list10 = mHisKData. getMa10DataList().subList(drawCandleIndex, drawCandleIndex + nums);
            list20 = mHisKData. getMa20DataList().subList(drawCandleIndex, drawCandleIndex + nums);
        } catch (IndexOutOfBoundsException e) {
            list = mHisKData.getCandleLineDataList().subList(drawCandleIndex, mHisKData.getCandleLineDataList().size() - 1);
            list5  = mHisKData.getMa5DataList().subList(drawCandleIndex, mHisKData.getCandleLineDataList().size() - 1);
            list10 = mHisKData.getMa10DataList().subList(drawCandleIndex, mHisKData.getCandleLineDataList().size() - 1);
            list20 = mHisKData.getMa20DataList().subList(drawCandleIndex, mHisKData.getCandleLineDataList().size() - 1);
        }

        float max = 0;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < list.size(); i++) {
            float lowPrice = list.get(i).getLowPrice();
            float heightPrice = list.get(i).getHeightPrice();
            float p5 = Float.parseFloat(list5.get(i));
            float p10 = Float.parseFloat(list10.get(i));
            float p20 = Float.parseFloat(list20.get(i));
            float[] limitP = {lowPrice, heightPrice, p5, p10, p20};
            Arrays.sort(limitP);
            if (limitP[0] < min)
                min = limitP[0];
            if (limitP[4] > max)
                max = limitP[4];
        }
        mHisKData.setYMaxPrice(max + "");
        mHisKData.setYMinPrice(min + "");
        List<String> xScale = new ArrayList<>();
        xScale.add(max + "");
        xScale.add((max + min) / 2 + "");
        xScale.add(min + "");
        mHisKData.setLeftScale(xScale);
        mHisKData.setRightScale(xScale);
        List<String> bottomScale = new ArrayList<>();
        bottomScale.add(mHisKData.getDates().get(drawCandleIndex));
        try {
            bottomScale.add(mHisKData.getDates().get(drawCandleIndex + nums - 1));
        } catch (IndexOutOfBoundsException e) {
            bottomScale.add(mHisKData.getDates().get(mHisKData.getDates().size() - 1));
        }
        mHisKData.setBottomScale(bottomScale);
    }
}
