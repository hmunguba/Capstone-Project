package com.community.hmunguba.condominium.view.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.model.repo.FirebaseUserAuthentication;
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
        String profileType = "";

        if (container == R.id.cond_profile_type_container) {
            profileType = "condominium";
        } else if (container == R.id.resident_profile_type_container) {
            profileType = "resident";
        }
        saveProfileTypeSelection(profileType);
        startProfileActivity(profileType);
    }

    public void saveProfileTypeSelection(String profileType) {
        String userEmail = FirebaseUserAuthentication.getInstance().getUserEmail();
        String prefFileName = getString(R.string.file_key_pref) + userEmail;

        SharedPreferences prefs = this.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.profile_type_pref), profileType);
        editor.commit();
    }

    public void startProfileActivity(String profileType) {
        Intent intent = new Intent(ChoseProfileType.this, ProfileActivity.class);
        intent.putExtra("profile_type", profileType);
        startActivity(intent);
    }
}
