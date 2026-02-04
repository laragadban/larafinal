package com.example.larafinal.data.triptable;

import android.content.Context;
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

    public MyJourneyAdapter(@NonNull Context context, int resource) {
        super(context, resource);
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
            ivTripImage.setImageResource(R.drawable.ic_launcher_background); // صورة افتراضية
            // }
        }

        // 5. إرجاع الواجهة بعد تعبئتها بالبيانات ليتم عرضها في الـ ListView
        return vitem;
    }
}