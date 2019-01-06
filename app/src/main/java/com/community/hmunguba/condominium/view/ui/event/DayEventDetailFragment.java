package com.community.hmunguba.condominium.view.ui.event;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.CommonAreas;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;
import com.community.hmunguba.condominium.viewmodel.EventViewModel;

import java.util.Date;
import java.util.List;

public class DayEventDetailFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = DayEventDetailFragment.class.getSimpleName();

    private Context mContext;
    private EventViewModel eventViewModel;
    private String date;
    private String condId;
    private String eventSimpleDate;

    private TextView eventDay;
    private TextInputLayout eventNameInput;
    private TextInputLayout amountOfPeopleInput;
    private TextInputLayout startTimeInput;
    private TextInputLayout endTimeInput;
    private RadioGroup eventAreasRadioGroup;
    private RadioButton gourmetAreaRb;
    private RadioButton poolAreaRb;
    private RadioButton barbecueAreaRb;
    private RadioButton moviesAreaRb;
    private RadioButton partyRoomAreaRb;
    private RadioButton sportsAreaRb;
    private TextView commonAreasTv;
    private Button saveBtn;

    private String eventName;
    private String eventParticipants;
    private CommonAreas eventArea;
    private String startTime;
    private String endTime;

    public DayEventDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        Bundle arguments = getArguments();
        date = "";
        if (arguments.containsKey(getString(R.string.bundle_event_date_key))) {
            date = arguments.getString(getString(R.string.bundle_event_date_key));
        }

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        eventArea = new CommonAreas();
        getCondAvailableCommonAreas();

        condId = Utils.getCondIdPreference(getContext());
        eventSimpleDate = Utils.getSimpleDateAsString(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_event_detail, container, false);

        eventDay = view.findViewById(R.id.event_detail_day_tv);
        eventNameInput = view.findViewById(R.id.event_detail_name_til);
        amountOfPeopleInput = view.findViewById(R.id.event_detail_amount_of_people_til);
        eventAreasRadioGroup = view.findViewById(R.id.common_areas_radio_group);
        gourmetAreaRb = view.findViewById(R.id.event_details_gourmet_area_radio_btn);
        poolAreaRb = view.findViewById(R.id.event_details_gourmet_area_radio_btn);
        barbecueAreaRb = view.findViewById(R.id.event_details_barbecue_area_radio_btn);
        moviesAreaRb = view.findViewById(R.id.event_details_movies_area_radio_btn);
        partyRoomAreaRb = view.findViewById(R.id.event_details_party_room_area_radio_btn);
        sportsAreaRb = view.findViewById(R.id.event_details_sports_area_radio_btn);
        commonAreasTv = view.findViewById(R.id.no_common_areas_tv);
        startTimeInput = view.findViewById(R.id.event_detail_time_start);
        endTimeInput = view.findViewById(R.id.event_detail_time_end);
        saveBtn = view.findViewById(R.id.event_detail_add_event);

        eventDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
        eventDay.setHint(Utils.getSimpleDateAsString(date));
        eventDay.setInputType(InputType.TYPE_NULL);
        eventDay.setKeyListener(null);

        saveBtn.setOnClickListener(this);

        return view;
    }

    private void getCondAvailableCommonAreas() {
        String condId = Utils.getCondIdPreference(getContext());
        CondominiumViewModel condominiumViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);
        condominiumViewModel.loadCond(condId).observe(this, new Observer<Condominium>() {
            @Override
            public void onChanged(@Nullable Condominium condominium) {
                CommonAreas areas = condominium.getCommonAreas();
                if (areas.isHasGourmetArea()) {
                    gourmetAreaRb.setVisibility(View.VISIBLE);
                }
                if (areas.isHasPoolArea()) {
                    poolAreaRb.setVisibility(View.VISIBLE);
                }
                if (areas.isHasBarbecueArea()) {
                    barbecueAreaRb.setVisibility(View.VISIBLE);
                }
                if (areas.isHasMoviesArea()) {
                    moviesAreaRb.setVisibility(View.VISIBLE);
                }
                if (areas.isHasPartyRoomArea()) {
                    partyRoomAreaRb.setVisibility(View.VISIBLE);
                }
                if (areas.isHasSportsCourtArea()) {
                    sportsAreaRb.setVisibility(View.VISIBLE);
                }
                if (!areas.isHasGourmetArea() && !areas.isHasPoolArea() && !areas.isHasBarbecueArea()
                        && !areas.isHasMoviesArea() && !areas.isHasPartyRoomArea()
                        && !areas.isHasSportsCourtArea()) {
                    commonAreasTv.setVisibility(View.VISIBLE);
                } else {
                    commonAreasTv.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.event_detail_add_event) {
            if (checkAllRequiredFields()) {
                Event event = createEvent();
                checkHasEventDayPlaceSamePlace(event);
            } else {
                Toast.makeText(mContext, R.string.insert_all_required_fiels_toast,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkHasEventDayPlaceSamePlace(final Event newEvent) {
        Log.d(TAG, "Checking if already exists an event at the same day and same area");

        eventViewModel.queryEventsForDay(condId, eventSimpleDate).observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                boolean foundEvent = false;
                if (events.size() > 0) {
                    for (int i = 0; i < events.size(); i++) {
                        Log.d(TAG, "checking event " + events.get(i).getTitle());
                        if (newEvent.hasSameCommonAreas(events.get(i)) &&
                                !newEvent.getEventId().equals(events.get(i).getEventId())) {
                            foundEvent = true;
                            break;
                        }
                    }
                }
                if (!foundEvent) {
                    addEventToServer(newEvent);
                } else {
                    Toast.makeText(getContext(), R.string.event_same_day_same_place, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Event createEvent() {
        Date eventDate = Utils.getDateFromString(date);
        String eventId = eventSimpleDate + "_" + eventName;

        String createdBy = FirebaseUserAuthentication.getInstance().getUserEmail();
        Event event = new Event(eventId, createdBy, eventName, eventDate, eventSimpleDate,
                Integer.parseInt(eventParticipants), eventArea, startTime, endTime, condId);
        return event;
    }

    public void addEventToServer(Event event) {
        eventViewModel.createEvent(event).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getContext(), getString(R.string.event_created_success),
                            Toast.LENGTH_SHORT).show();
                    backToEventsFragment();
                } else {
                    Toast.makeText(getContext(), getString(R.string.event_created_fail),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void backToEventsFragment() {
        EventFragment eventFragment = new EventFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.event_container, eventFragment);
        ft.commit();
    }

    private boolean checkAllRequiredFields() {
        eventName = eventNameInput.getEditText().getText().toString();
        eventParticipants = amountOfPeopleInput.getEditText().getText().toString();
        startTime = startTimeInput.getEditText().getText().toString();
        endTime = endTimeInput.getEditText().getText().toString();

        if (!eventName.isEmpty() && !eventParticipants.isEmpty() && eventArea != null
                && !startTime.isEmpty() && !endTime.isEmpty() && radioButtonIsChecked()) {
            return true;
        }
        return false;
    }

    public boolean radioButtonIsChecked() {
        if (eventAreasRadioGroup.getCheckedRadioButtonId() == -1) {
            return false;
        }
        if (gourmetAreaRb.isChecked()) {
            eventArea.setOnlyGourmetArea();
        } else if (poolAreaRb.isChecked()) {
            eventArea.setOnlyPoolArea();
        } else if (barbecueAreaRb.isChecked()) {
            eventArea.setOnlyBarbecueArea();
        } else if (moviesAreaRb.isChecked()) {
            eventArea.setOnlyMoviesArea();
        } else if (partyRoomAreaRb.isChecked()) {
            eventArea.setOnlyPartyRoomArea();
        } else if (sportsAreaRb.isChecked()) {
            eventArea.setOnlySportsCourtArea();
        }
        return true;
    }

}
