package com.mycillin.partner.modul.todo.completion.medicalRecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycillin.partner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicalRecordDetailActivity extends AppCompatActivity {

    public static String INTENT_KEY_DATE = "INTENT_KEY_DATE";
    public static String INTENT_KEY_DOCTOR = "INTENT_KEY_DOCTOR";

    @BindView(R.id.medicalRecordDetailActivity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.medicalRecordDetailActivity_tv_date)
    TextView dateTxt;
    @BindView(R.id.medicalRecordDetailActivity_tv_doctor)
    TextView doctorTxt;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.medicalRecordDetailActivity_title);

        dateTxt.setText(getIntent().getStringExtra(INTENT_KEY_DATE));
        doctorTxt.setText(getIntent().getStringExtra(INTENT_KEY_DOCTOR));

        accordionMenu();
    }

    private void accordionMenu() {
        diagnoseTitleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diagnoseContentContainer.getVisibility() == View.VISIBLE) {
                    diagnoseContentContainer.setVisibility(View.GONE);
                }
                else {
                    diagnoseContentContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        resultTitleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultContentContainer.getVisibility() == View.VISIBLE) {
                    resultContentContainer.setVisibility(View.GONE);
                }
                else {
                    resultContentContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        actionTitleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(actionContentContainer.getVisibility() == View.VISIBLE) {
                    actionContentContainer.setVisibility(View.GONE);
                }
                else {
                    actionContentContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        prescriptionTitleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prescriptionContentContainer.getVisibility() == View.VISIBLE) {
                    prescriptionContentContainer.setVisibility(View.GONE);
                }
                else {
                    prescriptionContentContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
