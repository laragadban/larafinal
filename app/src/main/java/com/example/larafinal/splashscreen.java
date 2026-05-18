package com.example.larafinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splashscreen extends AppCompatActivity {

    @Override
    // تنفذ مرة واحدة عند بدء التطبيق
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. تفعيل وضع ملء الشاشة
        EdgeToEdge.enable(this);
        setContentView(R.layout.splashscreen);

        // 2. تصحيح المستمع: نستخدم android.R.id.content للوصول للجذر (Root)
        // لضمان عدم حدوث إيرور إذا كان ID الـ Layout مختلفاً
        View mainView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 3. الطريقة الأفضل والآمنة للتأخير في أندرويد (Handler)
        // بدلاً من Thread يدوي لضمان استقرار التطبيق
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // الانتقال للشاشة التالية
            Intent intent = new Intent(splashscreen.this, loginsc.class);
            startActivity(intent);

            // 4. إغلاق هذه الشاشة نهائياً (مهم جداً!)
            finish();
        }, 3000); // تأخير 3 ثواني
    }
}
