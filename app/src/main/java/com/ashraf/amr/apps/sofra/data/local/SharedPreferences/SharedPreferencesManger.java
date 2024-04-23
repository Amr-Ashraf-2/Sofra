package com.ashraf.amr.apps.sofra.data.local.SharedPreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.City;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Region;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.CityData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RegionData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;


public class SharedPreferencesManger {
    private static SharedPreferences sharedPreferences = null;
    public static String RESTAURANT_ID = "RESTAURANT_ID";
    private static String RESTAURANT_REGION_ID = "RESTAURANT_REGION_ID";
    private static String RESTAURANT_REGION_NAME = "RESTAURANT_REGION_NAME";
    public static String RESTAURANT_CITY_ID = "RESTAURANT_CITY_ID";
    public static String RESTAURANT_CITY_NAME = "RESTAURANT_CITY_NAME";
    public static String RESTAURANT_NAME = "RESTAURANT_NAME";
    private static String RESTAURANT_EMAIL = "RESTAURANT_EMAIL";
    private static String RESTAURANT_DELIVERY_COS = "RESTAURANT_DELIVERY_COS";
    private static String RESTAURANT_MINIMUMCHAR = "RESTAURANT_MINIMUMCHAR";
    private static String RESTAURANT_PHONE = "RESTAURANT_PHONE";
    private static String RESTAURANT_PHOTO = "RESTAURANT_PHOTO";
    private static String RESTAURANT_AVAILABILITY = "RESTAURANT_AVAILABILITY";
    private static String RESTAURANT_RATE = "RESTAURANT_RATE";
    private static String RESTAURANT_WHATSAPP = "RESTAURANT_WHATSAPP";
    private static String RESTAURANT_ACTIVATED = "RESTAURANT_ACTIVATED";
    private static String RESTAURANT_PHOTOURL = "RESTAURANT_PHOTOURL";
    public static String RESTAURANT_API_TOKEN = "RESTAURANT_API_TOKEN";
    public static String RESTAURANT_PASSWORD = "RESTAURANT_PASSWORD";
    public static String RESTAURANT_DELIVERY_TIME = "RESTAURANT_DELIVERY_TIME";

    public static String REMEMBER = "REMEMBER";

    public static String WHERE = "WHERE";
    public static String OFFER = "OFFER";
    public static String MAIN = "MAIN";


    private static String CLIENT_ID = "CLIENT_ID";
    private static String CLIENT_REGION_ID = "CLIENT_REGION_ID";
    private static String CLIENT_REGION_NAME = "CLIENT_REGION_NAME";
    private static String CLIENT_CITY_ID = "CLIENT_CITY_ID";
    private static String CLIENT_CITY_NAME = "CLIENT_CITY_NAME";
    private static String CLIENT_NAME = "CLIENT_NAME";
    private static String CLIENT_EMAIL = "CLIENT_EMAIL";
    private static String CLIENT_PHONE = "CLIENT_PHONE";
    public static String CLIENT_API_TOKEN = "CLIENT_API_TOKEN";
    private static String CLIENT_Address = "CLIENT_Address";
    private static String CLIENT_profile_image = "CLIENT_profile_image";
    private static String CLIENT_PHOTO_URL = "CLIENT_PHOTO_URL";
    public static String CLIENTPASSWORD = "CLIENTPASSWORD";

    public static String CLIENTREMEMBER = "CLIENTREMEMBER";
    public static String CLIENTLOGINTYPE = "CLIENTLOGINTYPE";


    public static String USER_TYPE = "USER_TYPE";
    public static String CLIENT = "CLIENT";
    public static String RESTAURANT = "RESTAURANT";


