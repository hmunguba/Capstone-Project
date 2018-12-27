package com.community.hmunguba.condominium.view.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.view.ui.profile.ProfileActivity;

public class ChoseProfileType extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ChoseProfileType.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_profile_type);

        LinearLayout condProfileTypeContainer = findViewById(R.id.cond_profile_type_container);
        LinearLayout residentProfileTypeContainer = findViewById(R.id.resident_profile_type_container);

        condProfileTypeContainer.setOnClickListener(this);
        residentProfileTypeContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int container = view.getId();

        if (container == R.id.cond_profile_type_container) {
            saveProfileTypeSelection("condominium");
        } else if (container == R.id.resident_profile_type_container) {
            saveProfileTypeSelection("resident");
        }
        startProfileActivity();
    }

    public void saveProfileTypeSelection(String profileType) {
        Log.d(TAG, "Saving profile preference as " + profileType);

        String userEmail = FirebaseUserAuthentication.getInstance().getUserEmail();
        String prefFileName = getString(R.string.file_key_pref) + userEmail;

        SharedPreferences prefs = this.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.profile_type_pref), profileType);
        editor.commit();
    }

    public void startProfileActivity() {
        Intent intent = new Intent(ChoseProfileType.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chose_profile_menu, menu);
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

    public void startLoginActivity() {
        Intent intent = new Intent(ChoseProfileType.this, LoginActivity.class);
        startActivity(intent);
    }
}
