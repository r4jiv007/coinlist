package com.digital.coinlist.data.mapper;

import com.digital.coinlist.data.database.entity.CoinDbEntity;
import com.digital.coinlist.data.database.entity.CurrencyDbEntity;
import com.digital.coinlist.data.network.entity.CoinListItem;
import com.digital.coinlist.data.network.entity.PriceComparisonApiReq;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CoinMapper {

    private static final String MARKET_CAP = "_market_cap";
    private static final String _24HR_VOL = "_24h_vol";
    private static final String _24HR_CHANGE = "_24h_change";
    private static final String LAST_UPDATED_AT = "last_updated_at";
    private static final long MILLIS = 1000;
    private final static DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss zzz",
        Locale.ENGLISH);
    private final static DecimalFormat numberFormat = new DecimalFormat("0");

    static {
        numberFormat.setMaximumFractionDigits(340);
    }

    @Inject
    public CoinMapper() {

    }

    private CoinItem getCoinItem(CoinListItem listItem) {
        return new CoinItem(listItem.getSymbol(), listItem.getName(),
            listItem.getId());
    }

    private CoinItem getCoinItem(CoinDbEntity coinDbEntity) {
        return new CoinItem(coinDbEntity.getSymbol(), coinDbEntity.getName(), coinDbEntity.getId());
    }

    private CoinDbEntity getCoinDbItem(CoinListItem item) {
        return new CoinDbEntity(item.getSymbol(), item.getName(), item.getId());
    }

    public List<CoinItem> getCoinItemListFromApi(List<CoinListItem> list) {
        List<CoinItem> coinItemList = new ArrayList<>();
        for (CoinListItem item : list) {
            coinItemList.add(getCoinItem(item));
        }
        return coinItemList;
    }

    public List<CoinItem> getCoinItemListFromDb(List<CoinDbEntity> dbEntityList) {
        List<CoinItem> coinItemList = new ArrayList<>();
        for (CoinDbEntity item : dbEntityList) {
            coinItemList.add(getCoinItem(item));
        }
        return coinItemList;
    }

    public List<CoinDbEntity> getCoinDbItem(List<CoinListItem> itemList) {
        List<CoinDbEntity> dbItemList = new ArrayList<>();
        for (CoinListItem item : itemList) {
            dbItemList.add(getCoinDbItem(item));
        }
        return dbItemList;
    }

    private CurrencyItem getCurrencyItem(String currency) {
        return new CurrencyItem(currency);
    }

    public List<CurrencyItem> getCurrencyItemList(List<String> list) {
        List<CurrencyItem> selectableList = new ArrayList<>();
        for (String item : list) {
            selectableList.add(getCurrencyItem(item));
        }
        return selectableList;
    }

    private CurrencyDbEntity getCurrencyDbItem(String currency) {
        return new CurrencyDbEntity(currency);
    }

    private CurrencyItem getCurrencyItemFromDb(CurrencyDbEntity currency) {
        return new CurrencyItem(currency.getName());
    }

    public List<CurrencyDbEntity> getCurrencyDbItemList(List<String> currencyList) {
        List<CurrencyDbEntity> currencyDbEntityList = new ArrayList<>();
        for (String item : currencyList) {
            currencyDbEntityList.add(getCurrencyDbItem(item));
        }
        return currencyDbEntityList;
    }

    public List<CurrencyItem> getCurrencyItemListFromDb(List<CurrencyDbEntity> currencyList) {
        List<CurrencyItem> currencyItems = new ArrayList<>();
        for (CurrencyDbEntity item : currencyList) {
            currencyItems.add(getCurrencyItemFromDb(item));
        }
        return currencyItems;
    }

    public PriceComparisonApiReq getComparisonReq(PriceComparisonReq req) {
        return new PriceComparisonApiReq(req.getCryptoId(), req.getCurrencyId());
    }

    /**
     * sample resp:- { "3x-long-ethereum-token": { "btc": 0.04366834, "btc_market_cap":
     * 1821.5931286865932, "btc_24h_vol": 185.65511077731148, "btc_24h_change": 1.6481292040465076,
     * "last_updated_at": 1636636961 } }
     */
    public PriceItem getPriceItem(
        PriceComparisonReq req,
        Map<String, Map<String, Double>> comparisonResp
    ) {
        Map<String, Double> comparisonMap;
        String price = "NA", marketCap = "NA", _24hrVol = "NA", _24hrChange = "NA", lastUpdateDate = "NA";

        if (!comparisonResp.containsKey(req.getCryptoId())
            || comparisonResp.get(req.getCryptoId()) == null) {
            return new PriceItem(
                req.getCryptoId(), req.getCurrencyId(),
                price, marketCap, _24hrVol, _24hrChange, lastUpdateDate);
        }

        final String marketCapKey = String.format("%s%s", req.getCurrencyId(), MARKET_CAP);
        final String volumeKey = String.format("%s%s", req.getCurrencyId(), _24HR_VOL);
        final String changeKey = String.format("%s%s", req.getCurrencyId(), _24HR_CHANGE);

        comparisonMap = comparisonResp.get(req.getCryptoId());

        price = getValue(req.getCurrencyId(), req.getCurrencyId(), comparisonMap);
        marketCap = getValue(marketCapKey, "", comparisonMap);
        _24hrVol = getValue(volumeKey, "", comparisonMap);
        _24hrChange = getValue(changeKey, "", comparisonMap);
        lastUpdateDate = getLastUpdatedAt(comparisonMap);

        return new PriceItem(
            req.getCryptoId()
            , req.getCurrencyId()
            , price
            , marketCap
            , _24hrVol
            , _24hrChange
            , lastUpdateDate);
    }


    private String getValue(String itemKey, String optionalSuffix, Map<String, Double> priceMap) {
        String value = "NA";
        try {
            if (priceMap.containsKey(itemKey)) {
                value = String
                    .format("%s %s", numberFormat.format(priceMap.get(itemKey)), optionalSuffix)
                    .trim();
            }
        } catch (IllegalArgumentException | NullPointerException | ArithmeticException exception) {
            value = "NA";
        }
        return value;
    }


    private String getLastUpdatedAt(Map<String, Double> priceMap) {
        final String lastUpdateKey = LAST_UPDATED_AT;
        String value = "NA";

        try {
            if (priceMap.containsKey(lastUpdateKey)) {
                long lastUpdateAT = priceMap.get(lastUpdateKey).longValue();
                value = dateFormat.format(new Date(lastUpdateAT * MILLIS));
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            value = "NA";
        }
        return value;
    }
}
