package com.example.larafinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

     private RatingBar ratingBar ;
     private TextView tvReviews;

     private TextView tvReviewsCount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.journey_item_layout);
//البحث عن عنصر يحمل ID
     ivTripImage = findViewById(R.id.ivTripImage);
     tvTripName = findViewById(R.id.tvTripName);
     tvTripDescription = findViewById(R.id.tvTripDescription);
     tvRating = findViewById(R.id.tvRating);
     ratingBar = findViewById(R.id.ratingBar);
     tvReviews = findViewById(R.id.tvReviews);
     tvReviewsCount = findViewById(R.id.tvReviewsCount);

/**
 * ضبط الهوامش (Padding) للواجهة بحيث تتوافق مع أشرطة النظام.
 *
 * <p>
 * يتم استخدام ViewCompat.setOnApplyWindowInsetsListener للاستماع
 * إلى التغييرات في أبعاد أشرطة النظام مثل:
 * </p>
 *
 * <ul>
 *     <li>Status Bar (شريط الحالة العلوي)</li>
 *     <li>Navigation Bar (شريط التنقل السفلي)</li>
 * </ul>
 *
 * <p>
 * عند استلام قيم الـ Insets:
 * يتم استخراج أبعاد systemBars
 * ثم ضبط الـ padding للـ View الأساسي (R.id.main)
 * بحيث لا يتداخل محتوى الواجهة مع أشرطة النظام.
 * </p>
 *
 * <p>
 * هذا مهم خاصة عند استخدام وضع Edge-to-Edge
 * لعرض المحتوى بكامل الشاشة.
 * </p>
 */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
