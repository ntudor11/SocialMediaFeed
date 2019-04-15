package com.mobile.Smf.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.mobile.Smf.R;

public abstract class VerticalStackedTwoFragmentActivity extends  AppCompatActivity{

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity_layout);

        FragmentManager fm = getSupportFragmentManager();
        Fragment upperFragment = fm.findFragmentById(R.id.upper_fragment_container);
        Fragment lowerFragment = fm.findFragmentById(R.id.lower_fragment_container) ;

        if (upperFragment == null){
            upperFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.upper_fragment_container, upperFragment)
                    .commit();
        }
        if (lowerFragment == null) {
            lowerFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.lower_fragment_container, lowerFragment)
                    .commit();
        }
    }
}

