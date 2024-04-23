package com.ashraf.amr.apps.sofra.ui.fragments.authCycle;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.data.model.client.newpassword.NewPassword;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.ProfileActivity;
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
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.SaveData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class New_Password_Fragment extends BaseFragment {

    @BindView(R.id.New_Password_Fragment_Tv_EnterCode)
    TextView NewPasswordFragmentTvEnterCode;
    @BindView(R.id.New_Password_Fragment_Et_TheCode)
    EditText NewPasswordFragmentEtTheCode;
    @BindView(R.id.New_Password_Fragment_Tv_EnterNewPassword)
    TextView NewPasswordFragmentTvEnterNewPassword;
    @BindView(R.id.New_Password_Fragment_Et_NewPassword)
    EditText NewPasswordFragmentEtNewPassword;
    @BindView(R.id.New_Password_Fragment_Et_NewPassword_Confirm)
    EditText NewPasswordFragmentEtNewPasswordConfirm;
    @BindView(R.id.New_Password_Fragment_Btn_Send)
    Button NewPasswordFragmentBtnSend;
    @BindView(R.id.Lin)
    RelativeLayout Lin;
    Unbinder unbinder;

    private ApiServices apiService;

    public New_Password_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new__password, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.New_Password_Fragment_Btn_Send, R.id.Lin})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), view);
        switch (view.getId()) {
            case R.id.New_Password_Fragment_Btn_Send:
                validation();
                break;
            case R.id.Lin:
                break;
        }
    }

    private void validation() {


        String code = NewPasswordFragmentEtTheCode.getText().toString().trim();
        String password = NewPasswordFragmentEtNewPassword.getText().toString().trim();
        String password_confirmation = NewPasswordFragmentEtNewPasswordConfirm.getText().toString().trim();
        if (code.isEmpty()
                & password.isEmpty()
                & password_confirmation.isEmpty()
        ) {
            showToastMessage(getActivity(), getString(R.string.enter_inf));
        } else if (code.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_code));

        } else if (password.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_password));
        } else if (password.length() < 4) {
            showToastMessage(getActivity(), getResources().getString(R.string.passwordWeak));
        } else if (password_confirmation.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_password_conf));
        } else if (!password_confirmation.equals(password)) {
            showToastMessage(getActivity(), getResources().getString(R.string.invalid_confirm_password));
            return;
        } else {
            if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
                changeRestaurantPassword(code, password, password_confirmation);
            }
            if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
                changeClientPassword(code, password, password_confirmation);
            }
        }

    }

    private void changeClientPassword(String code, String password, String password_confirmation) {


        if (InternetState.isConnected(getActivity())) {
            HelperClass.showProgressDialog(getActivity(), getString(R.string.waiit));
            apiService.getnewpassword(code, password, password_confirmation).enqueue(new Callback<NewPassword>() {

                @Override
                public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                    try {
                        disappearKeypad(getActivity(), getView());
                        dismissProgressDialog();
                        if (response.body().getStatus() == 1) {
                            showSuccessToastMessage(getActivity(), response.body().getMsg());
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            SaveData(getActivity(), USER_TYPE, "");
                            SaveData(getActivity(), USER_TYPE, CLIENT);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            dismissProgressDialog();
                            showToastMessage(getActivity(), response.body().getMsg());

                        }
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<NewPassword> call, Throwable t) {
                    dismissProgressDialog();
                    showToastMessage(getActivity(), getString(R.string.error));
                }
            });

        } else {
            dismissProgressDialog();
            showToastMessage(getActivity(), getString(R.string.no_internet));

        }


    }

    private void changeRestaurantPassword(String code, String password, String password_confirmation) {

        if (InternetState.isConnected(getActivity())) {
            HelperClass.showProgressDialog(getActivity(), getString(R.string.waiit));
            apiService.getRestaurantNewpassword(code, password, password_confirmation).enqueue(new Callback<GeneralResponse>() {

                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    try {
                        disappearKeypad(getActivity(), getView());
                        dismissProgressDialog();
                        if (response.body().getStatus() == 1) {
                            showSuccessToastMessage(getActivity(), response.body().getMsg());
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            SaveData(getActivity(), USER_TYPE, "");
                            SaveData(getActivity(), USER_TYPE, RESTAURANT);
                            startActivity(intent);
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

    @Override
    public void onBack() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);

        if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
            SaveData(getActivity(), USER_TYPE, "");
            SaveData(getActivity(), USER_TYPE, RESTAURANT);
        } else {
            SaveData(getActivity(), USER_TYPE, "");
            SaveData(getActivity(), USER_TYPE, CLIENT);
        }

        startActivity(intent);
        getActivity().finish();
    }
}