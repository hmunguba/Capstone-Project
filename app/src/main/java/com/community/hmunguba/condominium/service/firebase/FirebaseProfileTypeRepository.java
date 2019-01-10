package com.community.hmunguba.condominium.service.firebase;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.ProfileType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseProfileTypeRepository<Model> {
    private static final String TAG = FirebaseCondRepository.class.getSimpleName();

    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mRef;
    private static FirebaseProfileTypeRepository firebaseProfileTypeRepository;


    public FirebaseProfileTypeRepository() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    public synchronized static FirebaseProfileTypeRepository getInstance() {
        if (firebaseProfileTypeRepository == null) {
            firebaseProfileTypeRepository = new FirebaseProfileTypeRepository();
        }
        return firebaseProfileTypeRepository;
    }

    public MutableLiveData<Boolean> createNewProfileType(ProfileType profileType) {
        final MutableLiveData<Boolean> isProfileTypeCreated = new MutableLiveData<>();

        mRef.child("profileType").child(profileType.getEmail()).setValue(profileType).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Add profile type to database with success!");
                        isProfileTypeCreated.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error adding profile type to database.");
                        isProfileTypeCreated.setValue(false);
                    }
                });
        return isProfileTypeCreated;
    }

    public MutableLiveData<ProfileType> queryProfileType(String userEmail) {
        final MutableLiveData<ProfileType> profileType = new MutableLiveData<>();
        Query query = mRef.child("profileType").child(userEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "datasnaposhot = " + dataSnapshot);
                profileType.setValue(dataSnapshot.getValue(ProfileType.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                profileType.setValue(null);
            }
        });
        return profileType;
    }
}
