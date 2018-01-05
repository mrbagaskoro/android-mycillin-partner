package com.mycillin.partner.modul.account.model.bannerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestBanner {
    @SerializedName("result")
    @Expose
    private ModelRestBannerResult modelRestBannerResult;

    public ModelRestBannerResult getResult() {
        return modelRestBannerResult;
    }

    public void setResult(ModelRestBannerResult modelRestBannerResult) {
        this.modelRestBannerResult = modelRestBannerResult;
    }
}
