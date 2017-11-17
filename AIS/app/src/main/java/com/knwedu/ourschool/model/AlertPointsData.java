package com.knwedu.ourschool.model;

import org.json.JSONObject;

/**
 * Created by ddasgupta on 2/28/2017.
 */

public class AlertPointsData {

    public String id;
    public String child_id;
    public String user_id;
    public String route_id;
    public String latitude;
    public String longitude;
    public String position_name;
    public String notification_status;
    public String custom_name;

    public JSONObject object;

    public AlertPointsData(JSONObject c){
        this.object = c;
        try {
            this.id = c.getString("id").trim();
            this.child_id = c.getString("child_id").trim();
            this.id = c.getString("id").trim();
            this.route_id = c.getString("route_id").trim();
            this.latitude = c.getString("latitude").trim();
            this.longitude = c.getString("longitude").trim();
            this.position_name = c.getString("position_name").trim();
            this.notification_status = c.getString("notification_status").trim();
            this.custom_name=c.getString("custom_name").trim();
        }catch (Exception e) {
        }

    }

}
