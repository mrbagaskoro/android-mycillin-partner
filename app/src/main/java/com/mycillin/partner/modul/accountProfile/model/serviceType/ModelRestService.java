package com.mycillin.partner.modul.accountProfile.model.serviceType;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestService {
    @SerializedName("result")
    @Expose
    private ModelRestServiceResult result;

    public ModelRestServiceResult getResult() {
        return result;
    }

    public void setResult(ModelRestServiceResult result) {
        this.result = result;
    }
}
