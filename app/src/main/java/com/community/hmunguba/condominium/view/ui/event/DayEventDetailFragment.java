package com.community.hmunguba.condominium.view.ui.event;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.CommonAreas;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.model.repo.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.viewmodel.EventViewModel;

import java.util.Date;

public class DayEventDetailFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = DayEventDetailFragment.class.getSimpleName();

    private Context mContext;
    private EventViewModel eventViewModel;
    private String date;

    private EditText eventDayEt;
    private EditText eventNameEt;
    private EditText amountOfPeopleEt;
    private CheckBox gourmetAreaCb;
    private CheckBox poolAreaCb;
    private CheckBox barbecueAreaCb;
    private CheckBox moviesAreaCb;
    private CheckBox partyRoomAreaCb;
    private CheckBox sportsAreaCb;
    private EditText startTimeEt;
    private EditText endTimeEt;
    private Button saveBtn;

    private String eventDay;
    private String eventName;
    private String eventParticipants;
    private CommonAreas eventAreas;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_event_detail, container, false);

        eventDayEt = view.findViewById(R.id.event_detail_day_ed);
        eventNameEt = view.findViewById(R.id.event_detail_name_ed);
        amountOfPeopleEt = view.findViewById(R.id.event_detail_amount_of_people_ed);
        gourmetAreaCb = view.findViewById(R.id.event_details_gourmet_area_check_box);
        poolAreaCb = view.findViewById(R.id.event_details_pool_area_check_box);
        barbecueAreaCb = view.findViewById(R.id.event_details_barbecue_area_check_box);
        moviesAreaCb = view.findViewById(R.id.event_details_movies_area_check_box);
        partyRoomAreaCb = view.findViewById(R.id.event_details_party_room_area_check_box);
        sportsAreaCb = view.findViewById(R.id.event_details_sports_area_check_box);
        startTimeEt = view.findViewById(R.id.event_detail_time_start);
        endTimeEt = view.findViewById(R.id.event_detail_time_end);
        saveBtn = view.findViewById(R.id.event_detail_add_event);

        eventDayEt.setHint(date);
        eventDayEt.setInputType(InputType.TYPE_NULL);
        eventDayEt.setKeyListener(null);

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
        String eventId = Utils.getSimpleDateAsString(date) + "_" + eventName;

        String createdBy = FirebaseUserAuthentication.getInstance().getUserEmail();
        Event event = new Event(eventId, createdBy, eventName, eventDate,
                Integer.parseInt(eventParticipants), eventAreas, startTime, endTime);

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
        eventName = eventNameEt.getText().toString();
        eventParticipants = amountOfPeopleEt.getText().toString();
        eventAreas = getCheckedAreas();
        startTime = startTimeEt.getText().toString();
        endTime = endTimeEt.getText().toString();

        if (!eventName.isEmpty() && !eventParticipants.isEmpty() && eventAreas != null
                && !startTime.isEmpty() && !endTime.isEmpty()) {
            return true;
        }
        return false;
    }

    private CommonAreas getCheckedAreas() {
        eventAreas = new CommonAreas();

        eventAreas.setHasGourmetArea(gourmetAreaCb.isChecked());
        eventAreas.setHasPoolArea(poolAreaCb.isChecked());
        eventAreas.setHasBarbecueArea(barbecueAreaCb.isChecked());
        eventAreas.setHasMoviesArea(moviesAreaCb.isChecked());
        eventAreas.setHasPartyRoomArea(partyRoomAreaCb.isChecked());
        eventAreas.setHasSportsCourtArea(sportsAreaCb.isChecked());
        return eventAreas;
    }
}
