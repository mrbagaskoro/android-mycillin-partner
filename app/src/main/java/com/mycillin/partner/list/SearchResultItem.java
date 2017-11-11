package com.mycillin.partner.list;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchResultItem implements Parcelable {

    public static final Creator<SearchResultItem> CREATOR = new Creator<SearchResultItem>() {
        @Override
        public SearchResultItem createFromParcel(Parcel in) {
            return new SearchResultItem(in);
        }

        @Override
        public SearchResultItem[] newArray(int size) {
            return new SearchResultItem[size];
        }
    };

    private String labelItem1 = "";
    private String item1 = "";
    private String labelItem2 = "";
    private String item2 = "";
    private String labelItem3 = "";
    private String item3 = "";
    private String labelItem4 = "";
    private String item4 = "";
    private String labelItem5 = "";
    private String item5 = "";
    private String labelItem6 = "";
    private String item6 = "";

    public SearchResultItem() {
    }

    public SearchResultItem(
            String labelItem1, String item1, String labelItem2, String item2) {
        this.labelItem1 = labelItem1;
        this.item1 = item1;
        this.labelItem2 = labelItem2;
        this.item2 = item2;
    }

    public SearchResultItem(
            String labelItem1, String item1, String labelItem2, String item2,
            String labelItem3, String item3, String labelItem4, String item4,
            String labelItem5, String item5, String labelItem6, String item6) {
        this.labelItem1 = labelItem1;
        this.item1 = item1;
        this.labelItem2 = labelItem2;
        this.item2 = item2;
        this.labelItem3 = labelItem3;
        this.item3 = item3;
        this.labelItem4 = labelItem4;
        this.item4 = item4;
        this.labelItem5 = labelItem5;
        this.item5 = item5;
        this.labelItem6 = labelItem6;
        this.item6 = item6;
    }

    protected SearchResultItem(Parcel in) {
        labelItem1 = in.readString();
        item1 = in.readString();
        labelItem2 = in.readString();
        item2 = in.readString();
        labelItem3 = in.readString();
        item3 = in.readString();
        labelItem4 = in.readString();
        item4 = in.readString();
        labelItem5 = in.readString();
        item5 = in.readString();
        labelItem6 = in.readString();
        item6 = in.readString();
    }

    public String getLabelItem1() {
        return labelItem1;
    }

    public String getItem1() {
        return item1;
    }

    public String getLabelItem2() {
        return labelItem2;
    }

    public String getItem2() {
        return item2;
    }

    public String getLabelItem3() {
        return labelItem3;
    }

    public String getItem3() {
        return item3;
    }

    public String getLabelItem4() {
        return labelItem4;
    }

    public String getItem4() {
        return item4;
    }

    public String getLabelItem5() {
        return labelItem5;
    }

    public String getItem5() {
        return item5;
    }

    public String getLabelItem6() {
        return labelItem6;
    }

    public String getItem6() {
        return item6;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(labelItem1);
        parcel.writeString(item1);
        parcel.writeString(labelItem2);
        parcel.writeString(item2);
        parcel.writeString(labelItem3);
        parcel.writeString(item3);
        parcel.writeString(labelItem4);
        parcel.writeString(item4);
        parcel.writeString(labelItem5);
        parcel.writeString(item5);
        parcel.writeString(labelItem6);
        parcel.writeString(item6);
    }
}
