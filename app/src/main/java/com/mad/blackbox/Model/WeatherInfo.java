package com.mad.blackbox.Model;

import android.util.Log;

/** this class stores weather information
 * Created by tomat on 2017-06-03.
 */

public class WeatherInfo {

    private String mHumidityStr;
    private String mTemperatureStr;
    private String mPressureStr;
    private String mWindSpeed;
    private String mCloudiness;

    public WeatherInfo(){}

    public WeatherInfo(String humidity, String temperature, String pressure) {
        this.mHumidityStr = humidity;
        this.mTemperatureStr = temperature;
        this.mPressureStr = pressure;
    }

    public String getHumidityStr() {
        return mHumidityStr;
    }

    public void setHumidityStr(String humidityStr){
        this.mHumidityStr = humidityStr;
        Log.d("temp", this.mHumidityStr);
    }

    public String getTemperatureStr() {
        return mTemperatureStr;
    }

    public void setTemperatureStr(String temperatureStr){
        this.mTemperatureStr = temperatureStr;
        Log.d("temp", this.mTemperatureStr);
    }

    public String getPressureStr() {
        return mPressureStr;
    }

    public void setPressureStr(String pressureStr){
        this.mPressureStr = pressureStr;
        Log.d("temp", this.mPressureStr);
    }

    public String getmWindSpeed() {
        return mWindSpeed;
    }

    public void setmWindSpeed(String mWindSpeed) {
        this.mWindSpeed = mWindSpeed;
    }

    public String getmCloudiness() {
        return mCloudiness;
    }

    public void setmCloudiness(String mCloudiness) {
        this.mCloudiness = mCloudiness;
    }
}
