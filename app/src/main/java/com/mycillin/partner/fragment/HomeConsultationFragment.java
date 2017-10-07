package com.mycillin.partner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycillin.partner.R;
import com.mycillin.partner.adapter.HomeConsultationAdapter;
import com.mycillin.partner.list.HomeConsultationList;
import com.mycillin.partner.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeConsultationFragment extends Fragment {

    @BindView(R.id.homeConsultationFragment_rv_recyclerView)
    RecyclerView homeConsultationRecyclerView;

    private List<HomeConsultationList> homeConsultationLists = new ArrayList<>();
    private HomeConsultationAdapter homeConsultationAdapter;

    public HomeConsultationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_consultation, container, false);
        ButterKnife.bind(this, rootView);

        getHomeConsultationList();
        
        return rootView;
    }

    public void getHomeConsultationList() {
        homeConsultationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeConsultationRecyclerView.setItemAnimator(new DefaultItemAnimator());

        homeConsultationLists.clear();
        homeConsultationLists.add(new HomeConsultationList("pic_01.jpg", "Reno Rasiwara", "Consultation request", "07 September 2017", "15.00 WIB"));
        homeConsultationLists.add(new HomeConsultationList("pic_01.jpg", "Tommi Asmara", "Consultation request", "07 September 2017", "18.00 WIB"));

        homeConsultationAdapter = new HomeConsultationAdapter(homeConsultationLists);
        homeConsultationRecyclerView.setAdapter(homeConsultationAdapter);
        homeConsultationAdapter.notifyDataSetChanged();

        homeConsultationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), homeConsultationRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HomeConsultationList list = homeConsultationLists.get(position);

                /*Intent intent = new Intent(getContext(), HomeConsultationDetailActivity.class);
                intent.putExtra(HomeConsultationDetailActivity.KEY_FLAG_PATIENT_NAME, list.getPatientName());
                intent.putExtra(HomeConsultationDetailActivity.KEY_FLAG_PATIENT_DATE, list.getBookDate());
                intent.putExtra(HomeConsultationDetailActivity.KEY_FLAG_PATIENT_TIME, list.getBookTime());
                intent.putExtra(HomeConsultationDetailActivity.KEY_FLAG_PATIENT_TYPE, list.getBookType());
                intent.putExtra(HomeConsultationDetailActivity.KEY_FLAG_PATIENT_PIC, list.getPatientPic());
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
