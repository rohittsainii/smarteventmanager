package com.example.smarteventmanager;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private EventDatabaseHelper dbHelper;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        this.dbHelper = new EventDatabaseHelper(context);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.tvTitle.setText(event.getTitle());
        holder.tvDescription.setText(event.getDescription());
        holder.tvDateTime.setText(event.getDate() + " at " + event.getTime());
        holder.tvCategory.setText(event.getCategory());
        holder.ratingBar.setRating(event.getPriority());
        holder.checkboxCompleted.setChecked(event.isCompleted());

        // Set priority color
        if (event.getPriority() >= 4) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
        } else if (event.getPriority() >= 2) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
        }

        // Checkbox listener
        holder.checkboxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            event.setCompleted(isChecked);
            dbHelper.updateEvent(event);
            Toast.makeText(context, isChecked ? "Marked as completed" : "Marked as pending",
                    Toast.LENGTH_SHORT).show();
        });

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetailActivity.class);
            intent.putExtra("eventId", event.getId());
            context.startActivity(intent);
        });

        // Long click for context menu
        holder.itemView.setOnLongClickListener(v -> {
            holder.currentEvent = event;
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        CardView cardView;
        TextView tvTitle, tvDescription, tvDateTime, tvCategory;
        RatingBar ratingBar;
        CheckBox checkboxCompleted;
        Event currentEvent;

        public EventViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            checkboxCompleted = itemView.findViewById(R.id.checkboxCompleted);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Event Options");
            menu.add(0, 1, 0, "Edit");
            menu.add(0, 2, 1, "Delete");
            menu.add(0, 3, 2, "Share");

            // Menu item click listener
            menu.getItem(0).setOnMenuItemClickListener(item -> {
                editEvent(currentEvent);
                return true;
            });

            menu.getItem(1).setOnMenuItemClickListener(item -> {
                deleteEvent(currentEvent);
                return true;
            });

            menu.getItem(2).setOnMenuItemClickListener(item -> {
                shareEvent(currentEvent);
                return true;
            });
        }

        private void editEvent(Event event) {
            Intent intent = new Intent(context, AddEventActivity.class);
            intent.putExtra("eventId", event.getId());
            intent.putExtra("isEdit", true);
            context.startActivity(intent);
        }

        private void deleteEvent(Event event) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        dbHelper.deleteEvent(event.getId());
                        eventList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        private void shareEvent(Event event) {
            String shareText = "Event: " + event.getTitle() + "\n" +
                    "Description: " + event.getDescription() + "\n" +
                    "Date: " + event.getDate() + "\n" +
                    "Time: " + event.getTime() + "\n" +
                    "Category: " + event.getCategory();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            context.startActivity(Intent.createChooser(shareIntent, "Share Event"));
        }
    }
}