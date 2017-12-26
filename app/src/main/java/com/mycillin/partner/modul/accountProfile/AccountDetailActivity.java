package com.mycillin.partner.modul.accountProfile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.mvc.imagepicker.ImagePicker;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.accountProfile.model.expertise.ModelRestExpertise;
import com.mycillin.partner.modul.accountProfile.model.expertise.ModelRestExpertiseData;
import com.mycillin.partner.modul.accountProfile.model.profession.ModelRestProfession;
import com.mycillin.partner.modul.accountProfile.model.profession.ModelRestProfessionData;
import com.mycillin.partner.modul.searchResult.SearchResultActivity;
import com.mycillin.partner.modul.searchResult.adapterList.SearchResultList;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.PartnerAPI;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.RestClient;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class AccountDetailActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_GET_PROFESSION = 1111;
    public static final int REQUEST_CODE_GET_EXPERTISE = 1112;
    public final int REQUEST_CODE_PLACE_AUTOCOMPLETE = 9001;

    @BindView(R.id.accountDetailActivity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.accountDetailActivity_ll_professionalDetail)
    LinearLayout professionalDetailLayout;
    @BindView(R.id.accountDetailActivity_ll_identityDetail)
    LinearLayout identityDetailLayout;
    @BindView(R.id.accountDetailActivity_ll_location)
    LinearLayout locationLayout;
    @BindView(R.id.accountDetailActivity_ll_profile)
    LinearLayout profileDetailLayout;

    @BindView(R.id.accountDetailActivity_el_expandableLayout)
    ExpandableLayout professionalDetailExpandableLayout;
    @BindView(R.id.accountDetailActivity_el_expandableLayout_identity)
    ExpandableLayout professionalDetailExpandableLayoutIdentity;
    @BindView(R.id.accountDetailActivity_el_expandableLayout_location)
    ExpandableLayout locationExpandableLayoutIdentity;
    @BindView(R.id.accountDetailActivity_el_expandableLayout_desc)
    ExpandableLayout descDetailExpandableLayoutIdentity;


    @BindView(R.id.accountDetailActivity_ib_addInsurance_data)
    ImageButton ibExpandableLayoutIdentity;
    @BindView(R.id.accountDetailActivity_ib_addInsurance)
    ImageButton ibExpandableLayoutProfession;
    @BindView(R.id.accountDetailActivity_ib_addInsurance_location)
    ImageButton ibExpandableLayoutLocation;
    @BindView(R.id.accountDetailActivity_ib_addInsurance_desc)
    ImageButton ibExpandableLayoutDesc;

    @BindView(R.id.accountDetailActivity_tv_fullName)
    TextView tvFullName;
    @BindView(R.id.accountDetailActivity_tv_profession)
    TextView tvProfession;
    @BindView(R.id.accountDetailActivity_tv_sip)
    TextView tvSip;
    @BindView(R.id.accountDetailActivity_tv_str)
    TextView tvStr;

    @BindView(R.id.accountDetailActivity_et_email)
    EditText edtxEmail;
    @BindView(R.id.accountDetailActivity_et_fullName)
    EditText edtxFullName;
    @BindView(R.id.accountDetailActivity_et_address)
    EditText edtxAddress;
    @BindView(R.id.accountDetailActivity_et_phone)
    EditText edtxPhone;
    @BindView(R.id.accountDetailActivity_rg_genderRg)
    RadioGroup rgGender;
    @BindView(R.id.accountDetailActivity_rb_genderMaleRb)
    RadioButton rbMale;
    @BindView(R.id.accountDetailActivity_rb_genderFemaleRb)
    RadioButton rbFemale;
    @BindView(R.id.accountDetailActivity_et_dob)
    EditText edtxDateOfBirth;
    @BindView(R.id.accountDetailActivity_et_professionCategory)
    EditText edtxProfessionCategory;
    @BindView(R.id.accountDetailActivity_et_areaOfExpertise)
    EditText edtxExpertise;
    @BindView(R.id.accountDetailActivity_et_areaOfWork)
    EditText edtxWorkArea;
    @BindView(R.id.accountDetailActivity_et_yearsOfPractice)
    EditText edtxYearPractice;
    @BindView(R.id.accountDetailActivity_et_institutionName)
    EditText edtxInstitutionName;
    @BindView(R.id.accountDetailActivity_et_professionDescription)
    EditText edtxProffesionDesc;
    @BindView(R.id.accountDetailActivity_et_practiceAddress)
    EditText edtxWorkAddress;

    @BindView(R.id.accountDetailActivity_rg_location)
    RadioGroup rgLocation;
    @BindView(R.id.accountDetailActivity_bt_location)
    Button btnSetLocation;


    private CircleImageView ivAvatar;
    private PartnerAPI partnerAPI;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private SessionManager sessionManager;

    private double selectedCurrentLatitude = 0.0;
    private double selectedCurrentLongitude = 0.0;
    private double selectedSearchedLatitude;
    private double selectedSearchedLongitude;
    private boolean isMapsLocation = false;

    private List<SearchResultList> searchResultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);
        partnerAPI = RestClient.getPartnerRestInterfaceNoToken();
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(this);
        sessionManager = new SessionManager(getApplicationContext());
        edtxYearPractice.setInputType(InputType.TYPE_CLASS_NUMBER);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.accountDetailActivity_title);
        }

        professionalDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                professionalDetailExpandableLayout.toggle();
            }
        });

        ibExpandableLayoutProfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                professionalDetailExpandableLayout.toggle();
            }
        });

        identityDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                professionalDetailExpandableLayoutIdentity.toggle();
            }
        });

        ibExpandableLayoutIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                professionalDetailExpandableLayoutIdentity.toggle();
            }
        });

        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationExpandableLayoutIdentity.toggle();
            }
        });

        ibExpandableLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationExpandableLayoutIdentity.toggle();
            }
        });

        profileDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descDetailExpandableLayoutIdentity.toggle();
            }
        });

        ibExpandableLayoutDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descDetailExpandableLayoutIdentity.toggle();
            }
        });

        edtxWorkArea.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtxAddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtxInstitutionName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtxInstitutionName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtxWorkAddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        rgLocation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton;

                if (i == R.id.accountDetailActivity_rb_gps) {
                    checkedRadioButton = radioGroup.findViewById(i);
                    if (checkedRadioButton.isChecked()) {
                        isMapsLocation = false;
                    }
                } else if (i == R.id.accountDetailActivity_rb_maps) {
                    checkedRadioButton = radioGroup.findViewById(i);
                    if (checkedRadioButton.isChecked()) {
                        isMapsLocation = true;
                    }
                }
            }
        });
        detailPartner();
    }

    private void getLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);

            assert locationManager != null;
            locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, true),
                    0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(final Location location) {
                            selectedCurrentLatitude = location.getLatitude();
                            selectedCurrentLongitude = location.getLongitude();

                            String latitudeLocation = selectedCurrentLatitude + "";
                            String longitudeLocation = selectedCurrentLongitude + "";
                            Snackbar.make(getWindow().getDecorView().getRootView(), location.getLatitude() + "", Snackbar.LENGTH_LONG).show();
                            edtxWorkAddress.setText(getString(R.string.itemConcat, latitudeLocation, longitudeLocation));
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.accountDetailActivity_bt_location)
    public void onSetLocationClicked() {
        if (isMapsLocation) {
            try {
                edtxWorkAddress.setText("");
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(Place.TYPE_COUNTRY)
                        .setCountry("ID")
                        .build();

                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .setFilter(typeFilter)
                        .build(AccountDetailActivity.this);

                startActivityForResult(intent, REQUEST_CODE_PLACE_AUTOCOMPLETE);

            } catch (GooglePlayServicesRepairableException e) {
                Snackbar.make(getWindow().getDecorView().getRootView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
            } catch (GooglePlayServicesNotAvailableException e) {
                Snackbar.make(getWindow().getDecorView().getRootView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        } else {
            String latitudeLocation = selectedCurrentLatitude + "";
            String longitudeLocation = selectedCurrentLongitude + "";
            edtxWorkAddress.setText(getString(R.string.itemConcat, latitudeLocation, longitudeLocation));
            getLocation();
        }
        //ImagePicker.pickImage(AccountDetailActivity.this, "Select Image From :");
    }

    @OnClick(R.id.accountDetailActivity_iv_userAvatar)
    public void onAvatarClicked() {
        //ImagePicker.pickImage(AccountDetailActivity.this, "Select Image From :");
    }

    @OnClick(R.id.accountDetailActivity_et_dob)
    public void onDateOfBirthClicked() {
        MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(1900, 0, 1);
        MonthAdapter.CalendarDay maxDate = new MonthAdapter.CalendarDay(1999, 0, 1);
        new CalendarDatePickerDialogFragment()
                .setDateRange(minDate, maxDate)
                .setPreselectedDate(1999, 0, 1)
                .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedEndDate = Calendar.getInstance();
                        selectedEndDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                        edtxDateOfBirth.setText(dateFormatter.format(selectedEndDate.getTime()));
                    }
                })
                .show(getSupportFragmentManager(), "Date Of Birth");
    }

    private void fillDoctorAvatar(String profilePhoto) {
        ivAvatar = findViewById(R.id.accountDetailActivity_iv_userAvatar);
        if (!profilePhoto.equals("null") && !profilePhoto.isEmpty()) {
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .circleCrop();
            Glide.with(this)
                    .load(profilePhoto)
                    .apply(requestOptions)
                    .into(ivAvatar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        MenuItem menuFinish = menu.findItem(R.id.action_save);
        menuFinish.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final RadioButton rbSelectedAddressType = findViewById(rgGender.getCheckedRadioButtonId());
        if (id == R.id.action_save) {
            if (rbSelectedAddressType == null) {
                DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Warning", "Please Choose Your Gender.", false);
            } else {
                new AlertDialog.Builder(AccountDetailActivity.this)
                        .setTitle("Info")
                        .setMessage("Are You Sure To Update Your Account Information ?")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doUpdateAccount();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // CLOSE
                            }
                        })
                        .show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doUpdateAccount() {

        final RadioButton rbSelectedAddressType = findViewById(rgGender.getCheckedRadioButtonId());
        //String mEmail = edtxEmail.getText().toString().trim();
        final String mFullName = edtxFullName.getText().toString().trim();
        String mUserAddress = edtxAddress.getText().toString().trim();
        String mPhoneNumber = edtxPhone.getText().toString().trim();
        String fmDOB = edtxDateOfBirth.getText().toString().trim();
        String mProfessionCategory = edtxProfessionCategory.getText().toString().trim();
        String mAreaExpertise = edtxExpertise.getText().toString().trim().split(" - ")[0];
        String mWorkArea = edtxWorkArea.getText().toString().trim();
        String mWorkYears = edtxYearPractice.getText().toString().trim();
        String mInstitution = edtxInstitutionName.getText().toString().trim();

        String mWorkDesc = edtxProffesionDesc.getText().toString().trim();
        String mPracticeAddress = edtxWorkAddress.getText().toString().trim();
        final String gender = rbSelectedAddressType.getText().toString();
        String jenisKelamin = "";
        switch (gender) {
            case "Male":
            case "Pria":
                jenisKelamin = "L";
                break;
            case "Female":
            case "Wanita":
                jenisKelamin = "P";
                break;
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", sessionManager.getUserId());
        params.put("full_name", mFullName);
        params.put("gender", jenisKelamin);
        params.put("address", mUserAddress);
        params.put("mobile_number", mPhoneNumber);
        params.put("dob", "");
        params.put("no_SIP", "");
        params.put("SIP_berakhir", "");
        params.put("no_STR", "");
        params.put("STR_berakhir", "");
        params.put("partner_type_id", mProfessionCategory);
        params.put("spesialisasi_id", mAreaExpertise);
        params.put("wilayah_kerja", mWorkArea);
        params.put("profile_desc", mWorkDesc);
        params.put("lama_professi", mWorkYears.equals("") ? "0" : Integer.parseInt(mWorkYears));
        params.put("alamat_praktik", mPracticeAddress);
        params.put("map_praktik", "");
        params.put("nama_institusi", mInstitution);

        JSONObject jsonObject = new JSONObject(params);

        Timber.tag("###").d("doUpdateAccount: %s", jsonObject);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "complete_account_partner/")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Warning", "Connection Problem, Please Try Again Later." + e, true);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                @SuppressWarnings("ConstantConditions") String result = response.body().string();
                Timber.tag("###").d("onResponse: %s", result);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                if (response.isSuccessful()) {
                    DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Info", "Account Updated", true);
                    sessionManager.setKeyCustomerName(mFullName);
                    /*try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("result")) {
                            boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                            if (status) {
                                DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Info", "Account Updated", true);
                                sessionManager.setKeyCustomerName(mFullName);
                            } else {
                                String message = jsonObject.getJSONObject("result").getString("message");
                                DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Warning", message, false);
                            }
                        } else {
                            DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Warning", "Please Try Again", false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String message;
                        if (jsonObject.has("result")) {
                            message = jsonObject.getJSONObject("result").getString("message");
                        } else {
                            message = jsonObject.getString("message");
                        }
                        DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Warning", message, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @OnClick(R.id.accountDetailActivity_et_professionCategory)
    public void onProfessionCategoryClicked() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        partnerAPI.getProfession().enqueue(new Callback<ModelRestProfession>() {
            @Override
            public void onResponse(@NonNull Call<ModelRestProfession> call, @NonNull Response<ModelRestProfession> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                searchResultList.clear();
                ModelRestProfession modelRestProfessions = response.body();
                if (response.isSuccessful()) {
                    assert modelRestProfessions != null;
                    for (ModelRestProfessionData modelRestProfessionData : modelRestProfessions.getResult().getData()) {
                        searchResultList.add(new SearchResultList("Code", modelRestProfessionData.getPartnerTypeId(), "Profession", modelRestProfessionData.getPartnerTypeDesc(),
                                "", "", "", "", "", "", "", ""));
                    }
                    Intent intent = new Intent(AccountDetailActivity.this, SearchResultActivity.class);
                    intent.putParcelableArrayListExtra(SearchResultActivity.EXTRA_SEARCH_DATA, (ArrayList<? extends Parcelable>) searchResultList);
                    intent.putExtra(SearchResultActivity.EXTRA_SEARCH_REQUEST_CODE, REQUEST_CODE_GET_PROFESSION);
                    startActivityForResult(intent, REQUEST_CODE_GET_PROFESSION);
                } else {
                    DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Error", modelRestProfessions + "", false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelRestProfession> call, @NonNull final Throwable t) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Error", "Connection problem " + t, false);
                    }
                });
            }
        });
    }

    @OnClick(R.id.accountDetailActivity_et_areaOfExpertise)
    public void onAreaOfExpertiseClicked() {
        String professionID = edtxProfessionCategory.getText().toString().trim();
        if (professionID.isEmpty()) {
            DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Warning", "Please Choose Your Profession", false);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBarHandler.show();
                }
            });
            HashMap<String, String> params = new HashMap<>();
            params.put("partner_type_id", professionID.split(" - ")[0]);

            partnerAPI.getExpertise(params).enqueue(new Callback<ModelRestExpertise>() {
                @Override
                public void onResponse(@NonNull Call<ModelRestExpertise> call, @NonNull Response<ModelRestExpertise> response) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBarHandler.hide();
                        }
                    });
                    searchResultList.clear();
                    ModelRestExpertise modelRestExpertise = response.body();
                    if (response.isSuccessful()) {
                        assert modelRestExpertise != null;
                        for (ModelRestExpertiseData modelRestExpertise1 : modelRestExpertise.getResult().getData()) {
                            searchResultList.add(new SearchResultList("Code", modelRestExpertise1.getSpesialisasiId(), "Area Of Expertise", modelRestExpertise1.getSpesialisasiDesc(),
                                    "", "", "", "", "", "", "", ""));
                        }
                        Intent intent = new Intent(AccountDetailActivity.this, SearchResultActivity.class);
                        intent.putParcelableArrayListExtra(SearchResultActivity.EXTRA_SEARCH_DATA, (ArrayList<? extends Parcelable>) searchResultList);
                        intent.putExtra(SearchResultActivity.EXTRA_SEARCH_REQUEST_CODE, REQUEST_CODE_GET_EXPERTISE);
                        startActivityForResult(intent, REQUEST_CODE_GET_EXPERTISE);
                    } else {
                        DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Error", modelRestExpertise + "", false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ModelRestExpertise> call, @NonNull final Throwable t) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBarHandler.hide();
                            DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Error", "Connection problem " + t, false);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        String item1 = "";
        String item2 = "";
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode != 234) {
                item1 = data.getStringExtra("ITEM_1");
                item2 = data.getStringExtra("ITEM_2");
            }
            switch (requestCode) {
                case REQUEST_CODE_GET_PROFESSION:
                    edtxProfessionCategory.setText(getString(R.string.itemConcat, item1, item2));
                    edtxExpertise.setText("");
                    break;
                case REQUEST_CODE_GET_EXPERTISE:
                    edtxExpertise.setText(getString(R.string.itemConcat, item1, item2));
                    break;
                case REQUEST_CODE_PLACE_AUTOCOMPLETE:
                    //Snackbar.make(getWindow().getDecorView().getRootView(), resultCode, Snackbar.LENGTH_LONG).show();
                    if (resultCode == RESULT_OK) {
                        Place place = PlaceAutocomplete.getPlace(this, data);

                        selectedSearchedLatitude = place.getLatLng().latitude;
                        selectedSearchedLongitude = place.getLatLng().longitude;
                        edtxWorkAddress.setText(place.getAddress());
                    }
                    break;
                case 234:
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        Bitmap bmp = ImagePicker.getImageFromResult(getApplicationContext(), requestCode, resultCode, data);
                        //todoUpload
                        if (bmp != null) {
                            ivAvatar.setImageBitmap(bmp);
                        }
                    } else {
                        DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Warning", "Warning", false);
                    }
                    break;
            }
        }
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

        Timber.tag("####").d("Detail Partner %s", jsonObject);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "detail_partner/")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Warning", "Please Try Again : " + e.getMessage(), true);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull final okhttp3.Response response) throws IOException {
                @SuppressWarnings("ConstantConditions") final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Timber.tag("####").d("Detail Partner %s", jsonObject);
                                boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                                if (status) {
                                    JSONArray result = jsonObject.getJSONObject("result").getJSONArray("data");
                                    final JSONObject data = result.getJSONObject(0);
                                    final String email = data.optString("email");
                                    final String fullName = data.optString("full_name");
                                    final String address = data.optString("address");
                                    final String mobileNo = data.optString("mobile_no");
                                    final String gender = data.optString("gender");
                                    final String dob = data.optString("dob").replace("null", ""); //1999-01-01
                                    final String partnerTypeId = data.optString("partner_type_id");
                                    final String partnerTypeDesc = data.optString("partner_type_desc");
                                    final String spesialisasiId = data.optString("spesialisasi_id");
                                    final String spesialisasiDesc = data.optString("spesialisasi_desc");

                                    final String wilayahKerja = data.optString("wilayah_kerja");
                                    final String yearProfession = data.optString("lama_professi");
                                    final String noSip = data.optString("no_SIP");
                                    final String noStr = data.optString("no_STR");
                                    final String institutionName = data.optString("nama_institusi");
                                    final String profileDesc = data.optString("profile_desc");
                                    final String addressPractice = data.optString("alamat_praktik");
                                    final String profilePhoto = data.optString("profile_photo");

                                    fillDoctorAvatar(profilePhoto);

                                    SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                                    Date tgldob_ = null;

                                    if (!dob.isEmpty()) {
                                        tgldob_ = dateParse.parse(dob);
                                    }

                                    final String finalDateDob = tgldob_ != null ? dateFormatter.format(tgldob_) : "";

                                    edtxEmail.setText(email.replace("null", ""));
                                    edtxFullName.setText(fullName.replace("null", ""));
                                    edtxAddress.setText(address.replace("null", ""));
                                    edtxPhone.setText(mobileNo.replace("null", ""));

                                    switch (gender) {
                                        case "L":
                                            rbMale.setChecked(true);
                                            break;
                                        case "P":
                                            rbFemale.setChecked(true);
                                            break;
                                    }

                                    edtxDateOfBirth.setText(finalDateDob);
                                    edtxProfessionCategory.setText(getString(R.string.itemConcat, partnerTypeId.replace("null", ""), partnerTypeDesc.replace("null", "")));
                                    edtxExpertise.setText(getString(R.string.itemConcat, spesialisasiId.replace("null", ""), spesialisasiDesc.replace("null", "")));
                                    edtxWorkArea.setText(wilayahKerja.replace("null", ""));
                                    edtxYearPractice.setText(yearProfession.replace("null", ""));
                                    edtxInstitutionName.setText(institutionName.replace("null", ""));
                                    edtxProffesionDesc.setText(profileDesc.replace("null", ""));
                                    edtxWorkAddress.setText(addressPractice.replace("null", ""));

                                    tvFullName.setText(fullName.replace("null", ""));
                                    tvProfession.setText(partnerTypeDesc.replace("null", ""));
                                    tvSip.setText(noSip.replace("null", ""));
                                    tvStr.setText(noStr.replace("null", ""));
                                }
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }
}
