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
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.recyclerview.widget.RecyclerView;

/**
 * AddJourneyActivity
 *
 * <p>
 * هذه الواجهة مسؤولة عن إضافة رحلة جديدة إلى التطبيق.
 * </p>
 *
 * <p>
 * الوظائف الرئيسية:
 * </p>
 * <ul>
 *     <li>إدخال بيانات الرحلة من المستخدم.</li>
 *     <li>التحقق من صحة الحقول.</li>
 *     <li>حفظ البيانات في قاعدة البيانات المحلية (Room).</li>
 *     <li>حفظ البيانات في Firebase Firestore.</li>
 * </ul>
 *
 * @version 1.0
 */
public class AddJourneyActivity extends AppCompatActivity {

    // ================== عناصر الواجهة ==================
    private TextView tvHeader , tvTripType , tvRating,tvReviews;
    private TextInputEditText etTripName, etCountry, etTown, etAddress, etDescription;
    private RadioGroup rgTripType;
    private RadioButton rbBusiness, rbLeisure, rbFamily;
    private Slider sliderRating;
    private Button btnSaveTrip;
    private ImageView ivTripImage;
    private RecyclerView rvReviews;

    /**
     * يتم استدعاؤها عند إنشاء الـ Activity.
     * تقوم بتحميل الواجهة وربط العناصر
     * وتحديد حدث الضغط على زر الحفظ.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // ربط عناصر الواجهة
        initViews();

        // حدث الضغط على زر الحفظ
        btnSaveTrip.setOnClickListener(v -> {
            validateFields();
        });
    }

    /**
     * ربط عناصر الواجهة الرسومية (XML)
     * مع المتغيرات داخل الكود.
     */
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

    /**
     * التحقق من صحة بيانات الإدخال.
     *
     * <p>
     * يتم التأكد من أن جميع الحقول المطلوبة غير فارغة.
     * في حال كانت البيانات صحيحة يتم:
     * </p>
     *
     * <ul>
      *     <li>تعبئة بيانات الرحلة.</li>
     *     <li>تحديد نوع الرحلة (Business / Leisure / Family).</li>
     *     <li>استدعاء دالة الحفظ.</li>
     * </ul>
     *
     * @return true إذا كانت البيانات صحيحة، false إذا كان هناك خطأ.
     */
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

            // إنشاء كائن الرحلة
            MyJourney myJourney = new MyJourney();
            myJourney.setTripName(tripName);
            myJourney.setCountry(country);
            myJourney.setTown(town);
            myJourney.setAddress(address);
            myJourney.setDescription(description);

            // إضافة التقييم
            float rating = sliderRating.getValue();
            myJourney.setRating(String.valueOf(rating));

            // تحديد نوع الرحلة من RadioGroup
            int selectedRadioButtonId = rgTripType.getCheckedRadioButtonId();
            if (selectedRadioButtonId == R.id.rbBusiness) {
                myJourney.setType("Business");
            } else if (selectedRadioButtonId == R.id.rbLeisure) {
                myJourney.setType("Leisure");
            } else if (selectedRadioButtonId == R.id.rbFamily) {
                myJourney.setType("Family");
            }

            // حفظ الرحلة
            saveMyJourney(myJourney);
        }

        return isValid;
    }

    /**
     * حفظ الرحلة في:
     * 1- قاعدة البيانات المحلية (Room).
     * 2- Firebase Firestore.
     *
     * @param myJourney كائن الرحلة المراد حفظه.
     */
    private void saveMyJourney(MyJourney myJourney) {

        // الحفظ في Room
        Appdatabase.getdb(this).tripQurery().insert(myJourney);

        // الحفظ في Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("journeys")
                .document(String.valueOf(myJourney.getId()))
                .set(myJourney)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this,
                            "Trip saved successfully",
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,
                            "Error saving trip: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    /**
     * حفظ مستخدم في Firebase Realtime Database.
     *
     * @param user كائن المستخدم المراد حفظه.
     */
    private void saveUser(MyUser user) {

        DatabaseReference database =
                FirebaseDatabase.getInstance().getReference("Users");

        String userId = database.push().getKey();

        if (userId != null) {

            database.child(userId)
                    .setValue(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this,
                                "User saved successfully",
                                Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this,
                                "Error saving user: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    });
        }
    }
}