package com.ashraf.amr.apps.sofra.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.model.notifications.clientnotificationlist.ClientNotificationsListData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ashraf.amr.apps.sofra.helper.GetTimeAgo.getDate;

public class ClientNotificationsListAdapter extends RecyclerView.Adapter<ClientNotificationsListAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<ClientNotificationsListData> clientNotificationsListData = new ArrayList<>();
    private String lang = "en";
    private View viewContainer;
    private Boolean timeAgo = false;

    public ClientNotificationsListAdapter(Context context, Activity activity, List<ClientNotificationsListData> clientNotificationsListData, View viewContainer) {
        this.context = context;
        this.activity = activity;
        this.clientNotificationsListData = clientNotificationsListData;
        this.viewContainer = viewContainer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        //holder.ItemNotificationListTvTitle.setText(clientNotificationsListData.get(position).getTitle());

        //holder.ItemNotificationListTvTimeAgo.setText(getDate(clientNotificationsListData.get(position).getUpdatedAt(), new Locale(lang)));

//        holder.ItemNotificationListTvTimeAgo.setText(clientNotificationsListData.get(position).getCreatedAt());

        if(ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_RTL){

            // if application langauge is Arabic

            if(clientNotificationsListData.get(position).getTitle() == null){
                holder.ItemNotificationListTvTitle.setText(activity.getString(R.string.no_title));
            }else {
                holder.ItemNotificationListTvTitle.setText(clientNotificationsListData.get(position).getTitle());
            }

        }else if (ViewCompat.getLayoutDirection(viewContainer) == ViewCompat.LAYOUT_DIRECTION_LTR){

            // if application langauge is English

            if(clientNotificationsListData.get(position).getTitleEn() == null){
                holder.ItemNotificationListTvTitle.setText(activity.getString(R.string.no_title));
            }else {
                holder.ItemNotificationListTvTitle.setText(clientNotificationsListData.get(position).getTitleEn());
            }

        }


        String currentDateAsString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String getFullDateTime = clientNotificationsListData.get(position).getUpdatedAt();
        String getDateOnlyAsString = getFullDateTime.substring(0, 10);

        String time;

        long dateDiff = getDiffDate(currentDateAsString,getDateOnlyAsString);

        //        else if(dateDiff > 60 && dateDiff <= 360){ // sana
//            time = String.valueOf(dateDiff);
//            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_days, time));
//        }
        if (dateDiff == 0){
            String getTimeOnlyAsString = getFullDateTime.substring(12, 19);
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            int timeDiff = getDiffTime(currentTime,getTimeOnlyAsString);
            time = String.valueOf(timeDiff);
            if (!timeAgo){ // minute
                holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_min, time));
            }else { // hour
                holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_hour, time));
            }
        }else if(dateDiff > 0 && dateDiff <30) { // 0 - 29
            time = String.valueOf(dateDiff);
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_days, time));
        }else if(dateDiff >= 30 && dateDiff < 60) { // 30 - 59 // 1
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 60 && dateDiff < 90) { // 60 - 89 // 2
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 90 && dateDiff < 120) { // 90 - 119 // 3
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 120 && dateDiff < 150) { // 120 - 149 // 4
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 150 && dateDiff < 180) { // 150 - 179 // 5
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 180 && dateDiff < 210) { // 180 - 209 // 6
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 210 && dateDiff < 240) { // 210 - 239 // 7
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 240 && dateDiff < 270) { // 240 - 269 // 8
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 270 && dateDiff < 300) { // 270 - 299 // 9
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 300 && dateDiff < 330) { // 300 - 329 // 10
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 330 && dateDiff < 360) { // 330 - 359 / 11
            time = String.valueOf(Math.round(dateDiff/30.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_months, time));
        }else if(dateDiff >= 360 && dateDiff <= 365) { // 360 - 364 / 12
            time = String.valueOf(1);
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_year, time));
        } else if (dateDiff > 365){ // 360
            time = String.valueOf(Math.round(dateDiff/365.0f));
            holder.ItemNotificationListTvTimeAgo.setText(context.getString(R.string.item_custom_notification_tv_alarm_time_year, time));
        }
    }

    private long getDiffDate(String currentDate, String oldDate){
        long differenceDates = 0;
        try {
            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());

            //Setting dates
            date1 = dates.parse(currentDate);
            date2 = dates.parse(oldDate);

            //Comparing dates
            long difference = 0;
            if (date1 != null && date2 != null) {
                difference = Math.abs(date2.getTime() - date1.getTime());
            }
            differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            //String dayDifference = Long.toString(differenceDates);

        } catch (Exception exception) {
            Toast.makeText(activity, "exception " + exception , Toast.LENGTH_SHORT).show();
        }

        return differenceDates;
    }

    private int getDiffTime(String currentTime, String oldTime){
        int returnValue= 0;
        try {
            Date date1;
            Date date2;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            date1 = simpleDateFormat.parse(currentTime);
            date2 = simpleDateFormat.parse(oldTime);

            long difference = 0;
            if (date1 != null && date2 != null) {
                difference = date2.getTime() - date1.getTime();
            }
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            //hours = (hours < 0 ? -hours : hours);

            if (hours < 1){
                returnValue = min;
                timeAgo = false;
            }else if(hours >= 1 && hours <= 24){
                returnValue = hours;
                timeAgo = true;
            }

        }catch (Exception exception){
            Toast.makeText(context, "exception " + exception , Toast.LENGTH_SHORT).show();
        }

        return returnValue;
    }

    private void setAction(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return clientNotificationsListData.size();
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
