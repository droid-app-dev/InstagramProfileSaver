package com.funcoders.Instadpsaver.RoomDb;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.funcoders.Instadpsaver.bean.ContactModel;
import com.funcoders.Instadpsaver.bean.InstaSearchHistorybean;
import com.funcoders.Instadpsaver.bean.ProfileBean;

@Database(version = 3,entities = {ProfileBean.class, InstaSearchHistorybean.class, ContactModel.class})
public abstract class TaskAppDatabase extends RoomDatabase {

public abstract TaskDao taskDao();

    private static TaskAppDatabase noteDB;

    public static TaskAppDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }
    private static TaskAppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                TaskAppDatabase.class, "SaveInstaProfile")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        noteDB = null;
    }


}
