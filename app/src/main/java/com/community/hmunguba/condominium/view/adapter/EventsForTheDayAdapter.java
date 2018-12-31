package com.community.hmunguba.condominium.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Event;

import java.util.List;

public class EventsForTheDayAdapter extends RecyclerView.Adapter<EventsForTheDayAdapter.EventsViewHolder> {
    private static final String TAG = EventsForTheDayAdapter.class.getSimpleName();
    private List<Event> mEvents;

    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        private ImageView eventItemIcon;
        private TextView eventItemDate;
        private TextView eventItemName;

        public EventsViewHolder(View view) {
            super(view);
            eventItemIcon = view.findViewById(R.id.item_event_icon);
            eventItemDate = view.findViewById(R.id.item_event_date);
            eventItemName = view.findViewById(R.id.item_event_name);
        }
    }

    public EventsForTheDayAdapter(List<Event> events) {
        Log.d(TAG, "events size = " + events.size());
       this.mEvents = events;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        int iconResource = getCorrectEventIcon(mEvents.get(position));
        holder.eventItemIcon.setBackgroundResource(iconResource);
        holder.eventItemDate.setText(mEvents.get(position).getSimpleDate());
        holder.eventItemName.setText(mEvents.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (mEvents == null) {
            return 0;
        }
        return mEvents.size();
    }

    private int getCorrectEventIcon(Event event) {
        if (event.getReservedArea().isHasGourmetArea()) {
            return R.drawable.ic_gourmet;
        } else if (event.getReservedArea().isHasPoolArea()) {
            return R.drawable.ic_pool;
        } else if (event.getReservedArea().isHasBarbecueArea()) {
            return R.drawable.ic_barbecue;
        } else if (event.getReservedArea().isHasMoviesArea()) {
            return R.drawable.ic_movies;
        } else if (event.getReservedArea().isHasPartyRoomArea()) {
            return R.drawable.ic_partyroom;
        } else {
            return R.drawable.ic_sports;
        }
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new EventsViewHolder(itemView);
    }

}
