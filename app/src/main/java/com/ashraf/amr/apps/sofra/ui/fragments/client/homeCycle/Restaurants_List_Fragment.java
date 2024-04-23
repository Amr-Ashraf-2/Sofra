package com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.RestaurantsAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.model.general.cities.Cities;
import com.ashraf.amr.apps.sofra.data.model.general.restaurantslist.RestaurantsList;
import com.ashraf.amr.apps.sofra.data.model.general.restaurantslist.RestaurantsListData;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.helper.OnEndLess;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.SplashActivity;
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

import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.keyboardVisibility;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.backBtn;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Restaurants_List_Fragment extends BaseFragment {


    @BindView(R.id.Restaurants_List_Fragment_Sp_City)
    Spinner RestaurantsListFragmentSpCity;
    @BindView(R.id.Restaurants_List_Fragment_Et_Restaurant_Keyword)
    EditText RestaurantsListFragmentEtRestaurantKeyword;
    @BindView(R.id.Restaurants_List_Fragment_Iv_Search)
    ImageView RestaurantsListFragmentIvSearch;
    @BindView(R.id.Restaurants_List_Fragment_Rv_Restaurants_List)
    RecyclerView RestaurantsListFragmentRvRestaurantsList;

    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;
    @BindView(R.id.Restaurant_Categories_Fragment_srl_Categories_list_refresh)
    SwipeRefreshLayout RestaurantCategoriesFragmentSrlCategoriesListRefresh;
    Unbinder unbinder;
    @BindView(R.id.Restaurants_List_Fragment_Rel_SubView)
    RelativeLayout RestaurantsListFragmentRelSubView;
    @BindView(R.id.Restaurants_List_Fragment_Tv_NoRestaurants)
    TextView RestaurantsListFragmentTvNoRestaurants;
    @BindView(R.id.Restaurants_List_Fragment_Tv_NoResults)
    TextView RestaurantsListFragmentTvNoResults;

    private int max = 0;
    private ApiServices apiService;
    private List<RestaurantsListData> restaurantDataList = new ArrayList<>();
    private RestaurantsAdapter restaurantsAdapter;
    private int city_id = 0;
    private String keyword = "";
    private boolean Filter = false;
    private List<String> citiesTxt = new ArrayList<>();
    private List<Integer> citiesId = new ArrayList<>();
    private OnEndLess onEndLess;
    private ArrayAdapter<String> adapter;

    public Restaurants_List_Fragment() {
        // Required empty public constructor
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Objects.requireNonNull(getActivity()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        View view = inflater.inflate(R.layout.fragment_restaurants__list, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        navBar.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.GONE);


        if (getActivity() != null){
            List<String> spinnerArray = new ArrayList<>();
            spinnerArray.add(getString(R.string.select_city_2));
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    getActivity(), R.layout.spinner_item , spinnerArray);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            RestaurantsListFragmentSpCity.setAdapter(spinnerAdapter);
        }

        setPagination();

        keyboardVisibility(view,getActivity());

        if (restaurantDataList.size() == 0) {
            SflShimmerFrameLayout.startShimmer();
            SflShimmerFrameLayout.setVisibility(View.VISIBLE);
            getRestaurantsList(1);
        } else {
            SflShimmerFrameLayout.stopShimmer();
            SflShimmerFrameLayout.setVisibility(View.GONE);
        }


        getCities();


        refresh();

        return view;
    }

    private void setPagination() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        RestaurantsListFragmentRvRestaurantsList.setLayoutManager(manager);

        onEndLess = new OnEndLess(manager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max) {
                    if (max != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        if (Filter) {
                            searchFilter(current_page);
                        } else {
                            getRestaurantsList(current_page);
                        }

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }

            }
        };

        RestaurantsListFragmentRvRestaurantsList.addOnScrollListener(onEndLess);
        restaurantsAdapter = new RestaurantsAdapter(getActivity(), getActivity(), restaurantDataList);
        RestaurantsListFragmentRvRestaurantsList.setAdapter(restaurantsAdapter);
    }

    private void getRestaurantsList(int page) {
        onCall(apiService.getRestaurantsList(page));
    }
    private void searchFilter(final int page) {
        if (city_id == 0) {
            onCall(apiService.getRestaurantsListFilterNoCity(keyword, page));

        } else {
            onCall(apiService.getRestaurantsListFilter(keyword, city_id, page));
        }

    }

    private void getCities() {
        apiService.getcities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(@NonNull Call<Cities> call, @NonNull Response<Cities> response) {

                try {
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {

                        citiesTxt = new ArrayList<>();
                        citiesId = new ArrayList<>();

                        //citiesTxt.add(getString(R.string.all_cities));
                        citiesTxt.add(getString(R.string.select_city_2));
                        citiesId.add(0);

                        for (int i = 0; i < response.body().getData().getData().size(); i++) {
                            citiesTxt.add(response.body().getData().getData().get(i).getName());
                            citiesId.add(response.body().getData().getData().get(i).getId());
                        }

                        int selectedItemPosition = 0;

                        if (adapter != null) {
                            selectedItemPosition = RestaurantsListFragmentSpCity.getSelectedItemPosition();

                        }

                        if (getActivity() != null){
                            adapter = new ArrayAdapter<>(getActivity(),
                                    R.layout.spinner_item, citiesTxt);
                        }



                        RestaurantsListFragmentSpCity.setAdapter(adapter);

                        RestaurantsListFragmentSpCity.setSelection(selectedItemPosition);

                        RestaurantsListFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    city_id = citiesId.get(i);

                                } else {
                                    city_id = 0;

                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }
                } catch (Exception e) {
                    //
                }

            }

            @Override
            public void onFailure(@NonNull Call<Cities> call, @NonNull Throwable t) {

            }
        });
    }


    private void refresh() {
        RestaurantCategoriesFragmentSrlCategoriesListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;

                restaurantDataList = new ArrayList<>();
                restaurantsAdapter = new RestaurantsAdapter(getActivity(), getActivity(), restaurantDataList);
                RestaurantsListFragmentRvRestaurantsList.setAdapter(restaurantsAdapter);

                SflShimmerFrameLayout.startShimmer();
                SflShimmerFrameLayout.setVisibility(View.VISIBLE);

                if (Filter) {
                    searchFilter(1);
                } else {
                    getRestaurantsList(1);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Restaurants_List_Fragment_Iv_Search, R.id.Restaurants_List_Fragment_Rel_SubView})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.Restaurants_List_Fragment_Iv_Search:
                keyword = RestaurantsListFragmentEtRestaurantKeyword.getText().toString().trim();
                if (city_id == 0 && keyword.equals("")) {
                        onEndLess.current_page = 1;
                        onEndLess.previousTotal = 0;
                        onEndLess.previous_page = 1;

                        max = 0;

                        restaurantDataList = new ArrayList<>();
                        restaurantsAdapter = new RestaurantsAdapter(getActivity(), getActivity(), restaurantDataList);
                        RestaurantsListFragmentRvRestaurantsList.setAdapter(restaurantsAdapter);

                        SflShimmerFrameLayout.startShimmer();
                        SflShimmerFrameLayout.setVisibility(View.VISIBLE);
                        getRestaurantsList(1);


                } else {
                    Filter = true;

                    onEndLess.current_page = 1;
                    onEndLess.previousTotal = 0;
                    onEndLess.previous_page = 1;

                    max = 0;

                    restaurantDataList = new ArrayList<>();
                    restaurantsAdapter = new RestaurantsAdapter(getActivity(), getActivity(), restaurantDataList);
                    RestaurantsListFragmentRvRestaurantsList.setAdapter(restaurantsAdapter);

                    SflShimmerFrameLayout.startShimmer();
                    SflShimmerFrameLayout.setVisibility(View.VISIBLE);
                    searchFilter(1);
                }
                break;
            case R.id.Restaurants_List_Fragment_Rel_SubView:
                break;


        }
    }


                                                        // final int page
    private void onCall(Call<RestaurantsList> restaurantsList) {
        if (InternetState.isConnected(getActivity())) {
            restaurantsList.enqueue(new Callback<RestaurantsList>() {
                @Override
                public void onResponse(@NonNull Call<RestaurantsList> call, @NonNull Response<RestaurantsList> response) {

                    try {

                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        RestaurantCategoriesFragmentSrlCategoriesListRefresh.setRefreshing(false);

                        assert response.body() != null;
                        if (response.body().getStatus() == 1) {

                            if (response.body().getData().getTotal() > 0) {
                                RestaurantsListFragmentTvNoRestaurants.setVisibility(View.GONE);

                            } else {
                                RestaurantsListFragmentTvNoRestaurants.setVisibility(View.VISIBLE);

                            }


                            max = response.body().getData().getLastPage();
                            restaurantDataList.addAll(response.body().getData().getData());
                            restaurantsAdapter.notifyDataSetChanged();
                        } else {
                            showToastMessage(getActivity(), response.body().getMsg());

                        }

                    } catch (Exception e) {
                        //
                        Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<RestaurantsList> call, @NonNull Throwable t) {
                    try {
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        RestaurantCategoriesFragmentSrlCategoriesListRefresh.setRefreshing(false);
                        Toast.makeText(getActivity(), t.getMessage() , Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        //
                        Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_SHORT).show();

                    }
                    showToastMessage(getActivity(), getString(R.string.error));

                }
            });

        } else {
            try {
                SflShimmerFrameLayout.stopShimmer();
                SflShimmerFrameLayout.setVisibility(View.GONE);
                RestaurantCategoriesFragmentSrlCategoriesListRefresh.setRefreshing(false);
            } catch (Exception e) {
                //
            }
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }
    }


    @Override
    public void onBack() {
        //getActivity().finish();
        startActivity(new Intent(getActivity(),SplashActivity.class));
    }
}