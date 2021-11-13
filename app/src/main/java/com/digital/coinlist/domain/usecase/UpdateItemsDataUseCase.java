package com.digital.coinlist.domain.usecase;

import com.digital.coinlist.domain.repo.CoinRepo;
import com.digital.coinlist.util.rx.SchedulerProvider;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javax.inject.Inject;


public class UpdateItemsDataUseCase extends CompletableUseCase<Boolean> {

    private final CoinRepo coinRepo;

    @Inject
    public UpdateItemsDataUseCase(
        CoinRepo coinRepo,
        SchedulerProvider schedulerProvider,
        CompositeDisposable compositeDisposable) {
        super(schedulerProvider, compositeDisposable);
        this.coinRepo = coinRepo;
    }

    @Override
    Completable buildUseCaseCompletable(Boolean force) {
        return Single.zip(coinRepo.getCoinList(force), coinRepo.getCurrencyList(force),
            (coinItems, currencyItems) -> coinItems.isEmpty() && currencyItems.isEmpty())
            .ignoreElement();
    }
}
