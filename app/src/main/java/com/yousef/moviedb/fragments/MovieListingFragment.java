package com.yousef.moviedb.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yousef.moviedb.activities.MainActivity;
import com.yousef.moviedb.adapters.MoviesRecyclerAdapter;
import com.yousef.moviedb.dataclasses.Movie;
import com.yousef.moviedb.dataclasses.MoviesListingResponse;
import com.yousef.moviedb.interfaces.MoviesRecyclerCallback;
import com.yousef.moviedb.managers.APIManager;
import com.yousef.moviedb.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListingFragment extends BaseFragment implements Callback<MoviesListingResponse> , MoviesRecyclerCallback{
    APIManager mApiManager;
    RecyclerView mMoviesRecycler;
    MoviesRecyclerAdapter mMoviesAdapter;
    SwipeRefreshLayout mRefreshLayout;
    View mLoadingView;
    int currentListingPage = 1;
    int totalPageCount = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mApiManager = new APIManager(mContext);
        View mView  = inflater.inflate(R.layout.fragment_movie_listing,container,false);
        mRefreshLayout = mView.findViewById(R.id.sr_movies_refresh_layout);
        mLoadingView = mView.findViewById(R.id.fl_loading_view);
        mMoviesRecycler = mView.findViewById(R.id.rv_movies);
        mMoviesAdapter = new MoviesRecyclerAdapter(mContext,this);
        mMoviesRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mMoviesRecycler.setAdapter(mMoviesAdapter);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
                mLoadingView.setVisibility(View.VISIBLE);
                loadMovies(true);
            }
        });
        loadMovies(true);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadMovies(boolean fullRefresh) {
        if(fullRefresh){
            currentListingPage = 1;
        }
        mApiManager.getMovies(currentListingPage,this);
    }


    @Override
    public void onResponse(Call<MoviesListingResponse> call, Response<MoviesListingResponse> response) {
        MoviesListingResponse mResponse = response.body();
        currentListingPage++;
        if (mResponse != null){
            totalPageCount = mResponse.getTotalPages();
            if (currentListingPage == 1) {
                mMoviesAdapter.refresh((ArrayList<Movie>) mResponse.getResults());
            } else {
                mMoviesAdapter.update((ArrayList<Movie>) mResponse.getResults());
            }
            mLoadingView.setVisibility(View.GONE);
        } else {
            onFailure(call,new Throwable("Null Movies API Response"));
        }
    }

    @Override
    public void onFailure(Call<MoviesListingResponse> call, Throwable t) {
        mLoadingView.setVisibility(View.GONE);
        Toast.makeText(mContext, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMoreThresholdReached() {
        loadMovies(false);
    }

    @Override
    public void onItemClicked(View view) {
        MoviesRecyclerAdapter.MovieViewHolder mHolder = (MoviesRecyclerAdapter.MovieViewHolder) mMoviesRecycler.findContainingViewHolder(view);
        int position = mHolder.getAdapterPosition();
        Movie mMovie = mMoviesAdapter.getMovie(position);
        ( (MainActivity) getActivity()).replaceFragment(MovieDetailsFragment.newInstance(mMovie),true);
    }


}
