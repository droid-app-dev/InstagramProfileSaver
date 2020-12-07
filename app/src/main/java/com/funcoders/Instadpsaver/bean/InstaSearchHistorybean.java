package com.funcoders.Instadpsaver.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class InstaSearchHistorybean implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name="insta_id")
    private String insta_id = "";

    public String getInsta_id() {
        return insta_id;
    }

    public void setInsta_id(String insta_id) {
        this.insta_id = insta_id;
    }

}
