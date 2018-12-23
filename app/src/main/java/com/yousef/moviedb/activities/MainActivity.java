package com.yousef.moviedb.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yousef.moviedb.fragments.MovieListingFragment;
import com.yousef.moviedb.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new MovieListingFragment(),false);
    }

    public void replaceFragment(Fragment newFragment, boolean addToStack){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        if(addToStack) {
            transaction.add(R.id.main_container, newFragment);
            transaction.addToBackStack(MovieListingFragment.class.toString());
        } else{
            transaction.replace(R.id.main_container, newFragment);
        }
        transaction.commit();
    }

}
