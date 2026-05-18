package com.example.larafinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.larafinal.data.triptable.MyJourney;

/**
 * شاشة عرض تفاصيل الرحلة
 *
 * تعرض:
 * - صورة الرحلة
 * - اسم الرحلة
 * - وصف الرحلة
 *
 * وتسمح للمستخدم بفتح خرائط جوجل للتنقل إلى الموقع.
 */
public class Activiydetails extends AppCompatActivity {

    /**
     * عناصر الواجهة الرسومية
     */
    private ImageView imgPlace;
    private TextView txtTitle, txtDescription;
    private Button btnClose, btnNavigate;

    /**
     * متغيرات لتخزين الإحداثيات
     */
    private double latitude = 0;
    private double longitude = 0;

    /**
     * كائن الرحلة الحالي
     */
    private MyJourney myJourney;

    /**
     * الدالة الرئيسية عند فتح الصفحة
     *
     * @param savedInstanceState بيانات الحالة السابقة
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // ربط ملف التصميم بالصفحة
        setContentView(R.layout.activity_activiydetails);

        /**
         * 1. ربط عناصر الواجهة
         */
        initViews();

        /**
         * 2. استقبال البيانات من الصفحة السابقة
         */
        handleIntentData();

        /**
         * 3. زر الإغلاق والعودة
         */
        btnClose.setOnClickListener(v -> finish());

        /**
         * 4. زر فتح الخرائط والتنقل
         */
        btnNavigate.setOnClickListener(v -> {

            // فحص إذا كانت بيانات الرحلة موجودة
            if(myJourney==null)
                return;

            /**
             * فحص إذا كانت الإحداثيات موجودة
             */
            if (myJourney.getLang() != 0 && myJourney.getLat() != 0) {

                // إنشاء رابط التنقل باستخدام Google Maps
                String uri = "google.navigation:q="
                        + myJourney.getLang()
                        + ","
                        + myJourney.getLat();

                Intent mapIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                // تحديد تطبيق خرائط جوجل
                mapIntent.setPackage("com.google.android.apps.maps");

                /**
                 * التأكد من وجود تطبيق الخرائط
                 */
                if (mapIntent.resolveActivity(getPackageManager()) != null) {

                    // فتح تطبيق الخرائط
                    startActivity(mapIntent);

                } else {

                    /**
                     * في حال عدم وجود التطبيق
                     * يتم فتح الخريطة في المتصفح
                     */
                    String url =
                            "https://www.google.com/maps/dir/?api=1&destination="
                                    + latitude
                                    + ","
                                    + longitude;

                    Intent browserIntent =
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    startActivity(browserIntent);
                }

            } else {

                // رسالة عند عدم توفر الإحداثيات
                Toast.makeText(
                        this,
                        "Location coordinates not available",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    /**
     * ربط عناصر الواجهة الرسومية مع الكود
     */
    private void initViews() {

        imgPlace = findViewById(R.id.img_place);

        txtTitle = findViewById(R.id.txt_title);

        txtDescription = findViewById(R.id.txt_description);

        btnClose = findViewById(R.id.btn_close);

        btnNavigate = findViewById(R.id.btn_navigate);
    }

    /**
     * استقبال البيانات المرسلة عبر Intent
     * وعرضها داخل الصفحة
     */
    private void handleIntentData() {

        Intent intent = getIntent();

        if (intent != null) {

            // استخراج بيانات الرحلة
            myJourney =
                    (MyJourney) getIntent().getSerializableExtra("jr");

            // عرض اسم الرحلة
            txtTitle.setText(myJourney.getName());

            // عرض وصف الرحلة
            txtDescription.setText(myJourney.getDescription());

            // عرض صورة الرحلة
            imgPlace.setImageBitmap(
                    decodeBase64ToBitmap(myJourney.getImage())
            );
        }
    }

    /**
     * تحويل نص Base64 إلى صورة Bitmap
     *
     * @param base64Str النص المشفر للصورة
     * @return صورة Bitmap
     */
    private Bitmap decodeBase64ToBitmap(String base64Str) {

        try {

            // فك تشفير النص إلى Bytes
            byte[] decodedBytes =
                    Base64.decode(base64Str, Base64.DEFAULT);

            // تحويل الـ Bytes إلى Bitmap
            return BitmapFactory.decodeByteArray(
                    decodedBytes,
                    0,
                    decodedBytes.length
            );

        } catch (Exception e) {

            // في حال حدوث خطأ
            return null;
        }
    }
}
