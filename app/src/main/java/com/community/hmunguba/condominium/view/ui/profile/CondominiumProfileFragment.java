package com.community.hmunguba.condominium.view.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.community.hmunguba.condominium.view.ui.menu.MenuActivity;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private CondominiumViewModel condViewModel;
    private boolean isAlreadyChecked = false;

    public CondominiumProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mContext = getActivity();

        condViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);
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

    //TODO: normalize cond name before add its Id to database

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ok_btn) {
            if (hasAllRequiredFields()) {
                final String id = "id_" + condName + "_" + condZipCode;
                condViewModel.checkCondExist(id).observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (aBoolean && !isAlreadyChecked) {
                            Toast.makeText(getContext(), getString(R.string.cond_already_exists),
                                    Toast.LENGTH_SHORT).show();
                        } if(!aBoolean) {
                            writeNewCondominium(id, condName, null, condLocation, condNumber,
                                    condZipCode, condState, condCity, getCondCommonAreas());
                        }
                        isAlreadyChecked = true;
                    }
                });

            } else {
                Toast.makeText(mContext, getString(R.string.insert_all_required_fiels_toast),
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

        if (!condName.isEmpty() && !condLocation.isEmpty() && !condNumber.isEmpty() &&
                !condZipCode.isEmpty() && condState != null && !condCity.isEmpty()) {
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
    private void writeNewCondominium(final String condId, String name, String profilePic, String location,
                                     String number, String zipCode, String state, String city,
                                     CommonAreas commonAreas) {

        final Condominium cond = new Condominium(condId, name, profilePic, location, number, zipCode,
                state, city, commonAreas);
         condViewModel.createCond(cond).observe(this, new Observer<Boolean>() {
             @Override
             public void onChanged(@Nullable Boolean aBoolean) {
                 if (aBoolean) {
                     Toast.makeText(getContext(), getString(R.string.cond_created_success),
                             Toast.LENGTH_SHORT).show();
                     startMenuActivity();

                 } else {
                     Toast.makeText(getContext(), getString(R.string.cond_created_fail),
                             Toast.LENGTH_LONG).show();
                 }
             }
         });
    }

    private void startMenuActivity() {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        startActivity(intent);
    }
}
