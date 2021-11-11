package com.digital.coinlist.data.network.api;

import com.digital.coinlist.data.network.entity.CoinListItem;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Map;
import retrofit2.http.GET;

public interface CoinGeckoApiService {

    @GET("coins/list")
    Single<List<CoinListItem>> getCoinList();

    @GET("simple/supported_vs_currencies")
    Single<List<String>> getSupportedCurrencies();

    @GET("simple/price")
    Single<Map<String, Map<String, Double>>> getPriceDetail();
}
