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


public class MyTaskAdapter extends ArrayAdapter<Trip> {

    private final int itemLayout;

    /**
     * פעולה בונה מתאם
     *
     * @param context  קישור להקשר (מסך- אקטיביטי)
     * @param resource עיצוב של פריט שיציג הנתונים של העצם
     */
    public MyTaskAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.itemLayout = resource;
    }
    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vitem= convertView;
        if(vitem==null)
            vitem= LayoutInflater.from(getContext()).inflate(itemLayout,parent,false);
        ImageView ivTripImage=vitem.findViewById(R.id.ivTripImage);
        TextView tvTripName=vitem.findViewById(R.id.tvTripName);
        TextView tvTripDescription=vitem.findViewById(R.id.tvTripDescription);

         Trip current=getItem(position);
        //הצגת הנתונים על שדות הרכיב הגרפי
       // ivTripImage.setImageResource(current.getImage());
        tvTripName.setText(current.getTripName());
        tvTripDescription.setText("Importance:"+current.getTripDescription());




        return vitem;


    }


}