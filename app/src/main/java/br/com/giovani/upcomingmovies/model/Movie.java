package br.com.giovani.upcomingmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {
    private List<Genre> genres;
    private static final String GENRE_SEPARATOR = ", ";

    @Expose
    private double popularity;

    @Expose
    @SerializedName("vote_count")
    private int voteCount;

    @Expose
    private boolean video;

    @Expose
    @SerializedName("poster_path")
    private String posterPath;

    @Expose
    private long id;

    @Expose
    private boolean adult;

    @Expose
    @SerializedName("backdrop_path")
    private String backdropPath;

    @Expose
    @SerializedName("original_language")
    private String originalLanguage;

    @Expose
    @SerializedName("original_title")
    private String originalTitle;

    @Expose
    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @Expose
    private String title;

    @Expose
    @SerializedName("vote_average")
    private double voteAverage;

    @Expose
    private String overview;

    @Expose
    @SerializedName("release_date")
    private String releaseDate;

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = new ArrayList<>();

        for (final Genre genre : genres) {
            for (final int id : getGenreIds()) {
                if (genre.getId() == id) {
                    this.genres.add(genre);
                }
            }
        }
    }

    public String formatGenres() {
        final StringBuilder builder = new StringBuilder();
        if (genres != null && !genres.isEmpty()) {
            for (int i = 0; i < genres.size(); i++) {
                if (i == 0) {
                    builder.append(genres.get(i).getName());
                } else {
                    builder.append(GENRE_SEPARATOR).append(genres.get(i).getName());
                }
            }
        }
        return builder.toString();
    }


    protected Movie(Parcel in) {
        popularity = in.readDouble();
        voteCount = in.readInt();
        video = in.readByte() != 0;
        posterPath = in.readString();
        id = in.readLong();
        adult = in.readByte() != 0;
        backdropPath = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        releaseDate = in.readString();
        genres = in.createTypedArrayList(Genre.CREATOR);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(popularity);
        parcel.writeInt(voteCount);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeString(posterPath);
        parcel.writeLong(id);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(backdropPath);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeString(title);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeTypedList(genres);
    }
}
