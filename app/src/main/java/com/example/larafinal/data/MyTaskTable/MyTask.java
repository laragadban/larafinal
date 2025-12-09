package com.example.larafinal.data.MyTaskTable;

import androidx.room.PrimaryKey;

public class MyTask {
   @PrimaryKey(autoGenerate = true)
   public long id;
   public String task;
   public boolean isDone;

}
