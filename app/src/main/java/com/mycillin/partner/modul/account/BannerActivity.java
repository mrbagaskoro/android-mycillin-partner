package com.mycillin.partner.modul.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerActivity extends AppCompatActivity {

    public static final String EXTRA_STATUS_BASE_64 = "BASE_64";
    public static final String EXTRA_STATUS_URL = "URL";

    @BindView(R.id.BannerActivity_iv_banner)
    ImageView bannerIv;
    @BindView(R.id.BannerActivity_fab_close)
    FloatingActionButton closeFab;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ButterKnife.bind(this);

        String imageBanner = getIntent().getStringExtra(EXTRA_STATUS_BASE_64);
        imageUrl = getIntent().getStringExtra(EXTRA_STATUS_URL);

        if (!imageBanner.equals("")) {
            byte[] imageByteArray = Base64.decode(imageBanner, Base64.DEFAULT);
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher);

            Glide.with(BannerActivity.this)
                    .load(imageByteArray)
                    .apply(requestOptions)
                    .into(bannerIv);
        }
        bannerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageUrl.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(imageUrl));
                    startActivity(intent);
                }
            }
        });

        closeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BannerActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BannerActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
