package com.ashraf.amr.apps.sofra.helper;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantcategories.CategoriesData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertFileToMultipart;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertToRequestBody;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrlCircle;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.openAlbum;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;


public class EditCategoryDialog extends Dialog {

    @BindView(R.id.Dialog_Categories_Civ_Category_Image)
    CircleImageView DialogCategoriesCivCategoryImage;
    @BindView(R.id.Dialog_Categories_Et_Category_Name)
    EditText DialogCategoriesEtCategoryName;
    @BindView(R.id.Dialog_Categories_Btn_Add)
    Button DialogCategoriesBtnAdd;
    @BindView(R.id.Dialog_Categories_Tv_Category_Image)
    TextView DialogCategoriesTvCategoryImage;
    @BindView(R.id.plus)
    ImageView plus;
    @BindView(R.id.Dialog_Categories_Relative_Category_Image)
    RelativeLayout DialogCategoriesRelativeCategoryImage;
    private Activity activity;
    private String apiToken = "";
    private ApiServices apiService;
    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();
    private RestaurantData user;
    private String name = "";
    public CategoriesData categoriesData;

    public EditCategoryDialog(Activity activity) {
        super(activity);
        this.activity = activity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.diaog_add_category);
        ButterKnife.bind(this);
        DialogCategoriesTvCategoryImage.setText("تعدل التصنيف");
        DialogCategoriesBtnAdd.setText("تعديل");
        plus.setVisibility(View.GONE);
        DialogCategoriesRelativeCategoryImage.setBackground(null);
        DialogCategoriesEtCategoryName.setText(categoriesData.getName());
        onLoadImageFromUrlCircle(DialogCategoriesCivCategoryImage, categoriesData.getPhotoUrl(), activity, 0);
    }


    private void addImage() {
        Action<ArrayList<AlbumFile>> action = new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                // TODO accept the result.

                ImagesFiles.clear();
                ImagesFiles.addAll(result);

                onLoadImageFromUrl(DialogCategoriesCivCategoryImage, ImagesFiles.get(0).getPath(), getContext());

            }
        };

        openAlbum(1, activity, ImagesFiles, action);
    }


    @OnClick({R.id.Dialog_Categories_Civ_Category_Image, R.id.Dialog_Categories_Btn_Add})
    public void onViewClicked(View view) {
        disappearKeypad(activity, view);

        switch (view.getId()) {
            case R.id.Dialog_Categories_Civ_Category_Image:
                addImage();
                break;

            case R.id.Dialog_Categories_Btn_Add:
                updateCategory();
                break;
        }
    }

    private void updateCategory() {

        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(activity);
        String name = DialogCategoriesEtCategoryName.getText().toString();


        if (ImagesFiles.size() == 0
                && name.equals(categoriesData.getName())) {

            return;
        }

        if (name.isEmpty()) {
            showToastMessage(activity, activity.getString(R.string.enter_product_name));
        }


        if (InternetState.isConnected(activity)) {
            showProgressDialog(activity, activity.getString(R.string.waiit));
            if (ImagesFiles.size() != 0) {
                apiService.updateCategory(
                        convertToRequestBody(name)
                        , convertToRequestBody(user.getApiToken())
                        , convertToRequestBody(String.valueOf(categoriesData.getId()))
                        , convertFileToMultipart(ImagesFiles.get(0).getPath(), "photo")
                ).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus() == 1) {
                                showSuccessToastMessage(activity, response.body().getMsg());
                                dismiss();
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


            } else if (ImagesFiles.size() == 0) {
                apiService.updateCategoryNoImage(
                        convertToRequestBody(name)
                        , convertToRequestBody(user.getApiToken())
                        , convertToRequestBody(String.valueOf(categoriesData.getId()))

                ).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        dismissProgressDialog();
                        try {
                            if (response.body().getStatus() == 1) {
                                showSuccessToastMessage(activity, response.body().getMsg());

                                dismiss();

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


        } else {
            dismissProgressDialog();
            showToastMessage(activity, activity.getString(R.string.no_internet));
        }


    }

}