package com.example.larafinal;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerContinents;
    private ListView listCountries;
    private Button btnContinue;

    // قارات + الدول
    private HashMap<String, ArrayList<String>> mapCountries;
    private String selectedContinent = "";
    private String selectedCountry = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); // غيّر الاسم حسب ملف XML

        // --------- ربط العناصر ---------
        spinnerContinents = findViewById(R.id.spinnerContinents);
        listCountries = findViewById(R.id.listCountries);
        btnContinue = findViewById(R.id.btnContinue);

        // --------- تجهيز البيانات ---------






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
}


