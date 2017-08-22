package com.mad.blackbox.DataSource;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * this class is used to remotely fetch weather data from a open source URL
 * Created by tomat on 2017-06-03.
 */

public class WeatherDataSource {

    SharedPreferences prefs;

    public WeatherDataSource(Activity activity, String city){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
        prefs.edit().putString("City",city).apply();//.commit()
    }

    public String getCity(){
        return prefs.getString("City", "Sydney, AU");
    }

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?id=6619279&APPID=fa2f1310a6eeddecf0dd3eb1ff92d96e";

    /**
     * this class gets the JSON object from the server API.
     * @param context
     * @param city
     * @return
     */
    public static JSONObject getJSON(Context context, String city){

        try{

            URL url = new URL (OPEN_WEATHER_MAP_API);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();
            /*connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));*/
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String tmp = "";
            while((tmp=reader.readLine()) != null){
                buffer.append(tmp).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(buffer.toString());
            if(data.getInt("cod") != 200) return null;
            else return data;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
