package com.example.larafinal.data.triptable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.larafinal.R;

/**
 * محول مخصص (Custom Adapter) لربط قائمة الرحلات (MyJourney) بـ ListView
 */
public class MyJourneyAdapter extends ArrayAdapter<MyJourney> {

    private final int itemLayout;

    /**

     * @param resource رقم ملف الـ Layout الذي يمثل شكل العنصر الواحد
 *                 داخل القائمة (مثال: journey_item_layout).
            */
    public MyJourneyAdapter(@NonNull Context context, int resource) {

        // استدعاء Constructor الخاص بالكلاس الأب (ArrayAdapter)
        super(context, resource);

        // تخزين رقم ملف التصميم لاستخدامه لاحقاً في getView()
        this.itemLayout = resource;
    }


    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. تجهيز واجهة العنصر (Item View)
        View vitem = convertView;
        if (vitem == null) {

            vitem = LayoutInflater.from(getContext()).inflate(itemLayout, parent, false);
        }

        // 2. ربط العناصر الرسومية الموجودة في ملف journey_item_layout.xml
        ImageView ivTripImage = vitem.findViewById(R.id.ivTripImage);
        TextView tvTripName = vitem.findViewById(R.id.tvTripName);
        TextView tvTripDescription = vitem.findViewById(R.id.tvTripDescription);

        // 3. الحصول على بيانات الرحلة الحالية بناءً على موقعها في القائمة
        MyJourney current = getItem(position);

        if (current != null) {
            // 4. عرض البيانات داخل العناصر الرسومية

            // تعيين اسم الرحلة
            tvTripName.setText(current.getTripName());

            // تعيين وصف الرحلة
            tvTripDescription.setText(current.getTripDescription());

            // تعيين الصورة (إذا كنت تخزن مسار الصورة أو ID الخاص بها)
            // ملاحظة: تأكد أن كلاس MyJourney يحتوي على صورة، وإلا اتركها افتراضية
            // if (current.getImage() != 0) {
            //     ivTripImage.setImageResource(current.getImage());
            // } else {
            if (current.getImage()!=null && current.getImage().length()>0)
                ivTripImage.setImageBitmap(stringToBitmap(current.getImage()));
            else
            ivTripImage.setImageResource(R.drawable.ic_launcher_foreground); // صورة افتراضية
            // }
        }

        // 5. إرجاع الواجهة بعد تعبئتها بالبيانات ليتم عرضها في الـ ListView
        return vitem;
    }

    /**
     * Decodes the image string and returns the corresponding Bitmap object.
     *
     * @param imageString the image string to decode
     * @return the decoded Bitmap object
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


}