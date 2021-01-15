package com.example.alarmclock.todo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmclock.R;
import com.example.alarmclock.todo.adapter.TodoAdapter;

import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnFocusChangeListener{
    ListView listView;
    ImageButton btnAddTodo;
    ArrayList<com.example.alarmclock.todo.Task> taskArrayList;
    TodoAdapter taskArrayAdapter;

    LayoutInflater mLayoutInflater;

    com.example.alarmclock.todo.DB Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_todo);

        mLayoutInflater = LayoutInflater.from(this);
        Helper = new com.example.alarmclock.todo.DB(this);
        listView = (ListView) findViewById(R.id.listView);
        taskArrayList = Helper.getAllTasks();
        taskArrayAdapter = new TodoAdapter(this, taskArrayList);

        listView.setAdapter(taskArrayAdapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        btnAddTodo = findViewById(R.id.btnAddTodo);
        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = mLayoutInflater.inflate(R.layout.task_new, null);
                //view.findViewById(R.id.txtTitle).setOnFocusChangeListener(this);
                AlertDialog.Builder mBuilder = createAlertDialog(view);
                mBuilder.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todolist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newTaskMenu:
            {
                View view = mLayoutInflater.inflate(R.layout.task_new, null);

                view.findViewById(R.id.txtTitle).setOnFocusChangeListener(this);
                AlertDialog.Builder mBuilder = createAlertDialog(view);
                mBuilder.show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshListView() {
        taskArrayList = Helper.getAllTasks();

        taskArrayAdapter.setTaskList(taskArrayList);
        taskArrayAdapter.notifyDataSetChanged();
    }

    private AlertDialog.Builder createAlertDialog(final View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        mBuilder.setView(view);
        mBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText titleTxt = (EditText) view.findViewById(R.id.txtTitle);
                String title = titleTxt.getText().toString().trim();
                EditText descTxt = (EditText) view.findViewById(R.id.txtDescription);
                String description = descTxt.getText().toString();
                DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
                String targetDate = String.format("%02d", datePicker.getDayOfMonth()) +"/" +
                        String.format("%02d", Integer.valueOf(datePicker.getMonth()+1)) + "/" + datePicker.getYear();

                if (title.isEmpty())
                {
                    Toast.makeText(com.example.alarmclock.todo.TodoListActivity.this, "Task empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Helper.insertTask(title, description, targetDate, 0);

                com.example.alarmclock.todo.TodoListActivity.this.refreshListView();
            }
        });
        mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mBuilder.setCancelable(true);
        return mBuilder;
    }

    private AlertDialog.Builder editTaskDialog(final View view, final com.example.alarmclock.todo.Task curTask) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        mBuilder.setView(view);
        mBuilder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Helper.deleteTask(curTask.getId());

                com.example.alarmclock.todo.TodoListActivity.this.refreshListView();
            }
        });
        mBuilder.setPositiveButton("SAVE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText titleTxt = (EditText) view.findViewById(R.id.txtTitle);
                String title = titleTxt.getText().toString().trim();
                EditText descTxt = (EditText) view.findViewById(R.id.txtDescription);
                String description = descTxt.getText().toString();
                DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
                String targetDate = String.format("%02d", datePicker.getDayOfMonth()) +"/" +
                        String.format("%02d", Integer.valueOf(datePicker.getMonth()+1)) + "/" + datePicker.getYear();

                if (title.isEmpty())
                {
                    Toast.makeText(com.example.alarmclock.todo.TodoListActivity.this, "Task not updated!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Helper.updateTask(curTask, title, description, targetDate);

                com.example.alarmclock.todo.TodoListActivity.this.refreshListView();
            }
        });
        mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mBuilder.setCancelable(true);
        return mBuilder;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        com.example.alarmclock.todo.Task curTask = (com.example.alarmclock.todo.Task) parent.getAdapter().getItem(position);

        if (curTask.getStatus() == 1) {
            int updateCount = Helper.updateTaskStatus(curTask.getId(), 0);
            refreshListView();
            return;
        }

        int updateCount = Helper.updateTaskStatus(curTask.getId(), 1);

        refreshListView();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        com.example.alarmclock.todo.Task currentTask = (com.example.alarmclock.todo.Task)parent.getAdapter().getItem(position);

        View dialogView = mLayoutInflater.inflate(R.layout.task_new, null);

        EditText txtTitle = (EditText) dialogView.findViewById(R.id.txtTitle);
        EditText txtDescription = (EditText) dialogView.findViewById(R.id.txtDescription);
        DatePicker targetDate = (DatePicker) dialogView.findViewById(R.id.datePicker);

        txtTitle.setOnFocusChangeListener(this);

        txtTitle.setText(currentTask.getTitle());
        txtDescription.setText(currentTask.getTaskDescription());
        String[] dateArray = currentTask.getDate().split("/");
        targetDate.init (Integer.valueOf(dateArray[2]),
                Integer.valueOf(dateArray[1])-1,
                Integer.valueOf(dateArray[0]),
                null);

        AlertDialog.Builder mBuilder = editTaskDialog(dialogView, currentTask);
        mBuilder.show();

        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.txtTitle) {
            EditText title = (EditText)v;
            if (title.getText().length() < 1)
            {
                title.setError("Title cant be empty!!");
            }
        }
    }
}
