package com.example.android.farm;

/**
 * Created by Tasos on 21-Dec-17.
 */

public class Problem {

    private String desc;
    private String name;
    private String userid;
    private String uniqueID;
    private double longitude;
    private double latitude;
    private String lux;
    private String temperature;

    public Problem(String desc, String name, String userid, String uniqueID, double longitude, double latitude, String lux, String temperature) {
        this.desc = desc;
        this.name = name;
        this.userid = userid;
        this.uniqueID = uniqueID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.lux = lux;
        this.temperature = temperature;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLux() {
        return lux;
    }

    public void setLux(String lux) {
        this.lux = lux;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}