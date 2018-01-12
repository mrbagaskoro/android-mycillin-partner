package com.mycillin.partner.modul.home.consultation;

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

public class HomeConsultationAdapter extends RecyclerView.Adapter<HomeConsultationAdapter.MyViewHolder> {
    private List<HomeConsultationList> HomeConsultationLists;
    private ArrayList<HomeConsultationList> arrayHomeConsultationLists;
    private HomeConsultationFragment homeConsultationFragment;

    HomeConsultationAdapter(List<HomeConsultationList> HomeConsultationLists, HomeConsultationFragment homeConsultationFragment) {
        this.HomeConsultationLists = HomeConsultationLists;
        this.arrayHomeConsultationLists = new ArrayList<>();
        this.homeConsultationFragment = homeConsultationFragment;
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
        if (!resultList.getPatientPic().equals("")) {
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
                    .circleCrop();

            Glide.with(homeConsultationFragment.getContext())
                    .load(resultList.getPatientPic())
                    .apply(requestOptions)
                    .into(holder.patientPic);
        }
        holder.patientName.setText(resultList.getPatientName());
        holder.bookType.setText(resultList.getBookType());
        holder.bookDate.setText(homeConsultationFragment.getString(R.string.itemConcat3, resultList.getBookDate(), resultList.getBookTime()));
        holder.paymentMethod.setText(resultList.getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return HomeConsultationLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView patientName;
        private TextView bookType;
        private TextView bookDate;
        private TextView paymentMethod;
        private CircleImageView patientPic;


        MyViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.rowConsultationList_tv_patientName);
            bookType = itemView.findViewById(R.id.rowConsultationList_tv_bookType);
            bookDate = itemView.findViewById(R.id.rowConsultationList_tv_bookDate);
            patientPic = itemView.findViewById(R.id.rowConsultationList_iv_patientAvatar);
            paymentMethod = itemView.findViewById(R.id.rowConsultationList_tv_payment_amount);
        }
    }
}
