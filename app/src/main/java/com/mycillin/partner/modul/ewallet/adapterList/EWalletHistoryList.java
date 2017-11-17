package com.mycillin.partner.modul.ewallet.adapterList;

public class EWalletHistoryList {
    private String description;
    private String amount;
    private String date;
    private boolean isTopUp;

    public EWalletHistoryList(String description, String amount, String date, boolean isTopUp){
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.isTopUp = isTopUp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isTopUp() {
        return isTopUp;
    }

    public void setTopUp(boolean topUp) {
        isTopUp = topUp;
    }
}
