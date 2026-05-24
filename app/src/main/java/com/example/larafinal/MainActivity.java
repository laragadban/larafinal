package com.example.larafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.larafinal.data.triptable.MyJourney;
import com.example.larafinal.data.triptable.MyJourneyAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * الشاشة الرئيسية للتطبيق
 * تعرض جميع الرحلات القادمة من Firebase وتحتوي على البحث والتنقل بين الصفحات
 */
public class MainActivity extends AppCompatActivity {

    // --- عناصر الواجهة الرسومية (UI Elements) ---
    private Spinner spinnerCon;        // قائمة لاختيار القارات
    private SearchView search;         // شريط البحث
    private ListView countryListView;  // القائمة التي تعرض الرحلات
    private MyJourneyAdapter adapter;  //   يربط البيانات بالتصميم
    private FloatingActionButton fabAdd; // زر الإضافة العائم (+)
    private MaterialButton btnfavorite; // زر الانتقال للمفضلات

    // --- قوائم البيانات  ---
    private List<MyJourney> allJourneysList = new ArrayList<>(); // تخزن كل البيانات القادمة من Firebase
    private List<MyJourney> displayList = new ArrayList<>();     // تخزن البيانات التي تظهر حالياً (بعد الفلترة)
    private HashMap<String, ArrayList<String>> mapCountries;    // خريطة لتنظيم القارات ودولها

    private DatabaseReference myRef;           // مرجع يشير إلى جدول "Journeys" في السحابة
    private ValueEventListener journeysListener; // مستمع لمراقبة التغييرات اللحظية في البيانات

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); // ربط الكود بملف التصميم

        // 1. ربط العناصر البرمجية بالـ ID الموجود في XML
        search = findViewById(R.id.search);
        countryListView = findViewById(R.id.countryListView);
        fabAdd = findViewById(R.id.fabAdd);
        btnfavorite = findViewById(R.id.btnfavorite);
        spinnerCon = findViewById(R.id.spinnerCon);

        // 2. إعداد زر المفضلات للانتقال إلى شاشة TripSchedule
        btnfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TripSchedule.class);
                startActivity(intent);
            }
        });

        // 3. تهيئة الـ Adapter وربطه بالـ ListView
        adapter = new MyJourneyAdapter(
                this,
                R.layout.journey_item_layout, // شكل العنصر الواحد
                displayList                  // القائمة التي سيتم عرضها
        );
        countryListView.setAdapter(adapter);

        // 4. برمجة مستمع البحث: يتم تنفيذه عند الكتابة في خانة البحث
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query); // تنفيذ الفلترة عند الضغط على زر البحث
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText); // تحديث النتائج فوراً أثناء الكتابة
                return true;
            }
        });

        // 5. إعداد زر الإضافة للانتقال إلى شاشة AddJourneyActivity
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddJourneyActivity.class);
                startActivity(intent);
            });
        }

        // 6. تشغيل دوال تجهيز البيانات
        setupContinentSpinner(); // تعبئة قائمة القارات
        getAllFromFirebase();    // البدء بجلب الرحلات من الإنترنت
    }

    /**
     * دالة لتصفية البيانات بناءً على النص المدخل في البحث
     */
    private void filterData(String text) {
        displayList.clear(); // مسح القائمة الحالية

        if (text == null || text.trim().isEmpty()) {
            // إذا كان البحث فارغاً، نعرض كل الرحلات
            displayList.addAll(allJourneysList);
        } else {
            String query = text.toLowerCase().trim();
            // البحث داخل القائمة الأصلية عن تطابق في الاسم أو الدولة
            for (MyJourney item : allJourneysList) {
                String country = (item.getCountry() != null) ? item.getCountry().toLowerCase() : "";
                String name = (item.getTripName() != null) ? item.getTripName().toLowerCase() : "";

                if (country.contains(query) || name.contains(query)) {
                    displayList.add(item); // إضافة العنصر المطابق للقائمة
                }
            }
        }
        adapter.notifyDataSetChanged(); // إبلاغ القائمة لتحديث شكلها فوراً
    }

    /**
     * دالة لجلب البيانات من Firebase Realtime Database
     */
    private void getAllFromFirebase() {
        if (myRef == null) {
            // الوصول إلى عقدة Journeys في قاعدة البيانات
            myRef = FirebaseDatabase.getInstance().getReference("Journeys");
        }

        // إزالة المستمع القديم (إن وجد) لمنع تكرار البيانات وتوفير الذاكرة
        if (journeysListener != null) {
            myRef.removeEventListener(journeysListener);
        }

        // إنشاء مستمع يراقب أي تغيير يحدث في السحابة
        journeysListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allJourneysList.clear(); // مسح البيانات القديمة
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    // تحويل البيانات من Firebase إلى كائن MyJourney
                    MyJourney task = taskSnapshot.getValue(MyJourney.class);
                    if (task != null) {
                        allJourneysList.add(task);
                    }
                }
                // تحديث العرض بناءً على كلمة البحث الحالية (إن وجدت)
                filterData(search.getQuery().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // عرض رسالة خطأ في حال فشل الاتصال بالإنترنت
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        // تفعيل المستمع للبدء بمراقبة البيانات
        myRef.addValueEventListener(journeysListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // إعادة التأكد من جلب البيانات عند العودة لهذه الشاشة
        getAllFromFirebase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // خطوة أمان: قطع الاتصال بـ Firebase عند إغلاق التطبيق نهائياً لتوفير موارد الهاتف
        if (myRef != null && journeysListener != null) {
            myRef.removeEventListener(journeysListener);
        }
    }

    /**
     * دالة لتجهيز قائمة القارات والدول (Spinner)
     */
    private void setupContinentSpinner() {
        mapCountries = new HashMap<>();

        // قارة آسيا
        ArrayList<String> asia = new ArrayList<>();
        asia.add("Saudi Arabia"); asia.add("UAE"); asia.add("Japan");
        mapCountries.put("Asia", asia);

        // قارة أوروبا
        ArrayList<String> europe = new ArrayList<>();
        europe.add("France"); europe.add("Germany");
        mapCountries.put("Europe", europe);

        // قارة أفريقيا
        ArrayList<String> africa = new ArrayList<>();
        africa.add("Egypt"); africa.add("Nigeria");
        mapCountries.put("Africa", africa);

        // ربط البيانات بالـ Spinner باستخدام ArrayAdapter بسيط
        ArrayAdapter<String> continentAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>(mapCountries.keySet())
        );
        continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCon.setAdapter(continentAdapter);
    }
}