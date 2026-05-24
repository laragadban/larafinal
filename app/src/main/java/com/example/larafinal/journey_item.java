package com.example.larafinal;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * شاشة عرض عنصر رحلة
 *
 * تعرض:
 * - صورة الرحلة
 * - اسم الرحلة
 * - وصف الرحلة
 * - التقييم
 * - عدد المراجعات
 */
public class journey_item extends AppCompatActivity {

    /**
     * عناصر الواجهة الرسومية
     */
    private ImageView ivTripImage;

    private TextView tvTripName;

    private TextView tvTripDescription;

    /**
     * نص عرض قيمة التقييم
     */
    private TextView tvRating;
    private Button btnGemini;
    private ImageButton btnLike;
    private boolean isLiked = false;

    /**
     * شريط التقييم
     */
    private RatingBar ratingBar;

    /**
     * عدد المراجعات
     */
    private TextView tvReviewsCount;


    /**
     * الدالة الرئيسية عند تشغيل الصفحة
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // تفعيل عرض الشاشة حتى الحواف
        EdgeToEdge.enable(this);

        // ربط ملف التصميم
        setContentView(R.layout.journey_item_layout);

        /**
         * 1. ربط عناصر الواجهة
         */
        initViews();

        /**
         * 2. استقبال البيانات القادمة من الصفحة السابقة
         */
        getAndSetIntentData();

        /**
         * 3. ضبط الحواف Padding للواجهة
         */
        setupWindowInsets();
    }

    /**
     * ربط عناصر الواجهة الرسومية بالكود
     */
    @SuppressLint("WrongViewCast")
    private void initViews() {

        ivTripImage = findViewById(R.id.ivTripImage);

        tvTripName = findViewById(R.id.tvTripName);

        tvTripDescription = findViewById(R.id.tvTripDescription);

        // عنصر عرض قيمة التقييم
        tvRating = findViewById(R.id.tvRatingValue);

        ratingBar = findViewById(R.id.ratingBar);

        tvReviewsCount = findViewById(R.id.tvReviewsCount);

        btnLike = findViewById(R.id.btnLike);



        btnLike.setOnClickListener(v -> toggleLike());
    }

    /**
     * استقبال البيانات المرسلة عبر Intent
     * وعرضها داخل الصفحة
     */
    private void getAndSetIntentData() {

        /**
         * التأكد من وجود بيانات
         */
        if (getIntent().hasExtra("name")) {

            // استقبال البيانات
            String name =
                    getIntent().getStringExtra("name");

            String description =
                    getIntent().getStringExtra("description");

            String ratingValue =
                    getIntent().getStringExtra("rating");

            String reviews =
                    getIntent().getStringExtra("reviews");

            String imageBase64 =
                    getIntent().getStringExtra("image");

            /**
             * عرض البيانات داخل الواجهة
             */
            tvTripName.setText(name);

            tvTripDescription.setText(description);

            tvRating.setText(ratingValue);

            tvReviewsCount.setText(
                    "(" +
                            (reviews != null ? reviews : "0")
                            + " Reviews)"
            );

            /**
             * تحويل التقييم النصي إلى رقم
             * وعرضه داخل RatingBar
             */
            try {

                if (ratingValue != null) {

                    float ratingFloat =
                            Float.parseFloat(ratingValue);

                    ratingBar.setRating(ratingFloat);
                }

            } catch (Exception e) {

                ratingBar.setRating(0);
            }

            /**
             * عرض الصورة إذا كانت موجودة
             */
            if (imageBase64 != null
                    && !imageBase64.isEmpty()) {

                ivTripImage.setImageBitmap(
                        decodeBase64ToBitmap(imageBase64)
                );
            }

        } else {

            /**
             * رسالة في حال عدم وصول بيانات
             */
            Toast.makeText(
                    this,
                    "No data received",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    /**
     * تحويل نص Base64 إلى صورة Bitmap
     *
     * @param base64Str النص المشفر للصورة
     * @return صورة Bitmap
     */
    private Bitmap decodeBase64ToBitmap(String base64Str) {

        try {

            // فك تشفير النص إلى Bytes
            byte[] decodedBytes =
                    Base64.decode(base64Str, Base64.DEFAULT);

            // تحويل الـ Bytes إلى Bitmap
            return BitmapFactory.decodeByteArray(
                    decodedBytes,
                    0,
                    decodedBytes.length
            );

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    /**
     * ضبط الحواف الخاصة بالنظام
     * مثل شريط الحالة والتنقل
     */
    private void setupWindowInsets() {

        /**
         * التأكد من وجود العنصر الرئيسي
         */
        View mainView = findViewById(R.id.main);

        if (mainView != null) {

            ViewCompat.setOnApplyWindowInsetsListener(
                    mainView,

                    (v, insets) -> {

                        Insets systemBars =
                                insets.getInsets(
                                        WindowInsetsCompat.Type.systemBars()
                                );

                        v.setPadding(
                                systemBars.left,
                                systemBars.top,
                                systemBars.right,
                                systemBars.bottom
                        );

                        return insets;
                    });
        }
    }

    /**
     * تبديل حالة الإعجاب
     * يغير لون القلب من أبيض إلى أحمر والعكس
     */
    private void toggleLike() {
        isLiked = !isLiked;

        if (isLiked) {
            btnLike.setColorFilter(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            btnLike.setColorFilter(getResources().getColor(android.R.color.white));
        }
    }
}