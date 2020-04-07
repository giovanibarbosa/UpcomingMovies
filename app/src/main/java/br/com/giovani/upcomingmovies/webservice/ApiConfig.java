package br.com.giovani.upcomingmovies.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import br.com.giovani.upcomingmovies.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ApiConfig {
    private static ApiConfig instance;
    private static final int TIMEOUT_IN_SECONDS = 30;

    private ApiConfig() {
    }

    static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    private OkHttpClient getDefaultHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    Retrofit getDefaultConfig() {
        final Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .client(getDefaultHttpClient())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
