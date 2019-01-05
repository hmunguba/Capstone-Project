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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.model.User;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.menu.MenuActivity;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;
import com.community.hmunguba.condominium.viewmodel.ResidentViewModel;

import java.util.ArrayList;
import java.util.List;

public class ResidentProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ResidentProfileFragment.class.getSimpleName();
    private Context mContext;

    private TextInputLayout firstNameInput;
    private TextInputLayout lastNameInput;
    private TextInputLayout houseNumberInput;
    private TextInputLayout phoneNumberInput;
    private TextInputLayout cityInput;
    private TextInputLayout condNameInput;
    private LinearLayout condInfoLl;
    private Spinner condOptionsSp;
    private Button loadCondsBtn;
    private Button saveBtn;

    private String residentId;
    private String firstName;
    private String lastName;
    private String houseNumber;
    private String phoneNumber;
    private String email;
    private String city;
    private String selectedCond;

    private ResidentViewModel residentViewModel;
    private ArrayList<Condominium> loadedConds;

    public ResidentProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        residentViewModel = ViewModelProviders.of(this).get(ResidentViewModel.class);
        loadedConds = new ArrayList<>();
        mContext = getActivity();

        setupCondsNamesViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        firstNameInput = rootView.findViewById(R.id.resident_first_name_til);
        lastNameInput = rootView.findViewById(R.id.resident_last_name_til);
        houseNumberInput = rootView.findViewById(R.id.resident_house_number_til);
        phoneNumberInput = rootView.findViewById(R.id.resident_phone_til);
        cityInput = rootView.findViewById(R.id.resident_city_til);
        condOptionsSp = rootView.findViewById(R.id.resident_cond_options);
        condNameInput = rootView.findViewById(R.id.resident_cond_name_til);
        condInfoLl = rootView.findViewById(R.id.resident_residence_info_ll);
        loadCondsBtn = rootView.findViewById(R.id.resident_load_conds_btn);
        saveBtn = rootView.findViewById(R.id.resident_ok_btn);

        loadCondsBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        checkUserSetup();

        return rootView;
    }

    public void checkUserSetup() {
        email = FirebaseUserAuthentication.getInstance().getUserEmail();
        residentId = Utils.removeSpecialCharacters(email);

        residentViewModel.getUserById(residentId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    // Disabling unchangeble fields
                    condInfoLl.setVisibility(View.VISIBLE);
                    loadCondsBtn.setVisibility(View.GONE);

                    firstNameInput.getEditText().setText(user.getFirstName());
                    lastNameInput.getEditText().setText(user.getLastName());
                    houseNumberInput.getEditText().setText(String.valueOf(user.getHouseNumber()));
                    phoneNumberInput.getEditText().setText(user.getPhone());
                    cityInput.getEditText().setText(user.getCity());
                    condNameInput.getEditText().setText(user.getCondominium());

                    firstNameInput.getEditText().setFocusable(false);
                    firstNameInput.getEditText().setInputType(InputType.TYPE_NULL);
                    firstNameInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));

                    lastNameInput.getEditText().setFocusable(false);
                    lastNameInput.getEditText().setInputType(InputType.TYPE_NULL);
                    lastNameInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));

                    cityInput.getEditText().setFocusable(false);
                    cityInput.getEditText().setInputType(InputType.TYPE_NULL);
                    cityInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));

                    condOptionsSp.setVisibility(View.GONE);
                    condNameInput.setVisibility(View.VISIBLE);
                    condNameInput.getEditText().setFocusable(false);
                    condNameInput.getEditText().setInputType(InputType.TYPE_NULL);
                    condNameInput.getEditText().setTextColor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
                } else {
                    // Load conds only after knowing the city name
                    condInfoLl.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setupCondsNamesViewModel() {
        Log.d(TAG, "setupCondsNamesViewModel");

        CondominiumViewModel condViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);
        condViewModel.getCondsNameList().observe(this, new Observer<List<Condominium>>() {
            @Override
            public void onChanged(@Nullable List<Condominium> conds) {
                for (Condominium c : conds) {
                    loadedConds.add(c);
                }
            }
        });
    }

    public ArrayList<String> getCondsNameForCity() {
        ArrayList<String> condNames = new ArrayList<>();

        for (int i = 0; i < loadedConds.size(); i++) {
            if (loadedConds.get(i).getCity().equals(city)){
                condNames.add(loadedConds.get(i).getName());
            }
        }
        return condNames;
    }

    public void populateCondSpinner(List<String> condsNames) {
        Log.e(TAG, "populateCondSpinner");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, condsNames);
        spinnerAdapter.notifyDataSetChanged();
        condOptionsSp.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.resident_ok_btn) {
            if (hasAllRequiredFields()) {
                createResident();
                saveCondIdPreference();
            } else {
                Toast.makeText(mContext, R.string.insert_all_required_fiels_toast, Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.resident_load_conds_btn) {
            checkCityName();
        }
    }

    public void checkCityName() {
        city = cityInput.getEditText().getText().toString();

        if (city == null || city.isEmpty()) {
            Toast.makeText(getContext(), "Please input a city name", Toast.LENGTH_SHORT).show();
        } else {
            condInfoLl.setVisibility(View.VISIBLE);
            ArrayList<String> condList = getCondsNameForCity();
            if (condList != null && condList.size() > 0) {
                //condOptionsSp.setEnabled(true);
                populateCondSpinner(condList);
            } else {
                Toast.makeText(getContext(), "No condominium found for this city", Toast.LENGTH_SHORT).show();
                populateCondSpinner(new ArrayList<String>());
            }
        }
    }

    private boolean hasAllRequiredFields() {
        firstName = firstNameInput.getEditText().getText().toString();
        lastName = lastNameInput.getEditText().getText().toString();
        houseNumber = houseNumberInput.getEditText().getText().toString();
        phoneNumber = phoneNumberInput.getEditText().getText().toString();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !houseNumber.isEmpty() &&
                !email.isEmpty() && !city.isEmpty() &&  condOptionsSp.getSelectedItem() != null) {
            selectedCond = condOptionsSp.getSelectedItem().toString();
            return true;
        }
        return false;
    }

    private void createResident() {
        String profilePic = "";
        User user = new User(residentId, firstName, lastName, city, selectedCond, profilePic,
                Integer.parseInt(houseNumber), phoneNumber, email);

        residentViewModel.createUser(user).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getContext(), getString(R.string.resident_created_success),
                            Toast.LENGTH_SHORT).show();
                    startMenuActivity();

                } else {
                    Toast.makeText(getContext(), getString(R.string.resident_created_fail),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveCondIdPreference() {
        String normalizedCondName = Utils.normalizeAndLowcaseName(selectedCond);
        final String condId = "id_" + normalizedCondName + "_" + city;

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
}
