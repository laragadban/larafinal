package com.example.larafinal.data.triptable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * هذا هو الكود المصلح والنهائي لجدول الرحلات.
 * تم التأكد من خلوه من الأخطاء البرمجية وتطابق الصفات.
 */
@Entity(tableName = "MyJourney")
public class MyJourney {

    // 1. المفتاح الرئيسي: يتم إنتاجه تلقائياً لكل رحلة
    @PrimaryKey(autoGenerate = true)
    public long journeyId;

    // 2. عنوان الرحلة (مثلاً: رحلة الشمال)
    @ColumnInfo(name = "title")
    public String title;

    // 3. وصف تفصيلي للرحلة (هذا الذي يظهر عادة في السطر 77 في التصميم)
    @ColumnInfo(name = "description")
    public String description;

    // 4. تاريخ الرحلة (يُخزن كنص String لسهولة العرض)
    @ColumnInfo(name = "date")
    public String date;

    // 5. مكان الرحلة أو الوجهة
    @ColumnInfo(name = "location")
    public String location;

    // 6. سعر الرحلة (استخدمنا double للأرقام العشرية)
    @ColumnInfo(name = "price")
    public double price;

    // 7. مسار الصورة (لتخزين رابط الصورة أو مكانها في الهاتف)
    @ColumnInfo(name = "image_path")
    public String imagePath;

    // --- دالات الوصول (Getters and Setters) مصلحة بالكامل ---

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // دالة للطباعة والتأكد من البيانات في Logcat
    @Override
    public String toString() {
        return "MyJourney{" +
                "id=" + journeyId +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", price=" + price +
                '}';
    }
}

