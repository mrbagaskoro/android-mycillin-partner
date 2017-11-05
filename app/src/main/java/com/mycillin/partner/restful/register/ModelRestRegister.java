package com.mycillin.partner.restful.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestRegister {
    @SerializedName("result")
    @Expose
    private ModelRestRegisterData result;

    public ModelRestRegisterData getResult() {
        return result;
    }

    public void setResult(ModelRestRegisterData result) {
        this.result = result;
    }
}
