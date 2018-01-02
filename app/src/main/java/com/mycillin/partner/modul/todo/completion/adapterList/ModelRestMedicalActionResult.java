package com.mycillin.partner.modul.todo.completion.adapterList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelRestMedicalActionResult {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<ModelRestMedicalActionResultData> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ModelRestMedicalActionResultData> getData() {
        return data;
    }

    public void setData(List<ModelRestMedicalActionResultData> data) {
        this.data = data;
    }
}
