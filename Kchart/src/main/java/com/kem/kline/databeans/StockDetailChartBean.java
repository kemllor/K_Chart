package com.kem.kline.databeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.kem.kline.viewbeans.Histogram;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：个股详情图表豆子
 *
 * @author zhaoyi
 */
public class StockDetailChartBean extends BasicStockBean {
    //判断获取周k,月k数据时,最后一条数据是否应该和倒数第二条数据比较合成一个周期
    protected String isSame = "";
    //日期
    protected String date = "";
    //开盘价
    protected String openPrice = "0";
    //现价
    protected String nowPrice = "0";
    //均价
    protected String averagePrice = "0";
    //涨跌幅
    protected String upDown = "0";
    //成交量
    protected String volume = "0";
    //昨收
    protected String yesterday = "0";
    //最大价格
    protected String YMaxPrice = "0";
    //最小价格
    protected String YMinPrice = "0";
    //最大成交量
    protected String YMaxTurnover = "0";
    //最小成交量
    protected String YMinTurnover = "0";
    //成交量
    protected List<Histogram.HistogramBean> turnovers = new ArrayList<Histogram.HistogramBean>();
    //左侧刻度
    protected List<String> leftScale = new ArrayList<String>();
    //右侧刻度
    protected List<String> rightScale = new ArrayList<String>();
    //底部刻度
    protected List<String> bottomScale = new ArrayList<String>();

    @Override
    public int describeContents() {
        return 20;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.market);
        dest.writeInt(this.type);
        dest.writeString(isSame);
        dest.writeString(this.nowPrice);
        dest.writeString(this.averagePrice);
        dest.writeString(this.date);
        dest.writeString(this.openPrice);
        dest.writeString(this.upDown);
        dest.writeString(this.volume);
        dest.writeString(this.yesterday);
        dest.writeString(this.YMaxPrice);
        dest.writeString(this.YMinPrice);
        dest.writeString(this.YMaxTurnover);
        dest.writeString(this.YMinTurnover);
        dest.writeList(this.turnovers);
        dest.writeStringList(this.leftScale);
        dest.writeStringList(this.rightScale);
        dest.writeStringList(this.bottomScale);
    }

    public StockDetailChartBean() {

    }

    public StockDetailChartBean(Parcel in) {
        this.name = in.readString();
        this.code = in.readString();
        this.market = in.readString();
        this.type = in.readInt();
        this.isSame = in.readString();
        this.nowPrice = in.readString();
        this.averagePrice = in.readString();
        this.date = in.readString();
        this.openPrice = in.readString();
        this.upDown = in.readString();
        this.volume = in.readString();
        this.yesterday = in.readString();
        this.YMaxPrice = in.readString();
        this.YMinPrice = in.readString();
        this.YMaxTurnover = in.readString();
        this.YMinTurnover = in.readString();
        in.readList(this.turnovers, List.class.getClassLoader());
        in.readStringList(this.leftScale);
        in.readStringList(this.rightScale);
        in.readStringList(this.bottomScale);
    }

    public static final Parcelable.Creator<StockDetailChartBean> CREATOR = new Parcelable.Creator<StockDetailChartBean>() {
        @Override
        public StockDetailChartBean createFromParcel(Parcel source) {

            return new StockDetailChartBean(source);
        }

        @Override
        public StockDetailChartBean[] newArray(int size) {

            return new StockDetailChartBean[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String isSame() {
        return isSame;
    }

    public void setSame(String isSame) {
        this.isSame = isSame;
    }

    public String getYesterday() {
        return yesterday;
    }

    public void setYesterday(String yesterday) {
        this.yesterday = yesterday;
    }

    public List<Histogram.HistogramBean> getTurnovers() {
        return turnovers;
    }

    public void setTurnovers(List<Histogram.HistogramBean> turnovers) {
        this.turnovers = turnovers;
    }

    public String getYMaxPrice() {
        return YMaxPrice;
    }

    public void setYMaxPrice(String YMaxPrice) {
        this.YMaxPrice = YMaxPrice;
    }

    public String getYMinPrice() {
        return YMinPrice;
    }

    public void setYMinPrice(String YMinPrice) {
        this.YMinPrice = YMinPrice;
    }

    public String getYMaxTurnover() {
        return YMaxTurnover;
    }

    public void setYMaxTurnover(String YMaxTurnover) {
        this.YMaxTurnover = YMaxTurnover;
    }

    public String getYMinTurnover() {
        return YMinTurnover;
    }

    public void setYMinTurnover(String YMinTurnover) {
        this.YMinTurnover = YMinTurnover;
    }

    public List<String> getLeftScale() {
        return leftScale;
    }

    public void setLeftScale(List<String> leftScale) {
        this.leftScale = leftScale;
    }

    public List<String> getRightScale() {
        return rightScale;
    }

    public void setRightScale(List<String> rightScale) {
        this.rightScale = rightScale;
    }

    public List<String> getBottomScale() {
        return bottomScale;
    }

    public void setBottomScale(List<String> bottomScale) {
        this.bottomScale = bottomScale;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }
}
