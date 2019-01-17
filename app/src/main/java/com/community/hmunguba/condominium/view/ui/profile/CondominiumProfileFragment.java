package com.community.hmunguba.condominium.view.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.model.CommonAreas;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.model.ProfileType;
import com.community.hmunguba.condominium.service.utils.ConnectionReceiver;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.menu.MenuActivity;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;
import com.community.hmunguba.condominium.viewmodel.ProfileTypeViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CondominiumProfileFragment extends Fragment implements View.OnClickListener,
        ConnectionReceiver.ConnectionReceiverListener {

    private static final String TAG = CondominiumProfileFragment.class.getSimpleName();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private Context mContext;

    private TextInputLayout nameInput;
    private TextInputLayout addressInput;
    private TextInputLayout numberInput;
    private TextInputLayout zipCodeInput;
    private TextInputLayout stateInput;
    private Spinner stateSp;
    private TextInputLayout cityInput;
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

    private static final String STATE_COND_NAME = "condName";
    private static final String STATE_COND_ADDRESS = "address";
    private static final String STATE_COND_NUMBER = "number";
    private static final String STATE_COND_STATE = "state";
    private static final String STATE_COND_ZIPCODE = "zipCode";
    private static final String STATE_COND_CITY = "city";
    private static final String STATE_GOURMET_AREA = "gourmetArea";
    private static final String STATE_POOL_AREA = "poolArea";
    private static final String STATE_BARBECUE_AREA = "barbecueArea";
    private static final String STATE_MOVIES_AREA = "moviesArea";
    private static final String STATE_PARTY_ROOM = "partyRoomArea";
    private static final String STATE_SPORTS_AREA = "sportsArea";

    public CondominiumProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mContext = getActivity();

        condViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);

        profileViewModelSetup();
        checkCondSetup();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cond_profile, container, false);

        nameInput = rootView.findViewById(R.id.cond_name_til);
        addressInput = rootView.findViewById(R.id.cond_address_til);
        numberInput = rootView.findViewById(R.id.cond_number_til);
        zipCodeInput = rootView.findViewById(R.id.cond_zipcode_til);
        stateSp = rootView.findViewById(R.id.cond_state_sp);
        stateInput = rootView.findViewById(R.id.cond_state_til);
        cityInput = rootView.findViewById(R.id.cond_city_til);
        gourmetAreaCb = rootView.findViewById(R.id.gourmet_area_check_box);
        poolAreaCb = rootView.findViewById(R.id.pool_area_check_box);
        barbecueAreaCb = rootView.findViewById(R.id.barbecue_area_check_box);
        moviesAreaCb = rootView.findViewById(R.id.movies_area_check_box);
        partyRoomAreaCb = rootView.findViewById(R.id.party_room_area_check_box);
        sportsCourtAreaCb = rootView.findViewById(R.id.sports_area_check_box);
        okBtn = rootView.findViewById(R.id.ok_btn);

        zipCodeInput.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(9)});

        zipCodeInput.getEditText().addTextChangedListener(new TextWatcher() {
            int prevL = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevL = zipCodeInput.getEditText().getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if ((prevL < length) && length == 5) {
                    editable.append("-");
                }
            }
        });

        okBtn.setOnClickListener(this);
        checkConnection();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_COND_NAME, nameInput.getEditText().getText().toString());
        outState.putString(STATE_COND_ADDRESS, addressInput.getEditText().getText().toString());
        outState.putString(STATE_COND_NUMBER, numberInput.getEditText().getText().toString());
        outState.putString(STATE_COND_ZIPCODE, zipCodeInput.getEditText().getText().toString());
        outState.putString(STATE_COND_CITY, cityInput.getEditText().getText().toString());
        outState.putBoolean(STATE_GOURMET_AREA, gourmetAreaCb.isChecked());
        outState.putBoolean(STATE_POOL_AREA, poolAreaCb.isChecked());
        outState.putBoolean(STATE_BARBECUE_AREA, barbecueAreaCb.isChecked());
        outState.putBoolean(STATE_MOVIES_AREA, moviesAreaCb.isChecked());
        outState.putBoolean(STATE_PARTY_ROOM, partyRoomAreaCb.isChecked());
        outState.putBoolean(STATE_SPORTS_AREA, sportsCourtAreaCb.isChecked());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            nameInput.getEditText().setText(savedInstanceState.getString(STATE_COND_NAME));
            addressInput.getEditText().setText(savedInstanceState.getString(STATE_COND_ADDRESS));
            numberInput.getEditText().setText(savedInstanceState.getString(STATE_COND_NUMBER));
            zipCodeInput.getEditText().setText(savedInstanceState.getString(STATE_COND_ZIPCODE));
            cityInput.getEditText().setText(savedInstanceState.getString(STATE_COND_CITY));
            gourmetAreaCb.setChecked(savedInstanceState.getBoolean(STATE_GOURMET_AREA));
            poolAreaCb.setChecked(savedInstanceState.getBoolean(STATE_POOL_AREA));
            barbecueAreaCb.setChecked(savedInstanceState.getBoolean(STATE_BARBECUE_AREA));
            moviesAreaCb.setChecked(savedInstanceState.getBoolean(STATE_MOVIES_AREA));
            partyRoomAreaCb.setChecked(savedInstanceState.getBoolean(STATE_PARTY_ROOM));
            sportsCourtAreaCb.setChecked(savedInstanceState.getBoolean(STATE_SPORTS_AREA));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectionReceiver.connectionListener = this;
    }

    public void profileViewModelSetup() {
        String email = Utils.removeSpecialCharacters(FirebaseUserAuthentication.getInstance().getUserEmail());
        ProfileType newProfileType = new ProfileType(email, "condominium");

        ProfileTypeViewModel profileTypeViewModel = ViewModelProviders.of(this).get(ProfileTypeViewModel.class);
        profileTypeViewModel.createProfileType(newProfileType).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    Log.e(TAG, "Failed to setup profile type");
                }
            }
        });
    }

    public void checkCondSetup() {
        final String condId = Utils.getCondIdPreference(getContext());

        condViewModel.getCondById(condId).observe(this, new Observer<Condominium>() {
            @Override
            public void onChanged(@Nullable Condominium condominium) {
                if (condominium != null) {
                    nameInput.getEditText().setText(condominium.getName());
                    addressInput.getEditText().setText(condominium.getLocation());
                    numberInput.getEditText().setText(condominium.getNumber());
                    zipCodeInput.getEditText().setText(condominium.getZipCode());
                    stateInput.getEditText().setText(condominium.getState());
                    cityInput.getEditText().setText(condominium.getCity());

                    CommonAreas condAreas = condominium.getCommonAreas();
                    gourmetAreaCb.setChecked(condAreas.isHasGourmetArea());
                    poolAreaCb.setChecked(condAreas.isHasPoolArea());
                    barbecueAreaCb.setChecked(condAreas.isHasBarbecueArea());
                    moviesAreaCb.setChecked(condAreas.isHasMoviesArea());
                    partyRoomAreaCb.setChecked(condAreas.isHasPartyRoomArea());
                    sportsCourtAreaCb.setChecked(condAreas.isHasSportsCourtArea());

                    nameInput.getEditText().setFocusable(false);
                    nameInput.getEditText().setInputType(InputType.TYPE_NULL);
                    nameInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));

                    addressInput.getEditText().setFocusable(false);
                    addressInput.getEditText().setInputType(InputType.TYPE_NULL);
                    addressInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));

                    numberInput.getEditText().setFocusable(false);
                    numberInput.getEditText().setInputType(InputType.TYPE_NULL);
                    numberInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));

                    zipCodeInput.getEditText().setFocusable(false);
                    zipCodeInput.getEditText().setInputType(InputType.TYPE_NULL);
                    zipCodeInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));

                    stateSp.setVisibility(View.GONE);
                    stateInput.setVisibility(View.VISIBLE);
                    stateInput.getEditText().setFocusable(false);
                    stateInput.getEditText().setInputType(InputType.TYPE_NULL);
                    stateInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));

                    cityInput.getEditText().setFocusable(false);
                    cityInput.getEditText().setInputType(InputType.TYPE_NULL);
                    cityInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));

                    if (gourmetAreaCb.isChecked()) {
                        gourmetAreaCb.setEnabled(false);
                        gourmetAreaCb.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
                    }

                    if (poolAreaCb.isChecked()) {
                        poolAreaCb.setEnabled(false);
                        poolAreaCb.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
                    }

                    if (barbecueAreaCb.isChecked()) {
                        barbecueAreaCb.setEnabled(false);
                        barbecueAreaCb.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
                    }

                    if (moviesAreaCb.isChecked()) {
                        moviesAreaCb.setEnabled(false);
                        moviesAreaCb.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
                    }

                    if (partyRoomAreaCb.isChecked()) {
                        partyRoomAreaCb.setEnabled(false);
                        partyRoomAreaCb.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
                    }

                    if (sportsCourtAreaCb.isChecked()) {
                        sportsCourtAreaCb.setEnabled(false);
                        sportsCourtAreaCb.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ok_btn) {
            if (hasAllRequiredFields()) {
                String normalizedCondName = Utils.normalizeAndLowcaseName(condName);
                final String id = "id_" + normalizedCondName + "_" + condCity;
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
                saveCondIdPreference();
            } else {
                Toast.makeText(mContext, R.string.insert_all_required_fiels_toast,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean hasAllRequiredFields() {
        condName = nameInput.getEditText().getText().toString();
        condLocation = addressInput.getEditText().getText().toString();
        condNumber = numberInput.getEditText().getText().toString();
        condZipCode = zipCodeInput.getEditText().getText().toString();
        condState = stateSp.getSelectedItem().toString();
        condCity = cityInput.getEditText().getText().toString();

        if (Utils.isValidZipCode(condZipCode) && !condName.isEmpty() && !condLocation.isEmpty()
                && !condNumber.isEmpty() && !condZipCode.isEmpty() && condState != null
                && !condCity.isEmpty()) {
            return true;
        }
        return false;
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

    private void writeNewCondominium(final String condId, String name, String profilePic, String location,
                                     String number, String zipCode, String state, String city,
                                     CommonAreas commonAreas) {

        String condEmail = Utils.removeSpecialCharacters(FirebaseUserAuthentication.getInstance().getUserEmail());
        final Condominium cond = new Condominium(condId, name, profilePic, location, number, zipCode,
                state, city, condEmail, commonAreas);
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

    private void saveCondIdPreference() {
        String normalizedCondName = Utils.normalizeAndLowcaseName(condName);
        final String condId = "id_" + normalizedCondName + "_" + condCity;

        Log.d(TAG, "Saving condId preference as " + condId);
        String prefFileName = Utils.getPreferenceFileName(getContext());

        SharedPreferences prefs = getContext().getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.cond_id_pref), condId);
        editor.commit();
    }

    private void startMenuActivity() {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d(TAG, "onNetworkConnectionChanged");
        settingUpButtons(isConnected);
    }

    public void checkConnection() {
        boolean isConnected = ConnectionReceiver.isConnected(getContext());
        settingUpButtons(isConnected);
        if (!isConnected) {
            Utils.displayNoConnectionToast(getContext());
        }
    }

    public void settingUpButtons(boolean isConnected) {
        Log.d(TAG, "setting button " + isConnected);
        okBtn.setEnabled(isConnected);
    }
}
