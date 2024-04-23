package com.ashraf.amr.apps.sofra.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ashraf.amr.apps.sofra.R;
import com.ashraf.amr.apps.sofra.data.api.ApiServices;
import com.ashraf.amr.apps.sofra.data.model.GeneralResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;


import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static com.ashraf.amr.apps.sofra.ui.activities.cycles.Navigation_Activity.navBar;

/**
 * Created by Amr Ashraf on 22/08/2020.
 */

public class HelperClass extends Activity {

    private static ProgressDialog checkDialog;
    public static AlertDialog alertDialog;
    public static Snackbar snackbar;
    private static Animation slide_up;
    private static boolean isKeyboardShowing;


//    public static void setInitRecyclerViewAsGridLayoutManager(Activity activity, RecyclerView recyclerView, GridLayoutManager gridLayoutManager, int numberOfColumns) {
//        gridLayoutManager = new GridLayoutManager(activity, numberOfColumns);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(gridLayoutManager);
//    }

    public static void setInitRecyclerViewAsLinearLayoutManager(Context context, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static MultipartBody.Part convertFileToMultipart(String pathImageFile, String Key) {
        if (pathImageFile != null) {
            File file = new File(pathImageFile);

            RequestBody reqFileselect = RequestBody.create(MediaType.parse("image/*"), file);

            return MultipartBody.Part.createFormData(Key, file.getName(), reqFileselect);
        } else {
            return null;
        }
    }

    public static RequestBody convertToRequestBody(String part) {
        try {
            if (!part.equals("")) {
                return RequestBody.create(MediaType.parse("multipart/form-data"), part);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void onLoadImageFromUrl(ImageView imageView, String URl, Context context) {
        Glide.with(context)
                .load(URl)
                .into(imageView);
    }

    public static void onLoadImageFromUrl(ImageView imageView, String IMAGE_BASE_URL, String URl, Context context, int drId) {
        Glide.with(context)
                .load(IMAGE_BASE_URL + URl)
                .into(imageView);
//        imageView.setImageURI(Uri.parse(URl));
    }

    public static void onLoadImageFromUrlCircle(CircleImageView imageView, String URl, Context context, int drId) {
        Glide.with(context)
                .load(URl)
                .into(imageView);
    }

    public static void onLoadImageFromUrl(CircleImageView imageView, String IMAGE_BASE_URL, String URl, Context context, int drId) {
        Glide.with(context)
                .load(IMAGE_BASE_URL + URl)
                .into(imageView);
    }

//    public static void createSnackBar(View view, String message, Context context) {
//        final Snackbar snackbar = Snackbar.make(view, message, 50000);
//        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                snackbar.dismiss();
//            }
//        })
//                .setActionTextColor(context.getResources().getColor(android.R.color.holo_red_light))
//
//                .show();
//    }

//    public static void createSnackBar(View view, String message, Context context, View.OnClickListener action, String Title) {
//        snackbar = Snackbar.make(view, message, 50000);
//        snackbar.setAction(Title, action)
//                .setActionTextColor(context.getResources().getColor(android.R.color.holo_red_light))
//                .show();
//    }

    //Calender
    public static void showCalender(Context context, String title, final TextView text_view_data, final DateTxt data1) {
        Calendar recent = Calendar.getInstance();
        DatePickerDialog mDatePicker = new DatePickerDialog(context, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat mFormat = new DecimalFormat("00", symbols);
                String data = selectedYear + "-" + mFormat.format(Double.valueOf((selectedMonth + 1))) + "-" + mFormat.format(Double.valueOf(selectedDay));
                data1.setDate_txt(data);
                data1.setDay(mFormat.format(Double.valueOf(selectedDay)));
                data1.setMonth(mFormat.format(Double.valueOf(selectedMonth + 1)));
                data1.setYear(String.valueOf(selectedYear));
                text_view_data.setText(data);

            }
        },

                recent.get(Calendar.YEAR),
                recent.get(Calendar.MONTH),
                recent.get(Calendar.DAY_OF_MONTH));
        mDatePicker.setTitle(title);

        mDatePicker.show();
    }
//    public static void showTimer(Context context, final TextView text_view_data, final DateTxt data1) {
//            TimePickerDialog mTimePicker;
//            mTimePicker = new TimePickerDialog(context, AlertDialog.THEME_HOLO_DARK, new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
//                    DecimalFormat mFormat = new DecimalFormat("00", symbols);
//                    String data = (mFormat.format(Double.valueOf(selectedHour)) + ":" + mFormat.format(Double.valueOf(selectedMinute)) + ":00");
//                    data1.setDate_txt(data);
//                    data1.setDay("05");
//                    data1.setMonth(mFormat.format(Double.valueOf(selectedMinute)));
//                    data1.setYear(mFormat.format(Double.valueOf(selectedHour)));
//                    text_view_data.setText(data);
//                }
//            }, Integer.parseInt(data1.getYear()), Integer.parseInt(data1.getMonth()), true);//Yes 24 hour time
//            mTimePicker.setTitle(context.getString(R.string.select_time));
//            mTimePicker.show();
//        }

    public static void showProgressDialog(Activity activity, String title) {
        try {
            checkDialog = new ProgressDialog(activity);
            checkDialog.setMessage(title);
            checkDialog.setIndeterminate(false);
            checkDialog.setCancelable(false);
            checkDialog.show();

        } catch (Exception e) {
            //
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (checkDialog != null && checkDialog.isShowing()) {
                checkDialog.dismiss();
            }
        } catch (Exception e) {
            //
        }
    }

//    public static void showAlertDialogButtonClicked(Context context, String title, String content, DialogInterface.OnClickListener action) {
//        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle(title);
//        alertDialog.setMessage(content);
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getResources().getText(R.string.ok),
//                action);
//        alertDialog.show();
//    }

    public static void replaceFragment(FragmentManager getChildFragmentManager, int id, Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void disappearKeypad(Activity activity, View v) {
        try {
            if (v != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {
            //
        }
    }


//    public static void setSpinner(Context context, Spinner spinner, List<String> strings) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
//                android.R.layout.simple_spinner_item, strings);
//
//        spinner.setAdapter(adapter);
//    }

//    public static void newFacebookIntent(Activity activity, PackageManager pm, String url) {
//        Uri uri = Uri.parse(url);
//        try {
//            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
//            if (applicationInfo.enabled) {
//                // http://stackoverflow.com/a/24547437/1048340
//                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
//            }
//        } catch (PackageManager.NameNotFoundException ignored) {
//        }
//        activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
//    }

//    public static void changeLan7Yg(Context context, String lang) {
//        Resources res = context.getResources();
//        // Change locale settings in the app.
//        DisplayMetrics dm = res.getDisplayMetrics();
//        android.content.res.Configuration conf = res.getConfiguration();
//        conf.setLocale(new Locale(lang)); // API 17+ only.
//        // Use conf.locale = new Locale(...) if targeting lower versions
//        res.updateConfiguration(conf, dm);
//    }

//    public static void showAlertDialogButtonClicked(Context context, String title, String content, List<DialogInterface.OnClickListener> action, List<String> titles) {
//        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle(title);
//        alertDialog.setMessage(content);
//        for (int i = 0; i < action.size(); i++) {
//            if (i == 0) {
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, titles.get(i), action.get(i));
//            } else {
//                alertDialog.setButton2(titles.get(i), action.get(i));
//            }
//        }
//
//        alertDialog.show();
//    }

//    public static void showOfflineLayout(RelativeLayout SubView, int Visibility) {
//
//        try {
//            SubView.setVisibility(Visibility);
//        } finally {
//        }
//    }

//    public static void showOfflineLayout(ImageView Image, int Image_txt, TextView Title, String Title_txt, Button button_action, String action_txt, View.OnClickListener action) {
//
//        try {
//            Image.setBackgroundResource(Image_txt);
//        } finally {
//        }
//
//        try {
//            Title.setText(Title_txt);
//        } finally {
//        }
//
//        try {
//            button_action.setText(action_txt);
//            button_action.setOnClickListener(action);
//        } finally {
//            //*/
//        }
//    }

//    public static int calculateColumnRecyclerView(Context context) {
//        try {
//            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
//            int Column = (int) (dpWidth / 180);
//
//            if (Column < 2) {
//                return 2;
//            } else {
//                return 3;
//            }
//
//        } catch (Exception e) {
//            return 2;
//        }
//    }

//    public static void editWidth(View view, RelativeLayout SubView) {
//        ViewGroup.LayoutParams appear = view.getLayoutParams();
//        if (appear.width == 0) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                TransitionManager.beginDelayedTransition(SubView);
//            }
//            ViewGroup.LayoutParams appear1 = view.getLayoutParams();
//            appear.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            view.setLayoutParams(appear);
//
//        } else {
//            ViewGroup.LayoutParams appear1 = view.getLayoutParams();
//            appear.width = 0;
//            view.setLayoutParams(appear);
//
//        }
//    }

//    public static void editHeight(View view, RelativeLayout SubView) {
//        ViewGroup.LayoutParams appear = view.getLayoutParams();
//        if (appear.height == 0) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                TransitionManager.beginDelayedTransition(SubView);
//            }
//            ViewGroup.LayoutParams appear1 = view.getLayoutParams();
//            appear.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            view.setLayoutParams(appear);
//
//        } else {
//            ViewGroup.LayoutParams appear1 = view.getLayoutParams();
//            appear.height = 0;
//            view.setLayoutParams(appear);
//
//        }
//    }


    public static void onPermission(Activity activity) {
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE};

        ActivityCompat.requestPermissions(activity,
                perms,
                100);
        //return;
    }

    public static void showToastMessage(Context context, String message) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) ((Activity) context).findViewById(R.id.my));
        // set a message
        TextView text = layout.findViewById(R.id.customToast);
        text.setText(message);
        ImageView image = layout.findViewById(R.id.customToast_Image);
        image.setImageResource(R.drawable.ic_exclamation_triangle_solid);
        // Toast...
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @SuppressLint("ResourceAsColor")
    public static void showSuccessToastMessage(Context context, String message) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) ((Activity) context).findViewById(R.id.my));
        // set a message
        TextView text = layout.findViewById(R.id.customToast);
        text.setText(message);
        text.setTextColor(R.color.lightblue);
        ImageView image = layout.findViewById(R.id.customToast_Image);
        image.setImageResource(R.drawable.ic_check_circle_solid);
        // Toast...
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void openAlbum(int Counter, Context context, final ArrayList<AlbumFile> ImagesFiles, Action<ArrayList<AlbumFile>> action) {
        Album album = new Album();
        Album.initialize(AlbumConfig.newBuilder(context)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.ENGLISH).build());
        album.image(context)// Image and video mix options.
                .multipleChoice()// Multi-Mode, Single-Mode: singleChoice().
                .columnCount(3) // The number of columns in the page list.
                .selectCount(Counter)  // Choose up to a few images.
                .camera(true) // Whether the camera appears in the Item.
                .checkedList(ImagesFiles) // To reverse the list.
                .widget(
                        Widget.newLightBuilder(context)
                                .title("")
                                .statusBarColor(Color.WHITE) // StatusBar color.
                                .toolBarColor(Color.WHITE) // Toolbar color.
                                .navigationBarColor(Color.WHITE) // Virtual NavigationBar color of Android5.0+.
                                .mediaItemCheckSelector(Color.BLUE, Color.GREEN) // Image or video selection box.
                                .bucketItemCheckSelector(Color.RED, Color.YELLOW) // Select the folder selection box.
                                .build()
                )
                .onResult(action)
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        // The Client canceled the operation.
                    }
                })
                .start();
    }

