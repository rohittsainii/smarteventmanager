package com.example.smarteventmanager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private TextView tvDate, tvTime, tvReminderTime;
    private Spinner spinnerCategory;
    private RatingBar ratingPriority;
    private SwitchCompat switchReminder;
    private SeekBar seekBarReminder;
    private Button btnSave;

    private EventDatabaseHelper dbHelper;
    private Calendar selectedDateTime;
    private int reminderMinutes = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Event");

        // Initialize Database
        dbHelper = new EventDatabaseHelper(this);
        selectedDateTime = Calendar.getInstance();

        // Initialize Views
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        ratingPriority = findViewById(R.id.ratingPriority);
        switchReminder = findViewById(R.id.switchReminder);
        seekBarReminder = findViewById(R.id.seekBarReminder);
        tvReminderTime = findViewById(R.id.tvReminderTime);
        btnSave = findViewById(R.id.btnSave);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set default values
        updateDateTimeDisplay();
        tvReminderTime.setText("Reminder: 30 minutes before");

        // Date Picker
        tvDate.setOnClickListener(v -> showDatePicker());

        // Time Picker
        tvTime.setOnClickListener(v -> showTimePicker());

        // SeekBar Listener
        seekBarReminder.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                reminderMinutes = progress;
                tvReminderTime.setText("Reminder: " + progress + " minutes before");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Toggle Reminder
        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            seekBarReminder.setEnabled(isChecked);
            if (!isChecked) {
                tvReminderTime.setText("Reminder: OFF");
            } else {
                tvReminderTime.setText("Reminder: " + reminderMinutes + " minutes before");
            }
        });

        // Save Button
        btnSave.setOnClickListener(v -> saveEvent());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTimeDisplay();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    updateDateTimeDisplay();
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void updateDateTimeDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        tvDate.setText(dateFormat.format(selectedDateTime.getTime()));
        tvTime.setText(timeFormat.format(selectedDateTime.getTime()));
    }

    private void saveEvent() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        float priority = ratingPriority.getRating();
        boolean reminderEnabled = switchReminder.isChecked();

        // Validation
        if (title.isEmpty()) {
            etTitle.setError("Title is required");
            etTitle.requestFocus();
            return;
        }

        if (priority == 0) {
            Toast.makeText(this, "Please set priority rating", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Event
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Event event = new Event(
                title,
                description,
                dateFormat.format(selectedDateTime.getTime()),
                timeFormat.format(selectedDateTime.getTime()),
                category,
                priority,
                reminderEnabled,
                reminderMinutes
        );

        // Save to Database
        long id = dbHelper.addEvent(event);

        if (id > 0) {
            // Set Reminder if enabled
            if (reminderEnabled) {
                setReminder((int) id);
            }

            Toast.makeText(this, "Event saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save event", Toast.LENGTH_SHORT).show();
        }
    }

    private void setReminder(int eventId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("eventTitle", etTitle.getText().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                eventId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Calculate reminder time
        Calendar reminderTime = (Calendar) selectedDateTime.clone();
        reminderTime.add(Calendar.MINUTE, -reminderMinutes);

        if (reminderTime.getTimeInMillis() > System.currentTimeMillis()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    reminderTime.getTimeInMillis(),
                    pendingIntent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}