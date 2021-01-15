package com.example.alarmclock.todo;

public class Task {
    private int id;
    private String title, description, date;

    private int isComplete; // 0 - False, 1 - True

    public Task() {
    }

    public Task(int _id, String _Title, String _Description, String _Date) {
        this.id = _id;
        this.title = _Title;
        this.description = _Description;
        this.date = _Date;
        this.isComplete = 0;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String _Title) {
        this.title = _Title;
    }
    public String getTitle() {
        return title;
    }
    public String getTaskDescription() {
        return description;
    }
    public void setDescription(String _Description) {
        this.description = _Description;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String _Date) {
        this.date = _Date;
    }
    public int getStatus() {
        return isComplete;
    }
    public void setStatus(int _isComplete) {
        this.isComplete = _isComplete;
    }
}
