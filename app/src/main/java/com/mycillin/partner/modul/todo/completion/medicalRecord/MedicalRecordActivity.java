package com.mycillin.partner.modul.todo.completion.medicalRecord;

import android.content.Intent;
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
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.modul.todo.completion.medicalRecord.adapterList.MedicalRecordAdapter;
import com.mycillin.partner.modul.todo.completion.medicalRecord.adapterList.MedicalRecordList;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DataHelper;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.RecyclerTouchListener;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class MedicalRecordActivity extends AppCompatActivity {

    @BindView(R.id.medicalRecordActivity_et_patientName)
    TextView tvDropdwon;
    @BindView(R.id.medicalRecordActivity_rv_recyclerView)
    RecyclerView medicalRecordRecyclerView;
    @BindView(R.id.medicalRecordActivity_toolbar)
    Toolbar toolbar;

    private SessionManager sessionManager;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private List<MedicalRecordList> medicalRecordLists = new ArrayList<>();
    private MedicalRecordAdapter medicalRecordAdapter;
    private ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(getApplicationContext());
        mProgressBarHandler = new ProgressBarHandler(MedicalRecordActivity.this);
        mHandler = new Handler(Looper.getMainLooper());
        toolbar.setTitle(R.string.nav_medical_record);

        getMedicalRecordListDetail();
        getMedicalRecordList();
    }

    private void getMedicalRecordList() {
        medicalRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        medicalRecordRecyclerView.setItemAnimator(new DefaultItemAnimator());
        medicalRecordLists.clear();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", DataHelper.userID);
        data.put("relation_id", DataHelper.relID);

        JSONObject jsonObject = new JSONObject(data);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "list_medical_record1/")
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
                DialogHelper.showDialog(mHandler, MedicalRecordActivity.this, "Warning", "Connection Problem, Please Try Again Later." + e.getMessage(), false);
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


                                /*"created_date": "2017-10-31 18:21:25",
                                        "partner_id": "pLJNOsgi9UMXZ0V",
                                        "partner_name": "DR IRIYANTO SPD",
                                        "record_id": "100000017",
                                        "user_id": "PBhJEj5TAZgtSoO",
                                        "service_type_desc": "IN HOUSE DOCTOR",
                                        "body_temperature": "31",
                                        "blood_sugar_level": "200",
                                        "cholesterol_level": "310",
                                        "blood_press_upper": "74",
                                        "blood_press_lower": "121",
                                        "patient_condition": "pucat dan muntah-muntah",
                                        "diagnosa": "anemia akut",
                                        "prescription_status": "Y",
                                        "prescription_id": "21",
                                        "prescription_type_desc": "RESEP FISIK"*/
                                final String createdDate = data.getJSONObject(i).optString("created_date").split(" ")[0];
                                final String partnerName = data.getJSONObject(i).optString("partner_name").trim();
                                final String partnerID = data.getJSONObject(i).optString("partner_id").trim();
                                final String recordID = data.getJSONObject(i).optString("record_id").trim();
                                final String userID = data.getJSONObject(i).optString("user_id").trim();
                                final String serviceDesc = data.getJSONObject(i).optString("service_type_desc").trim();
                                final String bodyTemp = data.getJSONObject(i).optString("body_temperature").trim();
                                final String bloodSugar = data.getJSONObject(i).optString("blood_sugar_level").trim();
                                final String bloodCholesterol = data.getJSONObject(i).optString("cholesterol_level").trim();
                                final String diastol = data.getJSONObject(i).optString("blood_press_upper").trim();
                                final String systol = data.getJSONObject(i).optString("blood_press_lower").trim();
                                final String patientCondition = data.getJSONObject(i).optString("patient_condition").trim();
                                final String diagnose = data.getJSONObject(i).optString("diagnosa").trim();
                                final String prescriptionStatus = data.getJSONObject(i).optString("prescription_status").trim();
                                final String prescriptionID = data.getJSONObject(i).optString("prescription_id").trim();
                                final String prescriptionDesc = data.getJSONObject(i).optString("prescription_type_desc").trim();

                                SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM", Locale.US);

                                Date monthDate_;
                                final String day;
                                final String month;
                                final String year;
                                if (!createdDate.equals("null") && !createdDate.isEmpty()) {
                                    day = createdDate.split("-")[2];
                                    year = createdDate.split("-")[0];
                                    monthDate_ = dateParse.parse(createdDate);
                                    month = monthDate_ != null ? dateFormatter.format(monthDate_) : "";
                                } else {
                                    day = "";
                                    month = "";
                                    year = "";
                                }

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        medicalRecordLists.add(new MedicalRecordList(day, month, year, partnerName, DataHelper.height, DataHelper.weight, partnerID, recordID, userID, serviceDesc, bodyTemp, bloodSugar, bloodCholesterol, diastol, systol, patientCondition, diagnose, prescriptionStatus, prescriptionID, prescriptionDesc));
                                        medicalRecordAdapter = new MedicalRecordAdapter(medicalRecordLists, MedicalRecordActivity.this);
                                        medicalRecordRecyclerView.setAdapter(medicalRecordAdapter);
                                        medicalRecordAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    } catch (JSONException | ParseException e) {
                        Timber.tag("###").d("onResponseror: %s", e.getMessage());
                    }
                }
            }
        });
    }

    public void getMedicalRecordListDetail() {
        medicalRecordRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), medicalRecordRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MedicalRecordList list = medicalRecordLists.get(position);
                Intent intent = new Intent(getApplicationContext(), MedicalRecordDetailActivity.class);
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_DATE, list.getDay() + " " + list.getMonth() + " " + list.getYear());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_DOCTOR, list.getDoctorName());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_HEIGHT, list.getHeight());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_WEIGHT, list.getWeight());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_PARTNER_ID, list.getPartnerID());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_RECORD_ID, list.getRecordID());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_USER_ID, list.getUserID());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_SERVICE_DESC, list.getServiceDesc());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_BODY_TEMP, list.getBodyTemp());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_BLOOD_SUGAR, list.getBloodSugar());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_BLOOD_CHOLESTEROL, list.getBloodCholesterol());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_DIASTOL, list.getDiastol());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_SYSTOL, list.getSystol());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_PATIENT_CONDITION, list.getPatientCondition());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_DIAGNOSE, list.getDiagnose());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_PRESCRIPTION_STATUS, list.getPrescriptionStatus());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_PRESCRIPTION_ID, list.getPrescriptionID());
                intent.putExtra(MedicalRecordDetailActivity.INTENT_KEY_PRESCRIPTION_DESC, list.getPrescriptionDesc());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
}
