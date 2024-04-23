package com.ashraf.amr.apps.sofra.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomDao;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;

public class CarItemsAdapter extends RecyclerView.Adapter<CarItemsAdapter.ViewHolder> {

    private final RoomDao roomDao;
    private final RoomManger roomManger;

    private Context context;
    private Activity activity;
    private List<ProductsData> foodItems = new ArrayList<>();
    private TextView carShopFragmentTvTotal;
    Double Total = 0.0;

    public CarItemsAdapter(Context context, Activity activity, List<ProductsData> foodItems, TextView carShopFragmentTvTotal) {
        this.context = context;
        this.activity = activity;
        this.foodItems = foodItems;
        this.carShopFragmentTvTotal = carShopFragmentTvTotal;
        roomManger = RoomManger.getInstance(activity);
        roomDao = roomManger.roomDao();
        for (int i = 0; i < foodItems.size(); i++) {
            Total = Total + (Double.parseDouble(foodItems.get(i).getPrice()) * Double.parseDouble(foodItems.get(i).getCounter()));
        }
        carShopFragmentTvTotal.setText(String.valueOf(Total));

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_items,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        onLoadImageFromUrl(holder.CartItemsItemIvPhoto, foodItems.get(position).getPhotoUrl(), context);
        holder.CartItemsItemTvName.setText(foodItems.get(position).getName());
        holder.CartItemsItemTvPrice.setText(foodItems.get(position).getPrice());
        int y = Integer.parseInt(foodItems.get(position).getCounter());
        holder.CartItemsItemTVQuantityShow.setText(String.valueOf(y));

    }

    private void setAction(final ViewHolder holder, final int position) {
        holder.CartItemsItemBtnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCount(holder, position);
            }
        });

        holder.CartItemsItemBtnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeToCount(holder, position);
            }
        });

        holder.CartItemsItemTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {

                        roomDao.deleteItemToCar(foodItems.get(position));

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                foodItems.remove(position);
                                notifyDataSetChanged();
                                for (int i = 0; i < foodItems.size(); i++) {
                                    Total = Total + (Double.parseDouble(foodItems.get(i).getPrice()) * Double.parseDouble(foodItems.get(i).getCounter()));
                                }
                            }
                        });

                    }

                });
            }
        });
    }


    private void addToCount(ViewHolder holder, int position) {
        try {
            Integer count = 0;
            if (!holder.CartItemsItemTVQuantityShow.getText().toString().equals("")) {
                count = Integer.valueOf(holder.CartItemsItemTVQuantityShow.getText().toString());
            }

            count = count + 1;
            foodItems.get(position).setCounter(String.valueOf(count));
            holder.CartItemsItemTVQuantityShow.setText(String.valueOf(count));

            Total = Total + Double.parseDouble(foodItems.get(position).getPrice());
            carShopFragmentTvTotal.setText(String.valueOf(Total));

            update(foodItems.get(position));
        } catch (Exception e) {

        }

    }

    private void removeToCount(ViewHolder holder, int position) {
        try {
            Integer count = 0;
            if (!holder.CartItemsItemTVQuantityShow.getText().toString().equals("")) {
                count = Integer.valueOf(holder.CartItemsItemTVQuantityShow.getText().toString());

            }
            if (!count.equals(0)) {
                count = count - 1;
                foodItems.get(position).setCounter(String.valueOf(count));
                holder.CartItemsItemTVQuantityShow.setText(String.valueOf(count));
                Total = Total - Double.parseDouble(foodItems.get(position).getPrice());
                carShopFragmentTvTotal.setText(String.valueOf(Total));


                update(foodItems.get(position));
            }

        } catch (Exception e) {

        }

    }

    private void update(final ProductsData productsData) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                roomDao.updateItemToCar(productsData);

            }

        });

    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Cart_Items_Item_Iv_Photo)
        PorterShapeImageView CartItemsItemIvPhoto;
        @BindView(R.id.Cart_Items_Item_Tv_Name)
        TextView CartItemsItemTvName;
        @BindView(R.id.Cart_Items_Item_Tv_Price)
        TextView CartItemsItemTvPrice;
        @BindView(R.id.Cart_Items_Item_Btn_Increase)
        Button CartItemsItemBtnIncrease;
        @BindView(R.id.Cart_Items_Item_tV_Quantity_Show)
        TextView CartItemsItemTVQuantityShow;
        @BindView(R.id.Cart_Items_Item_Btn_Decrease)
        Button CartItemsItemBtnDecrease;
        @BindView(R.id.Cart_Items_Item_Tv_Delete)
        TextView CartItemsItemTvDelete;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
