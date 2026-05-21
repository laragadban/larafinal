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

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCon;
    private SearchView search;
    private ListView countryListView;
    private MyJourneyAdapter adapter;
    private FloatingActionButton fabAdd;
    private MaterialButton btnAiChat;
    private boolean isHeartRed = true;

    // القائمة التي تخزن كل البيانات القادمة من Firebase (المرجع)
    private List<MyJourney> allJourneysList = new ArrayList<>();
    // القائمة التي يتم عرضها فعلياً في الـ ListView وتتأثر بالبحث
    private List<MyJourney> displayList = new ArrayList<>();

    private HashMap<String, ArrayList<String>> mapCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // ربط العناصر
        search = findViewById(R.id.search);
        countryListView = findViewById(R.id.countryListView);
        fabAdd = findViewById(R.id.fabAdd);
        btnAiChat = findViewById(R.id.btnAiChat);
        spinnerCon = findViewById(R.id.spinnerCon);

        // 1. إعداد الـ Adapter وربطه بـ displayList
        adapter = new MyJourneyAdapter(this, R.layout.journey_item_layout, displayList);
        countryListView.setAdapter(adapter);

        // 2. برمجة حقل البحث ليعمل عند كتابة أول حرف
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // الفلترة اللحظية بمجرد كتابة أول حرف
                filterData(newText);
                return true;
            }
        });

        // Heart button click listener - changes color when clicked
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
               isHeartRed = !isHeartRed;
                if (isHeartRed) {
                    fabAdd.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_light));
                } else {
                    fabAdd.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_purple));
                }
            });
        }

        // AI Chat button click listener - navigates to AiChat activity
        if (btnAiChat != null) {
            btnAiChat.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AiChat.class);
                startActivity(intent);
            });
        }

        setupContinentSpinner();
    }

    /**
     * دالة الفلترة الذكية
     */
    private void filterData(String text) {
        displayList.clear(); // مسح القائمة المعروضة حالياً

        if (text == null || text.trim().isEmpty()) {
            // إذا كان حقل البحث فارغاً، نعرض كل البيانات
            displayList.addAll(allJourneysList);
        } else {
            String query = text.toLowerCase().trim();
            for (MyJourney item : allJourneysList) {
                // فحص القيمة (الدولة أو اسم الرحلة)
                String country = (item.getCountry() != null) ? item.getCountry().toLowerCase() : "";
                String name = (item.getTripName() != null) ? item.getTripName().toLowerCase() : "";

                if (country.contains(query) || name.contains(query)) {
                    displayList.add(item);
                }
            }
        }
        // تحديث الـ Adapter ليعكس التغييرات على الشاشة
        adapter.notifyDataSetChanged();
    }

    private void getAllFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Journeys");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allJourneysList.clear();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    MyJourney task = taskSnapshot.getValue(MyJourney.class);
                    if (task != null) {
                        allJourneysList.add(task);
                    }
                }
                // عند تحديث البيانات من Firebase، نحدث القائمة المعروضة فوراً
                filterData(search.getQuery().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFromFirebase();
    }

    private void setupContinentSpinner() {
        mapCountries = new HashMap<>();
        ArrayList<String> asia = new ArrayList<>();
        asia.add("Saudi Arabia"); asia.add("UAE"); asia.add("Japan");
        mapCountries.put("Asia", asia);

        ArrayList<String> europe = new ArrayList<>();
        europe.add("France"); europe.add("Germany");
        mapCountries.put("Europe", europe);

        ArrayList<String> africa = new ArrayList<>();
        africa.add("Egypt"); africa.add("Nigeria");
        mapCountries.put("Africa", africa);

        ArrayAdapter<String> continentAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(mapCountries.keySet()));
        continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCon.setAdapter(continentAdapter);
    }
}