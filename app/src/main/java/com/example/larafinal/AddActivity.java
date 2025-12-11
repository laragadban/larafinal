package com.example.larafinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

public class AddActivity extends AppCompatActivity {
private TextView tvHeader;
private TextInputEditText etTripName;
private TextInputEditText etDestination;
private TextView tvTripType;
private RadioGroup rgTripType;
private RadioButton rbBusiness;
private RadioButton rbLeisure;
private RadioButton rbFamily;
private TextView tvRating;
private Slider sliderRating;
private Button btnSaveTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        tvHeader = findViewById(R.id.tvHeader);
        etTripName = findViewById(R.id.etTripName);
        etDestination = findViewById(R.id.etcountry);
        tvTripType = findViewById(R.id.tvTripType);
        rgTripType = findViewById(R.id.rgTripType);
        rbBusiness = findViewById(R.id.rbBusiness);
        rbLeisure = findViewById(R.id.rbLeisure);
        rbFamily = findViewById(R.id.rbFamily);
        tvRating = findViewById(R.id.tvRating);
        sliderRating = findViewById(R.id.sliderRating);
        btnSaveTrip = findViewById(R.id.btnSaveTrip);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}