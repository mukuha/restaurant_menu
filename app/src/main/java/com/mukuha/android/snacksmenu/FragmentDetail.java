package com.mukuha.android.snacksmenu;

import static com.mukuha.android.snacksmenu.DataItemAdapter.ITEM_ID_KEY;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mukuha.android.snacksmenu.model.DataItem;

import java.io.IOException;
import java.io.InputStream;

public class FragmentDetail extends Fragment {
    private static final String ITEM_KEY = "item_key";
    DataItem item;
    public FragmentDetail() {
        // Required empty public constructor
    }

    public static FragmentDetail newInstance(DataItem item) {
        FragmentDetail fragment = new FragmentDetail();
        Bundle args = new Bundle();
        args.putParcelable(ITEM_KEY, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            item = getArguments().getParcelable(ITEM_KEY);
        }

        if (item == null) {
            throw new AssertionError("Null data item received!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView tvName = view.findViewById(R.id.tvItemName);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        ImageView itemImage = view.findViewById(R.id.itemImage);

        tvName.setText(item.getItemName());
        tvDescription.setText(item.getDescription());

//        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
//        tvPrice.setText(nf.format(item.getPrice()));

        tvPrice.setText("Ksh " + (107*(item.getPrice())));

        InputStream inputStream = null;
        try {
            String imageFile = item.getImage();
            inputStream = getContext().getAssets().open(imageFile);
            Drawable d = Drawable.createFromStream(inputStream, null);
            itemImage.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return view;
    }
}