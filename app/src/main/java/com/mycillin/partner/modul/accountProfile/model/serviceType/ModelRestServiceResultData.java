package com.mycillin.partner.modul.accountProfile.model.serviceType;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestServiceResultData {
    @SerializedName("service_type_id")
    @Expose
    private String serviceTypeId;
    @SerializedName("service_type_desc")
    @Expose
    private String serviceTypeDesc;

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceTypeDesc() {
        return serviceTypeDesc;
    }

    public void setServiceTypeDesc(String serviceTypeDesc) {
        this.serviceTypeDesc = serviceTypeDesc;
    }
}
