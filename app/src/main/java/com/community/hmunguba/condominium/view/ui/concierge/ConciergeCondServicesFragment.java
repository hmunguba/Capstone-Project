package com.community.hmunguba.condominium.view.ui.concierge;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.menu.MenuActivity;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;

public class ConciergeCondServicesFragment extends Fragment implements View.OnClickListener {
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

        saveBtn.setOnClickListener(this);
        checkCondInfo();

        return rootView;
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
}
