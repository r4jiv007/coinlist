package com.digital.coinlist.domain.repo;

import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.domain.entity.SelectableCoinListItem;
import com.digital.coinlist.domain.entity.SelectableCurrencyListItem;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public interface CoinRepo {

    Single<List<SelectableCoinListItem>> getCoinList();

    Single<List<SelectableCurrencyListItem>> getCurrencyList();

    Single<PriceItem> getComparison(PriceComparisonReq comparisonReq);
}
