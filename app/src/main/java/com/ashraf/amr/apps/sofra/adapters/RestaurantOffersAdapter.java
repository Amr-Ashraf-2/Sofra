package com.ashraf.amr.apps.sofra.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.deletoffer.DeletOffer;
import com.ashraf.amr.apps.sofra.data.model.restaurant.offerlist.OffersListData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.CustomDialog;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.offers.RestaurantOffersFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.offers.UpdateOfferFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

public class RestaurantOffersAdapter extends RecyclerView.Adapter<RestaurantOffersAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private RestaurantData user;
    private List<OffersListData> offersDataList = new ArrayList<>();
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ApiServices apiService;
    private CustomDialog dailog;
    private RestaurantOffersFragment restaurantOffersFragment;

    public RestaurantOffersAdapter(Context context, Activity activity,
                                   List<OffersListData> offersDataList, RestaurantOffersFragment restaurantOffersFragment) {
        this.context = context;
        this.activity = activity;
        this.offersDataList = offersDataList;
        this.restaurantOffersFragment = restaurantOffersFragment;

        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(activity);
        viewBinderHelper.setOpenOnlyOne(true);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_offers,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setSwiping(holder, position);
        setData(holder, position);
        setAction(holder, position);
    }

    private void setSwiping(ViewHolder holder, final int position) {
        holder.ActivityItemPsIvActivitySwipeLayout.setDragEdge(SwipeRevealLayout.DRAG_EDGE_LEFT);

        viewBinderHelper.bind(holder.ActivityItemPsIvActivitySwipeLayout, String.valueOf(offersDataList.get(position).getId()));

        holder.SwipeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation_Activity restaurantNavigationActivity = (Navigation_Activity) activity;
                UpdateOfferFragment updateOffer = new UpdateOfferFragment();
                updateOffer.offersDataList = offersDataList.get(position);
                updateOffer.offer_id = offersDataList.get(position).getId();
                HelperClass.replaceFragment(restaurantNavigationActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, updateOffer);


            }
        });


        holder.SwipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View.OnClickListener ok = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showProgressDialog(activity, activity.getString(R.string.waiit));
                        apiService.deletOffer(offersDataList.get(position).getId(),
                                user.getApiToken()).enqueue(new Callback<DeletOffer>() {

                            @Override
                            public void onResponse(Call<DeletOffer> call, Response<DeletOffer> response) {
                                try {
                                    dismissProgressDialog();
                                    if (response.body().getStatus() == 1) {
                                        offersDataList.remove(position);
                                        restaurantOffersFragment.restaurantOffersAdapter.notifyDataSetChanged();
                                        if (restaurantOffersFragment.offersDataList.size() > 0) {
                                            restaurantOffersFragment.TvNoResult.setVisibility(View.GONE);
                                        } else {
                                            restaurantOffersFragment.TvNoResult.setVisibility(View.VISIBLE);

                                        }
                                        dailog.dismiss();
                                        showSuccessToastMessage(context, response.body().getMsg());
                                    } else {
                                        showToastMessage(context, response.body().getMsg());

                                    }

                                } catch (Exception e) {

                                }

                            }

                            @Override
                            public void onFailure(Call<DeletOffer> call, Throwable t) {
                                showToastMessage(activity, activity.getResources().getString(R.string.error));
                                dismissProgressDialog();

                            }
                        });


                    }
                };
                View.OnClickListener cancel = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dailog.dismiss();
                    }
                };

                dailog = new CustomDialog(activity, activity, activity.getString(R.string.want_delet), ok, cancel, true, true);

            }
        });

    }

    private void setData(ViewHolder holder, int position) {
        holder.RestaurantOffersItemTvTitle.setText(offersDataList.get(position).getDescription());
        onLoadImageFromUrl(holder.RestaurantOffersItemIvPhoto, offersDataList.get(position).getRestaurant().getPhotoUrl(), context);

    }

    private void setAction(ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return offersDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Swipe_Edit)
        ImageView SwipeEdit;
        @BindView(R.id.Swipe_Delete)
        ImageView SwipeDelete;
        @BindView(R.id.Restaurant_Offers_Item_Tv_Title)
        TextView RestaurantOffersItemTvTitle;
        @BindView(R.id.Restaurant_Offers_Item_Iv_Photo)
        CircleImageView RestaurantOffersItemIvPhoto;
        @BindView(R.id.Activity_Item_PsIv_Activity_Swipe_Layout)
        SwipeRevealLayout ActivityItemPsIvActivitySwipeLayout;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
