package com.ashraf.amr.apps.sofra.ui.fragments.authCycle;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.client.clientresetpass.ClientResetPass;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;
import static com.ashraf.amr.apps.sofra.helper.Validation.setEmailValidation;


public class Forget_Password_Fragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.Forget_Password_Fragment_Et_NewPassword_Email)
    EditText ForgetPasswordFragmentEtNewPasswordEmail;
    @BindView(R.id.Forget_Password_Fragment_Btn_Send)
    Button ForgetPasswordFragmentBtnSend;
    @BindView(R.id.Forget_Password_Fragment_Tv_EnterEmail)
    TextView ForgetPasswordFragmentTvEnterEmail;
    @BindView(R.id.Rel)
    RelativeLayout Rel;
    private ApiServices apiService;
    private Client client;
    private RestaurantData user;

    public Forget_Password_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget__password, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        client = SharedPreferencesManger.loadClientData(getActivity());
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void resetClientPassword() {
        String email = ForgetPasswordFragmentEtNewPasswordEmail.getText().toString().trim();
        if (email.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_email));
        }else if (!setEmailValidation(getActivity(), email)) {
            showToastMessage(getActivity(), getResources().getString(R.string.invalid_Email));
        }  else {
            if (InternetState.isConnected(getActivity())) {
                HelperClass.showProgressDialog(getActivity(), getString(R.string.waiit));
                apiService.getClientResetPassword(email).enqueue(new Callback<ClientResetPass>() {
                    @Override
                    public void onResponse(Call<ClientResetPass> call, Response<ClientResetPass> response) {
                        try {
                            disappearKeypad(getActivity(), getView());
                            dismissProgressDialog();
                            if (response.body().getStatus() == 1) {
                                New_Password_Fragment newPassword = new New_Password_Fragment();
                                replaceFragment(getFragmentManager(), R.id.Profile_Frame, newPassword);
                                showSuccessToastMessage(getActivity(), response.body().getMsg());
                            } else {
                                showToastMessage(getActivity(), getString(R.string.make_sure));
                            }
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<ClientResetPass> call, Throwable t) {
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
    private void resetRestaurantPassword() {
        String email = ForgetPasswordFragmentEtNewPasswordEmail.getText().toString().trim();
        if (email.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_email));
        }
        else if (!setEmailValidation(getActivity(), email)) {
            showToastMessage(getActivity(), getResources().getString(R.string.invalid_Email));
        }
        else {
            if (InternetState.isConnected(getActivity())) {
                HelperClass.showProgressDialog(getActivity(), getString(R.string.waiit));
                apiService.restaurantResetPassword(email).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        try {
                            disappearKeypad(getActivity(), getView());
                            dismissProgressDialog();
                            if (response.body().getStatus() == 1) {
                                New_Password_Fragment newPassword = new New_Password_Fragment();
                                replaceFragment(getFragmentManager(), R.id.Profile_Frame, newPassword);
                                showSuccessToastMessage(getActivity(), response.body().getMsg());
                            } else {
                                showToastMessage(getActivity(), getString(R.string.make_sure));
                            }
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
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


    @OnClick({R.id.Forget_Password_Fragment_Btn_Send, R.id.Rel})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), view);
        switch (view.getId()) {
            case R.id.Forget_Password_Fragment_Btn_Send:
                if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
                    resetRestaurantPassword();
                }
                if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
                    resetClientPassword();
                }
                break;
            case R.id.Rel:
                break;
        }
    }
}