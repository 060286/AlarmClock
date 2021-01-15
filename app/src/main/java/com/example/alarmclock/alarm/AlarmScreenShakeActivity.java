package com.example.alarmclock.alarm;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmclock.R;

public class AlarmScreenShakeActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private com.example.alarmclock.alarm.ShakeDetector mShakeDetector;
    private AudioManager audioManager ;
    private Ringtone ringtone;
    private ImageView img;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private PowerManager.WakeLock mWakelock;
    private static int WAKELOCK_TIME = 60 * 1000;
    public final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_alarm_screen_shake);

        String name = getIntent().getStringExtra(com.example.alarmclock.alarm.AlarmManagerHelper.NAME);
        int timeHour = getIntent().getIntExtra(com.example.alarmclock.alarm.AlarmManagerHelper.TIME_HOUR, 0);
        int timeMinute = getIntent().getIntExtra(com.example.alarmclock.alarm.AlarmManagerHelper.TIME_MINUTE, 0);
        int volumn = getIntent().getIntExtra(com.example.alarmclock.alarm.AlarmManagerHelper.VOL, 7);
        String tone = getIntent().getStringExtra(com.example.alarmclock.alarm.AlarmManagerHelper.TONE);

        audioManager= (AudioManager) getApplication().getSystemService(AUDIO_SERVICE);
        vibrator = (Vibrator) getApplication().getSystemService(getApplicationContext().VIBRATOR_SERVICE);

        img=findViewById(R.id.icon_sleep);

        TextView tvName =  findViewById(R.id.tvName);
        tvName.setText(name);

        TextView tvTime =  findViewById(R.id.tvTime);
        tvTime.setText(String.format("%02d : %02d", timeHour, timeMinute));

        final TextView tvShake = findViewById(R.id.tvShake);

        try {
            if (tone != null && !tone.equals("")) {
                Uri toneUri = Uri.parse(tone);
                if (toneUri != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volumn,AudioManager.FLAG_PLAY_SOUND);
                    //ringtone = RingtoneManager.getRingtone(this,toneUri);
                    //ringtone.play();
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),toneUri);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
                    Toast.makeText(this,"Ring",Toast.LENGTH_SHORT).show();
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

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new com.example.alarmclock.alarm.ShakeDetector();
        mShakeDetector.setOnShakeListener(new com.example.alarmclock.alarm.ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if(count==2){
                    img.setImageResource(R.drawable.icon_smile);
                }
                else if(count==3){
                    img.setImageResource(R.drawable.icon_happy);
                    tvShake.setText("Good morning you too");
                    //ringtone.stop();
                    mediaPlayer.stop();
                    finish();
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

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
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();

        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
        }
    }

}
