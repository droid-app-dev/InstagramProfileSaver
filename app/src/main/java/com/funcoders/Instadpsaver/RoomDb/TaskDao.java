package com.funcoders.Instadpsaver.RoomDb;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.funcoders.Instadpsaver.bean.ContactModel;
import com.funcoders.Instadpsaver.bean.InstaSearchHistorybean;
import com.funcoders.Instadpsaver.bean.ProfileBean;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM ProfileBean")
    List<ProfileBean> getAll();

    @Query("SELECT insta_id FROM ProfileBean")
    List<ProfileBean> getAllids();

    @Query("SELECT * FROM ContactModel")
    List<ContactModel> getContactNo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProfileBean task);


    @Delete
    void delete(ProfileBean task);


    //@Query("UPDATE TaskBean SET task=''")
   // @Update
   // UPDATE user SET first_name =:fname ,last_name=:lname WHERE email =:email


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertids(InstaSearchHistorybean task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertcontacts(ContactModel task);


    @Query("SELECT * FROM InstaSearchHistorybean")
    List<InstaSearchHistorybean> getAllsearchids();



    @Update
    void update(ProfileBean profileBean);


    @Query("DELETE FROM InstaSearchHistorybean")
     void deleteHistoryTable();

    /*@Query("UPDATE ProfileBean SET full_name=:full_name,followers=:followers, WHERE insta_id=:insta_id")
    void update(String insta_id, Integer followers, Integer following, String biography, String full_name, String profile_pic_url_hd);*/
}
