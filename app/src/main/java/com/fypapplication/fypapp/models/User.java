package com.fypapplication.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String name;
    public String type;
    public String id;
    public String email;
    public String phoneNumber;
    public String lat;
    public String lng;

    public User() {

    }

    protected User(Parcel in) {
        name = in.readString();
        type = in.readString();
        id = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        lat = in.readString();
        lng = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(lat);
        dest.writeString(lng);
    }
}
