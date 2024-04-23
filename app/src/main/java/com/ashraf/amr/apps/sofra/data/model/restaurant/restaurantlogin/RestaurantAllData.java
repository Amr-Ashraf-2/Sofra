
package com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantAllData {

    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("user")
    @Expose
    private RestaurantData user;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public RestaurantData getUser() {
        return user;
    }

    public void setUser(RestaurantData user) {
        this.user = user;
    }

}
