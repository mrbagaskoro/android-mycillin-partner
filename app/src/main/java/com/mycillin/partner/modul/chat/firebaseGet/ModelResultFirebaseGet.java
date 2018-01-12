package com.mycillin.partner.modul.chat.firebaseGet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelResultFirebaseGet {
    @SerializedName("result")
    @Expose
    private ModelResultDataFirebaseGet modelResultDataFirebaseGet;

    public ModelResultDataFirebaseGet getResult() {
        return modelResultDataFirebaseGet;
    }

    public void setResult(ModelResultDataFirebaseGet modelResultDataFirebaseGet) {
        this.modelResultDataFirebaseGet = modelResultDataFirebaseGet;
    }
}
