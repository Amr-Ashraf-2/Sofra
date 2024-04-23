package com.ashraf.amr.apps.sofra.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.fragments.client.buy_food.Food_Item_Details_Fragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {


    private Context context;
    private Activity activity;
    private List<ProductsData> myProductsData = new ArrayList<>();

    public FoodListAdapter(Context context, Activity activity, List<ProductsData> myProductsData) {
        this.context = context;
        this.activity = activity;
        this.myProductsData = myProductsData;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_foodlist,
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
        onLoadImageFromUrl(holder.ItemRestaurantFoodListIvProductPhoto, myProductsData.get(position).getPhotoUrl(), context);
        holder.ItemRestaurantFoodListTvName.setText(myProductsData.get(position).getName());
        holder.ItemRestaurantFoodListTvDescription.setText(myProductsData.get(position).getDescription());

        float priceAsFloat = Float.parseFloat(myProductsData.get(position).getPrice());
        int priceAsInteger = (int) priceAsFloat;
        String getPrice = String.valueOf(priceAsInteger);
        holder.ItemRestaurantFoodListTvPrice.setText(activity.getString(R.string.food_price, getPrice));

        if (myProductsData.get(position).isHasOffer()) {

            //holder.ItemRestaurantFoodListTvPrice.setText(activity.getString(R.string.food_price, getPrice));
            holder.ItemRestaurantFoodListIvOfferPrice.setVisibility(View.VISIBLE);
            holder.ItemRestaurantFoodListTvOfferPrice.setVisibility(View.VISIBLE);
            holder.ItemRestaurantFoodListTvSpecialOffer.setVisibility(View.VISIBLE);

            float price3AsFloat = Float.parseFloat(myProductsData.get(position).getOfferPrice());
            int price3AsInteger = (int) price3AsFloat;
            String getPrice3 = String.valueOf(price3AsInteger);
            holder.ItemRestaurantFoodListTvOfferPrice.setText(activity.getString(R.string.food_price, getPrice3));
        } else {
            holder.ItemRestaurantFoodListIvOfferPrice.setVisibility(View.GONE);
            holder.ItemRestaurantFoodListTvOfferPrice.setVisibility(View.GONE);
        }
    }

    private void setAction(ViewHolder holder, final int position) {

        if (LoadData(activity, USER_TYPE).equals(CLIENT)) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation_Activity navigationActivity = (Navigation_Activity) activity;
                    Food_Item_Details_Fragment foodItemDetails = new Food_Item_Details_Fragment();
                    foodItemDetails.foodItem = myProductsData.get(position);
                    replaceFragment(navigationActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, foodItemDetails);

                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return myProductsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Item_Restaurant_FoodList_Iv_ProductPhoto)
        PorterShapeImageView ItemRestaurantFoodListIvProductPhoto;
        @BindView(R.id.Item_Restaurant_FoodList_Tv_SpecialOrder)
        TextView ItemRestaurantFoodListTvSpecialOrder;
        @BindView(R.id.Item_Restaurant_FoodList_Tv_Name)
        TextView ItemRestaurantFoodListTvName;
        @BindView(R.id.Item_Restaurant_FoodList_Tv_Description)
        TextView ItemRestaurantFoodListTvDescription;
        @BindView(R.id.Item_Restaurant_FoodList_Tv_PriceWord)
        TextView ItemRestaurantFoodListTvPriceWord;
        @BindView(R.id.Item_Restaurant_FoodList_Tv_Price)
        TextView ItemRestaurantFoodListTvPrice;
        @BindView(R.id.Item_Restaurant_FoodList_Iv_OfferPrice)
        ImageView ItemRestaurantFoodListIvOfferPrice;
        @BindView(R.id.Item_Restaurant_FoodList_Rl_Price)
        RelativeLayout ItemRestaurantFoodListRlPrice;
        @BindView(R.id.Item_Restaurant_FoodList_Tv_OfferPrice)
        TextView ItemRestaurantFoodListTvOfferPrice;
        @BindView(R.id.Item_Restaurant_FoodList_Iv_Favourite)
        ImageView ItemRestaurantFoodListIvFavourite;
        @BindView(R.id.Item_Restaurant_FoodList_Tv_SpecialOffer)
        TextView ItemRestaurantFoodListTvSpecialOffer;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
