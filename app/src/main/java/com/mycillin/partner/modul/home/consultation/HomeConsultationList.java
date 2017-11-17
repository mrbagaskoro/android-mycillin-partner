package com.mycillin.partner.modul.home.consultation;

/**
 * Created by mrbagaskoro on 07-Oct-17.
 */

public class HomeConsultationList {
    private String patientPic;
    private String patientName;
    private String bookType;
    private String bookDate;
    private String bookTime;

    public HomeConsultationList(String patientPic, String patientName, String bookType, String bookDate, String bookTime){
        this.patientPic = patientPic;
        this.patientName = patientName;
        this.bookType = bookType;
        this.bookDate = bookDate;
        this.bookTime = bookTime;
    }

    public String getPatientPic() {
        return patientPic;
    }

    public void setPatientPic(String patientPic) {
        this.patientPic = patientPic;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }
}