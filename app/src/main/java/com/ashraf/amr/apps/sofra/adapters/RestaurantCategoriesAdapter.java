package com.ashraf.amr.apps.sofra.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantcategories.CategoriesData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.CustomDialog;
import com.ashraf.amr.apps.sofra.helper.EditCategoryDialog;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.categories.CategoriesFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.products.ProductsFragment;
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
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

public class RestaurantCategoriesAdapter extends RecyclerView.Adapter<RestaurantCategoriesAdapter.ViewHolder> {

    private final ApiServices apiService;

    private RestaurantData user;
    private Context context;
    private Activity activity;
    private List<CategoriesData> categoriesData = new ArrayList<>();
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private CustomDialog dailog;
    public CategoriesFragment categoriesFragment;

    public RestaurantCategoriesAdapter(Context context, Activity activity,
                                       List<CategoriesData> categoriesData,
                                       CategoriesFragment categoriesFragment) {
        this.context = context;
        this.activity = activity;
        this.categoriesData = categoriesData;
        this.categoriesFragment = categoriesFragment;
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        viewBinderHelper.setOpenOnlyOne(true);
        user = SharedPreferencesManger.loadRestaurantData(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_categories,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setSwiping(holder, position);
        setData(holder, position);
        setAction(holder, position);
    }

    @SuppressLint("SetTextI18n")
    private void setData(ViewHolder holder, int position) {
        onLoadImageFromUrl(holder.RestaurantCategoriesItemIvCategoryPhoto, categoriesData.get(position).getPhotoUrl(), context);
        holder.RestaurantCategoriesItemTvCategoryName.setText(categoriesData.get(position).getName());
    }

    private void setAction(ViewHolder holder, final int position) {
        holder.RestaurantCategoriesItemRlCategorySubView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation_Activity navigationActivity = (Navigation_Activity) activity;
                ProductsFragment productsFragment = new ProductsFragment();
                productsFragment.category_id = categoriesData.get(position).getId();
                productsFragment.name = categoriesData.get(position).getName();
                replaceFragment(navigationActivity.getSupportFragmentManager(),
                        R.id.Home_Cycle_Activity_FrameLayout, productsFragment);
            }
        });
    }

    private void setSwiping(ViewHolder holder, final int position) {
        holder.ActivityItemPsIvActivitySwipeLayout.setDragEdge(SwipeRevealLayout.DRAG_EDGE_LEFT);
        viewBinderHelper.bind(holder.ActivityItemPsIvActivitySwipeLayout, String.valueOf(categoriesData.get(position).getId()));

        holder.SwipeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCategoryDialog editCategoryDialog = new EditCategoryDialog(activity);
                editCategoryDialog.categoriesData = categoriesData.get(position);
                editCategoryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editCategoryDialog.setCanceledOnTouchOutside(true);
                editCategoryDialog.show();
            }
        });

        holder.SwipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View.OnClickListener ok = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showProgressDialog(activity, activity.getString(R.string.waiit));
                        apiService.deleteCategory(categoriesData.get(position).getId(), user.getApiToken()).enqueue(new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                try {
                                    dismissProgressDialog();
                                    if (response.body().getStatus() == 1) {
                                        categoriesData.remove(position);
                                        categoriesFragment.restaurantCategoriesAdapter.notifyDataSetChanged();
                                        if (categoriesFragment.categoriesData.size() > 0) {
                                            categoriesFragment.RestaurantCategoriesFragmentTvNoResults.setVisibility(View.GONE);
                                        } else {
                                            categoriesFragment.RestaurantCategoriesFragmentTvNoResults.setVisibility(View.VISIBLE);

                                        }
                                        showSuccessToastMessage(context, response.body().getMsg());
                                        dailog.dismiss();

                                    } else {
                                        showToastMessage(context, response.body().getMsg());

                                    }

                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(Call<GeneralResponse> call, Throwable t) {
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

    @Override
    public int getItemCount() {
        return categoriesData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Swipe_Edit)
        ImageView SwipeEdit;
        @BindView(R.id.Swipe_Delete)
        ImageView SwipeDelete;
        @BindView(R.id.Restaurant_Categories_Item_Iv_CategoryPhoto)
        PorterShapeImageView RestaurantCategoriesItemIvCategoryPhoto;
        @BindView(R.id.Restaurant_Categories_Item_Tv_CategoryName)
        TextView RestaurantCategoriesItemTvCategoryName;
        @BindView(R.id.Activity_Item_PsIv_Activity_Swipe_Layout)
        SwipeRevealLayout ActivityItemPsIvActivitySwipeLayout;
        @BindView(R.id.Restaurant_Categories_Item_Rl_Category_SubView)
        RelativeLayout RestaurantCategoriesItemRlCategorySubView;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
