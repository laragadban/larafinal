package com.example.larafinal.data.MyUserTable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * فئة (Class) تمثل جدول المستخدمين داخل قاعدة البيانات Room
 * كل كائن من هذا النوع يمثل مستخدم واحد
 */
@Entity
public class MyUser {

    /**
     * المفتاح الرئيسي للجدول
     * يتم توليده تلقائياً لكل مستخدم جديد
     */
    @PrimaryKey(autoGenerate = true)
    public long keyid;

    /**
     * اسم المستخدم الكامل
     * تم تغيير اسم العمود داخل قاعدة البيانات إلى full_Name
     */
    @ColumnInfo(name = "full_Name")
    public String UserName;

    /**
     * البريد الإلكتروني للمستخدم
     * اسم العمود سيكون نفسه اسم المتغير
     */
    public String email;

    /**
     * كلمة المرور الخاصة بالمستخدم
     */
    public String password;

    /**
     * تأكيد كلمة المرور
     */
    public String Confirmpassword;
    public String key;


    /**
     * إرجاع رقم المفتاح الرئيسي
     * @return keyid
     */
    public long getKeyid() {
        return keyid;
    }

    /**
     * تعيين رقم المفتاح الرئيسي
     * @param keyid رقم المستخدم
     */
    public void setKeyid(long keyid) {
        this.keyid = keyid;
    }

    /**
     * إرجاع اسم المستخدم
     * @return UserName
     */
    public String getUserName() {
        return UserName;
    }

    /**
     * تعيين اسم المستخدم
     * @param userName اسم المستخدم الجديد
     */
    public void setUserName(String userName) {
        UserName = userName;
    }

    /**
     * إرجاع البريد الإلكتروني
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * تعيين البريد الإلكتروني
     * @param email البريد الإلكتروني الجديد
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * إرجاع كلمة المرور
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * تعيين كلمة المرور
     * @param password كلمة المرور الجديدة
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * إرجاع تأكيد كلمة المرور
     * @return Confirmpassword
     */
    public String getConfirmpassword() {
        return Confirmpassword;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * تعيين تأكيد كلمة المرور
     * @param confirmpassword تأكيد كلمة المرور
     */
    public void setConfirmpassword(String confirmpassword) {
        Confirmpassword = confirmpassword;
    }

    /**
     * تحويل بيانات المستخدم إلى نص
     * تستخدم غالباً للفحص والطباعة
     */
    @Override
    public String toString() {
        return "MyUser{" +
                "keyid=" + keyid +
                ", UserName='" + UserName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", Confirmpassword='" + Confirmpassword + '\'' +
                '}';
    }
}