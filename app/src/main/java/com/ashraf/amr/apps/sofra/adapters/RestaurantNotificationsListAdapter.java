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

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.model.notifications.restaurantnotificationlist.RestaurantNotificationsListData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ashraf.amr.apps.sofra.helper.GetTimeAgo.getDate;


public class RestaurantNotificationsListAdapter extends RecyclerView.Adapter<RestaurantNotificationsListAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<RestaurantNotificationsListData> restaurantNotificationsListData = new ArrayList<>();
    private String lang = "en";

    public RestaurantNotificationsListAdapter(Context context, Activity activity, List<RestaurantNotificationsListData> restaurantNotificationsListData) {
        this.context = context;
        this.activity = activity;
        this.restaurantNotificationsListData = restaurantNotificationsListData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notifications_list,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.ItemNotificationListTvTitle.setText(restaurantNotificationsListData.get(position).getTitle());

        holder.ItemNotificationListTvTimeAgo.setText(getDate(restaurantNotificationsListData.get(position).getCreatedAt(), new Locale(lang)));

    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return restaurantNotificationsListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Item_Notification_List_Iv_Icon)
        ImageView ItemNotificationListIvIcon;
        @BindView(R.id.Item_Notification_List_Tv_Title)
        TextView ItemNotificationListTvTitle;
        @BindView(R.id.Item_Notification_List_Tv_TimeAgo)
        TextView ItemNotificationListTvTimeAgo;
        @BindView(R.id.Item_Notification_List_Iv_TimeIcon)
        ImageView ItemNotificationListIvTimeIcon;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
