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
import android.widget.Toast;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.model.general.listofoffers.NewOffersData;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.fragments.client.general.Offer_Details_Fragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrlCircle;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<NewOffersData> newOffersList = new ArrayList<>();

    public OffersAdapter(Context context, Activity activity, List<NewOffersData> newOffersList) {
        this.context = context;
        this.activity = activity;
        this.newOffersList = newOffersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_offers,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        onLoadImageFromUrlCircle(holder.OffersItemIvPhoto, newOffersList.get(position).getRestaurant().getPhotoUrl(), context, 0);

        if(newOffersList.get(position).getId() == 272){
            Glide.with(context)
                    .load(R.drawable.kfc)
                    .into(holder.OffersItemIvPhoto);
        }


        holder.OffersItemTvTitle.setText(newOffersList.get(position).getDescription());

    }

    private void setAction(ViewHolder holder, final int position) {
        holder.OffersItemBtnGetIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation_Activity restaurantNavigationActivity = (Navigation_Activity) activity;
                Offer_Details_Fragment offerDetails = new Offer_Details_Fragment();
                offerDetails.newOffersList = newOffersList.get(position);
                HelperClass.replaceFragment(restaurantNavigationActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, offerDetails);

            }
        });
    }

    @Override
    public int getItemCount() {
        return newOffersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.Offers_Item_Tv_Title)
        TextView OffersItemTvTitle;
        @BindView(R.id.Offers_Item_Btn_GetIt)
        Button OffersItemBtnGetIt;
        @BindView(R.id.Offers_Item_Iv_Photo)
        CircleImageView OffersItemIvPhoto;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
