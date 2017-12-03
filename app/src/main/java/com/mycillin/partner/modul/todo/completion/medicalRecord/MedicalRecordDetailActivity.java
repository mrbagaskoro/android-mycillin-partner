package com.mycillin.partner.modul.todo.completion.medicalRecord;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.modul.todo.completion.medicalRecord.adapterList.PrescriptionAdapter;
import com.mycillin.partner.modul.todo.completion.medicalRecord.adapterList.PrescriptionList;
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

public class MedicalRecordDetailActivity extends AppCompatActivity {

    public static String INTENT_KEY_DATE = "INTENT_KEY_DATE";
    public static String INTENT_KEY_DOCTOR = "INTENT_KEY_DOCTOR";
    public static String INTENT_KEY_HEIGHT = "INTENT_KEY_HEIGHT";
    public static String INTENT_KEY_WEIGHT = "INTENT_KEY_WEIGHT";
    public static String INTENT_KEY_PARTNER_ID = "INTENT_KEY_PARTNER_ID";
    public static String INTENT_KEY_RECORD_ID = "INTENT_KEY_RECORD_ID";
    public static String INTENT_KEY_USER_ID = "INTENT_KEY_USER_ID";
    public static String INTENT_KEY_SERVICE_DESC = "INTENT_KEY_SERVICE_DESC";
    public static String INTENT_KEY_BODY_TEMP = "INTENT_KEY_BODY_TEMP";
    public static String INTENT_KEY_BLOOD_SUGAR = "INTENT_KEY_BLOOD_SUGAR";
    public static String INTENT_KEY_BLOOD_CHOLESTEROL = "INTENT_KEY_BLOOD_CHOLESTEROL";
    public static String INTENT_KEY_DIASTOL = "INTENT_KEY_DIASTOL";
    public static String INTENT_KEY_SYSTOL = "INTENT_KEY_SYSTOL";
    public static String INTENT_KEY_PATIENT_CONDITION = "INTENT_KEY_PATIENT_CONDITION";
    public static String INTENT_KEY_DIAGNOSE = "INTENT_KEY_DIAGNOSE";
    public static String INTENT_KEY_PRESCRIPTION_STATUS = "INTENT_KEY_PRESCRIPTION_STATUS";
    public static String INTENT_KEY_PRESCRIPTION_ID = "INTENT_KEY_PRESCRIPTION_ID";
    public static String INTENT_KEY_PRESCRIPTION_DESC = "INTENT_KEY_PRESCRIPTION_DESC";
    @BindView(R.id.medicalRecordDetailActivity_rv_prescription)
    RecyclerView prescriptionRecyclerView;
    @BindView(R.id.medicalRecordDetailActivity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.medicalRecordDetailActivity_tv_date)
    TextView dateTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_doctor)
    TextView doctorTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_diagnose)
    TextView diagnoseTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_result1)
    TextView systoleTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_result2)
    TextView diastoleTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_result3)
    TextView weightTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_result4)
    TextView bloodSugarTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_result5)
    TextView bloodCholesterolTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_result6)
    TextView otherConditionTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_action)
    TextView medicalActionTxt;
    @BindView(R.id.medicalRecordDetailActivity_ll_diagnoseTitleContainer)
    LinearLayout diagnoseTitleContainer;
    @BindView(R.id.medicalRecordDetailActivity_ll_diagnoseContentContainer)
    LinearLayout diagnoseContentContainer;
    @BindView(R.id.medicalRecordDetailActivity_ll_resultTitleContainer)
    LinearLayout resultTitleContainer;
    @BindView(R.id.medicalRecordDetailActivity_ll_resultContentContainer)
    LinearLayout resultContentContainer;
    @BindView(R.id.medicalRecordDetailActivity_ll_actionTitleContainer)
    LinearLayout actionTitleContainer;
    @BindView(R.id.medicalRecordDetailActivity_ll_actionContentContainer)
    LinearLayout actionContentContainer;
    @BindView(R.id.medicalRecordDetailActivity_ll_prescriptionTitleContainer)
    LinearLayout prescriptionTitleContainer;
    @BindView(R.id.medicalRecordDetailActivity_ll_prescriptionContentContainer)
    LinearLayout prescriptionContentContainer;
    private List<PrescriptionList> prescriptionListList = new ArrayList<>();
    private PrescriptionAdapter prescriptionAdapter;
    private SessionManager sessionManager;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_detail);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        mProgressBarHandler = new ProgressBarHandler(this);
        mHandler = new Handler(Looper.getMainLooper());
        prescriptionRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        prescriptionRecyclerView.setItemAnimator(new DefaultItemAnimator());
        prescriptionListList.clear();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.medicalRecordDetailActivity_title);

        dateTxt.setText(getIntent().getStringExtra(INTENT_KEY_DATE));
        doctorTxt.setText(getIntent().getStringExtra(INTENT_KEY_DOCTOR));
        diagnoseTxt.setText(getIntent().getStringExtra(INTENT_KEY_DIAGNOSE));
        systoleTxt.setText(getIntent().getStringExtra(INTENT_KEY_SYSTOL) + " mm/hg");
        diastoleTxt.setText(getIntent().getStringExtra(INTENT_KEY_DIASTOL) + " mm/hg");
        weightTxt.setText(getIntent().getStringExtra(INTENT_KEY_WEIGHT) + " Kg");
        bloodSugarTxt.setText(getIntent().getStringExtra(INTENT_KEY_BLOOD_SUGAR));
        bloodCholesterolTxt.setText(getIntent().getStringExtra(INTENT_KEY_BLOOD_CHOLESTEROL));
        otherConditionTxt.setText(getIntent().getStringExtra(INTENT_KEY_PATIENT_CONDITION));
        medicalActionTxt.setText(getIntent().getStringExtra(INTENT_KEY_SERVICE_DESC));

        accordionMenu();
        getDetailPrescription(getIntent().getStringExtra(INTENT_KEY_PRESCRIPTION_ID));
    }

    private void getDetailPrescription(String prescriptionID) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put("prescription_no", "21");

        JSONObject jsonObject = new JSONObject(data);
        Timber.tag("###").d("getDetailPrescription: %s", jsonObject);
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "detail_prescription1/")
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
                DialogHelper.showDialog(mHandler, MedicalRecordDetailActivity.this, "Warning", "Connection Problem, Please Try Again Later." + e.getMessage(), false);
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
                                String namaObat = data.getJSONObject(i).optString("nama_obat").trim();
                                String jumlahObat = data.getJSONObject(i).optString("jumlah_obat").trim();
                                String satuanDesc = data.getJSONObject(i).optString("satuan_obat_desc").trim();
                                final String dosisPemakaian = data.getJSONObject(i).optString("dosis_pemakaian").trim();

                                final String obatDesc = namaObat + " (" + jumlahObat + " " + satuanDesc + ")";
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        prescriptionListList.add(new PrescriptionList(obatDesc, dosisPemakaian));
                                        prescriptionAdapter = new PrescriptionAdapter(prescriptionListList, MedicalRecordDetailActivity.this);
                                        prescriptionRecyclerView.setAdapter(prescriptionAdapter);
                                        prescriptionAdapter.notifyDataSetChanged();
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

    private void accordionMenu() {
        diagnoseTitleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (diagnoseContentContainer.getVisibility() == View.VISIBLE) {
                    diagnoseContentContainer.setVisibility(View.GONE);
                } else {
                    diagnoseContentContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        resultTitleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultContentContainer.getVisibility() == View.VISIBLE) {
                    resultContentContainer.setVisibility(View.GONE);
                } else {
                    resultContentContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        actionTitleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionContentContainer.getVisibility() == View.VISIBLE) {
                    actionContentContainer.setVisibility(View.GONE);
                } else {
                    actionContentContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        prescriptionTitleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prescriptionContentContainer.getVisibility() == View.VISIBLE) {
                    prescriptionContentContainer.setVisibility(View.GONE);
                } else {
                    prescriptionContentContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
