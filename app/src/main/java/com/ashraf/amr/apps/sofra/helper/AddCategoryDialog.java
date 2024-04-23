package com.ashraf.amr.apps.sofra.helper;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.newcategory.NewCategory;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.categories.CategoriesFragment;
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
import static com.ashraf.amr.apps.sofra.helper.HelperClass.openAlbum;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;


public class AddCategoryDialog extends Dialog {

    @BindView(R.id.Dialog_Categories_Civ_Category_Image)
    CircleImageView DialogCategoriesCivCategoryImage;
    @BindView(R.id.Dialog_Categories_Relative_Category_Image)
    RelativeLayout DialogCategoriesRelativeCategoryImage;
    @BindView(R.id.Dialog_Categories_Et_Category_Name)
    EditText DialogCategoriesEtCategoryName;
    @BindView(R.id.Dialog_Categories_Btn_Add)
    Button DialogCategoriesBtnAdd;
    private Activity activity;
    private String apiToken = "";
    private ApiServices apiService;
    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();
    private RestaurantData user;
    private String name = "";
    private CategoriesFragment categoriesFragment;

    public AddCategoryDialog(Activity activity, CategoriesFragment categoriesFragment) {
        super(activity);
        this.activity = activity;
        this.categoriesFragment = categoriesFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.diaog_add_category);
        ButterKnife.bind(this);

    }

    private void addCategory() {
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(activity);
        name = DialogCategoriesEtCategoryName.getText().toString();

        if (ImagesFiles.size() == 0) {
            showToastMessage(activity, "برجاء اختيار صورة التصنيف");
        } else if (name.isEmpty()) {
            showToastMessage(activity, "برجاء اختيار اسم التصنيف");
        } else {
            try {
                if (InternetState.isConnected(activity)) {
                    HelperClass.showProgressDialog(activity, activity.getString(R.string.waiit));
                    apiService.addCategory(convertToRequestBody(user.getApiToken())
                            , convertFileToMultipart(ImagesFiles.get(0).getPath(), "photo")
                            , convertToRequestBody(name)
                    ).enqueue(new Callback<NewCategory>() {
                        @Override
                        public void onResponse(Call<NewCategory> call, Response<NewCategory> response) {
                            try {
                                dismissProgressDialog();
                                if (response.body().getStatus() == 1) {
                                    showSuccessToastMessage(activity, response.body().getMsg());
                                    categoriesFragment.categoriesData.add(0, response.body().getData());
                                    categoriesFragment.restaurantCategoriesAdapter.notifyDataSetChanged();
                                    dismiss();
                                } else {
                                    showToastMessage(activity, response.body().getMsg());
                                }
                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<NewCategory> call, Throwable t) {
                            dismissProgressDialog();
                            showToastMessage(activity, activity.getString(R.string.error));
                        }
                    });

                } else {
                    dismissProgressDialog();
                    showToastMessage(activity, activity.getString(R.string.no_internet));
                }
            } catch (Exception e) {

            }

        }


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


    @OnClick({R.id.Dialog_Categories_Civ_Category_Image, R.id.Dialog_Categories_Relative_Category_Image, R.id.Dialog_Categories_Btn_Add})
    public void onViewClicked(View view) {
        disappearKeypad(activity, view);
        switch (view.getId()) {
            case R.id.Dialog_Categories_Civ_Category_Image:
                addImage();
                break;
            case R.id.Dialog_Categories_Relative_Category_Image:
                addImage();
                break;
            case R.id.Dialog_Categories_Btn_Add:
                addCategory();
                break;
        }
    }
}