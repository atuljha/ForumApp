package com.example.forumapp;

import java.io.Serializable;
import java.util.List;

public class Forum implements Serializable {

    String name;
    String createdAt;
    String title;
    String description;
    List<String> likes;
    String id;
    List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Forum() {
    }

    public Forum(String name, String createdAt, String title, String description, List<String> likes, List<Comment> comments) {
        this.name = name;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.likes = likes;
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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
}
