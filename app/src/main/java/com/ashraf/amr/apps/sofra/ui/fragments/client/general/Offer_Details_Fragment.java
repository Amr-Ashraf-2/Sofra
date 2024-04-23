package com.ashraf.amr.apps.sofra.ui.fragments.client.general;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.model.general.listofoffers.NewOffersData;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.restaurantDetails.Restaurant_Details_Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.OFFER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.SaveData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.WHERE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Offer_Details_Fragment extends BaseFragment {


    @BindView(R.id.Offer_Details_Fragment_Tv_Title)
    TextView OfferDetailsFragmentTvTitle;
    @BindView(R.id.Offer_Details_Fragment_Tv_Name)
    TextView OfferDetailsFragmentTvName;
    @BindView(R.id.Offer_Details_Fragment_Tv_Description)
    TextView OfferDetailsFragmentTvDescription;
    @BindView(R.id.Offer_Details_Fragment_Tv_From)
    TextView OfferDetailsFragmentTvFrom;
    @BindView(R.id.Offer_Details_Fragment_Tv_To)
    TextView OfferDetailsFragmentTvTo;
    @BindView(R.id.Offer_Details_Fragment_Btn_GetIt)
    Button OfferDetailsFragmentBtnGetIt;
    @BindView(R.id.Offer_Details_Fragment_Rel_Details)
    RelativeLayout OfferDetailsFragmentRelDetails;
    @BindView(R.id.Offer_Details_Fragment_Tv_Restaurant_Name)
    TextView OfferDetailsFragmentTvRestaurantName;
    Unbinder unbinder;

    public NewOffersData newOffersList;


    public Offer_Details_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer__details, container, false);
        unbinder = ButterKnife.bind(this, view);
        navBar.setVisibility(View.GONE);
        try {
            getData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void getData() throws ParseException {
        OfferDetailsFragmentTvRestaurantName.setText(newOffersList.getRestaurant().getName());
        OfferDetailsFragmentTvName.setText(newOffersList.getName());
        OfferDetailsFragmentTvDescription.setText(newOffersList.getDescription());

        String from = newOffersList.getStartingAt();
        String to = newOffersList.getEndingAt();

        //String date = "Mar 10, 2016 6:30:00 PM";
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date newFrom = spf.parse(from);
        Date newTo = spf.parse(to);
        spf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String newDateFrom = spf.format(newFrom);
        String newDateTo = spf.format(newTo);

        OfferDetailsFragmentTvFrom.setText(newDateFrom);
        OfferDetailsFragmentTvTo.setText(newDateTo);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.Offer_Details_Fragment_Btn_GetIt)
    public void onViewClicked() {
        SaveData(getActivity() , WHERE , OFFER);
        Restaurant_Details_Fragment restaurantDetails = new Restaurant_Details_Fragment();
        restaurantDetails.newOffersList = newOffersList;
        assert getFragmentManager() != null;
        replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, restaurantDetails);
    }
}
