package com.mukuha.android.snacksmenu;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mukuha.android.snacksmenu.database.AppDatabase;
import com.mukuha.android.snacksmenu.model.DataItem;
import com.mukuha.android.snacksmenu.sample.SampleDataProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    public static boolean tabletDetected;
    private final List<DataItem> dataItemList = SampleDataProvider.dataItemList;
    private AppDatabase db;
    public static final Executor executor = Executors.newSingleThreadExecutor();

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    public String[] mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Creating a database and loading data from a dummy data class(SampleDataProvider)
//      into the database when the application first starts, after installing
        db = AppDatabase.getInstance(this);
//      Executing database operations in a background thread
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int itemCount = db.dataItemDao().countItems();
                if (itemCount == 0) {
                    db.dataItemDao().insertAll(dataItemList);
                }
            }
        });

//      Code to add the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
        actionBar.setTitle(R.string.main_activity_tittle);

//      Detecting the layout that has loaded as the app starts to create the logic in DataItemAdapter
        ViewGroup detailFragmentContainer = findViewById(R.id.detail_fragment_container);
        tabletDetected = (detailFragmentContainer != null);

//        Code to manage sliding navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mCategories = getResources().getStringArray(R.array.categories);
        mDrawerList = findViewById(R.id.left_drawer);

//        mDrawerList.setAdapter(new ArrayAdapter<>(this,
//                android.R.layout.simple_selectable_list_item, mCategories));
        ArrayAdapter<String> adapter = new DrawerAdapter(this, mCategories);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String category = mCategories[i];
                    mDrawerLayout.closeDrawer(mDrawerList);
//                Log.i("seeCAT", "MAIN ACTIVITY onCreate: Category is " + category);
                    displayContent(category);
            }
        });
//        end of navigation drawer

        displayContent(null);
    }

    private void displayContent(String category) {
//      This code solve an issue where if device orientation changed from landscape to portrait,
//      the main screen would display two lists of menu items one on top of another
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_fragment_container);

        FragmentMain fragmentMain = FragmentMain.newInstance(category);
        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, fragmentMain)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, fragmentMain)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
//      De-referencing the database object within the singleton as the
//      activity shuts down, to avoid database leaks
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(mDrawerList);
        }else if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, PrefsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}