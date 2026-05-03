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

public class Activiydetails extends AppCompatActivity {

    private ImageView imgPlace;
    private TextView txtTitle, txtDescription;
    private Button btnClose, btnNavigate;

    // متغيرات لتخزين الإحداثيات
    private double latitude = 0;
    private double longitude = 0;
    private MyJourney myJourney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiydetails);

        // 1. ربط العناصر بالواجهة
        initViews();

        // 2. استقبال البيانات وعرضها
        handleIntentData();

        // 3. زر العودة
        btnClose.setOnClickListener(v -> finish());



        // 4. زر تشغيل الـ GPS والخرائط
        btnNavigate.setOnClickListener(v -> {
            if(myJourney==null)
                return;
            if (myJourney.getLang() != 0 && myJourney.getLat() != 0) {
                // إنشاء رابط لفتح خرائط جوجل للتوجه (Navigation)
                String uri = "google.navigation:q=" + myJourney.getLang() + "," + myJourney.getLat();
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");

                // التأكد من وجود تطبيق الخرائط
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    // إذا لم يتوفر التطبيق، افتح الخريطة في المتصفح
                    String url = "https://www.google.com/maps/dir/?api=1&destination=" + latitude + "," + longitude;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            } else {
                Toast.makeText(this, "Location coordinates not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        imgPlace = findViewById(R.id.img_place);
        txtTitle = findViewById(R.id.txt_title);
        txtDescription = findViewById(R.id.txt_description);
        btnClose = findViewById(R.id.btn_close);
        btnNavigate = findViewById(R.id.btn_navigate);
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        if (intent != null) {

        // استخراج الحدث الذي تم إرساله من صفحة الرحلات
         myJourney = (MyJourney) getIntent().getSerializableExtra("jr");
        txtTitle.setText(myJourney.getName());
        txtDescription.setText(myJourney.getDescription());
        imgPlace.setImageBitmap(decodeBase64ToBitmap(myJourney.getImage()));


        }
    }

    // دالة لتحويل الصورة من نص Base64 إلى Bitmap
    private Bitmap decodeBase64ToBitmap(String base64Str) {
        try {
            byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            return null;
        }
    }
}
