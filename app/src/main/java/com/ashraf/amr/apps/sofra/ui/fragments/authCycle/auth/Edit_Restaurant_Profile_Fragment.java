package com.ashraf.amr.apps.sofra.ui.fragments.authCycle.auth;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.general.cities.Cities;
import com.ashraf.amr.apps.sofra.data.model.general.regions.Regions;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantLogin;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.saveRestaurantData;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertFileToMultipart;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertToRequestBody;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.openAlbum;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;
import static com.ashraf.amr.apps.sofra.helper.Validation.setEmailValidation;

/**
 * A simple {@link Fragment} subclass.
 */
public class Edit_Restaurant_Profile_Fragment extends BaseFragment {


    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Civ_ProfilePicture)
    CircleImageView RestaurantEditProfileFragmentCivProfilePicture;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Et_Name)
    EditText RestaurantEditProfileFragmentEtName;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Et_Email)
    EditText RestaurantEditProfileFragmentEtEmail;
    @BindView(R.id.Image_Home)
    ImageView ImageHome;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Sp_City)
    Spinner RestaurantEditProfileFragmentSpCity;
    @BindView(R.id.Image_Home2)
    ImageView ImageHome2;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Sp_Region)
    Spinner RestaurantEditProfileFragmentSpRegion;

    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Et_Low_Order)
    EditText RestaurantEditProfileFragmentEtLowOrder;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Et_Delivery_Cost)
    EditText RestaurantEditProfileFragmentEtDeliveryCost;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Switch_State)
    Switch RestaurantEditProfileFragmentSwitchState;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Et_Phone)
    EditText RestaurantEditProfileFragmentEtPhone;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Et_Whats)
    EditText RestaurantEditProfileFragmentEtWhats;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Btn_Adjust)
    Button RestaurantEditProfileFragmentBtnAdjust;
    @BindView(R.id.Lin)
    LinearLayout Lin;
    @BindView(R.id.Restaurant_Edit_Profile_Fragment_Et_Delivery_Time)
    EditText RestaurantEditProfileFragmentEtDeliveryTime;


    private List<String> citiesTxt = new ArrayList<>();
    private List<Integer> citiesId = new ArrayList<>();

    private List<String> RegionsTxt = new ArrayList<>();
    private List<Integer> RegionsId = new ArrayList<>();

    private int region_id = 0;

    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();
    Unbinder unbinder;
    private ApiServices apiService;
    private RestaurantData user;
    private String availability;

    public Edit_Restaurant_Profile_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit__restaurant__profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        setData();
        getCities();
        return view;
    }

    private void setData() {
        onLoadImageFromUrl(RestaurantEditProfileFragmentCivProfilePicture, user.getPhotoUrl(), getContext());
        RestaurantEditProfileFragmentEtName.setText(user.getName());
        RestaurantEditProfileFragmentEtEmail.setText(user.getEmail());
        RestaurantEditProfileFragmentEtDeliveryTime.setText(user.getDeliveryTime());
        RestaurantEditProfileFragmentEtLowOrder.setText(user.getMinimumCharger());
        RestaurantEditProfileFragmentEtDeliveryCost.setText(user.getDeliveryCost());
        RestaurantEditProfileFragmentEtPhone.setText(user.getPhone());
        RestaurantEditProfileFragmentEtWhats.setText(user.getWhatsapp());
        if (user.getAvailability().equals("open")) {
            RestaurantEditProfileFragmentSwitchState.setChecked(true);
        } else if (user.getAvailability().equals("closed")) {
            RestaurantEditProfileFragmentSwitchState.setChecked(false);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Restaurant_Edit_Profile_Fragment_Civ_ProfilePicture, R.id.Restaurant_Edit_Profile_Fragment_Btn_Adjust, R.id.Lin})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.Restaurant_Edit_Profile_Fragment_Civ_ProfilePicture:
                addImage();
                break;
            case R.id.Restaurant_Edit_Profile_Fragment_Btn_Adjust:
                editProfile();
                break;
            case R.id.Lin:

                break;
        }
    }

    private void editProfile() {

        if (RestaurantEditProfileFragmentSwitchState.isChecked()) {
            availability = "open";
        } else {
            availability = "closed";
        }

        String name = RestaurantEditProfileFragmentEtName.getText().toString();
        String email = RestaurantEditProfileFragmentEtEmail.getText().toString();
        String phone = RestaurantEditProfileFragmentEtPhone.getText().toString();
        String delivery_time = RestaurantEditProfileFragmentEtDeliveryTime.getText().toString();
        String delivery_cost = RestaurantEditProfileFragmentEtDeliveryCost.getText().toString();
        String minimum_charger = RestaurantEditProfileFragmentEtLowOrder.getText().toString();
        String whatsapp = RestaurantEditProfileFragmentEtWhats.getText().toString();

        if (ImagesFiles.size() == 0
                && name.equals(user.getName())
                && email.equals(user.getEmail())
                && phone.equals(user.getPhone())
                && delivery_time.equals(user.getDeliveryTime())
                && delivery_cost.equals(user.getDeliveryCost())
                && minimum_charger.equals(user.getMinimumCharger())
                && whatsapp.equals(user.getWhatsapp())
                && region_id == user.getRegion().getId()
                && availability.equals(user.getAvailability())) {

            return;
        }
        if (name.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_restaurant_name));
        }
        if (email.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_email));
            return;
        }
        if (!setEmailValidation(getActivity(), email)) {
            showToastMessage(getActivity(), getResources().getString(R.string.invalid_Email));
            return;
        }
        if (delivery_time.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_delivery_time));
            return;
        }


        if (citiesId.size() > 0) {
            if (RestaurantEditProfileFragmentSpCity.getSelectedItemPosition() == 0) {
                showToastMessage(getActivity(), getResources().getString(R.string.select_city));
                return;
            }
        } else {
            showToastMessage(getActivity(), getResources().getString(R.string.select_city));
            return;
        }

        if (RegionsId.size() > 0) {
            region_id = RegionsId.get(RestaurantEditProfileFragmentSpRegion.getSelectedItemPosition());
            if (region_id == 0) {
                showToastMessage(getActivity(), getResources().getString(R.string.select_region));
                return;
            }

        } else {
            showToastMessage(getActivity(), getResources().getString(R.string.select_region));
            return;
        }

        if (minimum_charger.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.enter_minimum_charger));
            return;
        }
        if (delivery_cost.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.enter_delivery_cost));
            return;
        }


        if (phone.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_phone));
            return;
        }
        if (phone.length() != 11) {
            showToastMessage(getActivity(), getResources().getString(R.string.phoneLengh));
            return;
        }

        if (whatsapp.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_whatsapp_number));
            return;
        }
        if (whatsapp.length() != 11) {
            showToastMessage(getActivity(), getResources().getString(R.string.whatsappLengh));
            return;
        }
        if (InternetState.isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.waiit));
            Call<RestaurantLogin> call;
            if (ImagesFiles.size() != 0) {
                call = apiService.editRestaurantProfile(
                        convertFileToMultipart(ImagesFiles.get(0).getPath(), "photo")
                        , convertToRequestBody(name)
                        , convertToRequestBody(email)
                        , convertToRequestBody(phone)
                        , convertToRequestBody(String.valueOf(region_id))
                        , convertToRequestBody(delivery_time)
                        , convertToRequestBody(delivery_cost)
                        , convertToRequestBody(minimum_charger)
                        , convertToRequestBody(availability)
                        , convertToRequestBody(whatsapp)
                        , convertToRequestBody(user.getApiToken()));


            } else {
                call = apiService.editRestaurantProfileNoPhoto(
                        convertToRequestBody(name)
                        , convertToRequestBody(email)
                        , convertToRequestBody(phone)
                        , convertToRequestBody(String.valueOf(region_id))
                        , convertToRequestBody(delivery_time)
                        , convertToRequestBody(delivery_cost)
                        , convertToRequestBody(minimum_charger)
                        , convertToRequestBody(availability)
                        , convertToRequestBody(whatsapp)
                        , convertToRequestBody(user.getApiToken()));

            }

            call.enqueue(new Callback<RestaurantLogin>() {
                @Override
                public void onResponse(Call<RestaurantLogin> call, Response<RestaurantLogin> response) {
                    dismissProgressDialog();

                    try {
                        if (response.body().getStatus() == 1) {
                            dismissProgressDialog();
                            RestaurantData newUser = response.body().getData().getUser();
                            newUser.setApiToken(user.getApiToken());
                            saveRestaurantData(getActivity(), newUser);
                            disappearKeypad(getActivity(), getView());
                            showSuccessToastMessage(getActivity(), response.body().getMsg());
                            onBack();
                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());
                        }
                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<RestaurantLogin> call, Throwable t) {
                    dismissProgressDialog();

                    showToastMessage(getActivity(), getString(R.string.error));
                }
            });


        } else {
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }


    }

    private void getCities() {
        apiService.getcities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        citiesTxt.add(getString(R.string.choose_city));
                        citiesId.add(0);
                        int pos = 0;

                        for (int i = 0; i < response.body().getData().getData().size(); i++) {
                            citiesTxt.add(response.body().getData().getData().get(i).getName());
                            citiesId.add(response.body().getData().getData().get(i).getId());
                            if (response.body().getData().getData().get(i).getName().equals(user.getRegion().getCity().getName())) {
                                pos = i + 1;
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinner_item_4, citiesTxt);
                        RestaurantEditProfileFragmentSpCity.setAdapter(adapter);
                        RestaurantEditProfileFragmentSpCity.setSelection(pos);
                        getRegions(citiesId.get(pos));

                        RestaurantEditProfileFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    getRegions(citiesId.get(i));
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } else {

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    private void getRegions(int i) {
        apiService.getRegions(i).enqueue(new Callback<Regions>() {
            @Override
            public void onResponse(Call<Regions> call, Response<Regions> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        RegionsTxt = new ArrayList<>();
                        RegionsId = new ArrayList<>();

                        RegionsTxt.add(getString(R.string.choose_region));
                        RegionsId.add(0);
                        int pos = 0;

                        for (int i = 0; i < response.body().getData().getData().size(); i++) {
                            RegionsTxt.add(response.body().getData().getData().get(i).getName());
                            RegionsId.add(response.body().getData().getData().get(i).getId());
                            if (response.body().getData().getData().get(i).getName().equals(user.getRegion().getName())) {
                                pos = i + 1;
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinner_item_4, RegionsTxt);

                        RestaurantEditProfileFragmentSpRegion.setAdapter(adapter);
                        RestaurantEditProfileFragmentSpRegion.setSelection(pos);
                        RestaurantEditProfileFragmentSpRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    region_id = RegionsId.get(i);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } else {

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Regions> call, Throwable t) {

            }
        });
    }

    private void addImage() {
        Action<ArrayList<AlbumFile>> action = new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                // TODO accept the result.

                ImagesFiles.clear();
                ImagesFiles.addAll(result);

                onLoadImageFromUrl(RestaurantEditProfileFragmentCivProfilePicture, ImagesFiles.get(0).getPath(), getContext());

            }
        };

        openAlbum(1, getActivity(), ImagesFiles, action);
    }


    @Override
    public void onBack() {
        replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                , homeActivity.categoriesFragment);
    }

}
