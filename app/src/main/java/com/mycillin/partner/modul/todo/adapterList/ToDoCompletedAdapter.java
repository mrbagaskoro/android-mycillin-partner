package com.mycillin.partner.modul.todo.adapterList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mycillin.partner.R;
import com.mycillin.partner.modul.todo.ToDoCompletedFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .circleCrop();

            Glide.with(toDoCompletedFragment.getContext())
                    .load(resultList.getPatientPic())
                    .apply(requestOptions)
                    .into(holder.patientPic);
        }
        holder.patientName.setText(resultList.getPatientName());
        holder.bookType.setText(resultList.getBookType());
        holder.bookDate.setText(toDoCompletedFragment.getString(R.string.itemConcat3, resultList.getBookDate(), resultList.getBookTime()));
        holder.address.setText(resultList.getAddress());
    }

    @Override
    public int getItemCount() {
        return ToDoCompletedLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView patientName;
        private TextView bookType;
        private TextView bookDate;
        private TextView address;
        private CircleImageView patientPic;


        public MyViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.rowCompletedList_tv_patientName);
            bookType = itemView.findViewById(R.id.rowCompletedList_tv_bookType);
            bookDate = itemView.findViewById(R.id.rowCompletedList_tv_bookDate);
            patientPic = itemView.findViewById(R.id.rowCompletedList_iv_patientAvatar);
            address = itemView.findViewById(R.id.rowCompletedList_tv_address);
        }
    }
}
