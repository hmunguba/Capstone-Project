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

    //private final FirebaseRepository mRepository = new FirebaseRepository();
    private FirebaseRepository mRepository;

    private MutableLiveData<Condominium> cond;
    private MutableLiveData<List<Condominium>> conds;

    public LiveData<Condominium> getCond() {
        if (cond == null) {
            cond = new MutableLiveData<>();
            loadCond();
        }
        return cond;
    }

    public LiveData<List<Condominium>> getConds() {
        if (conds == null) {
            conds = new MutableLiveData<>();
            loadConds();
        }
        return conds;
    }

    public void loadCond() {

    }

    public LiveData<List<Condominium>> loadConds() {
        mRepository = new FirebaseRepository();
        mRepository.addListener(new FirebaseRepository.FirebaseRepositoryCallback() {
            @Override
            public void onSuccess(List result) {
                conds.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                conds.setValue(null);
            }
        });
        Log.d(TAG, "getting condominiums from database");
        List<Condominium> list = mRepository.queryConds();

        Log.d(TAG, "list is " + list);

        for (Condominium c : list) {
            Log.d(TAG, "cond is " + c.getName());
        }
        //List<String> list = mRepository.makeQueryByChild("condominiums", "name");
        conds.setValue(list);
        return conds;
    }
}
