package com.example.larafinal;
import static com.example.larafinal.data.Appdatabase.db;

import androidx.appcompat.app.AppCompatActivity;

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
    private MyJourneyAdapter adapter;
   private FloatingActionButton fabAdd;

    // قارات + الدول
    private HashMap<String, ArrayList<String>> mapCountries;
    private String selectedContinent = "";
    private String selectedCountry = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);// غيّر الاسم حسب ملف XML

        tvdes = findViewById(R.id.tvdes);
        spinnerCon = findViewById(R.id.spinnerCon);
        tvcou = findViewById(R.id.tvcou);
        search = findViewById(R.id.search);
        fabAdd=
        countryListView = findViewById(R.id.countryListView);
        adapter = new MyJourneyAdapter(this, R.layout.journey_item_layout);
        countryListView.setAdapter(adapter);


        // --------- ربط العناصر ---------
        spinnerCon = findViewById(R.id.spinnerCon);
        search = findViewById(R.id.search);

        // --------- تجهيز البيانات ---------
        mapCountries = new HashMap<>();

        ArrayList<String> asia = new ArrayList<>();
        asia.add("Saudi Arabia");
        asia.add("United Arab Emirates");
        asia.add("Japan");
        mapCountries.put("Asia", asia);

        ArrayList<String> africa = new ArrayList<>();
        africa.add("Egypt");
        africa.add("Morocco");
        africa.add("South Africa");
        mapCountries.put("Africa", africa);

        ArrayList<String> europe = new ArrayList<>();
        europe.add("France");
        europe.add("Germany");
        europe.add("Spain");
        mapCountries.put("Europe", europe);




        // --------- تعبئة قائمة القارات ---------
        ArrayAdapter<String> continentAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>(mapCountries.keySet())
        );
        continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCon.setAdapter(continentAdapter);

        // --------- عند اختيار قارة ---------
        spinnerCon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedContinent = parent.getItemAtPosition(position).toString();

                // عرض الدول الخاصة بالقارة المختارة
                ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        mapCountries.get(selectedContinent)
                );
                countryListView.setAdapter(countriesAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // --------- عند اختيار دولة ---------
        countryListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedCountry = parent.getItemAtPosition(position).toString();
        });
    }

    @Override
    protected void onResume() {
        super.onResume(); // Always call super first

        // 1. Clear the old data so you don't show duplicates
        adapter.clear();

        // 2. Fetch the list from the database and add it to the adapter
        // Assuming getAllTasks() returns List<Trip>
        if (db.myTaskQuery() != null) {
            adapter.addAll(db.myTaskQuery().getAllTasks());
        }
    }














}




