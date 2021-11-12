package com.digital.coinlist.domain.entity;

public class PriceItem {

    private final String cryptoCurrency;
    private final String currency;
    private final String price;
    private final String marketCap;
    private final String _24hVol;
    private final String _24hChange;
    private final String lastUpdated;

    public PriceItem(String cryptoCurrency, String currency, String price, String marketCap,
        String _24hVol, String _24hChange, String lastUpdated) {
        this.cryptoCurrency = cryptoCurrency;
        this.currency = currency;
        this.price = price;
        this.marketCap = marketCap;
        this._24hVol = _24hVol;
        this._24hChange = _24hChange;
        this.lastUpdated = lastUpdated;
    }

    public String getCryptoCurrency() {
        return cryptoCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPrice() {
        return price;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public String get_24hVol() {
        return _24hVol;
    }

    public String get_24hChange() {
        return _24hChange;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
}
