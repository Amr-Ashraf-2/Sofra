package com.ashraf.amr.apps.sofra.ui.fragments.authCycle;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.ClientLogin;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantLogin;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.authCycle.auth.RestaurantRegisterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTPASSWORD;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTREMEMBER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.REMEMBER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT_PASSWORD;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.SaveData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.saveClientData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.saveRestaurantData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.setClientName;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.setUserName;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;


public class Login_Fragment extends BaseFragment {

    @BindView(R.id.Login_Fragment_Tv_Login)
    TextView LoginFragmentTvLogin;
    @BindView(R.id.Login_Fragment_TIE_Email)
    TextInputEditText LoginFragmentTIEEmail;
    @BindView(R.id.Login_Fragment_TIE_Password)
    TextInputEditText LoginFragmentTIEPassword;
    @BindView(R.id.Login_Fragment_Tv_ForgetPassword)
    TextView LoginFragmentTvForgetPassword;
    @BindView(R.id.Tv)
    TextView Tv;
    @BindView(R.id.Login_Fragment_Rl_Login)
    RelativeLayout LoginFragmentRlLogin;
    Unbinder unbinder;
    @BindView(R.id.Login_Fragment_Rel_No_Account)
    RelativeLayout LoginFragmentRelNoAccount;
    @BindView(R.id.Rel)
    RelativeLayout Rel;
    private ApiServices apiService;

    public Login_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Login_Fragment_Tv_ForgetPassword,
            R.id.Login_Fragment_Rl_Login, R.id.Login_Fragment_Rel_No_Account, R.id.Rel})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), view);
        switch (view.getId()) {
            case R.id.Login_Fragment_Tv_ForgetPassword:
                Forget_Password_Fragment forgetPassword = new Forget_Password_Fragment();
                HelperClass.replaceFragment(getFragmentManager(), R.id.Profile_Frame, forgetPassword);
                break;
            case R.id.Login_Fragment_Rl_Login:
                validation();

                break;

            case R.id.Login_Fragment_Rel_No_Account:
                if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
                    RestaurantRegisterFragment restaurantRegister = new RestaurantRegisterFragment();
                    HelperClass.replaceFragment(getFragmentManager(), R.id.Profile_Frame, restaurantRegister);
                }
                if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
                    ClientRegisterFragment register = new ClientRegisterFragment();
                    HelperClass.replaceFragment(getFragmentManager(), R.id.Profile_Frame, register);
                }

                break;
            case R.id.Rel:

                break;
        }
    }

    private void validation() {
        String email = LoginFragmentTIEEmail.getText().toString().trim();
        final String password = LoginFragmentTIEPassword.getText().toString().trim();


        if (email.isEmpty() & password.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_inf));
        } else if (email.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_email));

        }  else if (password.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_password));
        } else {
            if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
                restaurantlogin(email, password);
            }
            if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
                clientLogin(email, password);
            }
        }

    }

    private void clientLogin(String email, final String password) {


        if (InternetState.isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.waiit));

            apiService.getClientlogin(email, password).enqueue(new Callback<ClientLogin>() {
                @Override
                public void onResponse(Call<ClientLogin> call, Response<ClientLogin> response) {
                    try {
                        if (response.body().getStatus() == 1) {
                            dismissProgressDialog();
                            Client client = response.body().getData().getUser();
                            client.setApiToken(response.body().getData().getApiToken());
                            saveClientData(getActivity(), client);
                            SaveData(getActivity(), USER_TYPE, "");
                            SaveData(getActivity(), USER_TYPE, CLIENT);
                            setClientName(getActivity(), CLIENTREMEMBER);
                            SaveData(getActivity(), CLIENTPASSWORD, password);
                            Intent intent = new Intent(getActivity(), Navigation_Activity.class);
                            startActivity(intent);
                            getActivity().finish();
                            showSuccessToastMessage(getActivity(), response.body().getMsg());

                        } else {
                            showToastMessage(getActivity(), getString(R.string.make_sure));

                        }

                    } catch (Exception e) {

                    }
                    disappearKeypad(getActivity(), getView());
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

    private void restaurantlogin(String email, final String password) {

        if (InternetState.isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.waiit));

            apiService.getRestaurantlogin(email, password).enqueue(new Callback<RestaurantLogin>() {
                @Override
                public void onResponse(Call<RestaurantLogin> call, Response<RestaurantLogin> response) {
                    try {
                        if (response.body().getStatus() == 1) {
                            dismissProgressDialog();
                            RestaurantData user = response.body().getData().getUser();
                            user.setApiToken(response.body().getData().getApiToken());
                            saveRestaurantData(getActivity(), user);
                            setUserName(getActivity(), REMEMBER);
                            SaveData(getActivity(), RESTAURANT_PASSWORD, password);
                            disappearKeypad(getActivity(), getView());
                            Intent intent = new Intent(getActivity(), Navigation_Activity.class);
                            startActivity(intent);
                            getActivity().finish();
                            showSuccessToastMessage(getActivity(), response.body().getMsg());

                        } else {
                            dismissProgressDialog();
                            showToastMessage(getActivity(), response.body().getMsg());

                        }
                    } catch (Exception e) {

                    }

                    dismissProgressDialog();
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


    @Override
    public void onBack() {
        getActivity().finish();
    }
}