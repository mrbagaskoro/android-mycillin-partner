package com.mycillin.partner.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.mycillin.partner.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountDetailActivity extends AppCompatActivity {

    @BindView(R.id.accountDetailActivity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.accountDetailActivity_ll_professionalDetail)
    LinearLayout professionalDetailLayout;
    @BindView(R.id.accountDetailActivity_el_expandableLayout)
    ExpandableLayout professionalDetailExpandableLayout;

    private CircleImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.accountDetailActivity_title);

        professionalDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                professionalDetailExpandableLayout.toggle();
            }
        });
        fillDoctorAvatar();
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
