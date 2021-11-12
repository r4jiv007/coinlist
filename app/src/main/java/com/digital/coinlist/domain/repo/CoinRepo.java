package com.digital.coinlist.domain.repo;

import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public interface CoinRepo {

    Single<List<CoinItem>> getCoinList();

    Single<List<CurrencyItem>> getCurrencyList();

    Single<PriceItem> getComparison(PriceComparisonReq comparisonReq);
}
