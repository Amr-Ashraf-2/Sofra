package com.ashraf.amr.apps.sofra.ui.fragments.authCycle;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
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
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.ClientLogin;
import com.ashraf.amr.apps.sofra.data.model.general.cities.Cities;
import com.ashraf.amr.apps.sofra.data.model.general.regions.Regions;
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

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTLOGINTYPE;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTPASSWORD;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTREMEMBER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.saveClientData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.setClientName;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertFileToMultipart;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertToRequestBody;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.openAlbum;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;
import static com.ashraf.amr.apps.sofra.helper.Validation.setEmailValidation;

public class ClientRegisterFragment extends BaseFragment {

    @BindView(R.id.Register_Fragment_Iv_Profile)
    CircleImageView RegisterFragmentIvProfile;
    @BindView(R.id.Register_Fragment_TIE_Name)
    TextInputEditText RegisterFragmentTIEName;
    @BindView(R.id.Register_Fragment_TIE_Email)
    TextInputEditText RegisterFragmentTIEEmail;
    @BindView(R.id.Register_Fragment_TIE_Phone)
    TextInputEditText RegisterFragmentTIEPhone;
    @BindView(R.id.Register_Fragment_Sp_City)
    Spinner RegisterFragmentSpCity;
    @BindView(R.id.Register_Fragment_Ll_City)
    LinearLayout RegisterFragmentLlCity;
    @BindView(R.id.Register_Fragment_Sp_Region)
    Spinner RegisterFragmentSpRegion;
    @BindView(R.id.Register_Fragment_Ll_Region)
    LinearLayout RegisterFragmentLlRegion;
    @BindView(R.id.Register_Fragment_TIE_Password)
    TextInputEditText RegisterFragmentTIEPassword;
    @BindView(R.id.Register_Fragment_TIE_PasswordConfirm)
    TextInputEditText RegisterFragmentTIEPasswordConfirm;
    @BindView(R.id.Tv)
    TextView Tv;
    @BindView(R.id.Register_Fragment_Rl_Register)
    RelativeLayout RegisterFragmentRlRegister;
    Unbinder unbinder;
    @BindView(R.id.Register_Fragment_Iv_AddImage)
    ImageView RegisterFragmentIvAddImage;
    @BindView(R.id.Register_Fragment_TIE_Address)
    TextInputEditText RegisterFragmentTIEAddress;
    @BindView(R.id.Lin)
    LinearLayout Lin;
    private ApiServices apiService;
    private List<String> citiesTxt = new ArrayList<>();
    private List<Integer> citiesId = new ArrayList<>();

    private List<String> RegionsTxt = new ArrayList<>();
    private List<Integer> RegionsId = new ArrayList<>();
    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();
    private int region_id;

    public ClientRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
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

    @OnClick({R.id.Register_Fragment_Iv_AddImage, R.id.Register_Fragment_Rl_Register, R.id.Lin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Register_Fragment_Iv_AddImage:
                addImage();
                break;
            case R.id.Register_Fragment_Rl_Register:
                onRegister();
                break;
            case R.id.Lin:
                disappearKeypad(getActivity(), view);
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
                        RegisterFragmentSpCity.setAdapter(adapter);

                        RegisterFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {

                                    getRegions(citiesId.get(i));

                                    RegisterFragmentLlRegion.setVisibility(View.VISIBLE);
                                } else {

                                    RegisterFragmentLlRegion.setVisibility(View.GONE);
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

                        RegisterFragmentSpRegion.setAdapter(adapter);

                        RegisterFragmentSpRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {

                                    RegisterFragmentSpRegion.setSelection(i);
                                } else {

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

                onLoadImageFromUrl(RegisterFragmentIvProfile, ImagesFiles.get(0).getPath(), getContext());

            }
        };

        openAlbum(1, getActivity(), ImagesFiles, action);
    }

    private void onRegister() {
        String name = RegisterFragmentTIEName.getText().toString();
        String email = RegisterFragmentTIEEmail.getText().toString();
        String phone = RegisterFragmentTIEPhone.getText().toString();
        final String password = RegisterFragmentTIEPassword.getText().toString();
        String password_confirmation = RegisterFragmentTIEPasswordConfirm.getText().toString();
        String address = RegisterFragmentTIEAddress.getText().toString();
        int region_id = RegisterFragmentSpRegion.getSelectedItemPosition();
//        if (ImagesFiles.isEmpty()
//                & name.isEmpty()
//                & email.isEmpty()
//                & phone.isEmpty()
//                & address.isEmpty()
//                & password.isEmpty()
//                & password_confirmation.isEmpty()
//        ) {
//            showToastMessage(getActivity(), getString(R.string.enter_inf));
//        } else
        if (ImagesFiles.isEmpty()) {
            showToastMessage(getActivity(), "برجاء اختيار صورة");

        } else if (name.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_name));
        } else if (email.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_email));
        } else if (!setEmailValidation(getActivity(), email)) {
            showToastMessage(getActivity(), getResources().getString(R.string.invalid_Email));
            return;
        } else if (phone.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_phone));
        } else if (phone.length() != 11) {
            showToastMessage(getActivity(), getString(R.string.passwordLengh));
        } else if (RegisterFragmentSpCity.getSelectedItemPosition() == 0) {
            showToastMessage(getActivity(), getString(R.string.enter_city));
        } else if (RegisterFragmentSpRegion.getSelectedItemPosition() == 0) {
            showToastMessage(getActivity(), getString(R.string.enter_region));
        } else if (address.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_address));
        } else if (password.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_password));
        } else if (password_confirmation.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_password_conf));
        } else {
            String path;
            if (ImagesFiles.size() > 0) {
                path = ImagesFiles.get(0).getPath();
            } else {
                path = null;
            }
            if (InternetState.isConnected(getActivity())) {
                showProgressDialog(getActivity(), getString(R.string.waiit));

                apiService.getClientRegister(convertFileToMultipart(path, "profile_image")
                        , convertToRequestBody(name)
                        , convertToRequestBody(email)
                        , convertToRequestBody(password)
                        , convertToRequestBody(password_confirmation)
                        , convertToRequestBody(phone)
                        , convertToRequestBody(address)
                        , convertToRequestBody(String.valueOf(RegionsId.get(region_id))))
                        .enqueue(new Callback<ClientLogin>() {
                            @Override
                            public void onResponse(Call<ClientLogin> call, Response<ClientLogin> response) {
                                try {
                                    if (response.body().getStatus() == 1) {
                                        Client user = response.body().getData().getUser();
                                        user.setApiToken(response.body().getData().getApiToken());
                                        saveClientData(getActivity(), user);
                                        showSuccessToastMessage(getActivity(), response.body().getMsg());
                                        SharedPreferencesManger.SaveData(getActivity(), CLIENTPASSWORD, password);
                                        setClientName(getActivity(), CLIENTREMEMBER);

                                        if (SharedPreferencesManger.getClientLooginType(getActivity()).equals(CLIENTLOGINTYPE)) {
//                                            Intent intent = new Intent(getActivity(), Navigation_Activity.class);
//                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                        dismissProgressDialog();
                                    } else {
                                        dismissProgressDialog();
                                        showToastMessage(getActivity(), response.body().getMsg());

                                    }

                                } catch (Exception e) {

                                }
                                dismissProgressDialog();

                            }

                            @Override
                            public void onFailure(Call<ClientLogin> call, Throwable t) {
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


}
