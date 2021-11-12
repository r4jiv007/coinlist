package com.digital.coinlist.domain.usecase;

import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.domain.repo.CoinRepo;
import com.digital.coinlist.util.rx.SchedulerProvider;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javax.inject.Inject;


public class PriceCompareUseCase extends BaseUseCase<PriceComparisonReq, PriceItem> {

    private final CoinRepo coinRepo;

    @Inject
    public PriceCompareUseCase(
        CoinRepo coinRepo,
        SchedulerProvider schedulerProvider,
        CompositeDisposable compositeDisposable) {
        super(schedulerProvider, compositeDisposable);
        this.coinRepo = coinRepo;
    }

    @Override
    Single<PriceItem> buildUseCaseSingle(PriceComparisonReq req) {
        return coinRepo.getComparison(req);
    }
}
