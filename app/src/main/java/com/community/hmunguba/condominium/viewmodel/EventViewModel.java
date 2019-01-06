package com.community.hmunguba.condominium.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.Event;
import com.community.hmunguba.condominium.service.firebase.FirebaseEventRepository;

import java.util.List;

public class EventViewModel extends ViewModel {
    public static final String TAG = EventViewModel.class.getSimpleName();

    private MutableLiveData<Event> event;

    public LiveData<Event> getEventById(String id) {
        if (event == null) {
            event = new MutableLiveData<>();
            loadEvent(id);
        }
        return event;
    }

    public LiveData<Boolean> createEvent(Event event) {
        Log.d(TAG, "createEvent " + event.getTitle());
        LiveData<Boolean> isEventCreated =  FirebaseEventRepository.getInstance().createNewEvent(event);
        getEventById(event.getEventId());

        return isEventCreated;
    }

    public LiveData<Event> loadEvent(String eventId) {
        Log.d(TAG, "loadEvent " + eventId);
        event = FirebaseEventRepository.getInstance().queryEvent(eventId);

        return event;
    }

    public LiveData<List<Event>> loadAllEvents(String condId) {
        Log.d(TAG, "loadAllEvents");
        LiveData<List<Event>> eventsList =
                FirebaseEventRepository.getInstance().queryAllEvents(condId);

        return eventsList;
    }

    public LiveData<List<Event>> queryEventsForDay(String condId, String simpleDate) {
        Log.d(TAG, "queryEventsForDay");
        LiveData<List<Event>> eventsList =
                FirebaseEventRepository.getInstance().queryEventsForDay(condId, simpleDate);

        return eventsList;
    }

    public LiveData<Boolean> deleteEvent(String id) {
        Log.d(TAG, "deleteEvent + " + id);
        LiveData<Boolean> isEventDeleted = FirebaseEventRepository.getInstance().deleteEvent(id);

        return  isEventDeleted;
    }
}
