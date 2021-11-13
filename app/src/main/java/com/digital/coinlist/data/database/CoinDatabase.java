package com.digital.coinlist.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.digital.coinlist.data.database.entity.CoinDbEntity;
import com.digital.coinlist.data.database.entity.CurrencyDbEntity;


@Database(entities = {CoinDbEntity.class, CurrencyDbEntity.class}, version = 1)
public abstract class CoinDatabase extends RoomDatabase {

    public abstract CoinDao getCoinDao();

    public abstract CurrencyDao getCurrencyDao();
}