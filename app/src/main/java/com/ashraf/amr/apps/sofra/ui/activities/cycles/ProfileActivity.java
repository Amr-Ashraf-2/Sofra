package com.ashraf.amr.apps.sofra.ui.activities.cycles;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.FrameLayout;

import com.ashraf.amr.apps.sofra.R;

import com.ashraf.amr.apps.sofra.ui.activities.BaseActivity;
import com.ashraf.amr.apps.sofra.ui.fragments.authCycle.Login_Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.Profile_Frame)
    FrameLayout ProfileFrame;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //changeLang(this, "ar");
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            Login_Fragment login = new Login_Fragment();
            replaceFragment(getSupportFragmentManager(), R.id.Profile_Frame, login);
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        HelperClass.changeLang(this, "ar");
//        super.onConfigurationChanged(newConfig);
//    }

}
