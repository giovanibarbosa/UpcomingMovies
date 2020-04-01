package br.com.giovani.upcomingmovies.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
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
        .UpcomingMovieViewHolder.OnItemClickListener, SearchView.OnQueryTextListener {

    private static final int FIRST_PAGE = 1;
    private List<Movie> mEntireMovieList = new ArrayList<>();
    private List<Movie> mPartialMovieList = new ArrayList<>();
    private ConstraintLayout mProgressLayout;
    private UpcomingMoviesAdapter mUpcomingMoviesAdapter;
    private List<Genre> mGenres;
    private SearchView mSearchView;
    private boolean mIsReadyToSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        fetchGenres();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                if (mSearchView.getVisibility() == View.GONE) {
                    showSearchView();
                } else {
                    dismissSearchView();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(this);

        final RecyclerView recyclerView = findViewById(R.id.movies_recycler_view);
        mProgressLayout = findViewById(R.id.progress_bar_layout);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        mUpcomingMoviesAdapter = new UpcomingMoviesAdapter(mEntireMovieList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mUpcomingMoviesAdapter);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView
                .getContext(), layoutManager.getOrientation());
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
                            fetchUpcomingMovies(FIRST_PAGE);
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

    private void fetchUpcomingMovies(final int page) {
        showProgressBar();

        new UpcomingMoviesCallBack(page) {
            @Override
            public void onResponse(Call<UpcomingMoviesResponse> call,
                                   Response<UpcomingMoviesResponse> response) {

                if (response.isSuccessful()) {
                    final UpcomingMoviesResponse upcomingMoviesResponse = response.body();
                    if (upcomingMoviesResponse != null &&
                            page <= upcomingMoviesResponse.getTotalPages()) {

                        mEntireMovieList.addAll(upcomingMoviesResponse.getMovies());
                        setMovieGenres();

                        if (page == upcomingMoviesResponse.getTotalPages()) {
                            dismissProgressBar();
                            mUpcomingMoviesAdapter.updateMovies(mEntireMovieList);
                            mPartialMovieList = mEntireMovieList;
                            mIsReadyToSearch = true;
                        } else {
                            fetchUpcomingMovies(upcomingMoviesResponse.getPage() + 1);
                        }
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
        for (final Movie movie : mEntireMovieList) {
            movie.setGenres(mGenres);
        }
    }

    private void showProgressBar() {
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        mProgressLayout.setVisibility(View.INVISIBLE);
    }

    private void showSearchView() {
        if (mIsReadyToSearch) {
            mSearchView.setVisibility(View.VISIBLE);
        }
    }

    private void dismissSearchView() {
        mSearchView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position) {
        final Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Constants.MOVIE_KEY, mPartialMovieList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (!s.trim().isEmpty()) {
            searchMoviesByName(s);
        } else {
            mPartialMovieList = mEntireMovieList;
        }
        mUpcomingMoviesAdapter.updateMovies(mPartialMovieList);
        return false;
    }

    private void searchMoviesByName(String text) {
        mPartialMovieList = new ArrayList<>();
        for (final Movie movie : mEntireMovieList) {
            if (movie.getTitle().toLowerCase().trim().contains(text.toLowerCase())) {
                mPartialMovieList.add(movie);
            }
        }
    }
}
