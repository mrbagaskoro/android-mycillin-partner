package com.mycillin.partner.modul.todo.completion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.badoualy.stepperindicator.StepperIndicator;
import com.mycillin.partner.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompleteRequestActivity extends AppCompatActivity {

    @BindView(R.id.completeRequestActivity_viewPager)
    ViewPager viewPager;
    @BindView(R.id.completeRequestActivity_stepperIndicator)
    StepperIndicator stepperIndicator;

    @BindView(R.id.completeRequestActivity_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_request);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.completeRequestActivity_title);

        setupViewPager(viewPager);
        stepperIndicator.setViewPager(viewPager, true);
        stepperIndicator.setStepCount(viewPager.getAdapter().getCount());
        stepperIndicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                viewPager.setCurrentItem(step, true);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CompleteCheckUpResultFragment(), "");
        adapter.addFragment(new CompleteDiagnoseFragment(), "");
        adapter.addFragment(new CompleteMedicalActionsFragment(), "");
        adapter.addFragment(new CompletePrescriptionsFragment(), "");
        viewPager.setAdapter(adapter);
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

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
