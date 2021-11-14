# Coinlist
Compare crypto currencies with other currencies.



# Setup and Run 
Android Gradle plugin 7.0.2 needs Java 11 to run

https://stackoverflow.com/questions/66980512/android-gradle-plugin-requires-java-11-to-run-you-are-currently-using-java-1-8

no other setup required.

min supported sdk version is 21
# Major Dependencies 

- Gson
- Retrofit
- RxJava3
- Dagger Hilt
- JetPack Navigation
- Room
- Api from [Coingecko](https://www.coingecko.com/api/documentations/v3)


# Code Structure

I have followed clean architecture with MVVM.
This  project has data, domain and ui layers.

## Base Structure
<img src="https://github.com/r4jiv007/coinlist/blob/main/imgs/base.png" width=20% height=20%>

<table>
  <tr>
    <td>Data Layer</td>
     <td>Domain Layer</td>
     <td>DI Layer</td>
  </tr>
  <tr>
    <td><img src="https://github.com/r4jiv007/coinlist/blob/main/imgs/data.png" width=50% height=50%></td>
    <td><img src="https://github.com/r4jiv007/coinlist/blob/main/imgs/domain.png" width=50% height=50%></td>
    <td><img src="https://github.com/r4jiv007/coinlist/blob/main/imgs/di.png" width=50% height=50%></td>
  </tr>
 </table>
 <table>
  <tr>
    <td>UI Layer</td>
  </tr>
  <tr>
    <td><img src="https://github.com/r4jiv007/coinlist/blob/main/imgs/ui.png" width=50% height=50%></td>
  </tr>
 </table>


# Functionality

## App usage
1. `CoinListFragment` is used to provide both crytpo_coin and currency selection.
2. Once both the selection has been made, the price comparison api will be called, also periodic update will start which will update the values in 10 sec Interval

## App internals
- Repo has two branches [feature/sharedViewModel](https://github.com/r4jiv007/coinlist/tree/feature/sharedViewModel) and [main](https://github.com/r4jiv007/coinlist/tree/main)
- `CoinMapper` contains all the logic for converting api response and database entities to respective domain entities.

### `main`
- `main` branch has more detailed implementation, room is used to cache the data locally.
- Every App launch will update the local database, reason there is no way to know if the new data is available.
- Subsequent call to get data will first check the results in DB if its present data from DB will be used or else api call will be made.
- the method to fetch data has forceRemote flag to force api call which is set to true when App is started.
- following code is form `CoinRepoImpl` class which handes the communication to database or api service.
```java
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
```

- `CoinListFragment` returns the result via [BackStackEntry and SaveStateHandle](https://developer.android.com/guide/navigation/navigation-programmatic#returning_a_result)
 to Previous fragment.
- Any request is triggered from UI --> viewmodel --> usecase --> repo --> DB/API service, and reponse is returned in reverese direction.
- Search feature is also provided via Room query 

for crytpo coins
```java
  @Query("SELECT * FROM coin_table WHERE name LIKE '%' || :search || '%'")
  Single<List<CoinDbEntity>> getAllCoin(String search);
```
for currencies
```java
    @Query("SELECT * FROM currency_table WHERE name LIKE '%' || :search || '%'")
    Single<List<CurrencyDbEntity>> getAllCurrency(String search);
```

 
### `feature/sharedViewModel`
- `feature/sharedViewModel` shows how to use [NavGraph scoped viewmodel to share data between destinations](https://developer.android.com/guide/navigation/navigation-programmatic#share_ui-related_data_between_destinations_with_viewmodel)
- in `feature/sharedViewModel` branch fragments `CoinListFragment` and `PriceFragment` shares `CoinViewModel`.
- I have used a custom MediatorLiveData to listen to values for crytp_coin and currency item, once both the values are recived the comparison api will be called.
- this branch has no database implementation and api is called everytime the list of crytpo_coin  or currencies are required.
- in-memory filteration is provided using `Filterable` interface provided by andorid and its implmented inside `CoinListAdapter`

# App ScreenShot


<table>
  <tr>
    <td>Initial-Screen</td>
     <td>Select Crypto Currency</td>
  </tr>
  <tr>
    <td><img src="https://github.com/r4jiv007/coinlist/blob/main/imgs/1.jpeg" width=50% height=50%></td>
    <td><img src="https://github.com/r4jiv007/coinlist/blob/main/imgs/2.jpeg" width=50% height=50%></td>
  </tr>
 </table>
 
<table>
  <tr>
    <td>Select VS_Currency to compare</td>
     <td>Check Comparison Result</td>
  </tr>
  <tr>
    <td><img src="https://github.com/r4jiv007/coinlist/blob/main/imgs/4.jpeg" width=50% height=50%></td>
    <td><img src="https://github.com/r4jiv007/coinlist/blob/main/imgs/5.jpeg" width=50% height=50%></td>
  </tr>
 </table>


