package com.mukuha.android.snacksmenu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mukuha.android.snacksmenu.database.AppDatabase;
import com.mukuha.android.snacksmenu.model.DataItem;

import java.util.List;

public class FragmentMain extends Fragment {
    private static final String CATEGORY_KEY = "category_key";
    private static String category;
    private List<DataItem> dataItemList;

    public FragmentMain() {
        // Required empty public constructor
    }

    public static FragmentMain newInstance(String category) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putString(CATEGORY_KEY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY_KEY);
        }

        if (getArguments() == null) {
            throw new AssertionError("Null data item received!");
        }

//      Retrieving data from the database to display it in the RecyclerView
        AppDatabase db = AppDatabase.getInstance(getContext());
        if (category == null) {
            category = "empty";
        }
        if (category.equals("empty") || category.equals("Menu") || category.equals("See all")) {
            dataItemList = db.dataItemDao().retrieveAll();

        }else{
//            Log.i("seeCAT", "MAIN FRAGMENT onCreate: Category is " + category);
            dataItemList = db.dataItemDao().retrieveAll(category);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//      Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

//        Reading the settings to modify how the app displays - ( reading default preferences)
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        RecyclerView recyclerView = view.findViewById(R.id.rvItems);
        if (grid) {
            recyclerView.setLayoutManager((new GridLayoutManager(getContext(), 3)));
        }
//      instantiating the adapter
        DataItemAdapter adapter = new DataItemAdapter(getContext(), dataItemList, getParentFragmentManager());
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onDestroy() {
//      De-referencing the database object within the singleton as the
//      activity shuts down to avoid database leaks
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}