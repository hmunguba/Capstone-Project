package com.community.hmunguba.condominium.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.community.hmunguba.condominium.service.firebase.FirebaseProfileTypeRepository;
import com.community.hmunguba.condominium.service.model.ProfileType;

public class ProfileTypeViewModel extends ViewModel {
    public static final String TAG = ProfileTypeViewModel.class.getSimpleName();

    private MutableLiveData<ProfileType> profileType;

    public LiveData<ProfileType> getProfileTypeByEmail(String email) {
        if (profileType == null) {
            profileType = new MutableLiveData<>();
            loadProfileType(email);
        }
        return profileType;
    }

    public LiveData<Boolean> createProfileType(ProfileType profileType) {
        Log.d(TAG, "createProfileType " + profileType.getEmail());
        LiveData<Boolean> isProfileSet=  FirebaseProfileTypeRepository.getInstance().createNewProfileType(profileType);
        getProfileTypeByEmail(profileType.getEmail());

        return isProfileSet;
    }

    public LiveData<ProfileType> loadProfileType(String userEmail) {
        Log.d(TAG, "loadProfileType " + userEmail);
        profileType = FirebaseProfileTypeRepository.getInstance().queryProfileType(userEmail);

        return profileType;
    }
}
