package com.metrox.demo64.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> selectedItem = new MutableLiveData<>();

    public MutableLiveData<String> getSelectedItem() {
        return selectedItem;
    }

    public void selectItem(String item) {
        selectedItem.setValue(item);
    }
}