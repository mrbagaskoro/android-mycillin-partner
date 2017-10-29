package com.mycillin.partner.restful.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestLogin {
    @SerializedName("result")
    @Expose
    private ModelRestLoginResult result;

    public ModelRestLoginResult getResult() {
        return result;
    }

    public void setResult(ModelRestLoginResult result) {
        this.result = result;
    }

}
