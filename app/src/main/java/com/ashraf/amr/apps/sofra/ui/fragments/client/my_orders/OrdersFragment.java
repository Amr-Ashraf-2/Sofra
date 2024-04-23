package com.ashraf.amr.apps.sofra.ui.fragments.client.my_orders;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.OrdersAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.client.listOfOrders.ListOfOrders;
import com.ashraf.amr.apps.sofra.data.model.client.listOfOrders.OrdersData;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.helper.OnEndLess;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle.My_Orders_Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends BaseFragment {

    @BindView(R.id.Current_Orders_Fragment_Rv)
    RecyclerView CurrentOrdersFragmentRv;
    @BindView(R.id.Current_Orders_Fragment_Srl_Orders_List_Refresh)
    SwipeRefreshLayout CurrentOrdersFragmentSrlOrdersListRefresh;
    @BindView(R.id.Current_Orders_Fragment_Tv_No_Results)
    TextView CurrentOrdersFragmentTvNoResults;
    Unbinder unbinder;

    public My_Orders_Fragment my_orders_fragment;
    public String state;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;
    private int max = 0;
    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;
    private ApiServices apiService;
    private Client client;
    public OrdersAdapter ordersAdapter;
    public List<OrdersData> ordersDataList = new ArrayList<>();

    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current__orders, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        client = SharedPreferencesManger.loadClientData(getActivity());
        initRecyclerView();
        if (ordersDataList.size() == 0) {
            SflShimmerFrameLayout.startShimmer();
            SflShimmerFrameLayout.setVisibility(View.VISIBLE);
            getList(1);
        }
        if (ordersDataList.size() != 0) {
            SflShimmerFrameLayout.stopShimmer();
            SflShimmerFrameLayout.setVisibility(View.GONE);
        }
        refresh();


        return view;
    }

    private void initRecyclerView() {


        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        CurrentOrdersFragmentRv.setLayoutManager(manager);
        onEndLess = new OnEndLess(manager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max) {
                    if (max != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        getList(current_page);
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        CurrentOrdersFragmentRv.addOnScrollListener(onEndLess);
        ordersAdapter = new OrdersAdapter(my_orders_fragment, state, getActivity(), getActivity(), ordersDataList);
        CurrentOrdersFragmentRv.setAdapter(ordersAdapter);


    }

    private void refresh() {
        CurrentOrdersFragmentSrlOrdersListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;

                ordersDataList = new ArrayList<>();
                ordersAdapter = new OrdersAdapter(my_orders_fragment, state, getActivity(), getActivity(), ordersDataList);
                CurrentOrdersFragmentRv.setAdapter(ordersAdapter);
                CurrentOrdersFragmentTvNoResults.setVisibility(View.GONE);

                SflShimmerFrameLayout.startShimmer();
                SflShimmerFrameLayout.setVisibility(View.VISIBLE);
                getList(1);


            }
        });

    }

    private void getList(final int page) {
        if (InternetState.isConnected(getActivity())) {

            apiService.getOrders(page, client.getApiToken(), state).enqueue(new Callback<ListOfOrders>() {
                @Override
                public void onResponse(Call<ListOfOrders> call, Response<ListOfOrders> response) {
                    try {
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        CurrentOrdersFragmentSrlOrdersListRefresh.setRefreshing(false);

                        if (response.body().getStatus() == 1) {
                            if (page == 1) {
                                if (response.body().getData().getTotal() > 0) {
                                    CurrentOrdersFragmentTvNoResults.setVisibility(View.GONE);
                                } else {
                                    CurrentOrdersFragmentTvNoResults.setVisibility(View.VISIBLE);

                                }

                                onEndLess.current_page = 1;
                                onEndLess.previousTotal = 0;
                                onEndLess.previous_page = 1;

                                max = 0;
                                ordersDataList = new ArrayList<>();
                                ordersAdapter = new OrdersAdapter(my_orders_fragment, state, getActivity(), getActivity(), ordersDataList);
                                CurrentOrdersFragmentRv.setAdapter(ordersAdapter);


                            }
                            max = response.body().getData().getLastPage();
                            ordersDataList.addAll(response.body().getData().getData());
                            ordersAdapter.notifyDataSetChanged();


                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }
                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<ListOfOrders> call, Throwable t) {
                    SflShimmerFrameLayout.stopShimmer();
                    SflShimmerFrameLayout.setVisibility(View.GONE);
                    showToastMessage(getActivity(), getString(R.string.error));
                    CurrentOrdersFragmentSrlOrdersListRefresh.setRefreshing(false);
                }
            });


        } else {
            SflShimmerFrameLayout.stopShimmer();
            SflShimmerFrameLayout.setVisibility(View.GONE);
            CurrentOrdersFragmentSrlOrdersListRefresh.setRefreshing(false);
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
        replaceFragment(baseActivity.getSupportFragmentManager(),
                R.id.Home_Cycle_Activity_FrameLayout
                , homeActivity.restaurantsList);
    }
}
