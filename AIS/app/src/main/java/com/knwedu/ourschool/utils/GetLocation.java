package com.knwedu.ourschool.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


public class GetLocation implements LocationListener {

    protected LocationManager locationManager;
    Context context;
    private static final long MIN_DISTANCE_FOR_UPDATE = 0;
    private static final long MIN_TIME_FOR_UPDATE = 0;
    private getCurrentLocation listener;



    public GetLocation(Context context, LocationManager locationManager, getCurrentLocation listener) {
        this.listener = listener;
        this.context = context;
        this.locationManager = locationManager;

        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null){
            DataManager.getInstance().setLocation(location);
            listener.getLocation(location);

        }else
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        DataManager.getInstance().setLocation(location);
        listener.getLocation(location);

        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
       // Toast.makeText(context, "onProviderEnabled>>>" + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public interface getCurrentLocation {
        public void getLocation(Location location);
    }
}
