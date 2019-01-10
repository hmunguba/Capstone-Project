package com.community.hmunguba.condominium.view.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.community.hmunguba.condominium.R;
import com.community.hmunguba.condominium.service.firebase.FirebaseUserAuthentication;
import com.community.hmunguba.condominium.service.model.AuthAnswer;
import com.community.hmunguba.condominium.service.model.ProfileType;
import com.community.hmunguba.condominium.service.utils.Utils;
import com.community.hmunguba.condominium.view.ui.menu.MenuActivity;
import com.community.hmunguba.condominium.view.ui.profile.ProfileActivity;
import com.community.hmunguba.condominium.viewmodel.LoginViewModel;
import com.community.hmunguba.condominium.viewmodel.ProfileTypeViewModel;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Part of code from:
 * https://github.com/firebase/quickstart-android/
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String userEmail;

    private LoginViewModel loginViewModel;
    private ProfileTypeViewModel profileTypeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        populateAutoComplete();
        mPasswordView = findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        findViewById(R.id.email_sign_out_button).setOnClickListener(this);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        profileTypeViewModel = ViewModelProviders.of(this).get(ProfileTypeViewModel.class);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View view) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (loginViewModel.hasCurrentUser()) {
            Log.d(TAG, "user is already signed in");
            mProgressView.setVisibility(View.VISIBLE);
            mLoginFormView.setVisibility(View.GONE);
            userEmail = FirebaseUserAuthentication.getInstance().getUserEmail();
            checkProfileTypeIsChoosen();
        }
    }

    // Create new user
    public void createAccount(String email, String password) {
        Log.d(TAG, "create account: " + email);

        if (!validateForm()) {
            return;
        }

        showProgress(true);

        loginViewModel.registerUser(email, password).observe(this, new Observer<AuthAnswer>() {
            @Override
            public void onChanged(@Nullable AuthAnswer authAnswer) {
                if (authAnswer.isSuccessful()) {
                    checkProfileTypeIsChoosen();
                } else {
                    Toast.makeText(LoginActivity.this,
                                    authAnswer.getMessage(), Toast.LENGTH_LONG).show();
                }
                showProgress(false);
            }
        });
    }

    // Sign in existing users
    public void signIn(String email, String password) {
        Log.d(TAG, "signIn: " + email);

        if (!validateForm()) {
            return;
        }

        showProgress(true);
        Log.d(TAG, "showind Progress");

        loginViewModel.signInUser(email, password).observe(this, new Observer<AuthAnswer>() {
            @Override
            public void onChanged(@Nullable AuthAnswer authAnswer) {
                if (authAnswer.isSuccessful()) {
                    checkProfileTypeIsChoosen();
                } else {
                    Toast.makeText(LoginActivity.this, authAnswer.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                showProgress(false);
            }
        });

    }

    private void signOut() {
        loginViewModel.signOutUser();
        Toast.makeText(LoginActivity.this, R.string.sign_out_toast_message,
                Toast.LENGTH_SHORT).show();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.required));
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.required));
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }

    public void startMenuActivity() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void startProfileActivity() {
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void startChoseProfileTypeActivity() {
        Intent intent = new Intent(LoginActivity.this, ChoseProfileType.class);
        startActivity(intent);
    }

    public void checkProfileTypeIsChoosen() {
        String email = Utils.removeSpecialCharacters(userEmail);
        profileTypeViewModel.loadProfileType(email).observe(this, new Observer<ProfileType>() {
            @Override
            public void onChanged(@Nullable ProfileType profileType) {
                if (profileType != null) {
                    saveProfileTypeSelection(profileType.getType());
                }
                String prefFileName = Utils.getPreferenceFileName(getApplicationContext());
                String currentCondId = Utils.getCondIdPreference(getApplicationContext());

                SharedPreferences prefs = getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
                Boolean hasProfileTypePref = prefs.getBoolean(getString(R.string.has_profile_type_pref), false);

//                mProgressView.setVisibility(View.GONE);
                if (!hasProfileTypePref) {
                    startChoseProfileTypeActivity();
                } else if (hasProfileTypePref && currentCondId.equals(getString(R.string.no_cond_id_set))) {
                    startProfileActivity();
                } else {
                    startMenuActivity();
                }
            }
        });

    }

    public void saveProfileTypeSelection(String profileType) {
        Log.d(TAG, "Saving profile preference as " + profileType);
        String prefFileName = Utils.getPreferenceFileName(this.getApplicationContext());

        SharedPreferences prefs = this.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(getString(R.string.has_profile_type_pref), true);
        editor.putString(getString(R.string.profile_type_pref), profileType);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        userEmail = mEmailView.getText().toString();

        if (i == R.id.email_sign_in_button) {
            signIn(mEmailView.getText().toString(), mPasswordView.getText().toString());
        } else if (i == R.id.email_create_account_button) {
            createAccount(mEmailView.getText().toString(), mPasswordView.getText().toString());
        } else if (i == R.id.email_sign_out_button) {
            signOut();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

