package com.community.hmunguba.condominium.view.ui.concierge;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.viewmodel.CondominiumViewModel;

public class ConciergeResidentServicesFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = ConciergeResidentServicesFragment.class.getSimpleName();

    private TextView lobbyTv;
    private CardView callLobbyCv;
    private CardView messageLobbyCv;
    private TextView syndicTv;
    private CardView callSyndicCv;
    private CardView messageSyndicCv;
    private CardView emailSydicCv;
    private TextView noServiceInfoTv;

    private CondominiumViewModel condominiumViewModel;
    private String lobbyPhone;
    private String syndicPhone;
    private String syndicEmail;

    public ConciergeResidentServicesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        condominiumViewModel = ViewModelProviders.of(this).get(CondominiumViewModel.class);
        setupCondViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resident_concierge_services, container, false);

        lobbyTv = rootView.findViewById(R.id.lobby_title_tv);
        callLobbyCv = rootView.findViewById(R.id.call_lobby_cv);
        messageLobbyCv = rootView.findViewById(R.id.send_message_lobby_cv);
        syndicTv = rootView.findViewById(R.id.syndic_title_tv);
        callSyndicCv = rootView.findViewById(R.id.call_syndic_cv);
        messageSyndicCv = rootView.findViewById(R.id.send_message_syndic_cv);
        emailSydicCv = rootView.findViewById(R.id.send_email_syndic_cv);
        noServiceInfoTv = rootView.findViewById(R.id.no_service_info);

        callLobbyCv.setOnClickListener(this);
        messageLobbyCv.setOnClickListener(this);
        callSyndicCv.setOnClickListener(this);
        messageSyndicCv.setOnClickListener(this);
        emailSydicCv.setOnClickListener(this);

        return rootView;
    }

    public void setupCondViewModel() {
        String condId = Utils.getCondIdPreference(getContext());
        condominiumViewModel.loadCond(condId).observe(this, new Observer<Condominium>() {
            @Override
            public void onChanged(@Nullable Condominium condominium) {
                lobbyPhone = condominium.getConciergePhoneNumber();
                syndicPhone = condominium.getSyndicPhone();
                syndicEmail = condominium.getSyndicMail();

                updateUI();
            }
        });
    }

    public void updateUI() {
        if (lobbyPhone == null || lobbyPhone.isEmpty()) {
            lobbyTv.setVisibility(View.GONE);
            callLobbyCv.setVisibility(View.GONE);
            messageLobbyCv.setVisibility(View.GONE);
        } else {
            lobbyTv.setVisibility(View.VISIBLE);
            callLobbyCv.setVisibility(View.VISIBLE);
            messageLobbyCv.setVisibility(View.VISIBLE);
        }

        if (syndicPhone == null || syndicPhone.isEmpty()) {
            callSyndicCv.setVisibility(View.GONE);
            messageSyndicCv.setVisibility(View.GONE);
        } else {
            callSyndicCv.setVisibility(View.VISIBLE);
            messageSyndicCv.setVisibility(View.VISIBLE);
        }
        if (syndicEmail == null || syndicEmail.isEmpty()) {
            emailSydicCv.setVisibility(View.GONE);
        } else {
            emailSydicCv.setVisibility(View.VISIBLE);
        }

        if ((syndicPhone == null || syndicPhone.isEmpty()) &&
                (syndicEmail == null || syndicEmail.isEmpty())) {
            syndicTv.setVisibility(View.GONE);
            if (lobbyPhone == null || lobbyPhone.isEmpty()) {
                noServiceInfoTv.setVisibility(View.VISIBLE);
            } else {
                noServiceInfoTv.setVisibility(View.GONE);
            }
        } else {
            syndicTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.call_lobby_cv) {
            Log.d(TAG, "Clicked to call lobby");
            dialNumber(lobbyPhone);
        } else if (view.getId() == R.id.send_message_lobby_cv) {
            Log.d(TAG, "Clicked to send message to lobby");
            sendMessage(lobbyPhone);
        } else if (view.getId() == R.id.call_syndic_cv) {
            Log.d(TAG, "Clicked to call syndic");
            dialNumber(syndicPhone);
        } else if (view.getId() == R.id.send_message_syndic_cv) {
            Log.d(TAG, "Clicked to send message to syndic");
            sendMessage(syndicPhone);
        } else if (view.getId() == R.id.send_email_syndic_cv) {
            Log.d(TAG, "Clicked to send email to syndic");
            sendEmail(syndicEmail);
        }
    }

    public void dialNumber(String phone) {
        String phoneNumber = String.format("tel: %s", phone);
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse(phoneNumber));
        if (dialIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(Intent.createChooser(dialIntent, "Choose application"));
        } else {
            Log.e(TAG, "Can't resolve app for Dial intent");
        }
    }

    public void sendMessage(String phoneNumber) {
        Intent messageIntent = new Intent(Intent.ACTION_VIEW,
                Uri.fromParts("sms", phoneNumber, null));

        if (messageIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(Intent.createChooser(messageIntent, "Choose application"));
        } else {
            Log.e(TAG, "Can't resolve app for send message intent");
        }
    }

    public void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:" + email));

        if (emailIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, "Choose application"));
        } else {
            Log.e(TAG, "Can't resolve app for send email intent");
        }
    }
}
