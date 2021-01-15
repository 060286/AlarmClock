package com.example.alarmclock.alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alarmclock.R;
import com.example.alarmclock.helper.DBHelper;

public class AlarmFragment extends Fragment {
    ListView listViewAlarm;
    ImageButton btnAddAlarm;
    AlarmListAdapter adapter;
    DBHelper dbHelper;

    public AlarmFragment(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);


        listViewAlarm = view.findViewById(R.id.listViewAlarm);
        adapter = new AlarmListAdapter(getContext(),dbHelper.getAlarms(), com.example.alarmclock.alarm.AlarmFragment.this);
        listViewAlarm.setAdapter(adapter);

        btnAddAlarm = view.findViewById(R.id.btnAddAlarm);
        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlarmDetailsActivity(-1);
            }
        });

        return view;
    }
    public void startAlarmDetailsActivity(long id) {
        Intent intent = new Intent(getContext(), com.example.alarmclock.alarm.AddAlarmActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 0);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            adapter.setAlarms(dbHelper.getAlarms());
            adapter.notifyDataSetChanged();
        }
    }
    public void deleteAlarm(long id) {
        final long alarmId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete "+ dbHelper.getAlarm(alarmId).getName() +" ?")
                .setCancelable(true)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteAlarm(alarmId);
                        adapter.setAlarms(dbHelper.getAlarms());
                        adapter.notifyDataSetChanged();
                    }
                }).show();
    }
    public void setAlarmEnabled(long id, boolean isEnabled) {
        AlarmManagerHelper.cancelAlarms(getContext());

        AlarmObject obj = dbHelper.getAlarm(id);
        obj.isEnabled = isEnabled;
        dbHelper.updateAlarm(obj);

        adapter.setAlarms(dbHelper.getAlarms());
        adapter.notifyDataSetChanged();

        AlarmManagerHelper.setAlarms(getContext());
    }
}
