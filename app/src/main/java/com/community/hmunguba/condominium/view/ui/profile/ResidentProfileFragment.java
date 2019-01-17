package com.community.hmunguba.condominium.view.ui.profile;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.model.ProfileType;
import com.community.hmunguba.condominium.service.model.User;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.utils.ConnectionReceiver;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.login.LoginActivity;
import com.community.hmunguba.condominium.view.ui.menu.MenuActivity;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;
import com.community.hmunguba.condominium.viewmodel.ProfileTypeViewModel;
import com.community.hmunguba.condominium.viewmodel.ResidentViewModel;

import java.util.ArrayList;
import java.util.List;

public class ResidentProfileFragment extends Fragment implements View.OnClickListener,
        ConnectionReceiver.ConnectionReceiverListener {

    public static final String TAG = ResidentProfileFragment.class.getSimpleName();
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
    private Button condNotFoundBtn;
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
    private ArrayList<String> condNames;
    private boolean condInfoAlreadyVisible = false;

    private static final String STATE_FIRST_NAME = "firstName";
    private static final String STATE_LAST_NAME = "lastName";
    private static final String STATE_PHONE = "phone";
    private static final String STATE_CITY = "city";
    private static final String STATE_COND = "cond";
    private static final String STATE_HOUSE_NUMBER = "houseNumber";
    private static final String STATE_COND_NAMES = "condNames";
    private static final String STATE_COND_LAYOUT_VISIBILITY = "layoutVisibility";

    public ResidentProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        residentViewModel = ViewModelProviders.of(this).get(ResidentViewModel.class);
        mContext = getActivity();

        profileViewModelSetup();
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
        condNotFoundBtn = rootView.findViewById(R.id.cond_not_found_btn);
        saveBtn = rootView.findViewById(R.id.resident_ok_btn);

        condNotFoundBtn.setOnClickListener(this);
        loadCondsBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        phoneNumberInput.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});

        phoneNumberInput.getEditText().addTextChangedListener(new TextWatcher() {
            int prevL = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevL = phoneNumberInput.getEditText().getText().toString().length();
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

        checkUserSetup();
        checkConnection();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectionReceiver.connectionListener = this;
    }

    public void profileViewModelSetup() {
        String email = Utils.removeSpecialCharacters(FirebaseUserAuthentication.getInstance().getUserEmail());
        ProfileType newProfileType = new ProfileType(email, "resident");

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(STATE_FIRST_NAME, firstNameInput.getEditText().getText().toString());
        outState.putString(STATE_LAST_NAME,lastNameInput.getEditText().getText().toString());
        outState.putString(STATE_PHONE, phoneNumberInput.getEditText().getText().toString());
        outState.putString(STATE_CITY, cityInput.getEditText().getText().toString());
        outState.putString(STATE_HOUSE_NUMBER, houseNumberInput.getEditText().getText().toString());
        outState.putInt(STATE_COND, condOptionsSp.getSelectedItemPosition());
        outState.putInt(STATE_COND_LAYOUT_VISIBILITY, condInfoLl.getVisibility());
        outState.putStringArrayList(STATE_COND_NAMES, condNames);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored");

        if (savedInstanceState != null) {
            firstNameInput.getEditText().setText(savedInstanceState.getCharSequence(STATE_FIRST_NAME));
            lastNameInput.getEditText().setText(savedInstanceState.getString(STATE_LAST_NAME));
            phoneNumberInput.getEditText().setText(savedInstanceState.getString(STATE_PHONE));
            cityInput.getEditText().setText(savedInstanceState.getString(STATE_CITY));
            houseNumberInput.getEditText().setText(savedInstanceState.getString(STATE_HOUSE_NUMBER));
            condInfoLl.setVisibility(savedInstanceState.getInt(STATE_COND_LAYOUT_VISIBILITY));
            condNames = savedInstanceState.getStringArrayList(STATE_COND_NAMES);
            condOptionsSp.setSelection(savedInstanceState.getInt(STATE_COND));

            if (condInfoLl.getVisibility() == View.VISIBLE) {
                condInfoAlreadyVisible = true;
                checkCityName();
            }
        }
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
                    condNotFoundBtn.setVisibility(View.GONE);

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
                } else if (!condInfoAlreadyVisible){
                    // Load conds only after knowing the city name
                    condInfoLl.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setupCondsNamesViewModel() {
        Log.d(TAG, "setupCondsNamesViewModel");
        condNames = new ArrayList<>();

        CondominiumViewModel condViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);
        condViewModel.getCondsNameList().observe(this, new Observer<List<Condominium>>() {
            @Override
            public void onChanged(@Nullable List<Condominium> conds) {
                for (Condominium c : conds) {
                    if (c.getCity().equals(city)) {
                        condNames.add(c.getName());
                    }
                }

                if (condNames != null && condNames.size() > 0) {
                    populateCondSpinner(condNames);
                } else {
                    Toast.makeText(getContext(), getString(R.string.no_condominium_for_city_toast), Toast.LENGTH_SHORT).show();
                    populateCondSpinner(new ArrayList<String>());
                }
            }
        });
    }

    public void populateCondSpinner(List<String> condsNames) {
        Log.d(TAG, "populateCondSpinner");

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
        } else if (view.getId() == R.id.cond_not_found_btn) {
            displayCondNotFoundDialog();
        }
    }

    public void checkCityName() {
        city = cityInput.getEditText().getText().toString();

        if (city == null || city.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.no_city_name_toast), Toast.LENGTH_SHORT).show();
        } else {
            condInfoLl.setVisibility(View.VISIBLE);
            setupCondsNamesViewModel();
        }
    }

    private boolean hasAllRequiredFields() {
        email = FirebaseUserAuthentication.getInstance().getUserEmail();
        residentId = Utils.removeSpecialCharacters(email);
        firstName = firstNameInput.getEditText().getText().toString();
        lastName = lastNameInput.getEditText().getText().toString();
        houseNumber = houseNumberInput.getEditText().getText().toString();
        phoneNumber = phoneNumberInput.getEditText().getText().toString();
        city = cityInput.getEditText().getText().toString();

        if (condOptionsSp.getVisibility() == View.GONE) {
            selectedCond = condNameInput.getEditText().getText().toString();
        } else {
            if (condOptionsSp.getSelectedItem() == null)
                return false;
            selectedCond = condOptionsSp.getSelectedItem().toString();
        }

        if (!firstName.isEmpty() && !lastName.isEmpty() && !houseNumber.isEmpty() &&
                !email.isEmpty() && !city.isEmpty() &&  !selectedCond.isEmpty()) {
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

    public void displayCondNotFoundDialog() {
        Log.d(TAG, "displayCondNotFoundDialog");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(R.string.dialog_create_cond).setTitle(R.string.cond_not_found);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseUserAuthentication.getInstance().signOut();
                startLoginActivity();
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void startMenuActivity() {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        startActivity(intent);
    }

    public void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
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
        saveBtn.setEnabled(isConnected);
        loadCondsBtn.setEnabled(isConnected);
        condNotFoundBtn.setEnabled(isConnected);
    }
}
