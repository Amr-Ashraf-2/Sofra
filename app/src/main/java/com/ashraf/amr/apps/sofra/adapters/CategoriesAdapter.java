package com.ashraf.amr.apps.sofra.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.model.general.categorieslist.CategoriesListData;
import com.ashraf.amr.apps.sofra.ui.fragments.client.restaurantDetails.Restaurant_FoodList_Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrlCircle;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {


    private Context context;
    private Activity activity;
    private List<CategoriesListData> categoriesListDataList = new ArrayList<>();
    private Restaurant_FoodList_Fragment restaurantFoodListFragment;
    private int relative;

    public CategoriesAdapter(Context context, Activity activity, List<CategoriesListData> categoriesListDataList, Restaurant_FoodList_Fragment restaurantFoodListFragment) {
        this.context = context;
        this.activity = activity;
        this.categoriesListDataList = categoriesListDataList;
        this.restaurantFoodListFragment = restaurantFoodListFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categories,
                parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setAction(holder, position);
        setData(holder, position);
    }

    private void setData(ViewHolder holder, final int position) {
        onLoadImageFromUrlCircle(holder.ItemCategoriesIvPhoto, categoriesListDataList.get(position).getPhotoUrl(), context, 0);
        holder.ItemCategoriesTvName.setText(categoriesListDataList.get(position).getName());

    }

    private void setAction(final ViewHolder holder, final int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relative = position;


                restaurantFoodListFragment.category_id = categoriesListDataList.get(position).getId();
                restaurantFoodListFragment.getProductsList(1);
                notifyDataSetChanged();

            }
        });
        if (relative == position) {
            holder.relative.setVisibility(View.VISIBLE);

        }else {
            holder.relative.setVisibility(View.GONE);

        }
    }


    @Override
    public int getItemCount() {
        return categoriesListDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Item_Categories_Iv_Photo)
        CircleImageView ItemCategoriesIvPhoto;
        @BindView(R.id.Item_Categories_Tv_Name)
        TextView ItemCategoriesTvName;
        @BindView(R.id.relative)
        RelativeLayout relative;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
