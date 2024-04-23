package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.products;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.updatProduct.UpdatProduct;
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
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertToRequestBody;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.openAlbum;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class Update_Product_Fragment extends BaseFragment {


    @BindView(R.id.Restaurant_Add_New_Product_Iv_ProductPhoto)
    PorterShapeImageView RestaurantAddNewProductIvProductPhoto;
    @BindView(R.id.Restaurant_Add_New_Product_Et_Product_Name)
    EditText RestaurantAddNewProductEtProductName;
    @BindView(R.id.Restaurant_Add_New_Product_Et_Product_Description)
    EditText RestaurantAddNewProductEtProductDescription;
    @BindView(R.id.Restaurant_Add_New_Product_Et_Price)
    EditText RestaurantAddNewProductEtPrice;
    @BindView(R.id.Restaurant_Add_New_Product_Btn_Add)
    Button RestaurantAddNewProductBtnAdd;
    @BindView(R.id.Restaurant_Add_New_Product_Et_SpecialPrice)
    EditText RestaurantAddNewProductEtSpecialPrice;
    @BindView(R.id.Restaurant_Add_New_Product_Iv_Choose)
    ImageView RestaurantAddNewProductIvChoose;
    @BindView(R.id.Restaurant_Add_New_Product_Linear)
    LinearLayout RestaurantAddNewProductLinear;
    private ProductsFragment productsFragment;
    private ApiServices apiService;
    private RestaurantData user;
    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();
    public int item_id;
    public ProductsData productsData;
    public String category_id;
    Unbinder unbinder;

    public Update_Product_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_product, container, false);
        unbinder = ButterKnife.bind(this, view);
        RestaurantAddNewProductBtnAdd.setText(getString(R.string.adjust));
        RestaurantAddNewProductIvChoose.setVisibility(View.GONE);
        RestaurantAddNewProductEtProductName.setText(productsData.getName());
        RestaurantAddNewProductEtProductDescription.setText(productsData.getDescription());
        RestaurantAddNewProductEtPrice.setText(productsData.getPrice());
        RestaurantAddNewProductEtSpecialPrice.setText(productsData.getOfferPrice());
        onLoadImageFromUrl(RestaurantAddNewProductIvProductPhoto, productsData.getPhotoUrl(), getActivity());
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.Restaurant_Add_New_Product_Iv_ProductPhoto
            , R.id.Restaurant_Add_New_Product_Btn_Add
            , R.id.Restaurant_Add_New_Product_Linear})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.Restaurant_Add_New_Product_Iv_ProductPhoto:
                addImage();
                break;
            case R.id.Restaurant_Add_New_Product_Btn_Add:
                updateProduct();
                break;

            case R.id.Restaurant_Add_New_Product_Linear:
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
                onLoadImageFromUrl(RestaurantAddNewProductIvProductPhoto, ImagesFiles.get(0).getPath(), getActivity());

            }
        };

        openAlbum(1, getActivity(), ImagesFiles, action);
    }


    private void updateProduct() {
        String name = RestaurantAddNewProductEtProductName.getText().toString();
        String description = RestaurantAddNewProductEtProductDescription.getText().toString();
        String price = RestaurantAddNewProductEtPrice.getText().toString();
        String offer_price = RestaurantAddNewProductEtSpecialPrice.getText().toString();

        if (ImagesFiles.size() == 0
                && name.equals(productsData.getName())
                && description.equals(productsData.getDescription())
                && price.equals(productsData.getPrice())
                && offer_price.equals(productsData.getOfferPrice())) {

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

        if (InternetState.isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.waiit));
            if (ImagesFiles.size() != 0) {
                apiService.updateProduct(
                        convertToRequestBody(description)
                        , convertToRequestBody(price)
                        , convertToRequestBody(String.valueOf(item_id))
                        , convertToRequestBody(name)
                        , convertToRequestBody(user.getApiToken())
                        , convertFileToMultipart(ImagesFiles.get(0).getPath(), "photo")
                        , convertToRequestBody(offer_price)
                        , convertToRequestBody((category_id))).enqueue(new Callback<UpdatProduct>() {
                    @Override
                    public void onResponse(Call<UpdatProduct> call, Response<UpdatProduct> response) {
                        try {
                            dismissProgressDialog();
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
                    public void onFailure(Call<UpdatProduct> call, Throwable t) {
                        dismissProgressDialog();
                        showToastMessage(getActivity(), getString(R.string.error));
                    }
                });


            } else if (ImagesFiles.size() == 0) {
                apiService.updateProductNoPhoto(
                        convertToRequestBody(description)
                        , convertToRequestBody(price)
                        , convertToRequestBody(String.valueOf(item_id))
                        , convertToRequestBody(name)
                        , convertToRequestBody(user.getApiToken())
                        , convertToRequestBody(offer_price)
                        , convertToRequestBody((category_id))).enqueue(new Callback<UpdatProduct>() {
                    @Override
                    public void onResponse(Call<UpdatProduct> call, Response<UpdatProduct> response) {
                        try {
                            dismissProgressDialog();
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
                    public void onFailure(Call<UpdatProduct> call, Throwable t) {
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
