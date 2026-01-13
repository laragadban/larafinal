package com.example.larafinal.data.MyTaskTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.larafinal.data.triptable.MyJourney;

import java.util.List;

@Dao
public interface MyTaskQuery {
    @Query("SELECT * FROM MyTask")
    List<MyTask> getAll();

    @Insert
    void insert(MyTask myTask);

    @Delete
    void delete(MyTask myTask);

    @Update
    void update(MyTask myTask);

    @Query("SELECT * FROM MyTask WHERE task = :task")
    List<MyTask> getTaskByTask(String task);

    @Query("SELECT * FROM MyTask")
    MyJourney getAllTasks();
} // ONLY ONE brace should be here at the end.
