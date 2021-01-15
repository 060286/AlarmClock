package com.example.alarmclock.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alarmclock.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {
    Context context;
    ArrayList<com.example.alarmclock.weather.Weather> arrayList;

    public WeatherAdapter(Context context, ArrayList<com.example.alarmclock.weather.Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_row_weather,null);

        com.example.alarmclock.weather.Weather weather = arrayList.get(position);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvMaxTemp = convertView.findViewById(R.id.tvMaxTemp);
        TextView tvMinTemp = convertView.findViewById(R.id.tvMinTemp);
        ImageView img = convertView.findViewById(R.id.imgState);
        tvDate.setText(weather.Date);
        tvStatus.setText(weather.Status);
        tvMaxTemp.setText(weather.maxTemp+"°C");
        tvMinTemp.setText(weather.minTemp+"°C");

        Picasso.with(context).load("https://openweathermap.org/img/wn/"+weather.img+".png").into(img);
        return convertView;
    }
}
