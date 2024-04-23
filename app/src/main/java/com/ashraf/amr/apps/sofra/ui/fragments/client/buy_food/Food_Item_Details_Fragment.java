package com.ashraf.amr.apps.sofra.ui.fragments.client.buy_food;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomDao;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomManger;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Food_Item_Details_Fragment extends Fragment {


    @BindView(R.id.Food_Item_Details_Fragment_Iv_ProductPhoto)
    ImageView FoodItemDetailsFragmentIvProductPhoto;
    @BindView(R.id.Food_Item_Details_Fragment_Tv_Name)
    TextView FoodItemDetailsFragmentTvName;
    @BindView(R.id.Food_Item_Details_Fragment_Tv_Description)
    TextView FoodItemDetailsFragmentTvDescription;
    @BindView(R.id.Food_Item_Details_Fragment_Tv_Price)
    TextView FoodItemDetailsFragmentTvPrice;
    @BindView(R.id.Iv_Coins)
    ImageView IvCoins;
    @BindView(R.id.Food_Item_Details_Fragment_Tv_Time)
    TextView FoodItemDetailsFragmentTvTime;
    @BindView(R.id.Food_Item_Details_Fragment_Tv_Special)
    TextView FoodItemDetailsFragmentTvSpecial;
    @BindView(R.id.Food_Item_Details_Fragment_Et_Special)
    EditText FoodItemDetailsFragmentEtSpecial;
    @BindView(R.id.Tv_Quantity)
    TextView TvQuantity;
    @BindView(R.id.Food_Item_Details_Fragment_Btn_Increase)
    RelativeLayout FoodItemDetailsFragmentBtnIncrease;
    @BindView(R.id.Food_Item_Details_Fragment_tV_Quantity_Show)
    TextView FoodItemDetailsFragmentTVQuantityShow;
    @BindView(R.id.Food_Item_Details_Fragment_Btn_Decrease)
    RelativeLayout FoodItemDetailsFragmentBtnDecrease;
    @BindView(R.id.Food_Item_Details_Fragment_Iv_Add)
    RelativeLayout FoodItemDetailsFragmentIvAdd;
    Unbinder unbinder;
    public ProductsData foodItem;
    @BindView(R.id.fragment_client_restaurant_menu_item_tv_rial)
    TextView fragmentClientRestaurantMenuItemTvRial;
    private RoomDao roomDao;
    private RoomManger roomManger;
    private List<ProductsData> foodItems = new ArrayList<>();
    private Cart_Items_Fragment cartItemsFragment;

    public Food_Item_Details_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_item__details, container, false);
        unbinder = ButterKnife.bind(this, view);
        roomManger = RoomManger.getInstance(getActivity());

        setData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Food_Item_Details_Fragment_Btn_Increase, R.id.Food_Item_Details_Fragment_Btn_Decrease, R.id.Food_Item_Details_Fragment_Iv_Add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Food_Item_Details_Fragment_Btn_Increase:
                addToCount();
                break;
            case R.id.Food_Item_Details_Fragment_Btn_Decrease:
                removeToCount();
                break;
            case R.id.Food_Item_Details_Fragment_Iv_Add:

                addToCar(false);
                break;
        }
    }

    private void addToCar(final boolean delete) {

        Integer count = 0;

        if (!FoodItemDetailsFragmentTVQuantityShow.getText().toString().equals("")) {
            if (FoodItemDetailsFragmentTVQuantityShow.getText().toString().equals("0")) {
                return;
            }
            count = Integer.valueOf(FoodItemDetailsFragmentTVQuantityShow.getText().toString());
        } else {
            return;
        }

        foodItem.setCounter(String.valueOf(count));

        final Integer finalCount = count;
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                roomDao = roomManger.roomDao();

                foodItems = roomDao.getAllItem();

                ProductsData newProductData = foodItem;

                if (false) {
                    roomDao.deleteAllItemToCar();
                }

                boolean in = false;
                for (int i = 0; i < foodItems.size(); i++) {
                    if (foodItem.getId().equals(foodItems.get(i).getId())) {
                        in = true;
                        foodItems.get(i).setCounter(String.valueOf(finalCount));
                        roomDao.updateItemToCar(foodItems.get(i));
                        break;
                    }
                }


                if (!in) {
                    roomDao.insertItemToCar(newProductData);
                }

                cartItemsFragment = new Cart_Items_Fragment();
                cartItemsFragment.foodItems = roomDao.getAllItem();

                if (getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            assert getFragmentManager() != null;
                            replaceFragment(getFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout, cartItemsFragment);
                        }
                    });
                }

            }

        });
    }

    private void setData() {
        onLoadImageFromUrl(FoodItemDetailsFragmentIvProductPhoto, foodItem.getPhotoUrl(), getActivity());

        float priceAsFloat = Float.parseFloat(foodItem.getPrice());
        int priceAsInteger = (int) priceAsFloat;
        String getPrice = String.valueOf(priceAsInteger);

        if (foodItem.getPrice() == null) {
            fragmentClientRestaurantMenuItemTvRial.setVisibility(View.GONE);
        }
        FoodItemDetailsFragmentTvPrice.setText(getPrice);
        FoodItemDetailsFragmentTvName.setText(foodItem.getName());
        FoodItemDetailsFragmentTvDescription.setText(foodItem.getDescription());

        if (foodItem.getPreparingTime() != null){
            FoodItemDetailsFragmentTvTime.setText(getString(R.string.order_preparing_duration, foodItem.getPreparingTime()));
            FoodItemDetailsFragmentTvTime.setVisibility(View.VISIBLE);
        }else {
            FoodItemDetailsFragmentTvTime.setVisibility(View.GONE);
        }
    }

    private void addToCount() {
        int count = 0;
        if (!FoodItemDetailsFragmentTVQuantityShow.getText().toString().equals("")) {
            count = Integer.parseInt(FoodItemDetailsFragmentTVQuantityShow.getText().toString());
        }

        count = count + 1;
        FoodItemDetailsFragmentTVQuantityShow.setText(String.valueOf(count));
    }

    private void removeToCount() {
        Integer count = 0;
        if (!FoodItemDetailsFragmentTVQuantityShow.getText().toString().equals("")) {
            count = Integer.valueOf(FoodItemDetailsFragmentTVQuantityShow.getText().toString());
        }

        if (!count.equals(0)) {
            count = count - 1;
            FoodItemDetailsFragmentTVQuantityShow.setText(String.valueOf(count));
        }
    }


}
