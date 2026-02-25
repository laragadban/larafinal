package com.example.larafinal.data.triptable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * كلاس يمثل جدول الرحلات داخل قاعدة البيانات باستخدام Room Database.
 *
 * @Entity:
 *  - يعرّف هذا الكلاس كجدول داخل قاعدة البيانات.
 *  - tableName = "MyJourney" هو اسم الجدول في قاعدة البيانات.
 */
@Entity(tableName = "MyJourney")
public class MyJourney {

    /**
     * المفتاح الأساسي (Primary Key) للجدول.
     * autoGenerate = true يعني أن Room سيقوم بتوليد ID تلقائيًا.
     */
    @PrimaryKey(autoGenerate = true)
    private long journeyId;

    /**
     * اسم الرحلة.
     * سيتم تخزينه في العمود trip_Name داخل قاعدة البيانات.
     */
    @ColumnInfo(name = "trip_Name")
    private String tripName;

    /**
     * وصف الرحلة.
     */
    @ColumnInfo(name = "trip_description")
    private String tripDescription;

    /**
     * عنوان الرحلة أو موقعها.
     */
    @ColumnInfo(name = "address")
    private String address;

    /**
     * المسافة إلى الرحلة.
     */
    @ColumnInfo(name = "distance")
    private String distance;

    /**
     * تقييم الرحلة (Rating).
     */
    @ColumnInfo(name = "rating")
    private String rating;

    /**
     * عدد المراجعات (Reviews).
     */
    @ColumnInfo(name = "reviews")
    private String reviews;

    // =============================
    // ====== Getters & Setters =====
    // =============================

    /**
     * إرجاع رقم الرحلة (ID).
     */
    public long getJourneyId() {
        return journeyId;
    }

    /**
     * تعيين رقم الرحلة.
     */
    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    /**
     * إرجاع اسم الرحلة.
     */
    public String getTripName() {
        return tripName;
    }

    /**
     * تعيين اسم الرحلة.
     */
    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    /**
     * إرجاع وصف الرحلة.
     */
    public String getTripDescription() {
        return tripDescription;
    }

    /**
     * تعيين وصف الرحلة.
     */
    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    /**
     * إرجاع العنوان.
     */
    public String getAddress() {
        return address;
    }

    /**
     * تعيين العنوان.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * إرجاع المسافة.
     */
    public String getDistance() {
        return distance;
    }

    /**
     * تعيين المسافة.
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     * إرجاع التقييم.
     */
    public String getRating() {
        return rating;
    }

    /**
     * تعيين التقييم.
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * إرجاع عدد المراجعات.
     */
    public String getReviews() {
        return reviews;
    }

    /**
     * تعيين عدد المراجعات.
     */
    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    /**
     * دالة مساعدة تُستخدم أحيانًا في الـ Adapter لإرجاع ID.
     */
    public long getId() {
        return journeyId;
    }

    /**
     * دالة لتحويل الكائن إلى نص (تستخدم غالبًا في التصحيح Debugging).
     */
    @Override
    public String toString() {
        return "MyJourney{" +
                "id=" + journeyId +
                ", name='" + tripName + '\'' +
                '}';
    }
}