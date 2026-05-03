package com.example.larafinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
    private TextView tvRating; // تم ربطه بـ tvRatingValue
    private RatingBar ratingBar;
    private TextView tvReviewsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.journey_item_layout);

        // 1. ربط العناصر بالواجهة
        initViews();

        // 2. استقبال البيانات القادمة من الصفحة السابقة
        getAndSetIntentData();

        // 3. ضبط الحواف (الـ Padding) للواجهة
        setupWindowInsets();
    }

    private void initViews() {
        ivTripImage = findViewById(R.id.ivTripImage);
        tvTripName = findViewById(R.id.tvTripName);
        tvTripDescription = findViewById(R.id.tvTripDescription);
        tvRating = findViewById(R.id.tvRatingValue); // المعرف الصحيح من الـ XML
        ratingBar = findViewById(R.id.ratingBar);
        tvReviewsCount = findViewById(R.id.tvReviewsCount);
    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("name")) {
            String name = getIntent().getStringExtra("name");
            String description = getIntent().getStringExtra("description");
            String ratingValue = getIntent().getStringExtra("rating");
            String reviews = getIntent().getStringExtra("reviews");
            String imageBase64 = getIntent().getStringExtra("image");

            tvTripName.setText(name);
            tvTripDescription.setText(description);
            tvRating.setText(ratingValue);
            tvReviewsCount.setText("(" + (reviews != null ? reviews : "0") + " Reviews)");

            try {
                if (ratingValue != null) {
                    float ratingFloat = Float.parseFloat(ratingValue);
                    ratingBar.setRating(ratingFloat);
                }
            } catch (Exception e) {
                ratingBar.setRating(0);
            }

            if (imageBase64 != null && !imageBase64.isEmpty()) {
                ivTripImage.setImageBitmap(decodeBase64ToBitmap(imageBase64));
            }
        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
        }
    }

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
        // تأكد أن الـ root view في XML يحتوي على android:id="@+id/main"
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }
}