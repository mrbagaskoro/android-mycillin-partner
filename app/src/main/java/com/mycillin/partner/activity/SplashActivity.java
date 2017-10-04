package com.mycillin.partner.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ahmedjazzar.rosetta.LanguageSwitcher;
import com.ahmedjazzar.rosetta.LanguagesListDialogFragment;
import com.mycillin.partner.BuildConfig;
import com.mycillin.partner.R;
import com.mycillin.partner.util.ApplicationPreferencesManager;
import com.mycillin.partner.util.LanguageOptions;

import java.util.HashSet;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splashScreen_tv_version)
    TextView tvVersion;

    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        defineLanguage();

        tvVersion.setText(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);

        final ApplicationPreferencesManager applicationPreferencesManager = new ApplicationPreferencesManager(getApplicationContext());
        if (!applicationPreferencesManager.isSelectLanguage()) {
            DialogFragment newFragment = SplashActivity.ChangeLanguageDialogFragment.newInstance(R.string.app_name, position);
            newFragment.setCancelable(false);
            newFragment.show(getSupportFragmentManager(), "dialog");
        }
        else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                public void run() {
                    if(applicationPreferencesManager.isIntroDone()) {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, PermissionCheckActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 2000); // 2000 milliseconds delay
        }
    }

    private void defineLanguage() {
        // This is the locale that you wanna your app to launch with.
        Locale firstLaunchLocale = new Locale("en");

        // You can use a HashSet<String> instead and call 'setSupportedStringLocales()' :)
        HashSet<Locale> supportedLocales = new HashSet<>();
        supportedLocales.add(new Locale("in"));
        supportedLocales.add(firstLaunchLocale);

        // You can make the following object static so you can use the same reference in all app's
        // classes. static is much stable.
        LanguageSwitcher ls = new LanguageSwitcher(getApplicationContext(), firstLaunchLocale);
        ls.setSupportedLocales(supportedLocales);
    }

    public static class ChangeLanguageDialogFragment extends LanguagesListDialogFragment {

        public static ChangeLanguageDialogFragment newInstance(int title, int position) {
            ChangeLanguageDialogFragment frag = new ChangeLanguageDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            args.putInt("position", position);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");
            final int position = getArguments().getInt("position");

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setSingleChoiceItems(LanguageOptions.languageList, position, null);
            builder.setTitle(title);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    AlertDialog alert = (AlertDialog)dialogInterface;
                    int position = alert.getListView().getCheckedItemPosition();

                    onLanguageSelected(position);
                    onPositiveClick();

                    ApplicationPreferencesManager applicationPreferencesManager = new ApplicationPreferencesManager(getContext());
                    applicationPreferencesManager.selectLanguage();

                    Intent intent = new Intent(getActivity(), PermissionCheckActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            return builder.create();
        }
    }
}
