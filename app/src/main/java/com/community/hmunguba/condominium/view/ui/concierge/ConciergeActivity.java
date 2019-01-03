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
            ConciergeCondServicesFragment condServicesFragment = new ConciergeCondServicesFragment();
            ft.replace(R.id.services_container, condServicesFragment);
            ft.commit();
        } else {
            ConciergeResidentServicesFragment residentServicesFragment = new ConciergeResidentServicesFragment();
            ft.replace(R.id.services_container, residentServicesFragment);
            ft.commit();
        }
    }
}
