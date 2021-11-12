package com.digital.coinlist.data.network.entity;

public class PriceComparisonApiReq {

    private final String cryptoId;
    private final String vsCurrencyId;

    public PriceComparisonApiReq(String cryptoId, String vsCurrencyId) {
        this.cryptoId = cryptoId;
        this.vsCurrencyId = vsCurrencyId;
    }

    public String getCryptoId() {
        return cryptoId;
    }

    public String getVsCurrencyId() {
        return vsCurrencyId;
    }

    public boolean isIncludeMarketCap() {
        return true;
    }

    public boolean isInclude24hrVol() {
        return true;
    }

    public boolean isInclude24hrChange() {
        return true;
    }

    public boolean isIncludeLastUpdateAt() {
        return true;
    }
}
