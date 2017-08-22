package com.mad.blackbox.Util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * this class arrange the background of the decibel activity.
 * Created by tomat on 2017-06-03.
 */

public class ScreenUtil {

    private ScreenUtil(){}

    public static float getDensity(Context context){
        if (context instanceof Activity) {
            context = context.getApplicationContext();
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.density;
    }
}
