package com.community.hmunguba.condominium.view.ui.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.community.hmunguba.condominium.R;

public class DayEventFragment extends Fragment implements View.OnClickListener {

    private TextView dateTv;
    private String date;
    private Button addEventBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        date = "teste";
        if (arguments.containsKey(getString(R.string.bundle_event_date_key))) {
            date = arguments.getString(getString(R.string.bundle_event_date_key));
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_event, container, false);
        dateTv = view.findViewById(R.id.date);
        addEventBtn = view.findViewById(R.id.add_event_btn);
        addEventBtn.setOnClickListener(this);

        updateResources(date);

        return view;
    }

    private void updateResources(String text) {
        Log.d("hadassa", "text is = " + text);
        dateTv.setText(text);
    }

    @Override
    public void onClick(View view) {
        Log.d("hadassa", "view clicked is: " + view);
        Log.d("hadassa", "view clicked is resources: " + view.getResources());
        Log.d("hadassa", "view clicked is id: " + view.getId());

        if (view.getId() == R.id.add_event_btn) {
            DayEventDetailFragment dayEventDetailFragment = new DayEventDetailFragment();

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.event_container, dayEventDetailFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
