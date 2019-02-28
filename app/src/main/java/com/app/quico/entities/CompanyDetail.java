package com.app.quico.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CompanyDetail {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("service_id")
    @Expose
    private Integer serviceId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("is_feature")
    @Expose
    private Integer isFeature;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("avg_rate")
    @Expose
    private float avgRate;
    @SerializedName("review_count")
    @Expose
    private Integer reviewCount;
    @SerializedName("is_verified")
    @Expose
    private Integer isVerified;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("area_id")
    @Expose
    private Integer areaId;
    @SerializedName("icon_path")
    @Expose
    private String iconPath;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("is_favorite")
    @Expose
    private Integer isFavorite;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("thread_id")
    @Expose
    private String threadId;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("services_details")
    @Expose
    private ArrayList<ServicesDetail> servicesDetails = new ArrayList<>();
    @SerializedName("social_details")
    @Expose
    private ArrayList<SocialDetail> socialDetails = new ArrayList<>();
    @SerializedName("reviews_details")
    @Expose
    private ArrayList<ReviewsDetail> reviewsDetails = new ArrayList<>();
    @SerializedName("project_details")
    @Expose
    private ArrayList<ProjectDetail> projectDetails = new ArrayList<>();

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    private boolean isBackBtn=false;

    public boolean isBackBtn() {
        return isBackBtn;
    }

    public void setBackBtn(boolean backBtn) {
        isBackBtn = backBtn;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getIsFeature() {
        return isFeature;
    }

    public void setIsFeature(Integer isFeature) {
        this.isFeature = isFeature;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public float getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(float avgRate) {
        this.avgRate = avgRate;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Integer isVerified) {
        this.isVerified = isVerified;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Integer isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<ServicesDetail> getServicesDetails() {
        return servicesDetails;
    }

    public void setServicesDetails(ArrayList<ServicesDetail> servicesDetails) {
        this.servicesDetails = servicesDetails;
    }

    public ArrayList<SocialDetail> getSocialDetails() {
        return socialDetails;
    }

    public void setSocialDetails(ArrayList<SocialDetail> socialDetails) {
        this.socialDetails = socialDetails;
    }

    public ArrayList<ReviewsDetail> getReviewsDetails() {
        return reviewsDetails;
    }

    public void setReviewsDetails(ArrayList<ReviewsDetail> reviewsDetails) {
        this.reviewsDetails = reviewsDetails;
    }

    public ArrayList<ProjectDetail> getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(ArrayList<ProjectDetail> projectDetails) {
        this.projectDetails = projectDetails;
    }
}
