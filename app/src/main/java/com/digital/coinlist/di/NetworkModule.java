package com.digital.coinlist.di;

import android.app.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(SingletonComponent.class)
@Module
public class NetworkModule {

    private static final long CACHE_SIZE = 10 * 1024 * 1024;
    private static final String BASE_URL = "https://api.coingecko.com/api/v3/";

    @Provides
    @Singleton
    public Cache provideHttpCache(Application application) {
        return new Cache(application.getCacheDir(), CACHE_SIZE);
    }

    @Provides
    @Singleton
    public Gson proivdeGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkhttpClient(Cache cache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().cache(cache);
        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
    }

}
