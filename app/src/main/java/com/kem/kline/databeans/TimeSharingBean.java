package com.kem.kline.databeans;

import android.os.Parcel;

import com.kem.kline.viewbeans.Histogram;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：分时图的豆子
 *
 * @author zhaoyi
 * @date   2016-11-7
 */
public class TimeSharingBean extends StockDetailChartBean implements Cloneable {

    //时间
    protected List<String> dates = new ArrayList<String>();
    //现价
    protected List<String> prices = new ArrayList<String>();
    //均价
    protected List<String> averages = new ArrayList<String>();

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
        dest.writeString(this.nowPrice);
        dest.writeString(this.averagePrice);
        dest.writeString(this.date);
        dest.writeString(this.volume);
        dest.writeString(this.yesterday);
        dest.writeString(this.YMaxPrice);
        dest.writeString(this.YMinPrice);
        dest.writeString(this.YMaxTurnover);
        dest.writeString(this.YMinTurnover);
        dest.writeStringList(this.dates);
        dest.writeStringList(this.prices);
        dest.writeStringList(this.averages);
        dest.writeList(this.turnovers);
        dest.writeStringList(this.leftScale);
        dest.writeStringList(this.rightScale);
        dest.writeStringList(this.bottomScale);
    }

    public TimeSharingBean() {

    }

    public TimeSharingBean(Parcel in) {
        this.name = in.readString();
        this.code = in.readString();
        this.market = in.readString();
        this.type = in.readInt();
        this.nowPrice = in.readString();
        this.averagePrice = in.readString();
        this.date = in.readString();
        this.volume = in.readString();
        this.yesterday = in.readString();
        this.YMaxPrice = in.readString();
        this.YMinPrice = in.readString();
        this.YMaxTurnover = in.readString();
        this.YMinTurnover = in.readString();
        in.readStringList(this.dates);
        in.readStringList(this.prices);
        in.readStringList(this.averages);
        in.readList(this.turnovers, List.class.getClassLoader());
        in.readStringList(this.leftScale);
        in.readStringList(this.rightScale);
        in.readStringList(this.bottomScale);
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<String> getPrices() {
        return prices;
    }

    public void setPrices(List<String> prices) {
        this.prices = prices;
    }

    public List<String> getAverages() {
        return averages;
    }

    public void setAverages(List<String> averages) {
        this.averages = averages;
    }

    public static final Creator<TimeSharingBean> CREATOR = new Creator<TimeSharingBean>() {
        @Override
        public TimeSharingBean createFromParcel(Parcel source) {

            return new TimeSharingBean(source);
        }

        @Override
        public TimeSharingBean[] newArray(int size) {

            return new TimeSharingBean[size];
        }
    };


    public TimeSharingBean clone() throws CloneNotSupportedException {
        TimeSharingBean beanClone = (TimeSharingBean) super.clone();
        List<String> datesClone = new ArrayList<String>();
        List<String> pricesClone = new ArrayList<String>();
        List<String> averagesClone = new ArrayList<String>();
        List<Histogram.HistogramBean> turnoversClone = new ArrayList<Histogram.HistogramBean>();
        datesClone.addAll(dates);
        pricesClone.addAll(prices);
        averagesClone.addAll(averages);
        turnoversClone.addAll(turnovers);
        beanClone.setDates(datesClone);
        beanClone.setPrices(pricesClone);
        beanClone.setAverages(averagesClone);
        beanClone.setTurnovers(turnoversClone);
        return beanClone;
    }
}
