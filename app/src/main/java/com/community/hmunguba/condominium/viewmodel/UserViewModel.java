package com.community.hmunguba.condominium.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.community.hmunguba.condominium.service.model.User;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> user;

    public LiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
            loadUser();
        }
        return user;
    }

    public void loadUser() {

    }
}
