package br.com.giovani.upcomingmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.com.giovani.upcomingmovies.R;
import br.com.giovani.upcomingmovies.model.Movie;
import br.com.giovani.upcomingmovies.utils.Constants;
import br.com.giovani.upcomingmovies.utils.ImageUrlBuilder;

public class UpcomingMoviesAdapter extends RecyclerView.Adapter<UpcomingMoviesAdapter
        .UpcomingMovieViewHolder>{

    private List<Movie> mMovies;
    private UpcomingMovieViewHolder.OnItemClickListener mOnclickListenerItem;

    public UpcomingMoviesAdapter(List<Movie> movies, UpcomingMovieViewHolder
            .OnItemClickListener onItemClickListener) {
        mMovies = movies;
        mOnclickListenerItem = onItemClickListener;
    }

    @NonNull
    @Override
    public UpcomingMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upcoming_movie_item, parent, false);
        return new UpcomingMovieViewHolder(view, mOnclickListenerItem);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingMovieViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);

        Glide.with(holder.itemView)
                .load(ImageUrlBuilder.getInstance()
                        .build(Constants.IMAGE_BASE_URL,
                                ImageUrlBuilder.ORIGINAL,
                                movie.getBackdropPath()))
                .placeholder(R.mipmap.broken_image)
                .into(holder.posterImageView);
        holder.nameTextView.setText(movie.getTitle());
        holder.genreTextView.setText(movie.formatGenres());
        holder.releaseDateTextView.setText(movie.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void updateMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public static class UpcomingMovieViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        ImageView posterImageView;
        TextView nameTextView;
        TextView genreTextView;
        TextView releaseDateTextView;
        OnItemClickListener mOnItemClickListener;

        UpcomingMovieViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.poster_image_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            genreTextView = itemView.findViewById(R.id.genre_text_view);
            releaseDateTextView = itemView.findViewById(R.id.release_date_text_view);
            mOnItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }
}
