package com.example.alarmclock.weather;

public class Weather {
    public String Date;
    public String Status;
    public String img;
    public String maxTemp;
    public String minTemp;

    public Weather(String date, String status, String img, String maxTemp, String minTemp) {
        Date = date;
        Status = status;
        this.img = img;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }
}
