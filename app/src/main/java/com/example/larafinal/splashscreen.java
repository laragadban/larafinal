package com.example.larafinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splashscreen extends AppCompatActivity {

    @Override
/**
 * onCreate - شاشة البداية (Splash Screen)
 *
 * السطر بسطر:
 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // استدعاء onCreate من الكلاس الأب لإعداد النشاط الأساسي

        EdgeToEdge.enable(this);
        // تفعيل وضع Edge-to-Edge لعرض المحتوى بكامل الشاشة

        setContentView(R.layout.splashscreen);
        // ربط واجهة المستخدم بملف XML الخاص بالشاشة

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnlogin1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // الحصول على أبعاد شريط الحالة وشريط التنقل

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // ضبط الهوامش للـ View لتجنب تداخل المحتوى مع شريط النظام

            return insets;
            // إعادة الـ Insets للمعالجة الافتراضية
        });

        /**
         * إنشاء Thread منفصل لإضافة تأخير زمني (3 ثواني)
         * قبل الانتقال إلى شاشة تسجيل الدخول.
         */
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // العودة إلى الـ UI Thread لتنفيذ أوامر الواجهة
            runOnUiThread(() -> {
                // إنشاء Intent للانتقال إلى شاشة تسجيل الدخول
                Intent intent = new Intent(splashscreen.this, loginsc.class);
                startActivity(intent);
            });
        }).start();
    }
}
