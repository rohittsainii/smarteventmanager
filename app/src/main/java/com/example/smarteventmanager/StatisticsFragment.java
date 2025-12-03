package com.example.smarteventmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsFragment extends Fragment {

    private TextView tvTotalEvents, tvCompletedEvents, tvUpcomingEvents;
    private TextView tvCategoryBreakdown, tvPriorityStats;
    private ProgressBar progressBar;
    private EventDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // Initialize Views
        tvTotalEvents = view.findViewById(R.id.tvTotalEvents);
        tvCompletedEvents = view.findViewById(R.id.tvCompletedEvents);
        tvUpcomingEvents = view.findViewById(R.id.tvUpcomingEvents);
        tvCategoryBreakdown = view.findViewById(R.id.tvCategoryBreakdown);
        tvPriorityStats = view.findViewById(R.id.tvPriorityStats);
        progressBar = view.findViewById(R.id.progressBar);

        // Initialize Database
        dbHelper = new EventDatabaseHelper(getContext());

        // Load Statistics
        loadStatistics();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatistics();
    }

    private void loadStatistics() {
        progressBar.setVisibility(View.VISIBLE);

        new android.os.Handler().postDelayed(() -> {
            List<Event> allEvents = dbHelper.getAllEvents();

            // Calculate statistics
            int totalEvents = allEvents.size();
            int completedEvents = 0;
            int upcomingEvents = 0;

            Map<String, Integer> categoryCount = new HashMap<>();
            Map<String, Integer> priorityCount = new HashMap<>();
            priorityCount.put("High (4-5)", 0);
            priorityCount.put("Medium (2-3)", 0);
            priorityCount.put("Low (1)", 0);

            for (Event event : allEvents) {
                if (event.isCompleted()) {
                    completedEvents++;
                } else {
                    upcomingEvents++;
                }

                // Category count
                String category = event.getCategory();
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);

                // Priority count
                float priority = event.getPriority();
                if (priority >= 4) {
                    priorityCount.put("High (4-5)", priorityCount.get("High (4-5)") + 1);
                } else if (priority >= 2) {
                    priorityCount.put("Medium (2-3)", priorityCount.get("Medium (2-3)") + 1);
                } else {
                    priorityCount.put("Low (1)", priorityCount.get("Low (1)") + 1);
                }
            }

            // Update UI
            tvTotalEvents.setText("Total Events: " + totalEvents);
            tvCompletedEvents.setText("Completed: " + completedEvents);
            tvUpcomingEvents.setText("Upcoming: " + upcomingEvents);

            // Category Breakdown
            StringBuilder categoryText = new StringBuilder("Category Breakdown:\n\n");
            for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
                categoryText.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\n");
            }
            tvCategoryBreakdown.setText(categoryText.toString());

            // Priority Stats
            StringBuilder priorityText = new StringBuilder("Priority Distribution:\n\n");
            for (Map.Entry<String, Integer> entry : priorityCount.entrySet()) {
                priorityText.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\n");
            }
            tvPriorityStats.setText(priorityText.toString());

            progressBar.setVisibility(View.GONE);
        }, 500);
    }
}