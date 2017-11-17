package com.knwedu.ourschool.model;

/**
 * Created by ddasgupta on 1/13/2017.
 */

public class InstituteData {
    private String base_url;
    private String organizaion_id;
    private String organization_name;
    private String type;
    private String app_logo;
    private String app_type;
    private String verify_code;
    private String allies_type;

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }



    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getOrganizaion_id() {
        return organizaion_id;
    }

    public void setOrganizaion_id(String organizaion_id) {
        this.organizaion_id = organizaion_id;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }



    public String getApp_logo() {
        return app_logo;
    }

    public void setApp_logo(String app_logo) {
        this.app_logo = app_logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }


    public String getAllies_type() {
        return allies_type;
    }

    public void setAllies_type(String allies_type) {
        this.allies_type = allies_type;
    }
}
