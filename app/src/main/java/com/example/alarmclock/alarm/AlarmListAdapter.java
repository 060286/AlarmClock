package com.example.alarmclock.alarm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.alarmclock.R;

import java.util.ArrayList;

public class AlarmListAdapter extends BaseAdapter {
    private ArrayList<com.example.alarmclock.alarm.AlarmObject> list;
    private Context context;
    private Fragment alarmFragment;
    private String[] day = {"Su ","Mo ","Tu ","We ","Th ", "Fr ","Sa"};

    public AlarmListAdapter(Context context, ArrayList<com.example.alarmclock.alarm.AlarmObject> list, Fragment alarmFragment) {
        this.list = list;
        this.context = context;
        this.alarmFragment = alarmFragment;
    }

    public void setAlarms(ArrayList<com.example.alarmclock.alarm.AlarmObject> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(list!=null){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(list != null) {
            return list.get(position).getId();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.alarm_list_item,null);


        TextView tvTime = row.findViewById(R.id.tvTime);
        TextView tvName = row.findViewById(R.id.tvName);
        Switch btnToggle = row.findViewById(R.id.btnToggle);
        TextView tvRepeat = row.findViewById(R.id.tvRepeat);
        final ImageView img = row.findViewById(R.id.imgType);

        final com.example.alarmclock.alarm.AlarmObject obj = (com.example.alarmclock.alarm.AlarmObject) getItem(position);
        tvName.setText(obj.getName());
        tvTime.setText(String.format("%02d:%02d", obj.timeHour, obj.timeMinute));
        btnToggle.setChecked(obj.isEnabled);
        tvRepeat.setText("");
        btnToggle.setChecked(obj.isEnabled);
        if(obj.type.equals("Default")){
            if (obj.isEnabled){
                img.setImageResource(R.drawable.icon_notion);
            }
            else {
                img.setImageResource(R.drawable.icon_notioff);
            }
        }
        else if (obj.type.equals("Math")) {
            img.setImageResource(R.drawable.icon_math);
        }
        else {
            img.setImageResource(R.drawable.icon_shake);
        }
        tvRepeat.setText(", ");
        if(obj.repeatWeekly){
            tvRepeat.setText(tvRepeat.getText()+"Everyday");
        }
        else{
            for(int i = 0;i<obj.repeatingDays.length;i++){
                if (obj.repeatingDays[i]){
                    tvRepeat.setText(tvRepeat.getText()+day[i]);
                }
            }
        }
        if (tvRepeat.getText().toString().equals(", ")){
            tvRepeat.setText(", Once");
        }


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity) context).startAlarmDetailsActivity(obj.id);
                ((com.example.alarmclock.alarm.AlarmFragment)alarmFragment).startAlarmDetailsActivity(obj.id);
            }
        });
        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(obj.type.equals("Default")){
                    if (obj.isEnabled){
                        img.setImageResource(R.drawable.icon_notion);
                    }
                    else {
                        img.setImageResource(R.drawable.icon_notioff);
                    }
                }
                //((MainActivity) context).setAlarmEnabled(obj.id, isChecked);
                ((com.example.alarmclock.alarm.AlarmFragment)alarmFragment).setAlarmEnabled(obj.id,isChecked);

            }
        });

        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //((MainActivity) context).deleteAlarm(obj.id);
                ((com.example.alarmclock.alarm.AlarmFragment)alarmFragment).deleteAlarm(obj.id);
                return true;
            }
        });

        return row;
    }
}
