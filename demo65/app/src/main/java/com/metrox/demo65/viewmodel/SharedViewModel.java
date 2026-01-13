package com.metrox.demo65.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> sharedData = new MutableLiveData<>();

    public MutableLiveData<String> getSharedData() {
        return sharedData;
    }

    public void setSharedData(String data) {
        sharedData.setValue(data);
    }
}
