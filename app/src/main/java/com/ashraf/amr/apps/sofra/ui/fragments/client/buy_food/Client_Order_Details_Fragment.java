package com.ashraf.amr.apps.sofra.ui.fragments.client.buy_food;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.api.RetrofitClient;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomDao;
import com.ashraf.amr.apps.sofra.data.local.Room.RoomManger;
import com.ashraf.amr.apps.sofra.data.local.SharedPreferences.SharedPreferencesManger;
import com.ashraf.amr.apps.sofra.data.model.client.clientlogin.Client;
import com.ashraf.amr.apps.sofra.data.model.client.listOfOrders.AddNewOrder;
import com.ashraf.amr.apps.sofra.data.model.restaurant.myproducts.ProductsData;
import com.ashraf.amr.apps.sofra.helper.HelperClass;
import com.ashraf.amr.apps.sofra.helper.InternetState;
import com.ashraf.amr.apps.sofra.ui.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.dismissProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.replaceFragment;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showProgressDialog;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showSuccessToastMessage;
import static com.ashraf.amr.apps.sofra.helper.HelperClass.showToastMessage;



public class Client_Order_Details_Fragment extends BaseFragment {


    @BindView(R.id.Client_OrderDetails_Fragment_Et_Notes)
    EditText ClientOrderDetailsFragmentEtNotes;
    @BindView(R.id.Client_OrderDetails_Fragment_Tv_Address)
    EditText ClientOrderDetailsFragmentTvAddress;
    @BindView(R.id.Client_OrderDetails_Fragment_Iv_Cash)
    RadioButton ClientOrderDetailsFragmentIvCash;
    @BindView(R.id.Client_OrderDetails_Fragment_Iv_Online)
    RadioButton ClientOrderDetailsFragmentIvOnline;
    @BindView(R.id.Client_OrderDetails_Fragment_Tv_Total)
    TextView ClientOrderDetailsFragmentTvTotal;
    @BindView(R.id.Client_OrderDetails_Fragment_Tv_DelieveryCost)
    TextView ClientOrderDetailsFragmentTvDelieveryCost;
    @BindView(R.id.Client_OrderDetails_Fragment_Tv_TotalAmount)
    TextView ClientOrderDetailsFragmentTvTotalAmount;
    Unbinder unbinder;

    public List<ProductsData> foodItems = new ArrayList<>();
    private ApiServices apiService;
    private Client client;
    private RoomDao roomDao;
    private RoomManger roomManger;

    public Client_Order_Details_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client__order__details, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getClient().create(ApiServices.class);

        client = SharedPreferencesManger.loadClientData(getActivity());
        roomManger = RoomManger.getInstance(getActivity());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Create_Order_Btn_Create
            , R.id.Client_OrderDetails_Fragment_Rel_SubView
            , R.id.Client_OrderDetails_Fragment_Iv_Cash
            , R.id.Client_OrderDetails_Fragment_Iv_Online

    })
    public void onViewClicked(View view) {
        HelperClass.disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.Create_Order_Btn_Create:
                order();
                break;
            case R.id.Client_OrderDetails_Fragment_Rel_SubView:
                break;
            case R.id.Client_OrderDetails_Fragment_Iv_Cash:
                ClientOrderDetailsFragmentIvOnline.setChecked(false);
                break;
            case R.id.Client_OrderDetails_Fragment_Iv_Online:
                ClientOrderDetailsFragmentIvCash.setChecked(false);
                break;
        }

    }

    private void order() {
        String address = ClientOrderDetailsFragmentTvAddress.getText().toString();
        String note = ClientOrderDetailsFragmentEtNotes.getText().toString();

        if (address.equals("")) {
            showToastMessage(getActivity() ,"ادخل عنوان تسليم الطلب");
            return;
        }

        String payment = "1";

        if (!ClientOrderDetailsFragmentIvCash.isChecked()
                && !ClientOrderDetailsFragmentIvOnline.isChecked()) {
            showToastMessage(getActivity() ,"اختر طريقة الدفع");
            return;
        } else {
            if (ClientOrderDetailsFragmentIvCash.isChecked()) {
                payment = "1";
            }

            if (ClientOrderDetailsFragmentIvOnline.isChecked()) {
                payment = "2";
            }
        }

        List<String> itemId = new ArrayList<>();
        List<String> itemCount = new ArrayList<>();
        List<String> itemNote = new ArrayList<>();


        for (int i = 0; i < foodItems.size(); i++) {
            itemId.add(String.valueOf(foodItems.get(0).getId()));
            itemCount.add(foodItems.get(0).getCounter());
            if (foodItems.get(0).getNote() == null) {
                itemNote.add(getString(R.string.nothing));
            } else {
                itemNote.add(foodItems.get(0).getNote());
            }
        }

        if (InternetState.isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.waiit));

            apiService.addNewOrder(foodItems.get(0).getRestaurantId(), note, address, payment, client.getPhone()
                    , client.getName(), client.getApiToken(), itemId, itemCount, itemNote).enqueue(new Callback<AddNewOrder>() {
                @Override
                public void onResponse(Call<AddNewOrder> call, Response<AddNewOrder> response) {
                    dismissProgressDialog();
                    if (response.body().getStatus() == 1) {
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                roomDao = roomManger.roomDao();
                                roomDao.deleteAllItemToCar();
                            }

                        });
                        showSuccessToastMessage(getActivity(), response.body().getMsg());
                        replaceFragment(baseActivity.getSupportFragmentManager(), R.id.Home_Cycle_Activity_FrameLayout
                                , homeActivity.restaurantsList);
                    } else {
                        showToastMessage(getActivity(), response.body().getMsg());

                    }

                }

                @Override
                public void onFailure(Call<AddNewOrder> call, Throwable t) {
                    dismissProgressDialog();
                    Log.d(TAG, "onFailure: ");
                }
            });

        } else {
            showToastMessage(getActivity(), getString(R.string.no_internet));
        }

    }

    @Override
    public void onBack() {
        super.onBack();
    }
}
