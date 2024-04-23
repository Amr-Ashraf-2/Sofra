package com.ashraf.amr.apps.sofra.ui.fragments.client.restaurantDetails;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.model.general.detils.Detils;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Restaurant_Information_Fragment extends BaseFragment {


    @BindView(R.id.Restaurant_Information_Fragment_Tv_State)
    TextView RestaurantInformationFragmentTvState;
    @BindView(R.id.Restaurant_Information_Fragment_Tv_City)
    TextView RestaurantInformationFragmentTvCity;
    @BindView(R.id.Restaurant_Information_Fragment_Tv_Region)
    TextView RestaurantInformationFragmentTvRegion;
    @BindView(R.id.Restaurant_Information_Fragment_Tv_Minimum)
    TextView RestaurantInformationFragmentTvMinimum;
    @BindView(R.id.Restaurant_Information_Fragment_Tv_Del_Cost)
    TextView RestaurantInformationFragmentTvDelCost;
    Unbinder unbinder;
    @BindView(R.id.Restaurant_Information_Fragment_Switch)
    SwitchCompat RestaurantInformationFragmentSwitch;
    private ApiServices apiService;
    public int restaurant_id;

    public Restaurant_Information_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant__information, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);

            getRestaurantDetails();
            //RestaurantInformationFragmentSwitch.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getRestaurantDetails() {
        apiService.getRestaurantsDetails(restaurant_id).enqueue(new Callback<Detils>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Detils> call, @NonNull Response<Detils> response) {
               try {
                   assert response.body() != null;
                   if (response.body().getStatus() == 1) {
                       float minimumChargeAsFloat = Float.parseFloat(response.body().getData().getMinimumCharger());
                       float deliveryFeeAsFloat = Float.parseFloat(response.body().getData().getDeliveryCost());
                       int minimumChargeAsInteger = (int) minimumChargeAsFloat;
                       int deliveryFeeAsInteger = (int) deliveryFeeAsFloat;
                       String getMinimumCharge = String.valueOf(minimumChargeAsInteger);
                       String getDeliveryFee = String.valueOf(deliveryFeeAsInteger);
                       RestaurantInformationFragmentTvState.setText(response.body().getData().getAvailability());
                       RestaurantInformationFragmentTvCity.setText(response.body().getData().getRegion().getCity().getName());
                       RestaurantInformationFragmentTvRegion.setText(response.body().getData().getRegion().getName());
                       RestaurantInformationFragmentTvMinimum.setText(getString(R.string.food_price, getMinimumCharge));
                       RestaurantInformationFragmentTvDelCost.setText(getString(R.string.food_price, getDeliveryFee));
                   }

               }catch (Exception e){
                    //
               }
            }

            @Override
            public void onFailure(@NonNull Call<Detils> call, @NonNull Throwable t) {

            }
        });
    }

}
