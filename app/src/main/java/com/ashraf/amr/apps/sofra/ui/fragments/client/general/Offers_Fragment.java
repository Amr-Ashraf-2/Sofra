package com.ashraf.amr.apps.sofra.ui.fragments.client.general;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.OffersAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.model.general.listofoffers.ListOfOffers;
import com.ashraf.amr.apps.sofra.data.model.general.listofoffers.NewOffersData;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.helper.OnEndLess;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.general.MoreFragment;
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
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.backBtn;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Offers_Fragment extends BaseFragment {


    @BindView(R.id.Offers_Fragment_Rv)
    RecyclerView OffersFragmentRv;
    Unbinder unbinder;
    @BindView(R.id.Tv_NoResult)
    TextView TvNoResult;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;
    @BindView(R.id.Restaurant_Offers_Fragment_srl_offers_list_refresh)
    SwipeRefreshLayout RestaurantOffersFragmentSrlOffersListRefresh;
    private int max = 0;
    private ApiServices apiService;
    private List<NewOffersData> newOffersList = new ArrayList<>();
    private OffersAdapter newOffersAdapter;
    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;

    public Offers_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offers, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);

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

        initRecyclerView();

        if (newOffersList.size() == 0) {
            SflShimmerFrameLayout.startShimmer();
            SflShimmerFrameLayout.setVisibility(View.VISIBLE);
            getList(1);
        }else {
            SflShimmerFrameLayout.stopShimmer();
            SflShimmerFrameLayout.setVisibility(View.GONE);
        }
        refresh();


        return view;
    }


    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        OffersFragmentRv.setLayoutManager(linearLayoutManager);
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
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
        OffersFragmentRv.addOnScrollListener(onEndLess);
        newOffersAdapter = new OffersAdapter(getActivity(), getActivity(), newOffersList);
        OffersFragmentRv.setAdapter(newOffersAdapter);
    }

    private void refresh() {
        RestaurantOffersFragmentSrlOffersListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;

                newOffersList = new ArrayList<>();
                newOffersAdapter = new OffersAdapter(getActivity(), getActivity(), newOffersList);
                OffersFragmentRv.setAdapter(newOffersAdapter);
                TvNoResult.setVisibility(View.GONE);

                SflShimmerFrameLayout.startShimmer();
                SflShimmerFrameLayout.setVisibility(View.VISIBLE);

                getList(1);
            }
        });

    }


    private void getList(final int page) {
        if (InternetState.isConnected(getActivity())) {
            apiService.getNewOffera(page).enqueue(new Callback<ListOfOffers>() {
                @Override
                public void onResponse(@NonNull Call<ListOfOffers> call, @NonNull Response<ListOfOffers> response) {
                    try {

                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        RestaurantOffersFragmentSrlOffersListRefresh.setRefreshing(false);
                        assert response.body() != null;
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

                                newOffersList = new ArrayList<>();
                                newOffersAdapter = new OffersAdapter(getActivity(), getActivity(), newOffersList);
                                OffersFragmentRv.setAdapter(newOffersAdapter);

                            }

                            max = response.body().getData().getLastPage();
                            newOffersList.addAll(response.body().getData().getData());
                            newOffersAdapter.notifyDataSetChanged();
                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }

                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ListOfOffers> call, @NonNull Throwable t) {
                    //showToastMessage(getActivity(), getString(R.string.error));
                    try {
                        RestaurantOffersFragmentSrlOffersListRefresh.setRefreshing(false);
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                    } catch (Exception e) {
                        //
                    }
                }
            });

        } else {
            try {
                RestaurantOffersFragmentSrlOffersListRefresh.setRefreshing(false);
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
            } catch (Exception e) {
                //
            }
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
