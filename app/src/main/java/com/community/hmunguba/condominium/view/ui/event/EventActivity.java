package com.community.hmunguba.condominium.view.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.community.hmunguba.condominium.R;

public class EventActivity extends AppCompatActivity {
    private static final String TAG = EventActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        EventFragment eventFragment = new EventFragment();
        ft.add(R.id.event_container, eventFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_events_action:
                Log.d(TAG, "starting my events");
                startListEventIntent(EventsListActivity.MY_EVENTS);
                return true;
            case R.id.all_events_action:
                Log.d(TAG, "starting all events");
                startListEventIntent(EventsListActivity.ALL_EVENTS);
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void startListEventIntent(int type) {
        Bundle arguments = new Bundle();
        arguments.putInt(getString(R.string.bundle_event_type_event), type);
        Intent listEventIntent = new Intent(EventActivity.this, EventsListActivity.class);
        listEventIntent.putExtras(arguments);
        startActivity(listEventIntent);
    }
}
