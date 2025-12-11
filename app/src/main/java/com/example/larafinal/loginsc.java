package com.example.larafinal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class loginsc extends AppCompatActivity {

    private TextView tv_login_title;
    private EditText et_login_email;
    private EditText et_login_password;
    private Button btnLogin;
    private TextView tv_forgot;
    private Button btnSignUp;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // ضع اسم ملف XML الصحيح

        // ---------- ربط العناصر ----------
        tv_login_title = findViewById(R.id.tv_login_title);
        et_login_email = findViewById(R.id.et_login_email);
        et_login_password = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btnLogin);
        tv_forgot = findViewById(R.id.tv_forgot);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginsc.this, SignUp.class);
                startActivity(intent);
            }
        });

        // ---------- زر تسجيل الدخول ----------
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginsc.this, AddActivity.class);
                startActivity(intent);
            }
        });


            }


}