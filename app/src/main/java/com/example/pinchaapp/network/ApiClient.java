package com.example.pinchaapp.network;

import com.example.pinchaapp.session.SessionManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://api.nodesv.com/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {

            // Cliente HTTP que agrega el token JWT en cada petición
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        String token = SessionManager.getToken();

                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .addHeader("Content-Type", "application/json")
                                .build();

                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}