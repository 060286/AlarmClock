package com.example.alarmclock.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.alarmclock.R;
import com.example.alarmclock.relax.RelaxActivity;
import com.example.alarmclock.todo.TodoListActivity;


public class MoreFragment extends Fragment {
    private static final int REQUEST_CODE_LOGIN = 0x9345;
    private static final String TAG = "More fragment";
    private Button btnTodo, btnTheme, btnSleep;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        btnTodo = view.findViewById(R.id.btnTodoList);
        btnTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openTodo = new Intent(getContext(), TodoListActivity.class);
                startActivity(openTodo);
            }
        });

        btnSleep = view.findViewById(R.id.btnSleepMusic);
        btnSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSleep = new Intent(getContext(), RelaxActivity.class);
                startActivity(openSleep);
            }
        });

        btnTheme = view.findViewById(R.id.btnTheme);
        btnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                onCreateView(inflater, container, savedInstanceState);
            }
        });
        return view;
    }
}
