package com.mycillin.partner.modul.todo.completion.requesterProfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mycillin.partner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequesterProfileActivity extends AppCompatActivity {

    @BindView(R.id.requesterProfileActivity_tv_name)
    TextView tvName;
    @BindView(R.id.requesterProfileActivity_tv_age)
    TextView tvAge;
    @BindView(R.id.requesterProfileActivity_tv_height)
    TextView tvHeight;
    @BindView(R.id.requesterProfileActivity_tv_weight)
    TextView tvWeight;
    @BindView(R.id.requesterProfileActivity_tv_bloodType)
    TextView tvBloodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester_profile);
        ButterKnife.bind(this);
        detailPatient();
    }

    private void detailPatient() {

    }
}
