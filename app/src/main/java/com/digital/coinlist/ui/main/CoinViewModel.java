package com.digital.coinlist.ui.main;

import androidx.lifecycle.LiveData;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.domain.entity.SelectableCoinListItem;
import com.digital.coinlist.domain.entity.SelectableCurrencyListItem;
import com.digital.coinlist.domain.usecase.GetCoinListUseCase;
import com.digital.coinlist.domain.usecase.GetCurrencyListUseCase;
import com.digital.coinlist.domain.usecase.PriceCompareUseCase;
import com.digital.coinlist.ui.base.BaseViewModel;
import com.digital.coinlist.ui.base.StateLiveData;
import com.digital.coinlist.ui.base.UiState;
import com.digital.coinlist.ui.main.adapter.Selectable;
import com.digital.coinlist.ui.main.list.CurrencyType;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.ResourceSingleObserver;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class CoinViewModel extends BaseViewModel {

    private final GetCoinListUseCase getCoinListUseCase;
    private final GetCurrencyListUseCase getCurrencyListUseCase;
    private final PriceCompareUseCase priceCompareUseCase;
    private Disposable disposable;
    private final StateLiveData<List<? extends Selectable>> coinListData = new StateLiveData<>();

    public LiveData<UiState<List<? extends Selectable>>> getCoinListData() {
        return coinListData;
    }

    private final StateLiveData<PriceItem> priceComparisonState = new StateLiveData<>();

    public LiveData<UiState<PriceItem>> getPriceComparisonState() {
        return priceComparisonState;
    }

    private final BehaviorSubject<SelectableCoinListItem> coinSubject = BehaviorSubject.create();
    private final BehaviorSubject<SelectableCurrencyListItem> currencySubject = BehaviorSubject
        .create();

    @Inject
    public CoinViewModel(
        GetCoinListUseCase coinListUseCase,
        GetCurrencyListUseCase currencyListUseCase,
        PriceCompareUseCase priceCompareUseCase
    ) {
        super(coinListUseCase, currencyListUseCase, priceCompareUseCase);
        this.getCoinListUseCase = coinListUseCase;
        this.getCurrencyListUseCase = currencyListUseCase;
        this.priceCompareUseCase = priceCompareUseCase;
        listenToSelection();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void loadItemList(CurrencyType currencyType) {
        if (currencyType == CurrencyType.CRYPTO_CURRENCY) {
            getCoinList();
        } else {
            getCurrencyList();
        }
    }

    private void listenToSelection() {
        disposable = Observable.combineLatest(
            coinSubject,
            currencySubject,
            (coin, currency) -> new PriceComparisonReq(coin.getId(), currency.displayName())
        ).subscribe(this::comparePrice);
    }

    private void getCoinList() {
        getCoinListUseCase.execute(new ResourceSingleObserver<List<SelectableCoinListItem>>() {
            @Override
            public void onSuccess(@NonNull List<SelectableCoinListItem> selectableCoinListItems) {
                coinListData.postSuccess(selectableCoinListItems);
            }

            @Override
            public void onError(@NonNull Throwable error) {
                coinListData.postError(error);
            }
        }, null);
    }

    private void getCurrencyList() {
        getCurrencyListUseCase
            .execute(new ResourceSingleObserver<List<SelectableCurrencyListItem>>() {
                @Override
                public void onSuccess(
                    @NonNull List<SelectableCurrencyListItem> selectableCoinListItems) {
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
            coinSubject.onNext((SelectableCoinListItem) item);
        } else {
            currencySubject.onNext((SelectableCurrencyListItem) item);
        }
    }

    private void comparePrice(PriceComparisonReq req) {
        priceCompareUseCase.execute(new ResourceSingleObserver<PriceItem>() {
            @Override
            public void onSuccess(@NonNull PriceItem priceItem) {
                priceComparisonState.postSuccess(priceItem);
            }

            @Override
            public void onError(@NonNull Throwable error) {
                priceComparisonState.postError(error);
            }
        }, req);
    }


}