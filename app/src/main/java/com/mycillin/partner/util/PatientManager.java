package com.mycillin.partner.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PatientManager {
    private static final String KEY_PREF_NAME = "MYCILLIN_SESSION_MANAGER";

    private static final String KEY_PATIENT_ID = "PATIENT_ID";
    private static final String KEY_PATIENT_BOOKING_ID = "PATIENT_BOOKING_ID";
    private static final String KEY_PATIENT_PHOTO = "PATIENT_PHOTO";
    private static final String KEY_PATIENT_ADDRESS = "PATIENT_ADDRESS";
    private static final String KEY_PATIENT_LONGITUDE = "PATIENT_LONGITUDE";
    private static final String KEY_PATIENT_LATITUDE = "PATIENT_LATITUDE";
    private static final String KEY_PATIENT_MOBILE_NO = "PATIENT_MOBILE_NO";
    private static final String KEY_DEFAULT_VALUE = " ";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PatientManager(Context con) {
        int PRIVATE_MODE = 0;
        sharedPreferences = con.getSharedPreferences(KEY_PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void clearAll() {
        editor.clear();
        editor.commit();
    }

    public String getPatientId() {
        return sharedPreferences.getString(KEY_PATIENT_ID, KEY_DEFAULT_VALUE);
    }

    public void setPatientId(String patientID) {
        editor.putString(KEY_PATIENT_ID, patientID);
        editor.commit();
    }

    public String getPatientBookingId() {
        return sharedPreferences.getString(KEY_PATIENT_BOOKING_ID, KEY_DEFAULT_VALUE);
    }

    public void setPatientBookingId(String bookingID) {
        editor.putString(KEY_PATIENT_BOOKING_ID, bookingID);
        editor.commit();
    }

    public String getPatientAddress() {
        return sharedPreferences.getString(KEY_PATIENT_ADDRESS, KEY_DEFAULT_VALUE);
    }

    public void setPatientAddress(String address) {
        editor.putString(KEY_PATIENT_ADDRESS, address);
        editor.commit();
    }

    public String getPatientLongitude() {
        return sharedPreferences.getString(KEY_PATIENT_LONGITUDE, KEY_DEFAULT_VALUE);
    }

    public void setPatientLongitude(String longitude) {
        editor.putString(KEY_PATIENT_LONGITUDE, longitude);
        editor.commit();
    }

    public String getPatientLatitude() {
        return sharedPreferences.getString(KEY_PATIENT_LATITUDE, KEY_DEFAULT_VALUE);
    }

    public void setPatientLatitude(String latitude) {
        editor.putString(KEY_PATIENT_LATITUDE, latitude);
        editor.commit();
    }

    public String getKeyPatientMobileNo() {
        return sharedPreferences.getString(KEY_PATIENT_MOBILE_NO, KEY_DEFAULT_VALUE);
    }

    public void setKeyPatientMobileNo(String mobileNo) {
        editor.putString(KEY_PATIENT_MOBILE_NO, mobileNo);
        editor.commit();
    }

    public String getKeyPatientPhoto() {
        return sharedPreferences.getString(KEY_PATIENT_PHOTO, KEY_DEFAULT_VALUE);
    }

    public void setKeyPatientPhoto(String patientPhoto) {
        editor.putString(KEY_PATIENT_PHOTO, patientPhoto);
        editor.commit();
    }



}