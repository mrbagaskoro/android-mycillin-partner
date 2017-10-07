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
import com.mycillin.partner.adapter.ToDoCompletedAdapter;
import com.mycillin.partner.list.ToDoCompletedList;
import com.mycillin.partner.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoCompletedFragment extends Fragment {

    @BindView(R.id.toDoCompletedFragment_rv_recyclerView)
    RecyclerView toDoCompletedRecyclerView;

    private List<ToDoCompletedList> toDoCompletedLists = new ArrayList<>();
    private ToDoCompletedAdapter toDoCompletedAdapter;

    public ToDoCompletedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_to_do_completed, container, false);
        ButterKnife.bind(this, rootView);

        getToDoCompletedList();

        return rootView;
    }

    public void getToDoCompletedList() {
        toDoCompletedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        toDoCompletedRecyclerView.setItemAnimator(new DefaultItemAnimator());

        toDoCompletedLists.clear();
        toDoCompletedLists.add(new ToDoCompletedList("pic_01.jpg", "Reno Rasiwara", "Visit request to home", "07 September 2017", "15.00 WIB"));
        toDoCompletedLists.add(new ToDoCompletedList("pic_01.jpg", "Tommi Asmara", "Visit request to home", "07 September 2017", "18.00 WIB"));

        toDoCompletedAdapter = new ToDoCompletedAdapter(toDoCompletedLists);
        toDoCompletedRecyclerView.setAdapter(toDoCompletedAdapter);
        toDoCompletedAdapter.notifyDataSetChanged();

        toDoCompletedRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), toDoCompletedRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ToDoCompletedList list = toDoCompletedLists.get(position);

                /*Intent intent = new Intent(getContext(), ToDoCompletedDetailActivity.class);
                intent.putExtra(ToDoCompletedDetailActivity.KEY_FLAG_PATIENT_NAME, list.getPatientName());
                intent.putExtra(ToDoCompletedDetailActivity.KEY_FLAG_PATIENT_DATE, list.getBookDate());
                intent.putExtra(ToDoCompletedDetailActivity.KEY_FLAG_PATIENT_TIME, list.getBookTime());
                intent.putExtra(ToDoCompletedDetailActivity.KEY_FLAG_PATIENT_TYPE, list.getBookType());
                intent.putExtra(ToDoCompletedDetailActivity.KEY_FLAG_PATIENT_PIC, list.getPatientPic());
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
