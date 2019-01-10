package com.community.hmunguba.condominium.view.ui.profile;

import android.content.Intent;
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
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String profileType = Utils.getProfileTypePreference(getApplicationContext());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (profileType.equals("condominium")) {
            CondominiumProfileFragment condominiumProfileFragment = new CondominiumProfileFragment();
            ft.replace(R.id.profile_container, condominiumProfileFragment);
            ft.commit();
        } else {
            ResidentProfileFragment residentProfileFragment = new ResidentProfileFragment();
            ft.replace(R.id.profile_container, residentProfileFragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String currentCondId = Utils.getCondIdPreference(getApplicationContext());
        if (currentCondId.equals(getString(R.string.no_cond_id_set))) {
            exitApp();
        }
    }

    public void exitApp() {
        Log.d(TAG, "exitApp");
        Intent exitApp = new Intent(Intent.ACTION_MAIN);
        exitApp.addCategory(Intent.CATEGORY_HOME);
        exitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exitApp);
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
            case R.id.action_sign_out:
                FirebaseUserAuthentication.getInstance().signOut();
                startLoginActivity();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
