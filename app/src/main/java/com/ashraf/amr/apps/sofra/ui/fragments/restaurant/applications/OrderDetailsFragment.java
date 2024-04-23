package com.ashraf.amr.apps.sofra.ui.fragments.restaurant.applications;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.adapters.OrderProductsAdapter;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.client.listOfOrders.OrdersData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myrequests.MyRequestsData;
import com.ashraf.amr.apps.sofra.data.model.restaurant.restaurantlogin.RestaurantData;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ashraf.amr.apps.sofra.helper.GetTimeAgo.getDate;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.onLoadImageFromUrl;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends BaseFragment {


    public String type;
    public MyRequestsData restaurantOrdersDataList;
    @BindView(R.id.Order_Details_Fragment_Iv_Photo)
    CircleImageView OrderDetailsFragmentIvPhoto;
    @BindView(R.id.Order_Details_Fragment_Tv_RestaurantName)
    TextView OrderDetailsFragmentTvRestaurantName;
    @BindView(R.id.Order_Details_Fragment_Tv_Date)
    TextView OrderDetailsFragmentTvDate;
    @BindView(R.id.Order_Details_Fragment_Tv_Address)
    TextView OrderDetailsFragmentTvAddress;
    @BindView(R.id.Order_Details_Fragment_Rv_RequestDetails)
    RecyclerView OrderDetailsFragmentRvRequestDetails;
    @BindView(R.id.Order_Details_Fragment_Tv_RequestPrice)
    TextView OrderDetailsFragmentTvRequestPrice;
    @BindView(R.id.Order_Details_Fragment_Tv_DelieveryPrice)
    TextView OrderDetailsFragmentTvDelieveryPrice;
    @BindView(R.id.Order_Details_Fragment_Tv_Total)
    TextView OrderDetailsFragmentTvTotal;
    @BindView(R.id.Order_Details_Fragment_Tv_Payment_Method)
    TextView OrderDetailsFragmentTvPaymentMethod;
    Unbinder unbinder;
    private ApiServices apiService;
    private RestaurantData user;
    public OrdersData ordersDataList;

    private OrderProductsAdapter orderProductsAdapter;
    private List<ProductsData> productsDataList = new ArrayList<>();
    private String lang = "en";

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        if (type.equals("restaurant")) {
            restaurantOrderDetails();
        }else  if (type.equals("client")){

            clientOrderDetails();
        }

        return view;
    }

    private void clientOrderDetails() {
        onLoadImageFromUrl(OrderDetailsFragmentIvPhoto, ordersDataList.getRestaurant().getPhotoUrl(), getActivity());
        OrderDetailsFragmentTvRestaurantName.setText(ordersDataList.getRestaurant().getName());
        OrderDetailsFragmentTvDate.setText(getDate(ordersDataList.getCreatedAt(), new Locale(lang)));

        OrderDetailsFragmentTvAddress.setText("العنوان : " + ordersDataList.getAddress());
        OrderDetailsFragmentTvRequestPrice.setText("سعر الطلب : " + ordersDataList.getCost() + "ريال");
        OrderDetailsFragmentTvDelieveryPrice.setText("سعر التوصيل : " + ordersDataList.getDeliveryCost() + "ريال");
        OrderDetailsFragmentTvTotal.setText("الأجمالى : " + ordersDataList.getTotal() + "ريال");
        if (ordersDataList.getPaymentMethodId().equals("1")) {
            OrderDetailsFragmentTvPaymentMethod.setText("طريقة الدفع : " + "كاش");
        } else {
            OrderDetailsFragmentTvPaymentMethod.setText("طريقة الدفع : " + "اونلاين");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        if (productsDataList.size() == 0) {
            productsDataList.addAll(ordersDataList.getItems());

        }
        OrderProductsAdapter orderProductsAdapter =
                new OrderProductsAdapter(getActivity(), getActivity(), productsDataList);
        OrderDetailsFragmentRvRequestDetails.setLayoutManager(linearLayoutManager);
        OrderDetailsFragmentRvRequestDetails.setAdapter(orderProductsAdapter);
    }

    private void restaurantOrderDetails() {
        onLoadImageFromUrl(OrderDetailsFragmentIvPhoto, restaurantOrdersDataList.getClient().getPhotoUrl(), getActivity());
        OrderDetailsFragmentTvRestaurantName.setText(restaurantOrdersDataList.getClient().getName());
        OrderDetailsFragmentTvDate.setText(getDate(restaurantOrdersDataList.getCreatedAt(), new Locale(lang)));

        OrderDetailsFragmentTvAddress.setText("العنوان : " + restaurantOrdersDataList.getAddress());
        OrderDetailsFragmentTvRequestPrice.setText("سعر الطلب : " + restaurantOrdersDataList.getCost() + "ريال");
        OrderDetailsFragmentTvDelieveryPrice.setText("سعر التوصيل : " + restaurantOrdersDataList.getDeliveryCost() + "ريال");
        OrderDetailsFragmentTvTotal.setText("الأجمالى : " + restaurantOrdersDataList.getTotal() + "ريال");
        if (restaurantOrdersDataList.getPaymentMethodId().equals("1")) {
            OrderDetailsFragmentTvPaymentMethod.setText("طريقة الدفع : " + "كاش");
        } else {
            OrderDetailsFragmentTvPaymentMethod.setText("طريقة الدفع : " + "اونلاين");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        if (productsDataList.size() == 0) {
            productsDataList.addAll(restaurantOrdersDataList.getItems());

        }
        OrderProductsAdapter orderProductsAdapter =
                new OrderProductsAdapter(getActivity(), getActivity(), productsDataList);
        OrderDetailsFragmentRvRequestDetails.setLayoutManager(linearLayoutManager);
        OrderDetailsFragmentRvRequestDetails.setAdapter(orderProductsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
