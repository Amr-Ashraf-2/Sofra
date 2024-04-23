package com.ashraf.amr.apps.sofra.ui.activities.cycles;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomDao;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomManger;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.CustomDialog;
import com.ashraf.amr.apps.sofra.ui.activities.BaseActivity;
import com.ashraf.amr.apps.sofra.ui.fragments.Notification_List_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.authCycle.auth.Edit_Restaurant_Profile_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.buy_food.Cart_Items_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle.Edit_Client_Profile_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle.My_Orders_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle.Restaurants_List_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.restaurantDetails.Restaurant_FoodList_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.applications.All_Applications_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.categories.CategoriesFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.general.CommissionFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.general.MoreFragment;

import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTLOGINTYPE;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTREMEMBER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.REMEMBER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.SaveData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.getUserName;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.loadClientData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.loadRestaurantData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.setClientLooginType;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.registerRestaurantNotification;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;

public class Navigation_Activity extends BaseActivity {

    @BindView(R.id.Home_Cycle_Activity_Iv_Cart)
    ImageView HomeCycleActivityIvCart;
    @BindView(R.id.Home_Cycle_Activity_Iv_Notification)
    ImageView HomeCycleActivityIvNotification;
    @BindView(R.id.Home_Cycle_Activity_Toolbar)
    RelativeLayout HomeCycleActivityToolbar;
    @BindView(R.id.Home_Cycle_Activity_FrameLayout)
    FrameLayout HomeCycleActivityFrameLayout;
    @BindView(R.id.Home_Cycle_Activity_Iv_Home)
    ImageView HomeCycleActivityIvHome;
    @BindView(R.id.Home_Cycle_Activity_Iv_FoodList)
    ImageView HomeCycleActivityIvFoodList;
    @BindView(R.id.Home_Cycle_Activity_Iv_Profile)
    ImageView HomeCycleActivityIvProfile;
    @BindView(R.id.Home_Cycle_Activity_Iv_More)
    ImageView HomeCycleActivityIvMore;
    @BindView(R.id.Home_Cycle_Activity_Ll_Bottom_Navigation)
    LinearLayout HomeCycleActivityLlBottomNavigation;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.Home_Cycle_Activity_Tv_Title)
    TextView HomeCycleActivityTvTitle;
    @BindView(R.id.Home_Cycle_Activity_Iv_Back)
    ImageView HomeCycleActivityIvBack;
    //private SharedPreferences sharedPreferences;
    private ApiServices apiServices;
    //private String android = "android";
    public CategoriesFragment categoriesFragment;
    public Restaurants_List_Fragment restaurantsList;
    private RoomManger roomManger;
    private RoomDao roomDao;
    private Cart_Items_Fragment cartItemsFragment;
    private CustomDialog dialog;
    public static LinearLayout navBar;
    public static ImageView backBtn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //changeLang(this, "ar");
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        setContentView(R.layout.activity_client_home);
        ButterKnife.bind(this);
        navBar = HomeCycleActivityLlBottomNavigation;
        backBtn = HomeCycleActivityIvBack;
        roomManger = RoomManger.getInstance(this);
        RestaurantData user = loadRestaurantData(this);
        Client client = loadClientData(this);
        HomeCycleActivityIvBack.setVisibility(View.GONE);

        if ((LoadData(Navigation_Activity.this, USER_TYPE)).equals(RESTAURANT)) {
            try {
                //Call regesiter noti
                registerRestaurantNotification(apiServices, user.getApiToken());
            } catch (Exception e) {
                //
            }

            if (getUserName(this).equals(REMEMBER)) {
                categoriesFragment = new CategoriesFragment();
                replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, categoriesFragment);
                HomeCycleActivityIvCart.setImageResource(R.drawable.ic_calculator_solid);

            }

        }
        if ((LoadData(Navigation_Activity.this, USER_TYPE)).equals(CLIENT)) {
            try {
                //Call regesiter noti
                registerRestaurantNotification(apiServices, client.getApiToken());

            } catch (Exception e) {
                //
            }

            restaurantsList = new Restaurants_List_Fragment();
            replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, restaurantsList);
            if (SharedPreferencesManger.getClientName(this).equals(CLIENTREMEMBER)) {
                HomeCycleActivityIvCart.setImageResource(R.drawable.ic_shopping_cart_solid);
                HomeCycleActivityIvNotification.setVisibility(View.VISIBLE);
                HomeCycleActivityToolbar.setVisibility(View.VISIBLE);
            } else {
                HomeCycleActivityIvCart.setVisibility(View.GONE);
                HomeCycleActivityIvNotification.setVisibility(View.GONE);
                HomeCycleActivityToolbar.setVisibility(View.GONE);

            }

        }
    }


    @OnClick({R.id.Home_Cycle_Activity_Iv_Cart,
            R.id.Home_Cycle_Activity_Iv_Notification,
            R.id.Home_Cycle_Activity_Iv_Home,
            R.id.Home_Cycle_Activity_Iv_FoodList,
            R.id.Home_Cycle_Activity_Iv_Profile,
            R.id.Home_Cycle_Activity_Iv_More})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Home_Cycle_Activity_Iv_Cart:
                if ((LoadData(Navigation_Activity.this, USER_TYPE)).equals(RESTAURANT)) {
                    CommissionFragment commission = new CommissionFragment();
                    replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, commission);
                } else {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            roomDao = roomManger.roomDao();

                            cartItemsFragment = new Cart_Items_Fragment();
                            cartItemsFragment.foodItems = roomDao.getAllItem();
                            cartItemsFragment.home = true;


                            runOnUiThread(new Runnable() {
                                public void run() {
                                    replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, cartItemsFragment);
                                }
                            });

                        }

                    });
                }
                break;
            case R.id.Home_Cycle_Activity_Iv_Notification:
                if ((LoadData(Navigation_Activity.this, USER_TYPE)).equals(RESTAURANT)) {
                    Notification_List_Fragment notificationList = new Notification_List_Fragment();
                    replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, notificationList);
                }
                if ((LoadData(Navigation_Activity.this, USER_TYPE)).equals(CLIENT)) {
                    if (SharedPreferencesManger.getClientName(Navigation_Activity.this).equals(CLIENTREMEMBER)) {
                        Notification_List_Fragment notificationList = new Notification_List_Fragment();
                        replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, notificationList);
                    } else {
                        setClientLooginType(Navigation_Activity.this, CLIENTLOGINTYPE);
                        SaveData(this, USER_TYPE, "");
                        SaveData(this, USER_TYPE, CLIENT);
                        Intent intent = new Intent(Navigation_Activity.this, ProfileActivity.class);
                        startActivity(intent);
                    }


                }

                break;
            case R.id.Home_Cycle_Activity_Iv_Home:
                try {
                    if ((LoadData(Navigation_Activity.this, USER_TYPE)).equals(RESTAURANT)) {
                        if (getUserName(this).equals(REMEMBER)) {
                            CategoriesFragment categoriesFragment = new CategoriesFragment();
                            replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, categoriesFragment);
                        }


                    }

                } catch (Exception e) {
                    //
                }

                if (LoadData(Navigation_Activity.this, USER_TYPE).equals(CLIENT)) {
                    Restaurants_List_Fragment restaurantsList = new Restaurants_List_Fragment();
                    replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, restaurantsList);
                }
                break;
            case R.id.Home_Cycle_Activity_Iv_FoodList:
                if (LoadData(this, USER_TYPE).equals(RESTAURANT)) {
                    All_Applications_Fragment allApplications = new All_Applications_Fragment();
                    replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, allApplications);
                }
                if (LoadData(this, USER_TYPE).equals(CLIENT)) {
                    if (SharedPreferencesManger.getClientName(Navigation_Activity.this).equals(CLIENTREMEMBER)) {
                        My_Orders_Fragment myOrders = new My_Orders_Fragment();
                        replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, myOrders);
                    } else {
                        showDialog();

                    }

                }

                break;
            case R.id.Home_Cycle_Activity_Iv_Profile:
                if ((LoadData(Navigation_Activity.this, USER_TYPE)).equals(RESTAURANT)) {
                    Edit_Restaurant_Profile_Fragment editRestaurantProfile = new Edit_Restaurant_Profile_Fragment();
                    replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, editRestaurantProfile);
                }

                if (LoadData(Navigation_Activity.this, USER_TYPE).equals(CLIENT)) {

                    if (SharedPreferencesManger.getClientName(Navigation_Activity.this).equals(CLIENTREMEMBER)) {
                        Edit_Client_Profile_Fragment editProfile = new Edit_Client_Profile_Fragment();
                        replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, editProfile);
                    } else {


                        showDialog();


                    }

                }

                break;
            case R.id.Home_Cycle_Activity_Iv_More:
                MoreFragment moreFragment = new MoreFragment();
                replaceFragment(getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, moreFragment);
                break;
        }
    }

    private void showDialog() {
        View.OnClickListener ok = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClientLooginType(Navigation_Activity.this, CLIENTLOGINTYPE);
                SaveData(Navigation_Activity.this, USER_TYPE, "");
                SaveData(Navigation_Activity.this, USER_TYPE, CLIENT);
                Intent intent = new Intent(Navigation_Activity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        };
        View.OnClickListener cancel = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        };

        dialog = new CustomDialog(Navigation_Activity.this, Navigation_Activity.this, getString(R.string.not_logined), ok, cancel, true, true);
    }

//    public void ChangeTitle(String title) {
//        HomeCycleActivityTvTitle.setText(title);
//    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        HelperClass.changeLang(this, "ar");
//    }

}
