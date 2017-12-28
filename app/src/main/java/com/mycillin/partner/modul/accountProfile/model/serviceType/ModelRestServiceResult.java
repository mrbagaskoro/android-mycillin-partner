package com.mycillin.partner.modul.accountProfile.model.serviceType;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelRestServiceResult {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<ModelRestServiceResultData> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ModelRestServiceResultData> getData() {
        return data;
    }

    public void setData(List<ModelRestServiceResultData> data) {
        this.data = data;
    }
}
