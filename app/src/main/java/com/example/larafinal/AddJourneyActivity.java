package com.example.larafinal;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.larafinal.data.Appdatabase;
import com.example.larafinal.data.MyUserTable.MyUser;
import com.example.larafinal.data.triptable.MyJourney;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


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

    private ImageView ivSelectedImage; //صفة كمؤشر لهذا الكائن
    private Uri selectedImageUri;//صفة لحفظ عنوان الصورة بعد اختيارها
    private ActivityResultLauncher<String> pickImage;// ‏كائن لطلب الصورة من الهاتف

    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

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
        requestReadMediaImagesPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Toast.makeText(this, "تم منح إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Toast.makeText(this, "تم رفض إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
        requestReadMediaVideoPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Toast.makeText(this, "تم منح إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Toast.makeText(this, "تم رفض إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Toast.makeText(this, "تم منح إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Toast.makeText(this, "تم رفض إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
        checkAndRequestPermissions();
//استدعاء دالة الفحص (سيتم تطبيقها لاحقا
    }

    // دالة لفحص وطلب الأذونات
    private void checkAndRequestPermissions() {
        // فحص وطلب إذن READ_MEDIA_IMAGES (للإصدارات الحديثة)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // أندرويد 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                Toast.makeText(this, "إذن قراءة الصور ممنوح بالفعل", Toast.LENGTH_SHORT).show();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // أندرويد 10 و 11 و 12// على هذه الإصدارات، READ_EXTERNAL_STORAGE له سلوك مختلف
            // إذا كنت تستخدم Scoped Storage بشكل صحيح، قد لا تحتاج إلى هذا الإذن
            // ولكن إذا كنت تحتاج إلى الوصول إلى جميع الصور، فقد تحتاج إلى READ_EXTERNAL_STORAGE
            // في هذا المثال، سنفحص READ_EXTERNAL_STORAGE للإصدارات الأقدم من 13
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                Toast.makeText(this, "إذن قراءة التخزين ممنوح بالفعل (للإصدارات الأقدم)", Toast.LENGTH_SHORT).show();
            }
        } else { // أندرويد 9 والإصدارات الأقدم
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                Toast.makeText(this, "إذن قراءة التخزين ممنوح بالفعل (للإصدارات الأقدم)", Toast.LENGTH_SHORT).show();
            }
        }


        // فحص وطلب إذن READ_MEDIA_VIDEO (للإصدارات الحديثة)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // أندرويد 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaVideoPermission.launch(android.Manifest.permission.READ_MEDIA_VIDEO);
            } else {
                Toast.makeText(this, "إذن قراءة الفيديو ممنوح بالفعل", Toast.LENGTH_SHORT).show();
            }
        }
        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    selectedImageUri = result;
                    ivSelectedImage.setImageURI(result);
                    ivSelectedImage.setVisibility(View.VISIBLE);
                }
            }


        });



        ivSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage.launch("image/*"); // Launch the image picker
            }
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
            if (selectedImageUri!=null)
                myJourney.setImage(convertImageToString(selectedImageUri));

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

            // تهيئة Firebase Realtime Database    //مؤشر لقاعدة البيانات
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
// ‏مؤشر لجدول المستعملين
            DatabaseReference usersRef = database.child("Journeys");
            // إنشاء مفتاح فريد للمستخدم الجديد
            DatabaseReference newUserRef = usersRef.push();
            // تعيين معرف المستخدم في كائن MyUser
        myJourney.setKey(newUserRef.getKey());
            // حفظ بيانات المستخدم في قاعدة البيانات
            //اضافة كائن "لمجموعة" المستعملين ومعالج حدث لفحص نجاح المطلوب
          //  معالج حدث لفحص هل تم المطلوب من قاعدة البيانات //
            newUserRef.setValue(myJourney).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddJourneyActivity.this, "FB Task added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddJourneyActivity.this, "FB Failed to add task", Toast.LENGTH_SHORT).show();
                    }
                }


            });


    }


    /**
     * Converts an image Uri to a Base64 string.
     *
     * @param uri The Uri of the image to convert.
     * @return The Base64 string representation of the image.
     */
    public String convertImageToString(Uri uri) {
        InputStream inputStream = null;
        String imageString = null;
        // تحتوي هذه الدالة على وظيفة تحويل الصورة من مكان التخزين المؤقت إلى نص بنموذج Base64 ليتم تخزينه في قاعدة البيانات، وهذا يتيح للبرنامج عرض الصورة من قاعدة البيانات في وقت لاحق بدون الحاجة إلى فتح الصورة من جهاز المستخدم.
        try {
            inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show();
                return null;
            }
            // Compress image to keep Base64 string within reasonable limit
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return imageString;
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Failed file not found", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }



}