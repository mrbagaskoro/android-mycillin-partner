package com.mycillin.partner.modul.todo.completion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.badoualy.stepperindicator.StepperIndicator;
import com.mycillin.partner.R;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.PatientManager;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import timber.log.Timber;

public class CompleteRequestActivity extends AppCompatActivity {

    @BindView(R.id.completeRequestActivity_viewPager)
    ViewPager viewPager;
    @BindView(R.id.completeRequestActivity_stepperIndicator)
    StepperIndicator stepperIndicator;

    @BindView(R.id.completeRequestActivity_toolbar)
    Toolbar toolbar;

    private MenuItem menuFinish;
    private static Fragment completeCheckUpResultFragment;
    private static Fragment completeDiagnoseFragment;
    private static Fragment completeMedicalActionsFragment;
    private static Fragment completePrescriptionsFragment;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private SessionManager sessionManager;
    private PatientManager patientManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_request);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.completeRequestActivity_title);
        }
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(CompleteRequestActivity.this);
        sessionManager = new SessionManager(getApplicationContext());
        patientManager = new PatientManager(getApplicationContext());

        completeCheckUpResultFragment = new CompleteCheckUpResultFragment();
        completeDiagnoseFragment = new CompleteDiagnoseFragment();
        completeMedicalActionsFragment = new CompleteMedicalActionsFragment();
        completePrescriptionsFragment = new CompletePrescriptionsFragment();

        setupViewPager(viewPager);

        stepperIndicator.setViewPager(viewPager, true);
        stepperIndicator.setStepCount(viewPager.getAdapter().getCount());
        stepperIndicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                viewPager.setCurrentItem(step, true);
                Timber.tag("#8#8#").d("onStepClicked: %s", step);
            }
        });

        viewPager.setVisibility(View.VISIBLE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 3) {
                    menuFinish.setVisible(true);
                } else {
                    menuFinish.setVisible(false);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(4);
        adapter.addFragment(completeCheckUpResultFragment);
        adapter.addFragment(completeDiagnoseFragment);
        adapter.addFragment(completeMedicalActionsFragment);
        adapter.addFragment(completePrescriptionsFragment);

        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        menuFinish = menu.findItem(R.id.action_save);
        menuFinish.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:
                validasiSave();
                break;
        }
        return true;
    }

    private void validasiSave() {
        final String systole = ((EditText) findViewById(R.id.accountDetailActivity_et_systole)).getText().toString();
        final String diastole = ((EditText) findViewById(R.id.accountDetailActivity_et_diastole)).getText().toString();
        final String bodyTemp = ((EditText) findViewById(R.id.accountDetailActivity_et_bodyTemperature)).getText().toString();
        final String bloodSugar = ((EditText) findViewById(R.id.accountDetailActivity_et_blood_sugar)).getText().toString();
        final String cholesterolLevel = ((EditText) findViewById(R.id.accountDetailActivity_et_cholesterol_level)).getText().toString();
        final String physicalCond = ((EditText) findViewById(R.id.accountDetailActivity_et_physicalCondition)).getText().toString();
        final String diagnosisInformation = ((EditText) findViewById(R.id.completeDiagnoseFragment_et_diagnosisInformation)).getText().toString();
        final String medicalActionOne = ((EditText) findViewById(R.id.completeMedicalActionsFragment_et_medicalActionOne)).getText().toString().split(" - ")[0];
        final String prescriptionType = ((EditText) findViewById(R.id.accountDetailActivity_et_prescriptionType)).getText().toString().split(" - ")[0];

        String message = " Cannot Empty\n";
        ArrayList<String> validasiNonDokumen = new ArrayList<>();
        if (systole.isEmpty()) {
            validasiNonDokumen.add("Systole" + message);
        }
        if (diastole.isEmpty()) {
            validasiNonDokumen.add("Diastole" + message);
        }
        if (bodyTemp.isEmpty()) {
            validasiNonDokumen.add("Body Temperature" + message);
        }
        if (bloodSugar.isEmpty()) {
            validasiNonDokumen.add("Blood Sugar" + message);
        }

        if (physicalCond.isEmpty()) {
            validasiNonDokumen.add("Physical Condition" + message);
        }
        if (diagnosisInformation.isEmpty()) {
            validasiNonDokumen.add("Diagnose Information" + message);
        }
        if (medicalActionOne.isEmpty()) {
            validasiNonDokumen.add("Medical Action" + message);
        }
        if (prescriptionType.isEmpty()) {
            validasiNonDokumen.add("Prescription" + message);
        }

        if (validasiNonDokumen.size() > 0) {
            DialogHelper.showDialog(mHandler, CompleteRequestActivity.this, "Warning!", "Error :\n" + validasiNonDokumen.toString().replace("[", "").replace("]", "").replace(", ", ""), false);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBarHandler.show();
                }
            });
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", sessionManager.getUserId());
            params.put("booking_id", patientManager.getPatientBookingId());
            params.put("action_type_id", medicalActionOne);
            params.put("body_temperature", bodyTemp);
            params.put("blood_sugar_level", bloodSugar);
            params.put("cholesterol_level", cholesterolLevel);
            params.put("blood_press_upper", systole);
            params.put("blood_press_lower", diastole);
            params.put("patient_condition", physicalCond);
            params.put("diagnosa", diagnosisInformation);
            params.put("prescription_status", "Y");
            params.put("prescription_type_id", prescriptionType);

            JSONObject jsonObject = new JSONObject(params);

            Timber.tag("###").d("Completion: %s", jsonObject);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(Configs.URL_REST_CLIENT + "partner_task_completed/")
                    .post(body)
                    .addHeader("content-type", "application/json; charset=utf-8")
                    .addHeader("Authorization", sessionManager.getUserToken())
                    .build();

            Timber.tag("###").d("Completion: %s", sessionManager.getUserToken());

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBarHandler.hide();
                        }
                    });
                    DialogHelper.showDialog(mHandler, CompleteRequestActivity.this, "Warning Send Token Fbase", "Connection Problem, Please Try Again Later." + e, false);
                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                    @SuppressWarnings("ConstantConditions") String result = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBarHandler.hide();
                        }
                    });
                    if (response.isSuccessful()) {
                        Timber.tag("#8#8#").d("onResponse: SIP Complete%s", result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.has("result")) {
                                boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                                if (status) {
                                    DialogHelper.showDialog(mHandler, CompleteRequestActivity.this, "Status", "Data Complete", true);
                                } else {
                                    String message = jsonObject.getJSONObject("result").getString("message");
                                    DialogHelper.showDialog(mHandler, CompleteRequestActivity.this, "Warning", "Completion Error : " + message, false);
                                }
                            } else {
                                DialogHelper.showDialog(mHandler, CompleteRequestActivity.this, "Warning", "Please Try Again", false);
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
                            DialogHelper.showDialog(mHandler, CompleteRequestActivity.this, "Warning", "Completion Error : " + message, false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add("");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
