package com.community.hmunguba.condominium.service.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.view.widget.CondominiumWidgetProvider;

import java.util.ArrayList;

public class LoadEventsService extends IntentService {
    public static final String TAG = LoadEventsService.class.getSimpleName();
    private static final String ACTION_UPDATE_EVENTS_WIDGET =
            "com.community.hmunguba.condominium.action.update_events_widget";

    public LoadEventsService() {
        super(TAG);
    }

    public static void startActionLoadEventsService(final Context context, ArrayList<Event> events) {
        Log.d(TAG, "startActionLoadEventsService");

        if (events != null && events.size() > 0) {
            Intent loadEventsIntent = new Intent(context, LoadEventsService.class);
            loadEventsIntent.setAction(ACTION_UPDATE_EVENTS_WIDGET);
            loadEventsIntent.putParcelableArrayListExtra(context.getString(R.string.bundle_events_widget), events);
            context.startService(loadEventsIntent);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            ArrayList<Event> events = intent.getParcelableArrayListExtra(getString(R.string.bundle_events_widget));

            String action = intent.getAction();
            if (action.equals(ACTION_UPDATE_EVENTS_WIDGET)) {
                handleActionUpdateEvents(events);
            }
        }
    }

    private void handleActionUpdateEvents(ArrayList<Event> events) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, CondominiumWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_events_lv);
        CondominiumWidgetProvider.updateEventsWidgets(this, appWidgetManager, appWidgetIds, events);
    }
}
