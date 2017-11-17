package com.knwedu.ourschool.model;

import org.json.JSONObject;

/**
 * Created by ddasgupta on 26/03/17.
 */

public class AimsSubject {
    public String id;
    public String sub_name;
    public JSONObject object;

    public AimsSubject(JSONObject c){
        this.object = c;
        try {
            this.sub_name = c.getString("sub_name").trim();
            this.id = c.getString("id").trim();
        }catch (Exception e){

        }

    }



}
