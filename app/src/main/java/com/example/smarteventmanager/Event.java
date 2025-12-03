package com.example.smarteventmanager;

public class Event {
    private int id;
    private String title;
    private String description;
    private String date;
    private String time;
    private String category;
    private float priority;
    private boolean isCompleted;
    private boolean reminderEnabled;
    private int reminderMinutes;

    public Event() {
    }

    public Event(String title, String description, String date, String time,
                 String category, float priority, boolean reminderEnabled,
                 int reminderMinutes) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.category = category;
        this.priority = priority;
        this.isCompleted = false;
        this.reminderEnabled = reminderEnabled;
        this.reminderMinutes = reminderMinutes;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPriority() {
        return priority;
    }

    public void setPriority(float priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public int getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(int reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }
}