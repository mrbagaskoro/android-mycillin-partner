package com.mycillin.partner.modul.todo.adapterList;

public class ToDoCompletedList {
    private String patientPic;
    private String patientName;
    private String bookType;
    private String bookDate;
    private String bookTime;
    private String address;
    private String paymentMethod;

    public ToDoCompletedList(String patientPic, String patientName, String bookType, String bookDate, String bookTime, String address, String paymentMethod) {
        this.patientPic = patientPic;
        this.patientName = patientName;
        this.bookType = bookType;
        this.bookDate = bookDate;
        this.bookTime = bookTime;
        this.address = address;
        this.paymentMethod = paymentMethod;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
