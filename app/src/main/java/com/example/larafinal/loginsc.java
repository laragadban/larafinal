package com.example.larafinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class loginsc extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private Button btnLogin;
    private TextView tvForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // ضع اسم ملف XML الصحيح

        // ---------- ربط العناصر ----------
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgot = findViewById(R.id.tv_forgot);

        // ---------- زر تسجيل الدخول ----------
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // هنا بتعمل logic تسجيل الدخول
            Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show();
        });

        // ---------- ضغط على "Forgot Password?" ----------
        tvForgot.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show();
        });
    }
}


