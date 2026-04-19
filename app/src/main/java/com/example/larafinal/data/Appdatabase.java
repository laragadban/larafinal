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
 * Appdatabase
 *
 * <p>
 * يمثل هذا الكلاس قاعدة البيانات المحلية للتطبيق باستخدام Room.
 * </p>
 *
 * <p>
 * يحتوي على كيانين (Entities):
 * </p>
 * <ul>
 *     <li>MyUser : جدول المستخدمين.</li>
 *     <li>MyJourney : جدول الرحلات.</li>
 * </ul>
 * لضمان وجود نسخة واحدة فقط من قاعدة البيانات في التطبيق.
 */
@Database(entities = {MyUser.class, MyJourney.class}, version = 2)
public abstract class Appdatabase extends RoomDatabase {

    /**
     * متغير ثابت لتخزين نسخة قاعدة البيانات (Singleton Instance).
     */
    public static Appdatabase db;

    /**
     * توفير الوصول إلى عمليات جدول المستخدمين (DAO).
     *
     * @return كائن MyUserQuery الذي يحتوي على أوامر الإدخال والاستعلام.
     */
    public abstract MyUserQuery myUserQuery();

    /**
     * توفير الوصول إلى عمليات جدول الرحلات (DAO).
     *
     * @return كائن JourneyQurery الذي يحتوي على أوامر إدارة الرحلات.
     */
    public abstract JourneyQurery tripQurery();

    /**
     * الحصول على نسخة قاعدة البيانات.
     *
     * <p>
     * في حال لم يتم إنشاء قاعدة البيانات مسبقاً،
     * يتم بناؤها باستخدام Room.databaseBuilder.
     * </p>
     *
     * <p>
     * allowMainThreadQueries():
     * تسمح بتنفيذ عمليات قاعدة البيانات على الـ Main Thread
     * (غير مستحسن في التطبيقات الكبيرة لأنه قد يسبب بطء في الواجهة).
     * </p>
     *
     * @param context سياق التطبيق (Application Context).
     * @return نسخة واحدة من قاعدة البيانات.
     */
    public static Appdatabase getdb(Context context) {

        if (db == null) {

            db = Room.databaseBuilder(
                            context,
                            Appdatabase.class,
                            "appdatabase"
                    )
                    .allowMainThreadQueries()
                    .build();
        }

        return db;
    }
}