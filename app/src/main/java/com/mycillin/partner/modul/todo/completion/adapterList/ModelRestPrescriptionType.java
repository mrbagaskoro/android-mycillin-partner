package com.mycillin.partner.modul.todo.completion.adapterList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestPrescriptionType {
    @SerializedName("result")
    @Expose
    private ModelRestPrescriptionTypeResult modelRestPrescriptionTypeResult;

    public ModelRestPrescriptionTypeResult getResult() {
        return modelRestPrescriptionTypeResult;
    }

    public void setResult(ModelRestPrescriptionTypeResult modelRestPrescriptionTypeResult) {
        this.modelRestPrescriptionTypeResult = modelRestPrescriptionTypeResult;
    }
}
