package com.example.larafinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
private TextView tvTitle;

private TextView tvSubTitle;
private TextInputEditText inputSearch;
private RecyclerView recyclerReport;
private Button btnAddReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubtitle);
        inputSearch = findViewById(R.id.inputSearch);
        recyclerReport = findViewById(R.id.recyclerReports);
        btnAddReport = findViewById(R.id.btnAddReport);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnAddReport), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}