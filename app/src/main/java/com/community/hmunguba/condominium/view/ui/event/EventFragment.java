package com.community.hmunguba.condominium.view.ui.event;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.service.LoadEventsService;
import com.community.hmunguba.condominium.service.utils.ConnectionReceiver;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.event.DayEventFragment;
import com.community.hmunguba.condominium.view.ui.login.LoginActivity;
import com.community.hmunguba.condominium.viewmodel.EventViewModel;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// https://github.com/developerVatsal/MyDynamicCalendarExample?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=5562

public class EventFragment extends Fragment implements ConnectionReceiver.ConnectionReceiverListener {
    private static final String TAG = EventFragment.class.getSimpleName();

    private Context mContext;
    private MyDynamicCalendar calendarView;
    private EventViewModel eventViewModel;

    public EventFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_event, container, false);
        calendarView = view.findViewById(R.id.calendar_events);

        initCalendarView();
        checkConnection();
        checkSetupEvents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectionReceiver.connectionListener = this;
    }

    public void checkSetupEvents() {
        if (FirebaseUserAuthentication.getInstance().hasCurrentUser()) {
            setupEvents();
        } else {
            Intent startLoginActivity = new Intent(getContext(), LoginActivity.class);
            startActivity(startLoginActivity);
        }

    }

    public void setupEvents() {
        String condId = Utils.getCondIdPreference(getContext());
        eventViewModel.loadAllEvents(condId).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                ArrayList<Event> allEvents = new ArrayList<>();

                for (Event event : events) {
                    Log.d(TAG, "Seting up event " + event.getTitle());
                    allEvents.add(event);
                    calendarView.addEvent(event.getSimpleDate(), event.getStartTime(),
                            event.getEndTime(), event.getTitle());
                }
                calendarView.refreshCalendar();
                LoadEventsService.startActionLoadEventsService(getContext(), allEvents);
            }
        });
    }

    private void initCalendarView() {
        calendarView.showMonthView();
        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Log.d("date clicked: ", String.valueOf(date));

                Bundle arguments = new Bundle();
                String simpleDate = Utils.getSimpleDateAsString(date.toString());
                arguments.putString(getString(R.string.bundle_event_date_key), simpleDate);

                DayEventFragment dayEventFragment = new DayEventFragment();
                dayEventFragment.setArguments(arguments);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.event_container, dayEventFragment);
                ft.addToBackStack(null);
                ft.commit();
            }

            @Override
            public void onLongClick(Date date) {
                Log.d("date", String.valueOf(date));
            }
        });

        calendarView.setCalendarBackgroundColor("#ffffff");
        calendarView.setHeaderBackgroundColor("#ffffff");
        calendarView.setHeaderTextColor("#3F51B5");
        calendarView.setNextPreviousIndicatorColor("#3F51B5");
        calendarView.setWeekDayLayoutBackgroundColor("#3F51B5");
        calendarView.setWeekDayLayoutTextColor("#ffffff");
        calendarView.setExtraDatesOfMonthBackgroundColor("#f6f4f6");
        calendarView.setExtraDatesOfMonthTextColor("#000000");
        calendarView.setDatesOfMonthBackgroundColor("#ffffff");
        calendarView.setDatesOfMonthTextColor("#3F51B5");
        calendarView.setCurrentDateBackgroundColor("#FF4081");
        calendarView.setCurrentDateTextColor("#ffffff");
        calendarView.setEventCellTextColor("#425684");
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
