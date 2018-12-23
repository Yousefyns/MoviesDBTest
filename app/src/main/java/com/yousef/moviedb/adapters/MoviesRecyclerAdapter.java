package com.yousef.moviedb.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yousef.moviedb.R;
import com.yousef.moviedb.dataclasses.Movie;
import com.yousef.moviedb.interfaces.MoviesRecyclerCallback;

import java.util.ArrayList;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> implements View.OnClickListener {
    public final static String images_base_url = "https://image.tmdb.org/t/p/w200/";

    Context mContext;
    MoviesRecyclerCallback mCallback;
    int LOAD_MORE_THREASHOLD = 5;
    ArrayList<Movie> dataSet = new ArrayList<>();


    public MoviesRecyclerAdapter(Context mContext, MoviesRecyclerCallback mCallback){
        this.mContext = mContext;
        this.mCallback = mCallback;

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_layout_movie,viewGroup,false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie mMovie = dataSet.get(i);
        movieViewHolder.mMovieTitle.setText(mMovie.getTitle());
        movieViewHolder.mMovieReleaseDate.setText(mMovie.getReleaseDate());
        movieViewHolder.mMovieOverview.setText(mMovie.getOverview());

        Glide.with(mContext)
                .load(images_base_url + mMovie.getPosterPath())
                .into(movieViewHolder.mMoviePoster);

        movieViewHolder.itemView.setOnClickListener(this);

        if(i >= (dataSet.size() - LOAD_MORE_THREASHOLD)){
            mCallback.onLoadMoreThresholdReached();
        }
    }

    public void update(ArrayList<Movie> newData){
        int previousDataSize = dataSet.size();
        this.dataSet.addAll(newData);

        notifyItemRangeChanged(previousDataSize,dataSet.size() - previousDataSize);
    }

    public void refresh(ArrayList<Movie> newData){
        this.dataSet = newData;
        if(dataSet.get(0) != null){
            dataSet.add(0,null);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onClick(View v) {
        mCallback.onItemClicked(v);
    }

    public Movie getMovie(int position) {
        return dataSet.get(position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView mMovieTitle;
        TextView mMovieReleaseDate;
        TextView mMovieOverview;
        ImageView mMoviePoster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            mMovieReleaseDate = itemView.findViewById(R.id.tv_movie_release_date);
            mMovieOverview = itemView.findViewById(R.id.tv_movie_overview);
            mMoviePoster = itemView.findViewById(R.id.iv_movie_poster);

        }
    }
}
