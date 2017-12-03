package com.mycillin.partner.modul.todo.completion.requesterProfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.util.DataHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequesterProfileActivity extends AppCompatActivity {

    @BindView(R.id.requesterProfileActivity_tv_name)
    TextView tvName;
    @BindView(R.id.requesterProfileActivity_tv_gender)
    TextView tvGender;
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

        tvName.setText(DataHelper.name);
        tvGender.setText(DataHelper.gender);
        tvAge.setText(DataHelper.age);
        tvHeight.setText(DataHelper.height);
        tvWeight.setText(DataHelper.weight);
        tvBloodType.setText(DataHelper.bloodType);
    }
}
