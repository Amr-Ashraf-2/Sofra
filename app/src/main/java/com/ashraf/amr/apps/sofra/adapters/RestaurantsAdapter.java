package com.ashraf.amr.apps.sofra.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.general.restaurantslist.RestaurantsListData;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.fragments.client.restaurantDetails.Restaurant_Details_Fragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.MAIN;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.WHERE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrlCircle;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;


public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<RestaurantsListData> restaurantDataList = new ArrayList<>();
    private String opened = "open";
    private String closed = "closed";

    public RestaurantsAdapter(Context context, Activity activity, List<RestaurantsListData> restaurantDataList) {
        this.context = context;
        this.activity = activity;
        this.restaurantDataList = restaurantDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurants_list,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    @SuppressLint("SetTextI18n")
    private void setData(ViewHolder holder, int position) {

        if(restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/uploads/restaurants/156397554624016.jpg") //1
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/tmp/phpwEJwoN") //16
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/tmp/php4ukpMA") //17
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/tmp/phpiFE7dV") //19
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/tmp/phpxyLv4w") //22
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/tmp/phpnuzXRp") //23
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/tmp/phpiBYzHl") //29
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/tmp/phpBVpvyY") //30
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/tmp/php8HtEWK") //31
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/tmp/php4QAgAo") //49
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/uploads/restaurants/157322570572919.svg") //83
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/uploads/restaurants/157711200317875.jpg") //119 //b
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/uploads/restaurants/157882798651853.jpg") //132 //b
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/uploads/restaurants/158084713437329.jpg") //146 //b
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/uploads/restaurants/159904802092010.png") //232
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/uploads/restaurants/159908143869155.jpg") //234
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/uploads/restaurants/159913215054486.jpg") //235
            || restaurantDataList.get(position).getPhotoUrl().equalsIgnoreCase("https://ipda3-tech.com/sofra-v2/uploads/restaurants/159913357524060.jpg") //236
        ){
            Glide.with(context)
                   .load(R.drawable.kfc)
                   .into(holder.ItemRestaurantsListIvPhoto);
        }else {
            onLoadImageFromUrlCircle(holder.ItemRestaurantsListIvPhoto, restaurantDataList.get(position).getPhotoUrl(), context, 0);
        }

        float minimumOrderAsFloat = Float.parseFloat(restaurantDataList.get(position).getMinimumCharger());
        float deliveryFeeAsFloat = Float.parseFloat(restaurantDataList.get(position).getDeliveryCost());
        int minimumOrderAsInteger = (int) minimumOrderAsFloat;
        int deliveryFeeAsInteger = (int) deliveryFeeAsFloat;
        String getMinimumOrder = String.valueOf(minimumOrderAsInteger);
        String getDeliveryFee = String.valueOf(deliveryFeeAsInteger);

        holder.ItemRestaurantsListTvName.setText(restaurantDataList.get(position).getName());
        holder.ItemRestaurantsListTvMinimum.setText(activity.getString(R.string.minimum_charge_1, getMinimumOrder));
        holder.ItemRestaurantsListTvDeliveryCost.setText(activity.getString(R.string.delivery_fee_1, getDeliveryFee));
        holder.ItemRestaurantsListTvState.setText(restaurantDataList.get(position).getAvailability());
        holder.itemRestaurantsListRating.setRating(Float.parseFloat(restaurantDataList.get(position).getRate()));
        if (restaurantDataList.get(position).getAvailability().equals(opened)) {
                                                                //ic_circle_solid
            holder.ItemRestaurantsListIvState.setImageResource(R.drawable.ic_opened_icon);
        } else {
                                                                //ic_circle_solid_red
            holder.ItemRestaurantsListIvState.setImageResource(R.drawable.ic_closed_icon);

        }
    }

    private void setAction(ViewHolder holder, final int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManger.SaveData(activity, WHERE , MAIN);

                Navigation_Activity clientNavigation = (Navigation_Activity) activity;
                Restaurant_Details_Fragment restaurantDetails = new Restaurant_Details_Fragment();
                restaurantDetails.restaurantsListData = restaurantDataList.get(position);
                replaceFragment(clientNavigation.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, restaurantDetails);

            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_restaurants_list_Rating)
        RatingBar itemRestaurantsListRating;
        @BindView(R.id.Item_Restaurants_List_Iv_Photo)
        CircleImageView ItemRestaurantsListIvPhoto;
        @BindView(R.id.relatt)
        RelativeLayout relatt;
        @BindView(R.id.Item_Restaurants_List_Tv_Name)
        TextView ItemRestaurantsListTvName;
        @BindView(R.id.Item_Restaurants_List_Tv_Minimum)
        TextView ItemRestaurantsListTvMinimum;
        @BindView(R.id.Item_Restaurants_List_Tv_Delivery_Cost)
        TextView ItemRestaurantsListTvDeliveryCost;
        @BindView(R.id.Item_Restaurants_List_Iv_State)
        ImageView ItemRestaurantsListIvState;
        @BindView(R.id.Item_Restaurants_List_Tv_State)
        TextView ItemRestaurantsListTvState;
        @BindView(R.id.Item_Restaurants_List_Rl)
        RelativeLayout ItemRestaurantsListRl;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
