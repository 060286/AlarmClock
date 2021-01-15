package com.example.alarmclock.todo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alarmclock.R;
import com.example.alarmclock.todo.Task;

import java.util.ArrayList;

public class TodoAdapter extends BaseAdapter {
    private ArrayList<Task> mTaskArrayList;
    private Context mContext;
    LayoutInflater mLayoutInflater;

    public TodoAdapter(Context mContext, ArrayList<Task> mTaskArrayList) {
        this.mContext = mContext;
        this.mTaskArrayList = mTaskArrayList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setTaskList (ArrayList<Task> taskList) {
        this.mTaskArrayList = taskList;
    }

    @Override
    public int getCount() {
        return mTaskArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.custom_tasklist, parent, false);

        TextView header = (TextView) convertView.findViewById(R.id.header);
        TextView description = (TextView) convertView.findViewById(R.id.taskDescription);
        TextView targetDate = (TextView) convertView.findViewById(R.id.targetDate);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        Task curTask = mTaskArrayList.get(position);
        header.setText(curTask.getTitle());
        description.setText(curTask.getTaskDescription());
        targetDate.setText(curTask.getDate());

        if (curTask.getStatus() == 1) {
            imageView.setImageResource(R.drawable.ic_checked);
        }
        else{
            imageView.setImageResource(R.drawable.ic_delete);
        }


        return convertView;
    }
}
