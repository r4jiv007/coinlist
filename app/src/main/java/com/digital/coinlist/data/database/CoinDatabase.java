package com.digital.coinlist.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {CoinDbEntity.class}, version = 1)
public abstract class CoinDatabase extends RoomDatabase {

    public abstract CoinDao getCoinDao();
}