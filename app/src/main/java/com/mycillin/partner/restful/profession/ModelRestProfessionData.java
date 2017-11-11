package com.mycillin.partner.restful.profession;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestProfessionData {
    @SerializedName("partner_type_id")
    @Expose
    private String partnerTypeId;
    @SerializedName("partner_type_desc")
    @Expose
    private String partnerTypeDesc;

    public String getPartnerTypeId() {
        return partnerTypeId;
    }

    public void setPartnerTypeId(String partnerTypeId) {
        this.partnerTypeId = partnerTypeId;
    }

    public String getPartnerTypeDesc() {
        return partnerTypeDesc;
    }

    public void setPartnerTypeDesc(String partnerTypeDesc) {
        this.partnerTypeDesc = partnerTypeDesc;
    }
}
