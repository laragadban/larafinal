package com.example.larafinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {
    private TextView TvSignUp;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPass;
    private EditText etConPass;
    private Button btnSignUp1;
    private  TextView Tvaccount;

    private TextView TvLOG ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        btnSignUp1 = findViewById(R.id.btnSignUp1);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etConPass = findViewById(R.id.etConPass);
        etPass = findViewById(R.id.etPass);
        Tvaccount = findViewById(R.id.Tvaccount);
        TvSignUp = findViewById(R.id.btnlogin1);

Tvaccount.setOnClickListener(v ->
        {
            Intent intent = new Intent(SignUp.this, loginsc.class);
            startActivity(intent);
            finish();
        });
        btnSignUp1.setOnClickListener(v ->
        {
            Intent intent = new Intent(SignUp.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnlogin1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}