package com.mycillin.partner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycillin.partner.R;
import com.mycillin.partner.list.EWalletHistoryList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 16003041 on 06/10/2017.
 */

public class EWalletHistoryAdapter extends RecyclerView.Adapter<EWalletHistoryAdapter.MyViewHolder> {
    private List<EWalletHistoryList> eWalletHistoryLists;
    private ArrayList<EWalletHistoryList> arrayEWalletHistoryLists;

    public EWalletHistoryAdapter(List<EWalletHistoryList> eWalletHistoryLists) {
        this.eWalletHistoryLists = eWalletHistoryLists;
        this.arrayEWalletHistoryLists = new ArrayList<>();
        this.arrayEWalletHistoryLists.addAll(eWalletHistoryLists);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ewallet_history_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EWalletHistoryList resultList = eWalletHistoryLists.get(position);
        holder.description.setText(resultList.getDescription());
        holder.amount.setText(resultList.getAmount());
        holder.date.setText(resultList.getDate());

        if(resultList.isTopUp()) {
            holder.icon.setImageResource(R.drawable.ic_action_add_green);
        }
    }

    @Override
    public int getItemCount() {
        return eWalletHistoryLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView description;
        private TextView amount;
        private TextView date;
        private ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.rowEWalletHistoryList_tv_description);
            amount = itemView.findViewById(R.id.rowEWalletHistoryList_tv_amount);
            date = itemView.findViewById(R.id.rowEWalletHistoryList_tv_date);
            icon = itemView.findViewById(R.id.rowEWalletHistoryList_iv_icon);
        }
    }
}
