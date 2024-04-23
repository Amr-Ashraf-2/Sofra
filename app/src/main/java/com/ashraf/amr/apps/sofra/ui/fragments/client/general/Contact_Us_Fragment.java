package com.ashraf.amr.apps.sofra.ui.fragments.client.general;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.general.MoreFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.LoadData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;
import static com.ashraf.amr.apps.sofra.helper.Validation.setEmailValidation;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.backBtn;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Contact_Us_Fragment extends BaseFragment {


    @BindView(R.id.Contact_Us_Fragment_Et_Name)
    EditText ContactUsFragmentEtName;
    @BindView(R.id.Contact_Us_Fragment_Et_Email)
    EditText ContactUsFragmentEtEmail;
    @BindView(R.id.Contact_Us_Fragment_Et_Phone)
    EditText ContactUsFragmentEtPhone;
    @BindView(R.id.Contact_Us_Fragment_Et_Content)
    EditText ContactUsFragmentEtContent;
    @BindView(R.id.Contact_Us_Fragment_Rb_Complaint)
    RadioButton ContactUsFragmentRbComplaint;
    @BindView(R.id.Contact_Us_Fragment_Rb_Suggestion)
    RadioButton ContactUsFragmentRbSuggestion;
    @BindView(R.id.Contact_Us_Fragment_Rb_Enquiry)
    RadioButton ContactUsFragmentRbEnquiry;
    @BindView(R.id.Contact_Us_Fragment_Btn_send)
    Button ContactUsFragmentBtnSend;
    @BindView(R.id.Lin)
    LinearLayout Lin;
    Unbinder unbinder;
    private ApiServices apiService;


    public Contact_Us_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact__us, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);

        if (navBar != null){
            navBar.setVisibility(View.GONE);
        }

        if (LoadData(getActivity(), USER_TYPE).equals(CLIENT)) {
            navBar.setVisibility(View.GONE);
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity()!=null){
                        MoreFragment moreFragment = new MoreFragment();
                        replaceFragment(getActivity().getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, moreFragment);
                    }
                }
            });
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Contact_Us_Fragment_Rb_Complaint, R.id.Contact_Us_Fragment_Rb_Suggestion, R.id.Contact_Us_Fragment_Rb_Enquiry, R.id.Contact_Us_Fragment_Btn_send, R.id.Lin})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.Contact_Us_Fragment_Rb_Complaint:
                ContactUsFragmentRbSuggestion.setChecked(false);
                ContactUsFragmentRbEnquiry.setChecked(false);
                break;
            case R.id.Contact_Us_Fragment_Rb_Suggestion:
                ContactUsFragmentRbComplaint.setChecked(false);
                ContactUsFragmentRbEnquiry.setChecked(false);
                break;
            case R.id.Contact_Us_Fragment_Rb_Enquiry:
                ContactUsFragmentRbComplaint.setChecked(false);
                ContactUsFragmentRbSuggestion.setChecked(false);
                break;
            case R.id.Contact_Us_Fragment_Btn_send:
                call();
                break;
            case R.id.Lin:
                break;
        }
    }

    private void call() {
        String type = "";
        if (ContactUsFragmentRbSuggestion.isChecked()) {
            type = "suggestion";
        }
        if (ContactUsFragmentRbComplaint.isChecked()) {
            type = "complaint ";

        }
        if (ContactUsFragmentRbEnquiry.isChecked()) {
            type = "inquiry";

        }

        String name = ContactUsFragmentEtName.getText().toString();
        String email = ContactUsFragmentEtEmail.getText().toString();
        String phone = ContactUsFragmentEtPhone.getText().toString();
        String content = ContactUsFragmentEtContent.getText().toString();

        if (name.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.your_name));

        } else if (email.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.enter_email));

        } else if (!setEmailValidation(getActivity(), email)) {
            showToastMessage(getActivity(), getResources().getString(R.string.invalid_Email));

        } else if (phone.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.enter_phone));

        } else if (phone.length() != 11) {
            showToastMessage(getActivity(), getString(R.string.passwordLengh));
        } else if (content.isEmpty()) {
            showToastMessage(getActivity(), getResources().getString(R.string.enter_your_message));

        } else if (type.equals("")) {
            showToastMessage(getActivity(), getResources().getString(R.string.enter_your_type));

        } else {
            if (InternetState.isConnected(getActivity())) {
                showProgressDialog(getActivity(), getString(R.string.waiit));
                apiService.contactUs(name, email, phone, type, content)
                        .enqueue(new Callback<GeneralResponse>() {

                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                try {
                                    dismissProgressDialog();
                                    disappearKeypad(getActivity(), getView());
                                    if (response.body().getStatus() == 1) {
                                        showSuccessToastMessage(getActivity(), response.body().getMsg());
                                        ContactUsFragmentEtName.setText("");
                                        ContactUsFragmentEtEmail.setText("");
                                        ContactUsFragmentEtPhone.setText("");
                                        ContactUsFragmentEtContent.setText("");


                                    } else {
                                        dismissProgressDialog();
                                        showToastMessage(getActivity(), response.body().getMsg());

                                    }
                                } catch (Exception e) {

                                }

                            }

                            @Override
                            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                dismissProgressDialog();
                                disappearKeypad(getActivity(), getView());
                                showToastMessage(getActivity(), getString(R.string.error));
                            }
                        });

            }
        }


    }

}
