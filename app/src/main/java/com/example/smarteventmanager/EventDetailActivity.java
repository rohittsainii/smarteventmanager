package com.example.smarteventmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EventDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvDateTime, tvCategory, tvPriority, tvReminder;
    private EventDatabaseHelper dbHelper;
    private Event currentEvent;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Event Details");

        // Initialize Views
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvCategory = findViewById(R.id.tvCategory);
        tvPriority = findViewById(R.id.tvPriority);
        tvReminder = findViewById(R.id.tvReminder);

        // Initialize Database
        dbHelper = new EventDatabaseHelper(this);

        // Get Event ID from Intent
        eventId = getIntent().getIntExtra("eventId", -1);

        if (eventId != -1) {
            loadEventDetails();
        } else {
            Toast.makeText(this, "Error loading event", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadEventDetails() {
        currentEvent = dbHelper.getEvent(eventId);

        if (currentEvent != null) {
            tvTitle.setText(currentEvent.getTitle());
            tvDescription.setText(currentEvent.getDescription());
            tvDateTime.setText(currentEvent.getDate() + " at " + currentEvent.getTime());
            tvCategory.setText("Category: " + currentEvent.getCategory());
            tvPriority.setText("Priority: " + currentEvent.getPriority() + " / 5.0");

            if (currentEvent.isReminderEnabled()) {
                tvReminder.setText("Reminder: " + currentEvent.getReminderMinutes() + " minutes before");
            } else {
                tvReminder.setText("Reminder: OFF");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            // Edit event (navigate to AddEventActivity in edit mode)
            Toast.makeText(this, "Edit functionality - to be implemented", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_delete) {
            showDeleteConfirmation();
            return true;
        } else if (id == R.id.action_share) {
            shareEvent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteEvent(eventId);
                    Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void shareEvent() {
        String shareText = "Event: " + currentEvent.getTitle() + "\n" +
                "Description: " + currentEvent.getDescription() + "\n" +
                "Date: " + currentEvent.getDate() + "\n" +
                "Time: " + currentEvent.getTime() + "\n" +
                "Category: " + currentEvent.getCategory();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share Event"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}