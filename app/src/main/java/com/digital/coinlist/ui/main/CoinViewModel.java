package com.digital.coinlist.ui.main;

import android.util.Log;
import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.domain.usecase.GetCoinListUseCase;
import com.digital.coinlist.domain.usecase.GetCurrencyListUseCase;
import com.digital.coinlist.domain.usecase.PriceCompareUseCase;
import com.digital.coinlist.ui.base.BaseViewModel;
import com.digital.coinlist.ui.base.CombinedLiveData;
import com.digital.coinlist.ui.base.StateLiveData;
import com.digital.coinlist.ui.base.UiState;
import com.digital.coinlist.ui.main.adapter.Selectable;
import com.digital.coinlist.ui.main.list.CurrencyType;
import com.digital.coinlist.util.rx.SchedulerProvider;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.observers.ResourceSingleObserver;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

@HiltViewModel
public class CoinViewModel extends BaseViewModel {

    private static final long UPDATE_INTERVAL = 10L;

    private final GetCoinListUseCase getCoinListUseCase;
    private final GetCurrencyListUseCase getCurrencyListUseCase;
    private final PriceCompareUseCase priceCompareUseCase;
    private Disposable disposable;
    private final SchedulerProvider schedulerProvider;

    private final StateLiveData<List<? extends Selectable>> coinListData = new StateLiveData<>();

    public LiveData<UiState<List<? extends Selectable>>> getCoinListData() {
        return coinListData;
    }

    private final StateLiveData<PriceItem> priceComparisonState = new StateLiveData<>();

    public LiveData<UiState<PriceItem>> getPriceComparisonState() {
        return priceComparisonState;
    }

    private final MutableLiveData<CoinItem> coinLiveData = new MutableLiveData<>();
    private final MutableLiveData<CurrencyItem> currencyLiveData = new MutableLiveData<>();
    private final CombinedLiveData<CoinItem, CurrencyItem> combinedLiveData = new CombinedLiveData<>(
        coinLiveData, currencyLiveData);

    public LiveData<Pair<CoinItem, CurrencyItem>> getCombinedLiveData() {
        return combinedLiveData;
    }

    @Inject
    public CoinViewModel(
        GetCoinListUseCase coinListUseCase,
        GetCurrencyListUseCase currencyListUseCase,
        PriceCompareUseCase priceCompareUseCase,
        SchedulerProvider schedulerProvider
    ) {
        super(coinListUseCase, currencyListUseCase, priceCompareUseCase);
        this.getCoinListUseCase = coinListUseCase;
        this.getCurrencyListUseCase = currencyListUseCase;
        this.priceCompareUseCase = priceCompareUseCase;
        this.schedulerProvider = schedulerProvider;
    }

    public void loadItemList(CurrencyType currencyType) {
        if (currencyType == CurrencyType.CRYPTO_CURRENCY) {
            getCoinList();
        } else {
            getCurrencyList();
        }
    }

    private void getCoinList() {
        coinListData.postLoading();
        getCoinListUseCase.execute(new ResourceSingleObserver<List<CoinItem>>() {
            @Override
            public void onSuccess(@NonNull List<CoinItem> coinItems) {
                coinListData.postSuccess(coinItems);
            }

            @Override
            public void onError(@NonNull Throwable error) {
                coinListData.postError(error);
            }
        }, null);
    }

    private void getCurrencyList() {
        coinListData.postLoading();
        getCurrencyListUseCase
            .execute(new ResourceSingleObserver<List<CurrencyItem>>() {
                @Override
                public void onSuccess(
                    @NonNull List<CurrencyItem> selectableCoinListItems) {
                    coinListData.postSuccess(selectableCoinListItems);
                }

                @Override
                public void onError(@NonNull Throwable error) {
                    coinListData.postError(error);
                }
            }, null);
    }

    public void submitSelectableItem(CurrencyType currencyType, Selectable item) {
        if (currencyType == CurrencyType.CRYPTO_CURRENCY) {
            coinLiveData.setValue((CoinItem) item);
        } else {
            currencyLiveData.setValue((CurrencyItem) item);
        }
    }

    public void refreshPrice() {
        comparePrice(false, false);
    }

    public void comparePrice(boolean showLoading, boolean newComparison) {
        Pair<CoinItem, CurrencyItem> pair = combinedLiveData.getValue();
        if (pair == null || pair.first == null || pair.second == null) {
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
            }
        }, new PriceComparisonReq(pair.first.getId(), pair.second.displayName()));
    }


    private void startPeriodicUpdate() {
        if (disposable != null && !disposable.isDisposed()) {
            return;
        }
        disposable = Observable.interval(UPDATE_INTERVAL,
            TimeUnit.SECONDS,
            schedulerProvider.computation())
            .observeOn(schedulerProvider.ui()).doOnDispose(new Action() {
                @Override
                public void run() throws Throwable {
                    Log.d("periodic_update", "disposed");
                }
            }).subscribe(time -> {
                Log.d("periodic_update", "" + time);
                comparePrice(false, false);
            });
    }

    private void stopPeriodicUpdate() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopPeriodicUpdate();
    }
}