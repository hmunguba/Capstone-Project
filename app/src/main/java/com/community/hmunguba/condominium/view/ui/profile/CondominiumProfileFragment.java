package com.community.hmunguba.condominium.view.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.CommonAreas;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CondominiumProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = CondominiumProfileFragment.class.getSimpleName();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private Context mContext;

    private EditText nameEt;
    private EditText locationEt;
    private EditText numberEt;
    private EditText zipCodeEt;
    private Spinner stateSp;
    private EditText cityEt;
    private CheckBox gourmetAreaCb;
    private CheckBox poolAreaCb;
    private CheckBox barbecueAreaCb;
    private CheckBox moviesAreaCb;
    private CheckBox partyRoomAreaCb;
    private CheckBox sportsCourtAreaCb;
    private Button okBtn;

    private String condName;
    private String condLocation;
    private String condNumber;
    private String condZipCode;
    private String condState;
    private String condCity;

    public CondominiumProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cond_profile, container, false);

        nameEt = rootView.findViewById(R.id.cond_name_et);
        locationEt = rootView.findViewById(R.id.cond_location_et);
        numberEt = rootView.findViewById(R.id.cond_number_et);
        zipCodeEt = rootView.findViewById(R.id.cond_zipcode_et);
        stateSp = rootView.findViewById(R.id.cond_state_sp);
        cityEt = rootView.findViewById(R.id.cond_city_et);
        gourmetAreaCb = rootView.findViewById(R.id.gourmet_area_check_box);
        poolAreaCb = rootView.findViewById(R.id.pool_area_check_box);
        barbecueAreaCb = rootView.findViewById(R.id.barbecue_area_check_box);
        moviesAreaCb = rootView.findViewById(R.id.movies_area_check_box);
        partyRoomAreaCb = rootView.findViewById(R.id.party_room_area_check_box);
        sportsCourtAreaCb = rootView.findViewById(R.id.sports_area_check_box);
        okBtn = rootView.findViewById(R.id.ok_btn);

        okBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ok_btn) {
            if (hasAllRequiredFields()) {
                String id = "id_" + condName + "_" + condZipCode;
                condAlreadyExistsInDatabase(id);
            } else {
                Toast.makeText(mContext, "Not all required fields are filled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean hasAllRequiredFields() {
        condName = nameEt.getText().toString();
        condLocation = locationEt.getText().toString();
        condNumber = numberEt.getText().toString();
        condZipCode = zipCodeEt.getText().toString();
        condState = stateSp.getSelectedItem().toString();
        condCity = cityEt.getText().toString();

        if (condName != null && condLocation != null && condNumber != null
                && condZipCode != null && condState != null && condCity != null) {
            return true;
        } else {
            Toast.makeText(mContext, R.string.insert_all_required_fiels_toast, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private CommonAreas getCondCommonAreas() {
        CommonAreas commonAreas = new CommonAreas();
        commonAreas.setHasGourmetArea(gourmetAreaCb.isChecked());
        commonAreas.setHasPoolArea(poolAreaCb.isChecked());
        commonAreas.setHasBarbecueArea(barbecueAreaCb.isChecked());
        commonAreas.setHasMoviesArea(moviesAreaCb.isChecked());
        commonAreas.setHasPartyRoomArea(partyRoomAreaCb.isChecked());
        commonAreas.setHasSportsCourtArea(sportsCourtAreaCb.isChecked());

        return commonAreas;
    }

    //TODO: Verify if all fields are filled
    private void writeNewCondominium(String condId, String name, String profilePic, String location,
                                     String number, String zipCode, String state, String city,
                                     CommonAreas commonAreas) {

        Condominium cond = new Condominium(condId, name, profilePic, location, number, zipCode,
                state, city, commonAreas);

        mRef.child("condominiums").child(condId).setValue(cond).addOnSuccessListener(
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

    private void condAlreadyExistsInDatabase(final String id) {
        Log.d(TAG, "check condAlreadyExistsInDatabase");

        final DatabaseReference condRef = mDatabase.getReference("condominiums");
        Query query = condRef.orderByChild("condId").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e(TAG, "Database already exists");
                    Toast.makeText(mContext, "Condominium already exists",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Is a new condominium");
                    writeNewCondominium(id, condName, null, condLocation, condNumber,
                            condZipCode, condState, condCity, getCondCommonAreas());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }
}
