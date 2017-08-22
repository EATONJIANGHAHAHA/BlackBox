package com.mad.blackbox.Util;

/**
 * this class arrange the decibel activity pointer.
 * Created by tomat on 2017-05-29.
 */

public class PointerAdjustUtil {

    public static float dbCount = 40;

    private static float lastDbCount = dbCount;
    private static float min = 0.5f;  //set minimum volume change
    private static float value = 0;   // decibel
    public static void setDbCount(float dbValue) {
        if (dbValue > lastDbCount) {
            value = dbValue - lastDbCount > min ? dbValue - lastDbCount : min;
        }else{
            value = dbValue - lastDbCount < -min ? dbValue - lastDbCount : -min;
        }
        dbCount = lastDbCount + value * 0.2f ; //prevent UI pointer change too fast.
        lastDbCount = dbCount;
    }
}
