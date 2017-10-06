package com.mycillin.partner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.list.HomeVisitList;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrbagaskoro on 06-Oct-17.
 */

public class HomeVisitAdapter extends RecyclerView.Adapter<HomeVisitAdapter.MyViewHolder> {
    private List<HomeVisitList> HomeVisitLists;
    private ArrayList<HomeVisitList> arrayHomeVisitLists;

    public HomeVisitAdapter(List<HomeVisitList> HomeVisitLists) {
        this.HomeVisitLists = HomeVisitLists;
        this.arrayHomeVisitLists = new ArrayList<>();
        this.arrayHomeVisitLists.addAll(HomeVisitLists);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_visit_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomeVisitList resultList = HomeVisitLists.get(position);
        holder.patientName.setText(resultList.getPatientName());
        holder.bookType.setText(resultList.getBookType());
        holder.bookDate.setText(resultList.getBookDate());
        holder.bookTime.setText(resultList.getBookTime());
    }

    @Override
    public int getItemCount() {
        return HomeVisitLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView patientName;
        private TextView bookType;
        private TextView bookDate;
        private TextView bookTime;
        private CircleImageView patientPic;


        public MyViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.rowVisitList_tv_patientName);
            bookType = itemView.findViewById(R.id.rowVisitList_tv_bookType);
            bookDate = itemView.findViewById(R.id.rowVisitList_tv_bookDate);
            bookTime = itemView.findViewById(R.id.rowVisitList_tv_bookTime);
            patientPic = itemView.findViewById(R.id.rowVisitList_iv_patientAvatar);
        }
    }
}