package com.example.larafinal;

import static com.example.larafinal.R.id.imgpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class loginsc extends AppCompatActivity {
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    private Button btnLogin;
    private Button btnSignUp;
    private EditText etPass;
    private EditText etEmail;
    private TextView Tvpass;
    private  ImageView imgpro;
    private TextView Tvor;
    private TextView Tvlog;
    private ImageView imageView;


    /* <<<<<<<<<<  7f0b32a2-6855-4fda-afcb-f05e0f552701  >>>>>>>>>>> */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);
        etPass = findViewById(R.id.etPass);
        etEmail = findViewById(R.id.etEmail);
        imageView = findViewById(R.id.imgpro);

btnSignUp.setOnClickListener(v1 -> {
    Intent intent = new Intent(loginsc.this, SignUp.class);
    startActivity(intent);
});
btnLogin.setOnClickListener(v2 -> {
    Intent intent = new Intent(loginsc.this, MainActivity.class);
    startActivity(intent);
});

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnlogin1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}