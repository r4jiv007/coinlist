package com.digital.coinlist.ui.main.list;

import androidx.lifecycle.LiveData;
import com.digital.coinlist.domain.entity.SelectableCoinListItem;
import com.digital.coinlist.domain.entity.SelectableCurrencyListItem;
import com.digital.coinlist.domain.usecase.GetCoinListUseCase;
import com.digital.coinlist.domain.usecase.GetCurrencyListUseCase;
import com.digital.coinlist.ui.base.BaseViewModel;
import com.digital.coinlist.ui.base.StateLiveData;
import com.digital.coinlist.ui.base.UiState;
import com.digital.coinlist.ui.main.adapter.Selectable;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.ResourceSingleObserver;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class CoinListViewModel extends BaseViewModel {

    private final GetCoinListUseCase getCoinListUseCase;
    private final GetCurrencyListUseCase getCurrencyListUseCase;

    private final StateLiveData<List<? extends Selectable>> coinListData = new StateLiveData<>();

    public LiveData<UiState<List<? extends Selectable>>> getCoinListData() {
        return coinListData;
    }

    @Inject
    public CoinListViewModel(
        GetCoinListUseCase coinListUseCase,
        GetCurrencyListUseCase currencyListUseCase) {
        super(coinListUseCase, currencyListUseCase);
        this.getCoinListUseCase = coinListUseCase;
        this.getCurrencyListUseCase = currencyListUseCase;
//        getCoinList();

        getCurrencyList();
    }

    void getCoinList() {
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

    void getCurrencyList() {
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
}