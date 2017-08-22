package com.mad.blackbox.Activity;

/**
 * created by tomat
 *
 * this class is the main activity that manage the main UI when user launches the application.
 */

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mad.blackbox.DataSource.WeatherDataSource;
import com.mad.blackbox.Model.WeatherInfo;
import com.mad.blackbox.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String[] permissionsRequires = new String[]{ Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
    };
    private final int REQUEST_CODE = 222;
    LinearLayout mLevelLl;
    LinearLayout mCompassLl;
    LinearLayout mDecibelLl;
    LinearLayout mThermometerLl;
    LinearLayout mHygrometerLl;
    LinearLayout mBarometerLl;
    LinearLayout mLocaterLl;
    LinearLayout mSpeedometerLl;
    LinearLayout mAltimeter;
    Handler handler;
    WeatherInfo weatherInfo = new WeatherInfo();
    String myCity = "Sydney, AU";
    boolean wasConnected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLevelLl = (LinearLayout) findViewById(R.id.levelLl);
        mLevelLl.setOnClickListener(this);
        mCompassLl = (LinearLayout) findViewById(R.id.compassLl);
        mCompassLl.setOnClickListener(this);
        mDecibelLl = (LinearLayout) findViewById(R.id.decibelLl);
        mDecibelLl.setOnClickListener(this);
        mThermometerLl = (LinearLayout) findViewById(R.id.thermometerLl);
        mThermometerLl.setOnClickListener(this);
        mHygrometerLl = (LinearLayout) findViewById(R.id.hygrometerLl);
        mHygrometerLl.setOnClickListener(this);
        mBarometerLl = (LinearLayout) findViewById(R.id.barometerLl);
        mBarometerLl.setOnClickListener(this);
        mLocaterLl = (LinearLayout) findViewById(R.id.locatorLl);
        mLocaterLl.setOnClickListener(this);
        mSpeedometerLl = (LinearLayout) findViewById(R.id.speedometerLl);
        mSpeedometerLl.setOnClickListener(this);
        mAltimeter = (LinearLayout) findViewById(R.id.altimeterLl);
        mAltimeter.setOnClickListener(this);

        handler = new Handler();

        getWeatherInfo();
    }

    /**
     * this method ask users for permissions
     */
    private void getPermission() {
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.INTERNET) !=
                        PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_NETWORK_STATE) !=
                        PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, permissionsRequires,1);
        }

    }

    /**
     * this method gets the weatherInfo by calling a background thread.
     */
    private void getWeatherInfo(){
        if(isConnectedToInternet()) {
            new DownloadWeatherInfo(myCity).execute();
        }
        else Toast.makeText(this,"Your phone is not connected to Internet, some tools may not be usable", Toast.LENGTH_LONG).show();
    }

    /**
     * this method handles the click events
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.levelLl:
                Intent levelIntent = new Intent(this,LevelActivity.class);
                startActivity(levelIntent);
                break;
            case R.id.compassLl:
                Intent compassIntent = new Intent(this,CompassActivity.class);
                startActivity(compassIntent);
                break;
            case R.id.decibelLl:
                Intent decibelIntent = new Intent(this,DecibelActivity.class);
                startActivity(decibelIntent);
                break;
            case R.id.thermometerLl:
                if(isConnectedToInternet()) {
                    Intent thermometerIntent = new Intent(this, ThermometerActivity.class);
                    thermometerIntent.putExtra("temp", weatherInfo.getTemperatureStr());
                    startActivity(thermometerIntent);
                }
                else Toast.makeText(this,"Please conenct to internet to use this tool", Toast.LENGTH_LONG).show();
                break;
            case R.id.hygrometerLl:
                if(isConnectedToInternet()) {
                    Intent hygrometerIntent = new Intent(this, HygrometerActivity.class);
                    hygrometerIntent.putExtra("humi", weatherInfo.getHumidityStr());
                    hygrometerIntent.putExtra("cloud", weatherInfo.getmCloudiness());
                    hygrometerIntent.putExtra("wind", weatherInfo.getmWindSpeed());
                    startActivity(hygrometerIntent);
                }
                else Toast.makeText(this,"Please conenct to internet to use this tool", Toast.LENGTH_LONG).show();
                break;
            case R.id.barometerLl:
                if(isConnectedToInternet()) {
                    Intent barometerInent = new Intent(this, BarometerActivity.class);
                    barometerInent.putExtra("pressure", weatherInfo.getPressureStr());
                    startActivity(barometerInent);
                }
                else Toast.makeText(this,"Please conenct to internet to use this tool", Toast.LENGTH_LONG).show();
                break;
            case R.id.locatorLl:
                Intent locatorIntent = new Intent(this,MapsActivity.class);

                startActivity(locatorIntent);
                break;
            case R.id.speedometerLl:
                Intent speedometerIntent = new Intent(this,SpeedometerActivity.class);
                startActivity(speedometerIntent);
                break;
            case R.id.altimeterLl:
                Intent altimeterIntent = new Intent(this,LocatorActivity.class);
                altimeterIntent.putExtra("pressure", weatherInfo.getPressureStr());
                startActivity(altimeterIntent);
                break;
        }
    }

    /**
     * this method defines the menu buttons
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_menu,menu);
        return true;
    }

    /**
     * this method handles the menu click events
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_feedback:
                Intent intent = new Intent(this,FeedbackActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * this method retreives data passed from feedback intent
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String nameStr = data.getStringExtra("name");
        String isCanceled = data.getStringExtra("isCanceled");
        switch (requestCode){ // currently not working.
            case REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    if (isCanceled == "false")
                        Toast.makeText(MainActivity.this, "Hi " + nameStr +
                                " , thanks for the feedback!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "You canceled submission",
                                Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * this method checkes the network connection when user relaunches the appolication
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(isConnectedToInternet() && !wasConnected){
            wasConnected = true;
            new DownloadWeatherInfo("").execute();
        }
        else wasConnected = isConnectedToInternet();
    }

    /**
     * checkes the internet connection
     * @return
     */
    private boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return  activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * this class downloads the weather information from openweather API.
     */
    private class DownloadWeatherInfo extends AsyncTask<String,JSONObject, String[]>{

        String mCity;

        ProgressDialog pd = new ProgressDialog(MainActivity.this);

        protected DownloadWeatherInfo (String cityName){
            this.mCity = cityName;
        }

        /**
         * this method displays the progressdialog and ask for permissions.
         */
        @Override
        protected void onPreExecute() {
            getPermission();
            pd.setMessage(getString(R.string.pdMessage));
            pd.show();
        }

        /**
         * this method get the JSON object from the openweather API
         * @param params
         * @return
         */
        @Override
        protected String[] doInBackground(String... params) {
            final JSONObject json = WeatherDataSource.getJSON(MainActivity.this, mCity);
            String[] JSON = new String[1];
            if(json == null){
                JSON[0] = "no JSONObject";
                return JSON;
            }
            else {
                try {
                    JSONObject main = json.getJSONObject("main");
                    double temp = (main.getDouble("temp")-273.15);
                    weatherInfo.setTemperatureStr(String.valueOf(temp));
                    weatherInfo.setHumidityStr(String.valueOf(main.getInt("humidity")));
                    weatherInfo.setPressureStr(String.valueOf(main.getInt("pressure")));//

                    JSONObject wind = json.getJSONObject("wind");
                    weatherInfo.setmWindSpeed(String.valueOf(wind.getDouble("speed")));

                    JSONObject cloud = json.getJSONObject("clouds");
                    weatherInfo.setmCloudiness(String.valueOf(cloud.getInt("all")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return JSON;
        }

        /**
         * this method close the progress dialog.
         * @param strings
         */
        @Override
        protected void onPostExecute(String[] strings) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    pd.dismiss();
                }
            };
            timer.schedule(task,4000 );
        }
    }
}
