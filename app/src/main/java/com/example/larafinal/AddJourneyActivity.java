
package com.example.larafinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.larafinal.data.Appdatabase;
import com.example.larafinal.data.MyUserTable.MyUser;
import com.example.larafinal.data.triptable.MyJourney;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import androidx.recyclerview.widget.RecyclerView;

public class AddJourneyActivity extends AppCompatActivity {

    // تعريف العناصر (UI Elements)
    private TextView tvHeader , tvTripType , tvRating,tvReviews;
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

        // 1. ربط العناصر بالكود (Initialization)
        initViews();

        // 2. إعداد مستمع الضغط على زر الحفظ
        btnSaveTrip.setOnClickListener(v -> {
            validateFields();
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
        boolean isValid = true;

        String tripName = etTripName.getText().toString().trim();
        if (tripName.isEmpty()) {
            etTripName.setError("Trip name is required");
            isValid = false;
        }

        String country = etCountry.getText().toString().trim();
        if (country.isEmpty()) {
            etCountry.setError("Country name is required");
            isValid = false;
        }

        String town = etTown.getText().toString().trim();
        if (town.isEmpty()) {
            etTown.setError("Town name is required");
            isValid = false;
        }

        String address = etAddress.getText().toString().trim();
        if (address.isEmpty()) {
            etAddress.setError("Address is required");
            isValid = false;
        }

        String description = etDescription.getText().toString().trim();
        if (description.isEmpty()) {
            etDescription.setError("Description is required");
            isValid = false;
        }

        if (isValid) {
            MyJourney myJourney = new MyJourney();
            myJourney.setTripName(tripName);
            myJourney.setCountry(country);
            myJourney.setTown(town);
            myJourney.setAddress(address);
            myJourney.setDescription(description);
            Appdatabase.getdb(this).tripQurery().insert(myJourney);
            Toast.makeText(this, "Trip saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
        return isValid;
    }
}
