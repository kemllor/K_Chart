//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

 package com.kem.kline.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class UnitUtils {
    public UnitUtils() {
    }

    public static float dpToPx(Context context, float dp) {
        return context == null?-1.0F:dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        return context == null?-1.0F:px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxToSp(Context context, float pxValue) {
        if(context == null) {
            return -1.0F;
        } else {
            float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return pxValue / fontScale + 0.5F;
        }
    }

    public static float spToPx(Context context, float spValue) {
        if(context == null) {
            return -1.0F;
        } else {
            float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return spValue * fontScale + 0.5F;
        }
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static float getScreenWidth(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService("window");
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return (float)point.x;
    }

    public static float getScreenHeight(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService("window");
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return (float)point.y;
    }
}
