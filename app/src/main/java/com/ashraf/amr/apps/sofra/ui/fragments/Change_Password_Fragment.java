package com.ashraf.amr.apps.sofra.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.InternetState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTPASSWORD;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT_PASSWORD;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class Change_Password_Fragment extends BaseFragment {

    @BindView(R.id.Change_Password_Fragment_Et_Old_Password)
    EditText ChangePasswordFragmentEtOldPassword;
    @BindView(R.id.Change_Password_Fragment_Et_New_Password)
    EditText ChangePasswordFragmentEtNewPassword;
    @BindView(R.id.Change_Password_Fragment_Et_Renter_Password)
    EditText ChangePasswordFragmentEtRenterPassword;
    @BindView(R.id.Change_Password_Fragment_Btn_Change)
    Button ChangePasswordFragmentBtnChange;
    @BindView(R.id.Change_Password_Fragment_Linear_SubView)
    LinearLayout ChangePasswordFragmentLinearSubView;
    Unbinder unbinder;
    private RestaurantData user;
    private Client client;
    private ApiServices apiService;

    public Change_Password_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change__password, container, false);
        unbinder = ButterKnife.bind(this, view);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        client = SharedPreferencesManger.loadClientData(getActivity());
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Change_Password_Fragment_Btn_Change, R.id.Change_Password_Fragment_Linear_SubView})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.Change_Password_Fragment_Btn_Change:
                changePassword();
                break;
            case R.id.Change_Password_Fragment_Linear_SubView:
                break;
        }
    }

    private void changePassword() {
        String old_password = ChangePasswordFragmentEtOldPassword.getText().toString();
        String password = ChangePasswordFragmentEtNewPassword.getText().toString();
        String password_confirmation = ChangePasswordFragmentEtRenterPassword.getText().toString();

        if (old_password.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.enter_old_password));
            return;
        }

        if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
            if (!old_password.equals(SharedPreferencesManger.LoadData(getActivity(), RESTAURANT_PASSWORD))) {
                showToastMessage(getActivity(), getResources().getString(R.string.old_password_wrong));
                return;
            }
        }
        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
            if (!old_password.equals(SharedPreferencesManger.LoadData(getActivity(), CLIENTPASSWORD))) {
                showToastMessage(getActivity(), getResources().getString(R.string.old_password_wrong));
                return;
            }
        }
        if (password.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.enter_newpassword));
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
        if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
            if (InternetState.isConnected(getActivity())) {
                HelperClass.showProgressDialog(getActivity(), getString(R.string.waiit));
                apiService.restaurantChangePassword(user.getApiToken(), old_password, password, password_confirmation)
                        .enqueue(new Callback<GeneralResponse>() {

                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                try {
                                    dismissProgressDialog();
                                    if (response.body().getStatus() == 1) {
                                        showSuccessToastMessage(getActivity(), response.body().getMsg());
//                                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                                        SaveData(getActivity(), USER_TYPE, "");
//                                        SaveData(getActivity(), USER_TYPE, RESTAURANT);
//                                        startActivity(intent);
                                    } else {
                                        dismissProgressDialog();
                                        showToastMessage(getActivity(), response.body().getMsg());

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

            }

        }
        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {

            if (InternetState.isConnected(getActivity())) {
                HelperClass.showProgressDialog(getActivity(), getString(R.string.waiit));
                apiService.clientChangePassword(client.getApiToken(), old_password, password, password_confirmation)
                        .enqueue(new Callback<GeneralResponse>() {

                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                try {
                                    dismissProgressDialog();
                                    if (response.body().getStatus() == 1) {
                                        showSuccessToastMessage(getActivity(), response.body().getMsg());
//                                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                                        SaveData(getActivity(), USER_TYPE, "");
//                                        SaveData(getActivity(), USER_TYPE, CLIENT);
//                                        startActivity(intent);
                                    } else {
                                        dismissProgressDialog();
                                        showToastMessage(getActivity(), response.body().getMsg());

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
}
