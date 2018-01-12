package com.mycillin.partner.modul.chat.firebaseGet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelResultDataFirebaseGet {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private List<ModelDataFirebaseGet> data = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelDataFirebaseGet> getData() {
        return data;
    }

    public void setData(List<ModelDataFirebaseGet> data) {
        this.data = data;
    }
}
