package com.community.hmunguba.condominium.view.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.community.hmunguba.condominium.R;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        String profileType = bundle.getString("profile_type");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (profileType.equals("condominium")) {
            CondominiumProfileFragment condominiumProfileFragment = new CondominiumProfileFragment();
            ft.replace(R.id.profile_container, condominiumProfileFragment);
            ft.commit();
        } else {
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            ft.replace(R.id.profile_container, userProfileFragment);
            ft.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_profile:
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
}
