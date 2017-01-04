package com.kem.kline.viewbeans;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：视图容器
 *
 * @author zhaoyi
 * @date 2016-11-7
 */
public class ViewContainer {
    //子容器的集合
    private List<ViewContainer> childrenList = null;
    //坐标系高度
    protected float coordinateHeight = 0;
    //坐标系宽度
    protected float maxCoordinateWidth = 0;
    //图表显示宽度
    protected float coordinateWidth = 0;
    //设置坐标系最大值
    protected float YMax = 0;
    //设置坐标系最小值
    protected float YMin = 0;
    //是否显示
    protected boolean isShow = true;

    protected int chartShowType;

    protected Context mContext;

    //最小手指间距离
    protected static final int MIN_FINGER_DISTANCE = 20;
    //最小移动距离
    protected static final int MIN_MOVE_DISTANCE = 5;

    //缩放增量
    protected int incremental = 6;

    public ViewContainer() {
        childrenList = new ArrayList<ViewContainer>() {
            @Override
            public boolean add(ViewContainer object) {
                this.remove(object);
                return super.add(object);
            }
            @Override
            public void add(int index, ViewContainer object) {
                this.remove(object);
                super.add(index, object);
            }
        };
    }

    public void draw(Canvas canvas) {
        for (ViewContainer viewContainer : childrenList) {
            viewContainer.draw(canvas);
        }
    }

    public void addChildren(ViewContainer viewContainer) {
        childrenList.add(viewContainer);
    }

    public void removeChildren(ViewContainer viewContainer) {
        childrenList.remove(viewContainer);
    }

    public List<ViewContainer> getChildrenList() {
        return childrenList;
    }

    public void setCoordinateHeight(float coordinateHeight) {
        this.coordinateHeight = coordinateHeight;
        for (ViewContainer viewContainer : getChildrenList()) {
            viewContainer.setCoordinateHeight(coordinateHeight);
        }
    }

    public void setCoordinateWidth(float coordinateWidth) {
        this.maxCoordinateWidth = coordinateWidth;
        this.coordinateWidth=maxCoordinateWidth-100;
        for (ViewContainer viewContainer : getChildrenList()) {
            viewContainer.setCoordinateWidth(this.coordinateWidth);
        }
    }

    public void move(MotionEvent event) {
        //do nothing
    }

    public void zoom(MotionEvent event) {
        //do nothing
    }

    public float getYMax() {
        return YMax;
    }

    public void setYMax(float YMax) {
        this.YMax = YMax;
    }

    public float getYMin() {
        return YMin;
    }

    public void setYMin(float YMin) {
        this.YMin = YMin;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public int getChartShowType() {
        return chartShowType;
    }

    public void setChartShowType(int chartShowType) {
        this.chartShowType = chartShowType;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }
}
