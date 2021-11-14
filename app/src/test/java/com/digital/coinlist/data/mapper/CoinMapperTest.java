package com.digital.coinlist.data.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.digital.coinlist.data.network.entity.CoinListItem;
import com.digital.coinlist.data.util.TestHelper;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;

public class CoinMapperTest {


    private CoinMapper coinMapper;

    @BeforeClass
    public void setUp() {
        coinMapper = new CoinMapper();
    }

    @Test
    public void testGetCoinItemListFromApi() {
        List<CoinListItem> list = TestHelper.getCoinListItem();
        List<CoinItem> coinList = coinMapper.getCoinItemListFromApi(list);

        assertEquals(list.size(), coinList.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i).getId(), coinList.get(i).getId());
            assertEquals(list.get(i).getName(), coinList.get(i).getName());
            assertEquals(list.get(i).getSymbol(), coinList.get(i).getSymbol());
        }
    }

    @Test
    public void testGetPriceItem() {
        Map<String, Map<String, Double>> priceItem = TestHelper.getPriceMap();
        PriceComparisonReq req = new PriceComparisonReq("3x-long-ethereum-token", "btc");
        PriceItem convertedItem = coinMapper.getPriceItem(req, priceItem);
        assertNotNull(convertedItem);
        assertEquals(convertedItem.getPrice(), String.valueOf(0.04366834) + " btc");
        assertEquals(convertedItem.getMarketCap(), String.valueOf(1821.5931286865932));
        assertEquals(convertedItem.get_24hVol(), String.valueOf(185.65511077731148));
        assertEquals(convertedItem.get_24hChange(), String.valueOf(1.6481292040465076));
    }
}
