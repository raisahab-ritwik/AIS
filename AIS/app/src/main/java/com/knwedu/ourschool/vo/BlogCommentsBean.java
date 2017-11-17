package com.knwedu.ourschool.vo;

public class BlogCommentsBean {

	private String name = "";
	private String createdAt = "";
	private String comment = "";
	private String comment_id = "";
	private String created_by = "";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getcreatedby() {
		return created_by;
	}
	public void setcreatedby(String created_by) {
		this.created_by = created_by;
	}
	public String getcommentid() {
		return comment_id;
	}
	public void setcommentid(String comment_id) {
		this.comment_id = comment_id;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
