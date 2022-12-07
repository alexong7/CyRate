package com.example.cyrate.models;

public class BusinessListCardModel {
    private int busId;
    private String busName;
    private String busType;
    private String phoneNumber;
    private String photoUrl;
    private String hours;
    private String location;
    private int ownerId;
    private String menuLink;
    private String priceGauge;

    //-------review's stuff--------//

    private int reviewSum;
    private int reviewCount;

    //-------favorites stuff--------//
    private int fid;

    /**
     *
     * @param busId
     * @param busName
     * @param busType
     * @param phoneNumber
     * @param photoUrl
     * @param hours
     * @param location
     * @param ownerId
     * @param menuLink
     * @param priceGauge
     * @param reviewSum
     * @param reviewCount
     */
    public BusinessListCardModel(int busId, String busName, String busType, String phoneNumber, String photoUrl,
                                 String hours, String location, int ownerId, String menuLink,
                                 String priceGauge, int reviewSum, int reviewCount) {
        this.busId = busId;
        this.busName = busName;
        this.busType = busType;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.hours = hours;
        this.location = location;
        this.ownerId = ownerId;
        this.menuLink = menuLink;
        this.priceGauge = priceGauge;
        this.reviewSum = reviewSum;
        this.reviewCount = reviewCount;
        this.fid = -1;
    }

    /**
     *
     * @return Business ID
     */
    public int getBusId() {
        return busId;
    }

    /**
     *
     * @return Favorite ID
     */
    public int getFid(){
        return fid;
    }

    /**
     * set favorites id for this business card to fid
     * @param fid
     */
    public void setFid(int fid){
        this.fid = fid;
    }

    /**
     *
     * @param busId
     */
    public void setBusId(int busId) {
        this.busId = busId;
    }

    /**
     *
     * @return Business Name
     */
    public String getBusName() {
        return busName;
    }

    /**
     *
     * @param busName
     */
    public void setBusName(String busName) {
        this.busName = busName;
    }

    /**
     *
     * @return Business Type
     */
    public String getBusType() {
        return busType;
    }

    /**
     *
     * @param busType
     */
    public void setBusType(String busType) {
        this.busType = busType;
    }

    /**
     *
     * @return Business Phone Number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return Business Profile Picture Url
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
     * @return Business Hours
     */
    public String getHours() {
        return hours;
    }

    /**
     *
     * @param hours
     */
    public void setHours(String hours) {
        this.hours = hours;
    }

    /**
     *
     * @return Business Location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return Business Owner (User) ID
     */
    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    public String getPriceGauge() {
        return priceGauge;
    }

    public void setPriceGauge(String priceGauge) {
        this.priceGauge = priceGauge;
    }

    public int getReviewSum() {
        return reviewSum;
    }

    public void setReviewSum(int reviewSum) {
        this.reviewSum = reviewSum;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    @Override
    public String toString() {
        return "RestaurantListCardModel{" +
                "busId=" + busId +
                ", busName='" + busName + '\'' +
                ", busType='" + busType + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", hours='" + hours + '\'' +
                ", location='" + location + '\'' +
                ", ownerId=" + ownerId +
                ", menuLink='" + menuLink + '\'' +
                ", priceGauge='" + priceGauge + '\'' +
                ", reviewSum=" + reviewSum +
                ", reviewCount=" + reviewCount +
                '}';
    }
}
