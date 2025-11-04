package com.example.larafinal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPass;
    private Button btnsign;
 private EditText etConPass;

    private Button btnSign;
    private Button btnIn;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnlogin1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnSign = findViewById(R.id.btnsign);
        btnIn = findViewById(R.id.btnIn);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        btnsign = findViewById(R.id.btnsign );
        etConPass = findViewById(R.id.etConPass);
        etPass = findViewById(R.id.etPass);

        btnSign.setOnClickListener(v ->
        {
            Intent intent = new Intent(SignUp.this, MainActivity.class);
            startActivity(intent);
        });
btnIn.setOnClickListener(v ->
{
    Intent intent = new Intent(SignUp.this, loginsc.class);
    startActivity(intent);
});
    }
}