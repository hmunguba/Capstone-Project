package com.community.hmunguba.condominium.service.model;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

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

public class FirebaseRepository<Model> {
    private static final String TAG = FirebaseRepository.class.getSimpleName();

    private final FirebaseDatabase mDatabase;
    private final DatabaseReference mRef;
    private static FirebaseRepository firebaseRepository;


    public FirebaseRepository() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    public synchronized static FirebaseRepository getInstance() {
        if (firebaseRepository == null) {
            firebaseRepository = new FirebaseRepository();
        }
        return firebaseRepository;
    }

    public void createNewCond(Condominium cond) {
        mRef.child("condominiums").child(cond.getCondId()).setValue(cond).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Add condominium to database with success!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error adding condominium to database.");
                    }
                });
    }

    public MutableLiveData<Condominium> queryCond(String condId) {
        final MutableLiveData<Condominium> cond = new MutableLiveData<>();
        Query query = mRef.child("condominiums").child(condId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "datasnaposhot = " + dataSnapshot);
                cond.setValue(dataSnapshot.getValue(Condominium.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
        return cond;
    }

    public MutableLiveData<List<String>> queryCondsNames() {
        final MutableLiveData<List<String>> data = new MutableLiveData<>();
        Query query = mRef.child("condominiums").orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> names = new ArrayList<>();

                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String name = childDataSnapshot.child("name").getValue(String.class);
                    names.add(name);
                }
                data.setValue(names);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<Boolean> checkIfCondExists(final String id) {
        Log.d(TAG, "checkIfCondExists");
        final MutableLiveData<Boolean> hasCondInDatabase = new MutableLiveData<>();

        Query query = mRef.child("condominiums").orderByChild("condId").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hasCondInDatabase.setValue(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
        return hasCondInDatabase;
    }

}
