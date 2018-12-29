package com.community.hmunguba.condominium.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Event;

import java.util.List;

public class EventsForTheDayAdapter extends RecyclerView.Adapter<EventsForTheDayAdapter.EventsViewHolder> {
    private static final String TAG = EventsForTheDayAdapter.class.getSimpleName();
    private List<Event> mEvents;

    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public EventsViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.item_event_name);
        }
    }

    public EventsForTheDayAdapter(List<Event> events) {
        Log.d(TAG, "events size = " + events.size());
       this.mEvents = events;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        holder.mTextView.setText(mEvents.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (mEvents == null) {
            return 0;
        }
        return mEvents.size();
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new EventsViewHolder(itemView);
    }

}
