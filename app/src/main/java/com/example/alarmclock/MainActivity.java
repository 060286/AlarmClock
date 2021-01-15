package com.example.alarmclock;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.alarmclock.alarm.AlarmFragment;
import com.example.alarmclock.helper.DBHelper;
import com.example.alarmclock.more.MoreFragment;
import com.example.alarmclock.news.NewsFragment;
import com.example.alarmclock.weather.WeatherFragment;



public class MainActivity extends AppCompatActivity {
    final int Code_Settings = 123001;
    ImageButton btnAlarm, btnWeather, btnNews, btnMore;
    TextView tvAlarm, tvWeather, tvNews, tvMore;
    DBHelper dbHelper = new DBHelper(this);
    AlarmFragment alarmFragment = new AlarmFragment(dbHelper);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);


        tvAlarm = findViewById(R.id.tvAlarm);
        tvNews = findViewById(R.id.tvNews);
        tvWeather = findViewById(R.id.tvWeather);
        tvMore = findViewById(R.id.tvMore);

        btnAlarm = findViewById(R.id.btnAlarm);
        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentContent(alarmFragment);
                highlightText(tvAlarm);
            }
        });

        btnWeather = findViewById(R.id.btnWeather);
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentContent(new WeatherFragment());
                highlightText(tvWeather);
            }
        });

        btnNews = findViewById(R.id.btnNews);
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentContent(new NewsFragment());
                highlightText(tvNews);
            }
        });

        btnMore = findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentContent(new MoreFragment());
                highlightText(tvMore);
            }
        });


        initAlarmFragment();
        highlightText(tvAlarm);

    }

    private void initAlarmFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.fragment, alarmFragment);

        ft.commit();

    }

    protected void replaceFragmentContent(Fragment fragment) {

        if (fragment != null) {

            FragmentManager fmgr = getSupportFragmentManager();

            FragmentTransaction ft = fmgr.beginTransaction();

            ft.replace(R.id.fragment, fragment);

            ft.commit();

        }

    }

    void highlightText(TextView tv) {
        tvWeather.setTextColor(Color.parseColor("#718792"));
        tvAlarm.setTextColor(Color.parseColor("#718792"));
        tvMore.setTextColor(Color.parseColor("#718792"));
        tvNews.setTextColor(Color.parseColor("#718792"));
        tv.setTextColor(getColor(R.color.colorText));
    }

}
