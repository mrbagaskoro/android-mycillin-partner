package com.mycillin.partner.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mycillin.partner.R;
import com.mycillin.partner.restful.PartnerAPI;
import com.mycillin.partner.restful.RestClient;
import com.mycillin.partner.restful.cancelReason.ModelRestCancelReason;
import com.mycillin.partner.restful.cancelReason.ModelRestCancelReasonData;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeReservationDetailActivity extends AppCompatActivity {

    @BindView(R.id.homeReservationDetailActivity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.homeReservationDetailActivity_fab_callFAB)
    FloatingActionButton callBtn;
    @BindView(R.id.homeReservationDetailActivity_fab_cancelFAB)
    FloatingActionButton cancelBtn;
    @BindView(R.id.homeReservationDetailActivity_fab_startFAB)
    FloatingActionButton startBtn;

    @BindView(R.id.homeReservationDetailActivity_tv_patientName)
    TextView patientName;
    @BindView(R.id.homeReservationDetailActivity_tv_bookDate)
    TextView bookDate;
    @BindView(R.id.homeReservationDetailActivity_tv_bookType)
    TextView bookType;

    private GoogleMap gMap;
    private PartnerAPI partnerAPI;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private ArrayList<String> cancelReasonList;

    public static String KEY_FLAG_PATIENT_NAME = "KEY_FLAG_PATIENT_NAME";
    public static String KEY_FLAG_PATIENT_TYPE = "KEY_FLAG_PATIENT_TYPE";
    public static String KEY_FLAG_PATIENT_DATE = "KEY_FLAG_PATIENT_DATE";
    public static String KEY_FLAG_PATIENT_TIME = "KEY_FLAG_PATIENT_TIME";
    public static String KEY_FLAG_PATIENT_PIC = "KEY_FLAG_PATIENT_PIC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_reservation_detail);
        ButterKnife.bind(this);

        partnerAPI = RestClient.getPartnerRestInterfaceNoToken();
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(this);

        toolbar.setTitle(R.string.reservationRequestDetail_title);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.homeReservationDetailActivity_fr_mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;

                LatLng bapindo = new LatLng(-6.224190, 106.80791);
                gMap.addMarker(new MarkerOptions().position(bapindo).title("Plaza Bapindo"));
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bapindo, 15.0f));
            }
        });

        patientName.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_NAME));
        bookDate.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_DATE) + ", " + getIntent().getStringExtra(KEY_FLAG_PATIENT_TIME));
        bookType.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_TYPE));
    }

    @OnClick(R.id.homeReservationDetailActivity_fab_callFAB)
    public void onClickCall() {
        Intent intent = new Intent(HomeReservationDetailActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.homeReservationDetailActivity_fab_startFAB)
    public void onClickStart() {
        Intent intent = new Intent(HomeReservationDetailActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.homeReservationDetailActivity_fab_cancelFAB)
    public void oncClickCancel() {
        cancelReasonList = new ArrayList<>();
        final String[] batal = new String[1];
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
                        Log.d("###", "onResponse: " + cancelReasonList);
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("Alasan pembatalan survey");
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setSingleChoiceItems(cancelReasonList, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                batal[0] = cancelReasonList.toString();
                                //Toast.makeText(getContext(), items[item], Toast.LENGTH_SHORT).show();
                            }
                        });*/
                    }
                } else {
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
}
