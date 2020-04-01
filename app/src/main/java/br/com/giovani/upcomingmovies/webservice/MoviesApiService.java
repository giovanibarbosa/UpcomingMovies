package br.com.giovani.upcomingmovies.webservice;

import br.com.giovani.upcomingmovies.model.GenresResponse;
import br.com.giovani.upcomingmovies.model.UpcomingMoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesApiService {
    @GET("movie/upcoming")
    Call<UpcomingMoviesResponse> fetchUpcomingMovies(@Query("api_key") String apiKey,
                                                     @Query("page") int page);

    @GET("genre/movie/list")
    Call<GenresResponse> fetchGenres(@Query("api_key") String apiKey);
}
