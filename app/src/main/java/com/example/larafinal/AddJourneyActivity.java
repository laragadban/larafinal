package com.example.larafinal;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.larafinal.data.Appdatabase;
import com.example.larafinal.data.triptable.MyJourney;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddJourneyActivity extends AppCompatActivity {
    private static final String TAG = "AddJourney";

    // تعريف العناصر (UI Elements)
    private TextView tvHeader, tvTripType, tvRating, tvReviews;
    private TextInputEditText etTripName, etCountry, etTown, etAddress, etDescription;
    private RadioGroup rgTripType;
    private RadioButton rbBusiness, rbLeisure, rbFamily;
    private Slider sliderRating;
    private Button btnSaveTrip;
    private ImageView ivTripImage;
    private RecyclerView rvReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // 1. ربط العناصر بالكود
        initViews();

        // 2. إعداد مستمع الضغط على زر الحفظ
        btnSaveTrip.setOnClickListener(v -> {
            if (validateFields()) {
                // سيتم الحفظ داخل دالة validateFields إذا كانت البيانات صحيحة
            }
        });
    }

    private void initViews() {
        tvHeader = findViewById(R.id.tvHeader);
        tvTripType = findViewById(R.id.tvTripType);
        tvRating = findViewById(R.id.tvRating);
        tvReviews = findViewById(R.id.tvReviews);
        etTripName = findViewById(R.id.etTripName);
        etCountry = findViewById(R.id.etcountry);
        etTown = findViewById(R.id.etTown);
        etAddress = findViewById(R.id.etAddress);
        etDescription = findViewById(R.id.etDescription);
        rgTripType = findViewById(R.id.rgTripType);
        rbBusiness = findViewById(R.id.rbBusiness);
        rbLeisure = findViewById(R.id.rbLeisure);
        rbFamily = findViewById(R.id.rbFamily);
        sliderRating = findViewById(R.id.sliderRating);
        btnSaveTrip = findViewById(R.id.btnSaveTrip);
        ivTripImage = findViewById(R.id.ivTripImage);
        rvReviews = findViewById(R.id.rvReviews);
    }

    private boolean validateFields() {
        String tripName = etTripName.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        String town = etTown.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (tripName.isEmpty() || country.isEmpty() || town.isEmpty() || address.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        // تجهيز كائن الرحلة
        MyJourney myJourney = new MyJourney();
        myJourney.setTripname(tripName);
        myJourney.setAddress(address + ", " + town + ", " + country);
        myJourney.setTripdescription(description);

        // جلب التقييم والمسافة (كمثال)
        float ratingValue = sliderRating.getValue();
        myJourney.setRating(String.valueOf(ratingValue));
        myJourney.setDistance("Unknown"); // أو جلبها من حقل إدخال إذا وجد
        myJourney.setReviews("No reviews yet");

        // استدعاء دالة الحفظ
        saveMyJourney(myJourney);
        return true;
    }

    private void saveMyJourney(MyJourney myJourney) {
        try {
            // 1. الحفظ في Room Database (محلياً)
            // تأكد أنك تستخدم getMyJourneyDao() كما أصلحناها في Appdatabase
            Appdatabase.getdb(this).getMyJourneyDao().insert(myJourney);
            Toast.makeText(this, "Saved to Local DB", Toast.LENGTH_SHORT).show();

            // 2. الحفظ في Firebase Firestore (سحابياً)
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("journeys")
                    .add(myJourney)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Saved to Cloud successfully", Toast.LENGTH_SHORT).show();
                        finish(); // العودة للشاشة السابقة
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Firebase Error: " + e.getMessage());
                        Toast.makeText(this, "Cloud Save Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}