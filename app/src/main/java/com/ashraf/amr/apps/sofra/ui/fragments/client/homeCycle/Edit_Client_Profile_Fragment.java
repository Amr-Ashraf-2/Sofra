package com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle;


import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.ClientLogin;
import com.ashraf.amr.apps.sofra.data.model.general.cities.Cities;
import com.ashraf.amr.apps.sofra.data.model.general.regions.Regions;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.saveClientData;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertFileToMultipart;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.convertToRequestBody;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.keyboardVisibility;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrlCircle;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.openAlbum;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;
import static com.ashraf.amr.apps.sofra.helper.Validation.setEmailValidation;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;


public class Edit_Client_Profile_Fragment extends BaseFragment {

    @BindView(R.id.Edit_Profile_Fragment_Civ_ProfilePicture)
    CircleImageView EditProfileFragmentCivProfilePicture;
    @BindView(R.id.Edit_Profile_Fragment_Et_Name)
    EditText EditProfileFragmentEtName;
    @BindView(R.id.Edit_Profile_Fragment_Et_Email)
    EditText EditProfileFragmentEtEmail;
    @BindView(R.id.Edit_Profile_Fragment_Et_Phone)
    EditText EditProfileFragmentEtPhone;
    @BindView(R.id.Image_Home)
    ImageView ImageHome;
    @BindView(R.id.Edit_Profile_Fragment_Sp_City)
    Spinner EditProfileFragmentSpCity;
    @BindView(R.id.Image_Home2)
    ImageView ImageHome2;
    @BindView(R.id.Edit_Profile_Fragment_Sp_Region)
    Spinner EditProfileFragmentSpRegion;
    @BindView(R.id.Edit_Profile_Fragment_Btn_Adjust)
    Button EditProfileFragmentBtnAdjust;
    @BindView(R.id.Lin)
    LinearLayout Lin;
    Unbinder unbinder;

    private Client client;
    private ApiServices apiService;
    private List<String> citiesTxt = new ArrayList<>();
    private List<Integer> citiesId = new ArrayList<>();

    private List<String> regionsTxt = new ArrayList<>();
    private List<Integer> regionsId = new ArrayList<>();
    private int region_id = 0;
    private ArrayList<AlbumFile> ImagesFiles = new ArrayList<>();
    private boolean imageFlag = false;
    private View convertView;
    private boolean isKeyboardShowing;
    private Animation slide_up, slide_down;
    private Animation fade_in, fade_out;

