package com.example.smarteventmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabAddEvent;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Views
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        fabAddEvent = findViewById(R.id.fabAddEvent);

        // Setup ViewPager with Fragments
        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Events");
                            break;
                        case 1:
                            tab.setText("Statistics");
                            break;
                    }
                }).attach();

        // FAB Click Listener
        fabAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh fragments when returning to activity
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Setup Search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    performSearch(newText);
                } else if (newText.isEmpty()) {
                    // Reset to show all events
                    refreshEventList();
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            showFilterDialog();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void performSearch(String query) {
        EventListFragment fragment = (EventListFragment)
                getSupportFragmentManager().findFragmentByTag("f0");
        if (fragment != null) {
            fragment.searchEvents(query);
        }
    }

    private void refreshEventList() {
        EventListFragment fragment = (EventListFragment)
                getSupportFragmentManager().findFragmentByTag("f0");
        if (fragment != null) {
            fragment.loadEvents();
        }
    }

    private void showFilterDialog() {
        String[] categories = {"All", "Work", "Personal", "Meeting", "Birthday", "Other"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by Category")
                .setItems(categories, (dialog, which) -> {
                    String selectedCategory = categories[which];
                    filterByCategory(selectedCategory);
                    Toast.makeText(this, "Filtered: " + selectedCategory, Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void filterByCategory(String category) {
        EventListFragment fragment = (EventListFragment)
                getSupportFragmentManager().findFragmentByTag("f0");
        if (fragment != null) {
            if (category.equals("All")) {
                fragment.loadEvents();
            } else {
                fragment.filterByCategory(category);
            }
        }
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Smart Event Manager")
                .setMessage("Version 1.0\n\nA comprehensive event management app with:\n" +
                        "• SQLite Database\n" +
                        "• Event Reminders\n" +
                        "• Priority Management\n" +
                        "• Statistics Dashboard\n" +
                        "\nDeveloped for Android")
                .setPositiveButton("OK", null)
                .show();
    }
}