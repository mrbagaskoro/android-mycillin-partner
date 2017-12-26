package com.mycillin.partner.modul.home.reservation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.chat.ChatActivity;
import com.mycillin.partner.modul.home.cancelAdapterList.ModelRestCancelReason;
import com.mycillin.partner.modul.home.cancelAdapterList.ModelRestCancelReasonData;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.PartnerAPI;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.RestClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeReservationDetailActivity extends AppCompatActivity {

    public static String KEY_FLAG_PATIENT_NAME = "KEY_FLAG_PATIENT_NAME";
    public static String KEY_FLAG_PATIENT_TYPE = "KEY_FLAG_PATIENT_TYPE";
    public static String KEY_FLAG_PATIENT_DATE = "KEY_FLAG_PATIENT_DATE";
    public static String KEY_FLAG_PATIENT_TIME = "KEY_FLAG_PATIENT_TIME";
    public static String KEY_FLAG_PATIENT_PIC = "KEY_FLAG_PATIENT_PIC";
    public static String KEY_FLAG_PATIENT_LOCATION = "KEY_FLAG_PATIENT_LOCATION";
    @BindView(R.id.homeReservationDetailActivity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.homeReservationDetailActivity_bt_call)
    Button callBtn;
    @BindView(R.id.homeReservationDetailActivity_bt_reject)
    Button cancelBtn;
    @BindView(R.id.homeReservationDetailActivity_bt_chat)
    Button chatBtn;
    @BindView(R.id.homeReservationDetailActivity_bt_confirmed)
    Button confirmBtn;
    @BindView(R.id.homeReservationDetailActivity_tv_patientName)
    TextView patientName;
    @BindView(R.id.homeReservationDetailActivity_tv_bookDate)
    TextView bookDate;
    @BindView(R.id.homeReservationDetailActivity_tv_bookType)
    TextView bookType;
    @BindView(R.id.homeReservationDetailActivity_tv_bookLocation)
    TextView bookLocation;
    private GoogleMap gMap;
    private PartnerAPI partnerAPI;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private ArrayList<String> cancelReasonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_reservation_detail);
        ButterKnife.bind(this);

        partnerAPI = RestClient.getPartnerRestInterfaceNoToken();
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(this);

        toolbar.setTitle(R.string.reservationRequestDetail_title);

        patientName.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_NAME));
        bookDate.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_DATE) + ", " + getIntent().getStringExtra(KEY_FLAG_PATIENT_TIME));
        bookType.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_TYPE));
        bookLocation.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_LOCATION));
    }

    @OnClick(R.id.homeReservationDetailActivity_bt_call)
    public void onClickCall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "085777255225"));
        startActivity(intent);
    }

    @OnClick(R.id.homeReservationDetailActivity_bt_chat)
    public void onClickStart() {
        Intent intent = new Intent(HomeReservationDetailActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.homeReservationDetailActivity_bt_reject)
    public void oncClickCancel() {
        cancelReason();
    }

    @OnClick(R.id.homeReservationDetailActivity_bt_confirmed)
    public void onConfirmedClicked() {
        //todo confirmed
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
                Log.d("###", "onResponse: Cancel 2");
                ModelRestCancelReason modelRestCancelReason = response.body();
                if (response.isSuccessful()) {
                    Log.d("###", "onResponse: Cancel 3");
                    assert modelRestCancelReason != null;
                    for (ModelRestCancelReasonData modelRestCancelReasonData : modelRestCancelReason.getResult().getData()) {
                        Log.d("###", "onResponse: Cancel 4");
                        cancelReasonList.add(modelRestCancelReasonData.getCancelReasonDesc());
                    }
                    Log.d("###", "onResponse: TES " + cancelReasonList);
                    cancelReasonDialog(cancelReasonList);
                } else {
                    Log.d("###", "onResponse: Cancel 5");
                    DialogHelper.showDialog(mHandler, HomeReservationDetailActivity.this, "Error", modelRestCancelReason + "", false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelRestCancelReason> call, @NonNull final Throwable t) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        DialogHelper.showDialog(mHandler, HomeReservationDetailActivity.this, "Error", "Connection problem " + t, false);
                    }
                });
            }
        });
    }

    private void cancelReasonDialog(ArrayList<String> cancelReasonList) {
        final String[] batal = new String[1];
        final String[] items = cancelReasonList.toArray(new String[cancelReasonList.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeReservationDetailActivity.this);
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
                        Toast.makeText(HomeReservationDetailActivity.this, "Berhasil " + batal[0], Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(HomeReservationDetailActivity.this, "Batal", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
