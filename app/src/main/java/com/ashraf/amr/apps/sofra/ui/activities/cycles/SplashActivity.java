package com.ashraf.amr.apps.sofra.ui.activities.cycles;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.helper.HelperClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.REMEMBER;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.RESTAURANT;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.SaveData;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.USER_TYPE;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.changeLang;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.Splash2_Activity_Btn_FoodOrder)
    Button Splash2ActivityBtnFoodOrder;
    @BindView(R.id.Splash2_Activity_Btn_FoodSell)
    Button Splash2ActivityBtnFoodSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //changeLang(SplashActivity.this, "ar");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash2);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.Splash2_Activity_Btn_FoodOrder, R.id.Splash2_Activity_Btn_FoodSell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Splash2_Activity_Btn_FoodOrder:
                SaveData(SplashActivity.this, USER_TYPE, "");
                SaveData(SplashActivity.this, USER_TYPE, CLIENT);
                Intent i = new Intent(SplashActivity.this, Navigation_Activity.class);
                startActivity(i);
                finish();

                break;

            case R.id.Splash2_Activity_Btn_FoodSell:
                SaveData(SplashActivity.this, USER_TYPE, "");
                SaveData(SplashActivity.this, USER_TYPE, RESTAURANT);

                if ((SharedPreferencesManger.getUserName(SplashActivity.this).equals(REMEMBER))) {
                    Intent intent = new Intent(SplashActivity.this, Navigation_Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        HelperClass.changeLang(this, "ar");
//        super.onConfigurationChanged(newConfig);
//    }

}
