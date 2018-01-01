package com.mycillin.partner.modul.home.visit;

public class HomeVisitList {
    private String patientPic;
    private String patientName;
    private String bookType;
    private String bookDate;
    private String bookTime;
    private String paymentMethod;

    private String patientID;
    private String bookingID;
    private String address;
    private String patientLatitude;
    private String patientLongitude;
    private String phoneNumber;

    public HomeVisitList(String patientPic, String patientName, String bookType, String bookDate, String bookTime, String paymentMethod, String patientID, String bookingID, String address, String patientLatitude, String patientLongitude, String phoneNumber) {
        this.patientPic = patientPic;
        this.patientName = patientName;
        this.bookType = bookType;
        this.bookDate = bookDate;
        this.bookTime = bookTime;
        this.paymentMethod = paymentMethod;
        this.patientID = patientID;
        this.bookingID = bookingID;
        this.address = address;
        this.patientLatitude = patientLatitude;
        this.patientLongitude = patientLongitude;
        this.phoneNumber = phoneNumber;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPatientLatitude() {
        return patientLatitude;
    }

    public void setPatientLatitude(String patientLatitude) {
        this.patientLatitude = patientLatitude;
    }

    public String getPatientLongitude() {
        return patientLongitude;
    }

    public void setPatientLongitude(String patientLongitude) {
        this.patientLongitude = patientLongitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
