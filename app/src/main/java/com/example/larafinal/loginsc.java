package com.example.larafinal;

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

    private Button btnlog;
    private EditText etPass;
    private EditText etEmail;
    private TextView Tvpass;
    private  ImageView imgpro;
    private TextView Tvor;
    private TextView Tvlog;
    private View btnSignup;


    /* <<<<<<<<<<  7f0b32a2-6855-4fda-afcb-f05e0f552701  >>>>>>>>>>> */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        btnSignup = findViewById(R.id.btnSignup);
        btnlog = findViewById(R.id.btnSignUp1);
        Tvlog = findViewById(R.id.Tvlog);
        Tvpass = findViewById(R.id.Tvpass);
        etPass = findViewById(R.id.etPass);
        etEmail = findViewById(R.id.etEmail);
        Tvor = findViewById(R.id.Tvor);
        imgpro = findViewById(R.id.imgpro);

        btnlog.setOnClickListener(v2 -> {
    Intent intent = new Intent(loginsc.this, MainActivity.class);
    startActivity(intent);
});
btnSignup.setOnClickListener(v1 -> {
    Intent intent = new Intent(loginsc.this, SignUp.class);
    startActivity(intent);
});


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnlogin1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}