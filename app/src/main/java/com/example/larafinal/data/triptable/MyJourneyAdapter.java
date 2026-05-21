package com.example.larafinal.data.triptable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.larafinal.Activiydetails;
import com.example.larafinal.AddJourneyActivity;
import com.example.larafinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * محول مخصص (Adapter) لعرض بيانات الرحلات داخل ListView
 */
public class MyJourneyAdapter extends ArrayAdapter<MyJourney> {

    /**
     * متغير لحفظ ملف التصميم الخاص بعنصر القائمة
     */
    private final int itemLayout;

    /**
     * Constructor
     *
     * @param context     السياق الحالي للتطبيق
     * @param resource    ملف تصميم العنصر داخل القائمة
     * @param displayList قائمة الرحلات
     */
    public MyJourneyAdapter(@NonNull Context context, int resource, List<MyJourney> displayList) {
        // استدعاء Constructor للكلاس الأب
        super(context, resource, displayList);
        // حفظ رقم ملف التصميم
        this.itemLayout = resource;
    }

    /**
     * دالة مسؤولة عن إنشاء وعرض عنصر داخل الـ ListView
     *
     * @param position    موقع العنصر داخل القائمة
     * @param convertView عنصر قديم لإعادة الاستخدام
     * @param parent      الحاوية الأساسية
     * @return View جاهزة للعرض
     */
    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // تجهيز عنصر الواجهة
        View vitem = convertView;

        // إذا لم يوجد عنصر جاهز يتم إنشاء عنصر جديد
        if (vitem == null) {
            vitem = LayoutInflater.from(getContext()).inflate(itemLayout, parent, false);
        }

        // ربط عناصر الواجهة الرسومية
        ImageView ivTripImage = vitem.findViewById(R.id.ivTripImage);
        TextView tvTripName = vitem.findViewById(R.id.tvTripName);
        TextView tvTripDescription = vitem.findViewById(R.id.tvTripDescription);
        ImageButton btnLike = vitem.findViewById(R.id.btnLike);

        // الحصول على الرحلة الحالية حسب موقعها
        MyJourney current = getItem(position);

        if (current != null) {
            // عرض اسم الرحلة
            tvTripName.setText(current.getTripName());

            // عرض وصف الرحلة
            tvTripDescription.setText(current.getTripDescription());

            // فحص إذا كانت الصورة موجودة
            if (current.getImage() != null && current.getImage().length() > 0) {
                ivTripImage.setImageBitmap(stringToBitmap(current.getImage()));
            } else {
                ivTripImage.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        // عند الضغط على الصورة يتم فتح شاشة التفاصيل
        ivTripImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current != null) {
                    Intent intent = new Intent(getContext(), Activiydetails.class);
                    intent.putExtra("jr", current);
                    getContext().startActivity(intent);
                }
            }
        });

        // منطق زر الإعجاب (قلب)
        final boolean[] isLiked = {false};
        btnLike.setOnClickListener(v -> {
            isLiked[0] = !isLiked[0]; // عكس الحالة
            if (isLiked[0]) {
                // تغيير اللون إلى الأحمر عند الضغط
                btnLike.setImageTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                saveFavoraitJourney(current);
            } else {
                // العودة للون الأبيض عند إلغاء الضغط
                btnLike.setImageTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));
            }
        });

        return vitem;
    }

    /**
     * تحويل نص مشفر Base64 إلى صورة Bitmap
     *
     * @param imageString النص المشفر للصورة
     * @return صورة Bitmap
     */
    private Bitmap stringToBitmap(String imageString) {
        if (imageString == null || imageString.isEmpty()) return null;
        try {
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            return null;
        }
    }
    private void saveFavoraitJourney(MyJourney myJourney) {

        DatabaseReference database =
                FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference journeysRef =
                database.child("Journeys_"+uid);

        DatabaseReference newJourneyRef =
                journeysRef.push();

        /**
         * حفظ المفتاح داخل الكائن
         */
        myJourney.setKey(newJourneyRef.getKey());

        /**
         * رفع البيانات إلى Firebase
         */
        newJourneyRef.setValue(myJourney)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                getContext(),
                                "Journey saved successfully!",
                                Toast.LENGTH_LONG
                        ).show();

                        // إغلاق الصفحة


                    } else {

                        Toast.makeText(
                                getContext(),
                                "Failed to save journey",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
