package com.example.larafinal;
import static com.example.larafinal.data.Appdatabase.db;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.larafinal.data.triptable.MyJourneyAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerContinents;
    private ListView listCountries;
    private Button btnContinue;
    private ListView listTrip;
    private MyJourneyAdapter adapter;

    // قارات + الدول
    private HashMap<String, ArrayList<String>> mapCountries;
    private String selectedContinent = "";
    private String selectedCountry = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); // غيّر الاسم حسب ملف XML
        listTrip = findViewById(R.id.listCountries);
        adapter = new MyJourneyAdapter(this, R.layout.journey_item_layout);
        listTrip.setAdapter(adapter);

        // --------- ربط العناصر ---------
        spinnerContinents = findViewById(R.id.spinnerContinents);
        listCountries = findViewById(R.id.listCountries);
        btnContinue = findViewById(R.id.btnContinue);

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
        spinnerContinents.setAdapter(continentAdapter);

        // --------- عند اختيار قارة ---------
        spinnerContinents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedContinent = parent.getItemAtPosition(position).toString();

                // عرض الدول الخاصة بالقارة المختارة
                ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        mapCountries.get(selectedContinent)
                );
                listCountries.setAdapter(countriesAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // --------- عند اختيار دولة ---------
        listCountries.setOnItemClickListener((parent, view, position, id) -> {
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




