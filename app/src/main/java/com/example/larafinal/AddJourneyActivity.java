package com.example.larafinal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.larafinal.data.triptable.MyJourney;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * شاشة إضافة رحلة جديدة
 *
 * تسمح للمستخدم بـ:
 * - إدخال بيانات الرحلة
 * - اختيار صورة
 * - تحديد الموقع الجغرافي GPS
 * - حفظ الرحلة في Firebase
 */
public class AddJourneyActivity extends AppCompatActivity {

    // ================== عناصر الواجهة ==================

    /**
     * حقول إدخال بيانات الرحلة
     */
    private TextInputEditText etTripName, etCountry,
            etTown, etAddress,
            etDescription,
            etLatitude, etLongitude;

    /**
     * مجموعة أنواع الرحلات
     */
    private RadioGroup rgTripType;

    /**
     * شريط التقييم
     */
    private Slider sliderRating;

    /**
     * الأزرار
     */
    private Button btnSaveTrip, btnGetLocation;

    /**
     * صورة الرحلة
     */
    private ImageView ivSelectedImage;

    /**
     * رابط الصورة المختارة
     */
    private Uri selectedImageUri;

    /**
     * Launcher لاختيار صورة
     */
    private ActivityResultLauncher<String> pickImage;

    /**
     * Launcher لصلاحية الموقع
     */
    private ActivityResultLauncher<String> requestLocationPermission;

    /**
     * Launcher لصلاحية الصور في Android 13+
     */
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;

    /**
     * Launcher لصلاحية التخزين للإصدارات القديمة
     */
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    /**
     * خدمة تحديد الموقع
     */
    private FusedLocationProviderClient fusedLocationClient;

    /**
     * الدالة الرئيسية عند تشغيل الصفحة
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // ربط ملف التصميم
        setContentView(R.layout.activity_add);

        /**
         * تهيئة خدمة الموقع
         */
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        /**
         * ربط عناصر الواجهة
         */
        initViews();

        /**
         * تهيئة الـ Launchers
         */
        initPermissionLaunchers();

        /**
         * طلب صلاحيات الصور
         */
        checkAndRequestMediaPermissions();

        /**
         * زر حفظ الرحلة
         */
        btnSaveTrip.setOnClickListener(v -> validateFields());

        /**
         * زر تحديد الموقع
         */
        btnGetLocation.setOnClickListener(v -> checkLocationPermission());

