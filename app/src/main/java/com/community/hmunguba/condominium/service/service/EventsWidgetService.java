package com.community.hmunguba.condominium.service.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.utils.Utils;
import java.util.ArrayList;

public class EventsWidgetService extends RemoteViewsService {
    public static final String TAG = EventsWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Bundle arguments = intent.getBundleExtra(getString(R.string.bundle_events_widget));
        ArrayList<Event> eventsList = arguments.getParcelableArrayList(getString(R.string.bundle_events_widget));

        return new EventsRemoteViewsFactory(this.getApplicationContext(), eventsList);
    }

    class EventsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private ArrayList<Event> mEvents;

        public EventsRemoteViewsFactory(Context context, ArrayList<Event> events) {
            mContext = context;
            mEvents = events;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataSetChanged");
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mEvents == null)
                return 0;
            return mEvents.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            int iconResource = Utils.getCorrectEventIcon(mEvents.get(i));

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.event_item_widget);
            views.setTextViewText(R.id.item_event_date, mEvents.get(i).getSimpleDate());
            views.setTextViewText(R.id.item_event_name, mEvents.get(i).getTitle());
            views.setImageViewResource(R.id.item_event_icon, iconResource);

            Intent intent = new Intent();
            Bundle arguments = new Bundle();
            arguments.putString(getString(R.string.bundle_event_date_key), mEvents.get(i).getSimpleDate());
            arguments.putParcelable(getString(R.string.bundle_event_key), mEvents.get(i));

            intent.putExtras(arguments);

            views.setOnClickFillInIntent(R.id.item_event_name, intent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
