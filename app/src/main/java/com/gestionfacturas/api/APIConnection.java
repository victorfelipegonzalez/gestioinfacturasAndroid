package com.gestionfacturas.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIConnection {

    //URL Emulador
    //private static final String BASE_URL = "http://10.0.2.2:9090";
    //private static final String BASE_URL = "http://46.27.139.232:9090";
    private static final String BASE_URL = "http://192.168.0.19:9090";
    private static Retrofit retrofit;

    public APIConnection() {
    }

    // Método para establacer la conexion
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