    public static void setUserName(Context context, String REMEMBER) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REMEMBER, (REMEMBER));
        editor.commit();
    }

    public static void setClientName(Context context, String CLIENTREMEMBER) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLIENTREMEMBER, (CLIENTREMEMBER));
        editor.commit();
    }

    public static void setClientLooginType(Context context, String CLIENTLOGINTYPE) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLIENTLOGINTYPE, (CLIENTLOGINTYPE));
        editor.commit();
    }

    public static String getUserName(Context context) {
        return sharedPreferences.getString(REMEMBER, "");
    }

    public static String getClientName(Context context) {
        return sharedPreferences.getString(CLIENTREMEMBER, "");
    }

    public static String getClientLooginType(Context context) {
        return sharedPreferences.getString(CLIENTLOGINTYPE, "");
    }

    public static void setSharedPreferences(Activity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences(
                    "Sofra", activity.MODE_PRIVATE);
        }
    }

    public static void SaveData(Activity activity, String data_Key, String data_Value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(data_Key, data_Value);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static void SaveBoolean(Activity activity, String data_Key, boolean data_Value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(data_Key, data_Value);
            editor.commit();
        } else {
            setSharedPreferences(activity);
        }
    }

    public static String LoadData(Activity activity, String data_Key) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } else {
            setSharedPreferences(activity);
        }

        return sharedPreferences.getString(data_Key, null);
    }

    public static boolean LoadBoolean(Activity activity, String data_Key) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
        } else {
            setSharedPreferences(activity);
        }

        return sharedPreferences.getBoolean(data_Key, false);
    }

    public static void saveRestaurantData(Activity activity, RestaurantData user) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(RESTAURANT_ID, user.getId());

            editor.putString(RESTAURANT_REGION_NAME, user.getRegion().getName());
            editor.putInt(RESTAURANT_REGION_ID, user.getRegion().getId());

            editor.putString(RESTAURANT_CITY_NAME, user.getRegion().getCity().getName());
            editor.putInt(RESTAURANT_CITY_ID, user.getRegion().getCity().getId());

            editor.putString(RESTAURANT_WHATSAPP, user.getWhatsapp());
            editor.putString(RESTAURANT_ACTIVATED, user.getActivated());
            editor.putString(RESTAURANT_PHOTOURL, user.getPhotoUrl());
            editor.putString(RESTAURANT_NAME, user.getName());
            editor.putString(RESTAURANT_EMAIL, user.getEmail());
            editor.putString(RESTAURANT_DELIVERY_COS, user.getDeliveryCost());
            editor.putString(RESTAURANT_DELIVERY_TIME, user.getDeliveryTime());
            editor.putString(RESTAURANT_MINIMUMCHAR, user.getMinimumCharger());
            editor.putString(RESTAURANT_PHONE, user.getPhone());
            editor.putString(RESTAURANT_PHOTO, user.getPhoto());
            editor.putString(RESTAURANT_AVAILABILITY, user.getAvailability());
            editor.putString(RESTAURANT_API_TOKEN, user.getApiToken());
            editor.putString(RESTAURANT_RATE, user.getRate());

            editor.commit();
        } else {

        }

    }

    public static void saveClientData(Activity activity, Client client) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(CLIENT_ID, client.getId());
            editor.putString(CLIENT_REGION_NAME, client.getRegion().getName());
            editor.putInt(CLIENT_REGION_ID, client.getRegion().getId());
            editor.putString(CLIENT_CITY_NAME, client.getRegion().getCity().getName());
            editor.putInt(CLIENT_CITY_ID, client.getRegion().getCity().getId());

            editor.putString(CLIENT_PHOTO_URL, client.getPhotoUrl());
            editor.putString(CLIENT_NAME, client.getName());
            editor.putString(CLIENT_EMAIL, client.getEmail());
            editor.putString(CLIENT_PHONE, client.getPhone());
            editor.putString(CLIENT_profile_image, client.getProfileImage());
            editor.putString(CLIENT_API_TOKEN, client.getApiToken());
            editor.putString(CLIENT_Address, client.getAddress());

            editor.commit();
        } else {

        }

    }

    public static RestaurantData loadRestaurantData(Activity activity) {
        RestaurantData user = new RestaurantData();
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int id = sharedPreferences.getInt(RESTAURANT_ID, 0);
            String name = sharedPreferences.getString(RESTAURANT_NAME, null);
            String email = sharedPreferences.getString(RESTAURANT_EMAIL, null);
            String deliveryCost = sharedPreferences.getString(RESTAURANT_DELIVERY_COS, null);
            String minimumCharger = sharedPreferences.getString(RESTAURANT_MINIMUMCHAR, null);
            String phone = sharedPreferences.getString(RESTAURANT_PHONE, null);
            String photo = sharedPreferences.getString(RESTAURANT_PHOTO, null);
            String availability = sharedPreferences.getString(RESTAURANT_AVAILABILITY, null);
            String whatsapp = sharedPreferences.getString(RESTAURANT_WHATSAPP, null);
            String activated = sharedPreferences.getString(RESTAURANT_ACTIVATED, null);
            String rate = sharedPreferences.getString(RESTAURANT_RATE, null);
            String photoUrl = sharedPreferences.getString(RESTAURANT_PHOTOURL, null);
            String apiToken = sharedPreferences.getString(RESTAURANT_API_TOKEN, null);
            String login = sharedPreferences.getString(REMEMBER, null);
            String deliveryTime = sharedPreferences.getString(RESTAURANT_DELIVERY_TIME, null);

            RegionData region = new RegionData();
            region.setId(sharedPreferences.getInt(RESTAURANT_REGION_ID, 0));
            region.setName(sharedPreferences.getString(RESTAURANT_REGION_NAME, null));

            region.setCityId(String.valueOf(sharedPreferences.getInt(RESTAURANT_CITY_ID, 0)));
            CityData cityData = new CityData();
            cityData.setName(sharedPreferences.getString(RESTAURANT_CITY_NAME, null));
            cityData.setId(sharedPreferences.getInt(RESTAURANT_CITY_ID, 0));
            region.setCity(cityData);

            user = new RestaurantData(id, String.valueOf(region.getId()), name, email, deliveryCost,
                    minimumCharger, phone, whatsapp, photo, availability, activated, rate,
                    photoUrl, apiToken, deliveryTime, region);

            editor.commit();
        } else {

        }
        return user;
    }

    public static Client loadClientData(Activity activity) {
        Client client = new Client();
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int id = sharedPreferences.getInt(CLIENT_ID, 0);
            String name = sharedPreferences.getString(CLIENT_NAME, null);
            String email = sharedPreferences.getString(CLIENT_EMAIL, null);
            String phone = sharedPreferences.getString(CLIENT_PHONE, null);
            String profileImage = sharedPreferences.getString(CLIENT_profile_image, null);
            String Photo_url = sharedPreferences.getString(CLIENT_PHOTO_URL, null);
            //String apiToken = sharedPreferences.getString(CLIENT_API_TOKEN, null);
            String apiToken = "K1X6AzRlJFeVbGnHwGYsdCu0ETP1BqYC7DpMTZ3zLvKgU5feHMvsEEnKTpzh";
            String address = sharedPreferences.getString(CLIENT_Address, null);

            Region region = new Region();
            region.setId(sharedPreferences.getInt(CLIENT_REGION_ID, 0));
            region.setName(sharedPreferences.getString(CLIENT_REGION_NAME, null));

            region.setCityId(String.valueOf(sharedPreferences.getInt(CLIENT_CITY_ID, 0)));

            City cityData = new City();
            cityData.setName(sharedPreferences.getString(CLIENT_CITY_NAME, null));
            cityData.setId(sharedPreferences.getInt(CLIENT_CITY_ID, 0));
            region.setCity(cityData);

            client = new Client(id, name, email, phone, String.valueOf(region.getId()), address, profileImage, Photo_url, region, apiToken);

            editor.commit();
        } else {

        }
        return client;
    }

    public static void clean(Activity activity) {
        setSharedPreferences(activity);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
    }

}
