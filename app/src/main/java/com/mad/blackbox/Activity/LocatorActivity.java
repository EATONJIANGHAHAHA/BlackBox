package com.mad.blackbox.Activity;

/**
 * this class get the google map API and load device's current location
 * Created by tomat on 2017-06-04.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mad.blackbox.DataSource.TrackGPS;
import com.mad.blackbox.R;

public class LocatorActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private TrackGPS gps;
    double longitudeDb;
    double latitudeDb;
    double altimeterDb;
    TextView longitude;
    TextView latitude;
    TextView altimeters;

    /**
     * get the devices current location
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_altimeter);
        longitude = (TextView)findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        altimeters = (TextView) findViewById(R.id.main_altitude);
        altimeterDb = Float.valueOf(getIntent().getStringExtra("pressure"));
        altimeters.setText(String.valueOf(((1030.25-altimeterDb)*9)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocatorActivity.this,permissions,1);
        }
        gps = new TrackGPS(LocatorActivity.this);
        LocationManager lm = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
        /*longitude = location.getLongitude();// null for now
        latitude = location.getLatitude();// nul for now
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        }*/
    }

    /**
     * close GPS
     */
    @Override
    protected void onDestroy () {
        super.onDestroy();
        gps.stopUsingGPS();
    }

    /**
     * update UI when gps data changed
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        longitude.setText(String.valueOf(location.getLongitude()));
        latitude.setText(String.valueOf(location.getLatitude()));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}