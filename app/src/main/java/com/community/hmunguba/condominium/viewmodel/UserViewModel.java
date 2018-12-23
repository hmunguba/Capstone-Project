package com.community.hmunguba.condominium.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.User;
import com.community.hmunguba.condominium.service.model.repo.FirebaseResidentRepository;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();

    private MutableLiveData<User> user;

    public LiveData<User> getUserById(String id) {
        if (user == null) {
            user = new MutableLiveData<>();
            loadUser(id);
        }
        return user;
    }

    public MutableLiveData<User> loadUser(String userId) {
        Log.d(TAG, "loadUser");
        user = FirebaseResidentRepository.getInstance().queryUser(userId);

        return user;
    }

    public LiveData<Boolean> createUser(User user) {
        Log.d(TAG, "createNewUser " + user.getFirstName());
        LiveData<Boolean> isUserCreated = FirebaseResidentRepository.getInstance().createNewUser(user);
        getUserById(user.getUserId());

        return isUserCreated;
    }

    public LiveData<Boolean> deleteUser(String id) {
        Log.d(TAG, "deleteUser " + id);
        LiveData<Boolean> isUserDeleted = FirebaseResidentRepository.getInstance().deleteUser(id);
        return isUserDeleted;
    }
}
