package com.community.hmunguba.condominium.view.ui.concierge;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.utils.Utils;

public class ConciergeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concierge);

        String profileType = Utils.getProfileTypePreference(getApplicationContext());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (profileType.equals("condominium")) {
            ConciergeResidentsListFragment residentsListFragment = new ConciergeResidentsListFragment();
            ft.replace(R.id.profile_container, residentsListFragment);
            ft.commit();
        } else {
            ConciergeServicesFragment conciergeServicesFragment = new ConciergeServicesFragment();
            ft.replace(R.id.profile_container, conciergeServicesFragment);
            ft.commit();
        }
    }
}
