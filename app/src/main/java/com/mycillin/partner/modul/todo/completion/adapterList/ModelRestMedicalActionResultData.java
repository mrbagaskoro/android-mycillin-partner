package com.mycillin.partner.modul.todo.completion.adapterList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestMedicalActionResultData {
    @SerializedName("action_type_id")
    @Expose
    private String actionTypeId;
    @SerializedName("action_type_desc")
    @Expose
    private String actionTypeDesc;

    public String getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(String actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    public String getActionTypeDesc() {
        return actionTypeDesc;
    }

    public void setActionTypeDesc(String actionTypeDesc) {
        this.actionTypeDesc = actionTypeDesc;
    }
}
