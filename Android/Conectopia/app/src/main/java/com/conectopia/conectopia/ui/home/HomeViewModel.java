package com.conectopia.conectopia.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    public final MutableLiveData<String> mText;
    public final MutableLiveData<String> mServerName;
    public final MutableLiveData<String> mDescription;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        mServerName = new MutableLiveData<>();
        mDescription = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getServerName() {
        return mServerName;
    }

    public LiveData<String> getDescription() {
        return mDescription;
    }
}