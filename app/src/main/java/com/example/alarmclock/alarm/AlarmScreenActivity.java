package com.example.alarmclock.alarm;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmclock.R;

public class AlarmScreenActivity extends AppCompatActivity {
    private Ringtone ringtone;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager ;
    private Vibrator vibrator;
    private PowerManager.WakeLock mWakelock;
    private static int WAKELOCK_TIME = 60 * 1000;
    public final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_alarm_screen);
        String name = getIntent().getStringExtra(com.example.alarmclock.alarm.AlarmManagerHelper.NAME);
        int timeHour = getIntent().getIntExtra(com.example.alarmclock.alarm.AlarmManagerHelper.TIME_HOUR, 0);
        int timeMinute = getIntent().getIntExtra(com.example.alarmclock.alarm.AlarmManagerHelper.TIME_MINUTE, 0);
        int volumn = getIntent().getIntExtra(com.example.alarmclock.alarm.AlarmManagerHelper.VOL, 7);
        String tone = getIntent().getStringExtra(com.example.alarmclock.alarm.AlarmManagerHelper.TONE);

        audioManager = (AudioManager) getApplication().getSystemService(AUDIO_SERVICE);
        vibrator = (Vibrator) getApplication().getSystemService(getApplicationContext().VIBRATOR_SERVICE);

        TextView tvName =  findViewById(R.id.tvName);
        tvName.setText(name);

        TextView tvTime =  findViewById(R.id.tvTime);
        tvTime.setText(String.format("%02d : %02d", timeHour, timeMinute));

        Button dismissBtn = findViewById(R.id.alarm_screen_dismiss);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //ringtone.stop();
                mediaPlayer.stop();
                vibrator.cancel();
                finish();
            }
        });

        try {
            if (tone != null && !tone.equals("")) {
                Uri toneUri = Uri.parse(tone);
                if (toneUri != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volumn,AudioManager.FLAG_PLAY_SOUND);
                    //ringtone = RingtoneManager.getRingtone(this,toneUri);
                    //ringtone.play();
                    long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
                    vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),toneUri);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    Toast.makeText(this,"WAKE UPPP",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        Runnable releaseWakelock = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

                if (mWakelock != null && mWakelock.isHeld()) {
                    mWakelock.release();
                }
            }
        };

        new Handler().postDelayed(releaseWakelock, WAKELOCK_TIME);
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        PowerManager pm = (PowerManager) getApplication().getSystemService(getApplicationContext().POWER_SERVICE);

        if (mWakelock == null) {
            mWakelock = pm.newWakeLock((PowerManager.PARTIAL_WAKE_LOCK  | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);

        }

        if (!mWakelock.isHeld()) {
            mWakelock.acquire();
            Log.i(TAG, "Wakelog acquired!");
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
        }
    }
}
