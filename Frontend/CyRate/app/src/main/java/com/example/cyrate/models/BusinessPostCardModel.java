package com.example.cyrate.models;

public class BusinessPostCardModel {
    private int postId;
    private String postTxt;
    private String date;
    private String photoUrl;
    private int likeCount;
    private BusinessListCardModel business;

    /**
     *
     * @param postId
     * @param postTxt
     * @param date
     * @param photoUrl
     * @param business
     */
    public BusinessPostCardModel(int postId, String postTxt, String date, String photoUrl, BusinessListCardModel business, int likeCount) {
        this.postId = postId;
        this.postTxt = postTxt;
        this.date = date;
        this.photoUrl = photoUrl;
        this.business = business;
        this.likeCount = likeCount;
    }



    /**
     *
     * @return Post ID
     */
    public int getPostId() {
        return postId;
    }

    /**
     *
     * @param postId
     */
    public void setPostId(int postId) {
        this.postId = postId;
    }

    /**
     *
     * @return Post Body Text
     */
    public String getPostTxt() {
        return postTxt;
    }

    /**
     *
     * @param postTxt
     */
    public void setPostTxt(String postTxt) {
        this.postTxt = postTxt;
    }

    /**
     *
     * @return Date of Post
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return Attached post photo url
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     *
     * @param photoUrl
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     *
     * @return Business that made the post
     */
    public BusinessListCardModel getBusiness() {
        return business;
    }

    /**
     *
     * @param business
     */
    public void setBusiness(BusinessListCardModel business) {
        this.business = business;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
