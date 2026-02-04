package com.example.larafinal;

import static com.example.larafinal.data.Appdatabase.db;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.larafinal.data.triptable.MyJourneyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView tvdes;
    private Spinner spinnerCon;
    private TextView tvcou;
    private SearchView search;
    private ListView countryListView;
    private MyJourneyAdapter adapter; // الـ Adapter المخصص
    private FloatingActionButton fabAdd;

    private HashMap<String, ArrayList<String>> mapCountries;
    private String selectedContinent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // 1. ربط العناصر
        tvdes = findViewById(R.id.tvdes);
        spinnerCon = findViewById(R.id.spinnerCon);
        tvcou = findViewById(R.id.tvcou);
        search = findViewById(R.id.search);
        countryListView = findViewById(R.id.countryListView);
        fabAdd = findViewById(R.id.fabAdd); // تأكد أن هذا الـ ID موجود في الـ XML

        // 2. إعداد الـ Custom Adapter الخاص بالرحلات
        adapter = new MyJourneyAdapter(this, R.layout.journey_item_layout);
        countryListView.setAdapter(adapter);

        // 3. زر الانتقال لشاشة الإضافة
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddJourneyActivity.class);
                startActivity(intent);
            });
        }

        // 4. تجهيز بيانات القارات (للعرض فقط)
        setupContinentSpinner();
    }

    private void setupContinentSpinner() {
        mapCountries = new HashMap<>();
        ArrayList<String> asia = new ArrayList<>();
        asia.add("Saudi Arabia"); asia.add("UAE"); asia.add("Japan");
        mapCountries.put("Asia", asia);

        ArrayList<String> europe = new ArrayList<>();
        europe.add("France"); europe.add("Germany");
        mapCountries.put("Europe", europe);

        ArrayAdapter<String> continentAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, new ArrayList<>(mapCountries.keySet())
        );
        continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCon.setAdapter(continentAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // تحديث القائمة بالبيانات من قاعدة البيانات (Custom Adapter)
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        if (db != null && db.tripQurery() != null) {
            // تنظيف القائمة القديمة
            adapter.clear();

            // جلب البيانات الجديدة من الـ DAO وإضافتها للـ Adapter المخصص
            // تأكد أن getAll() تعيد List<MyJourney>
            adapter.addAll(db.tripQurery().getAll());

            // إخطار القائمة بالتغيير
            adapter.notifyDataSetChanged();
        }
    }
}
















