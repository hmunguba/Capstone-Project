package com.community.hmunguba.condominium.view.ui.event;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.adapter.EventsForTheDayAdapter;
import com.community.hmunguba.condominium.viewmodel.EventViewModel;

import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends AppCompatActivity {
    private static final String TAG = EventsListActivity.class.getSimpleName();
    public static final int ALL_EVENTS = 0;
    public static final int MY_EVENTS = 1;

    private TextView titleTv;
    private RecyclerView eventsListRv;

    private EventViewModel eventViewModel;
    private ArrayList<Event> allEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        Bundle arguments = getIntent().getExtras();
        int eventType = -1;
        if (arguments.containsKey(getString(R.string.bundle_event_type_event))) {
            eventType = arguments.getInt(getString(R.string.bundle_event_type_event));
        }

        titleTv = findViewById(R.id.list_events_title);
        eventsListRv = findViewById(R.id.events_rv);

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        setupEvents(eventType);
    }

    public void setupEvents(final int type) {
        Log.d(TAG, "setting up events");
        allEvents = new ArrayList<>();
        String condId = Utils.getCondIdPreference(getApplicationContext());
        eventViewModel.loadAllEvents(condId).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                allEvents.addAll(events);

                if (type == ALL_EVENTS) {
                    if (allEvents.size() <= 0) {
                        titleTv.setText(getString(R.string.no_scheduled_events));
                        eventsListRv.setVisibility(View.GONE);
                    } else {
                        titleTv.setText(getString(R.string.all_events));
                        setupEventRecyclerView(allEvents);
                        eventsListRv.setVisibility(View.VISIBLE);
                    }

                } else if (type == MY_EVENTS){
                    ArrayList<Event> myEvents = getOnlyMyEvents();
                    if (myEvents.size() <= 0) {
                        titleTv.setText(getString(R.string.no_scheduled_events));
                        eventsListRv.setVisibility(View.GONE);
                    } else {
                        titleTv.setText(getString(R.string.my_events));
                        setupEventRecyclerView(myEvents);
                        eventsListRv.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public ArrayList<Event> getOnlyMyEvents() {
        ArrayList<Event> myEvents = new ArrayList<>();
        String userId = FirebaseUserAuthentication.getInstance().getUserEmail();

        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getCreatedBy().equals(userId)) {
                myEvents.add(allEvents.get(i));
            }
        }
        return myEvents;
    }

    private void setupEventRecyclerView(List<Event> events) {
        Log.d(TAG, "setupEventRecyclerView");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.Adapter adapter = new EventsForTheDayAdapter(events);
        eventsListRv.setLayoutManager(layoutManager);
        eventsListRv.setAdapter(adapter);
    }

}