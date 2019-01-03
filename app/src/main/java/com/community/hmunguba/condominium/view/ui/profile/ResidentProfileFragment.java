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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.User;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.menu.MenuActivity;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;
import com.community.hmunguba.condominium.viewmodel.ResidentViewModel;

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
    private Spinner condOptionsSp;
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

    public ResidentProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        residentViewModel = ViewModelProviders.of(this).get(ResidentViewModel.class);
        mContext = getActivity();
        setupCondsNamesViewModel();
        checkUserSetup();
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
        saveBtn = rootView.findViewById(R.id.resident_ok_btn);

        saveBtn.setOnClickListener(this);

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
                }
            }
        });
    }

    public void setupCondsNamesViewModel() {
        CondominiumViewModel condViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);
        condViewModel.getCondsNameList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> condNames) {
                if (condNames != null && condNames.size() > 0) {
                    populateCondSpinner(condNames);
                }
            }
        });
    }

    public void populateCondSpinner(List<String> condsNames) {
        Log.e(TAG, "populateCondSpinner");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, condsNames);
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
        }
    }

    private boolean hasAllRequiredFields() {
        firstName = firstNameInput.getEditText().getText().toString();
        lastName = lastNameInput.getEditText().getText().toString();
        houseNumber = houseNumberInput.getEditText().getText().toString();
        phoneNumber = phoneNumberInput.getEditText().getText().toString();
        city = cityInput.getEditText().getText().toString();
        selectedCond = condOptionsSp.getSelectedItem().toString();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !houseNumber.isEmpty() &&
                !email.isEmpty() && !city.isEmpty() &&  selectedCond != null) {
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
