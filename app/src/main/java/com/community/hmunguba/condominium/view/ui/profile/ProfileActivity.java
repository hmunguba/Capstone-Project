package com.community.hmunguba.condominium.view.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.community.hmunguba.condominium.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        final Button userProfileBtn = findViewById(R.id.user_profile_btn);
        final Button condProfileBtn = findViewById(R.id.cond_profile_btn);

        userProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfileBtn.setVisibility(View.GONE);
                condProfileBtn.setVisibility(View.GONE);
                UserProfileFragment userProfileFragment = new UserProfileFragment();
                ft.add(R.id.profile_container, userProfileFragment);
                ft.commit();
            }
        });

        condProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfileBtn.setVisibility(View.GONE);
                condProfileBtn.setVisibility(View.GONE);
                CondominiumProfileFragment condominiumProfileFragment = new CondominiumProfileFragment();
                ft.add(R.id.profile_container, condominiumProfileFragment);
                ft.commit();
            }
        });
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
