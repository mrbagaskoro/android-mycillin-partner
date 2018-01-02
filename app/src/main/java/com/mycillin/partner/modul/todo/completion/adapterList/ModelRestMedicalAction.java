package com.mycillin.partner.modul.todo.completion.adapterList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRestMedicalAction {
    @SerializedName("result")
    @Expose
    private ModelRestMedicalActionResult modelRestMedicalActionResult;

    public ModelRestMedicalActionResult getResult() {
        return modelRestMedicalActionResult;
    }

    public void setResult(ModelRestMedicalActionResult modelRestMedicalActionResult) {
        this.modelRestMedicalActionResult = modelRestMedicalActionResult;
    }
}
