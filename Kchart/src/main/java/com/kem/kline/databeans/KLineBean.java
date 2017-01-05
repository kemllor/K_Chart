package com.kem.kline.databeans;

import android.os.Parcel;

import com.kem.kline.viewbeans.CandleLine;
import com.kem.kline.viewbeans.Histogram;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：K线数据豆子
 *
 * @author zhaoyi
 */
public class KLineBean extends StockDetailChartBean implements Cloneable {
    //蜡烛线数据
    protected List<CandleLine.CandleLineBean> candleLineDataList = new ArrayList<CandleLine.CandleLineBean>();
    //MA5
    protected List<String> ma5DataList = new ArrayList<String>();
    //MA10
    protected List<String> ma10DataList = new ArrayList<String>();
    //MA20
    protected List<String> ma20DataList = new ArrayList<String>();
    //MA30
    protected List<String> ma60DataList = new ArrayList<String>();
    //日期
    protected List<String> dates = new ArrayList<String>();

    //分时线数据；
    protected List<String> times = new ArrayList<String>();


    //KDJ数据元素
    private String k[];
    private String d[];
    private String j[];
    private String maxkdj;
    private String minkdj;

    //MACD数据元素
    private String DIF[];
    private String EMA[];
    private String MACD[];
    private String maxMacd;
    protected List<Histogram.HistogramBean> macdTurnoversList = new ArrayList<Histogram.HistogramBean>();

    //boll线数据元素
    private String up[];
    private String middle[];
    private String down[];
    private String maxBoll;
    private String minBoll;

    //DMI数据元素
    private String pdi[];
    private String mdi[];
    private String adx[];
    private String adxr[];
    private String maxDmi;
    private String minDmi;

    //WR数据元素
    private String wr[];

    //OBV数据元素
    private String obv[];
    private String maxObv;

    //RSI数据元素
    private String rsi6[];
    private String rsi12[];
    private String rsi24[];
    private String minRsi;
    private String maxRsi;