        /**
         * عند الضغط على الصورة يتم فتح المعرض
         */
        ivSelectedImage.setOnClickListener(
                v -> pickImage.launch("image/*"));
    }

    /**
     * ربط عناصر الواجهة الرسومية مع الكود
     */
    private void initViews() {

        etTripName = findViewById(R.id.etTripName);

        etCountry = findViewById(R.id.etcountry);

        etTown = findViewById(R.id.etTown);

        etAddress = findViewById(R.id.etAddress);

        etDescription = findViewById(R.id.etDescription);

        etLatitude = findViewById(R.id.etLatitude);

        etLongitude = findViewById(R.id.etLongitude);

        rgTripType = findViewById(R.id.rgTripType);

        sliderRating = findViewById(R.id.sliderRating);

        btnSaveTrip = findViewById(R.id.btnSaveTrip);

        btnGetLocation = findViewById(R.id.btnGetLocation);

        ivSelectedImage = findViewById(R.id.ivTripImage);
    }

    /**
     * تهيئة Launchers الخاصة بالصلاحيات والصور
     */
    private void initPermissionLaunchers() {

        /**
         * اختيار صورة من المعرض
         */
        pickImage =
                registerForActivityResult(
                        new ActivityResultContracts.GetContent(),
                        result -> {

                            if (result != null) {

                                selectedImageUri = result;

                                ivSelectedImage.setImageURI(result);

                                ivSelectedImage.setVisibility(View.VISIBLE);
                            }
                        });

        /**
         * طلب صلاحية الموقع
         */
        requestLocationPermission =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {

                            if (isGranted) {

                                getCurrentLocation();

                            } else {

                                Toast.makeText(
                                        this,
                                        "Location permission denied",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });

        /**
         * طلب صلاحية الصور Android 13+
         */
        requestReadMediaImagesPermission =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {

                            if (!isGranted)

                                Toast.makeText(
                                        this,
                                        "Image permission denied",
                                        Toast.LENGTH_SHORT
                                ).show();
                        });

        /**
         * طلب صلاحية التخزين للإصدارات القديمة
         */
        requestReadExternalStoragePermission =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {

                            if (!isGranted)

                                Toast.makeText(
                                        this,
                                        "Storage permission denied",
                                        Toast.LENGTH_SHORT
                                ).show();
                        });
    }

    /**
     * فحص وطلب صلاحيات الصور حسب إصدار الأندرويد
     */
    private void checkAndRequestMediaPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES)

                    != PackageManager.PERMISSION_GRANTED) {

                requestReadMediaImagesPermission.launch(
                        Manifest.permission.READ_MEDIA_IMAGES);
            }

        } else {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)

                    != PackageManager.PERMISSION_GRANTED) {

                requestReadExternalStoragePermission.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    /**
     * فحص صلاحية الموقع
     */
    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)

                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();

        } else {

            requestLocationPermission.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    /**
     * جلب الموقع الحالي باستخدام GPS
     */
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)

                != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        /**
         * جلب الموقع الحالي بدقة عالية
         */
        fusedLocationClient
                .getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        null)

                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        etLatitude.setText(
                                String.valueOf(location.getLatitude()));

                        etLongitude.setText(
                                String.valueOf(location.getLongitude()));

                        Toast.makeText(
                                AddJourneyActivity.this,
                                "Location detected",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                AddJourneyActivity.this,
                                "Unable to find location. Please ensure GPS is ON.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    /**
     * فحص الحقول والتأكد من صحة البيانات
     */
    private void validateFields() {

        String tripName =
                etTripName.getText().toString().trim();

        String country =
                etCountry.getText().toString().trim();

        /**
         * فحص اسم الرحلة
         */
        if (tripName.isEmpty()) {

            etTripName.setError("Required");

            return;
        }

        /**
         * فحص الدولة
         */
        if (country.isEmpty()) {

            etCountry.setError("Required");

            return;
        }

        /**
         * إنشاء كائن رحلة جديد
         */
        MyJourney myJourney = new MyJourney();

        /**
         * تعبئة بيانات الرحلة
         */
        myJourney.setTripName(tripName);

        myJourney.setCountry(country);

        myJourney.setTown(
                etTown.getText().toString().trim());

        myJourney.setAddress(
                etAddress.getText().toString().trim());

        myJourney.setDescription(
                etDescription.getText().toString().trim());

        /**
         * تحويل الإحداثيات من نص إلى Double
         */
        try {

            String latStr =
                    etLatitude.getText().toString().trim();

            String lonStr =
                    etLongitude.getText().toString().trim();

            if (!latStr.isEmpty())

                myJourney.setLat(
                        Double.parseDouble(latStr));

            if (!lonStr.isEmpty())

                myJourney.setLang(
                        Double.parseDouble(lonStr));

        } catch (Exception e) {

            Log.e("AddJourney",
                    "Error parsing coordinates",
                    e);
        }

        /**
         * تحويل الصورة إلى Base64
         */
        if (selectedImageUri != null)

            myJourney.setImage(
                    convertImageToString(selectedImageUri));

        /**
         * تعيين التقييم
         */
        myJourney.setRating(
                String.valueOf(sliderRating.getValue()));

        /**
         * تحديد نوع الرحلة
         */
        int selectedId =
                rgTripType.getCheckedRadioButtonId();

        if (selectedId == R.id.rbBusiness)

            myJourney.setType("Business");

        else if (selectedId == R.id.rbLeisure)

            myJourney.setType("Leisure");

        else if (selectedId == R.id.rbFamily)

            myJourney.setType("Family");

        /**
         * حفظ الرحلة
         */
        saveMyJourney(myJourney);
    }

    /**
     * حفظ الرحلة داخل Firebase
     *
     * @param myJourney بيانات الرحلة
     */
    private void saveMyJourney(MyJourney myJourney) {

        DatabaseReference database =
                FirebaseDatabase.getInstance().getReference();

        DatabaseReference journeysRef =
                database.child("Journeys");

        DatabaseReference newJourneyRef =
                journeysRef.push();

        /**
         * حفظ المفتاح داخل الكائن
         */
        myJourney.setKey(newJourneyRef.getKey());

        /**
         * رفع البيانات إلى Firebase
         */
        newJourneyRef.setValue(myJourney)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                AddJourneyActivity.this,
                                "Journey saved successfully!",
                                Toast.LENGTH_SHORT
                        ).show();

                        // إغلاق الصفحة
                        finish();

                    } else {

                        Toast.makeText(
                                AddJourneyActivity.this,
                                "Failed to save journey",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    /**
     * تحويل الصورة إلى نص Base64
     *
     * @param uri رابط الصورة
     * @return نص مشفر للصورة
     */
    public String convertImageToString(Uri uri) {

        try {

            InputStream inputStream =
                    getContentResolver().openInputStream(uri);

            Bitmap bitmap =
                    BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            /**
             * ضغط الصورة لتقليل الحجم
             */
            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    40,
                    outputStream
            );

            return Base64.encodeToString(
                    outputStream.toByteArray(),
                    Base64.DEFAULT
            );

        } catch (FileNotFoundException e) {

            return null;
        }
    }
}