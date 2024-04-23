package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.general;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.CustomDialog;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.ProfileActivity;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.Change_Password_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.general.About_App_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.general.Contact_Us_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.general.Offers_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.restaurantDetails.CommentsAndRating_Fragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.offers.RestaurantOffersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTREMEMBER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.SaveData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.clean;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;


public class MoreFragment extends BaseFragment {


    @BindView(R.id.Restaurant_More_Fragment_Rel_CallUs)
    LinearLayout RestaurantMoreFragmentRelCallUs;
    @BindView(R.id.Restaurant_More_Fragment_Rel_AboutApp)
    LinearLayout RestaurantMoreFragmentRelAboutApp;
    @BindView(R.id.Restaurant_More_Fragment_Rel_SignOut)
    LinearLayout RestaurantMoreFragmentRelSignOut;
    Unbinder unbinder;
    @BindView(R.id.Restaurant_More_Fragment_Tv_Offers)
    TextView RestaurantMoreFragmentTvOffers;
    @BindView(R.id.Restaurant_More_Fragment_Rel_Offers)
    LinearLayout RestaurantMoreFragmentRelOffers;
    @BindView(R.id.Restaurant_More_Fragment_Tv_CallUs)
    TextView RestaurantMoreFragmentTvCallUs;
    @BindView(R.id.Restaurant_More_Fragment_Tv_AboutApp)
    TextView RestaurantMoreFragmentTvAboutApp;
    @BindView(R.id.Restaurant_More_Fragment_Tv_SignOut)
    TextView RestaurantMoreFragmentTvSignOut;
    @BindView(R.id.Restaurant_More_Fragment_Rel_Comments)
    LinearLayout RestaurantMoreFragmentRelComments;
    @BindView(R.id.Restaurant_More_Fragment_Tv_Change_Password)
    TextView RestaurantMoreFragmentTvChangePassword;
    @BindView(R.id.Restaurant_More_Fragment_Rel_Change_Password)
    LinearLayout RestaurantMoreFragmentRelChangePassword;
    @BindView(R.id.fragment_general_more_view_line_4)
    View fragmentGeneralMoreViewLine4;
    @BindView(R.id.fragment_general_more_view_line_5)
    View fragmentGeneralMoreViewLine5;
    @BindView(R.id.fragment_general_more_view_line_3)
    View fragmentGeneralMoreViewLine3;
    @BindView(R.id.ic_sign_out_alt_solid)
    ImageView icSignOutAltSolid;

