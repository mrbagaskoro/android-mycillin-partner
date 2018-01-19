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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mycillin.partner.R;
import com.mycillin.partner.modul.chat.ChatActivity;
import com.mycillin.partner.modul.chat.firebaseGet.ModelResultFirebaseGet;
import com.mycillin.partner.modul.home.cancelAdapterList.ModelRestCancelReason;
import com.mycillin.partner.modul.home.cancelAdapterList.ModelRestCancelReasonData;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    assert modelRestCancelReason != null;
                    for (ModelRestCancelReasonData modelRestCancelReasonData : modelRestCancelReason.getResult().getData()) {
                        cancelReasonList.add(modelRestCancelReasonData.getCancelReasonDesc());
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
        builder.setTitle("Alasan pembatalan survey");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                batal[0] = items[item];
            }
        });
        builder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(HomeVisitDetailActivity.this, "Berhasil " + batal[0], Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }
}
