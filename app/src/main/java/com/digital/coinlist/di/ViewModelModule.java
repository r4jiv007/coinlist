package com.digital.coinlist.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@InstallIn(ViewModelComponent.class)
@Module
public class ViewModelModule {

    @Provides
    CompositeDisposable providesCompositeDisposable() {
        return new CompositeDisposable();
    }
}
