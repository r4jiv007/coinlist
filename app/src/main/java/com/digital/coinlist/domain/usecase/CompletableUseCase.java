package com.digital.coinlist.domain.usecase;

import com.digital.coinlist.util.rx.SchedulerProvider;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.ResourceCompletableObserver;

public abstract class CompletableUseCase<REQ> implements DisposableUseCase {

    private Disposable lastDisposable;

    private CompositeDisposable compositeDisposable;

    private SchedulerProvider schedulerProvider;

    public CompletableUseCase(SchedulerProvider schedulerProvider,
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

    abstract Completable buildUseCaseCompletable(REQ req);

    public void execute(ResourceCompletableObserver observer, REQ req) {
        lastDisposable = buildUseCaseCompletable(req)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeWith(observer);
        compositeDisposable.add(lastDisposable);
    }
}
