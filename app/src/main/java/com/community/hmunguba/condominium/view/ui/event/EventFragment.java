package com.community.hmunguba.condominium.view.ui.event;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.event.DayEventFragment;
import com.community.hmunguba.condominium.viewmodel.EventViewModel;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;

import java.util.Date;
import java.util.List;

// https://github.com/developerVatsal/MyDynamicCalendarExample?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=5562

public class EventFragment extends Fragment {
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
        setupEvents();
        return view;
    }

    public void setupEvents() {
        String condId = Utils.getCondIdPreference(getContext());
        eventViewModel.loadAllEvents(condId).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                for (Event event : events) {
                    Log.d(TAG, "Seting up event " + event.getTitle());
                    calendarView.addEvent(event.getSimpleDate(), event.getStartTime()+":00",
                            event.getEndTime()+":00", event.getTitle());
                }
                calendarView.refreshCalendar();
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
        calendarView.setHeaderTextColor("#000000");
        calendarView.setNextPreviousIndicatorColor("#ffffff");
        calendarView.setWeekDayLayoutBackgroundColor("#965471");
        calendarView.setWeekDayLayoutTextColor("#ffffff");
        calendarView.setExtraDatesOfMonthBackgroundColor("#999999"); //light grey
        calendarView.setExtraDatesOfMonthTextColor("#000000");
        calendarView.setDatesOfMonthBackgroundColor("#ffffff");
        calendarView.setDatesOfMonthTextColor("#745632");
        calendarView.setCurrentDateBackgroundColor(R.color.black);
        calendarView.setCurrentDateTextColor("#f583f8"); //light pink
        calendarView.setBelowMonthEventTextColor("#425684");
        calendarView.setBelowMonthEventDividerColor("#635478");

        // set all saturday off(Holiday) - default value is false
        // isSaturdayOff(true/false, date_background_color, date_text_color);
        calendarView.isSaturdayOff(true, "#ffffff", "#000000");

        // set all sunday off(Holiday) - default value is false
        // isSundayOff(true/false, date_background_color, date_text_color);
        calendarView.isSundayOff(true, "#ffffff", "#000000");

        /** manage events */
        //calendarView.setEventCellBackgroundColor("#852365");
        calendarView.setEventCellTextColor("#425684");

        /** Manage holidays */
        calendarView.setHolidayCellBackgroundColor("#654248");
        calendarView.setHolidayCellTextColor("#d590bb");

        // set holiday date clickable true/false
        calendarView.setHolidayCellClickable(false);

        // Add event  -  addEvent(event_date, event_start_time, event_end_time, event_title)
        calendarView.addEvent("5-12-2018", "8:00", "8:15", "Today Event 1");
        // Add holiday  -  addHoliday(holiday_date)
        calendarView.addHoliday("25-12-2018");
    }

}
