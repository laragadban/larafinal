package com.example.larafinal.data.triptable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * JourneyQurery
 *
 * <p>
 * هذا هو DAO (Data Access Object) الخاص بجدول الرحلات (MyJourney)
 * ويحتوي على أوامر الوصول للبيانات داخل قاعدة البيانات المحلية Room.
 * </p>
 *
 * <p>الوظائف الرئيسية:</p>
 * <ul>
 *     <li>getAll() : جلب جميع الرحلات.</li>
 *     <li>insert(MyJourney trip) : إضافة رحلة جديدة.</li>
 *     <li>delete(MyJourney trip) : حذف رحلة موجودة.</li>
 *     <li>update(MyJourney trip) : تحديث بيانات رحلة موجودة.</li>
 * </ul>
 */
@Dao
public interface JourneyQurery {

    /**
     * جلب جميع عناصر الرحلات من جدول MyJourney.
     *
     * @return قائمة تحتوي على جميع الرحلات.
     */
    @Query("SELECT * FROM MyJourney")
    List<MyJourney> getAll();

    /**
     * إدراج رحلة جديدة في الجدول.
     *
     * @param trip كائن الرحلة المراد إضافته.
     */
    @Insert
    void insert(MyJourney trip);

    /**
     * حذف رحلة من الجدول.
     *
     * @param trip كائن الرحلة المراد حذفه.
     */
    @Delete
    void delete(MyJourney trip);

    /**
     * تحديث بيانات رحلة موجودة في الجدول.
     *
     * @param trip كائن الرحلة مع البيانات المحدثة.
     */
    @Update
    void update(MyJourney trip);
}