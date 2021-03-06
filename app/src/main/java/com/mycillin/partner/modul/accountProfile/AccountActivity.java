package com.mycillin.partner.modul.accountProfile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kyleduo.switchbutton.SwitchButton;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.account.LoginActivity;
import com.mycillin.partner.modul.accountProfile.termsCondition.TermAndPrivacyPolicyActivity;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class AccountActivity extends AppCompatActivity {

    public static final String EXTRA_STATUS_ON = "1";
    private final String EXTRA_STATUS_HOUSE_VISIT = "visit_id";
    private final String EXTRA_STATUS_RESERVASI = "reservasi_id";
    private final String EXTRA_STATUS_CONSULTATION = "consul_id";
    private final String EXTRA_STATUS_BPJS = "BPJS_RCV_status";
    @BindView(R.id.accountActivity_ll_manageAccount)
    LinearLayout manageAccount;
    @BindView(R.id.accountActivity_ll_changePassword)
    LinearLayout changePassword;
    @BindView(R.id.accountActivity_ll_signOut)
    LinearLayout signOut;
    @BindView(R.id.accountActivity_ll_termsPrivacyPolicy)
    LinearLayout termsAndPrivacyPolicy;
    @BindView(R.id.accountActivity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.accountActivity_tv_userName)
    TextView tvDocterName;
    @BindView(R.id.accountActivity_sb_houseVisit)
    SwitchButton sbHouseVisit;
    @BindView(R.id.accountActivity_sb_reservation)
    SwitchButton sbReservation;
    @BindView(R.id.accountActivity_sb_consultation)
    SwitchButton sbConsultation;
    @BindView(R.id.accountActivity_sb_bpjs)
    SwitchButton sbBpjs;

    private SessionManager sessionManager;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.nav_account);
        sessionManager = new SessionManager(this);
        tvDocterName.setText(sessionManager.getUserFullName());

        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(AccountActivity.this);

        manageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, AccountDetailActivity.class);
                startActivity(intent);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, UpdatePassActivity.class);
                startActivity(intent);
            }
        });
        termsAndPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, TermAndPrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
        detailPartner();
    }

    private void fillDoctorAvatar(String profilePhoto) {
        CircleImageView ivAvatar = findViewById(R.id.accountActivity_iv_userAvatar);
        if (!profilePhoto.equals("null") && !profilePhoto.isEmpty()) {
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .circleCrop();
            Glide.with(this)
                    .load(profilePhoto)
                    .apply(requestOptions)
                    .into(ivAvatar);
        }
    }

    @OnClick(R.id.accountActivity_ll_signOut)
    public void signOutClicked() {
        final SessionManager sessionManager = new SessionManager(getApplicationContext());
        new AlertDialog.Builder(AccountActivity.this)
                .setTitle("Register")
                .setMessage("Are you sure to exit ?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionManager.logoutUser();
                        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @OnCheckedChanged(R.id.accountActivity_sb_houseVisit)
    public void doHomeVisit() {
        boolean isActive = sbHouseVisit.isChecked();
        String value = !isActive ? "0" : "1";
        doToggleUpdate(value, EXTRA_STATUS_HOUSE_VISIT);
    }

    @OnCheckedChanged(R.id.accountActivity_sb_consultation)
    public void doConsultation() {
        boolean isActive = sbConsultation.isChecked();
        String value = !isActive ? "0" : "1";
        doToggleUpdate(value, EXTRA_STATUS_CONSULTATION);

    }

    @OnCheckedChanged(R.id.accountActivity_sb_reservation)
    public void doReservation() {
        boolean isActive = sbReservation.isChecked();
        String value = !isActive ? "0" : "1";
        doToggleUpdate(value, EXTRA_STATUS_RESERVASI);
    }

    @OnCheckedChanged(R.id.accountActivity_sb_bpjs)
    public void doBpjs() {
        boolean isActive = sbBpjs.isChecked();
        String value = !isActive ? "0" : "1";
        doToggleUpdate(value, EXTRA_STATUS_BPJS);
    }

    private void doToggleUpdate(final String value, final String status) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        MediaType JSON = MediaType.parse("application/accjson; charset=utf-8");

        Map<String, Object> data = new HashMap<>();
        data.put("user_id", sessionManager.getUserId());
        data.put("status", status);
        data.put("value", value);

        JSONObject jsonObject = new JSONObject(data);

        Timber.tag("####").d("saveAddress: OBJEK %s", jsonObject);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "toggle_status_partner/")
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

                        switch (status) {
                            case EXTRA_STATUS_HOUSE_VISIT:
                                if (value.equals(EXTRA_STATUS_ON)) {
                                    sbHouseVisit.setChecked(false);
                                } else {
                                    sbHouseVisit.setChecked(true);
                                }
                                break;
                            case EXTRA_STATUS_RESERVASI:
                                if (value.equals(EXTRA_STATUS_ON)) {
                                    sbReservation.setChecked(false);
                                } else {
                                    sbReservation.setChecked(true);
                                }
                                break;
                            case EXTRA_STATUS_CONSULTATION:
                                if (value.equals(EXTRA_STATUS_ON)) {
                                    sbConsultation.setChecked(false);
                                } else {
                                    sbConsultation.setChecked(true);
                                }
                                break;
                            case EXTRA_STATUS_BPJS:
                                if (value.equals(EXTRA_STATUS_ON)) {
                                    sbBpjs.setChecked(false);
                                } else {
                                    sbBpjs.setChecked(true);
                                }
                                break;
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        if (response.isSuccessful()) {
                            switch (status) {
                                case EXTRA_STATUS_HOUSE_VISIT:
                                    if (value.equals(EXTRA_STATUS_ON)) {
                                        sbHouseVisit.setChecked(true);
                                    } else {
                                        sbHouseVisit.setChecked(false);
                                    }
                                    break;
                                case EXTRA_STATUS_RESERVASI:
                                    if (value.equals(EXTRA_STATUS_ON)) {
                                        sbReservation.setChecked(true);
                                    } else {
                                        sbReservation.setChecked(false);
                                    }
                                    break;
                                case EXTRA_STATUS_CONSULTATION:
                                    if (value.equals(EXTRA_STATUS_ON)) {
                                        sbConsultation.setChecked(true);
                                    } else {
                                        sbConsultation.setChecked(false);
                                    }
                                    break;
                                case EXTRA_STATUS_BPJS:
                                    if (value.equals(EXTRA_STATUS_ON)) {
                                        sbBpjs.setChecked(true);
                                    } else {
                                        sbBpjs.setChecked(false);
                                    }
                                    break;
                            }
                        } else {
                            switch (status) {
                                case EXTRA_STATUS_HOUSE_VISIT:
                                    if (value.equals(EXTRA_STATUS_ON)) {
                                        sbHouseVisit.setChecked(false);
                                    } else {
                                        sbHouseVisit.setChecked(true);
                                    }
                                    break;
                                case EXTRA_STATUS_RESERVASI:
                                    if (value.equals(EXTRA_STATUS_ON)) {
                                        sbReservation.setChecked(false);
                                    } else {
                                        sbReservation.setChecked(true);
                                    }
                                    break;
                                case EXTRA_STATUS_CONSULTATION:
                                    if (value.equals(EXTRA_STATUS_ON)) {
                                        sbConsultation.setChecked(false);
                                    } else {
                                        sbConsultation.setChecked(true);
                                    }
                                    break;
                                case EXTRA_STATUS_BPJS:
                                    if (value.equals(EXTRA_STATUS_ON)) {
                                        sbBpjs.setChecked(false);
                                    } else {
                                        sbBpjs.setChecked(true);
                                    }
                                    break;
                            }
                        }
                    }
                });
            }
        });
    }

    private void detailPartner() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        Map<String, Object> data = new HashMap<>();
        data.put("user_id", sessionManager.getUserId());

        JSONObject jsonObject = new JSONObject(data);

        Timber.tag("####").d("saveAddress: OBJEK %s", jsonObject);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "detail_partner/")
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
                DialogHelper.showDialog(mHandler, AccountActivity.this, "Warning", "Please Try Again : " + e.getMessage(), false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                @SuppressWarnings("ConstantConditions") final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Timber.tag("###").d("onResponseyyyyy: %s", jsonObject);
                                boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                                if (status) {
                                    JSONArray result = jsonObject.getJSONObject("result").getJSONArray("data");
                                    final JSONObject data = result.getJSONObject(0);
                                    final String reservIsOn = data.optString("status_reservasi");
                                    final String visitIsOn = data.optString("status_visit");
                                    final String consulIsOn = data.optString("status_consul");
                                    final String bpjsIsOn = data.optString("status_BPJS");
                                    final String profilePhoto = data.optString("profile_photo");

                                    fillDoctorAvatar(profilePhoto);

                                    switch (reservIsOn) {
                                        case EXTRA_STATUS_ON:
                                            sbReservation.setChecked(true);
                                            break;
                                        default:
                                            sbReservation.setChecked(false);
                                    }
                                    switch (visitIsOn) {
                                        case EXTRA_STATUS_ON:
                                            sbHouseVisit.setChecked(true);
                                            break;
                                        default:
                                            sbHouseVisit.setChecked(false);
                                    }
                                    switch (consulIsOn) {
                                        case EXTRA_STATUS_ON:
                                            sbConsultation.setChecked(true);
                                            break;
                                        default:
                                            sbConsultation.setChecked(false);
                                    }
                                    switch (bpjsIsOn) {
                                        case EXTRA_STATUS_ON:
                                            sbBpjs.setChecked(true);
                                            break;
                                        default:
                                            sbBpjs.setChecked(false);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tvDocterName.setText(sessionManager.getUserFullName());
    }
}