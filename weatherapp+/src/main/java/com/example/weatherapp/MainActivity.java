package com.example.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.TooManyListenersException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLocation = (TextView) findViewById(R.id.location);
        tvTemperature = (TextView) findViewById(R.id.temperature);
        tvHumidity = (TextView)  findViewById(R.id.humidity);
        tvWindSpeed = (TextView) findViewById(R.id.windspeed);
        tvCloudiness = (TextView) findViewById(R.id.cloudiness);
        btnRefresh = (Button) findViewById(R.id.button_refresh);
        ivIcon = (ImageView) findViewById(R.id.icon);

        //request use asyntask
        /*
        new WeatherDataRetrieval().execute();
        */
        // request use volley
        requestViaVolley();
    }

    private TextView tvLocation, tvTemperature, tvHumidity, tvWindSpeed, tvCloudiness;
    private Button btnRefresh;
    private ImageView ivIcon;
    private static final String WEATHER_SOURCE = "http://api.openweathermap.org/data/2.5/weather?APPID=82445b6c96b99bc3ffb78a4c0e17fca5&mode=json&id=1735161";

    public void onClick(View view) {
        //request use asyntask
        /*
        new WeatherDataRetrieval().execute();
        */
        // request use volley
        requestViaVolley();
    }

    // Asynctask method

    private class WeatherDataRetrieval extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            NetworkInfo networkInfo = ((ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                //network connected
                URL url = null;
                try {
                    url = new URL(WEATHER_SOURCE);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(15000);
                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                        if (bufferedReader != null) {
                            String readline;
                            StringBuffer strBuffer = new StringBuffer();
                            while ((readline = bufferedReader.readLine()) != null) {
                                strBuffer.append(readline);
                            }
                            return strBuffer.toString();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //no connection
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) try {
                final JSONObject weatherJSON = new JSONObject(result);
                tvLocation.setText(weatherJSON.getString("name") + ", " + weatherJSON.getJSONObject("sys").getString("country"));
                tvWindSpeed.setText(String.valueOf(weatherJSON.getJSONObject("wind").getDouble("speed")) + " mps");
                tvCloudiness.setText(String.valueOf(weatherJSON.getJSONObject("clouds").getInt("all")) + "%");
                final JSONObject mainJSON = weatherJSON.getJSONObject("main");
                double TempC = mainJSON.getDouble("temp");
                double TempC2 = TempC - 273.15;
                NumberFormat formatter = new DecimalFormat("#.0");
                tvTemperature.setText(formatter.format(TempC2));
                tvHumidity.setText(String.valueOf(mainJSON.getInt("humidity")) + "%");


            final JSONArray weatherJSONArray = weatherJSON.getJSONArray("weather");
            if (weatherJSONArray.length()>0) {
                int code = weatherJSONArray.getJSONObject(0).getInt("id");
                ivIcon.setImageResource(getIcon(code));
            }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //end Asynctask method

    private int getIcon(int code) {
        if (code > 199 && code < 300) {
            return R.drawable.ic_thunderstorm_large;
        } else if (code > 299 && code < 500) {
            return R.drawable.ic_drizzle_large;
        } else if (code > 499 && code < 600) {
            return R.drawable.ic_snow_large;
        } else if (code > 599 && code < 700) {
            return R.drawable.ic_snow_large;
        } else if (code == 800) {
            return R.drawable.ic_day_clear_large;
        } else if (code == 801) {
            return R.drawable.ic_day_few_clouds_large;
        } else if (code == 802) {
            return R.drawable.ic_scattered_clouds_large;
        } else if (code == 803 || code == 804) {
            return R.drawable.ic_broken_clouds_large;
        } else if (code > 700 && code < 763) {
            return R.drawable.ic_fog_large;
        } else if (code == 781 || code == 900) {
            return R.drawable.ic_tornado_large;
        } else if (code == 905) {
            return R.drawable.ic_windy_large;
        } else if (code == 906) {
            return R.drawable.ic_hail_large;
        } else {
            return R.drawable.ic_day_clear_large;
        }
    }

    //Volley method
    private void requestViaVolley() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WEATHER_SOURCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject weatherJSON = new JSONObject(response);
                    tvLocation.setText(weatherJSON.getString("name") + ", " + weatherJSON.getJSONObject("sys").getString("country"));
                    tvWindSpeed.setText(String.valueOf(weatherJSON.getJSONObject("wind").getDouble("speed")) + " mps");
                    tvCloudiness.setText(String.valueOf(weatherJSON.getJSONObject("clouds").getInt("all")) + "%");
                    final JSONObject mainJSON = weatherJSON.getJSONObject("main");
                    double TempC = mainJSON.getDouble("temp");
                    double TempC2 = TempC - 273.15;
                    NumberFormat formatter = new DecimalFormat("#.0");
                    tvTemperature.setText(formatter.format(TempC2));
                    tvHumidity.setText(String.valueOf(mainJSON.getInt("humidity")) + "%");


                    final JSONArray weatherJSONArray = weatherJSON.getJSONArray("weather");
                    if (weatherJSONArray.length()>0) {
                        int code = weatherJSONArray.getJSONObject(0).getInt("id");
                        ivIcon.setImageResource(getIcon(code));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        queue.add(stringRequest);
    }


}