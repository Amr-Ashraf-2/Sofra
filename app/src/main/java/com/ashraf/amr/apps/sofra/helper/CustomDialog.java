package com.ashraf.amr.apps.sofra.helper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;


import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomDialog extends Dialog {



    @BindView(R.id.Dialog_Tv_Dialog_Title)
    TextView DialogTvDialogTitle;
    @BindView(R.id.Dialog_Ll_Dialog_Ok)
    LinearLayout DialogLlDialogOk;
    @BindView(R.id.Dialog_Ll_Dialog_Cancel)
    LinearLayout DialogLlDialogCancel;

    private boolean Visible;
    private Activity activity;
    private Context context;
    private String Title;
    private int Icon;
    private View.OnClickListener ok, cancel;
    private boolean Cancelable;

    public CustomDialog(Context context, Activity activity, String title,
                        View.OnClickListener ok, View.OnClickListener cancel, boolean Visible, boolean Cancelable) {
        super(context);
        this.activity = activity;
        this.context = context;
        Title = title;
        this.ok = ok;
        this.cancel = cancel;
        this.Visible = Visible;
        this.Cancelable = Cancelable;
        onCreate();
    }


    public void onCreate() {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog);
            ButterKnife.bind(this);

            CustomDialog.this.setCancelable(Cancelable);

            if (ok == null) {
                DialogLlDialogOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
            } else {
                DialogLlDialogOk.setOnClickListener(ok);
            }

            if (cancel == null) {
                DialogLlDialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
            } else {
                DialogLlDialogCancel.setOnClickListener(cancel);
            }

            DialogTvDialogTitle.setText(Title);


            if (Visible) {
                DialogLlDialogCancel.setVisibility(View.VISIBLE);
            } else {
                DialogLlDialogCancel.setVisibility(View.GONE);
            }
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            show();

        } catch (Exception e) {

        }

    }


}