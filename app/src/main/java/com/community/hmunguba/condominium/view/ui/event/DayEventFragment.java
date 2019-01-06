package com.community.hmunguba.condominium.view.ui.event;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.adapter.EventsForTheDayAdapter;
import com.community.hmunguba.condominium.viewmodel.EventViewModel;

import java.util.ArrayList;
import java.util.List;

public class DayEventFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = DayEventFragment.class.getSimpleName();

    private TextView scheduledEventsTv;
    private RecyclerView eventsRecyclerView;
    private Button addEventBtn;

    private String fullDate;
    private String simpleDate;
    private EventViewModel eventViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        fullDate = "";
        if (arguments.containsKey(getString(R.string.bundle_event_date_key))) {
            fullDate = arguments.getString(getString(R.string.bundle_event_date_key));
        }

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        simpleDate = Utils.getSimpleDateAsString(fullDate);
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

        return view;
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
        RecyclerView.Adapter adapter = new EventsForTheDayAdapter(events);
        eventsRecyclerView.setLayoutManager(layoutManager);
        eventsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_event_btn) {
            Bundle args = new Bundle();
            args.putString(getString(R.string.bundle_event_date_key), fullDate.toString());

            DayEventDetailFragment dayEventDetailFragment = new DayEventDetailFragment();
            dayEventDetailFragment.setArguments(args);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.event_container, dayEventDetailFragment);
            ft.commit();
        }
    }
}
