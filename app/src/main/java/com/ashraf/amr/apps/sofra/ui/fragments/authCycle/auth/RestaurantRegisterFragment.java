package com.ashraf.amr.apps.sofra.ui.fragments.authCycle.auth;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.model.general.cities.Cities;
import com.ashraf.amr.apps.sofra.data.model.general.regions.Regions;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantLogin;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.authCycle.Login_Fragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
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

public class RestaurantRegisterFragment extends BaseFragment {

    @BindView(R.id.Restaurant_Register_Fragment_TIE_Name)
    TextInputEditText RestaurantRegisterFragmentTIEName;
    @BindView(R.id.Restaurant_Register_Fragment_TIE_Email)
    TextInputEditText RestaurantRegisterFragmentTIEEmail;
    @BindView(R.id.Restaurant_Register_Fragment_Sp_City)
    Spinner RestaurantRegisterFragmentSpCity;
    @BindView(R.id.Restaurant_Register_Fragment_Ll_City)
    LinearLayout RestaurantRegisterFragmentLlCity;
    @BindView(R.id.Restaurant_Register_Fragment_Sp_Region)
    Spinner RestaurantRegisterFragmentSpRegion;
    @BindView(R.id.Restaurant_Register_Fragment_Ll_Region)
    LinearLayout RestaurantRegisterFragmentLlRegion;
    @BindView(R.id.Restaurant_Register_Fragment_TIE_Password)
    TextInputEditText RestaurantRegisterFragmentTIEPassword;
    @BindView(R.id.Restaurant_Register_Fragment_TIE_PasswordConfirm)
    TextInputEditText RestaurantRegisterFragmentTIEPasswordConfirm;
    @BindView(R.id.Restaurant_Register_Fragment_TIE_MinimumOrder)
    TextInputEditText RestaurantRegisterFragmentTIEMinimumOrder;
    @BindView(R.id.Restaurant_Register_Fragment_TIE_DelieveryCost)
    TextInputEditText RestaurantRegisterFragmentTIEDelieveryCost;
    @BindView(R.id.Restaurant_Register_Fragment_TIE_Phone)
    TextInputEditText RestaurantRegisterFragmentTIEPhone;
    @BindView(R.id.Restaurant_Register_Fragment_TIE_WhatsApp)
    TextInputEditText RestaurantRegisterFragmentTIEWhatsApp;
    @BindView(R.id.Restaurant_Complete_Register_Iv_ProductPhoto)
    PorterShapeImageView RestaurantCompleteRegisterIvProductPhoto;
    @BindView(R.id.Restaurant_Complete_Register_Fragment_Rl_Register)
    RelativeLayout RestaurantCompleteRegisterFragmentRlRegister;
    @BindView(R.id.Lin)
    LinearLayout Lin;
    @BindView(R.id.Restaurant_Register_Fragment_View)
    ImageView RestaurantRegisterFragmentView;
    @BindView(R.id.Tv)
    TextView Tv;
    Unbinder unbinder;
    @BindView(R.id.Restaurant_Register_Fragment_TIE_Delivery_Time)
    TextInputEditText RestaurantRegisterFragmentTIEDeliveryTime;
    @BindView(R.id.Restaurant_Complete_Register_Iv_ChoosePhoto)
    ImageView RestaurantCompleteRegisterIvChoosePhoto;

    private int region_id = 0;
    private ApiServices apiService;

    private List<String> citiesTxt = new ArrayList<>();
    private List<Integer> citiesId = new ArrayList<>();

    private List<String> RegionsTxt = new ArrayList<>();
    private List<Integer> RegionsId = new ArrayList<>();

    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();

    public RestaurantRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        getCities();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @OnClick({R.id.Restaurant_Complete_Register_Iv_ProductPhoto,
            R.id.Restaurant_Complete_Register_Fragment_Rl_Register,
            R.id.Lin,
            R.id.Restaurant_Complete_Register_Iv_ChoosePhoto})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.Restaurant_Complete_Register_Iv_ProductPhoto:
                addImage();
                break;
            case R.id.Restaurant_Complete_Register_Fragment_Rl_Register:
                register();
                break;
            case R.id.Lin:
                break;
            case R.id.Restaurant_Complete_Register_Iv_ChoosePhoto:
                addImage();
                RestaurantCompleteRegisterIvChoosePhoto.setVisibility(View.GONE);
                break;
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

                        for (int i = 0; i < response.body().getData().getData().size(); i++) {
                            citiesTxt.add(response.body().getData().getData().get(i).getName());
                            citiesId.add(response.body().getData().getData().get(i).getId());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinner_item2, citiesTxt);

