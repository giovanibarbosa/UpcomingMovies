package br.com.giovani.upcomingmovies.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import br.com.giovani.upcomingmovies.R;
import br.com.giovani.upcomingmovies.model.Movie;
import br.com.giovani.upcomingmovies.utils.Constants;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class MovieDetailActivity extends AppCompatActivity implements RequestListener<Drawable> {
    private Movie mMovie;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        final Intent intent = getIntent();
        mMovie = intent.getParcelableExtra(Constants.MOVIE_KEY);

        initToolBar();
        initViews();
    }

    private void initToolBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mMovie.getTitle());
        }
    }

    private void initViews() {
        final ImageView poster = findViewById(R.id.detail_poster_image_view);
        final TextView name = findViewById(R.id.detail_name_text_view);
        final TextView genres = findViewById(R.id.detail_genre_text_view);
        final TextView overview = findViewById(R.id.detail_overview_text_view);
        final TextView releaseDate = findViewById(R.id.detail_release_date_text_view);
        mProgressBar = findViewById(R.id.detail_poster_progress_bar);

        showProgressBar();

        if (mMovie != null) {
            Glide
                    .with(this)
                    .load(Constants.IMAGE_BASE_URL + mMovie.getPosterPath())//TODO change the base image url
                    .addListener(this)
                    .placeholder(android.R.color.darker_gray)
                    .into(poster);

            name.setText(mMovie.getTitle());

            genres.setText(String.format(getString(R.string.detail_genre_text), mMovie
                    .formatGenres()));

            overview.setText(mMovie.getOverview());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                overview.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }

            releaseDate.setText(String.format(getString(R.string.detail_release_date_text), mMovie
                    .getReleaseDate()));
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_movie_displayed),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                boolean isFirstResource) {
        dismissProgressBar();
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                   DataSource dataSource, boolean isFirstResource) {
        dismissProgressBar();
        return false;
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
