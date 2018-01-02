package com.mycillin.partner.modul.todo.completion.adapterList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelRestPrescriptionTypeResult {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<ModelRestPrescriptionTypeResultData> modelRestPrescriptionTypeResultData = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ModelRestPrescriptionTypeResultData> getData() {
        return modelRestPrescriptionTypeResultData;
    }

    public void setData(List<ModelRestPrescriptionTypeResultData> modelRestPrescriptionTypeResultData) {
        this.modelRestPrescriptionTypeResultData = modelRestPrescriptionTypeResultData;
    }
}
