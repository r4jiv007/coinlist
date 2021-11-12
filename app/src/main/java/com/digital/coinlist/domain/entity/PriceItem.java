package com.digital.coinlist.domain.entity;

public class PriceItem {

    private final String cryptoCurrency;
    private final String currency;
    private final double price;
    private final double marketCap;
    private final double _24hVol;
    private final double _24hChange;
    private final long lastUpdated;

    public PriceItem(String cryptoCurrency, String currency, double price, double marketCap,
        double _24hVol, double _24hChange, long lastUpdated) {
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

    public double getPrice() {
        return price;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double get_24hVol() {
        return _24hVol;
    }

    public double get_24hChange() {
        return _24hChange;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}
