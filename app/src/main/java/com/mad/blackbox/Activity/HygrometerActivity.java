package com.mad.blackbox.Activity;
/**
 * this class manages the hygrometer user interface and the sensor listener
 * create by tomat
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.blackbox.R;

public class HygrometerActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mHumidSensor;
    private SensorEventListener humidityListener;
    private TextView mHumiTv;
    private TextView mWindSpeed;
    private TextView mCloudiness;

    /**
     * check for sensors get online infomation that passed from main activity and bind the UI to this java code.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hygrometer);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mHumidSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(mHumidSensor ==null){
            Toast.makeText(this,"No Sensor detected, using online data instead", Toast.LENGTH_LONG).show();
        }
        else{
            this.humidityListener = new HumiListener();
            mSensorManager.registerListener(humidityListener, mHumidSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        mHumiTv = (TextView) findViewById(R.id.humid_persentage);
        mHumiTv.setText(getIntent().getStringExtra("humi") + "%");
        mWindSpeed = (TextView) findViewById(R.id.wind_speed);
        mWindSpeed.setText(getIntent().getStringExtra("wind")+ "m/h");
        mCloudiness = (TextView) findViewById(R.id.cloudness);
        mCloudiness.setText(getIntent().getStringExtra("cloud")+ "%");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * a class that defines sensor listener.
     */
    private class HumiListener implements SensorEventListener{
        /**
         * if the sensor data changed, handles the changed data
         * @param event
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onSensorChanged(SensorEvent event) {
            float humidityValue = event.values[0];
            BigDecimal bd = new BigDecimal(humidityValue);
            double temperature = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("Sensor", "onAccuracyChanged");
        }
    }
}
