package com.community.hmunguba.condominium.view.ui.notifications;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.community.hmunguba.condominium.R;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        NotificationsFragment notificationsFragment = new NotificationsFragment();
        ft.add(R.id.notifications_container, notificationsFragment);
        ft.commit();
    }

}
