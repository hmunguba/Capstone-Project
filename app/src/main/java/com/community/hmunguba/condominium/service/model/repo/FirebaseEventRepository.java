package com.community.hmunguba.condominium.service.model.repo;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseEventRepository {
    private static final String TAG = FirebaseEventRepository.class.getSimpleName();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private static FirebaseEventRepository firebaseEventRepository;

    public FirebaseEventRepository() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    public synchronized static FirebaseEventRepository getInstance() {
        if (firebaseEventRepository == null) {
            firebaseEventRepository = new FirebaseEventRepository();
        }
        return firebaseEventRepository;
    }

    public MutableLiveData<Boolean> createNewEvent(Event event) {
        final MutableLiveData<Boolean> isEventCreated = new MutableLiveData<>();

        Log.d(TAG, "event id = " + event.getEventId());
        mRef.child("events").child(event.getEventId()).setValue(event).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Event created with success!");
                        isEventCreated.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              e.printStackTrace();
                                              Log.e(TAG, "Error creating event.");
                                              isEventCreated.setValue(false);
                                          }
                                      }
                );
        return isEventCreated;
    }

    public MutableLiveData<List<Event>> queryAllEvents() {
        final MutableLiveData<List<Event>> data = new MutableLiveData<>();
        Query query = mRef.child("events");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> events = new ArrayList<>();

                Log.d(TAG, "datasnaposhot = " + dataSnapshot);
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Event event = childDataSnapshot.getValue(Event.class);
                    events.add(event);
                }
                data.setValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<List<Event>> queryEventsByMonth(String month, final String year) {
        final MutableLiveData<List<Event>> data = new MutableLiveData<>();
        Query query = mRef.child("events").child(month);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> events = new ArrayList<>();

                Log.d(TAG, "datasnaposhot = " + dataSnapshot);
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String childYear = childDataSnapshot.child("year").getValue(String.class);
                    if (childYear.equals(year)) {
                        Event event = childDataSnapshot.getValue(Event.class);
                        events.add(event);
                    }
                }
                data.setValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<List<Event>> queryEventsByDay(String day, final String month, final String year) {
        final MutableLiveData<List<Event>> data = new MutableLiveData<>();
        Query query = mRef.child("events").child(day);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> events = new ArrayList<>();

                Log.d(TAG, "datasnaposhot = " + dataSnapshot);
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String childYear = childDataSnapshot.child("year").getValue(String.class);
                    String childMonth = childDataSnapshot.child("month").getValue(String.class);
                    if (childYear.equals(year) && childMonth.equals(month)) {
                        Event event = childDataSnapshot.getValue(Event.class);
                        events.add(event);
                    }
                }
                data.setValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<Event> queryEvent(String eventId) {
        final MutableLiveData<Event> event = new MutableLiveData<>();
        Query query = mRef.child("events").child(eventId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "datasnaposhot = " + dataSnapshot);
                event.setValue(dataSnapshot.getValue(Event.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                event.setValue(null);
            }
        });
        return event;
    }

    public MutableLiveData<Boolean> deleteEvent(String id) {
        final MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();

        Query query = mRef.child("events").orderByChild("eventId").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    data.getRef().removeValue();
                }
                isDeleted.setValue(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
                isDeleted.setValue(false);
            }
        });
        return isDeleted;
    }
}
