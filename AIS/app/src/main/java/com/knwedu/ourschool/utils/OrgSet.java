package com.knwedu.ourschool.utils;


public class OrgSet {
	private String title, thumbnailUrl;
	private String baseurl;
	private String orgid;
	private String org_name;
	private String footerUrl;
	private String type;
	private String app_type;
	private String password_protection;


	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getbaseurl() {
		return baseurl;
	}

	public void setbaseurl(String baseurl) {
		this.baseurl = baseurl;
	}

	public String getorgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}


	public String getFooterUrl() {
		return footerUrl;
	}

	public void setFooterUrl(String footerUrl) {
		this.footerUrl = footerUrl;
	}


	public String getorg_name() {
		return org_name;
	}
	public void setetorg_name(String Org_name){
	this.org_name=Org_name;
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

	public String getPassword_protection() {
		return password_protection;
	}

	public void setPassword_protection(String password_protection) {
		this.password_protection = password_protection;
	}



}
