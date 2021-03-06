package com.mycillin.partner.modul.home.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycillin.partner.R;
import com.mycillin.partner.modul.chat.ChatActivityConsultation;
import com.mycillin.partner.modul.chat.firebaseGet.ModelResultFirebaseGet;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.PartnerAPI;
import com.mycillin.partner.util.PatientManager;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.RecyclerTouchListener;
import com.mycillin.partner.util.RestClient;
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

public class HomeConsultationFragment extends Fragment {
    public static final String EXTRA_FLAG_FROM_SWIPE = "SWIPE";
    public static final String EXTRA_FLAG_FROM_NO_SWIPE = "NO_SWIPE";

    @BindView(R.id.homeConsultationFragment_sr_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.homeConsultationFragment_rv_recyclerView)
    RecyclerView homeConsultationRecyclerView;

    private List<HomeConsultationList> homeConsultationLists = new ArrayList<>();
    private HomeConsultationAdapter homeConsultationAdapter;
    private SessionManager sessionManager;
    private PatientManager patientManager;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;

    public HomeConsultationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_consultation, container, false);
        ButterKnife.bind(this, rootView);
        sessionManager = new SessionManager(getActivity());
        patientManager = new PatientManager(getContext());
        mProgressBarHandler = new ProgressBarHandler(getActivity());
        mHandler = new Handler(Looper.getMainLooper());
        homeConsultationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeConsultationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        homeConsultationLists.clear();
        getHomeConsultationList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeConsultationLists.clear();
                getConsultationData(EXTRA_FLAG_FROM_SWIPE);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        return rootView;
    }

    public void getHomeConsultationList() {
        homeConsultationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), homeConsultationRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HomeConsultationList list = homeConsultationLists.get(position);
                Intent intent = new Intent(getContext(), ChatActivityConsultation.class);
                getFirebaseToken(list.getPatientID(), list.getPatientName());
                intent.putExtra(ChatActivityConsultation.KEY_FLAG_CHAT_PATIENT_ID, list.getPatientID());
                intent.putExtra(ChatActivityConsultation.KEY_FLAG_CHAT_PATIENT_NAME, list.getPatientName());
                intent.putExtra(ChatActivityConsultation.KEY_FLAG_CHAT_USER_ID, sessionManager.getUserId());
                intent.putExtra(ChatActivityConsultation.KEY_FLAG_CHAT_USER_NAME, sessionManager.getUserFullName());
                intent.putExtra(ChatActivityConsultation.KEY_FLAG_CHAT_BOOKING_ID, list.getBookingID());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getFirebaseToken(final String patientId, final String patientName) {
        PartnerAPI partnerAPI = RestClient.getPartnerRestInterfaceNoToken();
        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", patientId);

        partnerAPI.getFirebaseToken(sessionManager.getUserToken(), data).enqueue(new retrofit2.Callback<ModelResultFirebaseGet>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ModelResultFirebaseGet> call, @NonNull retrofit2.Response<ModelResultFirebaseGet> response) {
                if (response.isSuccessful()) {
                    ModelResultFirebaseGet modelResultDataFirebaseGet = response.body();
                    assert modelResultDataFirebaseGet != null;
                    sendNotification(modelResultDataFirebaseGet.getResult().getData().get(0).getToken(), patientId, patientName);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message;
                        if (jsonObject.has("result")) {
                            message = jsonObject.getJSONObject("result").getString("message");
                        } else {
                            message = jsonObject.getString("message");
                        }
                        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ModelResultFirebaseGet> call, @NonNull final Throwable t) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        DialogHelper.showDialog(mHandler, getActivity(), "Error", "Connection problem " + t.getMessage(), false);
                    }
                });
            }
        });
    }

    private void sendNotification(String token, String patientID, String patientName) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        Map<String, String> paramsNotif = new HashMap<>();
        paramsNotif.put("body", "You Have New Chat");
        paramsNotif.put("click_action", "CHAT");

        Map<String, String> paramsData = new HashMap<>();
        paramsData.put("CHAT_PATIENT_ID", patientID);
        paramsData.put("CHAT_PATIENT_NAME", patientName);
        paramsData.put("CHAT_USER_ID", sessionManager.getUserId());
        paramsData.put("CHAT_USER_NAME", sessionManager.getUserFullName());

        Map<String, Object> params = new HashMap<>();
        params.put("notification", paramsNotif);
        params.put("data", paramsData);
        params.put("to", token);

        JSONObject jsonObject = new JSONObject(params);
        Timber.tag("#8#8#").d("sendNotif: %s", jsonObject);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", "key=AAAAbynyk1I:APA91bENZXh3N4QC-HrUy4ApIVe8CnW3F0k5mG5OXdUMApskyFTKDYnjd6Pdwko-hqvkekZoH5KxtC-gyxu0-XoXcItm9PJYGw9zzrc5Wbzr6CY3FuaSvXb7MCYMNfmNEVmUWZA8SqB5")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                String result = response.body().string();
                Timber.tag("#8#8#").d("onResponse: %s", result);
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("result")) {
                            boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                            if (status) {
                                Timber.tag("#8#8#").d("onResponse: SIP");
                            } else {
                                String message = jsonObject.getJSONObject("result").getString("message");
                                Timber.tag("#8#8#").d("onResponse: SIP%s", message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String message;
                        if (jsonObject.has("result")) {
                            message = jsonObject.getJSONObject("result").getString("message");
                        } else {
                            message = jsonObject.getString("message");
                        }

                        Log.d("#8#8#", "onResponse: gagal" + message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getConsultationData(String flagFrom) {
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
                .url(Configs.URL_REST_CLIENT + "list_dash_konsultasi/")
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
                                final String patientLongitude = data.getJSONObject(i).optString("longitude_request").trim();
                                final String patientLatitude = data.getJSONObject(i).optString("latitude_request").trim();

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
                @SuppressWarnings("ConstantConditions") final String result = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Timber.d("onResponse: %s", jsonObject);
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
                                                serviceType = "Unknown";
                                                break;
                                        }
                                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                                        String v_priceAmount;
                                        if (!priceAmount.equals("null")) {
                                            v_priceAmount = numberFormat.format(Double.parseDouble(priceAmount.isEmpty() ? "0" : priceAmount));
                                        } else {
                                            v_priceAmount = "0";
                                        }

                                        homeConsultationLists.add(new HomeConsultationList(profilePhoto, fullName, serviceType, dateBookingS, timeBookingS + " WIB", v_priceAmount + " - " + paymentMethod, userID, bookingID, address, patientLatitude, patientLongitude, mobileNo));
                                        homeConsultationAdapter = new HomeConsultationAdapter(homeConsultationLists, HomeConsultationFragment.this);
                                        homeConsultationRecyclerView.setAdapter(homeConsultationAdapter);
                                        homeConsultationAdapter.notifyDataSetChanged();
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
