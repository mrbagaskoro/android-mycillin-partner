package com.mycillin.partner.modul.accountProfile.model.profession;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestProfession {
    @SerializedName("result")
    @Expose
    private ModelRestProfessionResult result;

    public ModelRestProfessionResult getResult() {
        return result;
    }

    public void setResult(ModelRestProfessionResult result) {
        this.result = result;
    }
}
