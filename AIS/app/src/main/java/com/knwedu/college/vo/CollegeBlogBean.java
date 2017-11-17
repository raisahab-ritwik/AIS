package com.knwedu.college.vo;

import java.util.ArrayList;

public class CollegeBlogBean {

	private String blogId = "";
	private String title = "";
	private String description = "";
	private String createdBy = "";
	private String createdAt = "";
	private String comment_status = "";
	private String file_name = "";
	private String no_comments = "";
	private String image = "";
	private ArrayList<CollegeBlogCommentsBean> commentList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getfilename() {
		return file_name;
	}

	public void setfilename(String file_name) {
		this.file_name = file_name;
	}
	
	public String getcommentstatus() {
		return comment_status;
	}

	public void setcommentstatus(String comment_status) {
		this.comment_status = comment_status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public ArrayList<CollegeBlogCommentsBean> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<CollegeBlogCommentsBean> commentList) {
		this.commentList = commentList;
	}

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getNo_comments() {
		return no_comments;
	}

	public void setNo_comments(String no_comments) {
		this.no_comments = no_comments;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	
}
