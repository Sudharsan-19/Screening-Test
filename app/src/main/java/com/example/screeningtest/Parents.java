package com.example.screeningtest;

import android.os.Parcel;
import android.os.Parcelable;


public class Parents implements Parcelable{
    private String Pname1;
    private String ParentEmail1;
    private String ParentMotherTongue;
    private String Password;
    private String CP1;
    private String ParentPhno1;

//    public Parents() {
//    }

    protected Parents(Parcel in) {
        Pname1 = in.readString();
        ParentEmail1 = in.readString();
        ParentMotherTongue = in.readString();
        Password = in.readString();
        CP1 = in.readString();
        ParentPhno1 = in.readString();
    }

    public static final Creator<Parents> CREATOR = new Creator<Parents>() {
        @Override
        public Parents createFromParcel(Parcel in) {
            return new Parents(in);
        }

        @Override
        public Parents[] newArray(int size) {
            return new Parents[size];
        }
    };

    public String getPName1() {
        return Pname1;
    }

    public void setPName1(String pname1) {
        this.Pname1 = pname1;
    }

    public String getPArentEmail1() {
        return ParentEmail1;
    }

    public void setPArentEmail1(String parentEmail1) {
        this.ParentEmail1 = parentEmail1;
    }

    public String getPArentMotherTongue() {
        return ParentMotherTongue;
    }

    public void setPArentMotherTongue(String parentMotherTongue) {
        this.ParentMotherTongue = parentMotherTongue;
    }

    public String getPAssword() {
        return Password;
    }

    public void setPAssword(String password) {
        this.Password = password;
    }

    public String getCP11() {
        return CP1;
    }

    public void setCP11(String CP1) {
        this.CP1 = CP1;
    }

    public String getPArentPhno1() {
        return ParentPhno1;
    }

    public void setPArentPhno1(String parentPhno1) {
        this.ParentPhno1 = parentPhno1;
    }

    public Parents(String pname1, String parentEmail1, String parentMotherTongue, String password, String CP1, String parentPhno1) {
        this.Pname1 = pname1;
        this.ParentEmail1 = parentEmail1;
        this.ParentMotherTongue = parentMotherTongue;
        this.Password = password;
        this.CP1 = CP1;
        this.ParentPhno1 = parentPhno1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Pname1);
        parcel.writeString(ParentEmail1);
        parcel.writeString(ParentMotherTongue);
        parcel.writeString(Password);
        parcel.writeString(CP1);
        parcel.writeString(ParentPhno1);
    }
}
