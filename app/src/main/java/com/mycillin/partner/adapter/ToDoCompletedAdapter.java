package com.mycillin.partner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.fragment.todo.ToDoCompletedFragment;
import com.mycillin.partner.list.ToDoCompletedList;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrbagaskoro on 07-Oct-17.
 */

public class ToDoCompletedAdapter extends RecyclerView.Adapter<ToDoCompletedAdapter.MyViewHolder> {
    private List<ToDoCompletedList> ToDoCompletedLists;
    private ArrayList<ToDoCompletedList> arrayToDoCompletedLists;
    private ToDoCompletedFragment toDoCompletedFragment;

    public ToDoCompletedAdapter(List<ToDoCompletedList> ToDoCompletedLists, ToDoCompletedFragment toDoCompletedFragment) {
        this.ToDoCompletedLists = ToDoCompletedLists;
        this.arrayToDoCompletedLists = new ArrayList<>();
        this.toDoCompletedFragment = toDoCompletedFragment;
        this.arrayToDoCompletedLists.addAll(ToDoCompletedLists);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_completed_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ToDoCompletedList resultList = ToDoCompletedLists.get(position);

        if (!resultList.getPatientPic().equals("")) {
            Picasso.with(toDoCompletedFragment.getContext())
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
        return ToDoCompletedLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView patientName;
        private TextView bookType;
        private TextView bookDate;
        private TextView bookTime;
        private CircleImageView patientPic;


        public MyViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.rowCompletedList_tv_patientName);
            bookType = itemView.findViewById(R.id.rowCompletedList_tv_bookType);
            bookDate = itemView.findViewById(R.id.rowCompletedList_tv_bookDate);
            bookTime = itemView.findViewById(R.id.rowCompletedList_tv_bookTime);
            patientPic = itemView.findViewById(R.id.rowCompletedList_iv_patientAvatar);
        }
    }
}
