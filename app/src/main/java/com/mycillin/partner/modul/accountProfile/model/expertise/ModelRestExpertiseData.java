package com.mycillin.partner.modul.accountProfile.model.expertise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestExpertiseData {
    @SerializedName("spesialisasi_id")
    @Expose
    private String spesialisasiId;
    @SerializedName("spesialisasi_desc")
    @Expose
    private String spesialisasiDesc;

    public String getSpesialisasiId() {
        return spesialisasiId;
    }

    public void setSpesialisasiId(String spesialisasiId) {
        this.spesialisasiId = spesialisasiId;
    }

    public String getSpesialisasiDesc() {
        return spesialisasiDesc;
    }

    public void setSpesialisasiDesc(String spesialisasiDesc) {
        this.spesialisasiDesc = spesialisasiDesc;
    }
}
