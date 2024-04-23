package com.ashraf.amr.apps.sofra.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.ClientNotificationsListAdapter;
import com.ashraf.amr.apps.sofra.adapters.RestaurantNotificationsListAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.notifications.clientnotificationlist.ClientNotificationList;
import com.ashraf.amr.apps.sofra.data.model.notifications.clientnotificationlist.ClientNotificationsListData;
import com.ashraf.amr.apps.sofra.data.model.notifications.restaurantnotificationlist.RestaurantNotificationList;
import com.ashraf.amr.apps.sofra.data.model.notifications.restaurantnotificationlist.RestaurantNotificationsListData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.helper.OnEndLess;
import com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle.Restaurants_List_Fragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.backBtn;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;

public class Notification_List_Fragment extends BaseFragment {


    @BindView(R.id.Notification_List_Fragment_Rv)
    RecyclerView NotificationListFragmentRv;
    Unbinder unbinder;
    @BindView(R.id.Notification_List_Fragment_srl_notification_list_refresh)
    SwipeRefreshLayout NotificationListFragmentSrlNotificationListRefresh;
    @BindView(R.id.Notification_List_Fragment_Tv_No_Results)
    TextView NotificationListFragmentTvNoResults;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;

    private RestaurantData user;
    private Client client;
    private ApiServices apiService;
    private int max = 0;
    //private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;
    private ClientNotificationsListAdapter clientNotificationsListAdapter;
    private List<ClientNotificationsListData> clientNotificationsListData = new ArrayList<>();

    private RestaurantNotificationsListAdapter restaurantNotificationsListAdapter;
    private List<RestaurantNotificationsListData> restaurantNotificationsListData = new ArrayList<>();
    private View convertView;

    public Notification_List_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification__list, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        client = SharedPreferencesManger.loadClientData(getActivity());
        convertView = view;

