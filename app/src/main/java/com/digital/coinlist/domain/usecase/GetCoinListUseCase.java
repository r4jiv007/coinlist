package com.digital.coinlist.domain.usecase;

import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.repo.CoinRepo;
import com.digital.coinlist.util.rx.SchedulerProvider;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;

public class GetCoinListUseCase extends BaseUseCase<Void, List<CoinItem>> {

    private final CoinRepo coinRepo;

    @Inject
    public GetCoinListUseCase(
        CoinRepo coinRepo,
        SchedulerProvider schedulerProvider,
        CompositeDisposable compositeDisposable) {
        super(schedulerProvider, compositeDisposable);
        this.coinRepo = coinRepo;
    }

    @Override
    Single<List<CoinItem>> buildUseCaseSingle(Void unused) {
        return coinRepo.getCoinList();
    }
}
