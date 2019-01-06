package com.community.hmunguba.condominium.view.ui.event;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
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

public class DayEventDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DayEventDetailActivity.class.getSimpleName();

    private EventViewModel eventViewModel;
    private String date = "";
    private String condId;
    private String eventSimpleDate;
    private boolean eventIsSetup = false;

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

    public DayEventDetailActivity() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_event_detail);

        Bundle arguments = getIntent().getExtras();
        Event previouslyCreatedEvent = null;

        if (arguments.containsKey(getString(R.string.bundle_event_date_key))) {
            date = arguments.getString(getString(R.string.bundle_event_date_key));
        }
        if (arguments.containsKey(getString(R.string.bundle_event_key))) {
            eventIsSetup = true;
            previouslyCreatedEvent = arguments.getParcelable(getString(R.string.bundle_event_key));
        }

        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        condId = Utils.getCondIdPreference(getApplicationContext());
        eventSimpleDate = Utils.getSimpleDateAsString(date);

        eventArea = new CommonAreas();
        getCondAvailableCommonAreas();

        eventDay = findViewById(R.id.event_detail_day_tv);
        eventNameInput = findViewById(R.id.event_detail_name_til);
        amountOfPeopleInput = findViewById(R.id.event_detail_amount_of_people_til);
        eventAreasRadioGroup = findViewById(R.id.common_areas_radio_group);
        gourmetAreaRb = findViewById(R.id.event_details_gourmet_area_radio_btn);
        poolAreaRb = findViewById(R.id.event_details_gourmet_area_radio_btn);
        barbecueAreaRb = findViewById(R.id.event_details_barbecue_area_radio_btn);
        moviesAreaRb = findViewById(R.id.event_details_movies_area_radio_btn);
        partyRoomAreaRb = findViewById(R.id.event_details_party_room_area_radio_btn);
        sportsAreaRb = findViewById(R.id.event_details_sports_area_radio_btn);
        commonAreasTv = findViewById(R.id.no_common_areas_tv);
        startTimeInput = findViewById(R.id.event_detail_time_start);
        endTimeInput = findViewById(R.id.event_detail_time_end);
        saveBtn = findViewById(R.id.event_detail_add_event);

        eventDay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisabled));
        eventDay.setHint(Utils.getSimpleDateAsString(date));
        eventDay.setInputType(InputType.TYPE_NULL);
        eventDay.setKeyListener(null);

        saveBtn.setOnClickListener(this);

        if (eventIsSetup) {
            updateUi(previouslyCreatedEvent);
        }
    }

    // disabling fields that cannot be changed
    private void updateUi(Event event) {
        Log.d(TAG, "updatingUI event");
        if (event == null)
            return;

        eventNameInput.getEditText().setText(event.getTitle());
        amountOfPeopleInput.getEditText().setText(Integer.toString(event.getNumberOfParticipants()));
        startTimeInput.getEditText().setText(event.getStartTime());
        endTimeInput.getEditText().setText(event.getEndTime());
        gourmetAreaRb.setChecked(event.getReservedArea().isHasGourmetArea());
        poolAreaRb.setChecked(event.getReservedArea().isHasPoolArea());
        barbecueAreaRb.setChecked(event.getReservedArea().isHasBarbecueArea());
        moviesAreaRb.setChecked(event.getReservedArea().isHasMoviesArea());
        partyRoomAreaRb.setChecked(event.getReservedArea().isHasPartyRoomArea());
        sportsAreaRb.setChecked(event.getReservedArea().isHasSportsCourtArea());

        eventNameInput.getEditText().setFocusable(false);
        eventNameInput.getEditText().setInputType(InputType.TYPE_NULL);
        eventNameInput.getEditText().setTextColor(ContextCompat
                .getColor(getApplicationContext(), R.color.colorDisabled));

        amountOfPeopleInput.getEditText().setFocusable(false);
        amountOfPeopleInput.getEditText().setInputType(InputType.TYPE_NULL);
        amountOfPeopleInput.getEditText().setTextColor(ContextCompat
                .getColor(getApplicationContext(), R.color.colorDisabled));

        startTimeInput.getEditText().setFocusable(false);
        startTimeInput.getEditText().setInputType(InputType.TYPE_NULL);
        startTimeInput.getEditText().setTextColor(ContextCompat
                .getColor(getApplicationContext(), R.color.colorDisabled));

        endTimeInput.getEditText().setFocusable(false);
        endTimeInput.getEditText().setInputType(InputType.TYPE_NULL);
        endTimeInput.getEditText().setTextColor(ContextCompat
                .getColor(getApplicationContext(), R.color.colorDisabled));

        gourmetAreaRb.setEnabled(false);
        gourmetAreaRb.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisabled));

        poolAreaRb.setEnabled(false);
        poolAreaRb.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisabled));

        barbecueAreaRb.setEnabled(false);
        barbecueAreaRb.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisabled));

        moviesAreaRb.setEnabled(false);
        moviesAreaRb.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisabled));

        partyRoomAreaRb.setEnabled(false);
        partyRoomAreaRb.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisabled));

        sportsAreaRb.setEnabled(false);
        sportsAreaRb.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDisabled));

        saveBtn.setVisibility(View.GONE);
    }

    private void getCondAvailableCommonAreas() {
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
                Toast.makeText(getApplicationContext(), R.string.insert_all_required_fiels_toast,
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
                    Toast.makeText(getApplicationContext(), R.string.event_same_day_same_place,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Event createEvent() {
        String newEventId = eventSimpleDate + "_" + eventName;

        String createdBy = FirebaseUserAuthentication.getInstance().getUserEmail();
        Event event = new Event(newEventId, createdBy, eventName, eventSimpleDate,
                Integer.parseInt(eventParticipants), eventArea, startTime, endTime, condId);
        return event;
    }

    public void addEventToServer(Event event) {
        eventViewModel.createEvent(event).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getApplicationContext(), getString(R.string.event_created_success),
                            Toast.LENGTH_SHORT).show();
                    startEventActivity();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.event_created_fail),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void startEventActivity() {
        Intent eventCalendarIntent = new Intent(DayEventDetailActivity.this, EventActivity.class);
        startActivity(eventCalendarIntent);
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
