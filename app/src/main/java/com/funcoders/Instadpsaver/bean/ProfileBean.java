package com.funcoders.Instadpsaver.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ProfileBean implements Serializable {


    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name="insta_id")
    private String insta_id = "";

    @ColumnInfo(name = "full_name")
    private String full_name = "";

    @ColumnInfo(name = "followers")
    private Integer followers = 0;

    @ColumnInfo(name = "following")
    private Integer following = 0;

    @ColumnInfo(name = "profile_pic_url_hd")
    private String profile_pic_url_hd = "";

    @ColumnInfo(name = "biography")
    private String biography = "";

    @ColumnInfo(name = "is_private")
    private String is_private="" ;

    public String getIs_private() {
        return is_private;
    }

    public void setIs_private(String is_private) {
        this.is_private = is_private;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getInsta_id() {
        return insta_id;
    }

    public void setInsta_id(String insta_id) {
        this.insta_id = insta_id;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public String getProfile_pic_url_hd() {
        return profile_pic_url_hd;
    }

    public void setProfile_pic_url_hd(String profile_pic_url_hd) {
        this.profile_pic_url_hd = profile_pic_url_hd;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
