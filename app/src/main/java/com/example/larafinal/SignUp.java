package com.example.larafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.larafinal.data.Appdatabase;
import com.example.larafinal.data.MyUserTable.MyUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private EditText etCon;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = findViewById(R.id.et_signup_name);
        etEmail = findViewById(R.id.et_signup_email);
        etPassword = findViewById(R.id.et_signup_password);
        btnSignup = findViewById(R.id.btn_signup);
        etCon = findViewById(R.id.etCon);

        btnSignup.setOnClickListener(v -> {
            validateFields();
        });
    }

    private void validateFields() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String con = etCon.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || con.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(con)) {
            Toast.makeText(this, "Password and confirm password not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // إنشاء كائن المستخدم
        MyUser myUser = new MyUser();
        myUser.setUserName(name);
        myUser.setEmail(email);
        myUser.setPassword(password);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // 1. استخدام خيط خلفي (Thread) لحفظ البيانات في Room لتجنب الـ Crash
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Appdatabase.getdb(SignUp.this).myUserQuery().insert(myUser);

                                    // 2. العودة للخيط الرئيسي (UI Thread) للانتقال للشاشة التالية
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                                            // --- كود الانتقال إلى MainActivity ---
                                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                                            startActivity(intent);
                                            finish(); // لإغلاق شاشة التسجيل لكي لا يعود إليها المستخدم عند ضغط زر الرجوع
                                        }
                                    });
                                }
                            }).start();

                        } else {
                            Toast.makeText(SignUp.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            etEmail.setError("Email already exists or invalid");
                        }
                    }
                });
    }
}
