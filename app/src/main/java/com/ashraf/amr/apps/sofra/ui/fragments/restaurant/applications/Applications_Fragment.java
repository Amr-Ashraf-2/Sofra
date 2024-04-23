package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.applications;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.ApplicationsAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myrequests.MyRequests;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myrequests.MyRequestsData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.helper.OnEndLess;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;


public class Applications_Fragment extends BaseFragment {

    public All_Applications_Fragment allApplications;

    Unbinder unbinder;
    @BindView(R.id.Restaurant_Applications_Fragment_Rv)
    RecyclerView RestaurantApplicationsFragmentRv;
    @BindView(R.id.Restaurant_Applications_Fragment_srl_products_list_refresh)
    SwipeRefreshLayout RestaurantApplicationsFragmentSrlProductsListRefresh;
    @BindView(R.id.Restaurant_Applications_Fragment_Tv_NoResults)
    TextView RestaurantApplicationsFragmentTvNoResults;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;
    private int max = 0;
    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;
    public ApplicationsAdapter applicationsAdapter;
    public List<MyRequestsData> requestsDataList = new ArrayList<>();
    public String Type;
    private ApiServices apiService;
    private RestaurantData user;


    public Applications_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applications, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        initRecyclerView();
        if (requestsDataList.size() == 0) {
            SflShimmerFrameLayout.startShimmer();
            SflShimmerFrameLayout.setVisibility(View.VISIBLE);
            getList(1);
        }
        if (requestsDataList.size() != 0) {
            SflShimmerFrameLayout.stopShimmer();
            SflShimmerFrameLayout.setVisibility(View.GONE);
        }
        refresh();

        return view;

    }

    private void initRecyclerView() {


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        RestaurantApplicationsFragmentRv.setLayoutManager(manager);
        onEndLess = new OnEndLess(manager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max) {
                    if (max != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        getList(current_page);

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        RestaurantApplicationsFragmentRv.addOnScrollListener(onEndLess);
        applicationsAdapter = new ApplicationsAdapter(allApplications, Type, getActivity(), getActivity(), requestsDataList);
        RestaurantApplicationsFragmentRv.setAdapter(applicationsAdapter);

    }

    private void refresh() {
        RestaurantApplicationsFragmentSrlProductsListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;

                requestsDataList = new ArrayList<>();
                applicationsAdapter = new ApplicationsAdapter(allApplications, Type, getActivity(), getActivity(), requestsDataList);
                RestaurantApplicationsFragmentRv.setAdapter(applicationsAdapter);
                RestaurantApplicationsFragmentTvNoResults.setVisibility(View.GONE);
                SflShimmerFrameLayout.startShimmer();
                SflShimmerFrameLayout.setVisibility(View.VISIBLE);

                getList(1);
            }
        });

    }

    private void getList(final int page) {
        if (InternetState.isConnected(getActivity())) {

            apiService.getRequests(user.getApiToken(), Type, page).enqueue(new Callback<MyRequests>() {
                @Override
                public void onResponse(Call<MyRequests> call, Response<MyRequests> response) {
                    try {
                        RestaurantApplicationsFragmentSrlProductsListRefresh.setRefreshing(false);
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        if (response.body().getStatus() == 1) {
                            if (page == 1) {
                                if (response.body().getData().getTotal() > 0) {
                                    RestaurantApplicationsFragmentTvNoResults.setVisibility(View.GONE);
                                } else {
                                    RestaurantApplicationsFragmentTvNoResults.setVisibility(View.VISIBLE);

                                }

                                onEndLess.current_page = 1;
                                onEndLess.previousTotal = 0;
                                onEndLess.previous_page = 1;

                                max = 0;

                                requestsDataList = new ArrayList<>();
                                applicationsAdapter = new ApplicationsAdapter(allApplications, Type, getActivity(), getActivity(), requestsDataList);
                                RestaurantApplicationsFragmentRv.setAdapter(applicationsAdapter);

                            }
                            max = response.body().getData().getLastPage();
                            requestsDataList.addAll(response.body().getData().getData());
                            applicationsAdapter.notifyDataSetChanged();

                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }
                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<MyRequests> call, Throwable t) {
                    SflShimmerFrameLayout.stopShimmer();
                    SflShimmerFrameLayout.setVisibility(View.GONE);
                    showToastMessage(getActivity(), getString(R.string.error));
                }
            });


        } else {
            try {
                RestaurantApplicationsFragmentSrlProductsListRefresh.setRefreshing(false);
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
            } catch (Exception e) {

            }
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                , homeActivity.categoriesFragment);
    }
}
