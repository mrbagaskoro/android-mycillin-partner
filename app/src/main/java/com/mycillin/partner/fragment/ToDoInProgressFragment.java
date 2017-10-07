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
import com.mycillin.partner.adapter.ToDoInProgressAdapter;
import com.mycillin.partner.list.ToDoInProgressList;
import com.mycillin.partner.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoInProgressFragment extends Fragment {

    @BindView(R.id.toDoInProgressFragment_rv_recyclerView)
    RecyclerView toDoInProgressRecyclerView;

    private List<ToDoInProgressList> toDoInProgressLists = new ArrayList<>();
    private ToDoInProgressAdapter toDoInProgressAdapter;

    public ToDoInProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_to_do_in_progress, container, false);
        ButterKnife.bind(this, rootView);

        getToDoInProgressList();
        
        return rootView;
    }

    public void getToDoInProgressList() {
        toDoInProgressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        toDoInProgressRecyclerView.setItemAnimator(new DefaultItemAnimator());

        toDoInProgressLists.clear();
        toDoInProgressLists.add(new ToDoInProgressList("pic_01.jpg", "Reno Rasiwara", "Visit request to home", "07 September 2017", "15.00 WIB"));
        toDoInProgressLists.add(new ToDoInProgressList("pic_01.jpg", "Tommi Asmara", "Visit request to home", "07 September 2017", "18.00 WIB"));

        toDoInProgressAdapter = new ToDoInProgressAdapter(toDoInProgressLists);
        toDoInProgressRecyclerView.setAdapter(toDoInProgressAdapter);
        toDoInProgressAdapter.notifyDataSetChanged();

        toDoInProgressRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), toDoInProgressRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToDoInProgressList list = toDoInProgressLists.get(position);

                /*Intent intent = new Intent(getContext(), ToDoInProgressDetailActivity.class);
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_NAME, list.getPatientName());
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_DATE, list.getBookDate());
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_TIME, list.getBookTime());
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_TYPE, list.getBookType());
                intent.putExtra(ToDoInProgressDetailActivity.KEY_FLAG_PATIENT_PIC, list.getPatientPic());
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