//    public static void selectValue(Spinner spinner, Object value) {
//        for (int i = 0; i < spinner.getCount(); i++) {
//            if (spinner.getItemAtPosition(i).equals(value)) {
//                spinner.setSelection(i);
//                break;
//            }
//        }
//    }

    public static void changeLang(Context context, String lang) {
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

    public static Date convertDateString(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());

            return format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DateTxt convertStringToDateTxtModel(String date) {
        try {
            Date date1 = convertDateString(date);
            String day = (String) DateFormat.format("dd", date1); // 20
            String monthNumber = (String) DateFormat.format("MM", date1); // 06
            String year = (String) DateFormat.format("yyyy", date1); // 2013

            return new DateTxt(day, monthNumber, year, date);

        } catch (Exception e) {
            return null;
        }
    }

//    public static void removeRestaurantnotification(ApiServices apiServices, String apiToken) {
//        apiServices.removeRestaurantnotification(FirebaseInstanceId.getInstance().getToken(), apiToken).enqueue(new Callback<GeneralResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
//            }
//        });
//    }

    public static void registerRestaurantNotification(ApiServices apiServices, String apiToken) {
        String token = FirebaseInstanceId.getInstance().getToken();
        apiServices.registerRestaurantNotification(token, apiToken, "android").enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
                Log.d(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onResponse: ");
            }
        });
    }

