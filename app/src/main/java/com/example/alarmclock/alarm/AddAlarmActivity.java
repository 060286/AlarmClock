package com.example.alarmclock.alarm;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmclock.R;
import com.example.alarmclock.helper.DBHelper;

import java.util.ArrayList;


public class AddAlarmActivity extends AppCompatActivity {

    private AlarmObject alarmDetails;
    DBHelper dbHelper = new DBHelper(this);
    private TimePicker timePicker;
    private EditText tbName;
    private TextView tvRepeat;
    private Button btnDone,btnCancel;
    private Spinner spinner,spinnerSound;
    private LayoutInflater layoutInflater;
    private ImageButton btnRepeat;
    private SeekBar seekBarVol;
    private AudioManager audioManager;
    private String[] day = {"Su ","Mo ","Tu ","We ","Th ", "Fr ","Sa"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_add_alarm);

        layoutInflater = LayoutInflater.from(this);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = layoutInflater.inflate(R.layout.repeat_days, null);
                AlertDialog.Builder mBuilder = createAlertDialog(view);
                mBuilder.show();
            }
        });

        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        seekBarVol = findViewById(R.id.seekBar);
        seekBarVol.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarVol.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        tvRepeat = findViewById(R.id.tvRepeat);
        timePicker = findViewById(R.id.timePicker);
        tbName = findViewById(R.id.tbName);
        btnCancel = findViewById(R.id.btnCancel);
        btnDone = findViewById(R.id.btnDone);

        spinner = findViewById(R.id.spinner);
        spinnerSound = findViewById(R.id.spinnerSound);

        timePicker.setIs24HourView(true);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Default");
        arrayList.add("Math");
        arrayList.add("Shake");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        ArrayList<String> arraySound = new ArrayList<>();
        arraySound.add("Android");
        arraySound.add("Iphone");
        ArrayAdapter arrayAdapterSound = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arraySound);
        arrayAdapterSound.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSound.setAdapter(arrayAdapterSound);

        final long id = getIntent().getExtras().getLong("id");
        if(id == -1){
            alarmDetails = new AlarmObject();
        }
        else{
            alarmDetails = dbHelper.getAlarm(id);
            timePicker.setHour(alarmDetails.getTimeHour());
            timePicker.setMinute(alarmDetails.getTimeMinute());
            tbName.setText(alarmDetails.getName());
            seekBarVol.setProgress(alarmDetails.volume);

            if(alarmDetails.type.equals("Default")){
                spinner.setSelection(0);
            }
            else if (alarmDetails.type.equals("Math")) {
                spinner.setSelection(1);
            }
            else {
                spinner.setSelection(2);
            }

            if(alarmDetails.alarmTone.equals(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))){
                spinnerSound.setSelection(0);
            }else {
                spinnerSound.setSelection(1);
            }

            tvRepeat.setText("");
            if(alarmDetails.repeatWeekly) {
                tvRepeat.setText("Everyday");
            }
            else if(!alarmDetails.repeatingDays[0] && !alarmDetails.repeatingDays[1]&&
                    !alarmDetails.repeatingDays[2] && !alarmDetails.repeatingDays[3]&&
                    !alarmDetails.repeatingDays[4] && !alarmDetails.repeatingDays[5]&&
                    !alarmDetails.repeatingDays[6]){
                tvRepeat.setText("Once");
            }
            else{
                for(int i = 0;i<alarmDetails.repeatingDays.length;i++){
                    if (alarmDetails.repeatingDays[i]){
                        tvRepeat.setText(tvRepeat.getText()+day[i]);
                    }
                }
            }
        }


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateModelFromLayout();
                if(alarmDetails.getId()<0){
                    dbHelper.createAlarm(alarmDetails);
                }
                else {
                    dbHelper.updateAlarm(alarmDetails);
                }
                AlarmManagerHelper.setAlarms(com.example.alarmclock.alarm.AddAlarmActivity.this);
                setResult(RESULT_OK);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private AlertDialog.Builder createAlertDialog(final View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final RadioButton rbMon,rbTue,rbWed,rbThu,rbFri,rbSat,rbSun;
        rbSun = view.findViewById(R.id.rbSunday);
        rbMon = view.findViewById(R.id.rbMonday);
        rbTue = view.findViewById(R.id.rbTuesday);
        rbWed = view.findViewById(R.id.rbWednesday);
        rbThu = view.findViewById(R.id.rbThursday);
        rbFri = view.findViewById(R.id.rbFriday);
        rbSat = view.findViewById(R.id.rbSaturday);

        View.OnTouchListener radioButtonTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (((RadioButton) v).isChecked()) {
                    // If the button was already checked, uncheck them all
                    ((RadioButton) v).setChecked(false);
                    // Prevent the system from re-checking it
                    return true;
                }
                return false;
            }
        };
        rbSun.setOnTouchListener(radioButtonTouch);
        rbMon.setOnTouchListener(radioButtonTouch);
        rbTue.setOnTouchListener(radioButtonTouch);
        rbWed.setOnTouchListener(radioButtonTouch);
        rbThu.setOnTouchListener(radioButtonTouch);
        rbFri.setOnTouchListener(radioButtonTouch);
        rbSat.setOnTouchListener(radioButtonTouch);

        rbSun.setChecked(alarmDetails.getRepeatingDay(alarmDetails.SUNDAY));
        rbMon.setChecked(alarmDetails.getRepeatingDay(alarmDetails.MONDAY));
        rbTue.setChecked(alarmDetails.getRepeatingDay(alarmDetails.TUESDAY));
        rbWed.setChecked(alarmDetails.getRepeatingDay(alarmDetails.WEDNESDAY));
        rbThu.setChecked(alarmDetails.getRepeatingDay(alarmDetails.THURSDAY));
        rbFri.setChecked(alarmDetails.getRepeatingDay(alarmDetails.FRIDAY));
        rbSat.setChecked(alarmDetails.getRepeatingDay(alarmDetails.SATURDAY));

        mBuilder.setView(view);

        mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarmDetails.setRepeatingDays(new boolean[]{rbSun.isChecked(),rbMon.isChecked(), rbTue.isChecked(),rbWed.isChecked(),rbThu.isChecked(),rbFri.isChecked(),rbSat.isChecked()});
                if(rbMon.isChecked() && rbTue.isChecked() && rbWed.isChecked() && rbThu.isChecked() && rbFri.isChecked() && rbSat.isChecked() && rbSun.isChecked()){
                    alarmDetails.repeatWeekly = true;
                }
                else{
                    alarmDetails.repeatWeekly = false;
                }

                tvRepeat.setText("");

                if(alarmDetails.repeatWeekly){
                    tvRepeat.setText(tvRepeat.getText()+"Everyday");
                }
                else if(!rbMon.isChecked() && !rbTue.isChecked() && !rbWed.isChecked() && !rbThu.isChecked() && !rbFri.isChecked() && !rbSat.isChecked() && !rbSun.isChecked()){
                    tvRepeat.setText("Once");
                }
                else{
                    for(int i = 0;i<alarmDetails.repeatingDays.length;i++){
                        if (alarmDetails.repeatingDays[i]){
                            tvRepeat.setText(tvRepeat.getText()+day[i]);
                        }
                    }
                }
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mBuilder.setCancelable(false);
        return mBuilder;
    }

    private void updateModelFromLayout() {
        alarmDetails.timeMinute = timePicker.getMinute();
        alarmDetails.timeHour = timePicker.getHour();
        alarmDetails.name = tbName.getText().toString();
        if(alarmDetails.name.isEmpty()){
            alarmDetails.name = "Alarm";
        }

        if(spinnerSound.getSelectedItem().toString().equals("Android")){
            alarmDetails.alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }
        else {
            alarmDetails.alarmTone= Uri.parse("android.resource://com.example.alarmclock/" + R.raw.iphone_alarm_morning);
        }
        alarmDetails.type = spinner.getSelectedItem().toString();
        alarmDetails.volume = seekBarVol.getProgress();
        alarmDetails.isEnabled = true;
    }

}
