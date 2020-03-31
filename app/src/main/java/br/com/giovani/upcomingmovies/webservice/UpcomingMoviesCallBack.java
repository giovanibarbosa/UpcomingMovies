package br.com.giovani.upcomingmovies.webservice;

import br.com.giovani.upcomingmovies.model.UpcomingMoviesResponse;
import br.com.giovani.upcomingmovies.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class UpcomingMoviesCallBack implements Callback<UpcomingMoviesResponse> {

    protected UpcomingMoviesCallBack(int page) {
        enqueueUpcomingMoviesCallBack(page);
    }

    private void enqueueUpcomingMoviesCallBack(int page) {
        final ApiConfig apiConfig = ApiConfig.getInstance();

        final MoviesApiService moviesApiService = apiConfig.getDefaultConfig()
                .create(MoviesApiService.class);

        final Call<UpcomingMoviesResponse> call = moviesApiService
                .fetchUpcomingMovies(Constants.API_KEY, page);
        call.enqueue(this);
    }

    @Override
    public abstract void onResponse(Call<UpcomingMoviesResponse> call,
                                    Response<UpcomingMoviesResponse> response);

    @Override
    public abstract void onFailure(Call<UpcomingMoviesResponse> call, Throwable t);
}
