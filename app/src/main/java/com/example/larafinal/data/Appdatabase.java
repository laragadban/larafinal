package com.example.larafinal.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.larafinal.data.MyUserTable.MyUser;
import com.example.larafinal.data.MyUserTable.MyUserQuery;
import com.example.larafinal.data.triptable.JourneyQurery;
import com.example.larafinal.data.triptable.MyJourney;

/**
 * كلاس قاعدة البيانات الرئيسية للتطبيق باستخدام Room
 *
 * يحتوي على:
 * - جدول المستخدمين MyUser
 * - جدول الرحلات MyJourney
 */
@Database(entities = {MyUser.class, MyJourney.class}, version = 5)
public abstract class Appdatabase extends RoomDatabase {

    /**
     * متغير ثابت لتخزين نسخة واحدة من قاعدة البيانات
     * Singleton Pattern
     */
    public static Appdatabase db;

    /**
     * دالة للوصول إلى استعلامات المستخدمين
     *
     * @return كائن MyUserQuery
     */
    public abstract MyUserQuery myUserQuery();

    /**
     * دالة للوصول إلى استعلامات الرحلات
     *
     * @return كائن JourneyQurery
     */
    public abstract JourneyQurery tripQurery();

    /**
     * دالة لإنشاء أو إرجاع قاعدة البيانات
     *
     * @param context سياق التطبيق
     * @return نسخة قاعدة البيانات
     */
    public static Appdatabase getdb(Context context) {

        /**
         * إذا لم يتم إنشاء قاعدة البيانات مسبقاً
         */
        if (db == null) {

            db = Room.databaseBuilder(
                            context,
                            Appdatabase.class,
                            "appdatabase"
                    )

                    /**
                     * السماح بتنفيذ أوامر قاعدة البيانات
                     * على الـ Main Thread
                     */
                    .allowMainThreadQueries()

                    /**
                     * حذف وإعادة إنشاء قاعدة البيانات
                     * عند تغيير رقم الإصدار Version
                     */
                    .fallbackToDestructiveMigration()

                    /**
                     * إنشاء قاعدة البيانات
                     */
                    .build();
        }

        // إرجاع قاعدة البيانات
        return db;
    }
}