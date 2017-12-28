package com.mycillin.partner.modul.todo.completion;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.badoualy.stepperindicator.StepperIndicator;
import com.mycillin.partner.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CompleteRequestActivity extends AppCompatActivity {

    @BindView(R.id.completeRequestActivity_viewPager)
    ViewPager viewPager;
    @BindView(R.id.completeRequestActivity_stepperIndicator)
    StepperIndicator stepperIndicator;

    @BindView(R.id.completeRequestActivity_toolbar)
    Toolbar toolbar;

    private MenuItem menuFinish;
    private static Fragment completeCheckUpResultFragment;
    private static Fragment completeDiagnoseFragment;
    private static Fragment completeMedicalActionsFragment;
    private static Fragment completePrescriptionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_request);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.completeRequestActivity_title);
        }

        completeCheckUpResultFragment = new CompleteCheckUpResultFragment();
        completeDiagnoseFragment = new CompleteDiagnoseFragment();
        completeMedicalActionsFragment = new CompleteMedicalActionsFragment();
        completePrescriptionsFragment = new CompletePrescriptionsFragment();

        setupViewPager(viewPager);

        stepperIndicator.setViewPager(viewPager, true);
        stepperIndicator.setStepCount(viewPager.getAdapter().getCount());
        stepperIndicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                viewPager.setCurrentItem(step, true);
                Timber.tag("#8#8#").d("onStepClicked: %s", step);
            }
        });

        viewPager.setVisibility(View.VISIBLE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 3) {
                    menuFinish.setVisible(true);
                } else {
                    menuFinish.setVisible(false);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(4);
        adapter.addFragment(completeCheckUpResultFragment);
        adapter.addFragment(completeDiagnoseFragment);
        adapter.addFragment(completeMedicalActionsFragment);
        adapter.addFragment(completePrescriptionsFragment);

        viewPager.setAdapter(adapter);

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
        switch (item.getItemId()) {

            case R.id.action_save:
                validasiSave();
                break;
        }
        return true;
    }

    private void validasiSave() {
        String systole = ((EditText) findViewById(R.id.accountDetailActivity_et_systole)).getText().toString();
        String diastole = ((EditText) findViewById(R.id.accountDetailActivity_et_diastole)).getText().toString();
        String bodyTemp = ((EditText) findViewById(R.id.accountDetailActivity_et_bodyTemperature)).getText().toString();
        String physicalCond = ((EditText) findViewById(R.id.accountDetailActivity_et_physicalCondition)).getText().toString();
        String patientComplaints = ((EditText) findViewById(R.id.accountDetailActivity_et_patientComplaints)).getText().toString();
        String additionalInfo = ((EditText) findViewById(R.id.accountDetailActivity_et_additionalInformation)).getText().toString();

        String diagnosisInformation = ((EditText) findViewById(R.id.completeDiagnoseFragment_et_diagnosisInformation)).getText().toString();
        String additionalInformation = ((EditText) findViewById(R.id.completeDiagnoseFragment_et_additionalInformation)).getText().toString();

        String medicalActionOne = ((EditText) findViewById(R.id.completeMedicalActionsFragment_et_medicalActionOne)).getText().toString();
        String medicalActionTwo = ((EditText) findViewById(R.id.completeMedicalActionsFragment_et_medicalActionTwo)).getText().toString();
        String medicalActionThree = ((EditText) findViewById(R.id.completeMedicalActionsFragment_et_medicalActionThree)).getText().toString();
        String medicalRecommendation = ((EditText) findViewById(R.id.completeMedicalActionsFragment_et_medicalRecommendation)).getText().toString();

        String prescriptionType = ((EditText) findViewById(R.id.accountDetailActivity_et_prescriptionType)).getText().toString();
        Drawable prescriptionImage = ((AppCompatImageView) findViewById(R.id.accountDetailActivity_iv_prescription)).getDrawable();


    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add("");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
