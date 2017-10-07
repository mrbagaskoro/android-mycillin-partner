package com.mycillin.partner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.adapter.MedicalRecordAdapter;
import com.mycillin.partner.list.MedicalRecordList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicalRecordActivity extends AppCompatActivity {

    @BindView(R.id.medicalRecordActivity_et_patientName)
    TextView tvDropdwon;
    @BindView(R.id.medicalRecordActivity_rv_recyclerView)
    RecyclerView medicalRecordRecyclerView;


    @BindView(R.id.medicalRecordActivity_toolbar)
    Toolbar toolbar;

    private List<MedicalRecordList> medicalRecordLists = new ArrayList<>();
    private MedicalRecordAdapter medicalRecordAdapter;
    private ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.nav_medical_record);

        getMedicalRecordList();
    }

    private void getMedicalRecordList() {
        medicalRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        medicalRecordRecyclerView.setItemAnimator(new DefaultItemAnimator());

        medicalRecordLists.clear();
        medicalRecordLists.add(new MedicalRecordList("10", "JAN", "2017", "dr Andi Husada"));
        medicalRecordLists.add(new MedicalRecordList("22", "MAR", "2017", "dr Hotman Sitorus"));
        medicalRecordLists.add(new MedicalRecordList("07", "MAY", "2017", "dr Andi Husada"));
        medicalRecordLists.add(new MedicalRecordList("05", "JUL", "2017", "dr Andi Husada"));

        medicalRecordAdapter = new MedicalRecordAdapter(medicalRecordLists, MedicalRecordActivity.this);
        medicalRecordRecyclerView.setAdapter(medicalRecordAdapter);
        medicalRecordAdapter.notifyDataSetChanged();
    }
}
