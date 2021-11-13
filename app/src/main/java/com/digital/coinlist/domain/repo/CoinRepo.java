package com.digital.coinlist.domain.repo;

import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public interface CoinRepo {

    Single<List<CoinItem>> getCoinList(boolean forceRemote);

    Single<List<CoinItem>> searchCoinList(String search);

    Single<List<CurrencyItem>> getCurrencyList(boolean forceRemote);

    Single<List<CurrencyItem>> searchCurrencyList(String search);

    Single<PriceItem> getComparison(PriceComparisonReq comparisonReq);

}
