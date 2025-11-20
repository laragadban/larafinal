package com.example.larafinal.data.MyUserTable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


    //Entity = Table =جدول
//عندما نريد ان نتعامل مع هذه الفئة كجدول معطيات
    /**
     * فئة تمثل المستعمل
     */
    @Entity
    public class MyUser
    {
        @PrimaryKey(autoGenerate = true)//تحديد الصفة كمفتاح رئيسي والذي يُنتجح بشكل تلقائي
        public long keyid;
        @ColumnInfo(name = "full_Name")//اعطاء اسم جديد للعامود-الصفة في الجدول
        public String UserName;
        public String email;//بحالة لم يتم اعطاء اسم للعامود يكون اسم الصفه هو اسم العامود
        public String password;
        public String Confirmpassword;

        public long getKeyid() {
            return keyid;
        }

        public void setKeyid(long keyid) {
            this.keyid = keyid;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmpassword() {
            return Confirmpassword;
        }

        public void setConfirmpassword(String confirmpassword) {
            Confirmpassword = confirmpassword;
        }

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




