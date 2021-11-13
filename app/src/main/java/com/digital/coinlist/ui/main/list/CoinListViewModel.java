package com.digital.coinlist.ui.main.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.usecase.GetCoinListUseCase;
import com.digital.coinlist.domain.usecase.GetCurrencyListUseCase;
import com.digital.coinlist.domain.usecase.SearchCoinUseCase;
import com.digital.coinlist.domain.usecase.SearchCurrencyUseCase;
import com.digital.coinlist.ui.base.BaseViewModel;
import com.digital.coinlist.ui.base.StateLiveData;
import com.digital.coinlist.ui.base.UiState;
import com.digital.coinlist.ui.main.adapter.Selectable;
import dagger.assisted.Assisted;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.ResourceSingleObserver;
import java.util.List;
import javax.inject.Inject;

@HiltViewModel
public class CoinListViewModel extends BaseViewModel {


    private final GetCoinListUseCase getCoinListUseCase;
    private final GetCurrencyListUseCase getCurrencyListUseCase;
    private final SearchCoinUseCase searchCoinUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    private final StateLiveData<List<? extends Selectable>> coinListData = new StateLiveData<>();

    public LiveData<UiState<List<? extends Selectable>>> getCoinListData() {
        return coinListData;
    }

    private final MutableLiveData<CoinItem> coinLiveData = new MutableLiveData<>();
    private final MutableLiveData<CurrencyItem> currencyLiveData = new MutableLiveData<>();

    @Inject
    public CoinListViewModel(
        GetCoinListUseCase coinListUseCase,
        GetCurrencyListUseCase currencyListUseCase,
        SearchCoinUseCase searchCoinUseCase,
        SearchCurrencyUseCase searchCurrencyUseCase
    ) {
        super(coinListUseCase,
            currencyListUseCase,
            searchCoinUseCase,
            searchCurrencyUseCase);

        this.getCoinListUseCase = coinListUseCase;
        this.getCurrencyListUseCase = currencyListUseCase;
        this.searchCoinUseCase = searchCoinUseCase;
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    public void loadItemList(CurrencyType currencyType) {
        if (currencyType == CurrencyType.COIN) {
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
        }, false);
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
            }, false);
    }

    public void submitSelectableItem(CurrencyType currencyType, Selectable item) {
        if (currencyType == CurrencyType.COIN) {
            coinLiveData.setValue((CoinItem) item);
        } else {
            currencyLiveData.setValue((CurrencyItem) item);
        }
    }

    public void searchItem(String search, CurrencyType currencyType) {
        if (currencyType == CurrencyType.COIN) {
            searchCoin(search);
        } else {
            searchCurrency(search);
        }
    }

    public void searchCoin(String search) {
        if (search == null || search.isEmpty()) {
            getCoinList();
        } else {
            searchCryptoCoin(search);
        }
    }

    private void searchCryptoCoin(String search) {
        searchCoinUseCase.execute(new ResourceSingleObserver<List<CoinItem>>() {
            @Override
            public void onSuccess(@NonNull List<CoinItem> coinItems) {
                coinListData.postSuccess(coinItems);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                coinListData.postError(e);
            }
        }, search);
    }

    private void searchCurrency(String search) {
        if (search == null || search.isEmpty()) {
            getCurrencyList();
        } else {
            searchCurrencyLocally(search);
        }
    }

    private void searchCurrencyLocally(String search) {
        searchCurrencyUseCase.execute(new ResourceSingleObserver<List<CurrencyItem>>() {
            @Override
            public void onSuccess(@NonNull List<CurrencyItem> coinItems) {
                coinListData.postSuccess(coinItems);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                coinListData.postError(e);
            }
        }, search);
    }
}