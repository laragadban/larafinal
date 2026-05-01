package com.example.larafinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class journey_item extends AppCompatActivity {
    private ImageView ivTripImage;
    private TextView tvTripName;
    private TextView tvTripDescription;
    private TextView tvRating;
    private RatingBar ratingBar;
    private TextView tvReviews;
    private TextView tvReviewsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.journey_item_layout);

        // 1. ربط العناصر بالواجهة
        initViews();

        // 2. استقبال البيانات القادمة من الصفحة السابقة (Adapter)
        getAndSetIntentData();

        // 3. ضبط الحواف (الـ Padding) للواجهة
        setupWindowInsets();
    }

    private void initViews() {
        ivTripImage = findViewById(R.id.ivTripImage);
        tvTripName = findViewById(R.id.tvTripName);
        tvTripDescription = findViewById(R.id.tvTripDescription);
        tvRating = findViewById(R.id.tvRating);
        ratingBar = findViewById(R.id.ratingBar);
        //tvReviews = findViewById(R.id.tvReviews);
        tvReviewsCount = findViewById(R.id.tvReviewsCount);
    }

    private void getAndSetIntentData() {
        // التحقق مما إذا كانت هناك بيانات مرسلة عبر الـ Intent
        if (getIntent().hasExtra("name")) {
            // جلب النصوص
            String name = getIntent().getStringExtra("name");
            String description = getIntent().getStringExtra("description");
            String ratingValue = getIntent().getStringExtra("rating");
            String reviews = getIntent().getStringExtra("reviews");
            String imageBase64 = getIntent().getStringExtra("image");

            // عرض النصوص في العناصر
            tvTripName.setText(name);
            tvTripDescription.setText(description);
            tvRating.setText(ratingValue);
            tvReviews.setText(reviews);
            tvReviewsCount.setText("(" + reviews + " Reviews)");

            // ضبط النجوم في الـ RatingBar
            try {
                float ratingFloat = Float.parseFloat(ratingValue);
                ratingBar.setRating(ratingFloat);
            } catch (Exception e) {
                ratingBar.setRating(0);
            }

            // تحويل الصورة من نص Base64 إلى Bitmap وعرضها
            if (imageBase64 != null && !imageBase64.isEmpty()) {
                ivTripImage.setImageBitmap(decodeBase64ToBitmap(imageBase64));
            }
        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
        }
    }

    // دالة مساعدة لتحويل النص (Base64) إلى صورة (Bitmap)
    private Bitmap decodeBase64ToBitmap(String base64Str) {
        try {
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}