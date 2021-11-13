package com.digital.coinlist.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

@Dao
public interface CoinDao {

    @Query("SELECT * FROM coin_table")
    Single<List<CoinDbEntity>> getAllCoin();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoins(List<CoinDbEntity> coinList);

    @Query("SELECT * FROM coin_table WHERE name LIKE '%' || :search || '%'")
    Single<List<CoinDbEntity>> getAllCoin(String search);
}
