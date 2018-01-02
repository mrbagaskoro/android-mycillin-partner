package com.mycillin.partner.modul.home.visit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mycillin.partner.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeVisitAdapter extends RecyclerView.Adapter<HomeVisitAdapter.MyViewHolder> {
    private List<HomeVisitList> HomeVisitLists;
    private ArrayList<HomeVisitList> arrayHomeVisitLists;
    private HomeVisitFragment homeVisitFragment;

    public HomeVisitAdapter(List<HomeVisitList> HomeVisitLists, HomeVisitFragment homeVisitFragment) {
        this.HomeVisitLists = HomeVisitLists;
        this.arrayHomeVisitLists = new ArrayList<>();
        this.homeVisitFragment = homeVisitFragment;
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
        if (!resultList.getPatientPic().equals("")) {
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .circleCrop();

            Glide.with(homeVisitFragment.getContext())
                    .load(resultList.getPatientPic())
                    .apply(requestOptions)
                    .into(holder.patientPic);
        }
        holder.patientName.setText(resultList.getPatientName());
        holder.bookType.setText(resultList.getBookType());
        holder.bookDate.setText(homeVisitFragment.getString(R.string.itemConcat3, resultList.getBookDate(), resultList.getBookTime()));
        holder.paymentMethod.setText(resultList.getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return HomeVisitLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView patientName;
        private TextView bookType;
        private TextView bookDate;
        private TextView paymentMethod;
        private CircleImageView patientPic;


        public MyViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.rowVisitList_tv_patientName);
            bookType = itemView.findViewById(R.id.rowVisitList_tv_bookType);
            bookDate = itemView.findViewById(R.id.rowVisitList_tv_bookDate);
            patientPic = itemView.findViewById(R.id.rowVisitList_iv_patientAvatar);
            paymentMethod = itemView.findViewById(R.id.rowVisitList_tv_payment_amount);
        }
    }
}