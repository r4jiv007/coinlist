package com.digital.coinlist.domain.entity;

import com.digital.coinlist.ui.main.adapter.Selectable;

public class CoinItem implements Selectable {

    private final String symbol;

    private final String name;

    private final String id;

    public CoinItem(String symbol, String name, String id) {
        this.symbol = symbol;
        this.name = name;
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String displayName() {
        return name;
    }
}
