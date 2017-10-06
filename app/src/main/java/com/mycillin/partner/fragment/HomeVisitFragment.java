package com.mycillin.partner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycillin.partner.R;
import com.mycillin.partner.activity.HomeVisitDetailActivity;
import com.mycillin.partner.adapter.HomeVisitAdapter;
import com.mycillin.partner.list.HomeVisitList;
import com.mycillin.partner.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeVisitFragment extends Fragment {

    @BindView(R.id.homeVisitFragment_rv_recyclerView)
    RecyclerView homeVisitRecyclerView;

    private List<HomeVisitList> homeVisitLists = new ArrayList<>();
    private HomeVisitAdapter homeVisitAdapter;

    public HomeVisitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_visit, container, false);
        ButterKnife.bind(this, rootView);

        getHomeVisitList();

        return rootView;
    }

    public void getHomeVisitList() {
        homeVisitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeVisitRecyclerView.setItemAnimator(new DefaultItemAnimator());

        homeVisitLists.clear();
        homeVisitLists.add(new HomeVisitList("pic_01.jpg", "Reno Rasiwara", "Visit request to home", "07 September 2017", "15.00 WIB"));
        homeVisitLists.add(new HomeVisitList("pic_01.jpg", "Tommi Asmara", "Visit request to home", "07 September 2017", "18.00 WIB"));

        homeVisitAdapter = new HomeVisitAdapter(homeVisitLists);
        homeVisitRecyclerView.setAdapter(homeVisitAdapter);
        homeVisitAdapter.notifyDataSetChanged();

        homeVisitRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), homeVisitRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HomeVisitList list = homeVisitLists.get(position);

                Intent intent = new Intent(getContext(), HomeVisitDetailActivity.class);
                intent.putExtra(HomeVisitDetailActivity.KEY_FLAG_PATIENT_NAME, list.getPatientName());
                intent.putExtra(HomeVisitDetailActivity.KEY_FLAG_PATIENT_DATE, list.getBookDate());
                intent.putExtra(HomeVisitDetailActivity.KEY_FLAG_PATIENT_TIME, list.getBookTime());
                intent.putExtra(HomeVisitDetailActivity.KEY_FLAG_PATIENT_TYPE, list.getBookType());
                intent.putExtra(HomeVisitDetailActivity.KEY_FLAG_PATIENT_PIC, list.getPatientPic());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
