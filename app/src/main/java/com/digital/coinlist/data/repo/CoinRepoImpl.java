package com.digital.coinlist.data.repo;

import com.digital.coinlist.data.database.CoinDao;
import com.digital.coinlist.data.database.CurrencyDao;
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
    private final CurrencyDao currencyDao;

    @Inject
    public CoinRepoImpl(CoinGeckoApiService apiService, CoinDao coinDao, CurrencyDao currencyDao,
        CoinMapper mapper) {
        this.apiService = apiService;
        this.coinDao = coinDao;
        this.mapper = mapper;
        this.currencyDao = currencyDao;
    }

    @Override
    public Single<List<CoinItem>> getCoinList(boolean forceRemote) {
        if (forceRemote) {
            return getCoinFromApiAndUpdateDb();
        }
        // fetch db if empty call api and then update db
        return getCoinFromDb();
    }

    private Single<List<CoinItem>> getCoinFromApiAndUpdateDb() {
        return apiService.getCoinList().map(coinListItems -> {
            coinDao.insertCoins(mapper.getCoinDbItem(coinListItems));
            return mapper.getCoinItemListFromApi(coinListItems);
        });
    }

    private Single<List<CoinItem>> getCoinFromDb() {
        return coinDao.getAllCoin().filter(dbEntityList -> !dbEntityList.isEmpty())
            .switchIfEmpty(apiService.getCoinList().map(mapper::getCoinDbItem)).map(
                dbEntityList -> {
                    coinDao.insertCoins(dbEntityList);
                    return mapper.getCoinItemListFromDb(dbEntityList);
                });
    }

    @Override
    public Single<List<CoinItem>> searchCoinList(String search) {
        return coinDao.getAllCoin(search).map(mapper::getCoinItemListFromDb);
    }

    @Override
    public Single<List<CurrencyItem>> getCurrencyList(boolean forceRemote) {
        if (forceRemote) {
            return getCurrencyFromApiAndUpdateDb();
        }
        // fetch db if empty call api and then update db
        return getCurrencyFromDb();
    }

    @Override
    public Single<List<CurrencyItem>> searchCurrencyList(String search) {
        return currencyDao.getAllCurrency(search).map(mapper::getCurrencyItemListFromDb);
    }

    private Single<List<CurrencyItem>> getCurrencyFromApiAndUpdateDb() {
        return apiService.getSupportedCurrencies().map(currencyItems -> {
            currencyDao.insertCurrencies(mapper.getCurrencyDbItemList(currencyItems));
            return mapper.getCurrencyItemList(currencyItems);
        });
    }

    private Single<List<CurrencyItem>> getCurrencyFromDb() {
        return currencyDao.getAllCurrency().filter(dbEntityList -> !dbEntityList.isEmpty())
            .switchIfEmpty(apiService.getSupportedCurrencies().map(mapper::getCurrencyDbItemList))
            .map(
                dbEntityList -> {
                    currencyDao.insertCurrencies(dbEntityList);
                    return mapper.getCurrencyItemListFromDb(dbEntityList);
                });
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
