package com.mycillin.partner.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.mycillin.partner.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountDetailActivity extends AppCompatActivity {

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
    EditText edtxWorkCategory;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);

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

        fillDoctorAvatar();
    }

    @OnClick(R.id.accountDetailActivity_iv_userAvatar)
    public void onAvatarClicked() {
        //todo Upload
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
