package com.community.hmunguba.condominium.view.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.community.hmunguba.condominium.service.model.repo.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.menu.MenuActivity;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;
import com.community.hmunguba.condominium.viewmodel.ResidentViewModel;

import java.util.List;

public class ResidentProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ResidentProfileFragment.class.getSimpleName();
    private Context mContext;

    private EditText firstNameEt;
    private EditText lastNameEt;
    private EditText houseNumberEt;
    private EditText phoneNumberEt;
    private EditText emailEt;
    private Spinner condOptionsSp;
    private Button saveBtn;

    private String residentId;
    private String firstName;
    private String lastName;
    private String houseNumber;
    private String phoneNumber;
    private String email;
    private String selectedCond;

    private CondominiumViewModel condViewModel;
    private ResidentViewModel residentViewModel;

    public ResidentProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        setupCondsNamesViewModel();
        residentViewModel = ViewModelProviders.of(this).get(ResidentViewModel.class);
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

        emailEt.setHint(FirebaseUserAuthentication.getInstance().getUserEmail());
        emailEt.setInputType(InputType.TYPE_NULL);
        emailEt.setKeyListener(null);

        saveBtn.setOnClickListener(this);

        return rootView;
    }

    public void setupCondsNamesViewModel() {
        condViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);
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
            } else {
                Toast.makeText(mContext, R.string.insert_all_required_fiels_toast, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean hasAllRequiredFields() {
        residentId = Utils.removeSpecialCharacters(emailEt.getText().toString());
        firstName = firstNameEt.getText().toString();
        lastName = lastNameEt.getText().toString();
        houseNumber = houseNumberEt.getText().toString();
        phoneNumber = phoneNumberEt.getText().toString();
        email = emailEt.getText().toString();
        selectedCond = condOptionsSp.getSelectedItem().toString();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !houseNumber.isEmpty() &&
                !email.isEmpty() && selectedCond != null) {
            return true;
        }
        return false;
    }

    private void createResident() {
        String profilePic = "";
        User user = new User(residentId, firstName, lastName, selectedCond, profilePic,
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

    private void startMenuActivity() {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        startActivity(intent);
    }
}
