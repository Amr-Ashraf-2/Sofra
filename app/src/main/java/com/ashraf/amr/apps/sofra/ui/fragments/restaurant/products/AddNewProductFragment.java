package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.products;


import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.ashraf.amr.apps.sofra.data.model.restaurant.addnewproduct.AddNewProduct;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
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

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertFileToMultipart;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertToRequestBody;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.openAlbum;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;

public class AddNewProductFragment extends BaseFragment {


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
    Unbinder unbinder;
    @BindView(R.id.Restaurant_Add_New_Product_Iv_Choose)
    ImageView RestaurantAddNewProductIvChoose;
    @BindView(R.id.Restaurant_Add_New_Product_Et_SpecialPrice)
    EditText RestaurantAddNewProductEtSpecialPrice;
    @BindView(R.id.Restaurant_Add_New_Product_Linear)
    LinearLayout RestaurantAddNewProductLinear;
    public int categoryId;
    private RestaurantData user;
    private ApiServices apiService;
    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();
    private ProductsFragment productsFragment;
    public AddNewProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_product, container, false);
        unbinder = ButterKnife.bind(this, view);
        if ((LoadData(getActivity(), USER_TYPE)).equals(RESTAURANT)) {
            apiService = RetrofitClient.getClient().create(ApiServices.class);
            user = SharedPreferencesManger.loadRestaurantData(getActivity());


        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Restaurant_Add_New_Product_Iv_ProductPhoto,
            R.id.Restaurant_Add_New_Product_Iv_Choose,
            R.id.Restaurant_Add_New_Product_Btn_Add,
            R.id.Restaurant_Add_New_Product_Linear})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.Restaurant_Add_New_Product_Iv_ProductPhoto:
                addImage();
                break;
            case R.id.Restaurant_Add_New_Product_Iv_Choose:
                addImage();
                RestaurantAddNewProductIvChoose.setVisibility(View.GONE);
                break;
            case R.id.Restaurant_Add_New_Product_Btn_Add:
                addNewProduct();
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

                onLoadImageFromUrl(RestaurantAddNewProductIvProductPhoto, ImagesFiles.get(0).getPath(), getContext());

            }
        };
        int x =1;
        openAlbum(x, getActivity(), ImagesFiles, action);
    }


    private void addNewProduct() {
        String description = RestaurantAddNewProductEtProductDescription.getText().toString();
        String price = RestaurantAddNewProductEtPrice.getText().toString();
        String name = RestaurantAddNewProductEtProductName.getText().toString();
        String offer_price = RestaurantAddNewProductEtSpecialPrice.getText().toString();


        if (ImagesFiles.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_product_image));
            return;
        }

        if (name.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_product_name));
            return;
        }
        if (description.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_product_description));
            return;
        }
        if (price.isEmpty()) {
            showToastMessage(getActivity(), getString(R.string.enter_product_price));
            return;
        }

        try {
            if (InternetState.isConnected(getActivity())) {
                showProgressDialog(getActivity(), getString(R.string.waiit));
                Call<AddNewProduct> call;
                call = apiService.addproduct(
                        convertToRequestBody(description)
                        , convertToRequestBody(price)
                        , convertToRequestBody(name)
                        , convertToRequestBody(user.getApiToken())
                        , convertFileToMultipart(ImagesFiles.get(0).getPath(), "photo")
                        , convertToRequestBody(offer_price)
                ,convertToRequestBody(String.valueOf(categoryId)));
                call.enqueue(new Callback<AddNewProduct>() {
                    @Override
                    public void onResponse(Call<AddNewProduct> call, Response<AddNewProduct> response) {
                        try {
                            dismissProgressDialog();
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
                    public void onFailure(Call<AddNewProduct> call, Throwable t) {
                        dismissProgressDialog();
                        showToastMessage(getActivity(), getString(R.string.error));
                    }
                });
            } else {
                dismissProgressDialog();
                showToastMessage(getActivity(), getString(R.string.no_internet));
            }
        } catch (Exception e) {

        }

    }

}
