package com.example.larafinal;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); // غيّر الاسم حسب ملف XML تبعك

        // ----------- ربط العناصر -----------
        etName = findViewById(R.id.et_signup_name);
        etEmail = findViewById(R.id.et_signup_email);
        etPassword = findViewById(R.id.et_signup_password);
        btnSignup = findViewById(R.id.btn_signup);

        // ----------- زر التسجيل -----------
        btnSignup.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            // فحص أولي للحقول
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // هون بتعمل منطق إنشاء الحساب
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

            // مثال: ترجع للمكان السابق
            // finish();
        });
    }
}
