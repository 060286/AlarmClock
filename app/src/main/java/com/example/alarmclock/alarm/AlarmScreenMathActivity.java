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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmclock.R;

import java.util.Random;

public class AlarmScreenMathActivity extends AppCompatActivity {
    private Ringtone ringtone;
    private Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnCheck;
    private TextView tvName,tvTime,tvFunction,tvResult;
    private ImageView img;
    private String result="";
    private int check = 0,x=0,y=0,value=0;
    private Random generator = null;
    private AudioManager audioManager ;
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
        setContentView(R.layout.activity_alarm_screen_math);
        final long id = getIntent().getIntExtra(AlarmManagerHelper.ID,-1);
        String name = getIntent().getStringExtra(AlarmManagerHelper.NAME);
        int timeHour = getIntent().getIntExtra(AlarmManagerHelper.TIME_HOUR, 0);
        int timeMinute = getIntent().getIntExtra(AlarmManagerHelper.TIME_MINUTE, 0);
        final String tone = getIntent().getStringExtra(AlarmManagerHelper.TONE);
        final boolean once = getIntent().getBooleanExtra(AlarmManagerHelper.ONCE,true);
        int volumn = getIntent().getIntExtra(AlarmManagerHelper.VOL, 7);

        audioManager = (AudioManager) getApplication().getSystemService(AUDIO_SERVICE);
        vibrator = (Vibrator) getApplication().getSystemService(getApplicationContext().VIBRATOR_SERVICE);

        img = findViewById(R.id.icon_wake);

        tvName =  findViewById(R.id.alarm_screen_name);
        tvTime =  findViewById(R.id.alarm_screen_time);

        tvName.setText(name);
        tvTime.setText(String.format("%02d : %02d", timeHour, timeMinute));

        tvFunction =findViewById(R.id.tvFunction);
        tvResult = findViewById(R.id.tvResult);
        tvFunction.setText("");
        tvResult.setText("");

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        View.OnClickListener btnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button temp = (Button)v;
                result += temp.getText().toString();
                tvResult.setText(result);
            }
        };

        btn0.setOnClickListener(btnClick);
        btn1.setOnClickListener(btnClick);
        btn2.setOnClickListener(btnClick);
        btn3.setOnClickListener(btnClick);
        btn4.setOnClickListener(btnClick);
        btn5.setOnClickListener(btnClick);
        btn6.setOnClickListener(btnClick);
        btn7.setOnClickListener(btnClick);
        btn8.setOnClickListener(btnClick);
        btn9.setOnClickListener(btnClick);

        generator = new Random();
        y = generator.nextInt(10)+1;
        x = generator.nextInt(10)+y;
        value = generator.nextInt(2);
        tvFunction.setText(stringFunction(value,x,y));

        btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultInt = Integer.valueOf(result);
                if(checkResult(value,x,y,resultInt)) {
                    check++;
                    if(check==2) {
                        img.setImageResource(R.drawable.icon_smile);
                    }
                    else if(check==3){
                        img.setImageResource(R.drawable.icon_happy);
                    }

                    if(check==3) {
                        //ringtone.stop();
                        mediaPlayer.stop();
                        vibrator.cancel();
                        finish();
                    }
                    else {
                        generator = new Random();
                        y = generator.nextInt(10)+1;
                        x = generator.nextInt(10)+y;
                        value = generator.nextInt(2);
                        tvFunction.setText(stringFunction(value,x,y));
                        result = "";
                        tvResult.setText(result);
                    }
                }
                else {
                    result = "";
                    tvResult.setText(result);
                }
            }
        });

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
                    Toast.makeText(this,"Wake UP!!!",Toast.LENGTH_SHORT).show();
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
    boolean checkResult(int value,int x, int y,int resultInt){
        switch (value){
            case 0:
                if(x+y==resultInt)
                    return true;
            case 1:
                if(x-y==resultInt)
                    return true;
            default:
                return false;
        }
    }
    String stringFunction(int value,int x,int y){
        String function = "";

        function += String.valueOf(x);
        switch (value){
            case 0:
                function += " + ";
                break;
            case 1:
                function += " - ";
                break;
        }
        function += String.valueOf(y);

        return function;
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