        if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
            initRecyclerView();
            if (restaurantNotificationsListData.size() == 0) {
                SflShimmerFrameLayout.startShimmer();
                SflShimmerFrameLayout.setVisibility(View.VISIBLE);
                getRestaurantList(1);
            } else {
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
            }
        }

        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
            navBar.setVisibility(View.GONE);
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity()!=null){
                        Restaurants_List_Fragment restaurantsList = new Restaurants_List_Fragment();
                        replaceFragment(getActivity().getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, restaurantsList);
                    }
                }
            });

            initRecyclerView();
            if (clientNotificationsListData.size() == 0) {
                SflShimmerFrameLayout.startShimmer();
                SflShimmerFrameLayout.setVisibility(View.VISIBLE);
                getClientList(1);
            } else {
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
            }

        }
        refresh();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        NotificationListFragmentRv.setLayoutManager(manager);
        onEndLess = new OnEndLess(manager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max) {
                    if (max != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
                            SflShimmerFrameLayout.stopShimmer();
                            SflShimmerFrameLayout.setVisibility(View.GONE);
                            getRestaurantList(current_page);

                        }
                        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
                            SflShimmerFrameLayout.stopShimmer();
                            SflShimmerFrameLayout.setVisibility(View.GONE);
                            getClientList(current_page);
                        }

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
            NotificationListFragmentRv.addOnScrollListener(onEndLess);
            restaurantNotificationsListAdapter = new RestaurantNotificationsListAdapter(getActivity(), getActivity(), restaurantNotificationsListData);
            NotificationListFragmentRv.setAdapter(restaurantNotificationsListAdapter);
        }
        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
            NotificationListFragmentRv.addOnScrollListener(onEndLess);
            clientNotificationsListAdapter = new ClientNotificationsListAdapter(getActivity(), getActivity(), clientNotificationsListData, convertView);
            NotificationListFragmentRv.setAdapter(clientNotificationsListAdapter);
        }
    }

    private void refresh() {
        NotificationListFragmentSrlNotificationListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;
                if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
                    restaurantNotificationsListData = new ArrayList<>();
                    restaurantNotificationsListAdapter = new RestaurantNotificationsListAdapter(getActivity(), getActivity(), restaurantNotificationsListData);
                    NotificationListFragmentRv.setAdapter(restaurantNotificationsListAdapter);
                    NotificationListFragmentTvNoResults.setVisibility(View.GONE);

                }
                if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
                    clientNotificationsListData = new ArrayList<>();
                    clientNotificationsListAdapter = new ClientNotificationsListAdapter(getActivity(), getActivity(), clientNotificationsListData, convertView);
                    NotificationListFragmentRv.setAdapter(clientNotificationsListAdapter);
                    NotificationListFragmentTvNoResults.setVisibility(View.GONE);

                }
                if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
                    SflShimmerFrameLayout.startShimmer();
                    SflShimmerFrameLayout.setVisibility(View.VISIBLE);
                    getRestaurantList(1);

                }
                if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
                    SflShimmerFrameLayout.startShimmer();
                    SflShimmerFrameLayout.setVisibility(View.VISIBLE);
                    getClientList(1);

                }

            }
        });

    }

    private void getClientList(final int page) {
        if (InternetState.isConnected(getActivity())) {
            apiService.getClientNotifications(client.getApiToken(), page).enqueue(new Callback<ClientNotificationList>() {
                @Override
                public void onResponse(@NonNull Call<ClientNotificationList> call, @NonNull Response<ClientNotificationList> response) {
                    try {
                        dismissProgressDialog();
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        NotificationListFragmentSrlNotificationListRefresh.setRefreshing(false);
                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {
                            if (page == 1) {
                                if (response.body().getData().getTotal() > 0) {
                                    NotificationListFragmentTvNoResults.setVisibility(View.GONE);
                                } else {
                                    NotificationListFragmentTvNoResults.setVisibility(View.VISIBLE);

                                }

                                onEndLess.current_page = 1;
                                onEndLess.previousTotal = 0;
                                onEndLess.previous_page = 1;

                                max = 0;

                                clientNotificationsListData = new ArrayList<>();
                                clientNotificationsListAdapter = new ClientNotificationsListAdapter(getActivity(), getActivity(), clientNotificationsListData, convertView);
                                NotificationListFragmentRv.setAdapter(clientNotificationsListAdapter);


                            }
                            max = response.body().getData().getLastPage();
                            clientNotificationsListData.addAll(response.body().getData().getData());
                            clientNotificationsListAdapter.notifyDataSetChanged();


                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }
                        dismissProgressDialog();
                    } catch (Exception e) {
                        //
                    }


                }

                @Override
                public void onFailure(@NonNull Call<ClientNotificationList> call, @NonNull Throwable t) {
                    SflShimmerFrameLayout.stopShimmer();
                    SflShimmerFrameLayout.setVisibility(View.GONE);
                    showToastMessage(getActivity(), getString(R.string.error));
                    NotificationListFragmentSrlNotificationListRefresh.setRefreshing(false);

                }
            });

        } else {
            try {
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
                NotificationListFragmentSrlNotificationListRefresh.setRefreshing(false);
            } catch (Exception e) {
                //
            }
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }

    }

    private void getRestaurantList(final int page) {
        if (InternetState.isConnected(getActivity())) {
            apiService.getRestaurantNotifications(user.getApiToken(), page).enqueue(new Callback<RestaurantNotificationList>() {
                @Override
                public void onResponse(@NonNull Call<RestaurantNotificationList> call, @NonNull Response<RestaurantNotificationList> response) {
                    try {
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        NotificationListFragmentSrlNotificationListRefresh.setRefreshing(false);
                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {
                            if (page == 1) {
                                if (response.body().getData().getTotal() > 0) {
                                    NotificationListFragmentTvNoResults.setVisibility(View.GONE);
                                } else {
                                    NotificationListFragmentTvNoResults.setVisibility(View.VISIBLE);

                                }

                                onEndLess.current_page = 1;
                                onEndLess.previousTotal = 0;
                                onEndLess.previous_page = 1;

                                max = 0;

                                restaurantNotificationsListData = new ArrayList<>();
                                restaurantNotificationsListAdapter = new RestaurantNotificationsListAdapter(getActivity(), getActivity(), restaurantNotificationsListData);
                                NotificationListFragmentRv.setAdapter(restaurantNotificationsListAdapter);


                            }
                            max = response.body().getData().getLastPage();
                            restaurantNotificationsListData.addAll(response.body().getData().getData());
                            restaurantNotificationsListAdapter.notifyDataSetChanged();


                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }
                    } catch (Exception e) {
                        //
                    }


                }

                @Override
                public void onFailure(@NonNull Call<RestaurantNotificationList> call, @NonNull Throwable t) {
                    SflShimmerFrameLayout.stopShimmer();
                    SflShimmerFrameLayout.setVisibility(View.GONE);
                    showToastMessage(getActivity(), getString(R.string.error));
                    NotificationListFragmentSrlNotificationListRefresh.setRefreshing(false);

                }
            });

        } else {
            try {
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
                NotificationListFragmentSrlNotificationListRefresh.setRefreshing(false);
            } catch (Exception e) {
                //
            }
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }

    }
    @Override
    public void onBack() {

        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
            replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                    , homeActivity.restaurantsList);

        }
        if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
            replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                    , homeActivity.categoriesFragment);

        }

    }
}
