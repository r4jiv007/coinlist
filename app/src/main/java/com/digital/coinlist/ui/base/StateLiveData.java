package com.digital.coinlist.ui.base;


import androidx.lifecycle.MutableLiveData;

public class StateLiveData<T> extends MutableLiveData<UiState<T>> {

    public void postLoading() {
        postValue(new UiState<T>().loading());
    }

    public void postError(Throwable throwable) {
        postValue(new UiState<T>().error(throwable));
    }

    public void postSuccess(T data) {
        postValue(new UiState<T>().success(data));
    }

    public void postComplete() {
        postValue(new UiState<T>().complete());
    }

}