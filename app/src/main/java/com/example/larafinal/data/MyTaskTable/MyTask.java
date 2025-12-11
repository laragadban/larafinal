package com.example.larafinal.data.MyTaskTable;

import androidx.room.Entity; // 1. Import this
import androidx.room.PrimaryKey;

@Entity // 2. Add this annotation
public class MyTask {
   @PrimaryKey(autoGenerate = true)
   public long id;
   public String task;
   public boolean isDone;
}
