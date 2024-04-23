package com.ashraf.amr.apps.sofra.ui.fragments;

import android.support.v4.app.Fragment;


import com.ashraf.amr.apps.sofra.ui.activities.BaseActivity;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity;

public class BaseFragment extends Fragment {

    public BaseActivity baseActivity;
    public Navigation_Activity homeActivity;

    public void setUpActivity() {
        baseActivity = (BaseActivity) getActivity();
        baseActivity.baseFragment = this;
    }

    public void setUpHomeActivity() {
        try {
            homeActivity = (Navigation_Activity) getActivity();
        } catch (Exception e) {

        }
    }

    public void onBack() {

        baseActivity.superBackPressed();

    }

    @Override
    public void onStart() {
        super.onStart();
        setUpActivity();
        setUpHomeActivity();
    }

}




