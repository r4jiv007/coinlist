package com.digital.coinlist.data.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "coin_table")
public class CoinDbEntity {

    @NonNull
    @ColumnInfo(name = "symbol")
    private String symbol;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public CoinDbEntity(@NonNull String symbol, @NonNull String name, @NonNull String id) {
        this.symbol = symbol;
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
