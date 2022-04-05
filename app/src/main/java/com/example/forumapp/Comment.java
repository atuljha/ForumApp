package com.example.forumapp;

import java.io.Serializable;

public class Comment implements Serializable {

    String createdBy;
    String comment;
    String createdAt;

    public Comment() {
    }

    public Comment(String createdBy, String comment, String createdAt) {
        this.createdBy = createdBy;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
