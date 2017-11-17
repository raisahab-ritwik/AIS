package com.knwedu.ourschool.vo;

import java.util.ArrayList;

public class BlogBean {

	private String blogId = "";
	private String title = "";
	private String description = "";
	private String createdBy = "";
	private String createdAt = "";
	private String no_comments = "";
	private String image = "";
	private String isComment = "0";
	private ArrayList<BlogCommentsBean> commentList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public ArrayList<BlogCommentsBean> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<BlogCommentsBean> commentList) {
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

	public String getIsComment() {
		return isComment;
	}

	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}
	
	
	
}
