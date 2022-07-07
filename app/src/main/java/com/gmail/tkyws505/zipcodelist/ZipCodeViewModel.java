package com.gmail.tkyws505.zipcodelist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ZipCodeViewModel extends ViewModel {
  private MutableLiveData<List<ZipCodeModel>> liveDataZipCode;

  public MutableLiveData<List<ZipCodeModel>> getLiveDataZipCode() {
    if (liveDataZipCode == null){
      liveDataZipCode = new MutableLiveData<>();
    }
    return liveDataZipCode;
  }
}
