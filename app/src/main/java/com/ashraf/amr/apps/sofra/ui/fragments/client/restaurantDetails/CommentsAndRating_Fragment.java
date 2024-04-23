package com.ashraf.amr.apps.sofra.ui.fragments.client.restaurantDetails;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.CommentsAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.general.comments.Comments;
import com.ashraf.amr.apps.sofra.data.model.general.comments.CommentsData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.AddCommentDialog;
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

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTREMEMBER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.getClientName;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsAndRating_Fragment extends BaseFragment {

    @BindView(R.id.CommentsAndRating_Fragment_Tv_Comment)
    TextView CommentsAndRatingFragmentTvComment;
    @BindView(R.id.CommentsAndRating_Fragment_Btn_Add_Comment)
    Button CommentsAndRatingFragmentBtnAddComment;
    @BindView(R.id.CommentsAndRating_Fragment_Rv)
    RecyclerView CommentsAndRatingFragmentRv;
    Unbinder unbinder;
    public int restaurant_id;
    @BindView(R.id.CommentsAndRating_Fragment_Tv_NoResults)
    TextView CommentsAndRatingFragmentTvNoResults;
    @BindView(R.id.rel)
    RelativeLayout rel;
    @BindView(R.id.Sfl_ShimmerFrameLayout)
    ShimmerFrameLayout SflShimmerFrameLayout;
    @BindView(R.id.Current_Orders_Fragment_Srl_CommentsAndRating_Refresh)
    SwipeRefreshLayout CurrentOrdersFragmentSrlCommentsAndRatingRefresh;


    private ApiServices apiService;
    private int max = 0;
    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;
    private CommentsAdapter commentsAdapter;
    private List<CommentsData> commentsData = new ArrayList<>();
    private AddCommentDialog addCommentDialog;
    private RestaurantData user;
    private Client client;

    public CommentsAndRating_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments_and_rating, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (SharedPreferencesManger.LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
            rel.setVisibility(View.GONE);
        }

        if (SharedPreferencesManger.LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {

        }

        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        client = SharedPreferencesManger.loadClientData(getActivity());

        initRecyclerView();
        if (commentsData.size() == 0) {
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

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        CommentsAndRatingFragmentRv.setLayoutManager(manager);
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
        CommentsAndRatingFragmentRv.addOnScrollListener(onEndLess);
        commentsAdapter = new CommentsAdapter(getActivity(), getActivity(), commentsData);
        CommentsAndRatingFragmentRv.setAdapter(commentsAdapter);

    }

    private void refresh() {
        CurrentOrdersFragmentSrlCommentsAndRatingRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onEndLess.current_page = 1;
                onEndLess.previousTotal = 0;
                onEndLess.previous_page = 1;

                max = 0;

                commentsData = new ArrayList<>();
                commentsAdapter = new CommentsAdapter(getActivity(), getActivity(), commentsData);
                CommentsAndRatingFragmentRv.setAdapter(commentsAdapter);

                SflShimmerFrameLayout.startShimmer();
                SflShimmerFrameLayout.setVisibility(View.VISIBLE);
                CommentsAndRatingFragmentTvNoResults.setVisibility(View.GONE);
                getList(1);


            }
        });
    }

    private void getList(final int page) {
        if (InternetState.isConnected(getActivity())) {
            apiService.getComments(page, restaurant_id).enqueue(new Callback<Comments>() {
                @Override
                public void onResponse(Call<Comments> call, Response<Comments> response) {
                    try {
                        dismissProgressDialog();
                        SflShimmerFrameLayout.stopShimmer();
                        SflShimmerFrameLayout.setVisibility(View.GONE);
                        CurrentOrdersFragmentSrlCommentsAndRatingRefresh.setRefreshing(false);

                        if (response.body().getStatus() == 1) {
                            if (page == 1) {
                                if (response.body().getData().getTotal() > 0) {
                                    CommentsAndRatingFragmentTvNoResults.setVisibility(View.GONE);
                                } else {
                                    CommentsAndRatingFragmentTvNoResults.setVisibility(View.VISIBLE);
                                    CommentsAndRatingFragmentTvNoResults.setText(R.string.no_comments);
                                }

                                onEndLess.current_page = 1;
                                onEndLess.previousTotal = 0;
                                onEndLess.previous_page = 1;

                                max = 0;
                                commentsData = new ArrayList<>();
                                commentsAdapter = new CommentsAdapter(getActivity(), getActivity(), commentsData);
                                CommentsAndRatingFragmentRv.setAdapter(commentsAdapter);


                            }
                            max = response.body().getData().getLastPage();
                            commentsData.addAll(response.body().getData().getData());
                            commentsAdapter.notifyDataSetChanged();


                        } else {
                            CommentsAndRatingFragmentTvNoResults.setVisibility(View.VISIBLE);
                            showToastMessage(getActivity(), response.body().getMsg());

                        }
                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<Comments> call, Throwable t) {
                    SflShimmerFrameLayout.stopShimmer();
                    SflShimmerFrameLayout.setVisibility(View.GONE);
                    showToastMessage(getActivity(), getString(R.string.error));
                    CurrentOrdersFragmentSrlCommentsAndRatingRefresh.setRefreshing(false);
                }
            });


        } else {
            SflShimmerFrameLayout.stopShimmer();
            SflShimmerFrameLayout.setVisibility(View.GONE);
            showToastMessage(getActivity(), getString(R.string.no_internet));
            CurrentOrdersFragmentSrlCommentsAndRatingRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.CommentsAndRating_Fragment_Btn_Add_Comment)
    public void onViewClicked() {
        if (getClientName(getActivity()).equals(CLIENTREMEMBER)) {
            AddCommentDialog addCommentDialog = new AddCommentDialog(getActivity(), restaurant_id, client.getApiToken());
            addCommentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            addCommentDialog.setCanceledOnTouchOutside(true);
            addCommentDialog.show();
        } else {
            showToastMessage(getActivity(), getString(R.string.login_please));

        }

    }
}
