package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.categories;


import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.RestaurantCategoriesAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantcategories.CategoriesData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantcategories.RestaurantCatogries;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.AddCategoryDialog;
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

import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends BaseFragment {

    @BindView(R.id.Restaurant_Categories_Fragment_Rv)
    RecyclerView RestaurantCategoriesFragmentRv;
    @BindView(R.id.Restaurant_Categories_Fragment_srl_Categories_list_refresh)
    SwipeRefreshLayout RestaurantCategoriesFragmentSrlCategoriesListRefresh;
    @BindView(R.id.Restaurant_Categories_Fragment_Fb)
    FloatingActionButton RestaurantCategoriesFragmentFb;
    @BindView(R.id.Restaurant_Categories_Fragment_Tv_NoResults)
    public TextView RestaurantCategoriesFragmentTvNoResults;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout RestaurantCategoriesFragmentSflAnmi;
    Unbinder unbinder;

    private ApiServices apiService;
    private RestaurantData user;
    public List<CategoriesData> categoriesData = new ArrayList<>();
    public RestaurantCategoriesAdapter restaurantCategoriesAdapter;
    private int max = 0;
    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        setUpHomeActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        initRecyclerView();
        if (categoriesData.size() == 0) {
            RestaurantCategoriesFragmentSflAnmi.startShimmer();
            RestaurantCategoriesFragmentSflAnmi.setVisibility(View.VISIBLE);
            getList(1);
        }
        if (categoriesData.size() != 0) {
            RestaurantCategoriesFragmentSflAnmi.stopShimmer();
            RestaurantCategoriesFragmentSflAnmi.setVisibility(View.GONE);
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

        linearLayoutManager = new LinearLayoutManager(getActivity());
        RestaurantCategoriesFragmentRv.setLayoutManager(linearLayoutManager);
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

        RestaurantCategoriesFragmentRv.addOnScrollListener(onEndLess);
        restaurantCategoriesAdapter = new RestaurantCategoriesAdapter(getActivity(), getActivity(), categoriesData,this);
        RestaurantCategoriesFragmentRv.setAdapter(restaurantCategoriesAdapter);


    }

    private void refresh() {
        RestaurantCategoriesFragmentSrlCategoriesListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;

                categoriesData = new ArrayList<>();
                restaurantCategoriesAdapter = new RestaurantCategoriesAdapter(getActivity(), getActivity(), categoriesData,null);
                RestaurantCategoriesFragmentRv.setAdapter(restaurantCategoriesAdapter);
                RestaurantCategoriesFragmentTvNoResults.setVisibility(View.GONE);

                RestaurantCategoriesFragmentSflAnmi.startShimmer();
                RestaurantCategoriesFragmentSflAnmi.setVisibility(View.VISIBLE);

                getList(1);
            }
        });

    }


    private void getList(final int page) {
        if (InternetState.isConnected(getActivity())) {
//            showProgressDialog(getActivity(), getString(R.string.waiit));

            apiService.getCatogries(user.getApiToken(), page).enqueue(new Callback<RestaurantCatogries>() {
                @Override
                public void onResponse(Call<RestaurantCatogries> call, Response<RestaurantCatogries> response) {
                    try {
                        dismissProgressDialog();
                        RestaurantCategoriesFragmentSflAnmi.stopShimmer();
                        RestaurantCategoriesFragmentSflAnmi.setVisibility(View.GONE);
                        RestaurantCategoriesFragmentSrlCategoriesListRefresh.setRefreshing(false);
                        if (response.body().getStatus() == 1) {
                            if (page == 1) {
                                if (response.body().getData().getTotal() > 0) {
                                    RestaurantCategoriesFragmentTvNoResults.setVisibility(View.GONE);
                                } else {
                                    RestaurantCategoriesFragmentTvNoResults.setVisibility(View.VISIBLE);

                                }

                                onEndLess.current_page = 1;
                                onEndLess.previousTotal = 0;
                                onEndLess.previous_page = 1;

                                max = 0;

                                categoriesData = new ArrayList<>();
                                restaurantCategoriesAdapter = new RestaurantCategoriesAdapter(getActivity(), getActivity(), categoriesData,null);
                                RestaurantCategoriesFragmentRv.setAdapter(restaurantCategoriesAdapter);

                            }

                            max = response.body().getData().getLastPage();
                            categoriesData.addAll(response.body().getData().getData());
                            restaurantCategoriesAdapter.notifyDataSetChanged();
                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }

                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<RestaurantCatogries> call, Throwable t) {
                    dismissProgressDialog();
                    try {
                        RestaurantCategoriesFragmentSflAnmi.stopShimmer();
                        RestaurantCategoriesFragmentSflAnmi.setVisibility(View.GONE);
                        RestaurantCategoriesFragmentSrlCategoriesListRefresh.setRefreshing(false);
                    } catch (Exception e) {

                    }
                    showToastMessage(getActivity(), getString(R.string.error));

                }
            });

        } else {
            dismissProgressDialog();
            try {
                RestaurantCategoriesFragmentSflAnmi.stopShimmer();
                RestaurantCategoriesFragmentSflAnmi.setVisibility(View.GONE);
                RestaurantCategoriesFragmentSrlCategoriesListRefresh.setRefreshing(false);
            } catch (Exception e) {

            }
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }

    }


    @OnClick(R.id.Restaurant_Categories_Fragment_Fb)
    public void onViewClicked() {
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog(getActivity(), this);
        addCategoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addCategoryDialog.setCanceledOnTouchOutside(true);
        addCategoryDialog.show();

    }

    @Override
    public void onBack() {
        getActivity().finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        super.onConfigurationChanged(newConfig);
        restaurantCategoriesAdapter = new RestaurantCategoriesAdapter(getActivity(), getActivity(), categoriesData,null);
        RestaurantCategoriesFragmentRv.setAdapter(restaurantCategoriesAdapter);
        linearLayoutManager.scrollToPositionWithOffset(position, 20);
    }
}
