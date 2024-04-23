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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.deletproducts.DeletProducts;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.CustomDialog;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.products.Update_Product_Fragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private final ApiServices apiService;
    private RestaurantData user;
    private Context context;
    private Activity activity;
    private List<ProductsData> productsData = new ArrayList<>();
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private CustomDialog dailog;

    public ProductsAdapter(Context context, Activity activity, List<ProductsData> productsData) {
        this.context = context;
        this.activity = activity;
        this.productsData = productsData;
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(activity);
        viewBinderHelper.setOpenOnlyOne(true);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_products,
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
        viewBinderHelper.bind(holder.ActivityItemPsIvActivitySwipeLayout, String.valueOf(productsData.get(position).getId()));
        holder.SwipeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation_Activity restaurantNavigationActivity = (Navigation_Activity) activity;
                Update_Product_Fragment updateProduct = new Update_Product_Fragment();
                updateProduct.productsData = productsData.get(position);
                updateProduct.item_id = productsData.get(position).getId();
                updateProduct.category_id = productsData.get(position).getCategoryId();
                HelperClass.replaceFragment(restaurantNavigationActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, updateProduct);

            }
        });

        holder.SwipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View.OnClickListener ok = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showProgressDialog(activity, activity.getResources().getString(R.string.waiit));

                        apiService.deletProduct(productsData.get(position).getId(), user.getApiToken()).enqueue(new Callback<DeletProducts>() {
                            @Override
                            public void onResponse(Call<DeletProducts> call, Response<DeletProducts> response) {
                                try {
                                    dismissProgressDialog();
                                    if (response.body().getStatus() == 1) {
                                        productsData.remove(position);
                                        notifyDataSetChanged();
                                        showSuccessToastMessage(context, response.body().getMsg());
                                        dailog.dismiss();

                                    } else {
                                        showToastMessage(context, response.body().getMsg());

                                    }

                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(Call<DeletProducts> call, Throwable t) {
                                showToastMessage(activity, activity.getResources().getString(R.string.error));

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

    @SuppressLint("SetTextI18n")
    private void setData(ViewHolder holder, int position) {
        onLoadImageFromUrl(holder.RestaurantProductsItemIvProductPhoto, productsData.get(position).getPhotoUrl(), context);
        holder.RestaurantProductsItemTvProductName.setText(productsData.get(position).getName());
        holder.RestaurantProductsItemTvProductDescription.setText(productsData.get(position).getDescription());
        holder.RestaurantProductsItemTvPrice.setText(productsData.get(position).getPrice() + "ريال ");
    }

    private void setAction(ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return productsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Restaurant_Products_Item_Iv_ProductPhoto)
        PorterShapeImageView RestaurantProductsItemIvProductPhoto;
        @BindView(R.id.Restaurant_Products_Item_Tv_Price)
        TextView RestaurantProductsItemTvPrice;
        @BindView(R.id.lin)
        LinearLayout lin;
        @BindView(R.id.Restaurant_Products_Item_Tv_ProductName)
        TextView RestaurantProductsItemTvProductName;
        @BindView(R.id.Restaurant_Products_Item_Tv_ProductDescription)
        TextView RestaurantProductsItemTvProductDescription;
        @BindView(R.id.Swipe_Edit)
        ImageView SwipeEdit;
        @BindView(R.id.Swipe_Delete)
        ImageView SwipeDelete;
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
