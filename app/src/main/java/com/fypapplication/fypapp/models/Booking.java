package com.fypapplication.fypapp.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Booking implements Parcelable {
    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };
    public String id;
    public String time;
    public String service;
    public String userId;
    public String mechanicId;
    public String createdAt;
    public String updatedAt;
    public String lat;
    public String lng;

    public Booking() {

    }

    protected Booking(Parcel in) {
        id = in.readString();
        time = in.readString();
        service = in.readString();
        userId = in.readString();
        mechanicId = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        lat = in.readString();
        lng = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(time);
        dest.writeString(service);
        dest.writeString(userId);
        dest.writeString(mechanicId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(lat);
        dest.writeString(lng);
    }
}
