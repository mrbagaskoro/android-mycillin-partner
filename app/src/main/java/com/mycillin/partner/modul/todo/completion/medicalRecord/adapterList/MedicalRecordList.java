package com.mycillin.partner.modul.todo.completion.medicalRecord.adapterList;

public class MedicalRecordList {
    private String day;
    private String month;
    private String year;
    private String doctorName;

    public MedicalRecordList(String day, String month, String year, String doctorName){
        this.day = day;
        this.month = month;
        this.year = year;
        this.doctorName = doctorName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
