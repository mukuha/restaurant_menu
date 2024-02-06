package com.mukuha.android.snacksmenu;

import static androidx.core.app.NotificationCompat.getColor;
import static com.mukuha.android.snacksmenu.R.color.primary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DrawerAdapter extends ArrayAdapter<String> {
    String[] mDataItems;
    LayoutInflater mInfater;
    public DrawerAdapter(@NonNull Context context, @NonNull Object[] objects) {
        super(context, R.layout.nav_drawer_list_item, (String[]) objects);

        mDataItems = (String[]) objects;
        mInfater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = mInfater.inflate(R.layout.nav_drawer_list_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.nav_drawer_list_item);
        textView.setText(mDataItems[position]);

        if (position == 0) {
            convertView.setPadding(250,300,25,10);
            convertView.setBackgroundResource(R.color.primary);
            textView.setTextColor(0xfffffada);
            textView.setTextSize(35);
        }
        return convertView;
    }
}
