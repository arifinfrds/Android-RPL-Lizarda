package com.lizarda.lizarda.model;

import java.util.Date;

/**
 * Created by arifinfrds on 11/20/17.
 */

public class Comment {

    private String id;
    private String commentOwnerId;
    private String text;
    private Date dateCreated;

    public Comment() {
    }

    public Comment(String id, String commentOwnerId, String text, Date dateCreated) {
        this.id = id;
        this.commentOwnerId = commentOwnerId;
        this.text = text;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentOwnerId() {
        return commentOwnerId;
    }

    public void setCommentOwnerId(String commentOwnerId) {
        this.commentOwnerId = commentOwnerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
