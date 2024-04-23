package com.ashraf.amr.apps.sofra.ui.fragments.client.restaurantDetails;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.ViewPagerAdapter;
import com.ashraf.amr.apps.sofra.data.model.general.listofoffers.NewOffersData;
import com.ashraf.amr.apps.sofra.data.model.general.restaurantslist.RestaurantsListData;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle.Restaurants_List_Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.MAIN;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.OFFER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.WHERE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.backBtn;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Restaurant_Details_Fragment extends BaseFragment {

    @BindView(R.id.Restaurant_Details_TabLayout)
    TabLayout RestaurantDetailsTabLayout;
    @BindView(R.id.Restaurant_Details_ViewPager)
    ViewPager RestaurantDetailsViewPager;
    public RestaurantsListData restaurantsListData;
    public NewOffersData newOffersList;


    Unbinder unbinder;

    public Restaurant_Details_Fragment() {
        // Required empty public constructor
    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant__details, container, false);
        unbinder = ButterKnife.bind(this, view);

        navBar.setVisibility(View.GONE);

        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity()!=null){
                    Restaurants_List_Fragment restaurantsList = new Restaurants_List_Fragment();
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, restaurantsList);
                }
            }
        });

        ViewPagerAdapter vadapter = new ViewPagerAdapter(getChildFragmentManager());

        Restaurant_FoodList_Fragment foodList = new Restaurant_FoodList_Fragment();
        if (LoadData(getActivity(), WHERE).equals(OFFER)) {
            foodList.restaurant_id = newOffersList.getRestaurant().getId();

        } else if (LoadData(getActivity(), WHERE).equals(MAIN)) {
            foodList.restaurant_id = restaurantsListData.getId();

        }

        CommentsAndRating_Fragment commentsAndRating = new CommentsAndRating_Fragment();
        if (LoadData(getActivity(), WHERE).equals(OFFER)) {
            commentsAndRating.restaurant_id = newOffersList.getRestaurant().getId();

        } else if (LoadData(getActivity(), WHERE).equals(MAIN)) {
            commentsAndRating.restaurant_id = restaurantsListData.getId();

        }

        Restaurant_Information_Fragment restaurantInformation = new Restaurant_Information_Fragment();
        if (LoadData(getActivity(), WHERE).equals(OFFER)) {
            restaurantInformation.restaurant_id = newOffersList.getRestaurant().getId();

        } else if (LoadData(getActivity(), WHERE).equals(MAIN)) {
            restaurantInformation.restaurant_id = restaurantsListData.getId();

        }
        vadapter.addPager(foodList, getString(R.string.food_menu));
        vadapter.addPager(commentsAndRating, getString(R.string.reviews));
        vadapter.addPager(restaurantInformation, getString(R.string.store_info));
        RestaurantDetailsViewPager.setAdapter(vadapter);
        RestaurantDetailsTabLayout.setupWithViewPager(RestaurantDetailsViewPager);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}

