package com.mycillin.partner.modul.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycillin.partner.BuildConfig;
import com.mycillin.partner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutFragment extends Fragment {

    @BindView(R.id.aboutFragment_tv_version)
    TextView version;

    @BindView(R.id.aboutFragment_ll_contact)
    LinearLayout contact;
    @BindView(R.id.aboutFragment_ll_facebook)
    LinearLayout facebook;
    @BindView(R.id.aboutFragment_ll_twitter)
    LinearLayout twitter;
    @BindView(R.id.aboutFragment_ll_instagram)
    LinearLayout instagram;
    @BindView(R.id.aboutFragment_ll_youtube)
    LinearLayout youtube;
    @BindView(R.id.aboutFragment_ll_playStore)
    LinearLayout playstore;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle(R.string.nav_about_mycillin);

        version.setText(getResources().getString(R.string.aboutFragment_version) + " " + BuildConfig.VERSION_NAME);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, "mycillin@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Please Fill This Body");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/mycillin.mycillin.9"));
                intent.setPackage("com.facebook.katana");
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mycillin.mycillin.9")));
                }
            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=mycillin")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/mycillin")));
                }
            }
        });


        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/mycillin"));
                intent.setPackage("com.instagram.android");
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com.com/mycillin")));
                }
            }
        });


        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:cSPfeTxl8Kw")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=cSPfeTxl8Kw")));
                }
            }
        });


        playstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.supercell.clashofclans")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.supercell.clashofclans")));
                }
            }
        });

        return rootView;
    }
}
