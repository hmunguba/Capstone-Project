package com.community.hmunguba.condominium.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.service.EventsWidgetService;
import com.community.hmunguba.condominium.service.service.LoadEventsService;
import com.community.hmunguba.condominium.view.ui.event.DayEventDetailActivity;
import com.community.hmunguba.condominium.view.ui.event.EventActivity;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class CondominiumWidgetProvider extends AppWidgetProvider {
    private static final String TAG = CondominiumWidgetProvider.class.getSimpleName();

    private static ArrayList<Event> mEvents;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<Event> events) {

        Log.d(TAG, "updating widget");

        Intent calendarIntent = new Intent(context, EventActivity.class);
        PendingIntent btnPendingIntent = PendingIntent.getActivity(context, 0, calendarIntent, 0);
        Intent eventsDetailIntent = new Intent(context, DayEventDetailActivity.class);
        PendingIntent eventsDetailPendingIntent = PendingIntent.getActivity(context, 0, eventsDetailIntent, 0);

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.condominium_widget_provider);
        views.setTextViewText(R.id.appwidget_title, widgetText);
        views.setOnClickPendingIntent(R.id.load_events_widget_btn, btnPendingIntent);
        views.setPendingIntentTemplate(R.id.widget_events_lv, eventsDetailPendingIntent);

        mEvents = events;

        if (mEvents != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelableArrayList(context.getString(R.string.bundle_events_widget), mEvents);

            Intent serviceIntent = new Intent(context, EventsWidgetService.class);
            serviceIntent.putExtra(context.getString(R.string.bundle_events_widget), arguments);

            if (mEvents.size() > 0) {
                views.setViewVisibility(R.id.load_events_widget_btn, View.GONE);
                views.setViewVisibility(R.id.widget_no_event_tv, View.GONE);
                views.setViewVisibility(R.id.widget_events_lv, View.VISIBLE);
                views.setRemoteAdapter(R.id.widget_events_lv, serviceIntent);
            } else {
                views.setViewVisibility(R.id.load_events_widget_btn, View.GONE);
                views.setViewVisibility(R.id.widget_no_event_tv, View.VISIBLE);
                views.setViewVisibility(R.id.widget_events_lv, View.GONE);
            }

        } else {
            views.setViewVisibility(R.id.load_events_widget_btn, View.VISIBLE);
            views.setViewVisibility(R.id.widget_no_event_tv, View.GONE);
            views.setViewVisibility(R.id.widget_events_lv, View.GONE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateEventsWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, ArrayList<Event> events) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, events);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG, "onUpdate");
        LoadEventsService.startActionLoadEventsService(context, mEvents);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, mEvents);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d(TAG, "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(TAG, "onDisabled");
    }
}

