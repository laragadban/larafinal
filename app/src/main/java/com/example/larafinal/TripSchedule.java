package com.example.larafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.larafinal.data.triptable.MyJourney;
import com.example.larafinal.data.triptable.MyJourneyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripSchedule extends AppCompatActivity {

    private ListView listView;
    private MyJourneyAdapter adapter;
    private FloatingActionButton fabAdd;
    private List<MyJourney> journeyList = new ArrayList<>();

    // تعريف المرجع خارج الدالة ليسهل الوصول إليه
    private DatabaseReference myRef;
    private ValueEventListener journeyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_schedule);

        // 1. ربط العناصر (تأكد أن الـ ID في الـ XML هو recyclerview)
        listView = findViewById(R.id.lvDailyEvents);
        fabAdd = findViewById(R.id.fabAdd);

        if (listView == null) {
            Toast.makeText(this, "Error: RecyclerView not found in XML", Toast.LENGTH_LONG).show();
            return;
        }



        // 3. إعداد الـ Adapter
        adapter = new MyJourneyAdapter(this, R.layout.journey_item_layout,journeyList);
        listView.setAdapter(adapter);
        getAllFromFirebase();
        // 4. زر إضافة رحلة جديدة

            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(TripSchedule.this, AddJourneyActivity.class);
                startActivity(intent);
            });


        // تجهيز مرجع قاعدة البيانات




    }

    // 5. جلب البيانات من Firebase Realtime Database
    private void getAllFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        journeyList.clear();
        adapter.clear();
        myRef = FirebaseDatabase.getInstance().getReference("Journeys_"+uid);
        journeyListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                journeyList.clear();
                adapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        MyJourney journey = dataSnapshot.getValue(MyJourney.class);
                        if (journey != null) {
                            journeyList.add(journey);
                        }
                    } catch (Exception e) {
                        // هذا الخطأ يظهر إذا كان كلاس MyJourney يفتقد للـ Empty Constructor
                        Toast.makeText(TripSchedule.this, "Data Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
              //  adapter.addAll(journeyList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TripSchedule.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        myRef.addValueEventListener(journeyListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFromFirebase();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // إيقاف المستمع عند الخروج من الشاشة لتوفير الموارد
        if (myRef != null && journeyListener != null) {
            myRef.removeEventListener(journeyListener);
        }
    }
}