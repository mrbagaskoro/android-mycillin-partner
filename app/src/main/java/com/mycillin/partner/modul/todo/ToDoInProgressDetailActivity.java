package com.mycillin.partner.modul.todo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.todo.completion.CompleteRequestActivity;
import com.mycillin.partner.modul.todo.completion.medicalRecord.MedicalRecordActivity;
import com.mycillin.partner.modul.todo.completion.requesterProfile.RequesterProfileActivity;
import com.mycillin.partner.util.DataHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoInProgressDetailActivity extends AppCompatActivity {

    public static String KEY_FLAG_PATIENT_NAME = "KEY_FLAG_PATIENT_NAME";
    public static String KEY_FLAG_PATIENT_TYPE = "KEY_FLAG_PATIENT_TYPE";
    public static String KEY_FLAG_PATIENT_DATE = "KEY_FLAG_PATIENT_DATE";
    public static String KEY_FLAG_PATIENT_TIME = "KEY_FLAG_PATIENT_TIME";
    public static String KEY_FLAG_PATIENT_PIC = "KEY_FLAG_PATIENT_PIC";
    public static String KEY_FLAG_PATIENT_LOCATION = "KEY_FLAG_PATIENT_LOCATION";
    public static String KEY_FLAG_PATIENT_AGE = "KEY_FLAG_PATIENT_AGE";
    public static String KEY_FLAG_PATIENT_HEIGHT = "KEY_FLAG_PATIENT_HEIGHT";
    public static String KEY_FLAG_PATIENT_WEIGHT = "KEY_FLAG_PATIENT_WEIGHT";
    public static String KEY_FLAG_PATIENT_BLOOD_TYPE = "KEY_FLAG_PATIENT_BLOOD_TYPE";
    public static String KEY_FLAG_PATIENT_GENDER = "KEY_FLAG_PATIENT_GENDER";
    public static String KEY_FLAG_PATIENT_USER_ID = "KEY_FLAG_PATIENT_USER_ID";
    public static String KEY_FLAG_PATIENT_REL_ID = "KEY_FLAG_PATIENT_REL_ID";
    @BindView(R.id.toDoInProgressDetailActivity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.toDoInProgressDetailActivity_tv_patientName)
    TextView patientName;
    @BindView(R.id.toDoInProgressDetailActivity_tv_bookDate)
    TextView bookDate;
    @BindView(R.id.toDoInProgressDetailActivity_tv_bookType)
    TextView bookType;
    @BindView(R.id.toDoInProgressDetailActivity_bt_callBtn)
    Button callButton;
    @BindView(R.id.toDoInProgressDetailActivity_bt_completeBtn)
    Button completeButton;
    @BindView(R.id.toDoInProgressDetailActivity_tv_bookLocation)
    TextView bookLocation;
    private GoogleMap gMap;
    private String name;
    private String age;
    private String height;
    private String weight;
    private String bloodType;
    private String gender;
    private String relID;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_in_progress_detail);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.toDoInProgressDetail_title);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.toDoInProgressDetailActivity_fr_mapFragment);
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
        bookDate.setText(getString(R.string.itemConcat2, getIntent().getStringExtra(KEY_FLAG_PATIENT_DATE), getIntent().getStringExtra(KEY_FLAG_PATIENT_TIME)));
        bookType.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_TYPE));
        bookLocation.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_LOCATION));

        name = getIntent().getStringExtra(KEY_FLAG_PATIENT_NAME);
        age = getIntent().getStringExtra(KEY_FLAG_PATIENT_AGE);
        height = getIntent().getStringExtra(KEY_FLAG_PATIENT_HEIGHT);
        weight = getIntent().getStringExtra(KEY_FLAG_PATIENT_WEIGHT);
        bloodType = getIntent().getStringExtra(KEY_FLAG_PATIENT_BLOOD_TYPE);
        gender = getIntent().getStringExtra(KEY_FLAG_PATIENT_GENDER);
        userID = getIntent().getStringExtra(KEY_FLAG_PATIENT_USER_ID);
        relID = getIntent().getStringExtra(KEY_FLAG_PATIENT_REL_ID);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoInProgressDetailActivity.this, CompleteRequestActivity.class);
                startActivity(intent);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "085777255225"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.in_progress_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_requester_profile) {
            String gender_;
            switch (gender) {
                case "L":
                case "M":
                    gender_ = getString(R.string.accountDetailActivity_maleDesc);
                    break;
                default:
                    gender_ = getString(R.string.accountDetailActivity_femaleDesc);
                    break;

            }
            DataHelper.name = name;
            DataHelper.age = age;
            DataHelper.height = height;
            DataHelper.weight = weight;
            DataHelper.bloodType = bloodType;
            DataHelper.gender = gender_;
            Intent intent = new Intent(ToDoInProgressDetailActivity.this, RequesterProfileActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.action_medical_record) {
            DataHelper.userID = userID;
            DataHelper.relID = relID;
            DataHelper.height = height;
            DataHelper.weight = weight;
            Intent intent = new Intent(ToDoInProgressDetailActivity.this, MedicalRecordActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
