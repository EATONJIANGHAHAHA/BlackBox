package com.mad.blackbox.Activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mad.blackbox.Model.WeatherInfo;
import com.mad.blackbox.R;
import com.mad.blackbox.View.TempView;

public class ThermometerActivity extends AppCompatActivity implements SensorEventListener{

    private WeatherInfo weatherInfo;
    private String mTemperatureStr;
    private TempView mTempView;
    private SensorManager mSensorManager;
    private Sensor mTempSensor;
    private SensorEventListener tempListener;
    Handler handler;

    public ThermometerActivity(){
        handler = new Handler();
    }

    /**
     * this class check if the device has the sensor, if not, use online data instead.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermometer);
        mTempView = (TempView) findViewById(R.id.TempView);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mTempSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        mTemperatureStr = getIntent().getStringExtra("temp");
        if (getIntent().getBooleanExtra("networkConnection", true)) {
            if (mTempSensor == null) {
                Toast.makeText(ThermometerActivity.this, "No temperature sensor detected, " +
                        "using online data instead.", Toast.LENGTH_LONG).show();
                mTempView.setTemp(mTemperatureStr);
            } else {
                this.tempListener = new TemListener();
                mSensorManager.registerListener(tempListener, mTempSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    /**
     * handles the data change
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float temperature = event.values[0];
        Message message = new Message();
        message.what = (int) temperature;
        myHandle.sendMessage(message);
    }

    Handler myHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //mTempView.setmTemp(msg.toString());
            mTempView.invalidate();
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mTempSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    /**
     * defines listener to handle data change
     */
    private class TemListener implements SensorEventListener{

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onSensorChanged(SensorEvent event) {
            float temperatureValue = event.values[0];
            BigDecimal bd = new BigDecimal(temperatureValue);
            double temperature = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("Sensor", "onAccuracyChanged");
        }
    }
}
