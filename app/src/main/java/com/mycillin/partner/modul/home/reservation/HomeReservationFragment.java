package com.mycillin.partner.modul.home.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycillin.partner.R;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.PatientManager;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.RecyclerTouchListener;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

import static com.mycillin.partner.modul.home.visit.HomeVisitFragment.EXTRA_FLAG_FROM_NO_SWIPE;
import static com.mycillin.partner.modul.home.visit.HomeVisitFragment.EXTRA_FLAG_FROM_SWIPE;

public class HomeReservationFragment extends Fragment {

    @BindView(R.id.homeReservationFragment_sr_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.homeReservationFragment_rv_recyclerView)
    RecyclerView homeReservationRecyclerView;

    private List<HomeReservationList> homeReservationLists = new ArrayList<>();
    private HomeReservationAdapter homeReservationAdapter;
    private SessionManager sessionManager;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;

    public HomeReservationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_reservation, container, false);
        ButterKnife.bind(this, rootView);
        sessionManager = new SessionManager(getActivity());
        mProgressBarHandler = new ProgressBarHandler(getActivity());
        mHandler = new Handler(Looper.getMainLooper());
        homeReservationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeReservationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        homeReservationLists.clear();

        getHomeReservationList();
        getReservationData(EXTRA_FLAG_FROM_NO_SWIPE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeReservationLists.clear();
                getReservationData(EXTRA_FLAG_FROM_SWIPE);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        return rootView;
    }

    public void getHomeReservationList() {
        homeReservationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), homeReservationRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HomeReservationList list = homeReservationLists.get(position);

                PatientManager patientManager = new PatientManager(getContext());

                patientManager.setPatientId(list.getPatientID());
                patientManager.setPatientBookingId(list.getBookingID());
                patientManager.setPatientAddress(list.getAddress());
                patientManager.setPatientLatitude(list.getPatientLatitude());
                patientManager.setPatientLongitude(list.getPatientLongitude());
                patientManager.setKeyPatientMobileNo(list.getPhoneNumber());
                patientManager.setKeyPatientPhoto(list.getPatientPic());

                Intent intent = new Intent(getContext(), HomeReservationDetailActivity.class);
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_NAME, list.getPatientName());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_DATE, list.getBookDate());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_FEE, list.getPaymentMethod());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_TIME, list.getBookTime());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_TYPE, list.getBookType());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_PIC, list.getPatientPic());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void getReservationData(String flagFrom) {
        homeReservationLists.clear();

        switch (flagFrom) {
            case EXTRA_FLAG_FROM_NO_SWIPE:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.show();
                    }
                });
                break;
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", sessionManager.getUserId());

        JSONObject jsonObject = new JSONObject(data);

        Timber.tag("####").d("saveAddress: OBJEK %s", jsonObject);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "list_dash_reservasi/")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        mProgressBarHandler.hide();
                    }
                });
                DialogHelper.showDialog(mHandler, getActivity(), "Warning", "Connection Problem, Please Try Again Later." + e, false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        mProgressBarHandler.hide();
                    }
                });
                @SuppressWarnings("ConstantConditions") final String result = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Timber.tag("###").d("onResponse: %s", jsonObject);
                        boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                        if (status) {
                            JSONArray data = jsonObject.getJSONObject("result").getJSONArray("data");
                            Timber.tag("###").d("onResponse2: %s", data);

                            for (int i = 0; i < data.length(); i++) {
                                final String userID = data.getJSONObject(i).optString("patient_id").trim();
                                String relationID = data.getJSONObject(i).optString("relation_id").trim();
                                final String serviceType = data.getJSONObject(i).optString("service_type_id").trim();
                                final String timeBooking = data.getJSONObject(i).optString("order_date").trim();
                                final String profilePhoto = data.getJSONObject(i).optString("profile_photo").trim();
                                final String dateBookingS = timeBooking.split(" ")[0];
                                final String timeBookingS = timeBooking.split(" ")[1];

                                final String priceAmount = data.getJSONObject(i).optString("price_amount").trim();
                                final String bookingID = data.getJSONObject(i).optString("booking_id").trim();
                                final String paymentMethod = data.getJSONObject(i).optString("pymt_methode_desc").trim();
                                final String patientLongitude = data.getJSONObject(i).optString("longitude_origin").trim();
                                final String patientLatitude = data.getJSONObject(i).optString("latitude_origin").trim();

                                SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);

                                Date orderDate_;
                                final String orderDates;
                                if (!dateBookingS.equals("null") && !dateBookingS.isEmpty()) {
                                    orderDate_ = dateParse.parse(dateBookingS);
                                    orderDates = orderDate_ != null ? dateFormatter.format(orderDate_) : "";
                                } else {
                                    orderDates = "";
                                }

                                getDetailUser(userID, relationID, serviceType, orderDates, timeBookingS, profilePhoto, priceAmount, bookingID, paymentMethod, patientLongitude, patientLatitude);
                            }
                        }
                    } catch (JSONException e) {
                        Timber.tag("###").d("onResponseror: %s", e.getMessage());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getDetailUser(final String userID, String relationID, final String serviceTypeID, final String dateBookingS, final String timeBookingS, final String profilePhoto, final String priceAmount, final String bookingID, final String paymentMethod, final String patientLongitude, final String patientLatitude) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", userID);
        data.put("relation_id", relationID);

        JSONObject jsonObject = new JSONObject(data);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "detail_user_partner/")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                DialogHelper.showDialog(mHandler, getActivity(), "Warning", "Connection Problem, Please Try Again Later." + e, false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                final String result = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Timber.tag("###").d("onResponse: %s", jsonObject);
                        boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                        if (status) {
                            JSONArray data = jsonObject.getJSONObject("result").getJSONArray("data");
                            Timber.tag("###").d("onResponse2: %s", data);
                            for (int i = 0; i < data.length(); i++) {
                                final String fullName = data.getJSONObject(i).optString("full_name").trim();
                                final String address = data.getJSONObject(i).optString("address").trim();
                                final String mobileNo = data.getJSONObject(i).optString("mobile_no").trim();
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String serviceType;
                                        switch (serviceTypeID) {
                                            case "00":
                                                serviceType = "Home Visit";
                                                break;
                                            case "01":
                                                serviceType = "Home Reservation";
                                                break;
                                            case "02":
                                                serviceType = "Consultation";
                                                break;
                                            case "03":
                                                serviceType = "Rent Ambulance";
                                                break;
                                            case "04":
                                                serviceType = "Medicine Store";
                                                break;
                                            case "05":
                                                serviceType = "Home Care";
                                                break;
                                            case "06":
                                                serviceType = "Psycolog";
                                                break;
                                            default:
                                                serviceType = "Servis Type";
                                                break;
                                        }
                                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                                        String v_priceAmount;
                                        if (!priceAmount.equals("null")) {
                                            v_priceAmount = numberFormat.format(Double.parseDouble(priceAmount.isEmpty() ? "0" : priceAmount));
                                        } else {
                                            v_priceAmount = "0";
                                        }
                                        homeReservationLists.add(new HomeReservationList(profilePhoto, fullName, serviceType, dateBookingS, timeBookingS + " WIB", v_priceAmount + " - " + paymentMethod, userID, bookingID, address, patientLatitude, patientLongitude, mobileNo));
                                        homeReservationAdapter = new HomeReservationAdapter(homeReservationLists, HomeReservationFragment.this);
                                        homeReservationRecyclerView.setAdapter(homeReservationAdapter);
                                        homeReservationAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        Timber.tag("###").d("onResponseror: %s", e.getMessage());
                    }
                }
            }
        });
    }
}
