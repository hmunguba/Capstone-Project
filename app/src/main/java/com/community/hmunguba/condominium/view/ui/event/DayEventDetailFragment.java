package com.community.hmunguba.condominium.view.ui.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.community.hmunguba.condominium.R;

public class DayEventDetailFragment extends Fragment {

    public DayEventDetailFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_event_detail, container, false);

        return view;
    }
}
