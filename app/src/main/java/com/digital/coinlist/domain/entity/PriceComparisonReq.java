package com.digital.coinlist.domain.entity;

public class PriceComparisonReq {

    private final String cryptoId;
    private final String currencyId;

    public PriceComparisonReq(String cryptoId, String currencyId) {
        this.cryptoId = cryptoId;
        this.currencyId = currencyId;
    }

    public String getCryptoId() {
        return cryptoId;
    }

    public String getCurrencyId() {
        return currencyId;
    }
}
