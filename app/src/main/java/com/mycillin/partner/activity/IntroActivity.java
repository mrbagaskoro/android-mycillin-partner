package com.mycillin.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.mycillin.partner.R;
import com.mycillin.partner.fragment.intro.IntroOneFragment;
import com.mycillin.partner.fragment.intro.IntroThreeFragment;
import com.mycillin.partner.fragment.intro.IntroTwoFragment;
import com.mycillin.partner.util.ApplicationPreferencesManager;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(IntroOneFragment.newInstance(R.layout.fragment_intro_one));
        addSlide(IntroTwoFragment.newInstance(R.layout.fragment_intro_two));
        addSlide(IntroThreeFragment.newInstance(R.layout.fragment_intro_three));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        ApplicationPreferencesManager applicationPreferencesManager = new ApplicationPreferencesManager(getApplicationContext());
        applicationPreferencesManager.introDone();

        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        ApplicationPreferencesManager applicationPreferencesManager = new ApplicationPreferencesManager(getApplicationContext());
        applicationPreferencesManager.introDone();

        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
