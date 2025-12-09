package com.example.larafinal;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class AddActivity extends AppCompatActivity {
private TextView tvHeader;
private TextInputEditText etTripName;
private TextInputEditText etDestination;
private TextInputEditText etPrice;
private TextView tvTripType;
private RadioGroup rgTripType;
private RadioGroup rbBusiness;
private RadioGroup rbLeisure;
private RadioGroup rbFamily;
private     tesliderRating;
private Button btnSaveTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}