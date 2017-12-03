package com.mycillin.partner.modul.todo.completion.medicalRecord.adapterList;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycillin.partner.R;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.MyViewHolder> {
    private List<PrescriptionList> prescriptionLists;
    private ArrayList<PrescriptionList> arrayPrescriptionLists;
    private Activity activity;

    public PrescriptionAdapter(List<PrescriptionList> prescriptionLists, Activity activity) {
        this.prescriptionLists = prescriptionLists;
        this.arrayPrescriptionLists = new ArrayList<>();
        this.arrayPrescriptionLists.addAll(prescriptionLists);
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_prescription_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final PrescriptionList prescriptionList = prescriptionLists.get(position);
        holder.drugs.setText(prescriptionList.getDrugs());
        holder.instruction.setText(prescriptionList.getInstruction());
    }

    @Override
    public int getItemCount() {
        return prescriptionLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView drugs;
        private TextView instruction;

        MyViewHolder(View itemView) {
            super(itemView);
            drugs = itemView.findViewById(R.id.rowPrescriptionList_tv_prescriptionInfo1);
            instruction = itemView.findViewById(R.id.rowPrescriptionList_tv_prescription1);
        }
    }
}
