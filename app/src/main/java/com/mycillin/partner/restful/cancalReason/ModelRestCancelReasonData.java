package com.mycillin.partner.restful.cancalReason;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestCancelReasonData {
    @SerializedName("cancel_reason_id")
    @Expose
    private String cancelReasonId;
    @SerializedName("cancel_reason_desc")
    @Expose
    private String cancelReasonDesc;

    public String getCancelReasonId() {
        return cancelReasonId;
    }

    public void setCancelReasonId(String cancelReasonId) {
        this.cancelReasonId = cancelReasonId;
    }

    public String getCancelReasonDesc() {
        return cancelReasonDesc;
    }

    public void setCancelReasonDesc(String cancelReasonDesc) {
        this.cancelReasonDesc = cancelReasonDesc;
    }
}
