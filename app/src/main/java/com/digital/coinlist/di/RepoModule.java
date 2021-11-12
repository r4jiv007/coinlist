package com.digital.coinlist.di;

import com.digital.coinlist.data.repo.CoinRepoImpl;
import com.digital.coinlist.domain.repo.CoinRepo;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@InstallIn(SingletonComponent.class)
@Module
public interface RepoModule {

    @Binds
    @Singleton
    CoinRepo bindCoinRepo(CoinRepoImpl rep);
}