package com.ashraf.amr.apps.sofra.ui.fragments.client.restaurantDetails;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.CategoriesAdapter;
import com.ashraf.amr.apps.sofra.adapters.FoodListAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.model.general.categorieslist.CategoriesList;
import com.ashraf.amr.apps.sofra.data.model.general.categorieslist.CategoriesListData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.MyProducts;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.helper.OnEndLess;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
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

import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.backBtn;

/**
 * A simple {@link Fragment} subclass.
 */
public class Restaurant_FoodList_Fragment extends BaseFragment {


    @BindView(R.id.Restaurant_FoodList_Fragment_Rv)
    RecyclerView RestaurantFoodListFragmentRv;
    @BindView(R.id.Restaurants_Food_List_Fragment_Tv_NoResults)
    TextView RestaurantsFoodListFragmentTvNoResults;
    @BindView(R.id.Restaurant_FoodList_Fragment_Categories_Rv)
    RecyclerView RestaurantFoodListFragmentCategoriesRv;
    @BindView(R.id.Restaurant_FoodList_Fragment_Relative)
    RelativeLayout RestaurantFoodListFragmentRelative;
    Unbinder unbinder;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;
    @BindView(R.id.Sfl_ShimmerFrameLayout2)
    ShimmerFrameLayout SflShimmerFrameLayout2;

    private List<CategoriesListData> categoriesListDataList = new ArrayList<>();
    public List<ProductsData> productsData = new ArrayList<>();
    private FoodListAdapter foodListAdapter;
    private CategoriesAdapter categoriesAdapter;

    private ApiServices apiService;
    private int max = 0;

    private OnEndLess onEndLess;
    public int restaurant_id;
    public int category_id = -1;

    public Restaurant_FoodList_Fragment() {
        // Required empty public constructor
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant__food_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        setRecycler();
        setCategoriesRecycler();
        if (categoriesListDataList.size() == 0) {
            SflShimmerFrameLayout.startShimmer();
            SflShimmerFrameLayout.setVisibility(View.VISIBLE);
            getCategoriesList();
        }
        if (categoriesListDataList.size() != 0) {
            SflShimmerFrameLayout.stopShimmer();
            SflShimmerFrameLayout.setVisibility(View.GONE);
        }

        if (productsData.size() == 0) {
            SflShimmerFrameLayout2.startShimmer();
            SflShimmerFrameLayout2.setVisibility(View.VISIBLE);
            getProductsList(1);
        } else {
            SflShimmerFrameLayout2.stopShimmer();
            SflShimmerFrameLayout2.setVisibility(View.GONE);

        }

        return view;
    }

    private void setCategoriesRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RestaurantFoodListFragmentCategoriesRv.setLayoutManager(layoutManager);

        categoriesAdapter = new CategoriesAdapter(getActivity(), getActivity()
                , categoriesListDataList, this);
        RestaurantFoodListFragmentCategoriesRv.setAdapter(categoriesAdapter);


    }

    private void getCategoriesList() {

        if (InternetState.isConnected(getActivity())) {
            apiService.getCategoriesList(restaurant_id, 1).enqueue(new Callback<CategoriesList>() {
                @Override
                public void onResponse(Call<CategoriesList> call, Response<CategoriesList> response) {
                    try {

                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        if (response.body().getStatus() == 1) {
                            categoriesListDataList.addAll(response.body().getData());
                            categoriesAdapter.notifyDataSetChanged();

                        } else {

                        }
                        dismissProgressDialog();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<CategoriesList> call, Throwable t) {
                    dismissProgressDialog();
                    try {
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                    } catch (Exception e) {

                    }

                    showToastMessage(getActivity(), getString(R.string.error));
                }
            });


        } else {
            dismissProgressDialog();
            try {
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
            } catch (Exception e) {

            }
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }


    }

    private void setRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        RestaurantFoodListFragmentRv.setLayoutManager(manager);
        onEndLess = new OnEndLess(manager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max) {
                    if (max != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        getProductsList(current_page);

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        RestaurantFoodListFragmentRv.addOnScrollListener(onEndLess);
        foodListAdapter = new FoodListAdapter(getActivity(), getActivity(), productsData);
        RestaurantFoodListFragmentRv.setAdapter(foodListAdapter);

    }


    public void getProductsList(final int page) {
        if (InternetState.isConnected(getActivity())) {

            if (page == 1) {
                RestaurantsFoodListFragmentTvNoResults.setVisibility(View.GONE);
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;

                productsData = new ArrayList<>();
                foodListAdapter = new FoodListAdapter(getActivity(), getActivity(), productsData);
                RestaurantFoodListFragmentRv.setAdapter(foodListAdapter);
                RestaurantsFoodListFragmentTvNoResults.setVisibility(View.GONE);
                SflShimmerFrameLayout2.startShimmer();
                SflShimmerFrameLayout2.setVisibility(View.VISIBLE);
            }

            apiService.getFoodList(page, restaurant_id, category_id).enqueue(new Callback<MyProducts>() {
                @Override
                public void onResponse(Call<MyProducts> call, Response<MyProducts> response) {
                    try {

                        SflShimmerFrameLayout2.stopShimmer();
                        SflShimmerFrameLayout2.setVisibility(View.GONE);

                        if (response.body().getStatus() == 1) {

                            if (page == 1) {
                                if (response.body().getData().getTotal() > 0) {
                                    RestaurantsFoodListFragmentTvNoResults.setVisibility(View.GONE);
                                } else {
                                    RestaurantsFoodListFragmentTvNoResults.setVisibility(View.VISIBLE);
                                    RestaurantsFoodListFragmentTvNoResults.setText(R.string.no_products);
                                }

                                onEndLess.current_page = 1;
                                onEndLess.previousTotal = 0;
                                onEndLess.previous_page = 1;

                                max = 0;

                                productsData = new ArrayList<>();
                                foodListAdapter = new FoodListAdapter(getActivity(), getActivity(), productsData);
                                RestaurantFoodListFragmentRv.setAdapter(foodListAdapter);

                            }

                            max = response.body().getData().getLastPage();
                            productsData.addAll(response.body().getData().getData());
                            foodListAdapter.notifyDataSetChanged();


                        }

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<MyProducts> call, Throwable t) {
                    try {
                        SflShimmerFrameLayout2.stopShimmer();
                        SflShimmerFrameLayout2.setVisibility(View.GONE);
                    } catch (Exception e) {

                    }
                    showToastMessage(getActivity(), getString(R.string.error));
                }
            });


        } else {
            try {
                SflShimmerFrameLayout2.stopShimmer();
                SflShimmerFrameLayout2.setVisibility(View.GONE);
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

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        int orientation = this.getResources().getConfiguration().orientation;
//        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            // code for portrait mode
//        } else {
//            // code for landscape mode
//        }
//        super.onConfigurationChanged(newConfig);
//
//    }

}