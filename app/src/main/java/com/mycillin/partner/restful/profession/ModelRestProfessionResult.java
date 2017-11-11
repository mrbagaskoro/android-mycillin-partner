package com.mycillin.partner.restful.profession;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelRestProfessionResult {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private List<ModelRestProfessionData> data = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelRestProfessionData> getData() {
        return data;
    }

    public void setData(List<ModelRestProfessionData> data) {
        this.data = data;
    }

}
