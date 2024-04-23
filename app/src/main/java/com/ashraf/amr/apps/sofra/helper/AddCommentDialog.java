package com.ashraf.amr.apps.sofra.helper;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.general.addcomment.AddComment;

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


public class AddCommentDialog extends Dialog {

    @BindView(R.id.Tv_Choose)
    TextView TvChoose;
    @BindView(R.id.Dialog_Comments_Iv_Love)
    ImageView DialogCommentsIvLove;
    @BindView(R.id.Dialog_Comments_Iv_Happy)
    ImageView DialogCommentsIvHappy;
    @BindView(R.id.Dialog_Comments_Iv_Good)
    ImageView DialogCommentsIvGood;
    @BindView(R.id.Dialog_Comments_Iv_Sad)
    ImageView DialogCommentsIvSad;
    @BindView(R.id.Dialog_Comments_Iv_Angry)
    ImageView DialogCommentsIvAngry;
    @BindView(R.id.Emojs)
    LinearLayout Emojs;
    @BindView(R.id.Dialog_Comments_Et_AddComment)
    EditText DialogCommentsEtAddComment;
    @BindView(R.id.Dialog_Comments_Iv_State)
    ImageView DialogCommentsIvState;
    @BindView(R.id.Dialog_Comments_Btn_Add)
    Button DialogCommentsBtnAdd;
    @BindView(R.id.Lin)
    LinearLayout Lin;
    @BindView(R.id.Dialog_Comments_Btn_Cancel)
    Button DialogCommentsBtnCancel;
    @BindView(R.id.Add_Comment_Dialog_Relative)
    RelativeLayout AddCommentDialogRelative;
    private Activity activity;
    private String rate = "0";
    private int restaurant_id;
    private Client client;
    private String apiToken = "";
    public String comment;
    private ApiServices apiService;

    public AddCommentDialog(Activity activity, int restaurant_id, String apiToken) {
        super(activity);
        this.activity = activity;
        this.restaurant_id = restaurant_id;
        this.apiToken = apiToken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.diaog_add_comment);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.Dialog_Comments_Iv_Love,
            R.id.Dialog_Comments_Iv_Happy,
            R.id.Dialog_Comments_Iv_Good,
            R.id.Dialog_Comments_Iv_Sad,
            R.id.Dialog_Comments_Iv_Angry,
            R.id.Dialog_Comments_Btn_Add,
            R.id.Dialog_Comments_Btn_Cancel,
            R.id.Add_Comment_Dialog_Relative})
    public void onViewClicked(View view) {
        disappearKeypad(activity, view);
        switch (view.getId()) {
            case R.id.Dialog_Comments_Iv_Love:
                rate = "1";
                DialogCommentsIvState.setImageResource(R.drawable.ic_in_love_regular);
                break;
            case R.id.Dialog_Comments_Iv_Happy:
                rate = "2";
                DialogCommentsIvState.setImageResource(R.drawable.ic_laugh_beam_regular);
                break;
            case R.id.Dialog_Comments_Iv_Good:
                rate = "3";
                DialogCommentsIvState.setImageResource(R.drawable.ic_smile_regular);
                break;
            case R.id.Dialog_Comments_Iv_Sad:
                rate = "4";
                DialogCommentsIvState.setImageResource(R.drawable.ic_sad_regular);
                break;
            case R.id.Dialog_Comments_Iv_Angry:
                rate = "5";
                DialogCommentsIvState.setImageResource(R.drawable.ic_angry_regular);
                break;
            case R.id.Dialog_Comments_Btn_Add:
                addComment();
                break;
            case R.id.Dialog_Comments_Btn_Cancel:
                dismiss();
                break;
            case R.id.Add_Comment_Dialog_Relative:

                break;
        }
    }

    private void addComment() {

        if(rate.equals("0") || DialogCommentsEtAddComment.getText().toString().isEmpty()) {

            if (rate.equals("0") && DialogCommentsEtAddComment.getText().toString().isEmpty()){
                Toast.makeText(activity, activity.getString(R.string.add_restaurant_review_rating_comment), Toast.LENGTH_SHORT).show();
            return;
            }

            if (rate.equals("0")){
                Toast.makeText(activity, activity.getString(R.string.add_restaurant_review_rating), Toast.LENGTH_SHORT).show();
            }

            if (DialogCommentsEtAddComment.getText().toString().isEmpty()){
                Toast.makeText(activity, activity.getString(R.string.add_restaurant_review_et_comment), Toast.LENGTH_SHORT).show();
            }

        }else {
            addReview();
        }
    }

    private void addReview(){
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        String comment = DialogCommentsEtAddComment.getText().toString();
        if (InternetState.isConnected(activity)) {
            showProgressDialog(activity, activity.getString(R.string.waiit));
            apiService.addComment(apiToken, comment, restaurant_id, rate).enqueue(new Callback<AddComment>() {
                @Override
                public void onResponse(@NonNull Call<AddComment> call, @NonNull Response<AddComment> response) {
                    dismissProgressDialog();
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        showSuccessToastMessage(activity, response.body().getMsg());
                    } else {

                        showToastMessage(activity, response.body().getMsg());
                    }
                    dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<AddComment> call, @NonNull Throwable t) {
                    dismissProgressDialog();
                    dismiss();
                    showToastMessage(activity, activity.getString(R.string.error));
                }
            });

        } else {
            dismiss();
            showToastMessage(activity, activity.getString(R.string.no_internet));
        }
    }

}