package com.semicolon.garage.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.semicolon.garage.services.Services;
import com.semicolon.garage.tags.Tags;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Retrofit retrofit = null;

    private static Retrofit getRetrofit()
    {
        if (retrofit==null)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1,TimeUnit.MINUTES)
                    .readTimeout(1,TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(Tags.base_url)
                    .build();

        }
        return retrofit;
    }

    public static Services getService()
    {
        Retrofit retrofit = getRetrofit();
        return retrofit.create(Services.class);

    }

}
