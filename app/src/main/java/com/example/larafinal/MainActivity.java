package com.example.larafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * الشاشة الرئيسية للتطبيق
 * تعرض جميع الرحلات القادمة من Firebase
 * وتحتوي على البحث والتنقل بين الصفحات
 */
public class MainActivity extends AppCompatActivity {

    // Spinner لاختيار القارة
    private Spinner spinnerCon;

    // حقل البحث
    private SearchView search;

    // ListView لعرض الرحلات
    private ListView countryListView;

    // Adapter المسؤول عن ربط البيانات بالـ ListView
    private MyJourneyAdapter adapter;

    // زر الإضافة (+)
    private FloatingActionButton fabAdd;

    // زر الذكاء الاصطناعي
    private MaterialButton btnAiChat;

    /**
     * قائمة تحتوي جميع الرحلات القادمة من Firebase
     */
    private List<MyJourney> allJourneysList = new ArrayList<>();

    /**
     * القائمة التي يتم عرضها فعلياً داخل ListView
     * وتتغير حسب البحث
     */
    private List<MyJourney> displayList = new ArrayList<>();

    /**
     * HashMap لتخزين القارات والدول التابعة لها
     */
    private HashMap<String, ArrayList<String>> mapCountries;

    /**
     * يتم استدعاؤها عند فتح الشاشة
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ربط الشاشة بملف XML
        setContentView(R.layout.main_activity);

        /**
         * ربط عناصر الواجهة مع الـ XML
         */
        search = findViewById(R.id.search);
        countryListView = findViewById(R.id.countryListView);
        fabAdd = findViewById(R.id.fabAdd);
        btnAiChat = findViewById(R.id.btnAiChat);
        spinnerCon = findViewById(R.id.spinnerCon);

        /**
         * إنشاء Adapter وربطه مع القائمة المعروضة
         */
        adapter = new MyJourneyAdapter(
                this,
                R.layout.journey_item_layout,
                displayList
        );

        // ربط الـ Adapter بالـ ListView
        countryListView.setAdapter(adapter);

        /**
         * برمجة البحث
         * يتم تنفيذ الفلترة مباشرة أثناء الكتابة
         */
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             * عند الضغط على زر البحث
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                return false;
            }

            /**
             * عند تغيير النص داخل البحث
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });

        /**
         * عند الضغط على زر الإضافة (+)
         * الانتقال إلى شاشة AddJourney
         */
        if (fabAdd != null) {

            fabAdd.setOnClickListener(v -> {

                Intent intent = new Intent(
                        MainActivity.this,
                        AddJourneyActivity.class
                );

                startActivity(intent);
            });
        }

        /**
         * عند الضغط على زر الذكاء الاصطناعي
         * الانتقال إلى شاشة AiChat
         */
        if (btnAiChat != null) {

            btnAiChat.setOnClickListener(v -> {

                Intent intent = new Intent(
                        MainActivity.this,
                        AiChat.class
                );

                startActivity(intent);
            });
        }

        // تجهيز Spinner القارات
        setupContinentSpinner();
    }

    /**
     * دالة فلترة البيانات حسب البحث
     *
     * @param text النص الذي يكتبه المستخدم
     */
    private void filterData(String text) {

        // تنظيف القائمة الحالية
        displayList.clear();

        /**
         * إذا كان البحث فارغاً
         * يتم عرض جميع الرحلات
         */
        if (text == null || text.trim().isEmpty()) {

            displayList.addAll(allJourneysList);

        } else {

            // تحويل النص إلى حروف صغيرة للمقارنة
            String query = text.toLowerCase().trim();

            /**
             * المرور على جميع الرحلات
             */
            for (MyJourney item : allJourneysList) {

                // الحصول على الدولة
                String country = (item.getCountry() != null)
                        ? item.getCountry().toLowerCase()
                        : "";

                // الحصول على اسم الرحلة
                String name = (item.getTripName() != null)
                        ? item.getTripName().toLowerCase()
                        : "";

                /**
                 * إذا كان اسم الدولة أو الرحلة يحتوي النص
                 */
                if (country.contains(query) || name.contains(query)) {

                    // إضافة العنصر للقائمة المعروضة
                    displayList.add(item);
                }
            }
        }

        // تحديث البيانات داخل ListView
        adapter.notifyDataSetChanged();
    }

    /**
     * جلب جميع الرحلات من Firebase
     */
    private void getAllFromFirebase() {

        // الاتصال بقاعدة البيانات
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // المرجع الخاص بالرحلات
        DatabaseReference myRef = database.getReference("Journeys");

        /**
         * مراقبة البيانات داخل Firebase
         */
        myRef.addValueEventListener(new ValueEventListener() {

            /**
             * عند وصول البيانات بنجاح
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // تنظيف القائمة القديمة
                allJourneysList.clear();

                /**
                 * المرور على جميع البيانات القادمة
                 */
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {

                    // تحويل البيانات إلى كائن MyJourney
                    MyJourney task = taskSnapshot.getValue(MyJourney.class);

                    // التأكد أن البيانات ليست فارغة
                    if (task != null) {

                        // إضافة الرحلة للقائمة
                        allJourneysList.add(task);
                    }
                }

                /**
                 * تحديث البيانات المعروضة
                 * مع المحافظة على البحث الحالي
                 */
                filterData(search.getQuery().toString());
            }

            /**
             * في حال حدوث خطأ أثناء الاتصال
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(
                        MainActivity.this,
                        "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    /**
     * يتم استدعاؤها عند الرجوع للشاشة
     */
    @Override
    protected void onResume() {
        super.onResume();

        // تحديث البيانات من Firebase
        getAllFromFirebase();
    }

    /**
     * تجهيز Spinner القارات والدول
     */
    private void setupContinentSpinner() {

        // إنشاء HashMap
        mapCountries = new HashMap<>();

        /**
         * قارة آسيا
         */
        ArrayList<String> asia = new ArrayList<>();
        asia.add("Saudi Arabia");
        asia.add("UAE");
        asia.add("Japan");

        mapCountries.put("Asia", asia);

        /**
         * قارة أوروبا
         */
        ArrayList<String> europe = new ArrayList<>();
        europe.add("France");
        europe.add("Germany");

        mapCountries.put("Europe", europe);

        /**
         * قارة أفريقيا
         */
        ArrayList<String> africa = new ArrayList<>();
        africa.add("Egypt");
        africa.add("Nigeria");

        mapCountries.put("Africa", africa);

        /**
         * إنشاء Adapter للـ Spinner
         */
        ArrayAdapter<String> continentAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>(mapCountries.keySet())
        );

        // تصميم القائمة المنسدلة
        continentAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        // ربط الـ Spinner بالـ Adapter
        spinnerCon.setAdapter(continentAdapter);
    }
}