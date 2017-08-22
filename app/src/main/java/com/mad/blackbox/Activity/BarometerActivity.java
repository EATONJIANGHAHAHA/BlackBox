package com.mad.blackbox.Activity;
/**
 * this class manages the barometer user interface
 * create by tomat
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mad.blackbox.R;

public class BarometerActivity extends AppCompatActivity {

    private double pressure;
    private double altitude;
    TextView barometerTv;
    TextView altitudeTv;

    @Override
    /**
     * this method link to view to the data and calculate the approximate altitude based on the air pressure.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barometer);
        barometerTv = (TextView) findViewById(R.id.pressure);
        String text = getIntent().getStringExtra("pressure");
        barometerTv.setText(text+ "hPa");
        pressure = Float.valueOf(text);
        altitudeTv = (TextView) findViewById(R.id.altitude);
        altitude = (1030.25-pressure) * 9;
        altitudeTv.setText(String.valueOf(altitude));
    }
}

