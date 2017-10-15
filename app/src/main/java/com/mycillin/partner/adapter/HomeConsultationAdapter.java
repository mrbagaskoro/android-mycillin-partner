package com.mycillin.partner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.list.HomeConsultationList;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrbagaskoro on 07-Oct-17.
 */

public class HomeConsultationAdapter extends RecyclerView.Adapter<HomeConsultationAdapter.MyViewHolder> {
    private List<HomeConsultationList> HomeConsultationLists;
    private ArrayList<HomeConsultationList> arrayHomeConsultationLists;

    public HomeConsultationAdapter(List<HomeConsultationList> HomeConsultationLists) {
        this.HomeConsultationLists = HomeConsultationLists;
        this.arrayHomeConsultationLists = new ArrayList<>();
        this.arrayHomeConsultationLists.addAll(HomeConsultationLists);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_consultation_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomeConsultationList resultList = HomeConsultationLists.get(position);
        holder.patientName.setText(resultList.getPatientName());
        holder.bookType.setText(resultList.getBookType());
        holder.bookDate.setText(resultList.getBookDate());
        holder.bookTime.setText(resultList.getBookTime());
    }

    @Override
    public int getItemCount() {
        return HomeConsultationLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView patientName;
        private TextView bookType;
        private TextView bookDate;
        private TextView bookTime;
        private CircleImageView patientPic;


        public MyViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.rowConsultationList_tv_patientName);
            bookType = itemView.findViewById(R.id.rowConsultationList_tv_bookType);
            bookDate = itemView.findViewById(R.id.rowConsultationList_tv_bookDate);
            bookTime = itemView.findViewById(R.id.rowConsultationList_tv_bookTime);
            patientPic = itemView.findViewById(R.id.rowConsultationList_iv_patientAvatar);
        }
    }
}