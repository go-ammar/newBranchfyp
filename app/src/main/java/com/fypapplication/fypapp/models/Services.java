package com.fypapplication.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Services implements Parcelable {

    public static final Creator<Services> CREATOR = new Creator<Services>() {
        @Override
        public Services createFromParcel(Parcel in) {
            return new Services(in);
        }

        @Override
        public Services[] newArray(int size) {
            return new Services[size];
        }
    };


    public String service;
    public String vehicleType;


    public Services(Parcel in) {
        service = in.readString();
        vehicleType = in.readString();
    }

    public Services() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(service);
        dest.writeString(vehicleType);
    }
}
