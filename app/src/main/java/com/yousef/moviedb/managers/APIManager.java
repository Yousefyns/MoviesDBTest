package com.yousef.moviedb.managers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yousef.moviedb.dataclasses.MoviesListingResponse;
import com.yousef.moviedb.interfaces.APIInterfaces;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {
    //API KEY
    static String API_KEY = "e5c9f968d7973d7718a85beff93f389f";

    //Base API URL
    static String BASE_URL = "https://api.themoviedb.org/3/";

    //Static API Params
    static String defaultSort = "popularity.desc";


    Gson gson;
    Retrofit retrofit;
    Context mContext;
    public APIManager(Context mContext){
        this.mContext = mContext;
        gson = new GsonBuilder()
                .setLenient()
                .create();

        // Creating an interceptor For error logging purposes
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Customizing Retrofit client settings
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(10, TimeUnit.SECONDS);
        client.readTimeout(10, TimeUnit.SECONDS);
        client.writeTimeout(10, TimeUnit.SECONDS);
        client.pingInterval(1, TimeUnit.SECONDS);
        client.addInterceptor(loggingInterceptor);

        client.build();

        //Creating a retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }


    public void getMovies(int page, final Callback<MoviesListingResponse> mCallback){
        APIInterfaces.ListMoviesApi listMoviesApi = retrofit.create(APIInterfaces.ListMoviesApi.class);

        Call<MoviesListingResponse> call = listMoviesApi.loadMovies(API_KEY,page,defaultSort);
        call.enqueue(new Callback<MoviesListingResponse>() {
            @Override
            public void onResponse(Call<MoviesListingResponse> call, Response<MoviesListingResponse> response) {
                mCallback.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<MoviesListingResponse> call, Throwable t) {
                mCallback.onFailure(call,t);
            }
        });
       // call.request();
    }


}
