package com.example.larafinal.data.triptable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
  كلاس (Entity) يمثل جدول الرحلات في قاعدة بيانات

 */
@Entity(tableName = "MyJourney") // تعريف الكلاس كجدول في قاعدة البيانات باسم MyJourney
public class MyJourney {

    /**
     * المعرف الفريد (Primary Key) للرحلة.
     * autoGenerate = true تعني أن قاعدة البيانات ستعطي رقمًا تلقائيًا لكل رحلة جديدة.
     */
    @PrimaryKey(autoGenerate = true)
    private long journeyId;

    /**
     * اسم الرحلة.
     * @ColumnInfo تستخدم لتحديد اسم العمود في قاعدة البيانات بشكل مختلف عن اسم المتغير في الجافا.
     */
    @ColumnInfo(name = "trip_Name")
    private String tripName;

    /**
     * وصف الرحلة (تفاصيل حول المكان أو التجربة).
     */
    @ColumnInfo(name = "trip_description")
    private String tripDescription;

    /**
     * العنوان الجغرافي للرحلة (مثلاً: الدولة، المدينة، الشارع).
     */
    @ColumnInfo(name = "address")
    private String address;

    /**
     * المسافة المقطوعة أو مسافة الرحلة.
     */
    @ColumnInfo(name = "distance")
    private String distance;

    /**
     * التقييم الخاص بالرحلة (مثلاً: من 1 إلى 5 نجوم).
     */
    @ColumnInfo(name = "rating")
    private String rating;

    /**
     * المراجعات أو التعليقات المكتوبة حول هذه الرحلة.
     */
    @ColumnInfo(name = "reviews")
    private String reviews;

    // --- الدوال الأساسية (Getters & Setters) ---
    // ملاحظة: مكتبة Room تحتاج لهذه الدوال للوصول إلى المتغيرات الخاصة (private).

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    /**
     * دالة مساعدة لطباعة بيانات الكائن بشكل نصي (مفيدة في الـ Log).
     */
    @Override
    public String toString() {
        return "MyJourney{" +
                "id=" + journeyId +
                ", name='" + tripName + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}