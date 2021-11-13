package com.digital.coinlist.di;

import android.content.Context;
import androidx.room.Room;
import com.digital.coinlist.data.database.CoinDao;
import com.digital.coinlist.data.database.CoinDatabase;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@InstallIn(SingletonComponent.class)
@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public CoinDatabase providesCoinDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, CoinDatabase.class, "coin-database").build();
    }

    @Provides
    public CoinDao providesCoinDao(CoinDatabase database) {
        return database.getCoinDao();
    }
}