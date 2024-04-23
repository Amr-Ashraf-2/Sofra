package com.ashraf.amr.apps.sofra.ui.fragments.client.homeCycle;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.ViewPagerAdapter;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;
import com.ashraf.amr.apps.sofra.ui.fragments.client.my_orders.OrdersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class My_Orders_Fragment extends BaseFragment {


    @BindView(R.id.My_Orders_Fragment_TabLayout)
    TabLayout MyOrdersFragmentTabLayout;
    @BindView(R.id.My_Orders_Fragment_ViewPager)
    ViewPager MyOrdersFragmentViewPager;
    Unbinder unbinder;
    public OrdersFragment newOrders, currentOrders, oldOrders;

    public My_Orders_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my__orders, container, false);
        unbinder = ButterKnife.bind(this, view);
        ViewPagerAdapter vadapter = new ViewPagerAdapter(getChildFragmentManager());


        newOrders = new OrdersFragment();
        newOrders.state = "pending";
        newOrders.my_orders_fragment = this;

        currentOrders = new OrdersFragment();
        currentOrders.state = "current";
        currentOrders.my_orders_fragment = this;

        oldOrders = new OrdersFragment();
        oldOrders.state = "completed";
        oldOrders.my_orders_fragment = this;


        vadapter.addPager(newOrders, getString(R.string.new_orders));
        vadapter.addPager(currentOrders, getString(R.string.current_orders));
        vadapter.addPager(oldOrders, getString(R.string.old_orders));

        MyOrdersFragmentViewPager.setAdapter(vadapter);
        MyOrdersFragmentTabLayout.setupWithViewPager(MyOrdersFragmentViewPager);
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
                , homeActivity.restaurantsList);
    }
}
