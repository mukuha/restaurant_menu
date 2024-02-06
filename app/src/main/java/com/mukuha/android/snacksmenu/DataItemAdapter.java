package com.mukuha.android.snacksmenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mukuha.android.snacksmenu.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {
    boolean tabletDetected = MainActivity.tabletDetected;
    public static final String ITEM_ID_KEY = "item_id_key";
    private final List<DataItem> mItems;
    private final Context mContext;
    private final FragmentManager mFragmentManager;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    public DataItemAdapter(Context context, List<DataItem> items, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mItems = items;
        this.mFragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  @Nullable String s) {
                Log.i("preferences", "onSharedPreferenceChanged: " + s);
            }
        };
        settings.registerOnSharedPreferenceChangeListener(prefsListener);

        boolean grid = settings.getBoolean(mContext.getString(R.string.pref_display_grid), false);
        int layoutId = grid ? R.layout.grid_item : R.layout.list_item;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(layoutId, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {
        DataItem item = mItems.get(position);
        try {
            holder.tvName.setText(item.getItemName());
            String imageFile = item.getImage();
            InputStream inputStream = mContext.getAssets().open(imageFile);
            Drawable d = Drawable.createFromStream(inputStream, null);
            holder.imageView.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabletDetected) {
                    FragmentDetail fragmentDetail = FragmentDetail.newInstance(item);

                    Fragment fragment = mFragmentManager.findFragmentById(R.id.detail_fragment_container);

                    if (fragment == null) {
                        mFragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .add(R.id.detail_fragment_container, fragmentDetail)
                                .commit();
                    } else {
                        mFragmentManager.popBackStack();//remove from backstack previous transaction
                        mFragmentManager.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.detail_fragment_container, fragmentDetail)
                                .commit();
                    }
                } else {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(ITEM_ID_KEY, item.getItemId());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imageView;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.itemNameText);
            imageView = itemView.findViewById(R.id.imageView);
            mView = itemView;
        }
    }
}