package com.example.larafinal.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.larafinal.data.MyTaskTable.MyTask;
import com.example.larafinal.data.MyTaskTable.MyTaskQuery;
import com.example.larafinal.data.MyUserTable.MyUser;
import com.example.larafinal.data.MyUserTable.MyUserQuery;
import com.example.larafinal.data.triptable.Trip;
import com.example.larafinal.data.triptable.TripQurery;

@Database(entities = {MyUser.class, MyTask.class, Trip.class}, version = 1)
public abstract class Appdatabase extends RoomDatabase {
    private static Appdatabase db;

    public abstract MyUserQuery myUserQuery();

    public abstract MyTaskQuery myTaskQuery();
    public abstract TripQurery tripQurery();

    public static Appdatabase getdb(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, Appdatabase.class, "appdatabase")
                    .fallbackToDestructiveMigration()  // Optional: handles database version changes
                    .build();

            return db;
        }
        return null;
    }
}
