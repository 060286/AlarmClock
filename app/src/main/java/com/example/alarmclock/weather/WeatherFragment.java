package com.example.alarmclock.weather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alarmclock.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherFragment extends Fragment {
    EditText search;
    Button btnSearch;
    TextView tvCity,tvTemp,tvState,tvHumidity,tvCloud,tvWind,tvDate;
    ImageView iconWeather;
    String City;
    String APIKey ="4bae31b7fd7ff07e795d544f0140725f";
    ListView lvWeather7days;
    WeatherAdapter weatherAdapter;
    ArrayList<Weather> arrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_weather,container,false);



        search = view.findViewById(R.id.search);
        btnSearch = view.findViewById(R.id.btnSearch);
        tvCity = view.findViewById(R.id.tvCityName);
        tvTemp = view.findViewById(R.id.tvTemp);
        tvState = view.findViewById(R.id.tvState);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvCloud = view.findViewById(R.id.tvCloud);
        tvWind = view.findViewById(R.id.tvWind);
        tvDate = view.findViewById(R.id.tvDate);
        iconWeather = view.findViewById(R.id.icon);
        lvWeather7days = view.findViewById(R.id.lvWeather7days);
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.findViewById(R.id.layoutTop).getLayoutParams();

        arrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(getContext(),arrayList);
        lvWeather7days.setAdapter(weatherAdapter);

        layoutParams.setMargins(0,0,0,200);
        lvWeather7days.setVisibility(View.GONE);

        new GetDataAsyncTask().execute("Saigon");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = search.getText().toString();
                if (city.equals("")){
                    City = "Saigon";
                }
                else {
                    City = city;
                }

                lvWeather7days.setVisibility(View.VISIBLE);
                layoutParams.setMargins(0,0,0,0);
                new GetDataAsyncTask().execute(City);
                new GetData7DaysAsyncTask().execute(City);
            }
        });

        return view;
    }
    private class GetDataAsyncTask extends AsyncTask <String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            getCurrentWeatherData(strings[0]);
            return null;
        }

    }
    private class GetData7DaysAsyncTask extends AsyncTask <String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            get7DaysData(strings[0]);
            return null;
        }

    }
    public void getCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String URL = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid="+APIKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String name = jsonObject.getString("name");
                    tvCity.setText(name);

                    String dateJSON = jsonObject.getString("dt");
                    long trans = Long.valueOf(dateJSON);
                    //Chuyen giay thanh mili giay
                    Date day = new Date(trans*1000);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                    String Date = simpleDateFormat.format(day);
                    tvDate.setText(Date);

                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String state = jsonObjectWeather.getString("main");
                    String icon = jsonObjectWeather.getString("icon");
                    Picasso.with(getContext()).load("https://openweathermap.org/img/wn/"+icon+".png").into(iconWeather);
                    tvState.setText(state);

                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    String temp = jsonObjectMain.getString("temp");
                    String humidity = jsonObjectMain.getString("humidity");
                    Double t = Double.valueOf(temp);
                    String tem = String.valueOf(t.intValue());
                    tvTemp.setText(tem+"°C");
                    tvHumidity.setText(humidity+"%");

                    JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                    String wi = jsonObjectWind.getString("speed");
                    tvWind.setText(wi+"m/s");

                    JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                    String cloud = jsonObjectCloud.getString("all");
                    tvCloud.setText(cloud+"%");

                    Log.e("JSON Object",response);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON Error",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Can't not find.",Toast.LENGTH_SHORT).show();
                Log.e("Response Error",error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }
    private void get7DaysData(String data) {
        if(!arrayList.isEmpty()){
            arrayList.clear();
        }
        String URL = "https://api.openweathermap.org/data/2.5/forecast/daily?q="+data+"&units=metric&cnt=7&appid="+APIKey;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                    for (int i =1;i<jsonArrayList.length();i++){
                        JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                        String date = jsonObjectList.getString("dt");
                        long trans = Long.valueOf(date);
                        //Chuyen giay thanh mili giay
                        Date day = new Date(trans*1000);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                        String Date = simpleDateFormat.format(day);

                        JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                        String maxTemp = jsonObjectTemp.getString("max");
                        String minTemp = jsonObjectTemp.getString("min");
                        Double max = Double.valueOf(maxTemp);
                        Double min = Double.valueOf(minTemp);
                        String Max = String.valueOf(max.intValue());
                        String Min = String.valueOf(min.intValue());

                        JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                        JSONObject weather = jsonArrayWeather.getJSONObject(0);
                        String status = weather.getString("description");
                        String icon = weather.getString("icon");

                        arrayList.add(new Weather(Date,status,icon,Max,Min));
                    }
                    weatherAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON Error",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Can't not find.",Toast.LENGTH_SHORT).show();
                Log.e("Response Error",error.toString());
            }
        });

        requestQueue.add(stringRequest);
    }
}
