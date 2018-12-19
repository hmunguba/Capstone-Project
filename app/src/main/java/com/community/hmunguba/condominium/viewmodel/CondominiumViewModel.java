package com.community.hmunguba.condominium.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.community.hmunguba.condominium.service.model.Condominium;
import com.community.hmunguba.condominium.service.model.FirebaseRepository;

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

    public LiveData<Condominium> createNewCond(Condominium condominium) {
        Log.d(TAG, "createNewCond");
        FirebaseRepository.getInstance().createNewCond(condominium);
        getCondById(condominium.getCondId());

        return cond;
    }

    public LiveData<Condominium> loadCond(String condId) {
        Log.d(TAG, "loadCond");
        cond = FirebaseRepository.getInstance().queryCond(condId);

        return cond;
    }

    public LiveData<Boolean> hasCondInDatabase(String condId) {
        LiveData<Boolean> exists = FirebaseRepository.getInstance().checkIfCondExists(condId);
        Log.d(TAG, "does cond exists? " + exists.getValue());
        return exists;
    }

    public LiveData<List<String>> loadCondsNameList() {
        Log.d(TAG, "loadCondsNameList");
        condsNameList = FirebaseRepository.getInstance().queryCondsNames();

        return condsNameList;
    }
}
