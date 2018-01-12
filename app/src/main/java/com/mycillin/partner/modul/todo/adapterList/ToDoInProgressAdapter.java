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
import com.mycillin.partner.modul.todo.ToDoInProgressFragment;

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
        this.toDoInProgressFragment = toDoInProgressFragment;
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
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .circleCrop();

            Glide.with(toDoInProgressFragment.getContext())
                    .load(resultList.getPatientPic())
                    .apply(requestOptions)
                    .into(holder.patientPic);
        }
        holder.patientName.setText(resultList.getPatientName());
        holder.bookType.setText(resultList.getBookType());
        holder.bookDate.setText(toDoInProgressFragment.getString(R.string.itemConcat3, resultList.getBookDate(), resultList.getBookTime()));
        holder.address.setText(resultList.getAddress());
        holder.age.setText(resultList.getAge());
        holder.height.setText(resultList.getHeight());
        holder.weight.setText(resultList.getWeight());
        holder.bloodype.setText(resultList.getBloodType());
        holder.gender.setText(resultList.getGender());
        holder.userID.setText(resultList.getUserID());
        holder.relID.setText(resultList.getRelID());
        holder.paymentMethod.setText(resultList.getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return ToDoInProgressLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView patientName;
        private TextView bookType;
        private TextView bookDate;
        private TextView address;
        private TextView age;
        private TextView height;
        private TextView weight;
        private TextView bloodype;
        private TextView gender;
        private TextView userID;
        private TextView relID;
        private TextView paymentMethod;
        private CircleImageView patientPic;

        MyViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.rowInProgressList_tv_patientName);
            bookType = itemView.findViewById(R.id.rowInProgressList_tv_bookType);
            bookDate = itemView.findViewById(R.id.rowInProgressList_tv_bookDate);
            patientPic = itemView.findViewById(R.id.rowInProgressList_iv_patientAvatar);
            address = itemView.findViewById(R.id.rowInProgressList_tv_address);
            age = itemView.findViewById(R.id.rowInProgressList_tv_age);
            height = itemView.findViewById(R.id.rowInProgressList_tv_height);
            weight = itemView.findViewById(R.id.rowInProgressList_tv_weight);
            bloodype = itemView.findViewById(R.id.rowInProgressList_tv_bloodTipe);
            gender = itemView.findViewById(R.id.rowInProgressList_tv_gender);
            userID = itemView.findViewById(R.id.rowInProgressList_tv_userID);
            relID = itemView.findViewById(R.id.rowInProgressList_tv_relID);
            paymentMethod = itemView.findViewById(R.id.rowInProgressList_tv_payment_amount);
        }
    }
}
