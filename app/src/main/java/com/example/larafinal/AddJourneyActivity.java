
package com.example.larafinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import androidx.recyclerview.widget.RecyclerView;

public class AddJourneyActivity extends AppCompatActivity {

    // تعريف العناصر (UI Elements)
    private TextInputEditText etTripName, etCountry, etTown, etAddress, etDescription;
    private RadioGroup rgTripType;
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
            saveTripData();
        });
    }

    private void initViews() {
        etTripName = findViewById(R.id.etTripName);
        etCountry = findViewById(R.id.etcountry);
        etTown = findViewById(R.id.etTown);
        etAddress = findViewById(R.id.etAddress); // تأكد من إصلاح ID المتكرر في XML
        etDescription = findViewById(R.id.etDescription);
        rgTripType = findViewById(R.id.rgTripType);
        sliderRating = findViewById(R.id.sliderRating);
        btnSaveTrip = findViewById(R.id.btnSaveTrip);
        ivTripImage = findViewById(R.id.ivTripImage);
        rvReviews = findViewById(R.id.rvReviews);
    }

    private void saveTripData() {
        // استخراج البيانات من الحقول
        String name = etTripName.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        String town = etTown.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        float rating = sliderRating.getValue();

        // معرفة نوع الرحلة المختار
        String tripType = "";
        int selectedId = rgTripType.getCheckedRadioButtonId();
        if (selectedId == R.id.rbBusiness) {
            tripType = "Activity";
        } else if (selectedId == R.id.rbLeisure) {
            tripType = "Archaeological site";
        } else if (selectedId == R.id.rbFamily) {
            tripType = "Restaurant";
        }

        // التحقق من أن الحقول الأساسية ليست فارغة
        if (name.isEmpty() || country.isEmpty()) {
            Toast.makeText(this, "Please enter Trip Name and Country", Toast.LENGTH_SHORT).show();
            return;
        }

        // هنا يتم إنشاء كائن الـ Entity وحفظه في قاعدة البيانات
        // مثال (حسب اسم الـ Entity الخاص بك):
        // MyTask newTrip = new MyTask(name, country, town, address, description, rating, tripType);

        // ملاحظة: الحفظ يجب أن يكون في Background Thread
        Toast.makeText(this, "Saving: " + name + " in " + country, Toast.LENGTH_LONG).show();

        // إغلاق النشاط بعد الحفظ
        finish();
    }
}
