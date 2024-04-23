package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.general;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.commissions.Commissions;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;


public class CommissionFragment extends BaseFragment {


    @BindView(R.id.Commission_Fragment_Tv_Title)
    TextView CommissionFragmentTvTitle;
    @BindView(R.id.Commission_Fragment_Tv_Details)
    TextView CommissionFragmentTvDetails;
    @BindView(R.id.Commission_Fragment_Tv_Total)
    TextView CommissionFragmentTvTotal;
    @BindView(R.id.Commission_Fragment_Tv_Commissions)
    TextView CommissionFragmentTvCommissions;
    @BindView(R.id.Commission_Fragment_Tv_Paid)
    TextView CommissionFragmentTvPaid;
    @BindView(R.id.Commission_Fragment_Tv_Net)
    TextView CommissionFragmentTvNet;
    Unbinder unbinder;
    private ApiServices apiService;
    private RestaurantData user;

    public CommissionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_commession, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        getCommissions();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getCommissions() {
        if (InternetState.isConnected(getActivity())) {
            HelperClass.showProgressDialog(getActivity() , getString(R.string.commession_in_progress));
            apiService.getCommisions(user.getApiToken()).enqueue(new Callback<Commissions>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<Commissions> call, Response<Commissions> response) {
                    try {
                        dismissProgressDialog();
                        if (response.body().getStatus() == 1) {
                            CommissionFragmentTvTotal.setText(getString(R.string.restaurant_sells) + response.body().getData().getTotal() + " ريال ");
                            CommissionFragmentTvCommissions.setText(getString(R.string.restaurant_commessions) + response.body().getData().getCommissions() + " ريال ");
                            CommissionFragmentTvPaid.setText(getString(R.string.paid) + response.body().getData().getPayments() + " ريال ");
                            CommissionFragmentTvNet.setText(getString(R.string.remain) + (response.body().getData().getNetCommissions()) + " ريال ");

                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<Commissions> call, Throwable t) {
                    showToastMessage(getActivity(), getString(R.string.error));
                    dismissProgressDialog();

                }
            });


        } else {
            showToastMessage(getActivity(), getString(R.string.no_internet));
            dismissProgressDialog();

        }
    }

}
