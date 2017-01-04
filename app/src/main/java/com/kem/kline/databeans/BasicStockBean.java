package com.kem.kline.databeans;

import android.os.Parcelable;

import java.io.Serializable;

/**

 * 所有证券豆子的基类

 * @author zhaoyi
 * @date   2016-11-7
 */
public abstract class BasicStockBean implements Serializable, Parcelable {
    /**
     * 证券名称
     */
    protected String name = "";
    /**
     * 证券代码
     */
    protected String code = "";
    /**
     * 证券市场
     */
    protected String market = "";
    /**
     * 证券类型
     */
    protected int type = -999;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
