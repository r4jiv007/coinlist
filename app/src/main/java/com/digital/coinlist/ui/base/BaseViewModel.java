package com.digital.coinlist.ui.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.digital.coinlist.domain.usecase.DisposableUseCase;

public abstract class BaseViewModel extends ViewModel {

    private final DisposableUseCase[] disposableUseCases;
//    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
//    private final MutableLiveData<Throwable> error = new MutableLiveData<>();
//
//    public LiveData<Boolean> getLoadingStatus() {
//        return isLoading;
//    }
//
//    public LiveData<Throwable> getError() {
//        return error;
//    }

    public BaseViewModel(DisposableUseCase... useCases) {
        this.disposableUseCases = useCases;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        for (DisposableUseCase useCase : disposableUseCases) {
            useCase.onDispose();
        }
    }
}
