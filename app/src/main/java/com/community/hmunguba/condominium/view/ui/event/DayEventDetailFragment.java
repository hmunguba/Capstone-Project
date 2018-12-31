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
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.CommonAreas;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.viewmodel.EventViewModel;

import java.util.Date;

public class DayEventDetailFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = DayEventDetailFragment.class.getSimpleName();

    private Context mContext;
    private EventViewModel eventViewModel;
    private String date;

    private TextInputLayout eventDayInput;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_event_detail, container, false);

        eventDayInput = view.findViewById(R.id.event_detail_day_til);
        eventNameInput = view.findViewById(R.id.event_detail_name_til);
        amountOfPeopleInput = view.findViewById(R.id.event_detail_amount_of_people_til);
        eventAreasRadioGroup = view.findViewById(R.id.common_areas_radio_group);
        gourmetAreaRb = view.findViewById(R.id.event_details_gourmet_area_radio_btn);
        poolAreaRb = view.findViewById(R.id.event_details_gourmet_area_radio_btn);
        barbecueAreaRb = view.findViewById(R.id.event_details_barbecue_area_radio_btn);
        moviesAreaRb = view.findViewById(R.id.event_details_movies_area_radio_btn);
        partyRoomAreaRb = view.findViewById(R.id.event_details_party_room_area_radio_btn);
        sportsAreaRb = view.findViewById(R.id.event_details_sports_area_radio_btn);
        startTimeInput = view.findViewById(R.id.event_detail_time_start);
        endTimeInput = view.findViewById(R.id.event_detail_time_end);
        saveBtn = view.findViewById(R.id.event_detail_add_event);

        eventDayInput.setHint(date);
        eventDayInput.getEditText().setInputType(InputType.TYPE_NULL);
        eventDayInput.getEditText().setKeyListener(null);

        saveBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.event_detail_add_event) {
            if (checkAllRequiredFields()) {
                createEvent();
            } else {
                Toast.makeText(mContext, R.string.insert_all_required_fiels_toast,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO: Autocomplete event Day

    private void createEvent() {
        Date eventDate = Utils.getDateFromString(date);
        String eventSimpleDate = Utils.getSimpleDateAsString(date);
        String eventId = eventSimpleDate + "_" + eventName;

        String createdBy = FirebaseUserAuthentication.getInstance().getUserEmail();
        Event event = new Event(eventId, createdBy, eventName, eventDate, eventSimpleDate,
                Integer.parseInt(eventParticipants), eventArea, startTime, endTime);

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
