package com.yp.dialogflowtest;

public class Message {

    private String name;
    private String msg;
    private Type type;
    private WeatherInfo weatherInfo;

    public Message(String name, String msg, Type type){
        this.name = name;
        this.msg = msg;
        this.type = type;
    }

    public enum Type{
        USER,ROBOT_NORMAL,ROBOT_WEATHER
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }
}
