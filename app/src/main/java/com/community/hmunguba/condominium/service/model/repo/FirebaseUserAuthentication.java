package com.community.hmunguba.condominium.service.model.repo;

import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.AuthAnswer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseUserAuthentication {
    private static final String TAG = FirebaseUserAuthentication.class.getSimpleName();

    private static FirebaseUserAuthentication firebaseUserAuthentication;
    private final FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public FirebaseUserAuthentication() {
        mAuth = FirebaseAuth.getInstance();
    }

    public synchronized static FirebaseUserAuthentication getInstance() {
        if (firebaseUserAuthentication == null) {
            firebaseUserAuthentication = new FirebaseUserAuthentication();
        }
        return firebaseUserAuthentication;
    }

    public boolean hasCurrentUser() {
        Log.d(TAG, "signin");
        if (mAuth.getCurrentUser() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void getCurrentUser() {
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Uri photoUri = currentUser.getPhotoUrl();

            String uid = currentUser.getUid();

            boolean emailVerified = currentUser.isEmailVerified();

            Log.d(TAG, "Get current user ");
            Log.d(TAG, "Get current user name = " + name);
            Log.d(TAG, "Get current user email = " + email);
            Log.d(TAG, "Get current user photoUri = " + photoUri);
            Log.d(TAG, "Get current user uid = " + uid);
            Log.d(TAG, "Get current user emailVerified = " + emailVerified);
        }
    }

    public String getUserEmail() {
        currentUser = mAuth.getCurrentUser();
        return currentUser.getEmail();
    }

    public MutableLiveData<AuthAnswer> createAccount(String email, String password) {
        Log.d(TAG, "createAccount");
        final MutableLiveData<AuthAnswer> answerLiveData = new MutableLiveData<>();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        AuthAnswer answer;
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //getCurrentUser();
                            answer = new AuthAnswer(true, null);
                        } else {
                            // If sign in fails, display a message to the user
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            answer = new AuthAnswer(false, task.getException().getMessage());
                        }
                        answerLiveData.setValue(answer);
                    }
                });
        return answerLiveData;
    }

    // When user already exists
    public MutableLiveData<AuthAnswer> signIn(String email, String password) {
        Log.d(TAG, "signin");
        final MutableLiveData<AuthAnswer> answerLiveData = new MutableLiveData<>();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        AuthAnswer answer;
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user information
                            Log.d(TAG, "signInWithEmail:success");
                            //getCurrentUser();
                            answer = new AuthAnswer(true, null);
                        } else {
                            // If sign in fails, display a mesage to the user
                            Log.e(TAG, "signInWithEmail:failure", task.getException());
                            answer = new AuthAnswer(false, task.getException().getMessage());
                        }
                        answerLiveData.setValue(answer);
                    }
                });
        return answerLiveData;
    }

    public MutableLiveData<AuthAnswer> updateProfile(String name, String photoUri) {
        Log.d(TAG, "updateProfile");
        final MutableLiveData<AuthAnswer> answerLiveData = new MutableLiveData<>();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(photoUri))
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AuthAnswer answer;
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated");
                            answer = new AuthAnswer(true, null);
                        } else {
                            Log.e(TAG, "error updating profile");
                            answer = new AuthAnswer(false, task.getException().getMessage());
                        }
                        answerLiveData.setValue(answer);
                    }
                });
        return answerLiveData;
    }

    public MutableLiveData<AuthAnswer> deleteUser() {
        Log.d(TAG, "deleteUser");
        final MutableLiveData<AuthAnswer> answerLiveData = new MutableLiveData<>();

        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AuthAnswer answer;
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted");
                            answer = new AuthAnswer(true, null);
                        } else {
                            Log.e(TAG, "Error deleting account");
                            answer = new AuthAnswer(false, task.getException().getMessage());
                        }
                        answerLiveData.setValue(answer);
                    }
                });
        return answerLiveData;
    }

    public void signOut() {
        Log.d(TAG, "signOut");
        mAuth.signOut();
    }
}
