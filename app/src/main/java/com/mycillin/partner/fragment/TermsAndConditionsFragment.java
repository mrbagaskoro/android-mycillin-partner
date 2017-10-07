package com.mycillin.partner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.mycillin.partner.R;
import com.mycillin.partner.util.Configs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsAndConditionsFragment extends Fragment {

    @BindView(R.id.termsAndConditionsFragment_wv_webView)
    WebView webView;

    public TermsAndConditionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
        ButterKnife.bind(this, rootView);

        webView.loadUrl(Configs.URL_TERMS_AND_CONDITIONS);

        return rootView;
    }
}
