package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.offers;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.RestaurantOffersAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.offerlist.OfferList;
import com.ashraf.amr.apps.sofra.data.model.restaurant.offerlist.OffersListData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.helper.OnEndLess;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantOffersFragment extends BaseFragment {


    @BindView(R.id.Restaurant_Offers_Fragment_Rv)
    RecyclerView RestaurantOffersFragmentRv;
    @BindView(R.id.Restaurant_Offers_Fragment_Btn_Add)
    Button RestaurantOffersFragmentBtnAdd;
    @BindView(R.id.Tv_NoResult)
    public TextView TvNoResult;
    @BindView(R.id.Restaurant_Offers_Fragment_srl_ofers_list_refresh)
    SwipeRefreshLayout RestaurantOffersFragmentSrlOfersListRefresh;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;
    Unbinder unbinder;

    private ApiServices apiService;
    private RestaurantData user;
    public List<OffersListData> offersDataList = new ArrayList<>();
    private int max = 0;
    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;
    public RestaurantOffersAdapter restaurantOffersAdapter;

    public RestaurantOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_offers, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());

        initRecyclerView();
        if (offersDataList.size() == 0) {
            SflShimmerFrameLayout.startShimmer();
            SflShimmerFrameLayout.setVisibility(View.VISIBLE);
            getList(1);
        }
        if (offersDataList.size() != 0) {
            SflShimmerFrameLayout.stopShimmer();
            SflShimmerFrameLayout.setVisibility(View.GONE);
            getList(1);
        }
        refresh();

        return view;

    }

    private void initRecyclerView() {

        linearLayoutManager = new LinearLayoutManager(getActivity());
        RestaurantOffersFragmentRv.setLayoutManager(linearLayoutManager);
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max) {
                    if (max != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        getList(current_page);

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        RestaurantOffersFragmentRv.addOnScrollListener(onEndLess);
        restaurantOffersAdapter = new RestaurantOffersAdapter(getActivity(), getActivity(), offersDataList,this);
        RestaurantOffersFragmentRv.setAdapter(restaurantOffersAdapter);
    }

    private void refresh() {
        RestaurantOffersFragmentSrlOfersListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;

                offersDataList = new ArrayList<>();
                restaurantOffersAdapter = new RestaurantOffersAdapter(getActivity(), getActivity(), offersDataList,null);
                RestaurantOffersFragmentRv.setAdapter(restaurantOffersAdapter);
                TvNoResult.setVisibility(View.GONE);

                SflShimmerFrameLayout.startShimmer();
                SflShimmerFrameLayout.setVisibility(View.VISIBLE);

                getList(1);
            }
        });

    }

    private void getList(final int page) {
        if (InternetState.isConnected(getActivity())) {
            apiService.getOffers(user.getApiToken(), page).enqueue(new Callback<OfferList>() {
                @Override
                public void onResponse(Call<OfferList> call, Response<OfferList> response) {
                    try {

                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        RestaurantOffersFragmentSrlOfersListRefresh.setRefreshing(false);
                        if (response.body().getStatus() == 1) {
                            if (page == 1) {
                                if (response.body().getData().getTotal() > 0) {
                                    TvNoResult.setVisibility(View.GONE);
                                } else {
                                    TvNoResult.setVisibility(View.VISIBLE);

                                }

                                onEndLess.current_page = 1;
                                onEndLess.previousTotal = 0;
                                onEndLess.previous_page = 1;

                                max = 0;

                                offersDataList = new ArrayList<>();
                                restaurantOffersAdapter = new RestaurantOffersAdapter(getActivity(), getActivity(), offersDataList,null);
                                RestaurantOffersFragmentRv.setAdapter(restaurantOffersAdapter);

                            }

                            max = response.body().getData().getLastPage();
                            offersDataList.addAll(response.body().getData().getData());
                            restaurantOffersAdapter.notifyDataSetChanged();
                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }

                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<OfferList> call, Throwable t) {

                    showToastMessage(getActivity(), getString(R.string.error));
                    try {
                        RestaurantOffersFragmentSrlOfersListRefresh.setRefreshing(false);
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                    } catch (Exception e) {

                    }
                }
            });

        } else {

            try {
                RestaurantOffersFragmentSrlOfersListRefresh.setRefreshing(false);
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

    @OnClick(R.id.Restaurant_Offers_Fragment_Btn_Add)
    public void onViewClicked() {
        AddNewOfferFragment addNewOffer = new AddNewOfferFragment();
        HelperClass.replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, addNewOffer);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        super.onConfigurationChanged(newConfig);
        restaurantOffersAdapter = new RestaurantOffersAdapter(getActivity(), getActivity(), offersDataList,null);
        RestaurantOffersFragmentRv.setAdapter(restaurantOffersAdapter);
        linearLayoutManager.scrollToPositionWithOffset(position, 20);
    }
}
