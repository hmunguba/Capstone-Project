package com.community.hmunguba.condominium.view.ui.concierge;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.utils.ConnectionReceiver;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.menu.MenuActivity;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;

public class ConciergeCondServicesFragment extends Fragment implements View.OnClickListener,
        ConnectionReceiver.ConnectionReceiverListener {
    private static final String TAG = ConciergeCondServicesFragment.class.getSimpleName();

    private TextInputLayout conciergePhoneNumberInput;
    private TextInputLayout syndicNameInput;
    private TextInputLayout syndicPhoneInput;
    private TextInputLayout syndicEmailInput;
    private Button saveBtn;

    private String conciergePhoneNumber;
    private String syndicName;
    private String syndicPhone;
    private String syndicEmail;

    private static final String STATE_LOBBY_PHONE = "lobbyPhone";
    private static final String STATE_SYNDIC_NAME = "syndicName";
    private static final String STATE_SYNDIC_PHONE = "syndicPhone";
    private static final String STATE_SYNDIC_EMAIL = "syndicMail";

    private CondominiumViewModel condominiumViewModel;

    public ConciergeCondServicesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        condominiumViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cond_concierge_services, container, false);

        conciergePhoneNumberInput = rootView.findViewById(R.id.services_phone_til);
        syndicNameInput = rootView.findViewById(R.id.services_syndic_name_til);
        syndicPhoneInput = rootView.findViewById(R.id.services_syndic_phone_til);
        syndicEmailInput = rootView.findViewById(R.id.services_syndic_mail_til);
        saveBtn = rootView.findViewById(R.id.services_ok_btn);

        syndicPhoneInput.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
        conciergePhoneNumberInput.getEditText().setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});

        conciergePhoneNumberInput.getEditText().addTextChangedListener(new TextWatcher() {
            int prevL = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevL = conciergePhoneNumberInput.getEditText().getText().toString().length();
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

        syndicPhoneInput.getEditText().addTextChangedListener(new TextWatcher() {
            int prevL = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevL = syndicPhoneInput.getEditText().getText().toString().length();
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

        saveBtn.setOnClickListener(this);
        checkCondInfo();
        checkConnection();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_LOBBY_PHONE, conciergePhoneNumberInput.getEditText().getText().toString());
        outState.putString(STATE_SYNDIC_NAME, syndicNameInput.getEditText().getText().toString());
        outState.putString(STATE_SYNDIC_PHONE, syndicPhoneInput.getEditText().getText().toString());
        outState.putString(STATE_SYNDIC_EMAIL, syndicEmailInput.getEditText().getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            conciergePhoneNumberInput.getEditText().setText(savedInstanceState.getString(STATE_LOBBY_PHONE));
            syndicNameInput.getEditText().setText(savedInstanceState.getString(STATE_SYNDIC_NAME));
            syndicPhoneInput.getEditText().setText(savedInstanceState.getString(STATE_SYNDIC_PHONE));
            syndicEmailInput.getEditText().setText(savedInstanceState.getString(STATE_SYNDIC_EMAIL));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectionReceiver.connectionListener = this;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.services_ok_btn) {
            updateCondServicesInfo();
            startMenuActivity();
        }
    }

    public void checkCondInfo() {
        String condId = Utils.getCondIdPreference(getContext());
        condominiumViewModel.loadCond(condId).observe(this, new Observer<Condominium>() {
            @Override
            public void onChanged(@Nullable Condominium condominium) {
                if (condominium.getConciergePhoneNumber() != null
                        && !condominium.getConciergePhoneNumber().isEmpty()) {
                    conciergePhoneNumberInput.getEditText()
                            .setText(condominium.getConciergePhoneNumber());
                }
                if (condominium.getSyndicName() != null
                        && !condominium.getSyndicName().isEmpty()) {
                    syndicNameInput.getEditText().setText(condominium.getSyndicName());
                }
                if (condominium.getSyndicPhone() != null
                        && !condominium.getSyndicPhone().isEmpty()) {
                    syndicPhoneInput.getEditText().setText(condominium.getSyndicPhone());
                }
                if (condominium.getSyndicMail() != null
                        && !condominium.getSyndicMail().isEmpty()) {
                    syndicEmailInput.getEditText().setText(condominium.getSyndicMail());
                }
            }
        });
    }

    public void updateCondServicesInfo() {
        Log.d(TAG, "updateCondServicesInfo");
        String condId = Utils.getCondIdPreference(getContext());

        condominiumViewModel.loadCond(condId).observe(this, new Observer<Condominium>() {
            @Override
            public void onChanged(@Nullable Condominium condominium) {
                conciergePhoneNumber = conciergePhoneNumberInput.getEditText().getText().toString();
                syndicName = syndicNameInput.getEditText().getText().toString();
                syndicPhone = syndicPhoneInput.getEditText().getText().toString();
                syndicEmail = syndicEmailInput.getEditText().getText().toString();

                condominium.setConciergePhoneNumber(conciergePhoneNumber);
                condominium.setSyndicName(syndicName);
                condominium.setSyndicPhone(syndicPhone);
                condominium.setSyndicMail(syndicEmail);

                condominiumViewModel.udateCondServices(condominium);
                Toast.makeText(getContext(), getString(R.string.data_successfully_updated),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startMenuActivity() {
        Intent menuIntent = new Intent(getActivity(), MenuActivity.class);
        startActivity(menuIntent);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d(TAG, "onNetworkConnectionChanged");
        settingUpButton(isConnected);
    }

    public void checkConnection() {
        boolean isConnected = ConnectionReceiver.isConnected(getContext());
        settingUpButton(isConnected);
        if (!isConnected) {
            Utils.displayNoConnectionToast(getContext());
        }
    }

    public void settingUpButton(boolean isConnected) {
        Log.d(TAG, "setting button " + isConnected);
        saveBtn.setEnabled(isConnected);
    }
}