    @Override
    public int describeContents() {
        return 41;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.market);
        dest.writeInt(this.type);
        dest.writeString(this.yesterday);
        dest.writeString(this.YMaxPrice);
        dest.writeString(this.YMinPrice);
        dest.writeString(this.YMaxTurnover);
        dest.writeString(this.YMinTurnover);
        dest.writeList(candleLineDataList);
        dest.writeStringList(ma5DataList);
        dest.writeStringList(ma10DataList);
        dest.writeStringList(ma20DataList);
        dest.writeStringList(ma60DataList);
        dest.writeStringList(this.dates);
        dest.writeStringList(this.times);
        dest.writeList(this.turnovers);
        dest.writeStringList(this.leftScale);
        dest.writeStringList(this.rightScale);
        dest.writeStringList(this.bottomScale);
        dest.writeStringArray(this.k);
        dest.writeStringArray(this.d);
        dest.writeStringArray(this.j);
        dest.writeStringArray(this.DIF);
        dest.writeStringArray(this.EMA);
        dest.writeStringArray(this.MACD);
        dest.writeString(this.maxMacd);
        dest.writeList(this.macdTurnoversList);
        dest.writeStringArray(this.pdi);
        dest.writeStringArray(this.mdi);
        dest.writeStringArray(this.adx);
        dest.writeStringArray(this.adxr);
        dest.writeString(this.maxDmi);
        dest.writeString(this.minDmi);
        dest.writeStringArray(this.wr);
        dest.writeStringArray(this.obv);
        dest.writeString(this.maxObv);
        dest.writeStringArray(this.rsi6);
        dest.writeStringArray(this.rsi12);
        dest.writeStringArray(this.rsi6);
        dest.writeString(this.maxRsi);
        dest.writeString(this.minRsi);
        dest.writeString(this.maxkdj);
        dest.writeString(this.minkdj);
    }

    public KLineBean() {

    }

    public KLineBean(Parcel in) {
        this.name = in.readString();
        this.code = in.readString();
        this.market = in.readString();
        this.type = in.readInt();
        this.yesterday = in.readString();
        this.YMaxPrice = in.readString();
        this.YMinPrice = in.readString();
        this.YMaxTurnover = in.readString();
        this.YMinTurnover = in.readString();
        in.readList(this.candleLineDataList, List.class.getClassLoader());
        in.readStringList(this.ma5DataList);
        in.readStringList(this.ma10DataList);
        in.readStringList(this.ma20DataList);
        in.readStringList(this.ma60DataList);
        in.readStringList(this.dates);
        in.readStringList(this.times);
        in.readList(this.turnovers, List.class.getClassLoader());
        in.readStringList(this.leftScale);
        in.readStringList(this.rightScale);
        in.readStringList(this.bottomScale);
        in.readStringArray(this.k);
        in.readStringArray(this.d);
        in.readStringArray(this.j);
        in.readStringArray(this.DIF);
        in.readStringArray(this.EMA);
        in.readStringArray(this.MACD);
        this.maxMacd = in.readString();
        in.readList(this.macdTurnoversList, List.class.getClassLoader());
        in.readStringArray(this.pdi);
        in.readStringArray(this.mdi);
        in.readStringArray(this.adx);
        in.readStringArray(this.adxr);
        this.maxDmi = in.readString();
        this.minDmi = in.readString();
        in.readStringArray(this.wr);
        in.readStringArray(this.obv);
        this.maxObv = in.readString();
        in.readStringArray(this.rsi6);
        in.readStringArray(this.rsi12);
        in.readStringArray(this.rsi24);
        this.maxRsi = in.readString();
        this.minRsi = in.readString();
        this.maxkdj = in.readString();
        this.minkdj = in.readString();
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public static final Creator<KLineBean> CREATOR = new Creator<KLineBean>() {
        @Override
        public KLineBean createFromParcel(Parcel source) {

            return new KLineBean(source);
        }

        @Override
        public KLineBean[] newArray(int size) {

            return new KLineBean[size];
        }
    };

    public String getMaxkdj() {
        return maxkdj;
    }

    public void setMaxkdj(String maxkdj) {
        this.maxkdj = maxkdj;
    }

    public String getMinkdj() {
        return minkdj;
    }

    public void setMinkdj(String minkdj) {
        this.minkdj = minkdj;
    }

    public String[] getRsi6() {
        return rsi6;
    }

    public void setRsi6(String[] rsi6) {
        this.rsi6 = rsi6;
    }

    public String[] getRsi12() {
        return rsi12;
    }

    public void setRsi12(String[] rsi12) {
        this.rsi12 = rsi12;
    }

    public String[] getRsi24() {
        return rsi24;
    }

    public void setRsi24(String[] rsi24) {
        this.rsi24 = rsi24;
    }

    public String getMinRsi() {
        return minRsi;
    }

    public void setMinRsi(String minRsi) {
        this.minRsi = minRsi;
    }

    public String getMaxRsi() {
        return maxRsi;
    }

    public void setMaxRsi(String maxRsi) {
        this.maxRsi = maxRsi;
    }

    public String[] getObv() {
        return obv;
    }

    public void setObv(String[] obv) {
        this.obv = obv;
    }

    public String getMaxObv() {
        return maxObv;
    }

    public void setMaxObv(String maxObv) {
        this.maxObv = maxObv;
    }

    public String[] getWr() {
        return wr;
    }

    public void setWr(String[] wr) {
        this.wr = wr;
    }

    public String getMaxDmi() {
        return maxDmi;
    }

    public void setMaxDmi(String maxDmi) {
        this.maxDmi = maxDmi;
    }

    public String getMinDmi() {
        return minDmi;
    }

    public void setMinDmi(String minDmi) {
        this.minDmi = minDmi;
    }

    public String[] getPdi() {
        return pdi;
    }

    public void setPdi(String[] pdi) {
        this.pdi = pdi;
    }

    public String[] getMdi() {
        return mdi;
    }

    public void setMdi(String[] mdi) {
        this.mdi = mdi;
    }

    public String[] getAdx() {
        return adx;
    }

    public void setAdx(String[] adx) {
        this.adx = adx;
    }

    public String[] getAdxr() {
        return adxr;
    }

    public void setAdxr(String[] adxr) {
        this.adxr = adxr;
    }

    public List<Histogram.HistogramBean> getMacdTurnoversList() {
        return macdTurnoversList;
    }

    public void setMacdTurnoversList(List<Histogram.HistogramBean> macdTurnoversList) {
        this.macdTurnoversList = macdTurnoversList;
    }

    public String getMaxMacd() {
        return maxMacd;
    }

    public void setMaxMacd(String maxMacd) {
        this.maxMacd = maxMacd;
    }

    public String[] getDIF() {
        return DIF;
    }

    public void setDIF(String[] DIF) {
        this.DIF = DIF;
    }

    public String[] getEMA() {
        return EMA;
    }

    public void setEMA(String[] EMA) {
        this.EMA = EMA;
    }

    public String[] getMACD() {
        return MACD;
    }

    public void setMACD(String[] MACD) {
        this.MACD = MACD;
    }

    public String[] getK() {
        return k;
    }

    public void setK(String[] k) {
        this.k = k;
    }

    public String[] getD() {
        return d;
    }

    public void setD(String[] d) {
        this.d = d;
    }

    public String[] getJ() {
        return j;
    }

    public void setJ(String[] j) {
        this.j = j;
    }

    public List<CandleLine.CandleLineBean> getCandleLineDataList() {
        return candleLineDataList;
    }

    public void setCandleLineDataList(List<CandleLine.CandleLineBean> candleLineDataList) {
        this.candleLineDataList = candleLineDataList;
    }

    public List<String> getMa5DataList() {
        return ma5DataList;
    }

    public void setMa5DataList(List<String> ma5DataList) {
        this.ma5DataList = ma5DataList;
    }

    public List<String> getMa10DataList() {
        return ma10DataList;
    }

    public void setMa10DataList(List<String> ma10DataList) {
        this.ma10DataList = ma10DataList;
    }

    public List<String> getMa20DataList() {
        return ma20DataList;
    }

    public void setMa20DataList(List<String> ma20DataList) {
        this.ma20DataList = ma20DataList;
    }

    public List<String> getMa60DataList() {
        return ma60DataList;
    }

    public void setMa60DataList(List<String> ma60DataList) {
        this.ma60DataList = ma60DataList;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    @Override
    public List<Histogram.HistogramBean> getTurnovers() {
        return turnovers;
    }

    @Override
    public void setTurnovers(List<Histogram.HistogramBean> turnovers) {
        this.turnovers = turnovers;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        KLineBean bean = new KLineBean();
        bean.setType(getType());
        bean.setDates(getDates());
        bean.setTurnovers(getTurnovers());
        bean.setCode(getCode());
        bean.setMarket(getMarket());
        bean.setName(getName());
        bean.setYesterday(getYesterday());
        for (int i = 0; i < getCandleLineDataList().size(); i++) {
            bean.getCandleLineDataList().add((CandleLine.CandleLineBean) getCandleLineDataList().get(i).clone());
        }
        return bean;
    }

    public String[] getUp() {
        return up;
    }

    public void setUp(String[] up) {
        this.up = up;
    }

    public String[] getMiddle() {
        return middle;
    }

    public void setMiddle(String[] middle) {
        this.middle = middle;
    }

    public String[] getDown() {
        return down;
    }

    public void setDown(String[] down) {
        this.down = down;
    }

    public String getMaxBoll() {
        return maxBoll;
    }

    public void setMaxBoll(String maxBoll) {
        this.maxBoll = maxBoll;
    }

    public static Creator<KLineBean> getCreator() {
        return CREATOR;
    }

    public String getMinBoll() {
        return minBoll;
    }

    public void setMinBoll(String minBoll) {
        this.minBoll = minBoll;
    }
}
