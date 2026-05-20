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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.larafinal.Activiydetails;
import com.example.larafinal.R;

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
     * @param displayList
     */
    public MyJourneyAdapter(@NonNull Context context, int resource, List<MyJourney> displayList) {

        // استدعاء Constructor للكلاس الأب
        super(context, resource);

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

        /**
         * ربط عناصر الواجهة الرسومية
         */
        ImageView ivTripImage = vitem.findViewById(R.id.ivTripImage);

        TextView tvTripName = vitem.findViewById(R.id.tvTripName);

        TextView tvTripDescription = vitem.findViewById(R.id.tvTripDescription);

        /**
         * الحصول على الرحلة الحالية حسب موقعها
         */
        MyJourney current = getItem(position);

        if (current != null) {

            // عرض اسم الرحلة
            tvTripName.setText(current.getTripName());

            // عرض وصف الرحلة
            tvTripDescription.setText(current.getTripDescription());

            /**
             * فحص إذا كانت الصورة موجودة
             */
            if (current.getImage()!=null && current.getImage().length()>0)

                // تحويل النص إلى صورة وعرضها
                ivTripImage.setImageBitmap(stringToBitmap(current.getImage()));

            else

                // عرض صورة افتراضية
                ivTripImage.setImageResource(R.drawable.ic_launcher_background);
        }

        /**
         * عند الضغط على الصورة يتم فتح شاشة التفاصيل
         */
        ivTripImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // إنشاء Intent للانتقال إلى شاشة التفاصيل
                Intent intent = new Intent(getContext(), Activiydetails.class);

                // إرسال بيانات الرحلة
                intent.putExtra("jr",current);

                // تشغيل شاشة التفاصيل
                getContext().startActivity(intent);

                ImageButton btnLike = view.findViewById(R.id.btnLike);
                btnLike.setOnClickListener(v -> {
                    // كود الإعجاب هنا
                    btnLike.setImageResource(android.R.drawable.btn_star_big_on); // تغيير الشكل عند الضغط
                });

            }
        });
        ImageButton btnLike = vitem.findViewById(R.id.btnLike);// حالة افتراضية (يمكنك لاحقاً ربطها بقاعدة البيانات)
        final boolean[] isLiked = {false};

        btnLike.setOnClickListener(v -> {
            if (!isLiked[0]) {
                // إذا ضغط وأصبح معجب (قلب ممتلئ)
                btnLike.setImageResource(R.drawable.ic_heart);
                isLiked[0] = true;
            } else {
                // إذا ضغط مرة أخرى لإزالة الإعجاب (قلب مفرغ)
                btnLike.setImageResource(R.drawable.ic_heart_white);
                isLiked[0] = false;
            }
        });
        // إرجاع العنصر بعد تعبئة البيانات
        return vitem;


    }

    /**
     * تحويل نص مشفر Base64 إلى صورة Bitmap
     *
     * @param imageString النص المشفر للصورة
     * @return صورة Bitmap
     */
    private Bitmap stringToBitmap(String imageString) {

        // فحص إذا كانت الصورة فارغة
        if (imageString == null || imageString.isEmpty()) return null;

        try {

            // فك تشفير النص إلى Bytes
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);

            // تحويل الـ Bytes إلى Bitmap
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        } catch (Exception e) {

            // في حالة حدوث خطأ
            return null;
        }
    }
}