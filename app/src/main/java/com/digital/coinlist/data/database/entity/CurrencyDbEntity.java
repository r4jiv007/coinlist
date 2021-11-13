package com.digital.coinlist.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "currency_table")
public class CurrencyDbEntity {


    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "name")
    private String name;

    public CurrencyDbEntity(@NonNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}