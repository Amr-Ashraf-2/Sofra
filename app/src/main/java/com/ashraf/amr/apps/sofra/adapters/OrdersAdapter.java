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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.client.listOfOrders.OrdersData;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle.My_Orders_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.applications.OrderDetailsFragment;

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

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {


    private My_Orders_Fragment myOrdersFragment;
    private String state;
    private Client client;
    private ApiServices apiService;
    private Context context;
    private Activity activity;
    private List<OrdersData> ordersDataList = new ArrayList<>();

    public OrdersAdapter(My_Orders_Fragment myOrdersFragment, String state, Context context, Activity activity, List<OrdersData> ordersDataList) {
        this.myOrdersFragment = myOrdersFragment;
        this.state = state;
        this.context = context;
        this.activity = activity;
        this.ordersDataList = ordersDataList;
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        client = SharedPreferencesManger.loadClientData(activity);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_current_oeders,
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
        try {
            onLoadImageFromUrl(holder.ItemCurrentOrdersIvPhoto, ordersDataList.get(position).getRestaurant().getPhotoUrl(), context);
            holder.ItemCurrentOrdersTvMealName.setText(ordersDataList.get(position).getRestaurant().getName());
            holder.ItemCurrentOrdersTvOrderNumber.setText("رقم الطلب : " + String.valueOf(ordersDataList.get(position).getId()));
            holder.ItemCurrentOrdersTvMealPrice.setText("الإجمالى : " + ordersDataList.get(position).getTotal());
            holder.ItemCurrentOrdersTvAddress.setText("العنوان : " + ordersDataList.get(position).getAddress());
            if (state.equals("pending")) {

            }

            if (state.equals("current")) {
                holder.ItemCurrentOrdersRelState.setVisibility(View.GONE);
                holder.ItemCurrentOrdersRelRecieve.setVisibility(View.VISIBLE);
            }
            if (state.equals("completed")) {
                holder.ItemCurrentOrdersRelState.setVisibility(View.GONE);
                holder.ItemCurrentOrdersRelRecieve.setVisibility(View.GONE);
                if (ordersDataList.get(position).getState().equals("delivered")) {
                    holder.ApplicationsItemTvOrderDone.setVisibility(View.VISIBLE);
                    holder.ApplicationsItemTvOrderRejected.setVisibility(View.GONE);
                } else if (ordersDataList.get(position).getState().equals("rejected") ||
                        ordersDataList.get(position).getState().equals("declined")) {
                    holder.ApplicationsItemTvOrderRejected.setVisibility(View.VISIBLE);
                    holder.ApplicationsItemTvOrderDone.setVisibility(View.GONE);

                }
            }
        } catch (Exception e) {

        }

    }

    private void setAction(final ViewHolder holder, final int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation_Activity restaurantNavigationActivity = (Navigation_Activity) activity;
                OrderDetailsFragment orderDetails = new OrderDetailsFragment();
                orderDetails.ordersDataList = ordersDataList.get(position);
                orderDetails.type = "client";
                HelperClass.replaceFragment(restaurantNavigationActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, orderDetails);


            }
        });
        holder.ItemCurrentOrdersRelRecieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    showProgressDialog(activity, activity.getResources().getString(R.string.waiit));
                    apiService.onConfirmOrder(client.getApiToken(), ordersDataList.get(position).getId()).enqueue(new Callback<GeneralResponse>() {

                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            dismissProgressDialog();
                            try {
                                if (response.body().getStatus() == 1) {
                                    showSuccessToastMessage(activity, response.body().getMsg());
//                                    myOrdersFragment.currentOrders.ordersDataList.remove(position);
//                                    myOrdersFragment.currentOrders.ordersAdapter.notifyDataSetChanged();
                                    myOrdersFragment.oldOrders.ordersDataList.add(0, ordersDataList.get(position));
                                    myOrdersFragment.oldOrders.ordersAdapter.notifyDataSetChanged();
                                    ordersDataList.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    showToastMessage(activity, response.body().getMsg());

                                }

                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            dismissProgressDialog();
                            try {

                            } catch (Exception e) {

                            }
                        }
                    });


                } catch (Exception e) {

                }


            }
        });

        holder.ItemCurrentOrdersRelState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    showProgressDialog(activity, activity.getResources().getString(R.string.waiit));
                    apiService.onDeclineOrder(client.getApiToken(), ordersDataList.get(position).getId()).enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            dismissProgressDialog();
                            try {

                                if (response.body().getStatus() == 1) {
                                    showSuccessToastMessage(activity, response.body().getMsg());
                                    ordersDataList.get(position).setState("rejected");
//                                    myOrdersFragment.newOrders.ordersDataList.remove(position);
//                                    myOrdersFragment.newOrders.ordersAdapter.notifyDataSetChanged();
                                    myOrdersFragment.oldOrders.ordersDataList.add(0, ordersDataList.get(position));
                                    myOrdersFragment.oldOrders.ordersAdapter.notifyDataSetChanged();
                                    ordersDataList.remove(position);
                                    notifyDataSetChanged();

                                }else {
                                    showToastMessage(activity, response.body().getMsg());

                                }


                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            dismissProgressDialog();
                            try {

                            } catch (Exception e) {

                            }
                        }
                    });


                } catch (Exception e) {

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.Item_Current_Orders_Iv_Photo)
        CircleImageView ItemCurrentOrdersIvPhoto;
        @BindView(R.id.Item_Current_Orders_Tv_MealName)
        TextView ItemCurrentOrdersTvMealName;
        @BindView(R.id.Item_Current_Orders_Tv_OrderNumber)
        TextView ItemCurrentOrdersTvOrderNumber;
        @BindView(R.id.Item_Current_Orders_Tv_MealPrice)
        TextView ItemCurrentOrdersTvMealPrice;
        @BindView(R.id.Item_Current_Orders_Tv_Address)
        TextView ItemCurrentOrdersTvAddress;
        @BindView(R.id.Linear_Tvs)
        LinearLayout LinearTvs;
        @BindView(R.id.Item_Current_Orders_Iv_False)
        ImageView ItemCurrentOrdersIvFalse;
        @BindView(R.id.Item_Current_Orders_Tv_Reject)
        TextView ItemCurrentOrdersTvReject;
        @BindView(R.id.Item_Current_Orders_Rel_State)
        RelativeLayout ItemCurrentOrdersRelState;
        @BindView(R.id.Item_Current_Orders_Tv_Recieve)
        TextView ItemCurrentOrdersTvRecieve;
        @BindView(R.id.Item_Current_Orders_Rel_Recieve)
        RelativeLayout ItemCurrentOrdersRelRecieve;
        @BindView(R.id.Applications_Item_Tv_OrderDone)
        TextView ApplicationsItemTvOrderDone;
        @BindView(R.id.Applications_Item_Tv_OrderRejected)
        TextView ApplicationsItemTvOrderRejected;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
