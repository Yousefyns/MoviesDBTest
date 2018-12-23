package com.yousef.moviedb.interfaces;

import android.view.View;

public interface MoviesRecyclerCallback {
    void onLoadMoreThresholdReached();
    void onItemClicked(View v);
}
