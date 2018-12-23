package com.yousef.moviedb.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yousef.moviedb.R;
import com.yousef.moviedb.adapters.MoviesRecyclerAdapter;
import com.yousef.moviedb.dataclasses.Movie;
import com.yousef.moviedb.dataclasses.MoviesListingResponse;
import com.yousef.moviedb.interfaces.MoviesRecyclerCallback;
import com.yousef.moviedb.managers.APIManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsFragment extends BaseFragment{

    final static String MOVIE_OBJECT_KEY = "MOVIE_KEY";
    Movie mCurrentlySelectedMovie = null;
    TextView mMovieTitle;
    TextView mMovieReleaseDate;
    TextView mMovieOverview;
    ImageView mMoviePoster;
    View mLoadingView;
    public static MovieDetailsFragment newInstance(Movie mMovieObject)
    {
        MovieDetailsFragment mFragment = new MovieDetailsFragment();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(MOVIE_OBJECT_KEY,mMovieObject);
        mFragment.setArguments(mBundle);
        return mFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            mCurrentlySelectedMovie = (Movie) getArguments().getSerializable(MOVIE_OBJECT_KEY);
            if(mCurrentlySelectedMovie == null){
                getActivity().onBackPressed();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView  = inflater.inflate(R.layout.fragment_movie_details,container,false);
        mLoadingView = mView.findViewById(R.id.fl_loading_view);
        mLoadingView.setVisibility(View.VISIBLE);
        mMovieTitle = mView.findViewById(R.id.tv_movie_title);
        mMovieReleaseDate = mView.findViewById(R.id.tv_movie_release_date);
        mMovieOverview = mView.findViewById(R.id.tv_movie_overview);
        mMoviePoster = mView.findViewById(R.id.iv_movie_poster);
        populateViews();
        mLoadingView.setVisibility(View.GONE);
        return mView;
    }

    private void populateViews(){
        mMovieTitle.setText(mCurrentlySelectedMovie.getTitle());
        mMovieReleaseDate.setText(mCurrentlySelectedMovie.getReleaseDate());
        mMovieOverview.setText(mCurrentlySelectedMovie.getOverview());
        Glide.with(mContext)
                .load(MoviesRecyclerAdapter.images_base_url + mCurrentlySelectedMovie.getPosterPath())
                .into(mMoviePoster);
    }




}
