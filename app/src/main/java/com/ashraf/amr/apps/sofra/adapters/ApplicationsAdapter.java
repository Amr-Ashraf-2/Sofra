package com.ashraf.amr.apps.sofra.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myrequests.MyRequestsData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.rejectrequests.RejectRequests;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.RefuseReasonDialog;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.applications.All_Applications_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.applications.OrderDetailsFragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.checkWriteExternalPermission;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onPermission;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {

    private String type;
    private Context context;
    private Activity activity;
    private List<MyRequestsData> requestsDataList = new ArrayList<>();
    private RestaurantData user;
    private ApiServices apiServices;
    private All_Applications_Fragment allApplicationsFragment;

    public ApplicationsAdapter(All_Applications_Fragment allApplicationsFragment, String type, Context context, Activity activity, List<MyRequestsData> requestsDataList) {
        this.allApplicationsFragment = allApplicationsFragment;
        this.type = type;
        this.context = context;
        this.activity = activity;
        this.requestsDataList = requestsDataList;
        apiServices = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_applications,
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
        if (type.equals("pending")) {

        }

        if (type.equals("current")) {
            holder.ApplicationsItemBtnReject.setVisibility(View.VISIBLE);
            holder.ApplicationsItemBtnAccept.setVisibility(View.GONE);
            holder.ApplicationsItemBtnConfirm.setVisibility(View.VISIBLE);
            holder.ApplicationsItemLinMobile.setVisibility(View.GONE);
        }
        if (type.equals("completed")) {
            holder.linearLayout5.setVisibility(View.GONE);
            if (requestsDataList.get(position).getState().equals("delivered")) {
                holder.ApplicationsItemTvOrderDone.setVisibility(View.VISIBLE);
                holder.ApplicationsItemTvOrderRejected.setVisibility(View.GONE);

            } else if (requestsDataList.get(position).getState().equals("rejected") ||
                    requestsDataList.get(position).getState().equals("declined")) {
                holder.ApplicationsItemTvOrderRejected.setVisibility(View.VISIBLE);
                holder.ApplicationsItemTvOrderDone.setVisibility(View.GONE);
            }
        }
        try {
            onLoadImageFromUrl(holder.ApplicationsItemIvPhoto, requestsDataList.get(position).getClient().getPhotoUrl(), context);
            holder.ApplicationsItemTvName.setText("العميل : " + requestsDataList.get(position).getClient().getName());
            holder.ApplicationsItemTvNumber.setText("رقم الطلب : " + String.valueOf(requestsDataList.get(position).getId()));
            holder.ApplicationsItemTvTotal.setText("الإجمالى : " + requestsDataList.get(position).getTotal() + "ريال");
            holder.ApplicationsItemTvAddress.setText("العنوان : " + requestsDataList.get(position).getAddress());


        } catch (Exception e) {

        }


    }

    private void setAction(ViewHolder holder, final int position) {

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation_Activity restaurantNavigationActivity = (Navigation_Activity) activity;
                OrderDetailsFragment orderDetails = new OrderDetailsFragment();
                orderDetails.restaurantOrdersDataList = requestsDataList.get(position);
                orderDetails.type = "restaurant";
                HelperClass.replaceFragment(restaurantNavigationActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, orderDetails);


            }
        });

        holder.ApplicationsItemLinMobile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (checkWriteExternalPermission(context)) {
                    onPermission(activity);
                    return;
                }

                String number = "tel:" + requestsDataList.get(position).getClient().getPhone();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                context.startActivity(callIntent);
            }
        });


        if (type.equals("pending")) {
            holder.ApplicationsItemBtnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog(activity, activity.getResources().getString(R.string.waiit));

                    apiServices.deletRequests(requestsDataList.get(position).getId(), user.getApiToken()).enqueue(new Callback<RejectRequests>() {
                        @Override
                        public void onResponse(Call<RejectRequests> call, Response<RejectRequests> response) {
                            dismissProgressDialog();
                            try {
                                if (response.body().getStatus() == 1) {
                                    showSuccessToastMessage(activity, response.body().getMsg());
                                    requestsDataList.get(position).setState("rejected");
                                    allApplicationsFragment.oldApplications.requestsDataList.add(0, requestsDataList.get(position));
                                    allApplicationsFragment.oldApplications.applicationsAdapter.notifyDataSetChanged();
                                    requestsDataList.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    showToastMessage(activity, response.body().getMsg());

                                }
                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<RejectRequests> call, Throwable t) {
                            dismissProgressDialog();
                            showToastMessage(activity, activity.getString(R.string.error));

                        }
                    });
                }
            });

        }
        if (type.equals("current")) {
            holder.ApplicationsItemBtnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RefuseReasonDialog refuseReasonDialog = new RefuseReasonDialog(activity,
                            requestsDataList.get(position).getId(), user.getApiToken()
                            , allApplicationsFragment, position);
                    refuseReasonDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    refuseReasonDialog.setCanceledOnTouchOutside(true);
                    refuseReasonDialog.show();


                }
            });
        }


        holder.ApplicationsItemBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(activity, activity.getResources().getString(R.string.waiit));

                apiServices.acceptPrequest(requestsDataList.get(position).getId(), user.getApiToken()).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        try {
                            dismissProgressDialog();
                            if (response.body().getStatus() == 1) {
                                showSuccessToastMessage(activity, response.body().getMsg());
                                allApplicationsFragment.nowApplications.requestsDataList.add(0, requestsDataList.get(position));
                                allApplicationsFragment.nowApplications.applicationsAdapter.notifyDataSetChanged();
                                requestsDataList.remove(position);
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
                        showToastMessage(activity, activity.getString(R.string.error));

                    }
                });

            }
        });

        holder.ApplicationsItemBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(activity, activity.getResources().getString(R.string.waiit));

                apiServices.confirmrequest(requestsDataList.get(position).getId(), user.getApiToken()).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        try {
                            dismissProgressDialog();
                            if (response.body().getStatus() == 1) {
                                showSuccessToastMessage(activity, response.body().getMsg());
                                requestsDataList.get(position).setState("delivered");
                                allApplicationsFragment.oldApplications.requestsDataList.add(0, requestsDataList.get(position));
                                allApplicationsFragment.oldApplications.applicationsAdapter.notifyDataSetChanged();
                                requestsDataList.remove(position);
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
                        showToastMessage(activity, activity.getString(R.string.error));

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Applications_Item_Iv_Photo)
        PorterShapeImageView ApplicationsItemIvPhoto;
        @BindView(R.id.Applications_Item_Tv_Name)
        TextView ApplicationsItemTvName;
        @BindView(R.id.Applications_Item_Tv_Number)
        TextView ApplicationsItemTvNumber;
        @BindView(R.id.Applications_Item_Tv_Total)
        TextView ApplicationsItemTvTotal;
        @BindView(R.id.Applications_Item_Tv_Address)
        TextView ApplicationsItemTvAddress;
        @BindView(R.id.Applications_Item_Lin_Mobile)
        LinearLayout ApplicationsItemLinMobile;
        @BindView(R.id.Applications_Item_Btn_Accept)
        Button ApplicationsItemBtnAccept;
        @BindView(R.id.Applications_Item_Btn_Confirm)
        Button ApplicationsItemBtnConfirm;
        @BindView(R.id.Applications_Item_Btn_Reject)
        Button ApplicationsItemBtnReject;
        @BindView(R.id.Applications_Item_Tv_OrderDone)
        TextView ApplicationsItemTvOrderDone;
        @BindView(R.id.linearLayout5)
        LinearLayout linearLayout5;
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
