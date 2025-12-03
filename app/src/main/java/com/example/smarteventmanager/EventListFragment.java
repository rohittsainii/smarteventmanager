package com.example.smarteventmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private EventDatabaseHelper dbHelper;
    private List<Event> eventList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Initialize Views
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList = new ArrayList<>();
        adapter = new EventAdapter(getContext(), eventList);
        recyclerView.setAdapter(adapter);

        // Initialize Database
        dbHelper = new EventDatabaseHelper(getContext());

        // Load Events
        loadEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents();
    }

    public void loadEvents() {
        progressBar.setVisibility(View.VISIBLE);

        // Simulate loading delay
        new android.os.Handler().postDelayed(() -> {
            eventList.clear();
            eventList.addAll(dbHelper.getAllEvents());
            adapter.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);

            if (eventList.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    public void searchEvents(String query) {
        eventList.clear();
        eventList.addAll(dbHelper.searchEvents(query));
        adapter.notifyDataSetChanged();

        if (eventList.isEmpty()) {
            tvEmpty.setText("No events found");
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void filterByCategory(String category) {
        eventList.clear();
        eventList.addAll(dbHelper.getEventsByCategory(category));
        adapter.notifyDataSetChanged();

        if (eventList.isEmpty()) {
            tvEmpty.setText("No events in this category");
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}