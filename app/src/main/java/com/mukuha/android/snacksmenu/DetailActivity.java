package com.mukuha.android.snacksmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.mukuha.android.snacksmenu.database.AppDatabase;
import com.mukuha.android.snacksmenu.model.DataItem;

public class DetailActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.detail_activity_title);
        //up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//      receiving the name of the menu item that was sent by the DataItemAdapter
//      as a string extra
        String itemId = getIntent().getStringExtra(DataItemAdapter.ITEM_ID_KEY);

//      retrieving that menu item from the database so as to display it in the DetailFragment
//      getting a database reference
        db = AppDatabase.getInstance(this);

        DataItem item = db.dataItemDao().retrieveByID(itemId);

//      displaying that menu item in details in the DetailFragment
        FragmentDetail fragmentDetail = FragmentDetail.newInstance(item);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detail_fragment_container, fragmentDetail)
                .commit();
    }

    @Override
    protected void onDestroy() {
//      De-referencing the database object within the singleton to avoid database leaks
        AppDatabase.destroyInstance();
        super.onDestroy();
    }
}