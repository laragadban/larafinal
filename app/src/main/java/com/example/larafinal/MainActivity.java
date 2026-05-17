package com.example.larafinal;


import static com.example.larafinal.data.Appdatabase.db;


import androidx.annotation.NonNull;
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
import android.widget.Toast;


import com.example.larafinal.data.triptable.MyJourney;
import com.example.larafinal.data.triptable.MyJourneyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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
        // , يحدد ملف ال MAIN ACTIVIT
        //بني كائن في واجهة التنسيق
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


        ArrayList<String> Africa = new ArrayList<>();
        Africa.add("Egypt"); Africa.add("Nigeria");
        mapCountries.put("Africa", Africa);


        ArrayAdapter<String> continentAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, new ArrayList<>(mapCountries.keySet())
        );
        continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCon.setAdapter(continentAdapter);
    }


    //todo get all from firbase


    // 5. دالة جلب البيانات من Firebase Firestore




    private void getAllFromFirebase(MyJourneyAdapter adapter) {
        //عنوان قاعدة البيانات
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // عنوان مجموعة المعطيات داخل قاعدة البيانات
        DatabaseReference myRef = database.getReference("Journeys");
//إضافة listener مما يسبب الإصغاء لكل تغيير حتلنة عرض المعطيات//
        myRef.addValueEventListener(new ValueEventListener() {
            @Override//دالة معالج حدث تقوم بتلقى نسخة عن كل المعطيات عند أي تغيير
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();//حذف كل المعطيات بالوسيط
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    //  استخراج كل المعطيات على وتحويلها لكائن ملائم//
                    MyJourney task = taskSnapshot.getValue(MyJourney.class);
                    adapter.add(task);//اضافة كل معطى (كائن) للمنسق
                }
                adapter.notifyDataSetChanged();//اعلام المنسق بالتغيير
                Toast.makeText(MainActivity.this, "Data fetched successfully", Toast.LENGTH_SHORT).show();




            }
            @Override//بحالة فشل استخراج المعطيات
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }






    @Override
    //تنفذ دائما عند فتح الشاشة
    protected void onResume() {
        super.onResume();
        // جلب البيانات من قاعدة البيانات المحلية (Room)
        //loadDataFromDatabase();


        // جلب البيانات من السحابة (Firebase)
        getAllFromFirebase(adapter);
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














