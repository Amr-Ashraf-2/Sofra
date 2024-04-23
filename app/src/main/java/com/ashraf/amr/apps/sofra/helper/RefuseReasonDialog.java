package com.ashraf.amr.apps.sofra.helper;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myrequests.MyRequestsData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.ui.fragments.restaurant.applications.All_Applications_Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.disappearKeypad;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;


public class RefuseReasonDialog extends Dialog {

    @BindView(R.id.Tv_reason)
    TextView TvReason;
    @BindView(R.id.Dialog_RefuseReason_Et_RefuseReason)
    EditText DialogRefuseReasonEtRefuseReason;
    @BindView(R.id.Dialog_RefuseReason_Btn_Refuse)
    Button DialogRefuseReasonBtnRefuse;
    @BindView(R.id.Add_Comment_Dialog_Relative)
    RelativeLayout AddCommentDialogRelative;

    private All_Applications_Fragment allApplicationsFragment;
    private List<MyRequestsData> requestsDataList = new ArrayList<>();
    private int position;
    public Activity activity;
    public Client client;
    public String apiToken = "";
    public String reson;
    public ApiServices apiService;
    public RestaurantData user;
    public int order_id;

    public RefuseReasonDialog(Activity activity, int order_id, String apiToken
            , All_Applications_Fragment allApplicationsFragment, int position) {
        super(activity);
        this.activity = activity;
        this.order_id = order_id;
        this.apiToken = apiToken;
        this.client = client;
        this.reson = reson;
        this.user = user;
        this.allApplicationsFragment = allApplicationsFragment;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.refuse_reason_dialog);
        ButterKnife.bind(this);

    }


    private void addReason() {
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(activity);

        String reson = DialogRefuseReasonEtRefuseReason.getText().toString();
        if (InternetState.isConnected(activity)) {
            showProgressDialog(activity, activity.getString(R.string.waiit));
            apiService.deletorder(order_id, apiToken, reson).enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    dismissProgressDialog();
                    try {

                        if (response.body().getStatus() == 1) {
                            allApplicationsFragment.nowApplications.requestsDataList.get(position).setState("rejected");
                            allApplicationsFragment.oldApplications.requestsDataList.add(0, allApplicationsFragment.nowApplications.requestsDataList.get(position));
                            allApplicationsFragment.oldApplications.applicationsAdapter.notifyDataSetChanged();
                            allApplicationsFragment.nowApplications.requestsDataList.remove(position);
                            allApplicationsFragment.nowApplications.applicationsAdapter.notifyDataSetChanged();
                            showSuccessToastMessage(activity, "تم الغاء الطلب");
                            dismiss();
                        } else {
                            showToastMessage(activity, "حدث خطأ ما");
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

        } else {
            showToastMessage(activity, activity.getString(R.string.no_internet));
        }

    }

    @OnClick({R.id.Dialog_RefuseReason_Btn_Refuse, R.id.Add_Comment_Dialog_Relative})
    public void onViewClicked(View view) {
        disappearKeypad(activity, view);
        switch (view.getId()) {
            case R.id.Dialog_RefuseReason_Btn_Refuse:
                addReason();
                break;
            case R.id.Add_Comment_Dialog_Relative:
                break;
        }
    }
}