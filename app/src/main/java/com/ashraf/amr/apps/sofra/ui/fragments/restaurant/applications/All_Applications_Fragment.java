package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.applications;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.ViewPagerAdapter;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class All_Applications_Fragment extends BaseFragment {


    @BindView(R.id.All_Applications_TabLayout)
    TabLayout AllApplicationsTabLayout;
    @BindView(R.id.All_Applications_ViewPager)
    ViewPager AllApplicationsViewPager;
    @BindView(R.id.All_Applications_Frame)
    FrameLayout AllApplicationsFrame;
    Unbinder unbinder;
    public Applications_Fragment newApplications, oldApplications, nowApplications;

    public All_Applications_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all__applications, container, false);
        unbinder = ButterKnife.bind(this, view);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        newApplications = new Applications_Fragment();
        newApplications.Type = "pending";
        newApplications.allApplications = this;

        nowApplications = new Applications_Fragment();
        nowApplications.Type = "current";
        nowApplications.allApplications = this;

        oldApplications = new Applications_Fragment();
        oldApplications.Type = "completed";
        oldApplications.allApplications = this;

        viewPagerAdapter.addPager(newApplications, " طلبات جديدة");
        viewPagerAdapter.addPager(nowApplications, " طلبات حالية");
        viewPagerAdapter.addPager(oldApplications, " طلبات سابقة");
        AllApplicationsViewPager.setAdapter(viewPagerAdapter);
        AllApplicationsTabLayout.setupWithViewPager(AllApplicationsViewPager);

        setUpActivity();
        setUpHomeActivity();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                , homeActivity.categoriesFragment);
    }
}
