package com.mycillin.partner.restful.cancalReason;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelRestCancelReasonResult {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private List<ModelRestCancelReasonData> data = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelRestCancelReasonData> getData() {
        return data;
    }

    public void setData(List<ModelRestCancelReasonData> data) {
        this.data = data;
    }


}
