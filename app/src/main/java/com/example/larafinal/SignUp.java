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

/**
 * SignUp Activity
 *
 * <p>
 * هذه الواجهة مسؤولة عن تسجيل مستخدم جديد داخل التطبيق.
 * </p>
 *
 * <p>
 * عملية التسجيل تمر بالمراحل التالية:
 * 1- قراءة البيانات من الحقول (الاسم، البريد الإلكتروني، كلمة المرور، تأكيد كلمة المرور).
 * 2- التحقق من صحة المدخلات.
 * 3- تخزين المستخدم محلياً داخل قاعدة البيانات (Room Database).
 * 4- إنشاء حساب جديد باستخدام Firebase Authentication.
 * 5- عرض رسالة نجاح أو فشل حسب نتيجة العملية.
 * </p>
 *
 * @version 1.0
 */
public class SignUp extends AppCompatActivity {

    // حقول إدخال البيانات
    private EditText etName, etEmail, etPassword;
    private EditText etCon;

    // زر التسجيل
    private Button btnSignup;

    /**
     * يتم استدعاؤها عند إنشاء الـ Activity.
     * تقوم بربط عناصر الواجهة وتحديد حدث الضغط على زر التسجيل.
     *
     * @param savedInstanceState يحتوي على بيانات الحالة السابقة إن وجدت.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up); // ربط ملف الواجهة

        // ----------- ربط العناصر من XML بالكود -----------
        etName = findViewById(R.id.et_signup_name);
        etEmail = findViewById(R.id.et_signup_email);
        etPassword = findViewById(R.id.et_signup_password);
        btnSignup = findViewById(R.id.btn_signup);
        etCon = findViewById(R.id.etCon);

        // ----------- حدث الضغط على زر التسجيل -----------
        btnSignup.setOnClickListener(v -> {
            validateFields();
        });
    }

    /**
     * دالة التحقق من صحة المدخلات.*
     * في حال كانت البيانات صحيحة:
     * - يتم إنشاء كائن MyUser وتخزينه في قاعدة البيانات المحلية.
     * - يتم إنشاء حساب في Firebase Authentication.
     * @return true إذا كانت البيانات صحيحة، false إذا كان هناك خطأ.
     */
    private boolean validateFields() {

        boolean isValid = true;

        // قراءة القيم من الحقول
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String con = etCon.getText().toString().trim();

        // التحقق من أن جميع الحقول ممتلئة
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || con.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // التحقق من تطابق كلمة المرور
        if (!password.equals(con)) {
            Toast.makeText(this, "Password and confirm password not match", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        // التحقق من صحة البريد الإلكتروني
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // في حال كانت البيانات صحيحة
        if (isValid) {

            /**
             * إنشاء كائن مستخدم جديد وتخزينه في قاعدة البيانات المحلية
             */
            MyUser myUser = new MyUser();
            myUser.setUserName(name);
            myUser.setEmail(email);
            myUser.setPassword(password);


            /**
             * إنشاء حساب باستخدام Firebase Authentication
             */
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                // الحصول على UID الخاص بالمستخدم المسجل
                                String uid = FirebaseAuth
                                        .getInstance()
                                        .getCurrentUser()
                                        .getUid();
                                Appdatabase.getdb(SignUp.this).myUserQuery().insert(myUser);

                                Toast.makeText(SignUp.this,
                                        "User registered successfully",
                                        Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(SignUp.this,
                                        "User registered failed",
                                        Toast.LENGTH_SHORT).show();

                                etEmail.setError("Email already exists");
                            }
                        }
                    });
        }

        return (isValid);
    }
}
