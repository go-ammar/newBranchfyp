package com.fypapplication.fypapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ChangesDue implements Parcelable {

    public static final Creator<ChangesDue> CREATOR = new Creator<ChangesDue>() {
        @Override
        public ChangesDue createFromParcel(Parcel in) {
            return new ChangesDue(in);
        }

        @Override
        public ChangesDue[] newArray(int size) {
            return new ChangesDue[size];
        }
    };
    public String typeOfChange;
    public String dateOfChange;
    public int duration;
    public String userName = "";
    public String userNumber = "";

    public ChangesDue() {

    }

    protected ChangesDue(Parcel in) {
        typeOfChange = in.readString();
        dateOfChange = in.readString();
        duration = in.readInt();
        userName = in.readString();
        userNumber = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(typeOfChange);
        dest.writeString(dateOfChange);
        dest.writeInt(duration);
        dest.writeString(userName);
        dest.writeString(userNumber);
    }
}
