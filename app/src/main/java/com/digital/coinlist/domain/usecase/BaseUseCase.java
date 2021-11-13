package com.digital.coinlist.domain.usecase;

import com.digital.coinlist.util.rx.SchedulerProvider;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.ResourceSingleObserver;

public abstract class BaseUseCase<REQ, RES> implements DisposableUseCase{

    private Disposable lastDisposable;

    private CompositeDisposable compositeDisposable;

    private SchedulerProvider schedulerProvider;

    public BaseUseCase(SchedulerProvider schedulerProvider,
        CompositeDisposable compositeDisposable) {
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void onDispose() {
        compositeDisposable.clear();
    }

    public void disposeLast() {
        if (lastDisposable != null && !lastDisposable.isDisposed()) {
            lastDisposable.dispose();
        }
    }

    abstract Single<RES> buildUseCaseSingle(REQ req);

    public void execute(ResourceSingleObserver<RES> observer, REQ req) {
        lastDisposable = buildUseCaseSingle(req)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeWith(observer);
        compositeDisposable.add(lastDisposable);
    }
}
