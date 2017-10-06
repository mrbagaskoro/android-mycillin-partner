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
import com.mycillin.partner.activity.HomeReservationDetailActivity;
import com.mycillin.partner.adapter.HomeReservationAdapter;
import com.mycillin.partner.list.HomeReservationList;
import com.mycillin.partner.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeReservationFragment extends Fragment {

    @BindView(R.id.homeReservationFragment_rv_recyclerView)
    RecyclerView homeReservationRecyclerView;

    private List<HomeReservationList> homeReservationLists = new ArrayList<>();
    private HomeReservationAdapter homeReservationAdapter;

    public HomeReservationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_reservation, container, false);
        ButterKnife.bind(this, rootView);

        getHomeReservationList();
        
        return rootView;
    }

    public void getHomeReservationList() {
        homeReservationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeReservationRecyclerView.setItemAnimator(new DefaultItemAnimator());

        homeReservationLists.clear();
        homeReservationLists.add(new HomeReservationList("pic_01.jpg", "Reno Rasiwara", "Reservation request", "07 September 2017", "15.00 WIB"));
        homeReservationLists.add(new HomeReservationList("pic_01.jpg", "Tommi Asmara", "Reservation request", "07 September 2017", "18.00 WIB"));

        homeReservationAdapter = new HomeReservationAdapter(homeReservationLists);
        homeReservationRecyclerView.setAdapter(homeReservationAdapter);
        homeReservationAdapter.notifyDataSetChanged();

        homeReservationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), homeReservationRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HomeReservationList list = homeReservationLists.get(position);

                Intent intent = new Intent(getContext(), HomeReservationDetailActivity.class);
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_NAME, list.getPatientName());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_DATE, list.getBookDate());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_TIME, list.getBookTime());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_TYPE, list.getBookType());
                intent.putExtra(HomeReservationDetailActivity.KEY_FLAG_PATIENT_PIC, list.getPatientPic());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
