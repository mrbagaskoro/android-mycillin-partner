package com.mycillin.partner.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycillin.partner.R;
import com.mycillin.partner.activity.HomeReservationDetailActivity;
import com.mycillin.partner.adapter.HomeReservationAdapter;
import com.mycillin.partner.list.HomeReservationList;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.RecyclerTouchListener;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class HomeReservationFragment extends Fragment {

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
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getReservationData();
    }

    public void getHomeReservationList() {
        homeReservationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), homeReservationRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HomeReservationList list = homeReservationLists.get(position);

                Intent intent = new Intent(getContext(), HomeReservationDetailActivity.class);
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_NAME, list.getPatientName());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_DATE, list.getBookDate());
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

    public void getReservationData() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", sessionManager.getUserId());
        data.put("booking_status_id", "");
        data.put("service_type_id", "01");
        data.put("booking_id", "");

        JSONObject jsonObject = new JSONObject(data);

        Log.d("####", "saveAddress: OBJEK " + jsonObject);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "list_partner_booking/")
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
                        Log.d("###", "onResponse: " + jsonObject);
                        boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                        if (status) {
                            JSONArray data = jsonObject.getJSONObject("result").getJSONArray("data");
                            Log.d("###", "onResponse2: " + data);

                            for (int i = 0; i < data.length(); i++) {
                                final String userID = data.getJSONObject(i).optString("user_id").trim();
                                String relationID = data.getJSONObject(i).optString("relation_id").trim();
                                final String serviceType = data.getJSONObject(i).optString("service_type_id").trim();
                                final String timeBooking = data.getJSONObject(i).optString("created_date").trim();
                                final String dateBookingS = timeBooking.split(" ")[0];
                                final String timeBookingS = timeBooking.split(" ")[1];
                                getDetailUser(userID, relationID, serviceType, dateBookingS, timeBookingS);
                            }
                        }
                    } catch (JSONException e) {
                        Log.d("###", "onResponseror: " + e);
                    }
                }
            }
        });
    }

    private void getDetailUser(String userID, String relationID, final String serviceTypeID, final String dateBookingS, final String timeBookingS) {
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
                        Log.d("###", "onResponse: " + jsonObject);
                        boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                        if (status) {
                            JSONArray data = jsonObject.getJSONObject("result").getJSONArray("data");
                            Log.d("###", "onResponse2: " + data);
                            for (int i = 0; i < data.length(); i++) {
                                final String fullName = data.getJSONObject(i).optString("full_name").trim();
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
                                        homeReservationLists.add(new HomeReservationList("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/John_Petrucci_-_01.jpg/240px-John_Petrucci_-_01.jpg", fullName, serviceType, dateBookingS, timeBookingS + " WIB"));
                                        homeReservationAdapter = new HomeReservationAdapter(homeReservationLists, HomeReservationFragment.this);
                                        homeReservationRecyclerView.setAdapter(homeReservationAdapter);
                                        homeReservationAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        Log.d("###", "onResponseror: " + e);
                    }
                }
            }
        });
    }
}
