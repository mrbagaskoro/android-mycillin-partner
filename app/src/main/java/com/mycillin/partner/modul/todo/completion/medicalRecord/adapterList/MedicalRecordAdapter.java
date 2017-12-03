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

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.MyViewHolder> {
    private List<MedicalRecordList> medicalRecordLists;
    private ArrayList<MedicalRecordList> ArrayMedicalRecordLists;
    private Activity activity;

    public MedicalRecordAdapter(List<MedicalRecordList> medicalRecordLists, Activity activity) {
        this.medicalRecordLists = medicalRecordLists;
        this.ArrayMedicalRecordLists = new ArrayList<>();
        this.ArrayMedicalRecordLists.addAll(medicalRecordLists);
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_medical_record_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MedicalRecordList resultList = medicalRecordLists.get(position);
        holder.day.setText(resultList.getDay());
        holder.month.setText(resultList.getMonth());
        holder.year.setText(resultList.getYear());
        holder.doctorName.setText(resultList.getDoctorName());

        holder.height.setText(resultList.getHeight());
        holder.weight.setText(resultList.getWeight());
        holder.partnerID.setText(resultList.getPartnerID());
        holder.recordID.setText(resultList.getRecordID());
        holder.userID.setText(resultList.getUserID());
        holder.serviceDesc.setText(resultList.getServiceDesc());
        holder.bodyTemp.setText(resultList.getBodyTemp());
        holder.bloodSugar.setText(resultList.getBloodSugar());
        holder.bloodCholesterol.setText(resultList.getBloodCholesterol());
        holder.diastol.setText(resultList.getDiastol());
        holder.systol.setText(resultList.getSystol());
        holder.patientCondition.setText(resultList.getPatientCondition());
        holder.diagnose.setText(resultList.getDiagnose());
        holder.prescriptionStatus.setText(resultList.getPrescriptionStatus());
        holder.prescriptionID.setText(resultList.getPrescriptionID());
        holder.prescriptionDesc.setText(resultList.getPrescriptionDesc());
    }

    @Override
    public int getItemCount() {
        return medicalRecordLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView day;
        private TextView month;
        private TextView year;
        private TextView doctorName;
        private TextView height;
        private TextView weight;
        private TextView partnerID;
        private TextView recordID;
        private TextView userID;
        private TextView serviceDesc;
        private TextView bodyTemp;
        private TextView bloodSugar;
        private TextView bloodCholesterol;
        private TextView diastol;
        private TextView systol;
        private TextView patientCondition;
        private TextView diagnose;
        private TextView prescriptionStatus;
        private TextView prescriptionID;
        private TextView prescriptionDesc;

        MyViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.rowMedicalRecordList_tv_day);
            month = itemView.findViewById(R.id.rowMedicalRecordList_tv_month);
            year = itemView.findViewById(R.id.rowMedicalRecordList_tv_year);
            doctorName = itemView.findViewById(R.id.rowMedicalRecordList_tv_doctorName);
            height = itemView.findViewById(R.id.rowMedicalRecordList_tv_height);
            weight = itemView.findViewById(R.id.rowMedicalRecordList_tv_weight);
            partnerID = itemView.findViewById(R.id.rowMedicalRecordList_tv_partnerId);
            recordID = itemView.findViewById(R.id.rowMedicalRecordList_tv_recordId);
            userID = itemView.findViewById(R.id.rowMedicalRecordList_tv_userId);
            serviceDesc = itemView.findViewById(R.id.rowMedicalRecordList_tv_serviceDesc);
            bodyTemp = itemView.findViewById(R.id.rowMedicalRecordList_tv_bodyTemp);
            bloodSugar = itemView.findViewById(R.id.rowMedicalRecordList_tv_bloodSugar);
            bloodCholesterol = itemView.findViewById(R.id.rowMedicalRecordList_tv_bloodCholesterol);
            diastol = itemView.findViewById(R.id.rowMedicalRecordList_tv_diastol);
            systol = itemView.findViewById(R.id.rowMedicalRecordList_tv_systol);
            patientCondition = itemView.findViewById(R.id.rowMedicalRecordList_tv_patient_condition);
            diagnose = itemView.findViewById(R.id.rowMedicalRecordList_tv_diagnose);
            prescriptionStatus = itemView.findViewById(R.id.rowMedicalRecordList_tv_prescriptionStatus);
            prescriptionID = itemView.findViewById(R.id.rowMedicalRecordList_tv_prescriptionID);
            prescriptionDesc = itemView.findViewById(R.id.rowMedicalRecordList_tv_prescriptionDesc);
        }
    }
}
