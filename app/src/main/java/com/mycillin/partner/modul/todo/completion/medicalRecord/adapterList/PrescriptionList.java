package com.mycillin.partner.modul.todo.completion.medicalRecord.adapterList;

public class PrescriptionList {
    private String drugs;
    private String instruction;

    public PrescriptionList(String drugs, String instruction) {
        this.drugs = drugs;
        this.instruction = instruction;
    }

    public String getDrugs() {
        return drugs;
    }

    public void setDrugs(String drugs) {
        this.drugs = drugs;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
