package com.digital.coinlist.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

@Dao
public interface CurrencyDao {

    @Query("SELECT * FROM currency_table")
    Single<List<CurrencyDbEntity>> getAllCurrency();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCurrencies(List<CurrencyDbEntity> currencyList);

    @Query("SELECT * FROM currency_table WHERE name LIKE '%' || :search || '%'")
    Single<List<CurrencyDbEntity>> getAllCurrency(String search);
}
