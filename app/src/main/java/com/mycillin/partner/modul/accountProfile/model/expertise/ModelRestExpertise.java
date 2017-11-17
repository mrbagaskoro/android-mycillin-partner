package com.mycillin.partner.modul.accountProfile.model.expertise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestExpertise {
    @SerializedName("result")
    @Expose
    private ModelRestExpertiseResult result;

    public ModelRestExpertiseResult getResult() {
        return result;
    }

    public void setResult(ModelRestExpertiseResult result) {
        this.result = result;
    }
}
