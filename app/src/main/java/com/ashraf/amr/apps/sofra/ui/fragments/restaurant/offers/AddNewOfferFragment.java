package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.offers;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.newoffer.NewOffer;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.helper.DateTxt;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertFileToMultipart;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertToRequestBody;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.openAlbum;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showCalender;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;


public class AddNewOfferFragment extends BaseFragment {

    @BindView(R.id.Restaurant_Add_New_Offer_Iv_ProductPhoto)
    PorterShapeImageView RestaurantAddNewOfferIvProductPhoto;
    @BindView(R.id.Restaurant_Add_New_Offer_Et_Offer_Name)
    EditText RestaurantAddNewOfferEtOfferName;
    @BindView(R.id.Restaurant_Add_New_Offer_Et_Offer_Description)
    EditText RestaurantAddNewOfferEtOfferDescription;
    @BindView(R.id.Restaurant_Add_New_Offer_Et_Price)
    EditText RestaurantAddNewOfferEtPrice;
    @BindView(R.id.Restaurant_Add_New_Offer_Tv_From)
    TextView RestaurantAddNewOfferTvFrom;
    @BindView(R.id.Restaurant_Add_New_Offer_Tv_To)
    TextView RestaurantAddNewOfferTvTo;
    @BindView(R.id.Restaurant_Add_New_Offer_Btn_Add)
    Button RestaurantAddNewOfferBtnAdd;
    Unbinder unbinder;
    @BindView(R.id.Restaurant_Add_New_Offer_Iv_Choose)
    ImageView RestaurantAddNewOfferIvChoose;
    @BindView(R.id.Restaurant_Add_New_Offer_Linear)
    LinearLayout RestaurantAddNewOfferLinear;

    private DateTxt From;
    private DateTxt To;
    private ApiServices apiService;
    private RestaurantData user;
    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();

    public AddNewOfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_offer, container, false);
        unbinder = ButterKnife.bind(this, view);

        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());

        getDate();


        return view;
    }

    private void getDate() {
        Calendar calander = Calendar.getInstance();
        DecimalFormat mFormat = new DecimalFormat("00");
        String cDay = mFormat.format(Double.valueOf(String.valueOf(calander.get(Calendar.DAY_OF_MONTH))));
        String cMonth = mFormat.format(Double.valueOf(String.valueOf(calander.get(Calendar.MONTH + 1))));
        String cYear = String.valueOf(calander.get(Calendar.YEAR));
        From = new DateTxt(cDay, cMonth, cYear, cDay + "-" + cMonth + "-" + cYear);
        To = new DateTxt(cDay, cMonth, cYear, cDay + "-" + cMonth + "-" + cYear);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Restaurant_Add_New_Offer_Iv_ProductPhoto,
            R.id.Restaurant_Add_New_Offer_Tv_From,
            R.id.Restaurant_Add_New_Offer_Tv_To,
            R.id.Restaurant_Add_New_Offer_Iv_Choose,
            R.id.Restaurant_Add_New_Offer_Btn_Add,
            R.id.Restaurant_Add_New_Offer_Linear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Restaurant_Add_New_Offer_Iv_ProductPhoto:
                addImage();
                break;
            case R.id.Restaurant_Add_New_Offer_Iv_Choose:
                addImage();
                RestaurantAddNewOfferIvChoose.setVisibility(View.GONE);
                break;
            case R.id.Restaurant_Add_New_Offer_Tv_From:
                showCalender(getActivity(), "تاريخ بدأ العرض", RestaurantAddNewOfferTvFrom, From);
                break;
            case R.id.Restaurant_Add_New_Offer_Tv_To:
                showCalender(getActivity(), "تاريخ إنتهاء العرض", RestaurantAddNewOfferTvTo, To);
                break;
            case R.id.Restaurant_Add_New_Offer_Btn_Add:
                addNewOffer();
                break;
            case R.id.Restaurant_Add_New_Offer_Linear:
                disappearKeypad(getActivity(), getView());
                break;
        }
    }

    private void addImage() {
        Action<ArrayList<AlbumFile>> action = new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                // TODO accept the result.

                ImagesFiles.clear();
                ImagesFiles.addAll(result);

                onLoadImageFromUrl(RestaurantAddNewOfferIvProductPhoto, ImagesFiles.get(0).getPath(), getContext());

            }
        };

        openAlbum(1, getActivity(), ImagesFiles, action);
    }


    private void addNewOffer() {
        String description = RestaurantAddNewOfferEtOfferDescription.getText().toString();
        String price = RestaurantAddNewOfferEtPrice.getText().toString();
        String starting_at = RestaurantAddNewOfferTvFrom.getText().toString();
        String name = RestaurantAddNewOfferEtOfferName.getText().toString();
        String ending_at = RestaurantAddNewOfferTvTo.getText().toString();

        if (ImagesFiles.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_offer_image));
            return;
        }
        if (name.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_offer_name));
            return;
        }
        if (description.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_offer_description));
            return;
        }
        if (price.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_offer_price));
            return;
        }
        if (starting_at.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_offer_start));
            return;
        }
        if (ending_at.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_offer_end));
            return;
        }

        try {
            if (InternetState.isConnected(getActivity())) {
                HelperClass.showProgressDialog(getActivity(), getString(R.string.waiit));

                Call<NewOffer> call;

                call = apiService.postnewoffer(
                        convertToRequestBody(description)
                        , convertToRequestBody(price)
                        , convertToRequestBody(starting_at)
                        , convertToRequestBody(ending_at)
                        , convertToRequestBody(name)
                        , convertToRequestBody(user.getApiToken())
                        , convertFileToMultipart(ImagesFiles.get(0).getPath(), "photo"));

                call.enqueue(new Callback<NewOffer>() {
                    @Override
                    public void onResponse(Call<NewOffer> call, Response<NewOffer> response) {
                        dismissProgressDialog();
                        disappearKeypad(getActivity() , getView());
                        try {
                            if (response.body().getStatus() == 1) {
                                showSuccessToastMessage(getActivity(), response.body().getMsg());
                                onBack();


                            } else {
                                dismissProgressDialog();
                                showToastMessage(getActivity(), response.body().getMsg());
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<NewOffer> call, Throwable t) {
                        dismissProgressDialog();
                        showToastMessage(getActivity(), getString(R.string.error));
                        disappearKeypad(getActivity() , getView());
                    }
                });

            } else {
                dismissProgressDialog();
                showToastMessage(getActivity(), getString(R.string.no_internet));
                disappearKeypad(getActivity() , getView());
            }
        } catch (Exception e) {

        }


    }

}
