package com.example.larafinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larafinal.data.Appdatabase;
import com.example.larafinal.data.MyUserTable.MyUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginsc extends AppCompatActivity {

    private EditText et_login_email;
    private EditText et_login_password;
    private Button btnLogin;
    private TextView tvAsk;
    private Button btnSignUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // حدد الملف وببني الكائنات في واجهة التنسيق
        setContentView(R.layout.activity_login);

        // 1. ربط العناصر بالواجهة
        et_login_email = findViewById(R.id.etEmail);
        et_login_password = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btn_signup);
        tvAsk = findViewById(R.id.tvAsk);

//        // 2. زر الانتقال لصفحة التسجيل
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(loginsc.this, SignUp.class);
//                startActivity(intent);
//            }
//        });

        tvAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginsc.this, SignUp.class);
                startActivity(intent);
            }
        });

        // 3. زر تسجيل الدخول
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLoginLogic();
            }
        }); // تم إغلاق الـ Listener بشكل صحيح هنا
    }

    private void performLoginLogic() {
        String email = et_login_email.getText().toString().trim();
        String password = et_login_password.getText().toString().trim();

        // فحص إذا كانت الحقول فارغة
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // فحص صيغة الإيميل
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // أولاً: تسجيل الدخول عبر Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // إذا نجح Firebase، نتحقق من وجود المستخدم محلياً في Room
                            checkUserInRoom(email, password);
                        } else {
                            // فشل تسجيل الدخول في Firebase
                            Toast.makeText(loginsc.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }); // تم إغلاق الـ addOnCompleteListener هنا
    }

    private void checkUserInRoom(String email, String password) {
        // الوصول لقاعدة البيانات Room يجب أن يكون في خيط خلفي (Thread) لتجنب الـ Crash
        new Thread(new Runnable() {
            @Override
            public void run() {
                // جلب المستخدم من قاعدة البيانات
                MyUser myUser = Appdatabase.getdb(loginsc.this).myUserQuery().checkEmailPassw(email, password);

                // العودة للخيط الرئيسي لتحديث الواجهة (UI)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (myUser != null) {
                            Toast.makeText(loginsc.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(loginsc.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // إغلاق صفحة اللوجين
                        } else {
                            // حالة نجاح Firebase ولكن المستخدم غير مسجل في Room محلياً
                            Toast.makeText(loginsc.this, "Firebase Success, but local user not found", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(loginsc.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).start();
    }
}