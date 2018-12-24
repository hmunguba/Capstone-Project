package com.community.hmunguba.condominium.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.User;
import com.community.hmunguba.condominium.service.model.repo.FirebaseResidentRepository;

public class ResidentViewModel extends ViewModel {
    private static final String TAG = ResidentViewModel.class.getSimpleName();

    private MutableLiveData<User> resident;

    public LiveData<User> getUserById(String id) {
        if (resident == null) {
            resident = new MutableLiveData<>();
            loadUser(id);
        }
        return resident;
    }

    public MutableLiveData<User> loadUser(String userId) {
        Log.d(TAG, "loadUser");
        resident = FirebaseResidentRepository.getInstance().queryResident(userId);

        return resident;
    }

    public LiveData<Boolean> createUser(User user) {
        Log.d(TAG, "createNewUser " + user.getFirstName());
        LiveData<Boolean> isUserCreated = FirebaseResidentRepository.getInstance().createNewResident(user);
        getUserById(user.getUserId());

        return isUserCreated;
    }

    public LiveData<Boolean> deleteUser(String id) {
        Log.d(TAG, "deleteUser " + id);
        LiveData<Boolean> isUserDeleted = FirebaseResidentRepository.getInstance().deleteResident(id);
        return isUserDeleted;
    }
}
