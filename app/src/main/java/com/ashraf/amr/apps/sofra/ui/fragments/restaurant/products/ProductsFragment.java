package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.products;


import android.content.res.Configuration;
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
import com.ashraf.amr.apps.sofra.adapters.ProductsAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.MyProducts;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
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
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends BaseFragment {


    @BindView(R.id.Restaurant_Products_Fragment_Tv_RestaurantName)
    TextView RestaurantProductsFragmentTvRestaurantName;
    @BindView(R.id.Restaurant_Products_Fragment_Rv)
    RecyclerView RestaurantProductsFragmentRv;
    @BindView(R.id.Restaurant_Products_Fragment_Fb)
    FloatingActionButton RestaurantProductsFragmentFb;
    Unbinder unbinder;
    @BindView(R.id.Restaurant_Products_Fragment_Tv_NoResults)
    TextView RestaurantProductsFragmentTvNoResults;
    @BindView(R.id.Restaurant_Products_Fragment_srl_products_list_refresh)
    SwipeRefreshLayout RestaurantProductsFragmentSrlProductsListRefresh;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;
    private ApiServices apiService;
    private RestaurantData user;
    public List<ProductsData> productsDataList = new ArrayList<>();
    public ProductsAdapter productsAdapter;
    public int category_id;
    private int max = 0;
    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;
    public String name;

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        RestaurantProductsFragmentTvRestaurantName.setText(name);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        initRecyclerView();

        if (productsDataList.size() == 0) {
            SflShimmerFrameLayout.startShimmer();
            SflShimmerFrameLayout.setVisibility(View.VISIBLE);
            getList(1);
        }
        if (productsDataList.size() != 0) {
            SflShimmerFrameLayout.stopShimmer();
            SflShimmerFrameLayout.setVisibility(View.GONE);
        }

        refresh();
        return view;
    }

    private void initRecyclerView() {

        linearLayoutManager = new LinearLayoutManager(getActivity());
        RestaurantProductsFragmentRv.setLayoutManager(linearLayoutManager);
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

        RestaurantProductsFragmentRv.addOnScrollListener(onEndLess);
        productsAdapter = new ProductsAdapter(getActivity(), getActivity(), productsDataList);
        RestaurantProductsFragmentRv.setAdapter(productsAdapter);

    }

    private void refresh() {
        RestaurantProductsFragmentSrlProductsListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;

                productsDataList = new ArrayList<>();
                productsAdapter = new ProductsAdapter(getActivity(), getActivity(), productsDataList);
                RestaurantProductsFragmentRv.setAdapter(productsAdapter);
                RestaurantProductsFragmentTvNoResults.setVisibility(View.GONE);

                SflShimmerFrameLayout.startShimmer();
                SflShimmerFrameLayout.setVisibility(View.VISIBLE);
                getList(1);
            }
        });

    }

    private void getList(final int page) {
        if (InternetState.isConnected(getActivity())) {
//            showProgressDialog(getActivity(), getString(R.string.waiit));

            apiService.getProductsList(user.getApiToken(), page, category_id).enqueue(new Callback<MyProducts>() {
                @Override
                public void onResponse(Call<MyProducts> call, Response<MyProducts> response) {
                    try {
                        dismissProgressDialog();
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        RestaurantProductsFragmentSrlProductsListRefresh.setRefreshing(false);
                        if (response.body().getStatus() == 1) {
                            if (page == 1) {
                                if (response.body().getData().getTotal() > 0) {
                                    RestaurantProductsFragmentTvNoResults.setVisibility(View.GONE);
                                } else {
                                    RestaurantProductsFragmentTvNoResults.setVisibility(View.VISIBLE);

                                }

                                onEndLess.current_page = 1;
                                onEndLess.previousTotal = 0;
                                onEndLess.previous_page = 1;

                                max = 0;

                                productsDataList = new ArrayList<>();
                                productsAdapter = new ProductsAdapter(getActivity(), getActivity(), productsDataList);
                                RestaurantProductsFragmentRv.setAdapter(productsAdapter);

                            }

                            max = response.body().getData().getLastPage();
                            productsDataList.addAll(response.body().getData().getData());
                            productsAdapter.notifyDataSetChanged();
                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }

                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<MyProducts> call, Throwable t) {
                    SflShimmerFrameLayout.stopShimmer();
                    SflShimmerFrameLayout.setVisibility(View.GONE);
                    RestaurantProductsFragmentSrlProductsListRefresh.setRefreshing(false);
                    showToastMessage(getActivity(), getString(R.string.error));

                }
            });

        } else {
            try {
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
                RestaurantProductsFragmentSrlProductsListRefresh.setRefreshing(false);
            } catch (Exception e) {

            }
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }

    }

    @OnClick(R.id.Restaurant_Products_Fragment_Fb)
    public void onViewClicked() {
        AddNewProductFragment addNewProduct = new AddNewProductFragment();
        addNewProduct.categoryId = category_id;
        replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, addNewProduct);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        super.onConfigurationChanged(newConfig);
        productsAdapter = new ProductsAdapter(getActivity(), getActivity(), productsDataList);
        RestaurantProductsFragmentRv.setAdapter(productsAdapter);
        linearLayoutManager.scrollToPositionWithOffset(position, 20);
    }
}
