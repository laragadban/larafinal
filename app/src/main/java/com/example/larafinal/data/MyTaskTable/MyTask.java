package com.example.larafinal.data.MyTaskTable;

import androidx.room.Entity; // 1. Import this
import androidx.room.PrimaryKey;

@Entity // 2. Add this annotation حول هذا الكلاس إلى جدول حقيقي داخل ذاكرة الهاتف".
public class MyTask {
   @PrimaryKey(autoGenerate = true)
   public long id;
   public String task;
   public boolean isDone;

   public String getTaskName() {
      return task;
   }
}
