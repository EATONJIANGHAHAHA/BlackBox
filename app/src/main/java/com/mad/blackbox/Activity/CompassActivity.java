package com.mad.blackbox.Activity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.mad.blackbox.R;

public class CompassActivity extends Activity implements SensorEventListener {

    private ImageView mImageView;
    private float mCurrentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mImageView = (ImageView)findViewById(R.id.compass_imageView);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(CompassActivity.this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * this method inform the change of data to the user interface fromm the event listener
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            float degree = event.values[0];

            RotateAnimation rotateAnimation = new RotateAnimation(mCurrentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);

            rotateAnimation.setDuration(200);
            mImageView.startAnimation(rotateAnimation);
            mCurrentDegree = -degree;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
