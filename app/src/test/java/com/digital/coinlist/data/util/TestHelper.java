package com.digital.coinlist.data.util;

import com.digital.coinlist.data.database.entity.CoinDbEntity;
import com.digital.coinlist.data.database.entity.CurrencyDbEntity;
import com.digital.coinlist.data.network.entity.CoinListItem;
import com.digital.coinlist.domain.entity.CoinItem;
import com.digital.coinlist.domain.entity.CurrencyItem;
import com.digital.coinlist.domain.entity.PriceItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestHelper {

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


    private final static Type COIN_LIST_TYPE_TOKEN = new TypeToken<List<CoinListItem>>() {
    }.getType();

    public static List<CoinListItem> getCoinListItem() {
        return GSON.fromJson(COIN_ITEM_LIST, COIN_LIST_TYPE_TOKEN);
    }

    private final static Type PRICE_MAP_TYPE_TOKEN = new TypeToken<Map<String, Map<String, Double>>>() {
    }.getType();

    public static Map<String, Map<String, Double>> getPriceMap() {
        return GSON.fromJson(COMPARISON_STRING, PRICE_MAP_TYPE_TOKEN);
    }

    public static List<CoinItem> getCoinItems() {
        List<CoinItem> items = new ArrayList<>();
        items.add(new CoinItem("bitcny", "bitCNY", "bitcny"));
        items.add(new CoinItem("bff", "Bitcoffeen", "bitcoffeen"));
        items.add(new CoinItem("b2g", "Bitcoiin", "bitcoiin"));
        return items;
    }

    public static List<CoinItem> getCoinItemsFromDb() {
        List<CoinItem> items = new ArrayList<>();
        items.add(new CoinItem("bitcny", "bitCNY", "bitcny"));
        items.add(new CoinItem("bff", "Bitcoffeen", "bitcoffeen"));
        items.add(new CoinItem("b2g", "Bitcoiin", "bitcoiin"));
        return items;
    }

    public static List<CoinDbEntity> getCoinDbEntities() {
        List<CoinDbEntity> items = new ArrayList<>();
        items.add(new CoinDbEntity("bitcny", "bitCNY", "bitcny"));
        items.add(new CoinDbEntity("bff", "Bitcoffeen", "bitcoffeen"));
        items.add(new CoinDbEntity("b2g", "Bitcoiin", "bitcoiin"));
        return items;
    }

    public static List<String> getCurrencyApiResp() {
        List<String> strings = new ArrayList<>();
        strings.add("inr");
        strings.add("aed");
        strings.add("usd");
        return strings;
    }

    public static List<CurrencyItem> getCurrencyItemList() {
        List<CurrencyItem> strings = new ArrayList<>();
        strings.add(new CurrencyItem("inr"));
        strings.add(new CurrencyItem("aed"));
        strings.add(new CurrencyItem("usd"));
        return strings;
    }

    public static List<CurrencyDbEntity> getCurrencyDBItemList() {
        List<CurrencyDbEntity> strings = new ArrayList<>();
        strings.add(new CurrencyDbEntity("inr"));
        strings.add(new CurrencyDbEntity("aed"));
        strings.add(new CurrencyDbEntity("usd"));
        return strings;
    }

    public static PriceItem getPriceItem() {
        return new PriceItem("3x-long-ethereum-token", "btc",
            "0.04366834", "1821.5931286865932", "185.65511077731148", "1.6481292040465076",
            "dummy date");
    }

}
