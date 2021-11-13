package com.digital.coinlist.data.repo;

import com.digital.coinlist.data.database.CoinDao;
import com.digital.coinlist.data.mapper.CoinMapper;
import com.digital.coinlist.data.network.api.CoinGeckoApiService;
import com.digital.coinlist.data.network.entity.PriceComparisonApiReq;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.domain.repo.CoinRepo;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import javax.inject.Inject;

public class CoinRepoImpl implements CoinRepo {

    private final CoinGeckoApiService apiService;
    private final CoinMapper mapper;
    private final CoinDao coinDao;

    @Inject
    public CoinRepoImpl(CoinGeckoApiService apiService, CoinDao coinDao, CoinMapper mapper) {
        this.apiService = apiService;
        this.coinDao = coinDao;
        this.mapper = mapper;
    }

    @Override
    public Single<List<CoinItem>> getCoinList() {
        return apiService.getCoinList().map(mapper::getSelectableCoinList);
    }

    @Override
    public Single<List<CurrencyItem>> getCurrencyList() {
        return apiService.getSupportedCurrencies().map(mapper::getSelectableCurrencyList);
    }

    @Override
    public Single<PriceItem> getComparison(PriceComparisonReq req) {
        PriceComparisonApiReq comparisonApiReq = mapper.getComparisonReq(req);
        return apiService.getPriceDetail(
            comparisonApiReq.getCryptoId(),
            comparisonApiReq.getVsCurrencyId(),
            comparisonApiReq.isIncludeMarketCap(),
            comparisonApiReq.isInclude24hrVol(),
            comparisonApiReq.isInclude24hrChange(),
            comparisonApiReq.isIncludeLastUpdateAt()
        ).map(priceMap -> mapper.getPriceItem(req, priceMap));
    }
}
