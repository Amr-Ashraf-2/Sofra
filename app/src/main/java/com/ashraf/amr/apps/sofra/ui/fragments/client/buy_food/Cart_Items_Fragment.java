package com.ashraf.amr.apps.sofra.ui.fragments.client.buy_food;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.CarItemsAdapter;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomDao;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomManger;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;
import com.ashraf.amr.apps.sofra.ui.activities.cycles.ProfileActivity;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTLOGINTYPE;
import static com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger.CLIENTREMEMBER;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.setInitRecyclerViewAsLinearLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cart_Items_Fragment extends BaseFragment {

    @BindView(R.id.Cart_Items_Fragment_Rv)
    RecyclerView CartItemsFragmentRv;
    @BindView(R.id.Cart_Items_Fragment_Tv_TotlCost)
    TextView CartItemsFragmentTvTotlCost;
    @BindView(R.id.Cart_Items_Fragment_Btn_confirm)
    Button CartItemsFragmentBtnConfirm;
    @BindView(R.id.Cart_Items_Fragment_Btn_AddMore)
    Button CartItemsFragmentBtnAddMore;
    Unbinder unbinder;

    public boolean home = false;
    public List<ProductsData> foodItems = new ArrayList<>();
    private CarItemsAdapter carItemsAdapter;
    private LinearLayoutManager mLayoutManager;
    private RoomManger roomManger;
    private RoomDao roomDao;

    public Cart_Items_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart__items, container, false);
        unbinder = ButterKnife.bind(this, view);
        setInitRecyclerViewTool();

        if (home) {
            CartItemsFragmentBtnAddMore.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Cart_Items_Fragment_Btn_confirm, R.id.Cart_Items_Fragment_Btn_AddMore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Cart_Items_Fragment_Btn_confirm:
                if (foodItems.size() != 0) {
                    if (SharedPreferencesManger.getClientName(getActivity()).equals(CLIENTREMEMBER)) {
                        Client_Order_Details_Fragment client_order_details_fragment = new Client_Order_Details_Fragment();
                        client_order_details_fragment.foodItems = foodItems;
                        replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                                , client_order_details_fragment);
                    } else {
                        SharedPreferencesManger.setClientLooginType(getActivity(), CLIENTLOGINTYPE);
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        startActivity(intent);
                    }
                } else {

                }

                break;
            case R.id.Cart_Items_Fragment_Btn_AddMore:
                onBack();
                onBack();
                break;
        }
    }

    private void setInitRecyclerViewTool() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        setInitRecyclerViewAsLinearLayoutManager(getActivity(), CartItemsFragmentRv, mLayoutManager);
        getAllItem(getActivity());
    }

    private void getAllItem(Context context) {
        carItemsAdapter = new CarItemsAdapter(getActivity(), getActivity(), foodItems, CartItemsFragmentTvTotlCost);
        CartItemsFragmentRv.setAdapter(carItemsAdapter);
        carItemsAdapter.notifyDataSetChanged();
    }
}
