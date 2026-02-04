package com.example.larafinal.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.larafinal.data.MyUserTable.MyUser;
import com.example.larafinal.data.MyUserTable.MyUserQuery;
import com.example.larafinal.data.triptable.JourneyQurery;
import com.example.larafinal.data.triptable.MyJourney;

@Database(entities = {MyUser.class, MyJourney.class}, version = 2)
public abstract class Appdatabase extends RoomDatabase {
    public static Appdatabase db;

    public abstract MyUserQuery myUserQuery();

    public abstract JourneyQurery tripQurery();

    public static Appdatabase getdb(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, Appdatabase.class, "appdatabase")
                    .allowMainThreadQueries()  // Optional: handles database version changes
                    .build();
        }
            return db;

    }
}
