package com.knwedu.ourschool.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ddasgupta on 2/27/2017.
 */

public class ListBusData  {
    public String transport_fee;
    public String id;
    public String begin_time;
    public String end_time;
    public String route_number;
    public String description;
    public String operator_name;
    public String emergency_contact_number;
    public String attachment;
    public String transport_coupon;
    public JSONObject object;

    public ListBusData(JSONObject c){
        this.object = c;
        try {
            this.route_number = c.getString("route_number").trim();
            this.transport_fee = c.getString("transport_fee").trim();
            this.id = c.getString("id").trim();
            this.begin_time = c.getString("begin_time").trim();
            this.end_time = c.getString("end_time").trim();

            this.description = c.getString("description").trim();
            this.operator_name = c.getString("operator_name").trim();
            this.emergency_contact_number = c.getString("emergency_contact_number").trim();
            this.attachment = c.getString("attachment").trim();
            this.transport_coupon = c.getString("transport_coupon").trim();

        } catch (Exception e) {
        }

    }





}