    public Edit_Client_Profile_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit__profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        convertView = view;
        slide_down = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getActivity() ,R.anim.slide_up);
        fade_in = AnimationUtils.loadAnimation(getActivity() ,R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getActivity() ,R.anim.fade_out);


        apiService = RetrofitClient.getClient().create(ApiServices.class);
        client = SharedPreferencesManger.loadClientData(getActivity());
        onLoadImageFromUrlCircle(EditProfileFragmentCivProfilePicture, client.getPhotoUrl(), getActivity(), 0);
        imageFlag = true;
        EditProfileFragmentEtName.setText(client.getName());
        EditProfileFragmentEtEmail.setText(client.getEmail());
        EditProfileFragmentEtPhone.setText(client.getPhone());

        getCities();

        convertView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        convertView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = convertView.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        //Log.d(TAG, "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                if (navBar != null) {
                                    navBar.startAnimation(fade_out);
                                    navBar.setVisibility(View.GONE);
                                    //Toast.makeText(context, "slide down", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                if (navBar != null) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            navBar.startAnimation(fade_in);
                                            navBar.setVisibility(View.VISIBLE);
                                        }
                                    }, 5);
                                }
                            }
                        }
                    }
                });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getCities() {
        if (getActivity()!= null){
            showProgressDialog(getActivity(), getActivity().getString(R.string.please_wait));
        }

        apiService.getcities().enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(@NonNull Call<Cities> call, @NonNull Response<Cities> response) {
                try {
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        //dismissProgressDialog();
                        citiesTxt.add(getString(R.string.select_city_3));
                        citiesId.add(0);
                        int pos = 0;

                        for (int i = 0; i < response.body().getData().getData().size(); i++) {
                            citiesTxt.add(response.body().getData().getData().get(i).getName());
                            citiesId.add(response.body().getData().getData().get(i).getId());
                            if (response.body().getData().getData().get(i).getName().equals(client.getRegion().getCity().getName())) {
                                pos = i + 1;
                            }
                        }

                        if (getActivity() != null){

                            ArrayAdapter<String> adapter;
                            if(ViewCompat.getLayoutDirection(convertView) == ViewCompat.LAYOUT_DIRECTION_RTL){ //Arabic
                                adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, citiesTxt);
                                EditProfileFragmentSpCity.setAdapter(adapter);
                            }else if (ViewCompat.getLayoutDirection(convertView) == ViewCompat.LAYOUT_DIRECTION_LTR){ // English
                                adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_left, citiesTxt);
                                EditProfileFragmentSpCity.setAdapter(adapter);
                            }

                            EditProfileFragmentSpCity.setSelection(pos);
                            getRegions(citiesId.get(pos));
                        }

                        EditProfileFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    getRegions(citiesId.get(i));
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    } //if response == 0

                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cities> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    private void getRegions(int i) {

        apiService.getRegions(i).enqueue(new Callback<Regions>() {
            @Override
            public void onResponse(@NonNull Call<Regions> call, @NonNull Response<Regions> response) {
                try {
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        regionsTxt = new ArrayList<>();
                        regionsId = new ArrayList<>();
                        if (getActivity() != null){
                            regionsTxt.add(getActivity().getString(R.string.choose_region));
                        }
                        regionsId.add(0);
                        int pos = 0;

                        for (int i = 0; i < response.body().getData().getData().size(); i++) {
                            regionsTxt.add(response.body().getData().getData().get(i).getName());
                            regionsId.add(response.body().getData().getData().get(i).getId());
                            if (response.body().getData().getData().get(i).getName().equals(client.getRegion().getName())) {
                                pos = i + 1;
                            }
                        }

                        ArrayAdapter<String> adapter;
                        if(ViewCompat.getLayoutDirection(convertView) == ViewCompat.LAYOUT_DIRECTION_RTL){ //Arabic
                            adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_right, regionsTxt);
                            EditProfileFragmentSpRegion.setAdapter(adapter);
                        }else if (ViewCompat.getLayoutDirection(convertView) == ViewCompat.LAYOUT_DIRECTION_LTR){ // English
                            adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_left, regionsTxt);
                            EditProfileFragmentSpRegion.setAdapter(adapter);
                        }

                        EditProfileFragmentSpRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    region_id = regionsId.get(i);
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });


                        EditProfileFragmentSpRegion.setSelection(pos);
                        dismissProgressDialog();

                    } // if response == 0

                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(@NonNull Call<Regions> call, @NonNull Throwable t) {
                //
            }
        });
    }

    private void addImage() {
        Action<ArrayList<AlbumFile>> action = new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                // TODO accept the result.

                ImagesFiles.clear();
                ImagesFiles.addAll(result);

                onLoadImageFromUrl(EditProfileFragmentCivProfilePicture, ImagesFiles.get(0).getPath(), getContext());

            }
        };

        openAlbum(1, getActivity(), ImagesFiles, action);

        imageFlag = true;
    }

    @OnClick({R.id.Edit_Profile_Fragment_Btn_Adjust, R.id.Lin, R.id.Edit_Profile_Fragment_Civ_ProfilePicture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Edit_Profile_Fragment_Btn_Adjust:
                onCheck();
                break;
            case R.id.Lin:
                disappearKeypad(getActivity(), view);
                break;
            case R.id.Edit_Profile_Fragment_Civ_ProfilePicture:
                addImage();
                break;
        }


    }

    private void onCheck() {
        String email = EditProfileFragmentEtEmail.getText().toString();
        String phone = EditProfileFragmentEtPhone.getText().toString();
        //String name = EditProfileFragmentEtName.getText().toString();

//        if (ImagesFiles.size() == 0
//                && name.equals(client.getName())
//                && email.equals(client.getEmail())
//                && phone.equals(client.getPhone())
//                && region_id == client.getRegion().getId()) {
//            return;
//        }

        if(!imageFlag &&
                EditProfileFragmentEtName.getText().toString().isEmpty() &&
                EditProfileFragmentEtEmail.toString().isEmpty() &&
                EditProfileFragmentEtPhone.getText().toString().isEmpty() &&
                EditProfileFragmentSpCity.getSelectedItemPosition() == 0 &&
                EditProfileFragmentSpRegion.getSelectedItemPosition() == 0){

            showToastMessage(getActivity(), getString(R.string.enter_inf));

        }else if(!imageFlag ||
                EditProfileFragmentEtName.getText().toString().isEmpty() ||
                EditProfileFragmentEtEmail.toString().isEmpty() ||
                EditProfileFragmentEtPhone.getText().toString().isEmpty() ||
                EditProfileFragmentSpCity.getSelectedItemPosition() == 0 ||
                EditProfileFragmentSpRegion.getSelectedItemPosition() == 0){

            if(!imageFlag){
                EditProfileFragmentCivProfilePicture.setBorderWidth(5);
                EditProfileFragmentCivProfilePicture.setBorderColor(Color.RED);
                //showToastMessage(getActivity(), getString(R.string.choose_image));
            }

            if (EditProfileFragmentEtName.getText().toString().isEmpty()){
                EditProfileFragmentEtName.setError(getString(R.string.register_client_et_name_error));
            }


            if (EditProfileFragmentEtEmail.getText().toString().isEmpty()){
                EditProfileFragmentEtEmail.setError(getString(R.string.enter_email));
            }

            if (!EditProfileFragmentEtEmail.getText().toString().equalsIgnoreCase("")){
                if (!setEmailValidation(getActivity(), email)) {
                    //showToastMessage(getActivity(), getString(R.string.invalid_Email));
                    EditProfileFragmentEtEmail.setError(getString(R.string.invalid_Email));
                    return;
                }
            }

            if (EditProfileFragmentEtPhone.getText().toString().isEmpty()){
                EditProfileFragmentEtPhone.setError(getString(R.string.register_client_et_phone_error));
            }

            if (!EditProfileFragmentEtPhone.getText().toString().equalsIgnoreCase("")){
                if (phone.length() != 11) {
                    //showToastMessage(getActivity(), getString(R.string.phoneLengh));
                    EditProfileFragmentEtPhone.setError(getString(R.string.phoneLengh));
                    return;
                }
            }

            if(EditProfileFragmentSpCity.getSelectedItemPosition() == 0){
                EditProfileFragmentSpCity.setBackgroundResource(R.drawable.shape_edit_text_rounded_gray_error);
                showToastMessage(getActivity(), getString(R.string.enter_city));
            }

            if(EditProfileFragmentSpRegion.getSelectedItemPosition() == 0){
                EditProfileFragmentSpRegion.setBackgroundResource(R.drawable.shape_edit_text_rounded_gray_error);
                showToastMessage(getActivity(), getString(R.string.enter_region));
            }

        }else {
            adjustInformation();
        }
    }

    private void adjustInformation() {
        String name = EditProfileFragmentEtName.getText().toString();
        String email = EditProfileFragmentEtEmail.getText().toString();
        String phone = EditProfileFragmentEtPhone.getText().toString();

        if (InternetState.isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.please_wait));
            if (ImagesFiles.size() == 0) {
                apiService.editClientProfilewithoutPhoto(convertToRequestBody(client.getApiToken())
                        , convertToRequestBody(name)
                        , convertToRequestBody(email)
                        , convertToRequestBody(phone)
                        , convertToRequestBody(String.valueOf(region_id)))
                        .enqueue(new Callback<ClientLogin>() {
                            @Override
                            public void onResponse(@NonNull Call<ClientLogin> call, @NonNull Response<ClientLogin> response) {
                                try {
                                    dismissProgressDialog();

                                    assert response.body() != null;
                                    if (response.body().getStatus() == 1) {
                                        Client user = response.body().getData().getUser();
                                        user.setApiToken(client.getApiToken());
                                        saveClientData(getActivity(), user);
                                        disappearKeypad(getActivity(), getView());
                                        showSuccessToastMessage(getActivity(), response.body().getMsg());

                                    } else {
                                        showToastMessage(getActivity(), response.body().getMsg());

                                    }

                                } catch (Exception e) {
                                    //
                                }

                            }

                            @Override
                            public void onFailure(@NonNull Call<ClientLogin> call, @NonNull Throwable t) {
                                dismissProgressDialog();
                                showToastMessage(getActivity(), getString(R.string.error));
                            }
                        });

            } else { //if (ImagesFiles.size() != 0)
                apiService.editClientProfile(convertToRequestBody(client.getApiToken())
                        , convertFileToMultipart(ImagesFiles.get(0).getPath(), "profile_image")
                        , convertToRequestBody(name)
                        , convertToRequestBody(email)
                        , convertToRequestBody(phone)
                        , convertToRequestBody(String.valueOf(region_id)))
                        .enqueue(new Callback<ClientLogin>() {
                            @Override
                            public void onResponse(@NonNull Call<ClientLogin> call, @NonNull Response<ClientLogin> response) {
                                try {
                                    assert response.body() != null;
                                    if (response.body().getStatus() == 1) {

                                        Client user = response.body().getData().getUser();
                                        user.setApiToken(client.getApiToken());
                                        saveClientData(getActivity(), user);
                                        disappearKeypad(getActivity(), getView());
                                        showSuccessToastMessage(getActivity(), response.body().getMsg());
                                    } else {
                                        dismissProgressDialog();
                                        showToastMessage(getActivity(), response.body().getMsg());

                                    }

                                } catch (Exception e) {
                                    //
                                }
                                dismissProgressDialog();

                            }

                            @Override
                            public void onFailure(@NonNull Call<ClientLogin> call, @NonNull Throwable t) {
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

    @Override
    public void onBack() {
        replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                , homeActivity.restaurantsList);
    }

}
