package com.funcoders.Instadpsaver.bean;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ContactModel {



    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "mob_No")
    public String mobileNumber="";

    @ColumnInfo(name = "id")
    public String id="";

    @ColumnInfo(name = "full_Name")
    public String name="";



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


}
