package com.mad.blackbox.Activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mad.blackbox.R;
import com.mad.blackbox.View.LevelView;

public class LevelActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccSensor;
    private Sensor mMagSensor;

    private float[] mAccValues = new float[3];
    private float[] mMagValues = new float[3];
    private float[] mR = new float[9];
    private float[] mValues = new float[3];

    private LevelView mLevelView;
    private TextView mHorizontalTv;
    private TextView mVerticalTv;

    /**
     * get system sensor manager
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        mLevelView = (LevelView) findViewById(R.id.level_view);

        mHorizontalTv = (TextView) findViewById(R.id.horizontal_tv);
        mVerticalTv = (TextView) findViewById(R.id.vertical_tv);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    /**
     * get system sensor
     */
    @Override
    protected void onResume() {
        super.onResume();

        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this,mAccSensor,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mMagSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * unregister sensor listener when exit the application.
     */
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    /**
     * when sensor data changed, update ui
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        switch (sensorType){
            case Sensor.TYPE_ACCELEROMETER:
                mAccValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagValues = event.values.clone();
                break;
        }
        SensorManager.getRotationMatrix(mR,null,mAccValues,mMagValues);
        SensorManager.getOrientation(mR,mValues);

        float azimuth = mValues[0];
        float pitchAngle = mValues[1];
        float rollAngle = - mValues[2];

        onAngleChanged(rollAngle, pitchAngle, azimuth);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void onAngleChanged(float rollAngle, float pitchAngle, float azimuth){
        mLevelView.setAngle(rollAngle,pitchAngle);

        mHorizontalTv.setText(String.valueOf((int)Math.toDegrees(rollAngle)) + "°");
        mVerticalTv.setText(String.valueOf((int)Math.toDegrees(pitchAngle)) + "°");

    }
}
