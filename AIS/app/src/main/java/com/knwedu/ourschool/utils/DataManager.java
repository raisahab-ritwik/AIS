package com.knwedu.ourschool.utils;


import android.location.Location;


public class DataManager {
    public static DataManager dManager;
Location location=null;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static DataManager getInstance() {

        if (dManager == null) {
            dManager = new DataManager();
            return dManager;
        } else {
            return dManager;
        }
    }




}
