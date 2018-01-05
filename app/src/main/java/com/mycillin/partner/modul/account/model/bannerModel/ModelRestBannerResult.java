package com.mycillin.partner.modul.account.model.bannerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelRestBannerResult {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<ModelRestBannerResultData> modelRestBannerResultData = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ModelRestBannerResultData> getData() {
        return modelRestBannerResultData;
    }

    public void setData(List<ModelRestBannerResultData> modelRestBannerResultData) {
        this.modelRestBannerResultData = modelRestBannerResultData;
    }
}
