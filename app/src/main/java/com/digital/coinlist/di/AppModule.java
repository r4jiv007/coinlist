package com.digital.coinlist.di;

import com.digital.coinlist.util.rx.AppSchedulerProvider;
import com.digital.coinlist.util.rx.SchedulerProvider;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@InstallIn(SingletonComponent.class)
@Module
public interface AppModule {

    @Binds
    @Singleton
    SchedulerProvider bindSchedulerProvide(AppSchedulerProvider schedulerProvider);
}
