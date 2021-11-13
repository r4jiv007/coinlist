package com.digital.coinlist.data.mapper;

import com.digital.coinlist.data.network.entity.CoinListItem;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.PriceComparisonReq;
import com.digital.coinlist.domain.entity.PriceItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.BeforeClass;

public class CoinMapperTest extends TestCase {

    public static final Gson GSON = new Gson();
    public static final String COIN_ITEM_LIST = "[{\n"
        + "      \"id\":\"bitcny\",\n"
        + "      \"symbol\":\"bitcny\",\n"
        + "      \"name\":\"bitCNY\"\n"
        + "   },\n"
        + "   {\n"
        + "      \"id\":\"bitcoffeen\",\n"
        + "      \"symbol\":\"bff\",\n"
        + "      \"name\":\"Bitcoffeen\"\n"
        + "   },\n"
        + "   {\n"
        + "      \"id\":\"bitcoiin\",\n"
        + "      \"symbol\":\"b2g\",\n"
        + "      \"name\":\"Bitcoiin\"\n"
        + "   }]";

    private final static String COMPARISON_STRING =
        "{ \"3x-long-ethereum-token\": { \"btc\": 0.04366834, \"btc_market_cap\":\n"
            + "      1821.5931286865932, \"btc_24h_vol\": 185.65511077731148, \"btc_24h_change\": 1.6481292040465076,\n"
            + "      \"last_updated_at\": 1636636961 } }";

    private CoinMapper coinMapper;

    @BeforeClass
    public void setUp() {
        coinMapper = new CoinMapper();
    }

    public void testGetCoinItemListFromApi() {
        Type typeToken = new TypeToken<List<CoinListItem>>() {
        }.getType();
        List<CoinListItem> list = GSON.fromJson(COIN_ITEM_LIST, typeToken);

        List<CoinItem> coinList = coinMapper.getCoinItemListFromApi(list);

        assertEquals(list.size(), coinList.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i).getId(), coinList.get(i).getId());
            assertEquals(list.get(i).getName(), coinList.get(i).getName());
            assertEquals(list.get(i).getSymbol(), coinList.get(i).getSymbol());
        }
    }

    public void testGetPriceItem() {
        Type typeToken = new TypeToken<Map<String, Map<String, Double>>>() {
        }.getType();
        Map<String, Map<String, Double>> priceItem = GSON.fromJson(COMPARISON_STRING, typeToken);
        PriceComparisonReq req = new PriceComparisonReq("3x-long-ethereum-token", "btc");
        PriceItem convertedItem = coinMapper.getPriceItem(req, priceItem);
        assertNotNull(convertedItem);
        assertEquals(convertedItem.getPrice(), String.valueOf(0.04366834)+" btc");
        assertEquals(convertedItem.getMarketCap(), String.valueOf(1821.5931286865932));
        assertEquals(convertedItem.get_24hVol(), String.valueOf(185.65511077731148));
        assertEquals(convertedItem.get_24hChange(), String.valueOf(1.6481292040465076));
    }
}