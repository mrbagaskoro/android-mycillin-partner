package com.mycillin.partner.modul.todo;

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
import com.mycillin.partner.modul.todo.adapterList.ToDoCompletedAdapter;
import com.mycillin.partner.modul.todo.adapterList.ToDoCompletedList;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;
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
import timber.log.Timber;

import static com.mycillin.partner.modul.home.visit.HomeVisitFragment.EXTRA_FLAG_FROM_NO_SWIPE;
import static com.mycillin.partner.modul.home.visit.HomeVisitFragment.EXTRA_FLAG_FROM_SWIPE;

public class ToDoCompletedFragment extends Fragment {
    @BindView(R.id.htoDoCompletedFragment_sr_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toDoCompletedFragment_rv_recyclerView)
    RecyclerView toDoCompletedRecyclerView;

    private List<ToDoCompletedList> toDoCompletedLists = new ArrayList<>();
    private ToDoCompletedAdapter toDoCompletedAdapter;
    private SessionManager sessionManager;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;

    public ToDoCompletedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_to_do_completed, container, false);
        ButterKnife.bind(this, rootView);
        sessionManager = new SessionManager(getActivity());
        mProgressBarHandler = new ProgressBarHandler(getActivity());
        mHandler = new Handler(Looper.getMainLooper());
        toDoCompletedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        toDoCompletedRecyclerView.setItemAnimator(new DefaultItemAnimator());

        toDoCompletedLists.clear();
        getCompletedData(EXTRA_FLAG_FROM_NO_SWIPE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                toDoCompletedLists.clear();
                getCompletedData(EXTRA_FLAG_FROM_SWIPE);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        return rootView;
    }

    public void getCompletedData(String flagFrom) {
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
                .url(Configs.URL_REST_CLIENT + "list_todo_completed/")
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
                        swipeRefreshLayout.setRefreshing(false);
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
                                final String userID = data.getJSONObject(i).optString("patient_id").trim();
                                String relationID = data.getJSONObject(i).optString("relation_id").trim();
                                final String serviceType = data.getJSONObject(i).optString("service_type_id").trim();
                                final String timeBooking = data.getJSONObject(i).optString("order_date").trim();
                                final String profilePhoto = data.getJSONObject(i).optString("profile_photo").trim();
                                final String dateBookingS = timeBooking.split(" ")[0];
                                final String timeBookingS = timeBooking.split(" ")[1];
                                getDetailUser(userID, relationID, serviceType, dateBookingS, timeBookingS, profilePhoto);
                            }
                        }
                    } catch (JSONException e) {
                        Timber.tag("###").d("onResponseror: %s", e.getMessage());
                    }
                }
            }
        });
    }

    private void getDetailUser(String userID, String relationID, final String serviceTypeID, final String dateBookingS, final String timeBookingS, final String profilePhoto) {
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
                                        toDoCompletedLists.add(new ToDoCompletedList(profilePhoto, fullName, serviceType, dateBookingS, timeBookingS + " WIB", address));
                                        toDoCompletedAdapter = new ToDoCompletedAdapter(toDoCompletedLists, ToDoCompletedFragment.this);
                                        toDoCompletedRecyclerView.setAdapter(toDoCompletedAdapter);
                                        toDoCompletedAdapter.notifyDataSetChanged();
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
