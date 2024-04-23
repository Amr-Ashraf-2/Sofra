package com.ashraf.amr.apps.sofra.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.ViewHolder> {
    private Context context;
    private Activity activity;
    private List<ProductsData> productsDataList = new ArrayList<>();

    public OrderProductsAdapter(Context context, Activity activity, List<ProductsData> productsDataList) {
        this.context = context;
        this.activity = activity;
        this.productsDataList = productsDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_details,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.ItemOrderDetailsTvName.setText(productsDataList.get(position).getName());
        holder.ItemOrderDetailsTvPrice.setText(productsDataList.get(position).getPrice());

    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return productsDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Item_Order_Details_Tv_Name)
        TextView ItemOrderDetailsTvName;
        @BindView(R.id.Item_Order_Details_Tv_Price)
        TextView ItemOrderDetailsTvPrice;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
