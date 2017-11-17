package com.mycillin.partner.util;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient extends Application {
    private final static OkHttpClient okHttpClientHeader = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("Authorization", DataHelper.token).build();
                    return chain.proceed(request);
                }
            })
            .build();
    private final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    private static Retrofit retrofit = null;

    public static PartnerAPI getPartnerRestInterfaceNoToken() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Configs.URL_REST_CLIENT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }

        return retrofit.create(PartnerAPI.class);
    }

    public static PartnerAPI getPartnerRestInterfaceToken() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Configs.URL_REST_CLIENT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClientHeader)
                    .build();
        }
        return retrofit.create(PartnerAPI.class);
    }
}
