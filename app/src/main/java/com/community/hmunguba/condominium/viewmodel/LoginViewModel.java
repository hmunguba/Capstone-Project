package com.community.hmunguba.condominium.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.AuthAnswer;
import com.community.hmunguba.condominium.service.model.repo.FirebaseUserAuthentication;

public class LoginViewModel extends ViewModel {
    private static final String TAG = LoginViewModel.class.getSimpleName();

    public boolean hasCurrentUser() {
        return FirebaseUserAuthentication.getInstance().hasCurrentUser();
    }

    public LiveData<AuthAnswer> registerUser(String email, String password) {
        Log.d(TAG, "registerUser : " + email);
        LiveData<AuthAnswer> isUserCreated =
                FirebaseUserAuthentication.getInstance().createAccount(email, password);
        return isUserCreated;
    }

    public LiveData<AuthAnswer> signInUser(String email, String password) {
        Log.d(TAG, "signInUser : " + email);
        LiveData<AuthAnswer> isUserSignedIn =
                FirebaseUserAuthentication.getInstance().signIn(email, password);
        return isUserSignedIn;
    }

    public LiveData<AuthAnswer> updateUser(String name, String photoUri) {
        Log.d(TAG, "updateUser : " + name);
        LiveData<AuthAnswer> isUserUpdated =
                FirebaseUserAuthentication.getInstance().updateProfile(name, photoUri);
        return isUserUpdated;
    }

    public LiveData<AuthAnswer> deleteUser() {
        Log.d(TAG, "");
        LiveData<AuthAnswer> isUserDeleted =
                FirebaseUserAuthentication.getInstance().deleteUser();
        return isUserDeleted;
    }

    public void signOutUser() {
        Log.d(TAG, "");
        FirebaseUserAuthentication.getInstance().signOut();
    }
}
