package com.mycillin.partner.modul.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.about.AboutFragment;
import com.mycillin.partner.modul.accountProfile.AccountActivity;
import com.mycillin.partner.modul.ewallet.EWalletFragment;
import com.mycillin.partner.modul.todo.ToDoParentFragment;
import com.mycillin.partner.util.BottomNavigationViewHelper;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DataHelper;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    private final String EXTRA_STATUS_AVAILABILITY = "available_id";

    private SessionManager sessionManager;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private Handler handler;
    private LocationManager locationManager;
    private Location lokasi;

    @BindView(R.id.accountActivity_sb_availability)
    SwitchButton sbAvaliability;
    @BindView(R.id.accountActivity_tv_status)
    TextView tvStatus;

    private boolean doubleBackToExitPressedOnce = false;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.nav_home:
                    tx.replace(R.id.mainActivity_fl_framecontainer, new HomeParentFragment());
                    tx.commit();
                    getSupportActionBar().setTitle(R.string.app_name);

                    return true;
                case R.id.nav_todo:
                    tx.replace(R.id.mainActivity_fl_framecontainer, new ToDoParentFragment());
                    tx.commit();
                    getSupportActionBar().setTitle(R.string.nav_todo);

                    return true;
                case R.id.nav_wallet:
                    tx.replace(R.id.mainActivity_fl_framecontainer, new EWalletFragment());
                    tx.commit();
                    getSupportActionBar().setTitle(R.string.nav_e_wallet);

                    return true;
                case R.id.nav_about:
                    tx.replace(R.id.mainActivity_fl_framecontainer, new AboutFragment());
                    tx.commit();
                    getSupportActionBar().setTitle(R.string.nav_about_mycillin);

                    return true;
            }

            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());
        ButterKnife.bind(this);
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(this);
        DataHelper.token = sessionManager.getUserToken();
        handler = new Handler(Looper.getMainLooper());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // -------------------------------------------------------------------------------------- //
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.mainActivity_fl_framecontainer, new HomeParentFragment());
        tx.commit();
        getSupportActionBar().setTitle(R.string.app_name);
        detailPartner();
        checkLocation();
        boolean isActive = sbAvaliability.isChecked();
        if (isActive) {
            tvStatus.setText("Available");
        } else {
            tvStatus.setText("Off");
        }
    }

    private void sendLongLatFunc() {
        double latitude = 0.0;
        double longitude = 0.0;
        if (getLocation() != null) {
            latitude = getLocation().getLatitude();
            longitude = getLocation().getLongitude();
            Timber.tag("getlatitude").d("%s", latitude);
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", sessionManager.getUserId());
        params.put("latitude", latitude);
        params.put("longitude", longitude);

        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "partner_loc_autoupdate/")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                final String msg = e.toString();
                DialogHelper.showDialog(handler, HomeActivity.this, "Info", msg, false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                @SuppressWarnings("ConstantConditions")
                String result = response.body().string();

                if (response.code() == 200) {
                    Timber.tag("result latlong").d(result);
                    // TODO SOMETHING

                } else {
                    Timber.tag("result latlong").d(result);
                }
            }
        });
    }

    @OnCheckedChanged(R.id.accountActivity_sb_availability)
    public void changeStatusAvail() {
        boolean isActive = sbAvaliability.isChecked();
        if (isActive) {
            tvStatus.setText("Available");
        } else {
            tvStatus.setText("Off");
        }
        String value = isActive ? "0" : "1";
        doToggleUpdate(value, EXTRA_STATUS_AVAILABILITY);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.pressBackAgainToLeave, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_account) {
            Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.action_invite) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shareIntent_subject));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareIntent_text) + sessionManager.getUserId());
            startActivity(Intent.createChooser(intent, getString(R.string.shareIntent_title)));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doToggleUpdate(final String value, final String status) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
                            case EXTRA_STATUS_AVAILABILITY:
                                if (value.equals("0")) {
                                    sbAvaliability.setChecked(false);
                                } else {
                                    sbAvaliability.setChecked(true);
                                }
                                break;
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                final String x = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        if (response.isSuccessful()) {
                            switch (status) {
                                case EXTRA_STATUS_AVAILABILITY:
                                    if (value.equals("0")) {
                                        sbAvaliability.setChecked(true);
                                    } else {
                                        sbAvaliability.setChecked(false);
                                    }
                                    break;
                            }
                        } else {
                            switch (status) {
                                case EXTRA_STATUS_AVAILABILITY:
                                    if (value.equals("0")) {
                                        sbAvaliability.setChecked(false);
                                    } else {
                                        sbAvaliability.setChecked(true);
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
                DialogHelper.showDialog(mHandler, HomeActivity.this, "Warning", "Please Try Again : " + e.getMessage(), false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                final String result = response.body().string();
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
                                    final String availability = data.optString("available_status");

                                    switch (availability) {
                                        case AccountActivity.EXTRA_STATUS_ON:
                                            sbAvaliability.setChecked(true);
                                            break;
                                        default:
                                            sbAvaliability.setChecked(false);
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

    private void checkLocation() {
        handler = new Handler(Looper.getMainLooper());
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            DialogHelper.showDialog(handler, HomeActivity.this, "Warning", "Aktifkan GPS", true);
        }
    }

    public Location getLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (isNetworkEnabled) {
                //  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, this);
                if (locationManager != null) {
                    lokasi = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            } else if (isGPSEnabled) {
                if (lokasi == null) {
                    //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                    if (locationManager != null) {
                        lokasi = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }
        }
        return lokasi;
    }

    @Override
    public void onLocationChanged(Location location) {
        sendLongLatFunc();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
