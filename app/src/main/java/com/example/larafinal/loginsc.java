package com.example.larafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

class LoginActivity extends AppCompatActivity {

    private TextInputEditText et_login_email;
    private TextInputEditText et_login_password;
    private Button btnLogin;
// كائن نظام التحقق من الهوية الخاص بـ Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // تهيئة Firebase
        mAuth = FirebaseAuth.getInstance();

        // ربط العناصر من XML
        et_login_email = findViewById(R.id.et_login_email);
        et_login_password = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btnLogin);

        // عند الضغط على زر تسجيل الدخول
        btnLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {

        String email = et_login_email.getText().toString().trim();
        String password = et_login_password.getText().toString().trim();

        // فحص الحقول
        if (email.isEmpty()) {
            et_login_email.setError("Enter your email");
            et_login_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            et_login_password.setError("Enter your password");
            et_login_password.requestFocus();
            return;
        }

        // تسجيل الدخول عبر Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT).show();

                            // الانتقال إلى الصفحة الرئيسية
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(LoginActivity.this,
                                    "Authentication Failed: " +
                                            task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
