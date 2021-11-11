package com.digital.coinlist.di;

import com.digital.coinlist.data.network.api.CoinGeckoApiService;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;
import retrofit2.Retrofit;


@InstallIn(SingletonComponent.class)
@Module
public class ApiServiceModule {

    @Provides
    @Singleton
    public CoinGeckoApiService getApiService(Retrofit retrofit) {
        return retrofit.create(CoinGeckoApiService.class);
    }
}
