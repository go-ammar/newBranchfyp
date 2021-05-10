package com.fypapplication.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MechMyAccount implements Parcelable {

    public static final Creator<MechMyAccount> CREATOR = new Creator<MechMyAccount>() {
        @Override
        public MechMyAccount createFromParcel(Parcel in) {
            return new MechMyAccount(in);
        }

        @Override
        public MechMyAccount[] newArray(int size) {
            return new MechMyAccount[size];
        }
    };
    public String name;
    public String date;
    public String description;
    public int cost;
    public String phoneNumber;

    public MechMyAccount() {

    }

    protected MechMyAccount(Parcel in) {
        name = in.readString();
        date = in.readString();
        description = in.readString();
        phoneNumber = in.readString();
        cost = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeString(phoneNumber);
        dest.writeInt(cost);
    }
}
