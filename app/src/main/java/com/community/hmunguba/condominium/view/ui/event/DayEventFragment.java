package com.community.hmunguba.condominium.view.ui.event;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.utils.ConnectionReceiver;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.adapter.EventsForTheDayAdapter;
import com.community.hmunguba.condominium.viewmodel.EventViewModel;

import java.util.List;

public class DayEventFragment extends Fragment implements View.OnClickListener,
        EventsForTheDayAdapter.OnEventClickListener, ConnectionReceiver.ConnectionReceiverListener {
    private static final String TAG = DayEventFragment.class.getSimpleName();

    private TextView scheduledEventsTv;
    private RecyclerView eventsRecyclerView;
    private Button addEventBtn;

    private String simpleDate = "";
    private EventViewModel eventViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments.containsKey(getString(R.string.bundle_event_date_key))) {
            simpleDate = arguments.getString(getString(R.string.bundle_event_date_key));
        }

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        loadEventsForThisDay();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_event, container, false);
        scheduledEventsTv = view.findViewById(R.id.scheduled_events_title);
        eventsRecyclerView = view.findViewById(R.id.events_for_the_day_rv);
        addEventBtn = view.findViewById(R.id.add_event_btn);

        addEventBtn.setOnClickListener(this);
        checkConnection();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectionReceiver.connectionListener = this;
    }

    public void loadEventsForThisDay() {
        String condId = Utils.getCondIdPreference(getContext());
        eventViewModel.queryEventsForDay(condId, simpleDate).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                if (events.size() > 0) {
                    scheduledEventsTv.setText(getString(R.string.scheduled_events));
                    eventsRecyclerView.setVisibility(View.VISIBLE);

                    setupEventRecyclerView(events);
                    for (Event event : events) {
                        Log.d(TAG, "loading event -> " + event.getTitle());
                    }
                } else {
                    //display no events sheduled
                    scheduledEventsTv.setText(getString(R.string.no_scheduled_events));
                }
            }
        });
    }

    private void setupEventRecyclerView(List<Event> events) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.Adapter adapter = new EventsForTheDayAdapter(events, this);
        eventsRecyclerView.setLayoutManager(layoutManager);
        eventsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onEventClick(View view, Event event) {
        Log.d(TAG, "Event clicked " + event.getTitle());

        Bundle arguments = new Bundle();
        arguments.putParcelable(getString(R.string.bundle_event_key), event);
        arguments.putString(getString(R.string.bundle_event_date_key), event.getSimpleDate());
        Intent createEventIntent = new Intent(getActivity(), DayEventDetailActivity.class);
        createEventIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        createEventIntent.putExtras(arguments);
        startActivity(createEventIntent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_event_btn) {
            startCreateEventIntent();
        }
    }

    private void startCreateEventIntent() {
        Bundle arguments = new Bundle();
        arguments.putString(getString(R.string.bundle_event_date_key), simpleDate);
        Intent createEventIntent = new Intent(getActivity(), DayEventDetailActivity.class);
        createEventIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        createEventIntent.putExtras(arguments);
        startActivity(createEventIntent);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Toast.makeText(getContext(), R.string.no_internet_for_loading_events_toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), R.string.loading_events_toast, Toast.LENGTH_SHORT).show();
        }
    }

    public void checkConnection() {
        boolean isConnected = ConnectionReceiver.isConnected(getContext());
        if (!isConnected) {
            Toast.makeText(getContext(), R.string.no_internet_for_loading_events_toast, Toast.LENGTH_SHORT).show();
        }
    }
}
