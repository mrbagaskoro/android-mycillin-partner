package com.mycillin.partner.restful.cancelReason;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestCancelReason {
    @SerializedName("result")
    @Expose
    private ModelRestCancelReasonResult result;

    public ModelRestCancelReasonResult getResult() {
        return result;
    }

    public void setResult(ModelRestCancelReasonResult result) {
        this.result = result;
    }
}
