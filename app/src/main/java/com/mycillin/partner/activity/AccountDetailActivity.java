package com.mycillin.partner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.mycillin.partner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountDetailActivity extends AppCompatActivity {

    @BindView(R.id.accountDetailActivity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.accountDetailActivity_ll_professionalDetail)
    LinearLayout professionalDetailLayout;
    @BindView(R.id.accountDetailActivity_el_expandableLayout)
    ExpandableLayout professionalDetailExpandableLayout;

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
