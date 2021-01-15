package com.example.alarmclock.news;

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

public class NewsAdapter extends BaseAdapter {
    private Context context;
    ArrayList<String> arrayTitle,arrayLink,arrayImg;

    public NewsAdapter(Context context, ArrayList<String> arrayTitle,ArrayList<String> arrayLink,ArrayList<String> arrayImg) {
        this.context = context;
        this.arrayTitle = arrayTitle;
        this.arrayLink = arrayLink;
        this.arrayImg=arrayImg;
    }

    @Override
    public int getCount() {
        return arrayTitle.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayTitle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_row_news,null);

        TextView name = convertView.findViewById(R.id.tvName);
        ImageView icon = convertView.findViewById(R.id.img);

        name.setText(arrayTitle.get(position));
        Picasso.with(context).load(arrayImg.get(position)).into(icon);

        return convertView;
    }
}
