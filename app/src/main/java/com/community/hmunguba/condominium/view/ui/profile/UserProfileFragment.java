package com.community.hmunguba.condominium.view.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.model.User;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserProfileFragment extends Fragment {

    private static final String TAG = UserProfileFragment.class.getSimpleName();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private Context mContext;

    private EditText firstNameEt;
    private EditText lastNameEt;
    private EditText houseNumberEt;
    private EditText phoneNumberEt;
    private EditText emailEt;
    private Spinner condOptionsSp;
    private Button saveBtn;

    private CondominiumViewModel condViewModel;

    public UserProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mContext = getActivity();

        condViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);
        condViewModel.getConds().observe(this, new Observer<List<Condominium>>() {
            @Override
            public void onChanged(@Nullable List<Condominium> condominiums) {
                if (condominiums != null || condominiums.size() < 1) {
                    Log.d(TAG, "condominiuns is NOT null");
                    List<String> condNames = null;

                    for (Condominium c : condominiums) {
                        Log.d(TAG, "name is " + c.getName());
                        condNames.add(c.getName());
                    }
                    //populateCondSpinner(condNames);
                } else {
                    Log.d(TAG, "condominiuns is null or empty");
                }
            }


        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        firstNameEt = rootView.findViewById(R.id.resident_first_name_et);
        lastNameEt = rootView.findViewById(R.id.resident_last_name_et);
        houseNumberEt = rootView.findViewById(R.id.resident_house_number_et);
        phoneNumberEt = rootView.findViewById(R.id.resident_phone_et);
        emailEt = rootView.findViewById(R.id.resident_email_et);
        condOptionsSp = rootView.findViewById(R.id.resident_cond_options);
        saveBtn = rootView.findViewById(R.id.resident_ok_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedCond = condOptionsSp.getSelectedItem().toString();
                Log.d(TAG, "selected cond is " + selectedCond);
            }
        });

        return rootView;
    }

    public void populateCondSpinner(List<String> condsNames) {
        Log.e(TAG, "populateCondSpinner");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, condsNames);
        condOptionsSp.setAdapter(spinnerAdapter);
    }

    private void writeNewUser(String userId, String firstName, String lastName,
                              Condominium condominium, String profilePic, int houseNumber,
                              String phone, String email) {
        User user = new User(userId, firstName, lastName, condominium, profilePic, houseNumber,
                phone, email);

        mRef.child("users").child(userId).setValue(user);

    }
}
