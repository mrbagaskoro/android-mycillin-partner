package com.mycillin.partner.modul.accountProfile;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.mvc.imagepicker.ImagePicker;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.searchResult.SearchResultActivity;
import com.mycillin.partner.modul.searchResult.adapterList.SearchResultList;
import com.mycillin.partner.util.PartnerAPI;
import com.mycillin.partner.util.RestClient;
import com.mycillin.partner.modul.accountProfile.model.expertise.ModelRestExpertise;
import com.mycillin.partner.modul.accountProfile.model.expertise.ModelRestExpertiseData;
import com.mycillin.partner.modul.accountProfile.model.profession.ModelRestProfession;
import com.mycillin.partner.modul.accountProfile.model.profession.ModelRestProfessionData;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    @BindView(R.id.accountDetailActivity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.accountDetailActivity_ll_professionalDetail)
    LinearLayout professionalDetailLayout;
    @BindView(R.id.accountDetailActivity_el_expandableLayout)
    ExpandableLayout professionalDetailExpandableLayout;
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
    @BindView(R.id.accountDetailActivity_et_permittNumber)
    EditText edtxSIPP;
    @BindView(R.id.accountDetailActivity_et_professionDescription)
    EditText edtxProffesionDesc;
    @BindView(R.id.accountDetailActivity_et_practiceAddress)
    EditText edtxWorkAddress;
    @BindView(R.id.accountDetailActivity_cb_isAgree)
    CheckBox cbIsAgree;

    private CircleImageView ivAvatar;
    private MenuItem menuFinish;
    private PartnerAPI partnerAPI;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private List<SearchResultList> searchResultList = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);
        partnerAPI = RestClient.getPartnerRestInterfaceNoToken();
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(this);
        sessionManager = new SessionManager(getApplicationContext());
        edtxEmail.setText(sessionManager.getUserEmail());
        edtxFullName.setText(sessionManager.getUserFullName());
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

        cbIsAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    menuFinish.setVisible(true);
                } else {
                    menuFinish.setVisible(false);
                }
            }
        });

        fillDoctorAvatar();
    }

    @OnClick(R.id.accountDetailActivity_iv_userAvatar)
    public void onAvatarClicked() {
        ImagePicker.pickImage(AccountDetailActivity.this, "Select Image From :");
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
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                        edtxDateOfBirth.setText(dateFormatter.format(selectedEndDate.getTime()));
                    }
                })
                .show(getSupportFragmentManager(), "Date Of Birth");
    }

    private void fillDoctorAvatar() {
        ivAvatar = findViewById(R.id.accountDetailActivity_iv_userAvatar);
        //// TODO: 05/11/2017 FROM SERVICE
        Picasso.with(getApplicationContext())
                .load("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Bill_Gates_in_WEF%2C_2007.jpg/220px-Bill_Gates_in_WEF%2C_2007.jpg")
               /* .transform(new RoundedTransformation(80, 0))*/
                .resize(150, 150)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .centerCrop()
                .into(ivAvatar);
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
        int id = item.getItemId();

        if (id == R.id.action_save) {
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doUpdateAccount() {

        final RadioButton rbSelectedAddressType = findViewById(rgGender.getCheckedRadioButtonId());
        String mEmail = edtxEmail.getText().toString().trim();
        final String mFullName = edtxFullName.getText().toString().trim();
        String mUserAddress = edtxAddress.getText().toString().trim();
        String mPhoneNumber = edtxPhone.getText().toString().trim();
        String mDOB = edtxDateOfBirth.getText().toString().trim();
        String mProfessionCategory = edtxProfessionCategory.getText().toString().trim();
        String mAreaExpertise = edtxExpertise.getText().toString().trim();
        String mWorkArea = edtxWorkArea.getText().toString().trim();
        String mWorkYears = edtxYearPractice.getText().toString().trim();
        String mPermitNUmber = edtxSIPP.getText().toString().trim();
        String mWorkDesc = edtxProffesionDesc.getText().toString().trim();
        String mPracticeAddress = edtxWorkAddress.getText().toString().trim();
        final String gender = rbSelectedAddressType.getText().toString();
        String jenisKelamin = "";
        switch (gender) {
            case "Male":
            case "Female":
                jenisKelamin = "L";
                break;
            case "FEMALE":
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
        params.put("dob", mDOB);
        params.put("no_SIP", mPermitNUmber);
        params.put("SIP_berakhir", "");
        params.put("no_STR", "170");
        params.put("STR_berakhir", "");
        params.put("partner_type_id", mProfessionCategory);
        params.put("spesialisasi_id", mAreaExpertise);
        params.put("wilayah_kerja", mWorkArea);
        params.put("profile_desc", mWorkDesc);
        params.put("lama_professi", mWorkYears.equals("") ? "0" : Integer.parseInt(mWorkYears));
        params.put("alamat_praktik", mPracticeAddress);
        params.put("map_praktik", "");
        params.put("nama_institusi", "");

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
                DialogHelper.showDialog(mHandler, AccountDetailActivity.this, "Warning", "Connection Problem, Please Try Again Later." + e, false);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                if (response.isSuccessful()) {
                    try {
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
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        partnerAPI.getExpertise().enqueue(new Callback<ModelRestExpertise>() {
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
                    break;
                case REQUEST_CODE_GET_EXPERTISE:
                    edtxExpertise.setText(getString(R.string.itemConcat, item1, item2));
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
}
