
package com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("region_id")
    @Expose
    private String regionId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("delivery_time")
    @Expose
    private String deliveryTime;
    @SerializedName("delivery_cost")
    @Expose
    private String deliveryCost;
    @SerializedName("minimum_charger")
    @Expose
    private String minimumCharger;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("whatsapp")
    @Expose
    private String whatsapp;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("availability")
    @Expose
    private String availability;
    @SerializedName("activated")
    @Expose
    private String activated;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("photo_url")
    @Expose
    private String photoUrl;

    @SerializedName("region")
    @Expose
    private RegionData region;

    private String apiToken = "";

    public RegionData getRegion() {
        return region;
    }

    public void setRegion(RegionData region) {
        this.region = region;
    }

    public RestaurantData() {

    }

    public RestaurantData(int id
            , String regionId,
                          String name,
                          String email,
                          String deliveryCost,
                          String minimumCharger,
                          String phone,
                          String whatsapp,
                          String photo,
                          String availability,
                          String activated,
                          String rate,
                          String photoUrl,
                          String apiToken,
                          String deliveryTime,
                          RegionData region
    ) {
        this.id = id;
        this.regionId = regionId;
        this.name = name;
        this.email = email;
        this.deliveryCost = deliveryCost;
        this.minimumCharger = minimumCharger;
        this.phone = phone;
        this.whatsapp = whatsapp;
        this.photo = photo;
        this.availability = availability;
        this.activated = activated;
        this.rate = rate;
        this.photoUrl = photoUrl;
        this.region = region;
        this.apiToken = apiToken;
        this.deliveryTime = deliveryTime;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
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

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getMinimumCharger() {
        return minimumCharger;
    }

    public void setMinimumCharger(String minimumCharger) {
        this.minimumCharger = minimumCharger;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;

    }

}
