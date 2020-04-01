package br.com.giovani.upcomingmovies.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.giovani.upcomingmovies.R;
import br.com.giovani.upcomingmovies.adapter.UpcomingMoviesAdapter;
import br.com.giovani.upcomingmovies.model.Genre;
import br.com.giovani.upcomingmovies.model.GenresResponse;
import br.com.giovani.upcomingmovies.model.Movie;
import br.com.giovani.upcomingmovies.model.UpcomingMoviesResponse;
import br.com.giovani.upcomingmovies.utils.Constants;
import br.com.giovani.upcomingmovies.webservice.GenresCallBack;
import br.com.giovani.upcomingmovies.webservice.UpcomingMoviesCallBack;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UpcomingMoviesAdapter
        .UpcomingMovieViewHolder.OnItemClickListener {

    private List<Movie> mMovies = new ArrayList<>();
    private ConstraintLayout mProgressLayout;
    private LinearLayoutManager mLayoutManager;
    private UpcomingMoviesAdapter mUpcomingMoviesAdapter;
    private int mCurrentPage = 0;
    private List<Genre> mGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        fetchGenres();
    }

    private void initViews() {
        final RecyclerView recyclerView = findViewById(R.id.movies_recycler_view);
        mProgressLayout = findViewById(R.id.progress_bar_layout);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);

        mUpcomingMoviesAdapter = new UpcomingMoviesAdapter(mMovies, this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mUpcomingMoviesAdapter);
        recyclerView.addOnScrollListener(getOnScrollListener());

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView
                .getContext(), mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void fetchGenres() {
        showProgressBar();

        if (mGenres == null) {
            new GenresCallBack() {
                @Override
                public void onResponse(Call<GenresResponse> call,
                                       Response<GenresResponse> response) {
                    dismissProgressBar();
                    if (response.isSuccessful()) {
                        final GenresResponse genresResponse = response.body();
                        if (genresResponse != null) {
                            mGenres = genresResponse.getGenres();
                            fetchUpcomingMovies();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GenresResponse> call, Throwable t) {
                    dismissProgressBar();
                    Toast.makeText(MainActivity.this, R.string.on_failure_message, Toast
                            .LENGTH_LONG).show();
                }
            };
        }
    }

    private void fetchUpcomingMovies() {
        showProgressBar();
        mCurrentPage++;

        new UpcomingMoviesCallBack(mCurrentPage) {
            @Override
            public void onResponse(Call<UpcomingMoviesResponse> call,
                                   Response<UpcomingMoviesResponse> response) {
                dismissProgressBar();

                if (response.isSuccessful()) {
                    final UpcomingMoviesResponse upcomingMoviesResponse = response.body();
                    if (upcomingMoviesResponse != null) {
                        mMovies.addAll(upcomingMoviesResponse.getMovies());
                        setMovieGenres();
                        mUpcomingMoviesAdapter.updateMovies(mMovies);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpcomingMoviesResponse> call, Throwable t) {
                dismissProgressBar();
                Toast.makeText(MainActivity.this, R.string.on_failure_message, Toast
                        .LENGTH_LONG).show();
            }
        };
    }

    private void setMovieGenres() {
        for (final Movie movie : mMovies) {
            movie.setGenres(mGenres);
        }
    }

    private RecyclerView.OnScrollListener getOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int lastItemPosition = mLayoutManager.findLastVisibleItemPosition();

                if (lastItemPosition >= mMovies.size() - 1 && dy > 0) {
                    fetchUpcomingMovies();
                }
            }
        };
    }

    private void showProgressBar() {
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        mProgressLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        final Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Constants.MOVIE_KEY, mMovies.get(position));
        startActivity(intent);
    }
}
