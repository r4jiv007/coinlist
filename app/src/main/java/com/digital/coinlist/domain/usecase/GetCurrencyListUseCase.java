package com.digital.coinlist.domain.usecase;

import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.repo.CoinRepo;
import com.digital.coinlist.util.rx.SchedulerProvider;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

public class GetCurrencyListUseCase extends BaseUseCase<Boolean, List<CurrencyItem>> {

    private final CoinRepo coinRepo;

    @Inject
    public GetCurrencyListUseCase(
        CoinRepo coinRepo,
        SchedulerProvider schedulerProvider,
        CompositeDisposable compositeDisposable) {
        super(schedulerProvider, compositeDisposable);
        this.coinRepo = coinRepo;
    }

    @Override
    Single<List<CurrencyItem>> buildUseCaseSingle(Boolean forceRemote) {
        return coinRepo.getCurrencyList(forceRemote);
    }
}
