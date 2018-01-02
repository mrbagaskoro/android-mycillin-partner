package com.mycillin.partner.modul.todo.completion.adapterList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestPrescriptionTypeResultData {
    @SerializedName("prescription_type_id")
    @Expose
    private String prescriptionTypeId;
    @SerializedName("prescription_type_desc")
    @Expose
    private String prescriptionTypeDesc;

    public String getPrescriptionTypeId() {
        return prescriptionTypeId;
    }

    public void setPrescriptionTypeId(String prescriptionTypeId) {
        this.prescriptionTypeId = prescriptionTypeId;
    }

    public String getPrescriptionTypeDesc() {
        return prescriptionTypeDesc;
    }

    public void setPrescriptionTypeDesc(String prescriptionTypeDesc) {
        this.prescriptionTypeDesc = prescriptionTypeDesc;
    }
}
