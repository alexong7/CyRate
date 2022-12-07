package com.example.cyrate.models;

public class CommentThreadCardModel {
    private String name;
    private String photoUrl;
    private String commentBody;
    private String date;

    public CommentThreadCardModel(String name, String photoUrl, String commentBody, String date) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.commentBody = commentBody;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
