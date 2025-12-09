package com.example.larafinal.data.triptable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripQurery {
    @Query("SELECT * FROM Trip")
    List<Trip> getAll();
    @Insert
    void insert(Trip trip);
    @Delete
    void delete(Trip trip);
    @Update
    void update(Trip trip);


}