    private CustomDialog dailog;
    //private ApiServices apiServices;
    private RestaurantData user;
    private Client client;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restauranr__more, container, false);
        unbinder = ButterKnife.bind(this, view);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        client = SharedPreferencesManger.loadClientData(getActivity());

        if (ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_RTL) { //Arabic
            icSignOutAltSolid.setScaleX(1);
        } else if (ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_LTR) { // English
            icSignOutAltSolid.setScaleX(-1);
        }

        if (navBar != null){
            navBar.setVisibility(View.VISIBLE);
        }

        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {

            RestaurantMoreFragmentTvOffers.setText(R.string.offers);
            RestaurantMoreFragmentRelComments.setVisibility(View.GONE);
            fragmentGeneralMoreViewLine4.setVisibility(View.GONE);

            if (SharedPreferencesManger.getClientName(getActivity()).equals(CLIENTREMEMBER)) {
                fragmentGeneralMoreViewLine3.setVisibility(View.VISIBLE);
                RestaurantMoreFragmentRelChangePassword.setVisibility(View.VISIBLE);
                fragmentGeneralMoreViewLine5.setVisibility(View.VISIBLE);
                RestaurantMoreFragmentRelSignOut.setVisibility(View.VISIBLE);

            } else {
                fragmentGeneralMoreViewLine3.setVisibility(View.GONE);
                RestaurantMoreFragmentRelChangePassword.setVisibility(View.GONE);
                fragmentGeneralMoreViewLine5.setVisibility(View.GONE);
                RestaurantMoreFragmentRelSignOut.setVisibility(View.GONE);
            }
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Restaurant_More_Fragment_Rel_Offers,
            R.id.Restaurant_More_Fragment_Rel_CallUs,
            R.id.Restaurant_More_Fragment_Rel_AboutApp,
            R.id.Restaurant_More_Fragment_Rel_SignOut,
            R.id.Restaurant_More_Fragment_Rel_Change_Password,
            R.id.Restaurant_More_Fragment_Rel_Comments})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.Restaurant_More_Fragment_Rel_Offers:
                if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
                    RestaurantOffersFragment restaurantOffers = new RestaurantOffersFragment();
                    assert getFragmentManager() != null;
                    replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, restaurantOffers);
                }
                if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
                    Offers_Fragment offers = new Offers_Fragment();
                    assert getFragmentManager() != null;
                    replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, offers);
                }


                break;
            case R.id.Restaurant_More_Fragment_Rel_CallUs:
                Contact_Us_Fragment contactUs = new Contact_Us_Fragment();
                assert getFragmentManager() != null;
                replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, contactUs);

                break;
            case R.id.Restaurant_More_Fragment_Rel_AboutApp:
                About_App_Fragment aboutApp = new About_App_Fragment();
                assert getFragmentManager() != null;
                replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, aboutApp);
                break;

            case R.id.Restaurant_More_Fragment_Rel_Comments:
                CommentsAndRating_Fragment commentsAndRating = new CommentsAndRating_Fragment();
                commentsAndRating.restaurant_id = user.getId();
                assert getFragmentManager() != null;
                replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, commentsAndRating);
                break;
            case R.id.Restaurant_More_Fragment_Rel_Change_Password:
                Change_Password_Fragment changePassword = new Change_Password_Fragment();
                assert getFragmentManager() != null;
                replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, changePassword);

                break;

            case R.id.Restaurant_More_Fragment_Rel_SignOut:

                View.OnClickListener ok = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
                            clean(getActivity());
                            SaveData(getActivity(), USER_TYPE, "");
                            SaveData(getActivity(), USER_TYPE, RESTAURANT);
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            startActivity(intent);
                        }

//                        if (LoadData(getActivity(), USER_TYPE).equals(RESTAURANT)) {
//                            apiServices.removeRestaurantnotification(FirebaseInstanceId.getInstance().getToken(), Client.getApiToken()).enqueue(new Callback<RemoveRestaurantNotification>() {
//                                @Override
//                                public void onResponse(Call<RemoveRestaurantNotification> call, Response<RemoveRestaurantNotification> response) {
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<RemoveRestaurantNotification> call, Throwable t) {
//
//                                }
//                            });
//
//                        }
//                        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
//
//                            apiServices.removeClientnotification(FirebaseInstanceId.getInstance().getToken(), client.getApiToken()).enqueue(new Callback<RemoveClientNotification>() {
//                                @Override
//                                public void onResponse(Call<RemoveClientNotification> call, Response<RemoveClientNotification> response) {
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<RemoveClientNotification> call, Throwable t) {
//
//                                }
//                            });
//
//                        }

                        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
                            clean(getActivity());
                            SaveData(getActivity(), USER_TYPE, "");
                            SaveData(getActivity(), USER_TYPE, CLIENT);
                            Intent intent = new Intent(getActivity(), Navigation_Activity.class);
                            startActivity(intent);

                        }

                    }
                };
                View.OnClickListener cancel = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dailog.dismiss();
                    }
                };

                dailog = new CustomDialog(getActivity(), getActivity(), getString(R.string.log_out_title), ok, cancel, true, true);

                break;
        }
    }

    @Override
    public void onBack() {
        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
            replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                    , homeActivity.restaurantsList);
        } else {
            replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                    , homeActivity.categoriesFragment);

        }
    }
}
