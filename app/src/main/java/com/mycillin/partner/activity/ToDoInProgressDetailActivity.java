package com.mycillin.partner.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mycillin.partner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoInProgressDetailActivity extends AppCompatActivity {

    @BindView(R.id.toDoInProgressDetailActivity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.toDoInProgressDetailActivity_tv_patientName)
    TextView patientName;
    @BindView(R.id.toDoInProgressDetailActivity_tv_bookDate)
    TextView bookDate;
    @BindView(R.id.toDoInProgressDetailActivity_tv_bookType)
    TextView bookType;

    private GoogleMap gMap;

    public static String KEY_FLAG_PATIENT_NAME = "KEY_FLAG_PATIENT_NAME";
    public static String KEY_FLAG_PATIENT_TYPE = "KEY_FLAG_PATIENT_TYPE";
    public static String KEY_FLAG_PATIENT_DATE = "KEY_FLAG_PATIENT_DATE";
    public static String KEY_FLAG_PATIENT_TIME = "KEY_FLAG_PATIENT_TIME";
    public static String KEY_FLAG_PATIENT_PIC = "KEY_FLAG_PATIENT_PIC";

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
        bookDate.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_DATE) + ", " + getIntent().getStringExtra(KEY_FLAG_PATIENT_TIME));
        bookType.setText(getIntent().getStringExtra(KEY_FLAG_PATIENT_TYPE));
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
            Intent intent = new Intent(ToDoInProgressDetailActivity.this, RequesterProfileActivity.class);
            startActivity(intent);

            return true;
        }
        else if(id == R.id.action_medical_record) {
            /*Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);*/

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
