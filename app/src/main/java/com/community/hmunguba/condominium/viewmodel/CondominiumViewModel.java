package com.community.hmunguba.condominium.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.firebase.FirebaseCondRepository;

import java.util.List;

public class CondominiumViewModel extends ViewModel {
    public static final String TAG = CondominiumViewModel.class.getSimpleName();

    private MutableLiveData<Condominium> cond;
    private MutableLiveData<List<String>> condsNameList;

    public LiveData<Condominium> getCondById(String id) {
        if (cond == null) {
            cond = new MutableLiveData<>();
            loadCond(id);
        }
        return cond;
    }

    public LiveData<List<String>> getCondsNameList() {
        if (condsNameList == null) {
            condsNameList = new MutableLiveData<>();
            loadCondsNameList();
        }
        return condsNameList;
    }

    public LiveData<Boolean> createCond(Condominium condominium) {
        Log.d(TAG, "createNewCond " + condominium.getName());
        LiveData<Boolean> isCondCreated =  FirebaseCondRepository.getInstance().createNewCond(condominium);
        getCondById(condominium.getCondId());

        return isCondCreated;
    }

    public LiveData<Condominium> loadCond(String condId) {
        Log.d(TAG, "loadCond " + condId);
        cond = FirebaseCondRepository.getInstance().queryCond(condId);

        return cond;
    }

    public void udateCondServices(Condominium condominium) {
        Log.d(TAG, "udateCondServices " + condominium.getName());
        FirebaseCondRepository.getInstance().updateCondServicesInfo(condominium);
    }

    public LiveData<List<String>> loadCondsNameList() {
        Log.d(TAG, "loadCondsNameList");
        condsNameList = FirebaseCondRepository.getInstance().queryCondsNames();

        return condsNameList;
    }

    public LiveData<Boolean> checkCondExist(String condId) {
        Log.d(TAG, "does cond exists? " + condId);
        LiveData<Boolean> exists = FirebaseCondRepository.getInstance().checkIfCondExists(condId);
        Log.d(TAG, "Does cond already existed? " + exists.getValue());
        return exists;
    }

    public LiveData<Boolean> deleteCond(String id) {
        Log.d(TAG, "deleteCond + " + id);
        LiveData<Boolean> isCondDeleted = FirebaseCondRepository.getInstance().deleteCond(id);

        return  isCondDeleted;
    }
}
