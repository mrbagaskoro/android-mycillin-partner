package com.mycillin.partner.restful.expertise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelRestExpertiseResult {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private List<ModelRestExpertiseData> data = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelRestExpertiseData> getData() {
        return data;
    }

    public void setData(List<ModelRestExpertiseData> data) {
        this.data = data;
    }
}
