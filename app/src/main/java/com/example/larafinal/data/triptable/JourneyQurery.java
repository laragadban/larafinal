package com.example.larafinal.data.triptable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JourneyQurery {
    @Query("SELECT * FROM MyJourney")
    List<MyJourney> getAll();
    @Insert
    void insert(MyJourney trip);
    @Delete
    void delete(MyJourney trip);
    @Update
    void update(MyJourney trip);


}
