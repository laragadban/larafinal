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
 * تعرض جميع الرحلات القادمة من Firebase
 * وتحتوي على البحث والتنقل بين الصفحات
 */
public class MainActivity extends AppCompatActivity {

    // عناصر الواجهة
    private Spinner spinnerCon;
    private SearchView search;
    private ListView countryListView;
    private MyJourneyAdapter adapter;
    private FloatingActionButton fabAdd;

    // القوائم
    private List<MyJourney> allJourneysList = new ArrayList<>();
    private List<MyJourney> displayList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> mapCountries;

    // Firebase variables to manage the listener
    private DatabaseReference myRef;
    private ValueEventListener journeysListener;
    MaterialButton  btnfavorite ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // ربط عناصر الواجهة
        search = findViewById(R.id.search);
        countryListView = findViewById(R.id.countryListView);
        fabAdd = findViewById(R.id.fabAdd);
        btnfavorite = findViewById(R.id.btnfavorite);
        spinnerCon = findViewById(R.id.spinnerCon);

       btnfavorite.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, TripSchedule.class);
               startActivity(intent);
           }
       });


        // إعداد الـ Adapter
        adapter = new MyJourneyAdapter(
                this,
                R.layout.journey_item_layout,
                displayList
        );
        countryListView.setAdapter(adapter);

        // برمجة البحث
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });

        // زر الإضافة
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddJourneyActivity.class);
                startActivity(intent);
            });
        }

        setupContinentSpinner();
        
        // جلب البيانات لأول مرة
        getAllFromFirebase();
    }

    private void filterData(String text) {
        displayList.clear();
        if (text == null || text.trim().isEmpty()) {
            displayList.addAll(allJourneysList);
        } else {
            String query = text.toLowerCase().trim();
            for (MyJourney item : allJourneysList) {
                String country = (item.getCountry() != null) ? item.getCountry().toLowerCase() : "";
                String name = (item.getTripName() != null) ? item.getTripName().toLowerCase() : "";
                if (country.contains(query) || name.contains(query)) {
                    displayList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void getAllFromFirebase() {
        if (myRef == null) {
            myRef = FirebaseDatabase.getInstance().getReference("Journeys");
        }

        // إزالة المستمع القديم إذا وجد لتجنب التكرار
        if (journeysListener != null) {
            myRef.removeEventListener(journeysListener);
        }

        journeysListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allJourneysList.clear();
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    MyJourney task = taskSnapshot.getValue(MyJourney.class);
                    if (task != null) {
                        allJourneysList.add(task);
                    }
                }
                // تحديث العرض بناءً على البحث الحالي
                filterData(search.getQuery().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        // تفعيل المستمع الجديد
        myRef.addValueEventListener(journeysListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // بما أننا نستخدم addValueEventListener، فهو يعمل تلقائياً
        // إذا أردت التأكد من جلب البيانات عند كل رجوع، يمكنك تركها،
        // ولكن الكود الجديد في getAllFromFirebase يمنع التكرار.
        getAllFromFirebase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // تنظيف المستمع عند إغلاق التطبيق
        if (myRef != null && journeysListener != null) {
            myRef.removeEventListener(journeysListener);
        }
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

        ArrayAdapter<String> continentAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>(mapCountries.keySet())
        );
        continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCon.setAdapter(continentAdapter);
    }
}
