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

     ivTripImage = findViewById(R.id.ivTripImage);
     tvTripName = findViewById(R.id.tvTripName);
     tvTripDescription = findViewById(R.id.tvTripDescription);
     tvRating = findViewById(R.id.tvRating);
     ratingBar = findViewById(R.id.ratingBar);
     tvReviews = findViewById(R.id.tvReviews);
     tvReviewsCount = findViewById(R.id.tvReviewsCount);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
