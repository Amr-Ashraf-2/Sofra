
package com.ashraf.amr.apps.sofra.data.model.restaurant.newcategory;

import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantcategories.CategoriesData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewCategory {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private CategoriesData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CategoriesData getData() {
        return data;
    }

    public void setData(CategoriesData data) {
        this.data = data;
    }

}