                        RestaurantRegisterFragmentSpCity.setAdapter(adapter);

                        RestaurantRegisterFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    getRegions(citiesId.get(i));
                                    RestaurantRegisterFragmentLlRegion.setVisibility(View.VISIBLE);
                                    RestaurantRegisterFragmentView.setVisibility(View.VISIBLE);
                                } else {
                                    RestaurantRegisterFragmentLlRegion.setVisibility(View.GONE);
                                    RestaurantRegisterFragmentView.setVisibility(View.GONE);

                                }
                                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

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

                        for (int i = 0; i < response.body().getData().getData().size(); i++) {
                            RegionsTxt.add(response.body().getData().getData().get(i).getName());
                            RegionsId.add(response.body().getData().getData().get(i).getId());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                R.layout.spinner_item2, RegionsTxt);

                        RestaurantRegisterFragmentSpRegion.setAdapter(adapter);

                        RestaurantRegisterFragmentSpRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    region_id = RegionsId.get(i);
                                }
                                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

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

                onLoadImageFromUrl(RestaurantCompleteRegisterIvProductPhoto, ImagesFiles.get(0).getPath(), getContext());

            }
        };

        openAlbum(1, getActivity(), ImagesFiles, action);
    }


    private void register() {

        String name = RestaurantRegisterFragmentTIEName.getText().toString();
        String email = RestaurantRegisterFragmentTIEEmail.getText().toString();
        final String password = RestaurantRegisterFragmentTIEPassword.getText().toString();
        String password_confirmation = RestaurantRegisterFragmentTIEPasswordConfirm.getText().toString();
        String phone = RestaurantRegisterFragmentTIEPhone.getText().toString();
        String minimum_charger = RestaurantRegisterFragmentTIEMinimumOrder.getText().toString();
        String whatsapp = RestaurantRegisterFragmentTIEWhatsApp.getText().toString();
        String delivery_cost = RestaurantRegisterFragmentTIEDelieveryCost.getText().toString();
        String delivery_time = RestaurantRegisterFragmentTIEDeliveryTime.getText().toString();

        if (ImagesFiles.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_restaurant_image));
            return;
        }
        if (name.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_restaurant_name));
            return;
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
            if (RestaurantRegisterFragmentSpCity.getSelectedItemPosition() == 0) {
                showToastMessage(getActivity(), getResources().getString(R.string.select_city));
                return;
            }
        } else {
            showToastMessage(getActivity(), getResources().getString(R.string.select_city));
            return;
        }

        if (RegionsId.size() > 0) {
            region_id = RegionsId.get(RestaurantRegisterFragmentSpRegion.getSelectedItemPosition());
            if (region_id == 0) {
                showToastMessage(getActivity(), getResources().getString(R.string.select_region));
                return;
            }

        } else {
            showToastMessage(getActivity(), getResources().getString(R.string.select_region));
            return;
        }
        if (password.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.enterPasswordd));
            return;
        }
        if (password.length() < 4) {
            showToastMessage(getActivity(), getResources().getString(R.string.passwordWeak));
            return;
        }
        if (password_confirmation.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.enterPasswordAgain));
            return;
        }

        if (!password_confirmation.equals(password)) {
            showToastMessage(getActivity(), getResources().getString(R.string.invalid_confirm_password));
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

            apiService.register(convertFileToMultipart(ImagesFiles.get(0).getPath(), "photo")
                    , convertToRequestBody(name)
                    , convertToRequestBody(email)
                    , convertToRequestBody(delivery_time)
                    , convertToRequestBody(password)
                    , convertToRequestBody(password_confirmation)
                    , convertToRequestBody(phone)
                    , convertToRequestBody(whatsapp)
                    , convertToRequestBody(String.valueOf(region_id))
                    , convertToRequestBody(delivery_cost)
                    , convertToRequestBody(minimum_charger)).enqueue(new Callback<RestaurantLogin>() {
                @Override
                public void onResponse(Call<RestaurantLogin> call, Response<RestaurantLogin> response) {
                    try {
                        dismissProgressDialog();
                        disappearKeypad(getActivity(), getView());

                        if (response.body().getStatus() == 1) {
                            Login_Fragment login = new Login_Fragment();
                            replaceFragment(getFragmentManager(), R.id.Profile_Frame, login);
                            showSuccessToastMessage(getActivity(), response.body().getMsg());

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
            dismissProgressDialog();
            showToastMessage(getActivity(), getString(R.string.no_internet));

        }


    }

}
