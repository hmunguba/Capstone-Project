package com.community.hmunguba.condominium.service.model;

import android.support.annotation.NonNull;
import android.util.Log;

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

    protected FirebaseRepositoryCallback<Model> firebaseRepositoryCallback;

    public FirebaseRepository() {
        Log.d(TAG, "constructor of FirebaseRepository");

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    public void addListener(FirebaseRepositoryCallback<Model> firebaseCallback) {
        this.firebaseRepositoryCallback = firebaseCallback;
    }

    public List<Condominium> queryConds() {
        Log.d(TAG, "queryConds");
        final List<Condominium> list = new ArrayList<>();

        Query query = mRef.child("condominiums").orderByChild("name");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "datasnaposhot = " + dataSnapshot);
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "childDataSnapshot = " + childDataSnapshot);
                    Condominium cond = childDataSnapshot.getValue(Condominium.class);
                    list.add(cond);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
        return list;
    }

    public List<String> makeQueryByChild(String child, final String param) {
        final List<String> list = new ArrayList<>();
        Query query = mRef.child(child).orderByChild(param);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "-->" + childDataSnapshot.child(param).getValue());
                    String data = childDataSnapshot.child(param).getValue().toString();
                    list.add(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
        return list;
    }

    public interface FirebaseRepositoryCallback<Model> {
        void onSuccess(List<Model> result);

        void onError(Exception e);
    }

}