//    public static void removeClientnotification(ApiServices apiServices, String apiToken) {
//        apiServices.removeClientnotification(FirebaseInstanceId.getInstance().getToken(), apiToken).enqueue(new Callback<GeneralResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
//            }
//        });
//    }

//    public static void registerClientNotification(ApiServices apiServices, String apiToken) {
//        String token = FirebaseInstanceId.getInstance().getToken();
//        apiServices.registerClientNotification(token, apiToken, "android").enqueue(new Callback<GeneralResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<GeneralResponse> call, @NonNull Response<GeneralResponse> response) {
//                Log.d(TAG, "onResponse: ");
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<GeneralResponse> call, @NonNull Throwable t) {
//                Log.d(TAG, "onResponse: ");
//            }
//        });
//    }

    public static boolean checkWriteExternalPermission(Context context) {
        boolean check = false;
        String permission1 = android.Manifest.permission.ACCESS_FINE_LOCATION;
        int res1 = context.checkCallingOrSelfPermission(permission1);
        if (res1 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }
//
//        String permission2 = android.Manifest.permission.READ_CONTACTS;
//        int res2 = context.checkCallingOrSelfPermission(permission2);
//        if (res2 != PackageManager.PERMISSION_GRANTED) {
//            check = true;
//        }

        String permission3 = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        int res3 = context.checkCallingOrSelfPermission(permission3);
        if (res3 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }

        String permission4 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res4 = context.checkCallingOrSelfPermission(permission4);
        if (res4 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }

//        String permission5 = android.Manifest.permission.READ_PHONE_STATE;
//        int res5 = context.checkCallingOrSelfPermission(permission5);
//        if (res5 != PackageManager.PERMISSION_GRANTED) {
//            check = true;
//        }

        String permission6 = android.Manifest.permission.CALL_PHONE;
        int res6 = context.checkCallingOrSelfPermission(permission6);
        if (res6 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }

        return check;
    }

//    private long millisFromDateAndTime(String date, String time) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy",Locale.getDefault());
//        SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm:ss",Locale.getDefault());
//        try {
//            Date mDate = dateFormat.parse(date);
//            Date mTime = timeFormat.parse(time);
//            long dateInMilliseconds = mDate.getTime();
//            long timeInMilliseconds = mTime.getTime();
//            return timeInMilliseconds + dateInMilliseconds;
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return 0;
//    }

//    private String timeAgo(long startTime) {
//        long currentTime = System.currentTimeMillis();
//        long timeDifference = (currentTime - startTime) / 1000;
//        Log.d("!!!!DIFF!!!!!!", String.valueOf(timeDifference));
//        long timeUnit;
//
//        if (timeDifference < 60) {
//            timeUnit = DateUtils.SECOND_IN_MILLIS;
//        } else if (timeDifference < 3600) {
//            timeUnit = DateUtils.MINUTE_IN_MILLIS;
//        } else if (timeDifference < 86400) {
//            timeUnit = DateUtils.HOUR_IN_MILLIS;
//        } else if (timeDifference < 31536000) {
//            timeUnit = DateUtils.DAY_IN_MILLIS;
//        } else {
//            timeUnit = DateUtils.YEAR_IN_MILLIS;
//        }
//
//        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(startTime, currentTime,
//                timeUnit, DateUtils.FORMAT_ABBREV_RELATIVE);
//
//        return String.valueOf(timeAgo);
//    }

    public static void keyboardVisibility(final View contentView, final Activity context){
        isKeyboardShowing = false;
        // ContentView is the root view of the layout of this activity/fragment
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        contentView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = contentView.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        //Log.d(TAG, "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                onKeyboardVisibilityChanged(true, context);
                            }
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged(false, context);
                            }
                        }
                    }
                });
    }

    private static void onKeyboardVisibilityChanged(boolean opened, Activity context) {
        if (navBar != null){
            if (opened){
                Animation slide_down = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                navBar.startAnimation(slide_down);
                navBar.setVisibility(View.GONE);
                //Toast.makeText(context, "slide down", Toast.LENGTH_SHORT).show();
            }else {
                slide_up = AnimationUtils.loadAnimation(context,R.anim.slide_up);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navBar.startAnimation(slide_up);
                        navBar.setVisibility(View.VISIBLE);
                    }
                }, 5);
            }
        }
    }
}


