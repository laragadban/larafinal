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
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;

    private EditText etCon; ;
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
        etCon = findViewById(R.id.etCon);

        // ----------- زر التسجيل -----------
        btnSignup.setOnClickListener(v -> {

validateFields();
        });
    }
        private boolean validateFields() {
            boolean isValid = true;
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String con = etCon.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || con.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (!password.equals(con)) {
                Toast.makeText(this, "Password and confirm password not match", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
            if (isValid)
            {
                MyUser myUser = new MyUser();
                myUser.setUserName(name);
                myUser.setEmail(email);
                myUser.setPassword(password);
                Appdatabase.getdb(this).myUserQuery().insert(myUser);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
                finish();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(SignUp.this, "User registered failed", Toast.LENGTH_SHORT).show();
                                etEmail.setError("Email already exists");
                            }

                        }
                    });


            }
            return (isValid) ;
            }

}
