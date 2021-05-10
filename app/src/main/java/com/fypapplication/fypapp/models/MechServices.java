package com.fypapplication.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MechServices implements Parcelable {
    public String service;
    public String vehicleType;
    public int price;
    public String id;

    protected MechServices(Parcel in) {
        service = in.readString();
        vehicleType = in.readString();
        id = in.readString();
        price = in.readInt();
    }

    public MechServices(){

    }

    public static final Creator<MechServices> CREATOR = new Creator<MechServices>() {
        @Override
        public MechServices createFromParcel(Parcel in) {
            return new MechServices(in);
        }

        @Override
        public MechServices[] newArray(int size) {
            return new MechServices[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(service);
        dest.writeString(vehicleType);
        dest.writeString(id);
        dest.writeInt(price);
    }
}
