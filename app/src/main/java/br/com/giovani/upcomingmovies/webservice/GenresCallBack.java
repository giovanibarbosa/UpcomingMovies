package br.com.giovani.upcomingmovies.webservice;

import br.com.giovani.upcomingmovies.model.GenresResponse;
import br.com.giovani.upcomingmovies.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class GenresCallBack implements Callback<GenresResponse> {
    public GenresCallBack() {
        enqueueGenresCallBack();
    }

    private void enqueueGenresCallBack() {
        final ApiConfig apiConfig = ApiConfig.getInstance();

        final MoviesApiService moviesApiService = apiConfig.getDefaultConfig()
                .create(MoviesApiService.class);

        final Call<GenresResponse> call = moviesApiService.fetchGenres(Constants.API_KEY);
        call.enqueue(this);
    }

    @Override
    public abstract void onResponse(Call<GenresResponse> call, Response<GenresResponse> response);

    @Override
    public abstract void onFailure(Call<GenresResponse> call, Throwable t);
}
