package com.digital.coinlist.data.mapper;

import com.digital.coinlist.data.network.entity.CoinListItem;
import com.digital.coinlist.data.network.entity.PriceComparisonApiReq;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.digital.coinlist.domain.entity.SelectableCoinListItem;
import com.digital.coinlist.domain.entity.SelectableCurrencyListItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CoinMapper {

    @Inject
    public CoinMapper() {

    }

    private SelectableCoinListItem getSelectableCoinListItem(CoinListItem listItem) {
        return new SelectableCoinListItem(listItem.getSymbol(), listItem.getName(),
            listItem.getId());
    }

    public List<SelectableCoinListItem> getSelectableCoinList(List<CoinListItem> list) {
        List<SelectableCoinListItem> selectableList = new ArrayList<>();
        for (CoinListItem item : list) {
            selectableList.add(getSelectableCoinListItem(item));
        }
        return selectableList;
    }

    private SelectableCurrencyListItem getSelectableCurrencyListItem(String currency) {
        return new SelectableCurrencyListItem(currency);
    }

    public List<SelectableCurrencyListItem> getSelectableCurrencyList(List<String> list) {
        List<SelectableCurrencyListItem> selectableList = new ArrayList<>();
        for (String item : list) {
            selectableList.add(getSelectableCurrencyListItem(item));
        }
        return selectableList;
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
        if (!comparisonResp.containsKey(req.getCryptoId())) {
            return new PriceItem(
                req.getCryptoId(), req.getCurrencyId(),
                0, 0, 0, 0, 0);
        }

        comparisonMap = comparisonResp.get(req.getCryptoId());

        double price = 0, marketCap = 0, _24hrVol = 0, _24hrChange = 0;
        long lastUpdateAT = 0;

        final String marketCapKey = String.format("%s%s", req.getCurrencyId(), MARKET_CAP);
        final String volumeKey = String.format("%s%s", req.getCurrencyId(), _24HR_VOL);
        final String changeKey = String.format("%s%s", req.getCurrencyId(), _24HR_CHANGE);
        final String lastUpdateKey = String.format("%s%s", req.getCurrencyId(), LAST_UPDATED_AT);

        if (comparisonMap.containsKey(req.getCurrencyId())) {
            price = comparisonMap.get(req.getCurrencyId());
        }

        if (comparisonMap.containsKey(marketCapKey)) {
            marketCap = comparisonMap.get(marketCapKey);
        }
        if (comparisonMap.containsKey(volumeKey)) {
            _24hrVol = comparisonMap.get(volumeKey);
        }
        if (comparisonMap.containsKey(changeKey)) {
            _24hrChange = comparisonMap.get(changeKey);
        }
        if (comparisonMap.containsKey(lastUpdateKey)) {
            lastUpdateAT = comparisonMap.get(lastUpdateKey).longValue();
        }
        return new PriceItem(
            req.getCryptoId()
            , req.getCurrencyId()
            , price, marketCap
            , _24hrVol
            , _24hrChange
            , lastUpdateAT);
    }

    private static final String MARKET_CAP = "_market_cap";
    private static final String _24HR_VOL = "_24h_vol";
    private static final String _24HR_CHANGE = "_market_cap";
    private static final String LAST_UPDATED_AT = "_market_cap";
}
