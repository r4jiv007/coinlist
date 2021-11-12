package com.digital.coinlist.domain.entity;

import com.digital.coinlist.ui.main.adapter.Selectable;

public class SelectableCurrencyListItem implements Selectable {

    private final String currency;

    public SelectableCurrencyListItem(String currency) {
        this.currency = currency;
    }

    @Override
    public String displayName() {
        return currency;
    }
}
