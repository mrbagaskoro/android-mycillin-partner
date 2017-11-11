package com.mycillin.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.adapter.SearchResultAdapter;
import com.mycillin.partner.list.SearchResultItem;
import com.mycillin.partner.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultActivity extends AppCompatActivity {
    public static final String EXTRA_SEARCH_DATA = "SEARCH_DATA";
    public static final String EXTRA_SEARCH_REQUEST_CODE = "SEARCH_REQUEST_CODE";

    public int reqCode;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchResult_TV_count)
    TextView tvResultsCount;
    @BindView(R.id.searchResult_RV_results)
    RecyclerView rvResults;
    @BindView(R.id.searchResult_ET_filter)
    EditText etFilter;

    private List<SearchResultItem> mSearchResultItems = new ArrayList<>();
    private SearchResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        reqCode = getIntent().getExtras().getInt(EXTRA_SEARCH_REQUEST_CODE);

        String title;
        switch (reqCode) {
            case AccountDetailActivity.REQUEST_CODE_GET_PROFESSION:
                title = "Profession Category";
                break;
            case AccountDetailActivity.REQUEST_CODE_GET_EXPERTISE:
                title = "Area Of Expertise";
                break;
            default:
                title = getIntent().getExtras().getString("SEARCH_KEY");
                break;
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(title);

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = etFilter.getText().toString();
                mAdapter.filter(text);
                tvResultsCount.setText(getString(R.string.result_list, mSearchResultItems.size()));
            }
        });

        mSearchResultItems.clear();
        mSearchResultItems = getIntent().getParcelableArrayListExtra(EXTRA_SEARCH_DATA);
        Collections.sort(mSearchResultItems, new Comparator<SearchResultItem>() {
            @Override
            public int compare(SearchResultItem x, SearchResultItem y) {
                return x.getItem1().compareTo(y.getItem1());
            }
        });

        rvResults.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvResults.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new SearchResultAdapter(mSearchResultItems, reqCode, SearchResultActivity.this);
        rvResults.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        rvResults.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvResults, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SearchResultItem resultList = mSearchResultItems.get(position);
                Intent intent = new Intent();
                intent.putExtra("ITEM_1", resultList.getItem1());
                intent.putExtra("ITEM_2", resultList.getItem2());
                intent.putExtra("ITEM_3", resultList.getItem3());
                intent.putExtra("ITEM_4", resultList.getItem4());
                intent.putExtra("ITEM_5", resultList.getItem5());
                intent.putExtra("ITEM_6", resultList.getItem6());
                setResult(reqCode, intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        tvResultsCount.setText(getString(R.string.result_list, mSearchResultItems.size()));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_clear_dropdown, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_clear:
                Intent intent = new Intent();
                intent.putExtra("ITEM_1", "");
                intent.putExtra("ITEM_2", "");
                intent.putExtra("ITEM_3", "");
                intent.putExtra("ITEM_4", "");
                intent.putExtra("ITEM_5", "");
                intent.putExtra("ITEM_6", "");
                setResult(reqCode, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
