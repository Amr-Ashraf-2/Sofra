package com.ashraf.amr.apps.sofra.data.api;


import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.ClientLogin;
import com.ashraf.amr.apps.sofra.data.model.client.clientresetpass.ClientResetPass;
import com.ashraf.amr.apps.sofra.data.model.client.clientshoworder.ClientShowOrder;
import com.ashraf.amr.apps.sofra.data.model.client.listOfOrders.AddNewOrder;
import com.ashraf.amr.apps.sofra.data.model.client.listOfOrders.ListOfOrders;
import com.ashraf.amr.apps.sofra.data.model.client.newpassword.NewPassword;
import com.ashraf.amr.apps.sofra.data.model.general.addcomment.AddComment;
import com.ashraf.amr.apps.sofra.data.model.general.categorieslist.CategoriesList;
import com.ashraf.amr.apps.sofra.data.model.general.cities.Cities;
import com.ashraf.amr.apps.sofra.data.model.general.comments.Comments;
import com.ashraf.amr.apps.sofra.data.model.general.detils.Detils;
import com.ashraf.amr.apps.sofra.data.model.general.listofoffers.ListOfOffers;
import com.ashraf.amr.apps.sofra.data.model.general.regions.Regions;
import com.ashraf.amr.apps.sofra.data.model.general.restaurantslist.RestaurantsList;
import com.ashraf.amr.apps.sofra.data.model.notifications.clientnotificationlist.ClientNotificationList;
import com.ashraf.amr.apps.sofra.data.model.notifications.restaurantnotificationlist.RestaurantNotificationList;
import com.ashraf.amr.apps.sofra.data.model.restaurant.addnewproduct.AddNewProduct;
import com.ashraf.amr.apps.sofra.data.model.restaurant.commissions.Commissions;
import com.ashraf.amr.apps.sofra.data.model.restaurant.deletoffer.DeletOffer;
import com.ashraf.amr.apps.sofra.data.model.restaurant.deletproducts.DeletProducts;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.MyProducts;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myrequests.MyRequests;
import com.ashraf.amr.apps.sofra.data.model.restaurant.newcategory.NewCategory;
import com.ashraf.amr.apps.sofra.data.model.restaurant.newoffer.NewOffer;
import com.ashraf.amr.apps.sofra.data.model.restaurant.offerlist.OfferList;
import com.ashraf.amr.apps.sofra.data.model.restaurant.rejectrequests.RejectRequests;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantcategories.RestaurantCatogries;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantLogin;
import com.ashraf.amr.apps.sofra.data.model.restaurant.state.State;
import com.ashraf.amr.apps.sofra.data.model.restaurant.updatProduct.UpdatProduct;
import com.ashraf.amr.apps.sofra.data.model.restaurant.updateoffer.UpdateOffer;
import com.ashraf.amr.apps.sofra.data.model.settings.Settings;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiServices {

    @POST("restaurant/login")
    @FormUrlEncoded
    Call<RestaurantLogin> getRestaurantlogin(@Field("email") String email,
                                             @Field("password") String password);

    @GET("cities")
    Call<Cities> getcities();

    //
    @GET("regions")
    Call<Regions> getRegions(@Query("city_id") int city_id);


    @POST("restaurant/reset-password")
    @FormUrlEncoded
    Call<GeneralResponse> restaurantResetPassword(@Field("email") String email);

    @POST("restaurant/new-password")
    @FormUrlEncoded
    Call<GeneralResponse> getRestaurantNewpassword(@Field("code") String code,
                                                   @Field("password") String password,
                                                   @Field("password_confirmation") String password_confirmation);

    @GET("restaurant/my-offers")
    Call<OfferList> getOffers(@Query("api_token") String api_token,
                              @Query("page") int page);

    @GET("restaurant/my-categories")
    Call<RestaurantCatogries> getCatogries(@Query("api_token") String api_token,
                                           @Query("page") int page);

    @GET("categories")
    Call<CategoriesList> getCategoriesList(@Query("restaurant_id") int restaurant_id,
                                           @Query("prepend") int prepend);

    @POST("restaurant/new-offer")
    @Multipart
    Call<NewOffer> postnewoffer(@Part("description") RequestBody description,
                                @Part("price") RequestBody price,
                                @Part("starting_at") RequestBody starting_at,
                                @Part("ending_at") RequestBody ending_at,
                                @Part("name") RequestBody name,
                                @Part("api_token") RequestBody api_token,
                                @Part MultipartBody.Part photo);

    @POST("restaurant/update-offer")
    @Multipart
    Call<UpdateOffer> updateOffer(@Part("description") RequestBody description,
                                  @Part("price") RequestBody price,
                                  @Part("starting_at") RequestBody starting_at,
                                  @Part("ending_at") RequestBody ending_at,
                                  @Part("name") RequestBody name,
                                  @Part("api_token") RequestBody api_token,
                                  @Part("offer_id") RequestBody offer_id,
                                  @Part MultipartBody.Part photo);

    @POST("restaurant/update-offer")
    @Multipart
    Call<UpdateOffer> updateOfferNoImage(@Part("description") RequestBody description,
                                         @Part("price") RequestBody price,
                                         @Part("starting_at") RequestBody starting_at,
                                         @Part("ending_at") RequestBody ending_at,
                                         @Part("name") RequestBody name,
                                         @Part("api_token") RequestBody api_token,
                                         @Part("offer_id") RequestBody offer_id);

    @POST("restaurant/delete-offer")
    @FormUrlEncoded
    Call<DeletOffer> deletOffer(@Field("offer_id") int offer_id,
                                @Field("api_token") String api_token);

    @GET("restaurant/my-items")
    Call<MyProducts> getProductsList(@Query("api_token") String api_token,
                                     @Query("page") int page,
                                     @Query("category_id") int category_id);

    //
    @POST("restaurant/delete-item")
    @FormUrlEncoded
    Call<DeletProducts> deletProduct(@Field("item_id") int item_id,
                                     @Field("api_token") String api_token);

    //
    @POST("restaurant/new-item")
    @Multipart
    Call<AddNewProduct> addproduct(@Part("description") RequestBody description,
                                   @Part("price") RequestBody price,
                                   @Part("name") RequestBody name,
                                   @Part("api_token") RequestBody api_token,
                                   @Part MultipartBody.Part photo,
                                   @Part("offer_price") RequestBody offer_price,
                                   @Part("category_id") RequestBody category_id);

    @GET("restaurant/my-orders")
    Call<MyRequests> getRequests(@Query("api_token") String api_token,
                                 @Query("state") String state,
                                 @Query("page") int page);

    @POST("restaurant/sign-up")
    @Multipart
    Call<RestaurantLogin> register(@Part MultipartBody.Part photo,
                                   @Part("name") RequestBody name,
                                   @Part("email") RequestBody email,
                                   @Part("delivery_time") RequestBody delivery_time,
                                   @Part("password") RequestBody password,
                                   @Part("password_confirmation") RequestBody password_confirmation,
                                   @Part("phone") RequestBody phone,
                                   @Part("whatsapp") RequestBody whatsapp,
                                   @Part("region_id") RequestBody region_id,
                                   @Part("delivery_cost") RequestBody delivery_cost,
                                   @Part("minimum_charger") RequestBody minimum_charger);

    @POST("restaurant/reject-order")
    @FormUrlEncoded
    Call<RejectRequests> deletRequests(@Field("order_id") int order_id,
                                       @Field("api_token") String api_token);
    @POST("restaurant/reject-order")
    @FormUrlEncoded
    Call<GeneralResponse> deletorder(@Field("order_id") int order_id,
                                       @Field("api_token") String api_token,
                                     @Field("refuse_reason") String refuse_reason);


    @POST("restaurant/accept-order")
    @FormUrlEncoded
    Call<GeneralResponse> acceptPrequest(@Field("order_id") int order_id,
                                         @Field("api_token") String api_token);

    @POST("restaurant/confirm-order")
    @FormUrlEncoded
    Call<GeneralResponse> confirmrequest(@Field("order_id") int order_id,
                                        @Field("api_token") String api_token);


    @GET("restaurant/commissions")
    Call<Commissions> getCommisions(@Query("api_token") String api_token);

    @GET("restaurants")
    Call<RestaurantsList> getRestaurantsList(@Query("page") int page);

    @GET("restaurants")
    Call<RestaurantsList> getRestaurantsListFilter(@Query("keyword") String name,
                                                   @Query("city_id") int city_id,
                                                   @Query("page") int page);
    @GET("restaurants")
    Call<RestaurantsList> getRestaurantsListFilterNoCity(@Query("keyword") String name,
                                                   @Query("page") int page);
    //
    @GET("items")
    Call<MyProducts> getFoodList(@Query("page") int page,
                                 @Query("restaurant_id") int restaurant_id,
                                 @Query("category_id") int category_id);

    @GET("new-offers")
    Call<MyProducts> getFoodListOffers(@Query("page") int page,
                                       @Query("restaurant_id") int restaurant_id);

    //
    @GET("restaurant")
    Call<Detils> getRestaurantsDetails(@Query("restaurant_id") int restaurant_id);

    //
    @POST("client/login")
    @FormUrlEncoded
    Call<ClientLogin> getClientlogin(@Field("email") String email,
                                     @Field("password") String password);

    //
    @POST("client/sign-up")
    @Multipart
    Call<ClientLogin> getClientRegister(@Part MultipartBody.Part profile_image,
                                        @Part("name") RequestBody name,
                                        @Part("email") RequestBody email,
                                        @Part("password") RequestBody password,
                                        @Part("password_confirmation") RequestBody password_confirmation,
                                        @Part("phone") RequestBody phone,
                                        @Part("address") RequestBody address,
                                        @Part("region_id") RequestBody region_id);

    @GET("restaurant/reviews")
    Call<Comments> getComments(@Query("page") int page
            , @Query("restaurant_id") int restaurant_id);

    @GET("restaurant/reviews")
    Call<Comments> getClientComments(@Query("page") int page
            , @Query("api_token") String api_token);

    @GET("offers")
    Call<ListOfOffers> getNewOffera(@Query("page") int page);

    @GET("client/my-orders")
    Call<ListOfOrders> getOrders(@Query("page") int page,
                                 @Query("api_token") String api_token,
                                 @Query("state") String state);

    @POST("client/reset-password")
    @FormUrlEncoded
    Call<ClientResetPass> getClientResetPassword(@Field("email") String email);

    @POST("client/restaurant/review")
    @FormUrlEncoded
    Call<AddComment> addComment(@Field("api_token") String api_token,
                                @Field("comment") String comment,
                                @Field("restaurant_id") int restaurant_id,
                                @Field("rate") String rate);

    @POST("client/new-password")
    @FormUrlEncoded
    Call<NewPassword> getnewpassword(@Field("code") String code,
                                     @Field("password") String password,
                                     @Field("password_confirmation") String password_confirmation);


    @GET("restaurant/show-order")
    Call<ClientShowOrder> showOrder(@Query("api_token") String api_token,
                                    @Query("order_id") int order_id);

    @POST("contact")
    @FormUrlEncoded
    Call<GeneralResponse> contactUs(@Field("name") String name,
                           @Field("email") String email,
                           @Field("phone") String phone,
                           @Field("type") String type,
                           @Field("content") String content);

    @POST("contact")
    @FormUrlEncoded
    Call<State> getState(@Field("state") String state,
                         @Field("api_token") String api_token);

    @POST("client/profile")
    @Multipart
    Call<ClientLogin> editClientProfile(@Part("api_token") RequestBody api_token,
                                        @Part MultipartBody.Part profile_image,
                                        @Part("name") RequestBody name,
                                        @Part("email") RequestBody email,
                                        @Part("phone") RequestBody phone,
                                        @Part("region_id") RequestBody region_id);

    @POST("client/profile")
    @Multipart
    Call<ClientLogin> editClientProfilewithoutPhoto(@Part("api_token") RequestBody api_token,
                                                    @Part("name") RequestBody name,
                                                    @Part("email") RequestBody email,
                                                    @Part("phone") RequestBody phone,
                                                    @Part("region_id") RequestBody region_id);

    @POST("client/confirm-order")
    @FormUrlEncoded
    Call<GeneralResponse> onConfirmOrder(@Field("api_token") String api_token,
                                         @Field("order_id") int order_id);

    @POST("client/decline-order")
    @FormUrlEncoded
    Call<GeneralResponse> onDeclineOrder(@Field("api_token") String api_token,
                                         @Field("order_id") int order_id);

    @POST("restaurant/update-item")
    @Multipart
    Call<UpdatProduct> updateProduct(@Part("description") RequestBody description,
                                     @Part("price") RequestBody price,
                                     @Part("item_id") RequestBody item_id,
                                     @Part("name") RequestBody name,
                                     @Part("api_token") RequestBody api_token,
                                     @Part MultipartBody.Part photo,
                                     @Part("offer_price") RequestBody offer_price,
                                     @Part("category_id") RequestBody category_id);

    @POST("restaurant/update-item")
    @Multipart
    Call<UpdatProduct> updateProductNoPhoto(@Part("description") RequestBody description,
                                            @Part("price") RequestBody price,
                                            @Part("item_id") RequestBody item_id,
                                            @Part("name") RequestBody name,
                                            @Part("api_token") RequestBody api_token,
                                            @Part("offer_price") RequestBody offer_price,
                                            @Part("category_id") RequestBody category_id);

    @POST("restaurant/register-token")
    @FormUrlEncoded
    Call<GeneralResponse> registerRestaurantNotification(@Field("token") String token,
                                                         @Field("api_token") String api_token,
                                                         @Field("platform") String platform);

    @POST("restaurant/remove-token")
    @FormUrlEncoded
    Call<GeneralResponse> removeRestaurantnotification(@Field("token") String token,
                                                       @Field("api_token") String api_token);

    @POST("client/register-token")
    @FormUrlEncoded
    Call<GeneralResponse> registerClientNotification(@Field("token") String token,
                                                     @Field("api_token") String api_token,
                                                     @Field("platform") String platform);

    @POST("client/remove-token")
    @FormUrlEncoded
    Call<GeneralResponse> removeClientnotification(@Field("token") String token,
                                                   @Field("api_token") String api_token);

    @GET("restaurant/notifications")
    Call<RestaurantNotificationList> getRestaurantNotifications(@Query("api_token") String api_token,
                                                                @Query("page") int page);

    @GET("client/notifications")
    Call<ClientNotificationList> getClientNotifications(@Query("api_token") String api_token,
                                                        @Query("page") int page);

    @POST("restaurant/new-category")
    @Multipart
    Call<NewCategory> addCategory(@Part("api_token") RequestBody api_token,
                                  @Part MultipartBody.Part photo,
                                  @Part("name") RequestBody name);

    @POST("restaurant/delete-category")
    @FormUrlEncoded
    Call<GeneralResponse> deleteCategory(@Field("category_id") int category_id,
                                         @Field("api_token") String api_token);

    @POST("restaurant/update-category")
    @Multipart
    Call<GeneralResponse> updateCategory(
            @Part("name") RequestBody name,
            @Part("api_token") RequestBody api_token,
            @Part("category_id") RequestBody category_id,
            @Part MultipartBody.Part photo);

    @POST("restaurant/update-category")
    @Multipart
    Call<GeneralResponse> updateCategoryNoImage(@Part("name") RequestBody name,
                                                @Part("api_token") RequestBody api_token,
                                                @Part("category_id") RequestBody category_id);

    @POST("restaurant/change-password")
    @FormUrlEncoded
    Call<GeneralResponse> restaurantChangePassword(@Field("api_token") String api_token
            , @Field("old_password") String old_password
            , @Field("password") String password
            , @Field("password_confirmation") String password_confirmation);

    @POST("client/change-password")
    @FormUrlEncoded
    Call<GeneralResponse> clientChangePassword(@Field("api_token") String api_token,
                                               @Field("old_password") String old_password,
                                               @Field("password") String password,
                                               @Field("password_confirmation") String password_confirmation);

    @POST("restaurant/profile")
    @Multipart
    Call<RestaurantLogin> editRestaurantProfile(@Part MultipartBody.Part photo,
                                                @Part("name") RequestBody name,
                                                @Part("email") RequestBody email,
                                                @Part("phone") RequestBody phone,
                                                @Part("region_id") RequestBody region_id,
                                                @Part("delivery_time") RequestBody delivery_time,
                                                @Part("delivery_cost") RequestBody delivery_cost,
                                                @Part("minimum_charger") RequestBody minimum_charger,
                                                @Part("availability") RequestBody availability,
                                                @Part("whatsapp") RequestBody whatsapp,
                                                @Part("api_token") RequestBody api_token);

    @POST("restaurant/profile")
    @Multipart
    Call<RestaurantLogin> editRestaurantProfileNoPhoto(@Part("name") RequestBody name,
                                                       @Part("email") RequestBody email,
                                                       @Part("phone") RequestBody phone,
                                                       @Part("region_id") RequestBody region_id,
                                                       @Part("delivery_time") RequestBody delivery_time,
                                                       @Part("delivery_cost") RequestBody delivery_cost,
                                                       @Part("minimum_charger") RequestBody minimum_charger,
                                                       @Part("availability") RequestBody availability,
                                                       @Part("whatsapp") RequestBody whatsapp,
                                                       @Part("api_token") RequestBody api_token);

    @POST("client/new-order")
    @FormUrlEncoded
    Call<AddNewOrder> addNewOrder(@Field("restaurant_id") String restaurant_id,
                                  @Field("note") String note,
                                  @Field("address") String address,
                                  @Field("payment_method_id") String payment_method_id,
                                  @Field("phone") String phone,
                                  @Field("name") String name,
                                  @Field("api_token") String api_token,
                                  @Field("items[]") List<String> items,
                                  @Field("quantities[]") List<String> quantities,
                                  @Field("notes[]") List<String> notes);

    @GET("settings")
    Call<Settings> getSettingsData();

}
