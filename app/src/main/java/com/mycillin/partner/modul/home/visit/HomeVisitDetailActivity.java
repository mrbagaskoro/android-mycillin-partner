package com.mycillin.partner.modul.home.visit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.chat.ChatActivity;
import com.mycillin.partner.modul.chat.firebaseGet.ModelResultFirebaseGet;
import com.mycillin.partner.modul.home.cancelAdapterList.ModelRestCancelReason;
import com.mycillin.partner.modul.home.cancelAdapterList.ModelRestCancelReasonData;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.PartnerAPI;
import com.mycillin.partner.util.PatientManager;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.RestClient;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HomeVisitDetailActivity extends AppCompatActivity {

    public static String KEY_FLAG_PATIENT_NAME = "KEY_FLAG_PATIENT_NAME";
    public static String KEY_FLAG_PATIENT_TYPE = "KEY_FLAG_PATIENT_TYPE";
    public static String KEY_FLAG_PATIENT_DATE = "KEY_FLAG_PATIENT_DATE";
    public static String KEY_FLAG_PATIENT_TIME = "KEY_FLAG_PATIENT_TIME";
    public static String KEY_FLAG_PATIENT_FEE = "KEY_FLAG_PATIENT_FEE";
    public static String KEY_FLAG_PATIENT_PIC = "KEY_FLAG_PATIENT_PIC";

    @BindView(R.id.homeVisitDetailActivity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.homeVisitDetailActivity_bt_chat)
    Button chatBtn;
    @BindView(R.id.homeVisitDetailActivity_bt_reject)
    Button cancelBtn;
    @BindView(R.id.homeVisitDetailActivity_bt_maps)
    Button mapsBtn;
    @BindView(R.id.homeVisitDetailActivity_bt_call)
    Button callBtn;

    @BindView(R.id.homeVisitDetailActivity_tv_patientName)
    TextView patientNames;
    @BindView(R.id.homeVisitDetailActivity_tv_bookDate)
    TextView bookDate;
    @BindView(R.id.homeVisitDetailActivity_tv_bookType)
    TextView bookType;
    @BindView(R.id.homeVisitDetailActivity_tv_bookLocation)
    TextView bookLocation;
    @BindView(R.id.homeVisitDetailActivity_iv_patientAvatar)
    CircleImageView circleImageView;

    @BindView(R.id.homeVisitDetailActivity_tv_bookFee)
    TextView bookFee;

    private PartnerAPI partnerAPI;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private ArrayList cancelReasonList = new ArrayList();
    private PatientManager patientManager;
    private SessionManager sessionManager;
    private String patientName;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_visit_detail);
        ButterKnife.bind(this);

        partnerAPI = RestClient.getPartnerRestInterfaceNoToken();
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(HomeVisitDetailActivity.this);
        patientManager = new PatientManager(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        toolbar.setTitle(R.string.homeVisitDetail_title);
        patientName = getIntent().getStringExtra(KEY_FLAG_PATIENT_NAME);
        patientNames.setText(patientName);
        bookDate.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_DATE) + "\n" + getIntent().getStringExtra(KEY_FLAG_PATIENT_TIME));
        bookType.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_TYPE));
        bookLocation.setText(patientManager.getPatientAddress());
        bookFee.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_FEE));

        getFirebaseToken(patientManager.getPatientId());

        if (!patientManager.getKeyPatientPhoto().equals("")) {
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .circleCrop();

            Glide.with(HomeVisitDetailActivity.this)
                    .load(patientManager.getKeyPatientPhoto())
                    .apply(requestOptions)
                    .into(circleImageView);
        }

    }

    private void getFirebaseToken(String patientId) {
        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", patientId);

        partnerAPI.getFirebaseToken(sessionManager.getUserToken(), data).enqueue(new Callback<ModelResultFirebaseGet>() {
            @Override
            public void onResponse(@NonNull Call<ModelResultFirebaseGet> call, @NonNull Response<ModelResultFirebaseGet> response) {
                if (response.isSuccessful()) {
                    ModelResultFirebaseGet modelResultDataFirebaseGet = response.body();
                    assert modelResultDataFirebaseGet != null;
                    userToken = modelResultDataFirebaseGet.getResult().getData().get(0).getToken();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message;
                        if (jsonObject.has("result")) {
                            message = jsonObject.getJSONObject("result").getString("message");
                        } else {
                            message = jsonObject.getString("message");
                        }
                        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelResultFirebaseGet> call, @NonNull final Throwable t) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        DialogHelper.showDialog(mHandler, HomeVisitDetailActivity.this, "Error", "Connection problem " + t.getMessage(), false);
                    }
                });
            }
        });
    }

    @OnClick(R.id.homeVisitDetailActivity_bt_maps)
    public void onMapsStart() {
        String patientLatitude = patientManager.getPatientLatitude();
        String patientLongitude = patientManager.getPatientLongitude();

        String userLatitude = sessionManager.getKeyUserLatitude();
        String userLongitude = sessionManager.getKeyUserLongitude();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + userLatitude + "," + userLongitude + "&daddr=" + patientLatitude + "," + patientLongitude + ""));
        startActivity(intent);
    }

    @OnClick(R.id.homeVisitDetailActivity_bt_call)
    public void onClickCall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String phoneNumber = patientManager.getKeyPatientMobileNo();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "" + phoneNumber + ""));
        startActivity(intent);
    }

    @OnClick(R.id.homeVisitDetailActivity_bt_chat)
    public void onClickStart() {
        sendNotification();
        Intent intent = new Intent(HomeVisitDetailActivity.this, ChatActivity.class);
        intent.putExtra(ChatActivity.KEY_FLAG_CHAT_PATIENT_ID, patientManager.getPatientId());
        intent.putExtra(ChatActivity.KEY_FLAG_CHAT_PATIENT_NAME, patientName);
        intent.putExtra(ChatActivity.KEY_FLAG_CHAT_USER_ID, sessionManager.getUserId());
        intent.putExtra(ChatActivity.KEY_FLAG_CHAT_USER_NAME, sessionManager.getUserFullName());
        intent.putExtra(ChatActivity.KEY_FLAG_CHAT_BOOKING_ID, patientManager.getPatientBookingId());
        startActivity(intent);
    }

    private void sendNotification() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        Map<String, String> paramsNotif = new HashMap<>();
        paramsNotif.put("body", "You Have New Chat");
        paramsNotif.put("click_action", "CHAT");

        Map<String, String> paramsData = new HashMap<>();
        paramsData.put("CHAT_PATIENT_ID", patientManager.getPatientId());
        paramsData.put("CHAT_PATIENT_NAME", patientName);
        paramsData.put("CHAT_USER_ID", sessionManager.getUserId());
        paramsData.put("CHAT_USER_NAME", sessionManager.getUserFullName());

        Map<String, Object> params = new HashMap<>();
        params.put("notification", paramsNotif);
        params.put("data", paramsData);
        params.put("to", userToken);

        JSONObject jsonObject = new JSONObject(params);
        Log.d("#8#8#", "sendNotif: " + jsonObject);

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
                Log.d("#8#8#", "onResponse: " + result);
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("result")) {
                            boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                            if (status) {
                                Log.d("#8#8#", "onResponse: SIP");
                            } else {
                                String message = jsonObject.getJSONObject("result").getString("message");
                                Log.d("#8#8#", "onResponse: SIP" + message);
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

    @OnClick(R.id.homeVisitDetailActivity_bt_reject)
    public void onClickCancel() {
        cancelReason();
    }

    public void cancelReason() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        partnerAPI.getCancelReason().enqueue(new Callback<ModelRestCancelReason>() {
            @Override
            public void onResponse(@NonNull Call<ModelRestCancelReason> call, @NonNull Response<ModelRestCancelReason> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                ModelRestCancelReason modelRestCancelReason = response.body();
                if (response.isSuccessful()) {
                    cancelReasonList.clear();
                    assert modelRestCancelReason != null;
                    for (ModelRestCancelReasonData modelRestCancelReasonData : modelRestCancelReason.getResult().getData()) {
                        cancelReasonList.add(modelRestCancelReasonData.getCancelReasonId() + " - " + modelRestCancelReasonData.getCancelReasonDesc());
                    }
                    cancelReasonDialog(cancelReasonList);
                } else {
                    DialogHelper.showDialog(mHandler, HomeVisitDetailActivity.this, "Error", modelRestCancelReason + "", false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelRestCancelReason> call, @NonNull final Throwable t) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        DialogHelper.showDialog(mHandler, HomeVisitDetailActivity.this, "Error", "Connection problem " + t, false);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void cancelReasonDialog(ArrayList<String> cancelReasonList) {
        final String[] batal = new String[1];
        final String[] items = cancelReasonList.toArray(new String[cancelReasonList.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeVisitDetailActivity.this);
        builder.setTitle("Alasan pembatalan request");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                batal[0] = items[item];
            }
        });
        builder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(HomeVisitDetailActivity.this, "Berhasil " + batal[0].split(" - ")[0], Toast.LENGTH_SHORT).
                                show();
                        rejectRequestor(batal[0].split(" - ")[0]);
                    }
                });
        builder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(HomeVisitDetailActivity.this, "Batal", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void rejectRequestor(String kodeReject) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        Map<String, Object> data = new HashMap<>();
        data.put("user_id", sessionManager.getUserId());
        data.put("booking_id", patientManager.getPatientBookingId());
        data.put("cancel_reason_id", kodeReject);

        JSONObject jsonObject = new JSONObject(data);

        Timber.tag("JINX").d("saveAddress: OBJEK %s", jsonObject);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "partner_cancel_transaction/")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                DialogHelper.showDialog(mHandler, HomeVisitDetailActivity.this, "Warning", "Please Try Again : " + e.getMessage(), false);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull final okhttp3.Response response) throws IOException {
                @SuppressWarnings("ConstantConditions") final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        if (response.isSuccessful()) {
                            DialogHelper.showDialog(mHandler, HomeVisitDetailActivity.this, "Status", "Rejected", true);

                        } else {
                            DialogHelper.showDialog(mHandler, HomeVisitDetailActivity.this, "Warning", "Please Try Again : " + result, false);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

}
