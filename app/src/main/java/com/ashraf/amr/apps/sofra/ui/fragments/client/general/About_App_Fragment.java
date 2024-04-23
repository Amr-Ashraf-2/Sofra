package com.ashraf.amr.apps.sofra.ui.fragments.client.general;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.model.settings.Settings;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.general.MoreFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;
import static com.ashraf.amr.apps.sofra.helper.InternetState.isConnected;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.backBtn;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;


public class About_App_Fragment extends BaseFragment {


    @BindView(R.id.About_App_Fragment_Tv_About)
    TextView AboutAppFragmentTvAbout;
    Unbinder unbinder;
    private ApiServices apiService;

    public About_App_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about__app, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);

        if (navBar != null){
            navBar.setVisibility(View.GONE);
        }

        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
            navBar.setVisibility(View.GONE);
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity()!=null){
                        MoreFragment moreFragment = new MoreFragment();
                        replaceFragment(getActivity().getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, moreFragment);
                    }
                }
            });
        }

        if(isConnected(getActivity())){
            getAboutData();
        }else {
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }

        return view;
    }

    private void getAboutData() {

        if (getActivity()!= null){
            showProgressDialog(getActivity(), getActivity().getString(R.string.please_wait));
        }

        apiService.getSettingsData().enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(@NonNull Call<Settings> call, @NonNull Response<Settings> response) {
                dismissProgressDialog();
                assert response.body() != null;
                if (response.body().getStatus() == 1) {
                    AboutAppFragmentTvAbout.setText(response.body().getData().getAboutApp());
                } else {
                    Toast.makeText(getActivity(), "onResponse status-0: \n" + response.body().getMsg(), Toast.LENGTH_LONG).show();
                    //
                }
            }

            @Override
            public void onFailure(@NonNull Call<Settings> call, @NonNull Throwable t) {
                dismissProgressDialog();
                Toast.makeText(getActivity(), "onFailure : " + t.getMessage() ,Toast.LENGTH_LONG).show();
                //showToastMessage(getActivity(), getString(R.string.error));
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
