package com.community.hmunguba.condominium.view.ui.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.CommonAreas;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CondominiumProfileFragment extends Fragment {

    private static final String TAG = CondominiumProfileFragment.class.getSimpleName();
    private DatabaseReference mDatabase;

    public CondominiumProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cond_profile, container, false);
    }

    private void writeNewCondominium(String condId, String name, String profilePic, String location,
                                     int number, String zipCode, String state, String city,
                                     CommonAreas commonAreas) {
        Condominium cond = new Condominium(condId, name, profilePic, location, number, zipCode,
                state, city, commonAreas);

        mDatabase.child("condominiums").child(condId).setValue(cond);

    }
}
