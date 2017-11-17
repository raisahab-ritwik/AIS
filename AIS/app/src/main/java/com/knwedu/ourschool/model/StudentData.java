package com.knwedu.ourschool.model;

import org.json.JSONObject;

/**
 * Created by ddasgupta on 25/03/17.
 */

public class StudentData {
    public String name;
    public String id;
    public String roll_no;
    public String user_code;
    public String registration_no;
    public JSONObject object;

    public StudentData(JSONObject c){
        this.object = c;


        try {
            this.name = c.getString("name").trim();
            this.id = c.getString("id").trim();
            this.roll_no = c.getString("roll_no").trim();
            this.user_code = c.getString("user_code").trim();
            this.registration_no = c.getString("registration_no").trim();

        }catch (Exception e){

        }


    }
}
