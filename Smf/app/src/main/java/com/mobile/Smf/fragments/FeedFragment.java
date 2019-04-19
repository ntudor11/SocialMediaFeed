package com.mobile.Smf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobile.Smf.R;
import com.mobile.Smf.model.Post;

public class FeedFragment extends Fragment {

    private RecyclerView feedRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View feedView = inflater.inflate(R.layout.fragment_feed,container,false);

        feedRecyclerView = (RecyclerView) feedView.findViewById(R.id.feed_recyclerview);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        startRecyclerView();

        return feedView;
    }

    private void startRecyclerView(){

    }

    private class PostHolder extends RecyclerView.ViewHolder {

        private Post post;

        public PostHolder(LayoutInflater inflater, ViewGroup parent){
            super();
        }

    }

}
