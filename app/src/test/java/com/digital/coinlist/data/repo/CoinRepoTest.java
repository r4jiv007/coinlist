package com.digital.coinlist.data.repo;

import static org.junit.Assert.assertFalse;

import com.digital.coinlist.data.database.CoinDao;
import com.digital.coinlist.data.database.CurrencyDao;
import com.digital.coinlist.data.database.entity.CoinDbEntity;
import com.digital.coinlist.data.database.entity.CurrencyDbEntity;
import com.digital.coinlist.data.mapper.CoinMapper;
import com.digital.coinlist.data.network.api.CoinGeckoApiService;
import com.digital.coinlist.data.network.entity.CoinListItem;
import com.digital.coinlist.data.network.entity.PriceComparisonApiReq;
import com.digital.coinlist.data.util.TestHelper;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.domain.repo.CoinRepo;
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CoinRepoTest {

    @Mock
    CoinDao coinDao;
    @Mock
    CurrencyDao currencyDao;
    @Mock
    CoinGeckoApiService apiService;
    @Mock
    CoinMapper coinMapper;
    CoinRepo coinRepo;

    List<CoinListItem> coinApiResp;
    List<CoinDbEntity> coinDBEntityList;
    List<CoinItem> coinDomainList;

    List<String> currencyApiResp;
    List<CurrencyDbEntity> currencyDBEntityList;
    List<CurrencyItem> currencyItems;

    @Before
    public void setUp() throws Exception {
        coinApiResp = TestHelper.getCoinListItem();
        coinDBEntityList = TestHelper.getCoinDbEntities();
        coinDomainList = TestHelper.getCoinItems();

        currencyApiResp = TestHelper.getCurrencyApiResp();
        currencyDBEntityList = TestHelper.getCurrencyDBItemList();
        currencyItems = TestHelper.getCurrencyItemList();

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

        this.coinRepo = new CoinRepoImpl(apiService, coinDao, currencyDao, coinMapper);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCoinList() {
        // Other preparations
        Mockito.doReturn(Single.just(coinApiResp))
            .when(apiService)
            .getCoinList();
        Mockito.doReturn(coinDBEntityList)
            .when(coinMapper)
            .getCoinDbItem(coinApiResp);
        Mockito.doReturn(coinDomainList)
            .when(coinMapper)
            .getCoinItemListFromApi(coinApiResp);

        List<CoinItem> coinItemList = coinRepo.getCoinList(true).blockingGet();

        Mockito.verify(apiService, Mockito.times(1)).getCoinList();
        Mockito.verify(coinMapper, Mockito.times(1)).getCoinDbItem(coinApiResp);
        Mockito.verify(coinDao, Mockito.times(1)).insertCoins(coinDBEntityList);
        Mockito.verify(coinDao, Mockito.times(0)).getAllCoin();
        Mockito.verify(coinMapper, Mockito.times(1)).getCoinItemListFromApi(coinApiResp);
    }


    @Test
    public void searchCoinList() {
        Mockito.doReturn(Single.just(coinDBEntityList))
            .when(coinDao)
            .getAllCoin("search");

        List<CoinItem> coinItemList = coinRepo.searchCoinList("search").blockingGet();
        Mockito.verify(coinDao, Mockito.times(1)).getAllCoin("search");
        Mockito.verify(coinMapper, Mockito.times(1)).getCoinItemListFromDb(coinDBEntityList);
    }

    @Test
    public void getCurrencyList() {
        Mockito.doReturn(Single.just(currencyApiResp))
            .when(apiService)
            .getSupportedCurrencies();
        Mockito.doReturn(currencyDBEntityList)
            .when(coinMapper)
            .getCurrencyDbItemList(currencyApiResp);
        Mockito.doReturn(currencyItems)
            .when(coinMapper)
            .getCurrencyItemList(currencyApiResp);

        List<CurrencyItem> coinItemList = coinRepo.getCurrencyList(true).blockingGet();

        Mockito.verify(apiService, Mockito.times(1)).getSupportedCurrencies();
        Mockito.verify(coinMapper, Mockito.times(1)).getCurrencyDbItemList(currencyApiResp);
        Mockito.verify(currencyDao, Mockito.times(1)).insertCurrencies(currencyDBEntityList);
        Mockito.verify(currencyDao, Mockito.times(0)).getAllCurrency();
        Mockito.verify(coinMapper, Mockito.times(1)).getCurrencyItemList(currencyApiResp);

    }

    @Test
    public void searchCurrencyList() {
        Mockito.doReturn(Single.just(currencyDBEntityList))
            .when(currencyDao)
            .getAllCurrency("search");

        List<CurrencyItem> itemList = coinRepo.searchCurrencyList("search").blockingGet();
        Mockito.verify(currencyDao, Mockito.times(1)).getAllCurrency("search");
        Mockito.verify(coinMapper, Mockito.times(1))
            .getCurrencyItemListFromDb(currencyDBEntityList);
    }

    @Test
    public void getComparison() {
        Map<String, Map<String, Double>> compareApiResp = TestHelper.getPriceMap();
        PriceComparisonReq comparisonReq = new PriceComparisonReq("3x-long-ethereum-token", "btc");
        PriceComparisonApiReq apiReq = new PriceComparisonApiReq("3x-long-ethereum-token", "btc");
        PriceItem priceItem = TestHelper.getPriceItem();

        Mockito.doReturn(apiReq)
            .when(coinMapper)
            .getComparisonReq(comparisonReq);

        Mockito.doReturn(priceItem)
            .when(coinMapper)
            .getPriceItem(comparisonReq,compareApiResp);

        Mockito.doReturn(Single.just(compareApiResp))
            .when(apiService)
            .getPriceDetail(
                "3x-long-ethereum-token",
                "btc",
                true,
                true,
                true,
                true
            );
        PriceItem resp = coinRepo
            .getComparison(comparisonReq).blockingGet();

        Mockito.verify(apiService, Mockito.times(1)).getPriceDetail(
            "3x-long-ethereum-token",
            "btc",
            true,
            true,
            true,
            true
        );

        Mockito.verify(coinMapper, Mockito.times(1)).getPriceItem(comparisonReq, compareApiResp);
    }
}