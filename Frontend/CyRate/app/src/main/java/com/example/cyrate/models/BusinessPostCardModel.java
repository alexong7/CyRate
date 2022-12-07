package com.example.cyrate.models;

public class BusinessPostCardModel {
    private int postId;
    private String postTxt;
    private String date;
    private int likeCount;
    private BusinessListCardModel business;
    private byte[] blobPhoto;

    /**
     *
     * @param postId
     * @param postTxt
     * @param date
     * @param blobPhoto
     * @param business
     */
    public BusinessPostCardModel(int postId, String postTxt, String date, byte[] blobPhoto, BusinessListCardModel business) {
        this.postId = postId;
        this.postTxt = postTxt;
        this.date = date;
        this.blobPhoto = blobPhoto;
        this.business = business;
        this.likeCount = 0;
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


    public byte[] getBlobPhoto() {
        return blobPhoto;
    }

    public void setBlobPhoto(byte[] blobPhoto) {
        this.blobPhoto = blobPhoto;
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
