package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.offers;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.offerlist.OffersListData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.updateoffer.UpdateOffer;
import com.ashraf.amr.apps.sofra.helper.DateTxt;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertFileToMultipart;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertStringToDateTxtModel;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertToRequestBody;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.openAlbum;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateOfferFragment extends BaseFragment {

    public OffersListData offersDataList;
    @BindView(R.id.Restaurant_Update_Offer_Iv_ProductPhoto)
    PorterShapeImageView RestaurantUpdateOfferIvProductPhoto;
    @BindView(R.id.Restaurant_Update_Offer_Et_Offer_Name)
    EditText RestaurantUpdateOfferEtOfferName;
    @BindView(R.id.Restaurant_Update_Offer_Et_Offer_Description)
    EditText RestaurantUpdateOfferEtOfferDescription;
    @BindView(R.id.Restaurant_Update_Offer_Et_Price)
    EditText RestaurantUpdateOfferEtPrice;
    @BindView(R.id.Restaurant_Update_Offer_Tv_From)
    TextView RestaurantUpdateOfferTvFrom;
    @BindView(R.id.Restaurant_Update_Offer_Tv_To)
    TextView RestaurantUpdateOfferTvTo;
    @BindView(R.id.Restaurant_Update_Offer_Btn_Add)
    Button RestaurantUpdateOfferBtnAdd;
    Unbinder unbinder;
    @BindView(R.id.Restaurant_Update_Offer_Linear)
    LinearLayout RestaurantUpdateOfferLinear;
    private DateTxt From ,To;
    private ApiServices apiService;
    private RestaurantData user;
    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();
    public int offer_id;

    public UpdateOfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_offer, container, false);
        unbinder = ButterKnife.bind(this, view);
        RestaurantUpdateOfferEtOfferName.setText(offersDataList.getName());
        RestaurantUpdateOfferEtOfferDescription.setText(offersDataList.getDescription());
        RestaurantUpdateOfferEtPrice.setText(offersDataList.getPrice());
        RestaurantUpdateOfferTvFrom.setText(offersDataList.getStartingAt());
        RestaurantUpdateOfferTvTo.setText(offersDataList.getEndingAt());
        onLoadImageFromUrl(RestaurantUpdateOfferIvProductPhoto, offersDataList.getPhotoUrl(), getActivity());

        From = convertStringToDateTxtModel(offersDataList.getStartingAt());
        To = convertStringToDateTxtModel(offersDataList.getEndingAt());
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Restaurant_Update_Offer_Iv_ProductPhoto,
            R.id.Restaurant_Update_Offer_Tv_From,
            R.id.Restaurant_Update_Offer_Tv_To,
            R.id.Restaurant_Update_Offer_Btn_Add,
            R.id.Restaurant_Update_Offer_Linear})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.Restaurant_Update_Offer_Iv_ProductPhoto:
                addImage();

                break;
            case R.id.Restaurant_Update_Offer_Tv_From:
                HelperClass.showCalender(getActivity(), getString(R.string.offer_start_date), RestaurantUpdateOfferTvFrom, From);

                break;
            case R.id.Restaurant_Update_Offer_Tv_To:
                HelperClass.showCalender(getActivity(), getString(R.string.offer_end_date), RestaurantUpdateOfferTvTo, To);

                break;
            case R.id.Restaurant_Update_Offer_Btn_Add:
                addNewOffer();

                break;
            case R.id.Restaurant_Update_Offer_Linear:
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

                onLoadImageFromUrl(RestaurantUpdateOfferIvProductPhoto, ImagesFiles.get(0).getPath(), getContext());

            }
        };

        openAlbum(1, getActivity(), ImagesFiles, action);
    }


    private void addNewOffer() {
        String description = RestaurantUpdateOfferEtOfferDescription.getText().toString();
        String price = RestaurantUpdateOfferEtPrice.getText().toString();
        String starting_at = RestaurantUpdateOfferTvFrom.getText().toString();
        String name = RestaurantUpdateOfferEtOfferName.getText().toString();
        String ending_at = RestaurantUpdateOfferTvTo.getText().toString();

        if (ImagesFiles.size() == 0
                && name.equals(offersDataList.getName())
                && description.equals(offersDataList.getDescription())
                && price.equals(offersDataList.getPrice())
                && starting_at.equals(offersDataList.getStartingAt())
                && ending_at.equals(offersDataList.getEndingAt())) {

            return;
        }
        if (name.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_product_name));
        }

        if (description.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_product_description));
        }
        if (price.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_product_price));
        }
        if (starting_at.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_product_preparing_time));
        }
        if (ending_at.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_product_preparing_time));
        }
        if (InternetState.isConnected(getActivity())) {
            HelperClass.showProgressDialog(getActivity(), getString(R.string.waiit));
            if (ImagesFiles.size() != 0) {

                apiService.updateOffer(
                        convertToRequestBody(description)
                        , convertToRequestBody(price)
                        , convertToRequestBody(starting_at)
                        , convertToRequestBody(ending_at)
                        , convertToRequestBody(name)
                        , convertToRequestBody(user.getApiToken())
                        , convertToRequestBody(String.valueOf(offer_id))
                        , convertFileToMultipart(ImagesFiles.get(0).getPath(), "photo"))
                        .enqueue(new Callback<UpdateOffer>() {
                            @Override
                            public void onResponse(Call<UpdateOffer> call, Response<UpdateOffer> response) {
                                dismissProgressDialog();
                                try {
                                    if (response.body().getStatus() == 1) {
                                        showSuccessToastMessage(getActivity(), response.body().getMsg());
                                        onBack();
                                    } else {
                                        showToastMessage(getActivity(), response.body().getMsg());
                                    }
                                } catch (Exception e) {

                                }

                            }

                            @Override
                            public void onFailure(Call<UpdateOffer> call, Throwable t) {
                                dismissProgressDialog();
                                showToastMessage(getActivity(), getString(R.string.error));
                            }
                        });

            } else if (ImagesFiles.size() == 0) {
                apiService.updateOfferNoImage(
                        convertToRequestBody(description)
                        , convertToRequestBody(price)
                        , convertToRequestBody(starting_at)
                        , convertToRequestBody(ending_at)
                        , convertToRequestBody(name)
                        , convertToRequestBody(user.getApiToken())
                        , convertToRequestBody(String.valueOf(offer_id)))
                        .enqueue(new Callback<UpdateOffer>() {
                            @Override
                            public void onResponse(Call<UpdateOffer> call, Response<UpdateOffer> response) {

                                dismissProgressDialog();
                                try {
                                    if (response.body().getStatus() == 1) {
                                        showSuccessToastMessage(getActivity(), response.body().getMsg());
                                        onBack();
                                    } else {
                                        showToastMessage(getActivity(), response.body().getMsg());
                                    }
                                } catch (Exception e) {

                                }

                            }

                            @Override
                            public void onFailure(Call<UpdateOffer> call, Throwable t) {
                                dismissProgressDialog();
                                showToastMessage(getActivity(), getString(R.string.error));
                            }
                        });

            }


        } else {
            dismissProgressDialog();
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }


    }


}
