package com.community.hmunguba.condominium.service.model.repo;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseResidentRepository<Model> {
    private static final String TAG = FirebaseCondRepository.class.getSimpleName();

    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mRef;
    private static FirebaseResidentRepository firebaseResidentRepository;

    public FirebaseResidentRepository() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    public synchronized static FirebaseResidentRepository getInstance() {
        if (firebaseResidentRepository == null) {
            firebaseResidentRepository = new FirebaseResidentRepository();
        }
        return firebaseResidentRepository;
    }

    public MutableLiveData<Boolean> createNewResident(User user) {
        final MutableLiveData<Boolean> isUserCreated = new MutableLiveData<>();

        mRef.child("residents").child(user.getUserId()).setValue(user).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Add user to database with success!");
                        isUserCreated.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error adding userm to database.");
                        isUserCreated.setValue(false);
                    }
                }
        );
        return isUserCreated;
    }

    public MutableLiveData<User> queryResident(String userId) {
        final MutableLiveData<User> user = new MutableLiveData<>();
        Query query = mRef.child("residents").child(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "datasnaposhot = " + dataSnapshot);
                user.setValue(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                user.setValue(null);
            }
        });
        return user;
    }

    public MutableLiveData<Boolean> deleteResident(String id) {
        final MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();

        Query query = mRef.child("residents").orderByChild("userId").equalTo(id);
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
