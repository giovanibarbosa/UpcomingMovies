package br.com.giovani.upcomingmovies.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.giovani.upcomingmovies.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ApiConfig {
    private static ApiConfig instance;

    private ApiConfig() {
    }

    static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    Retrofit getDefaultConfig() {
        final Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
