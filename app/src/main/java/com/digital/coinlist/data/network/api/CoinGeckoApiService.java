package com.digital.coinlist.data.network.api;

import com.digital.coinlist.data.network.entity.CoinListItem;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinGeckoApiService {

    @GET("coins/list")
    Single<List<CoinListItem>> getCoinList();

    @GET("simple/supported_vs_currencies")
    Single<List<String>> getSupportedCurrencies();

    @GET("simple/price")
    Single<Map<String, Map<String, Double>>> getPriceDetail(
        @Query("ids") String cryptoId,
        @Query("vs_currencies") String vsCurrency,
        @Query("include_market_cap") Boolean includeMarketCap,
        @Query("include_24hr_vol") Boolean include24hrVolume,
        @Query("include_24hr_change") Boolean include24hrChange,
        @Query("include_last_updated_at") Boolean includeLastUpdateAt
    );

}
