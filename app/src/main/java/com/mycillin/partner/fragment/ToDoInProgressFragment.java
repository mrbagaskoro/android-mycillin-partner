package com.mycillin.partner.fragment;

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
import com.mycillin.partner.activity.ToDoInProgressDetailActivity;
import com.mycillin.partner.adapter.ToDoInProgressAdapter;
import com.mycillin.partner.list.ToDoInProgressList;
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

public class ToDoInProgressFragment extends Fragment {

    @BindView(R.id.toDoInProgressFragment_rv_recyclerView)
    RecyclerView toDoInProgressRecyclerView;

    private List<ToDoInProgressList> toDoInProgressLists = new ArrayList<>();
    private ToDoInProgressAdapter toDoInProgressAdapter;
    private SessionManager sessionManager;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;

    public ToDoInProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_to_do_in_progress, container, false);
        ButterKnife.bind(this, rootView);
        sessionManager = new SessionManager(getActivity());
        mProgressBarHandler = new ProgressBarHandler(getActivity());
        mHandler = new Handler(Looper.getMainLooper());
        toDoInProgressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        toDoInProgressRecyclerView.setItemAnimator(new DefaultItemAnimator());
        toDoInProgressLists.clear();
        getToDoInProgressList();
        getTodoData();
        return rootView;
    }

    public void getToDoInProgressList() {
        toDoInProgressRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), toDoInProgressRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToDoInProgressList list = toDoInProgressLists.get(position);

                Intent intent = new Intent(getContext(), ToDoInProgressDetailActivity.class);
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_NAME, list.getPatientName());
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_DATE, list.getBookDate());
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_TIME, list.getBookTime());
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_TYPE, list.getBookType());
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_PIC, list.getPatientPic());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    public void getTodoData() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", sessionManager.getUserId());
        data.put("booking_status_id", "03");
        data.put("service_type_id", "");
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
                                        toDoInProgressLists.add(new ToDoInProgressList("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/John_Petrucci_-_01.jpg/240px-John_Petrucci_-_01.jpg", fullName, serviceType, dateBookingS, timeBookingS + " WIB"));
                                        toDoInProgressAdapter = new ToDoInProgressAdapter(toDoInProgressLists, ToDoInProgressFragment.this);
                                        toDoInProgressRecyclerView.setAdapter(toDoInProgressAdapter);
                                        toDoInProgressAdapter.notifyDataSetChanged();
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
