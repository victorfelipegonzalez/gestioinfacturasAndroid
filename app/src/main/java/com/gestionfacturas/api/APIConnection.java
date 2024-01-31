package com.gestionfacturas.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIConnection {
    // URL Toledo
    //private static final String BASE_URL = "http://47.61.178.189:9090";

    //URL Emulador
    private static final String BASE_URL = "http://10.0.2.2:9090";

    //URL ALBACETE PARCELA
    //private static final String BASE_URL = "http://192.168.3.104:9090";

    //URL ALBACETE
    //private static final String BASE_URL = "http://192.168.1.40:9090";

    private static Retrofit retrofit;

    public APIConnection() {
    }

    public static synchronized Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
