package com.mycillin.partner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
                Toast.makeText(getActivity(), getResources().getString(R.string.aboutFragment_contact), Toast.LENGTH_SHORT).show();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), getResources().getString(R.string.aboutFragment_facebook), Toast.LENGTH_SHORT).show();
            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), getResources().getString(R.string.aboutFragment_twitter), Toast.LENGTH_SHORT).show();
            }
        });


        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), getResources().getString(R.string.aboutFragment_instagram), Toast.LENGTH_SHORT).show();
            }
        });


        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), getResources().getString(R.string.aboutFragment_youtube), Toast.LENGTH_SHORT).show();
            }
        });


        playstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), getResources().getString(R.string.aboutFragment_playstore), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
