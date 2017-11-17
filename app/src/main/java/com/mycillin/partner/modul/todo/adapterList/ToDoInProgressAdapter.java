package com.mycillin.partner.modul.todo.adapterList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.modul.todo.ToDoInProgressFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ToDoInProgressAdapter extends RecyclerView.Adapter<ToDoInProgressAdapter.MyViewHolder> {
    private List<ToDoInProgressList> ToDoInProgressLists;
    private ArrayList<ToDoInProgressList> arrayToDoInProgressLists;
    private ToDoInProgressFragment toDoInProgressFragment;

    public ToDoInProgressAdapter(List<ToDoInProgressList> ToDoInProgressLists, ToDoInProgressFragment toDoInProgressFragment) {
        this.ToDoInProgressLists = ToDoInProgressLists;
        this.arrayToDoInProgressLists = new ArrayList<>();
        this.toDoInProgressFragment = new ToDoInProgressFragment();
        this.arrayToDoInProgressLists.addAll(ToDoInProgressLists);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_in_progress_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ToDoInProgressList resultList = ToDoInProgressLists.get(position);
        if (!resultList.getPatientPic().equals("")) {
            Picasso.with(toDoInProgressFragment.getContext())
                    .load(resultList.getPatientPic())
                    .resize(150, 150)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .centerCrop()
                    .into(holder.patientPic);
        }
        holder.patientName.setText(resultList.getPatientName());
        holder.bookType.setText(resultList.getBookType());
        holder.bookDate.setText(resultList.getBookDate());
        holder.bookTime.setText(resultList.getBookTime());
    }

    @Override
    public int getItemCount() {
        return ToDoInProgressLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView patientName;
        private TextView bookType;
        private TextView bookDate;
        private TextView bookTime;
        private CircleImageView patientPic;


        public MyViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.rowInProgressList_tv_patientName);
            bookType = itemView.findViewById(R.id.rowInProgressList_tv_bookType);
            bookDate = itemView.findViewById(R.id.rowInProgressList_tv_bookDate);
            bookTime = itemView.findViewById(R.id.rowInProgressList_tv_bookTime);
            patientPic = itemView.findViewById(R.id.rowInProgressList_iv_patientAvatar);
        }
    }
}
