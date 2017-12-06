package com.lizarda.lizarda.model.deprecated;

/**
 * Created by arifinfrds on 11/20/17.
 */

public class Post {

    private String id;
    private String title;
    private String description;
    private String photoUrl;

    private String postOwnerId;
    private String commentsId;

    public Post() {
    }

    public Post(String id, String title, String description, String photoUrl, String postOwnerId, String commentsId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photoUrl = photoUrl;
        this.postOwnerId = postOwnerId;
        this.commentsId = commentsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPostOwnerId() {
        return postOwnerId;
    }

    public void setPostOwnerId(String postOwnerId) {
        this.postOwnerId = postOwnerId;
    }

    public String getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(String commentsId) {
        this.commentsId = commentsId;
    }
}
