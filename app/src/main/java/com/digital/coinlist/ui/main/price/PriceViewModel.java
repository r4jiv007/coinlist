package com.digital.coinlist.ui.main.price;

import android.util.Log;
import androidx.lifecycle.LiveData;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.domain.usecase.PriceCompareUseCase;
import com.digital.coinlist.domain.usecase.UpdateItemsDataUseCase;
import com.digital.coinlist.ui.base.BaseViewModel;
import com.digital.coinlist.ui.base.StateLiveData;
import com.digital.coinlist.ui.base.UiState;
import com.digital.coinlist.util.rx.SchedulerProvider;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.observers.ResourceCompletableObserver;
import io.reactivex.rxjava3.observers.ResourceSingleObserver;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@HiltViewModel
public class PriceViewModel extends BaseViewModel {


    private static final long UPDATE_INTERVAL = 10L;
    private final BehaviorSubject<CoinItem> coinSubject = BehaviorSubject.create();
    private final BehaviorSubject<CurrencyItem> currencySubject = BehaviorSubject
        .create();
    private final PriceCompareUseCase priceCompareUseCase;
    private final UpdateItemsDataUseCase updateDataUseCase;
    private final SchedulerProvider schedulerProvider;
    private Disposable periodicDisposable;
    private Disposable compareReqDisposable;

    @Inject
    public PriceViewModel(PriceCompareUseCase priceCompareUseCase,
        UpdateItemsDataUseCase updateDataUseCase,
        SchedulerProvider schedulerProvider) {
        super(priceCompareUseCase, updateDataUseCase);
        this.priceCompareUseCase = priceCompareUseCase;
        this.updateDataUseCase = updateDataUseCase;
        this.schedulerProvider = schedulerProvider;
        updateLocalData();
        listenToSelection();
    }

    private void listenToSelection() {
        compareReqDisposable = Observable.combineLatest(
            coinSubject,
            currencySubject,
            (coin, currency) -> new PriceComparisonReq(coin.getId(), currency.displayName())
        ).subscribe(req -> comparePrice(true, true, req));
    }

    private final StateLiveData<PriceItem> priceComparisonState = new StateLiveData<>();

    public LiveData<UiState<PriceItem>> getPriceComparisonState() {
        return priceComparisonState;
    }

    private final StateLiveData<Void> updateLocalDataState = new StateLiveData<>();

    public LiveData<UiState<Void>> getUpdateLocalDataState() {
        return updateLocalDataState;
    }


    public void refreshPrice() {
        comparePrice(false, false, getCurrentReq());
    }

    private void comparePrice(boolean showLoading, boolean newComparison, PriceComparisonReq req) {
        if (req == null) {
            priceComparisonState.postComplete();
            return;
        }
        if (showLoading) {
            priceComparisonState.postLoading();
        }
        if (newComparison) {
            stopPeriodicUpdate();
        }
        priceCompareUseCase.execute(new ResourceSingleObserver<PriceItem>() {
            @Override
            public void onSuccess(@NonNull PriceItem priceItem) {
                priceComparisonState.postSuccess(priceItem);
                startPeriodicUpdate();
            }

            @Override
            public void onError(@NonNull Throwable error) {
                priceComparisonState.postError(error);
                stopPeriodicUpdate();
            }
        }, req);
    }


    public void startPeriodicUpdate() {
        if (periodicDisposable != null && !periodicDisposable.isDisposed()) {
            return;
        }
        periodicDisposable = Observable.interval(UPDATE_INTERVAL,
            TimeUnit.SECONDS,
            schedulerProvider.computation())
            .observeOn(schedulerProvider.ui()).doOnDispose(new Action() {
                @Override
                public void run() throws Throwable {
                    Log.d("periodic_update", "disposed");
                }
            }).subscribe(time -> {
                Log.d("periodic_update", "" + time);
                comparePrice(false, false, getCurrentReq());
            });
    }

    private PriceComparisonReq getCurrentReq() {
        CoinItem coinItem = coinSubject.getValue();
        CurrencyItem currencyItem = currencySubject.getValue();
        if (coinItem == null || currencyItem == null) {
            return null;
        }
        return new PriceComparisonReq(coinItem.getId(), currencyItem.displayName());
    }

    public void stopPeriodicUpdate() {
        if (periodicDisposable != null && !periodicDisposable.isDisposed()) {
            periodicDisposable.dispose();
            periodicDisposable = null;
        }
    }

    public void submitCoin(CoinItem coinItem) {
        coinSubject.onNext(coinItem);
    }

    public void submitCurrency(CurrencyItem currencyItem) {
        currencySubject.onNext(currencyItem);
    }

    public void updateLocalData() {
        updateLocalDataState.postLoading();
        updateDataUseCase.execute(new ResourceCompletableObserver() {
            @Override
            public void onComplete() {
                updateLocalDataState.postSuccess(null);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                updateLocalDataState.postError(e);
            }
        }, true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopPeriodicUpdate();
        if (compareReqDisposable != null && !compareReqDisposable.isDisposed()) {
            compareReqDisposable.dispose();
        }
    }
}
