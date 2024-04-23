package com.ashraf.amr.apps.sofra.ui.activities;

import android.support.v7.app.AppCompatActivity;

import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;

public class BaseActivity extends AppCompatActivity {

    public BaseFragment baseFragment;

    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }
}

