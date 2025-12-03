package com.example.smarteventmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class EventDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EventManager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_REMINDER_ENABLED = "reminder_enabled";
    private static final String COLUMN_REMINDER_MINUTES = "reminder_minutes";

    public EventDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_PRIORITY + " REAL,"
                + COLUMN_COMPLETED + " INTEGER,"
                + COLUMN_REMINDER_ENABLED + " INTEGER,"
                + COLUMN_REMINDER_MINUTES + " INTEGER"
                + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    // Create
    public long addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, event.getTitle());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_DATE, event.getDate());
        values.put(COLUMN_TIME, event.getTime());
        values.put(COLUMN_CATEGORY, event.getCategory());
        values.put(COLUMN_PRIORITY, event.getPriority());
        values.put(COLUMN_COMPLETED, event.isCompleted() ? 1 : 0);
        values.put(COLUMN_REMINDER_ENABLED, event.isReminderEnabled() ? 1 : 0);
        values.put(COLUMN_REMINDER_MINUTES, event.getReminderMinutes());

        long id = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return id;
    }

    // Read Single Event
    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENTS, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        Event event = null;
        if (cursor != null && cursor.moveToFirst()) {
            event = cursorToEvent(cursor);
            cursor.close();
        }
        db.close();
        return event;
    }

    // Read All Events
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                eventList.add(cursorToEvent(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventList;
    }

    // Update
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, event.getTitle());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_DATE, event.getDate());
        values.put(COLUMN_TIME, event.getTime());
        values.put(COLUMN_CATEGORY, event.getCategory());
        values.put(COLUMN_PRIORITY, event.getPriority());
        values.put(COLUMN_COMPLETED, event.isCompleted() ? 1 : 0);
        values.put(COLUMN_REMINDER_ENABLED, event.isReminderEnabled() ? 1 : 0);
        values.put(COLUMN_REMINDER_MINUTES, event.getReminderMinutes());

        int result = db.update(TABLE_EVENTS, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(event.getId())});
        db.close();
        return result;
    }

    // Delete
    public void deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Get events by category
    public List<Event> getEventsByCategory(String category) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENTS, null, COLUMN_CATEGORY + "=?",
                new String[]{category}, null, null, COLUMN_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                eventList.add(cursorToEvent(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventList;
    }

    // Search events
    public List<Event> searchEvents(String query) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String searchQuery = "SELECT * FROM " + TABLE_EVENTS +
                " WHERE " + COLUMN_TITLE + " LIKE ? OR " + COLUMN_DESCRIPTION + " LIKE ?";
        String searchPattern = "%" + query + "%";

        Cursor cursor = db.rawQuery(searchQuery, new String[]{searchPattern, searchPattern});

        if (cursor.moveToFirst()) {
            do {
                eventList.add(cursorToEvent(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventList;
    }

    // Get event count
    public int getEventCount() {
        String countQuery = "SELECT * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // Helper method to convert cursor to Event object
    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event();
        event.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        event.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
        event.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
        event.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
        event.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)));
        event.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
        event.setPriority(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY)));
        event.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1);
        event.setReminderEnabled(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_ENABLED)) == 1);
        event.setReminderMinutes(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_MINUTES)));
        return event;
    }
}